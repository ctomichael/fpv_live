package dji.thirdparty.io.reactivex.subjects;

import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.observers.DeferredScalarDisposable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public final class AsyncSubject<T> extends Subject<T> {
    static final AsyncDisposable[] EMPTY = new AsyncDisposable[0];
    static final AsyncDisposable[] TERMINATED = new AsyncDisposable[0];
    Throwable error;
    final AtomicReference<AsyncDisposable<T>[]> subscribers = new AtomicReference<>(EMPTY);
    T value;

    public static <T> AsyncSubject<T> create() {
        return new AsyncSubject<>();
    }

    AsyncSubject() {
    }

    public void onSubscribe(Disposable s) {
        if (this.subscribers.get() == TERMINATED) {
            s.dispose();
        }
    }

    public void onNext(T t) {
        if (this.subscribers.get() != TERMINATED) {
            if (t == null) {
                nullOnNext();
            } else {
                this.value = t;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void nullOnNext() {
        this.value = null;
        Throwable ex = new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        this.error = ex;
        for (AsyncDisposable<T> as : (AsyncDisposable[]) this.subscribers.getAndSet(TERMINATED)) {
            as.onError(ex);
        }
    }

    public void onError(Throwable t) {
        if (t == null) {
            t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        if (this.subscribers.get() == TERMINATED) {
            RxJavaPlugins.onError(t);
            return;
        }
        this.value = null;
        this.error = t;
        for (AsyncDisposable<T> as : (AsyncDisposable[]) this.subscribers.getAndSet(TERMINATED)) {
            as.onError(t);
        }
    }

    public void onComplete() {
        if (this.subscribers.get() != TERMINATED) {
            T v = this.value;
            AsyncDisposable<T>[] array = (AsyncDisposable[]) this.subscribers.getAndSet(TERMINATED);
            if (v == null) {
                for (AsyncDisposable<T> as : array) {
                    as.onComplete();
                }
                return;
            }
            for (AsyncDisposable<T> as2 : array) {
                as2.complete(v);
            }
        }
    }

    public boolean hasObservers() {
        return ((AsyncDisposable[]) this.subscribers.get()).length != 0;
    }

    public boolean hasThrowable() {
        return this.subscribers.get() == TERMINATED && this.error != null;
    }

    public boolean hasComplete() {
        return this.subscribers.get() == TERMINATED && this.error == null;
    }

    public Throwable getThrowable() {
        if (this.subscribers.get() == TERMINATED) {
            return this.error;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> s) {
        AsyncDisposable<T> as = new AsyncDisposable<>(s, this);
        s.onSubscribe(as);
        if (!add(as)) {
            Throwable ex = this.error;
            if (ex != null) {
                s.onError(ex);
                return;
            }
            T v = this.value;
            if (v != null) {
                as.complete(v);
            } else {
                as.onComplete();
            }
        } else if (as.isDisposed()) {
            remove(as);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean add(AsyncDisposable<T> ps) {
        AsyncDisposable<T>[] a;
        AsyncDisposable<T>[] b;
        do {
            a = (AsyncDisposable[]) this.subscribers.get();
            if (a == TERMINATED) {
                return false;
            }
            int n = a.length;
            b = new AsyncDisposable[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = ps;
        } while (!this.subscribers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: package-private */
    public void remove(AsyncDisposable<T> ps) {
        AsyncDisposable<T>[] a;
        AsyncDisposable<T>[] b;
        do {
            a = (AsyncDisposable[]) this.subscribers.get();
            int n = a.length;
            if (n != 0) {
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (a[i] == ps) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j < 0) {
                    return;
                }
                if (n == 1) {
                    b = EMPTY;
                } else {
                    b = new AsyncDisposable[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                }
            } else {
                return;
            }
        } while (!this.subscribers.compareAndSet(a, b));
    }

    public boolean hasValue() {
        return this.subscribers.get() == TERMINATED && this.value != null;
    }

    public T getValue() {
        if (this.subscribers.get() == TERMINATED) {
            return this.value;
        }
        return null;
    }

    public Object[] getValues() {
        T v = getValue();
        if (v == null) {
            return new Object[0];
        }
        return new Object[]{v};
    }

    public T[] getValues(T[] array) {
        T v = getValue();
        if (v == null) {
            if (array.length != 0) {
                array[0] = null;
            }
            return array;
        }
        if (array.length == 0) {
            array = Arrays.copyOf(array, 1);
        }
        array[0] = v;
        if (array.length != 1) {
            array[1] = null;
        }
        return array;
    }

    static final class AsyncDisposable<T> extends DeferredScalarDisposable<T> {
        private static final long serialVersionUID = 5629876084736248016L;
        final AsyncSubject<T> parent;

        AsyncDisposable(Observer<? super T> actual, AsyncSubject<T> parent2) {
            super(actual);
            this.parent = parent2;
        }

        public void dispose() {
            if (super.tryDispose()) {
                this.parent.remove(this);
            }
        }

        /* access modifiers changed from: package-private */
        public void onComplete() {
            if (!isDisposed()) {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: package-private */
        public void onError(Throwable t) {
            if (isDisposed()) {
                RxJavaPlugins.onError(t);
            } else {
                this.actual.onError(t);
            }
        }
    }
}
