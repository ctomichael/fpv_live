package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeLastTimed<T> extends AbstractFlowableWithUpstream<T, T> {
    final int bufferSize;
    final long count;
    final boolean delayError;
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    public FlowableTakeLastTimed(Flowable<T> source, long count2, long time2, TimeUnit unit2, Scheduler scheduler2, int bufferSize2, boolean delayError2) {
        super(source);
        this.count = count2;
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.bufferSize = bufferSize2;
        this.delayError = delayError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber) new TakeLastTimedSubscriber(s, this.count, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError));
    }

    static final class TakeLastTimedSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -5677354903406201275L;
        volatile boolean cancelled;
        final long count;
        final boolean delayError;
        volatile boolean done;
        final Subscriber<? super T> downstream;
        Throwable error;
        final SpscLinkedArrayQueue<Object> queue;
        final AtomicLong requested = new AtomicLong();
        final Scheduler scheduler;
        final long time;
        final TimeUnit unit;
        Subscription upstream;

        TakeLastTimedSubscriber(Subscriber<? super T> actual, long count2, long time2, TimeUnit unit2, Scheduler scheduler2, int bufferSize, boolean delayError2) {
            this.downstream = actual;
            this.count = count2;
            this.time = time2;
            this.unit = unit2;
            this.scheduler = scheduler2;
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
            this.delayError = delayError2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                this.downstream.onSubscribe(this);
                s.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            SpscLinkedArrayQueue<Object> q = this.queue;
            long now = this.scheduler.now(this.unit);
            q.offer(Long.valueOf(now), t);
            trim(now, q);
        }

        public void onError(Throwable t) {
            if (this.delayError) {
                trim(this.scheduler.now(this.unit), this.queue);
            }
            this.error = t;
            this.done = true;
            drain();
        }

        public void onComplete() {
            trim(this.scheduler.now(this.unit), this.queue);
            this.done = true;
            drain();
        }

        /* access modifiers changed from: package-private */
        public void trim(long now, SpscLinkedArrayQueue<Object> q) {
            long time2 = this.time;
            long c = this.count;
            boolean unbounded = c == LongCompanionObject.MAX_VALUE;
            while (!q.isEmpty()) {
                if (((Long) q.peek()).longValue() < now - time2 || (!unbounded && ((long) (q.size() >> 1)) > c)) {
                    q.poll();
                    q.poll();
                } else {
                    return;
                }
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.upstream.cancel();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super T> a = this.downstream;
                SpscLinkedArrayQueue<Object> q = this.queue;
                boolean delayError2 = this.delayError;
                do {
                    if (this.done) {
                        if (!checkTerminated(q.isEmpty(), a, delayError2)) {
                            long r = this.requested.get();
                            long e = 0;
                            while (true) {
                                if (checkTerminated(q.peek() == null, a, delayError2)) {
                                    return;
                                }
                                if (r != e) {
                                    q.poll();
                                    a.onNext(q.poll());
                                    e++;
                                } else if (e != 0) {
                                    BackpressureHelper.produced(this.requested, e);
                                }
                            }
                        } else {
                            return;
                        }
                    }
                    missed = addAndGet(-missed);
                } while (missed != 0);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean empty, Subscriber<? super T> a, boolean delayError2) {
            if (this.cancelled) {
                this.queue.clear();
                return true;
            }
            if (!delayError2) {
                Throwable e = this.error;
                if (e != null) {
                    this.queue.clear();
                    a.onError(e);
                    return true;
                } else if (empty) {
                    a.onComplete();
                    return true;
                }
            } else if (empty) {
                Throwable e2 = this.error;
                if (e2 != null) {
                    a.onError(e2);
                    return true;
                }
                a.onComplete();
                return true;
            }
            return false;
        }
    }
}
