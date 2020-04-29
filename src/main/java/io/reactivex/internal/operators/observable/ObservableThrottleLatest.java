package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableThrottleLatest<T> extends AbstractObservableWithUpstream<T, T> {
    final boolean emitLast;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    public ObservableThrottleLatest(Observable<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2, boolean emitLast2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.emitLast = emitLast2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new ThrottleLatestObserver(observer, this.timeout, this.unit, this.scheduler.createWorker(), this.emitLast));
    }

    static final class ThrottleLatestObserver<T> extends AtomicInteger implements Observer<T>, Disposable, Runnable {
        private static final long serialVersionUID = -8296689127439125014L;
        volatile boolean cancelled;
        volatile boolean done;
        final Observer<? super T> downstream;
        final boolean emitLast;
        Throwable error;
        final AtomicReference<T> latest = new AtomicReference<>();
        final long timeout;
        volatile boolean timerFired;
        boolean timerRunning;
        final TimeUnit unit;
        Disposable upstream;
        final Scheduler.Worker worker;

        ThrottleLatestObserver(Observer<? super T> downstream2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2, boolean emitLast2) {
            this.downstream = downstream2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
            this.emitLast = emitLast2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                this.downstream.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.latest.set(t);
            drain();
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            drain();
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void dispose() {
            this.cancelled = true;
            this.upstream.dispose();
            this.worker.dispose();
            if (getAndIncrement() == 0) {
                this.latest.lazySet(null);
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void run() {
            this.timerFired = true;
            drain();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            boolean empty;
            if (getAndIncrement() == 0) {
                int missed = 1;
                AtomicReference<T> latest2 = this.latest;
                Observer<? super T> downstream2 = this.downstream;
                while (!this.cancelled) {
                    boolean d = this.done;
                    if (!d || this.error == null) {
                        if (latest2.get() == null) {
                            empty = true;
                        } else {
                            empty = false;
                        }
                        if (d) {
                            T v = latest2.getAndSet(null);
                            if (!empty && this.emitLast) {
                                downstream2.onNext(v);
                            }
                            downstream2.onComplete();
                            this.worker.dispose();
                            return;
                        }
                        if (empty) {
                            if (this.timerFired) {
                                this.timerRunning = false;
                                this.timerFired = false;
                            }
                        } else if (!this.timerRunning || this.timerFired) {
                            downstream2.onNext(latest2.getAndSet(null));
                            this.timerFired = false;
                            this.timerRunning = true;
                            this.worker.schedule(this, this.timeout, this.unit);
                        }
                        missed = addAndGet(-missed);
                        if (missed == 0) {
                            return;
                        }
                    } else {
                        latest2.lazySet(null);
                        downstream2.onError(this.error);
                        this.worker.dispose();
                        return;
                    }
                }
                latest2.lazySet(null);
            }
        }
    }
}
