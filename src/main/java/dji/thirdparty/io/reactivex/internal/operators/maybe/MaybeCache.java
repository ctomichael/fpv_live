package dji.thirdparty.io.reactivex.internal.operators.maybe;

import dji.thirdparty.io.reactivex.Maybe;
import dji.thirdparty.io.reactivex.MaybeObserver;
import dji.thirdparty.io.reactivex.MaybeSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeCache<T> extends Maybe<T> implements MaybeObserver<T> {
    static final CacheDisposable[] EMPTY = new CacheDisposable[0];
    static final CacheDisposable[] TERMINATED = new CacheDisposable[0];
    Throwable error;
    final AtomicReference<CacheDisposable<T>[]> observers = new AtomicReference<>(EMPTY);
    final AtomicReference<MaybeSource<T>> source;
    T value;

    public MaybeCache(MaybeSource<T> source2) {
        this.source = new AtomicReference<>(source2);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        CacheDisposable<T> parent = new CacheDisposable<>(observer, this);
        observer.onSubscribe(parent);
        if (add(parent)) {
            if (parent.isDisposed()) {
                remove(parent);
                return;
            }
            MaybeSource<T> src = this.source.getAndSet(null);
            if (src != null) {
                src.subscribe(this);
            }
        } else if (!parent.isDisposed()) {
            Throwable ex = this.error;
            if (ex != null) {
                observer.onError(ex);
                return;
            }
            T v = this.value;
            if (v != null) {
                observer.onSuccess(v);
            } else {
                observer.onComplete();
            }
        }
    }

    public void onSubscribe(Disposable d) {
    }

    public void onSuccess(T value2) {
        this.value = value2;
        CacheDisposable<T>[] arr$ = (CacheDisposable[]) this.observers.getAndSet(TERMINATED);
        for (CacheDisposable<T> inner : arr$) {
            if (!inner.isDisposed()) {
                inner.actual.onSuccess(value2);
            }
        }
    }

    public void onError(Throwable e) {
        this.error = e;
        CacheDisposable<T>[] arr$ = (CacheDisposable[]) this.observers.getAndSet(TERMINATED);
        for (CacheDisposable<T> inner : arr$) {
            if (!inner.isDisposed()) {
                inner.actual.onError(e);
            }
        }
    }

    public void onComplete() {
        CacheDisposable<T>[] arr$ = (CacheDisposable[]) this.observers.getAndSet(TERMINATED);
        for (CacheDisposable<T> inner : arr$) {
            if (!inner.isDisposed()) {
                inner.actual.onComplete();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean add(CacheDisposable<T> inner) {
        CacheDisposable<T>[] a;
        CacheDisposable<T>[] b;
        do {
            a = (CacheDisposable[]) this.observers.get();
            if (a == TERMINATED) {
                return false;
            }
            int n = a.length;
            b = new CacheDisposable[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = inner;
        } while (!this.observers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: package-private */
    public void remove(CacheDisposable<T> inner) {
        CacheDisposable<T>[] a;
        CacheDisposable<T>[] b;
        do {
            a = (CacheDisposable[]) this.observers.get();
            int n = a.length;
            if (n != 0) {
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (a[i] == inner) {
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
                    b = new CacheDisposable[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                }
            } else {
                return;
            }
        } while (!this.observers.compareAndSet(a, b));
    }

    static final class CacheDisposable<T> extends AtomicReference<MaybeCache<T>> implements Disposable {
        private static final long serialVersionUID = -5791853038359966195L;
        final MaybeObserver<? super T> actual;

        CacheDisposable(MaybeObserver<? super T> actual2, MaybeCache<T> parent) {
            super(parent);
            this.actual = actual2;
        }

        public void dispose() {
            MaybeCache<T> mc = (MaybeCache) getAndSet(null);
            if (mc != null) {
                mc.remove(this);
            }
        }

        public boolean isDisposed() {
            return get() == null;
        }
    }
}
