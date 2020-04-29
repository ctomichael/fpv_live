package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.disposables.SequentialDisposable;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.QueueDisposable;
import dji.thirdparty.io.reactivex.internal.fuseable.SimpleQueue;
import dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue;
import dji.thirdparty.io.reactivex.internal.util.AtomicThrowable;
import dji.thirdparty.io.reactivex.internal.util.ErrorMode;
import dji.thirdparty.io.reactivex.observers.SerializedObserver;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableConcatMap<T, U> extends AbstractObservableWithUpstream<T, U> {
    final int bufferSize;
    final ErrorMode delayErrors;
    final Function<? super T, ? extends ObservableSource<? extends U>> mapper;

    public ObservableConcatMap(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends U>> mapper2, int bufferSize2, ErrorMode delayErrors2) {
        super(source);
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
        this.bufferSize = Math.max(8, bufferSize2);
    }

    public void subscribeActual(Observer<? super U> s) {
        if (!ObservableScalarXMap.tryScalarXMapSubscribe(this.source, s, this.mapper)) {
            if (this.delayErrors == ErrorMode.IMMEDIATE) {
                this.source.subscribe(new SourceObserver(new SerializedObserver<>(s), this.mapper, this.bufferSize));
            } else {
                this.source.subscribe(new ConcatMapDelayErrorObserver(s, this.mapper, this.bufferSize, this.delayErrors == ErrorMode.END));
            }
        }
    }

    static final class SourceObserver<T, U> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = 8828587559905699186L;
        volatile boolean active;
        final Observer<? super U> actual;
        final int bufferSize;
        volatile boolean disposed;
        volatile boolean done;
        int fusionMode;
        final Observer<U> inner;
        final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
        SimpleQueue<T> queue;
        Disposable s;
        final SequentialDisposable sa = new SequentialDisposable();

        SourceObserver(Observer<? super U> actual2, Function<? super T, ? extends ObservableSource<? extends U>> mapper2, int bufferSize2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.bufferSize = bufferSize2;
            this.inner = new InnerObserver(actual2, this);
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                if (s2 instanceof QueueDisposable) {
                    QueueDisposable<T> qd = (QueueDisposable) s2;
                    int m = qd.requestFusion(3);
                    if (m == 1) {
                        this.fusionMode = m;
                        this.queue = qd;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        drain();
                        return;
                    } else if (m == 2) {
                        this.fusionMode = m;
                        this.queue = qd;
                        this.actual.onSubscribe(this);
                        return;
                    }
                }
                this.queue = new SpscLinkedArrayQueue(this.bufferSize);
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.fusionMode == 0) {
                    this.queue.offer(t);
                }
                drain();
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            dispose();
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerComplete() {
            this.active = false;
            drain();
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public void dispose() {
            this.disposed = true;
            this.sa.dispose();
            this.s.dispose();
            if (getAndIncrement() == 0) {
                this.queue.clear();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerSubscribe(Disposable s2) {
            this.sa.update(s2);
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                while (!this.disposed) {
                    if (!this.active) {
                        boolean d = this.done;
                        try {
                            T t = this.queue.poll();
                            boolean empty = t == null;
                            if (d && empty) {
                                this.actual.onComplete();
                                return;
                            } else if (!empty) {
                                try {
                                    ObservableSource<? extends U> o = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null ObservableSource");
                                    this.active = true;
                                    o.subscribe(this.inner);
                                } catch (Throwable ex) {
                                    Exceptions.throwIfFatal(ex);
                                    dispose();
                                    this.queue.clear();
                                    this.actual.onError(ex);
                                    return;
                                }
                            }
                        } catch (Throwable ex2) {
                            Exceptions.throwIfFatal(ex2);
                            dispose();
                            this.queue.clear();
                            this.actual.onError(ex2);
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
                this.queue.clear();
            }
        }

        static final class InnerObserver<U> implements Observer<U> {
            final Observer<? super U> actual;
            final SourceObserver<?, ?> parent;

            InnerObserver(Observer<? super U> actual2, SourceObserver<?, ?> parent2) {
                this.actual = actual2;
                this.parent = parent2;
            }

            public void onSubscribe(Disposable s) {
                this.parent.innerSubscribe(s);
            }

            public void onNext(U t) {
                this.actual.onNext(t);
            }

            public void onError(Throwable t) {
                this.parent.dispose();
                this.actual.onError(t);
            }

            public void onComplete() {
                this.parent.innerComplete();
            }
        }
    }

    static final class ConcatMapDelayErrorObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = -6951100001833242599L;
        volatile boolean active;
        final Observer<? super R> actual;
        final SequentialDisposable arbiter;
        final int bufferSize;
        volatile boolean cancelled;
        Disposable d;
        volatile boolean done;
        final AtomicThrowable error = new AtomicThrowable();
        final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
        final DelayErrorInnerObserver<R> observer;
        SimpleQueue<T> queue;
        int sourceMode;
        final boolean tillTheEnd;

        ConcatMapDelayErrorObserver(Observer<? super R> actual2, Function<? super T, ? extends ObservableSource<? extends R>> mapper2, int bufferSize2, boolean tillTheEnd2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.bufferSize = bufferSize2;
            this.tillTheEnd = tillTheEnd2;
            this.observer = new DelayErrorInnerObserver<>(actual2, this);
            this.arbiter = new SequentialDisposable();
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                if (d2 instanceof QueueDisposable) {
                    QueueDisposable<T> qd = (QueueDisposable) d2;
                    int m = qd.requestFusion(3);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = qd;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        drain();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = qd;
                        this.actual.onSubscribe(this);
                        return;
                    }
                }
                this.queue = new SpscLinkedArrayQueue(this.bufferSize);
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T value) {
            if (this.sourceMode == 0) {
                this.queue.offer(value);
            }
            drain();
        }

        public void onError(Throwable e) {
            if (this.error.addThrowable(e)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void dispose() {
            this.cancelled = true;
            this.d.dispose();
            this.arbiter.dispose();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                Observer<? super R> actual2 = this.actual;
                SimpleQueue<T> queue2 = this.queue;
                AtomicThrowable error2 = this.error;
                while (true) {
                    if (!this.active) {
                        if (this.cancelled) {
                            queue2.clear();
                            return;
                        } else if (this.tillTheEnd || ((Throwable) error2.get()) == null) {
                            boolean d2 = this.done;
                            try {
                                T v = queue2.poll();
                                boolean empty = v == null;
                                if (d2 && empty) {
                                    Throwable ex = error2.terminate();
                                    if (ex != null) {
                                        actual2.onError(ex);
                                        return;
                                    } else {
                                        actual2.onComplete();
                                        return;
                                    }
                                } else if (!empty) {
                                    try {
                                        ObservableSource<? extends R> o = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null ObservableSource");
                                        if (o instanceof Callable) {
                                            try {
                                                R w = ((Callable) o).call();
                                                if (w != null && !this.cancelled) {
                                                    actual2.onNext(w);
                                                }
                                            } catch (Throwable ex2) {
                                                Exceptions.throwIfFatal(ex2);
                                                error2.addThrowable(ex2);
                                            }
                                        } else {
                                            this.active = true;
                                            o.subscribe(this.observer);
                                        }
                                    } catch (Throwable ex3) {
                                        Exceptions.throwIfFatal(ex3);
                                        this.d.dispose();
                                        queue2.clear();
                                        error2.addThrowable(ex3);
                                        actual2.onError(error2.terminate());
                                        return;
                                    }
                                }
                            } catch (Throwable ex4) {
                                Exceptions.throwIfFatal(ex4);
                                this.d.dispose();
                                error2.addThrowable(ex4);
                                actual2.onError(error2.terminate());
                                return;
                            }
                        } else {
                            queue2.clear();
                            actual2.onError(error2.terminate());
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
            }
        }

        static final class DelayErrorInnerObserver<R> implements Observer<R> {
            final Observer<? super R> actual;
            final ConcatMapDelayErrorObserver<?, R> parent;

            DelayErrorInnerObserver(Observer<? super R> actual2, ConcatMapDelayErrorObserver<?, R> parent2) {
                this.actual = actual2;
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                this.parent.arbiter.replace(d);
            }

            public void onNext(R value) {
                this.actual.onNext(value);
            }

            public void onError(Throwable e) {
                ConcatMapDelayErrorObserver<?, R> p = this.parent;
                if (p.error.addThrowable(e)) {
                    if (!p.tillTheEnd) {
                        p.d.dispose();
                    }
                    p.active = false;
                    p.drain();
                    return;
                }
                RxJavaPlugins.onError(e);
            }

            public void onComplete() {
                ConcatMapDelayErrorObserver<?, R> p = this.parent;
                p.active = false;
                p.drain();
            }
        }
    }
}
