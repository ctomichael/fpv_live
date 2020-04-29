package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.disposables.ObserverFullArbiter;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.observers.FullArbiterObserver;
import dji.thirdparty.io.reactivex.observers.DisposableObserver;
import dji.thirdparty.io.reactivex.observers.SerializedObserver;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableTimeout<T, U, V> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<U> firstTimeoutIndicator;
    final Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator;
    final ObservableSource<? extends T> other;

    interface OnTimeout {
        void innerError(Throwable th);

        void timeout(long j);
    }

    public ObservableTimeout(ObservableSource<T> source, ObservableSource<U> firstTimeoutIndicator2, Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator2, ObservableSource<? extends T> other2) {
        super(source);
        this.firstTimeoutIndicator = firstTimeoutIndicator2;
        this.itemTimeoutIndicator = itemTimeoutIndicator2;
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> t) {
        if (this.other == null) {
            this.source.subscribe(new TimeoutObserver(new SerializedObserver(t), this.firstTimeoutIndicator, this.itemTimeoutIndicator));
        } else {
            this.source.subscribe(new TimeoutOtherObserver(t, this.firstTimeoutIndicator, this.itemTimeoutIndicator, this.other));
        }
    }

    static final class TimeoutObserver<T, U, V> extends AtomicReference<Disposable> implements Observer<T>, Disposable, OnTimeout {
        private static final long serialVersionUID = 2672739326310051084L;
        final Observer<? super T> actual;
        final ObservableSource<U> firstTimeoutIndicator;
        volatile long index;
        final Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator;
        Disposable s;

        TimeoutObserver(Observer<? super T> actual2, ObservableSource<U> firstTimeoutIndicator2, Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator2) {
            this.actual = actual2;
            this.firstTimeoutIndicator = firstTimeoutIndicator2;
            this.itemTimeoutIndicator = itemTimeoutIndicator2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                Observer<? super T> a = this.actual;
                ObservableSource<U> p = this.firstTimeoutIndicator;
                if (p != null) {
                    TimeoutInnerObserver<T, U, V> tis = new TimeoutInnerObserver<>(this, 0);
                    if (compareAndSet(null, tis)) {
                        a.onSubscribe(this);
                        p.subscribe(tis);
                        return;
                    }
                    return;
                }
                a.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            long idx = this.index + 1;
            this.index = idx;
            this.actual.onNext(t);
            Disposable d = (Disposable) get();
            if (d != null) {
                d.dispose();
            }
            try {
                ObservableSource<V> p = (ObservableSource) ObjectHelper.requireNonNull(this.itemTimeoutIndicator.apply(t), "The ObservableSource returned is null");
                TimeoutInnerObserver<T, U, V> tis = new TimeoutInnerObserver<>(this, idx);
                if (compareAndSet(d, tis)) {
                    p.subscribe(tis);
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                dispose();
                this.actual.onError(e);
            }
        }

        public void onError(Throwable t) {
            DisposableHelper.dispose(this);
            this.actual.onError(t);
        }

        public void onComplete() {
            DisposableHelper.dispose(this);
            this.actual.onComplete();
        }

        public void dispose() {
            if (DisposableHelper.dispose(this)) {
                this.s.dispose();
            }
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void timeout(long idx) {
            if (idx == this.index) {
                dispose();
                this.actual.onError(new TimeoutException());
            }
        }

        public void innerError(Throwable e) {
            this.s.dispose();
            this.actual.onError(e);
        }
    }

    static final class TimeoutInnerObserver<T, U, V> extends DisposableObserver<Object> {
        boolean done;
        final long index;
        final OnTimeout parent;

        TimeoutInnerObserver(OnTimeout parent2, long index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onNext(Object t) {
            if (!this.done) {
                this.done = true;
                dispose();
                this.parent.timeout(this.index);
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.parent.innerError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.timeout(this.index);
            }
        }
    }

    static final class TimeoutOtherObserver<T, U, V> extends AtomicReference<Disposable> implements Observer<T>, Disposable, OnTimeout {
        private static final long serialVersionUID = -1957813281749686898L;
        final Observer<? super T> actual;
        final ObserverFullArbiter<T> arbiter;
        boolean done;
        final ObservableSource<U> firstTimeoutIndicator;
        volatile long index;
        final Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator;
        final ObservableSource<? extends T> other;
        Disposable s;

        TimeoutOtherObserver(Observer<? super T> actual2, ObservableSource<U> firstTimeoutIndicator2, Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator2, ObservableSource<? extends T> other2) {
            this.actual = actual2;
            this.firstTimeoutIndicator = firstTimeoutIndicator2;
            this.itemTimeoutIndicator = itemTimeoutIndicator2;
            this.other = other2;
            this.arbiter = new ObserverFullArbiter<>(actual2, this, 8);
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.arbiter.setDisposable(s2);
                Observer<? super T> a = this.actual;
                ObservableSource<U> p = this.firstTimeoutIndicator;
                if (p != null) {
                    TimeoutInnerObserver<T, U, V> tis = new TimeoutInnerObserver<>(this, 0);
                    if (compareAndSet(null, tis)) {
                        a.onSubscribe(this.arbiter);
                        p.subscribe(tis);
                        return;
                    }
                    return;
                }
                a.onSubscribe(this.arbiter);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long idx = this.index + 1;
                this.index = idx;
                if (this.arbiter.onNext(t, this.s)) {
                    Disposable d = (Disposable) get();
                    if (d != null) {
                        d.dispose();
                    }
                    try {
                        ObservableSource<V> p = (ObservableSource) ObjectHelper.requireNonNull(this.itemTimeoutIndicator.apply(t), "The ObservableSource returned is null");
                        TimeoutInnerObserver<T, U, V> tis = new TimeoutInnerObserver<>(this, idx);
                        if (compareAndSet(d, tis)) {
                            p.subscribe(tis);
                        }
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        this.actual.onError(e);
                    }
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            dispose();
            this.arbiter.onError(t, this.s);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                dispose();
                this.arbiter.onComplete(this.s);
            }
        }

        public void dispose() {
            if (DisposableHelper.dispose(this)) {
                this.s.dispose();
            }
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void timeout(long idx) {
            if (idx == this.index) {
                dispose();
                this.other.subscribe(new FullArbiterObserver(this.arbiter));
            }
        }

        public void innerError(Throwable e) {
            this.s.dispose();
            this.actual.onError(e);
        }
    }
}
