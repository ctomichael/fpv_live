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
import io.reactivex.internal.observers.InnerQueuedObserver;
import io.reactivex.internal.observers.InnerQueuedObserverSupport;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableConcatMapEager<T, R> extends AbstractObservableWithUpstream<T, R> {
    final ErrorMode errorMode;
    final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
    final int maxConcurrency;
    final int prefetch;

    public ObservableConcatMapEager(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends R>> mapper2, ErrorMode errorMode2, int maxConcurrency2, int prefetch2) {
        super(source);
        this.mapper = mapper2;
        this.errorMode = errorMode2;
        this.maxConcurrency = maxConcurrency2;
        this.prefetch = prefetch2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super R> observer) {
        this.source.subscribe(new ConcatMapEagerMainObserver(observer, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode));
    }

    static final class ConcatMapEagerMainObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable, InnerQueuedObserverSupport<R> {
        private static final long serialVersionUID = 8080567949447303262L;
        int activeCount;
        volatile boolean cancelled;
        InnerQueuedObserver<R> current;
        volatile boolean done;
        final Observer<? super R> downstream;
        final AtomicThrowable error = new AtomicThrowable();
        final ErrorMode errorMode;
        final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
        final int maxConcurrency;
        final ArrayDeque<InnerQueuedObserver<R>> observers = new ArrayDeque<>();
        final int prefetch;
        SimpleQueue<T> queue;
        int sourceMode;
        Disposable upstream;

        ConcatMapEagerMainObserver(Observer<? super R> actual, Function<? super T, ? extends ObservableSource<? extends R>> mapper2, int maxConcurrency2, int prefetch2, ErrorMode errorMode2) {
            this.downstream = actual;
            this.mapper = mapper2;
            this.maxConcurrency = maxConcurrency2;
            this.prefetch = prefetch2;
            this.errorMode = errorMode2;
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
                this.queue = new SpscLinkedArrayQueue(this.prefetch);
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

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.upstream.dispose();
                drainAndDispose();
            }
        }

        /* access modifiers changed from: package-private */
        public void drainAndDispose() {
            if (getAndIncrement() == 0) {
                do {
                    this.queue.clear();
                    disposeAll();
                } while (decrementAndGet() != 0);
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void disposeAll() {
            InnerQueuedObserver<R> inner = this.current;
            if (inner != null) {
                inner.dispose();
            }
            while (true) {
                InnerQueuedObserver<R> inner2 = this.observers.poll();
                if (inner2 != null) {
                    inner2.dispose();
                } else {
                    return;
                }
            }
        }

        public void innerNext(InnerQueuedObserver<R> inner, R value) {
            inner.queue().offer(value);
            drain();
        }

        public void innerError(InnerQueuedObserver<R> inner, Throwable e) {
            if (this.error.addThrowable(e)) {
                if (this.errorMode == ErrorMode.IMMEDIATE) {
                    this.upstream.dispose();
                }
                inner.setDone();
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void innerComplete(InnerQueuedObserver<R> inner) {
            inner.setDone();
            drain();
        }

        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                SimpleQueue<T> q = this.queue;
                ArrayDeque<InnerQueuedObserver<R>> observers2 = this.observers;
                Observer<? super R> a = this.downstream;
                ErrorMode errorMode2 = this.errorMode;
                while (true) {
                    int ac = this.activeCount;
                    while (ac != this.maxConcurrency) {
                        if (this.cancelled) {
                            q.clear();
                            disposeAll();
                            return;
                        } else if (errorMode2 != ErrorMode.IMMEDIATE || ((Throwable) this.error.get()) == null) {
                            try {
                                T v = q.poll();
                                if (v == null) {
                                    break;
                                }
                                ObservableSource<? extends R> source = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null ObservableSource");
                                InnerQueuedObserver<R> inner = new InnerQueuedObserver<>(this, this.prefetch);
                                observers2.offer(inner);
                                source.subscribe(inner);
                                ac++;
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                this.upstream.dispose();
                                q.clear();
                                disposeAll();
                                this.error.addThrowable(ex);
                                a.onError(this.error.terminate());
                                return;
                            }
                        } else {
                            q.clear();
                            disposeAll();
                            a.onError(this.error.terminate());
                            return;
                        }
                    }
                    this.activeCount = ac;
                    if (this.cancelled) {
                        q.clear();
                        disposeAll();
                        return;
                    } else if (errorMode2 != ErrorMode.IMMEDIATE || ((Throwable) this.error.get()) == null) {
                        InnerQueuedObserver<R> active = this.current;
                        if (active == null) {
                            if (errorMode2 != ErrorMode.BOUNDARY || ((Throwable) this.error.get()) == null) {
                                boolean d = this.done;
                                active = observers2.poll();
                                boolean empty = active == null;
                                if (!d || !empty) {
                                    if (!empty) {
                                        this.current = active;
                                    }
                                } else if (((Throwable) this.error.get()) != null) {
                                    q.clear();
                                    disposeAll();
                                    a.onError(this.error.terminate());
                                    return;
                                } else {
                                    a.onComplete();
                                    return;
                                }
                            } else {
                                q.clear();
                                disposeAll();
                                a.onError(this.error.terminate());
                                return;
                            }
                        }
                        if (active != null) {
                            SimpleQueue<R> aq = active.queue();
                            while (!this.cancelled) {
                                boolean d2 = active.isDone();
                                if (errorMode2 != ErrorMode.IMMEDIATE || ((Throwable) this.error.get()) == null) {
                                    try {
                                        R w = aq.poll();
                                        boolean empty2 = w == null;
                                        if (d2 && empty2) {
                                            this.current = null;
                                            this.activeCount--;
                                        } else if (!empty2) {
                                            a.onNext(w);
                                        }
                                    } catch (Throwable ex2) {
                                        Exceptions.throwIfFatal(ex2);
                                        this.error.addThrowable(ex2);
                                        this.current = null;
                                        this.activeCount--;
                                    }
                                } else {
                                    q.clear();
                                    disposeAll();
                                    a.onError(this.error.terminate());
                                    return;
                                }
                            }
                            q.clear();
                            disposeAll();
                            return;
                        }
                        missed = addAndGet(-missed);
                        if (missed == 0) {
                            return;
                        }
                    } else {
                        q.clear();
                        disposeAll();
                        a.onError(this.error.terminate());
                        return;
                    }
                }
            }
        }
    }
}
