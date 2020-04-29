package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableThrottleLatest<T> extends AbstractFlowableWithUpstream<T, T> {
    final boolean emitLast;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    public FlowableThrottleLatest(Flowable<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2, boolean emitLast2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.emitLast = emitLast2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber) new ThrottleLatestSubscriber(s, this.timeout, this.unit, this.scheduler.createWorker(), this.emitLast));
    }

    static final class ThrottleLatestSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = -8296689127439125014L;
        volatile boolean cancelled;
        volatile boolean done;
        final Subscriber<? super T> downstream;
        final boolean emitLast;
        long emitted;
        Throwable error;
        final AtomicReference<T> latest = new AtomicReference<>();
        final AtomicLong requested = new AtomicLong();
        final long timeout;
        volatile boolean timerFired;
        boolean timerRunning;
        final TimeUnit unit;
        Subscription upstream;
        final Scheduler.Worker worker;

        ThrottleLatestSubscriber(Subscriber<? super T> downstream2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2, boolean emitLast2) {
            this.downstream = downstream2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
            this.emitLast = emitLast2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                this.downstream.onSubscribe(this);
                s.request(LongCompanionObject.MAX_VALUE);
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

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
            }
        }

        public void cancel() {
            this.cancelled = true;
            this.upstream.cancel();
            this.worker.dispose();
            if (getAndIncrement() == 0) {
                this.latest.lazySet(null);
            }
        }

        public void run() {
            this.timerFired = true;
            drain();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                AtomicReference<T> latest2 = this.latest;
                AtomicLong requested2 = this.requested;
                Subscriber<? super T> downstream2 = this.downstream;
                while (!this.cancelled) {
                    boolean d = this.done;
                    if (!d || this.error == null) {
                        boolean empty = latest2.get() == null;
                        if (d) {
                            if (empty || !this.emitLast) {
                                latest2.lazySet(null);
                                downstream2.onComplete();
                            } else {
                                T v = latest2.getAndSet(null);
                                long e = this.emitted;
                                if (e != requested2.get()) {
                                    this.emitted = 1 + e;
                                    downstream2.onNext(v);
                                    downstream2.onComplete();
                                } else {
                                    downstream2.onError(new MissingBackpressureException("Could not emit final value due to lack of requests"));
                                }
                            }
                            this.worker.dispose();
                            return;
                        }
                        if (empty) {
                            if (this.timerFired) {
                                this.timerRunning = false;
                                this.timerFired = false;
                            }
                        } else if (!this.timerRunning || this.timerFired) {
                            T v2 = latest2.getAndSet(null);
                            long e2 = this.emitted;
                            if (e2 != requested2.get()) {
                                downstream2.onNext(v2);
                                this.emitted = 1 + e2;
                                this.timerFired = false;
                                this.timerRunning = true;
                                this.worker.schedule(this, this.timeout, this.unit);
                            } else {
                                this.upstream.cancel();
                                downstream2.onError(new MissingBackpressureException("Could not emit value due to lack of requests"));
                                this.worker.dispose();
                                return;
                            }
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
