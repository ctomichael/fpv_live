package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    public void subscribeActual(Observer<? super U> observer) {
        if (!ObservableScalarXMap.tryScalarXMapSubscribe(this.source, observer, this.mapper)) {
            if (this.delayErrors == ErrorMode.IMMEDIATE) {
                this.source.subscribe(new SourceObserver(new SerializedObserver<>(observer), this.mapper, this.bufferSize));
            } else {
                this.source.subscribe(new ConcatMapDelayErrorObserver(observer, this.mapper, this.bufferSize, this.delayErrors == ErrorMode.END));
            }
        }
    }

    static final class SourceObserver<T, U> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = 8828587559905699186L;
        volatile boolean active;
        final int bufferSize;
        volatile boolean disposed;
        volatile boolean done;
        final Observer<? super U> downstream;
        int fusionMode;
        final InnerObserver<U> inner;
        final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
        SimpleQueue<T> queue;
        Disposable upstream;

        SourceObserver(Observer<? super U> actual, Function<? super T, ? extends ObservableSource<? extends U>> mapper2, int bufferSize2) {
            this.downstream = actual;
            this.mapper = mapper2;
            this.bufferSize = bufferSize2;
            this.inner = new InnerObserver<>(actual, this);
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                if (d instanceof QueueDisposable) {
                    QueueDisposable<T> qd = (QueueDisposable) d;
                    int m = qd.requestFusion(3);
                    if (m == 1) {
                        this.fusionMode = m;
                        this.queue = qd;
                        this.done = true;
                        this.downstream.onSubscribe(this);
                        drain();
                        return;
                    } else if (m == 2) {
                        this.fusionMode = m;
                        this.queue = qd;
                        this.downstream.onSubscribe(this);
                        return;
                    }
                }
                this.queue = new SpscLinkedArrayQueue(this.bufferSize);
                this.downstream.onSubscribe(this);
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
            this.downstream.onError(t);
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
            this.inner.dispose();
            this.upstream.dispose();
            if (getAndIncrement() == 0) {
                this.queue.clear();
            }
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
                                this.disposed = true;
                                this.downstream.onComplete();
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
                                    this.downstream.onError(ex);
                                    return;
                                }
                            }
                        } catch (Throwable ex2) {
                            Exceptions.throwIfFatal(ex2);
                            dispose();
                            this.queue.clear();
                            this.downstream.onError(ex2);
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

        static final class InnerObserver<U> extends AtomicReference<Disposable> implements Observer<U> {
            private static final long serialVersionUID = -7449079488798789337L;
            final Observer<? super U> downstream;
            final SourceObserver<?, ?> parent;

            InnerObserver(Observer<? super U> actual, SourceObserver<?, ?> parent2) {
                this.downstream = actual;
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.replace(this, d);
            }

            public void onNext(U t) {
                this.downstream.onNext(t);
            }

            public void onError(Throwable t) {
                this.parent.dispose();
                this.downstream.onError(t);
            }

            public void onComplete() {
                this.parent.innerComplete();
            }

            /* access modifiers changed from: package-private */
            public void dispose() {
                DisposableHelper.dispose(this);
            }
        }
    }

    static final class ConcatMapDelayErrorObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = -6951100001833242599L;
        volatile boolean active;
        final int bufferSize;
        volatile boolean cancelled;
        volatile boolean done;
        final Observer<? super R> downstream;
        final AtomicThrowable error = new AtomicThrowable();
        final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
        final DelayErrorInnerObserver<R> observer;
        SimpleQueue<T> queue;
        int sourceMode;
        final boolean tillTheEnd;
        Disposable upstream;

        ConcatMapDelayErrorObserver(Observer<? super R> actual, Function<? super T, ? extends ObservableSource<? extends R>> mapper2, int bufferSize2, boolean tillTheEnd2) {
            this.downstream = actual;
            this.mapper = mapper2;
            this.bufferSize = bufferSize2;
            this.tillTheEnd = tillTheEnd2;
            this.observer = new DelayErrorInnerObserver<>(actual, this);
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                if (d instanceof QueueDisposable) {
                    QueueDisposable<T> qd = (QueueDisposable) d;
                    int m = qd.requestFusion(3);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = qd;
                        this.done = true;
                        this.downstream.onSubscribe(this);
                        drain();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = qd;
                        this.downstream.onSubscribe(this);
                        return;
                    }
                }
                this.queue = new SpscLinkedArrayQueue(this.bufferSize);
                this.downstream.onSubscribe(this);
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
            return this.cancelled;
        }

        public void dispose() {
            this.cancelled = true;
            this.upstream.dispose();
            this.observer.dispose();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                Observer<? super R> actual = this.downstream;
                SimpleQueue<T> queue2 = this.queue;
                AtomicThrowable error2 = this.error;
                while (true) {
                    if (!this.active) {
                        if (this.cancelled) {
                            queue2.clear();
                            return;
                        } else if (this.tillTheEnd || ((Throwable) error2.get()) == null) {
                            boolean d = this.done;
                            try {
                                T v = queue2.poll();
                                boolean empty = v == null;
                                if (d && empty) {
                                    this.cancelled = true;
                                    Throwable ex = error2.terminate();
                                    if (ex != null) {
                                        actual.onError(ex);
                                        return;
                                    } else {
                                        actual.onComplete();
                                        return;
                                    }
                                } else if (!empty) {
                                    try {
                                        ObservableSource<? extends R> o = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null ObservableSource");
                                        if (o instanceof Callable) {
                                            try {
                                                R w = ((Callable) o).call();
                                                if (w != null && !this.cancelled) {
                                                    actual.onNext(w);
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
                                        this.cancelled = true;
                                        this.upstream.dispose();
                                        queue2.clear();
                                        error2.addThrowable(ex3);
                                        actual.onError(error2.terminate());
                                        return;
                                    }
                                }
                            } catch (Throwable ex4) {
                                Exceptions.throwIfFatal(ex4);
                                this.cancelled = true;
                                this.upstream.dispose();
                                error2.addThrowable(ex4);
                                actual.onError(error2.terminate());
                                return;
                            }
                        } else {
                            queue2.clear();
                            this.cancelled = true;
                            actual.onError(error2.terminate());
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
            }
        }

        static final class DelayErrorInnerObserver<R> extends AtomicReference<Disposable> implements Observer<R> {
            private static final long serialVersionUID = 2620149119579502636L;
            final Observer<? super R> downstream;
            final ConcatMapDelayErrorObserver<?, R> parent;

            DelayErrorInnerObserver(Observer<? super R> actual, ConcatMapDelayErrorObserver<?, R> parent2) {
                this.downstream = actual;
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.replace(this, d);
            }

            public void onNext(R value) {
                this.downstream.onNext(value);
            }

            public void onError(Throwable e) {
                ConcatMapDelayErrorObserver<?, R> p = this.parent;
                if (p.error.addThrowable(e)) {
                    if (!p.tillTheEnd) {
                        p.upstream.dispose();
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

            /* access modifiers changed from: package-private */
            public void dispose() {
                DisposableHelper.dispose(this);
            }
        }
    }
}
