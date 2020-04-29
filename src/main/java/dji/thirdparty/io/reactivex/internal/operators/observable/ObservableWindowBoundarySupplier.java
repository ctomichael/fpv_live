package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.observers.QueueDrainObserver;
import dji.thirdparty.io.reactivex.internal.queue.MpscLinkedQueue;
import dji.thirdparty.io.reactivex.internal.util.NotificationLite;
import dji.thirdparty.io.reactivex.observers.DisposableObserver;
import dji.thirdparty.io.reactivex.observers.SerializedObserver;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.subjects.UnicastSubject;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWindowBoundarySupplier<T, B> extends AbstractObservableWithUpstream<T, Observable<T>> {
    final int bufferSize;
    final Callable<? extends ObservableSource<B>> other;

    public ObservableWindowBoundarySupplier(ObservableSource<T> source, Callable<? extends ObservableSource<B>> other2, int bufferSize2) {
        super(source);
        this.other = other2;
        this.bufferSize = bufferSize2;
    }

    public void subscribeActual(Observer<? super Observable<T>> t) {
        this.source.subscribe(new WindowBoundaryMainObserver(new SerializedObserver(t), this.other, this.bufferSize));
    }

    static final class WindowBoundaryMainObserver<T, B> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable {
        static final Object NEXT = new Object();
        final AtomicReference<Disposable> boundary = new AtomicReference<>();
        final int bufferSize;
        final Callable<? extends ObservableSource<B>> other;
        Disposable s;
        UnicastSubject<T> window;
        final AtomicLong windows = new AtomicLong();

        WindowBoundaryMainObserver(Observer<? super Observable<T>> actual, Callable<? extends ObservableSource<B>> other2, int bufferSize2) {
            super(actual, new MpscLinkedQueue());
            this.other = other2;
            this.bufferSize = bufferSize2;
            this.windows.lazySet(1);
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                Observer<? super Observable<T>> a = this.actual;
                a.onSubscribe(this);
                if (!this.cancelled) {
                    try {
                        ObservableSource<B> p = (ObservableSource) ObjectHelper.requireNonNull(this.other.call(), "The first window ObservableSource supplied is null");
                        UnicastSubject<T> w = UnicastSubject.create(this.bufferSize);
                        this.window = w;
                        a.onNext(w);
                        WindowBoundaryInnerObserver<T, B> inner = new WindowBoundaryInnerObserver<>(this);
                        if (this.boundary.compareAndSet(null, inner)) {
                            this.windows.getAndIncrement();
                            p.subscribe(inner);
                        }
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        s2.dispose();
                        a.onError(e);
                    }
                }
            }
        }

        public void onNext(T t) {
            if (fastEnter()) {
                this.window.onNext(t);
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                this.queue.offer(NotificationLite.next(t));
                if (!enter()) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            if (this.windows.decrementAndGet() == 0) {
                DisposableHelper.dispose(this.boundary);
            }
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                if (enter()) {
                    drainLoop();
                }
                if (this.windows.decrementAndGet() == 0) {
                    DisposableHelper.dispose(this.boundary);
                }
                this.actual.onComplete();
            }
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void drainLoop() {
            MpscLinkedQueue<Object> q = (MpscLinkedQueue) this.queue;
            Observer<? super Observable<T>> a = this.actual;
            int missed = 1;
            UnicastSubject<T> w = this.window;
            while (true) {
                boolean d = this.done;
                Object o = q.poll();
                boolean empty = o == null;
                if (d && empty) {
                    DisposableHelper.dispose(this.boundary);
                    Throwable e = this.error;
                    if (e != null) {
                        w.onError(e);
                        return;
                    } else {
                        w.onComplete();
                        return;
                    }
                } else if (empty) {
                    missed = leave(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else if (o == NEXT) {
                    w.onComplete();
                    if (this.windows.decrementAndGet() == 0) {
                        DisposableHelper.dispose(this.boundary);
                        return;
                    } else if (!this.cancelled) {
                        try {
                            ObservableSource<B> p = (ObservableSource) ObjectHelper.requireNonNull(this.other.call(), "The ObservableSource supplied is null");
                            w = UnicastSubject.create(this.bufferSize);
                            this.windows.getAndIncrement();
                            this.window = w;
                            a.onNext(w);
                            WindowBoundaryInnerObserver<T, B> b = new WindowBoundaryInnerObserver<>(this);
                            if (this.boundary.compareAndSet(this.boundary.get(), b)) {
                                p.subscribe(b);
                            }
                        } catch (Throwable e2) {
                            Exceptions.throwIfFatal(e2);
                            DisposableHelper.dispose(this.boundary);
                            a.onError(e2);
                            return;
                        }
                    }
                } else {
                    w.onNext(NotificationLite.getValue(o));
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void next() {
            this.queue.offer(NEXT);
            if (enter()) {
                drainLoop();
            }
        }
    }

    static final class WindowBoundaryInnerObserver<T, B> extends DisposableObserver<B> {
        boolean done;
        final WindowBoundaryMainObserver<T, B> parent;

        WindowBoundaryInnerObserver(WindowBoundaryMainObserver<T, B> parent2) {
            this.parent = parent2;
        }

        public void onNext(B b) {
            if (!this.done) {
                this.done = true;
                dispose();
                this.parent.next();
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.parent.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.onComplete();
            }
        }
    }
}
