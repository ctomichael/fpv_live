package io.reactivex.internal.operators.parallel;

import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.parallel.ParallelFlowable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelFromPublisher<T> extends ParallelFlowable<T> {
    final int parallelism;
    final int prefetch;
    final Publisher<? extends T> source;

    public ParallelFromPublisher(Publisher<? extends T> source2, int parallelism2, int prefetch2) {
        this.source = source2;
        this.parallelism = parallelism2;
        this.prefetch = prefetch2;
    }

    public int parallelism() {
        return this.parallelism;
    }

    public void subscribe(Subscriber<? super T>[] subscribers) {
        if (validate(subscribers)) {
            this.source.subscribe(new ParallelDispatcher(subscribers, this.prefetch));
        }
    }

    static final class ParallelDispatcher<T> extends AtomicInteger implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -4470634016609963609L;
        volatile boolean cancelled;
        volatile boolean done;
        final long[] emissions;
        Throwable error;
        int index;
        final int limit;
        final int prefetch;
        int produced;
        SimpleQueue<T> queue;
        final AtomicLongArray requests;
        int sourceMode;
        final AtomicInteger subscriberCount = new AtomicInteger();
        final Subscriber<? super T>[] subscribers;
        Subscription upstream;

        ParallelDispatcher(Subscriber<? super T>[] subscribers2, int prefetch2) {
            this.subscribers = subscribers2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
            int m = subscribers2.length;
            this.requests = new AtomicLongArray(m + m + 1);
            this.requests.lazySet(m + m, (long) m);
            this.emissions = new long[m];
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> qs = (QueueSubscription) s;
                    int m = qs.requestFusion(7);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = qs;
                        this.done = true;
                        setupSubscribers();
                        drain();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = qs;
                        setupSubscribers();
                        s.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                setupSubscribers();
                s.request((long) this.prefetch);
            }
        }

        /* access modifiers changed from: package-private */
        public void setupSubscribers() {
            Subscriber<? super T>[] subs = this.subscribers;
            int m = subs.length;
            for (int i = 0; i < m && !this.cancelled; i++) {
                this.subscriberCount.lazySet(i + 1);
                subs[i].onSubscribe(new RailSubscription(i, m));
            }
        }

        final class RailSubscription implements Subscription {
            final int j;
            final int m;

            RailSubscription(int j2, int m2) {
                this.j = j2;
                this.m = m2;
            }

            public void request(long n) {
                long r;
                if (SubscriptionHelper.validate(n)) {
                    AtomicLongArray ra = ParallelDispatcher.this.requests;
                    do {
                        r = ra.get(this.j);
                        if (r != LongCompanionObject.MAX_VALUE) {
                        } else {
                            return;
                        }
                    } while (!ra.compareAndSet(this.j, r, BackpressureHelper.addCap(r, n)));
                    if (ParallelDispatcher.this.subscriberCount.get() == this.m) {
                        ParallelDispatcher.this.drain();
                    }
                }
            }

            public void cancel() {
                if (ParallelDispatcher.this.requests.compareAndSet(this.m + this.j, 0, 1)) {
                    ParallelDispatcher.this.cancel(this.m + this.m);
                }
            }
        }

        public void onNext(T t) {
            if (this.sourceMode != 0 || this.queue.offer(t)) {
                drain();
                return;
            }
            this.upstream.cancel();
            onError(new MissingBackpressureException("Queue is full?"));
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

        /* access modifiers changed from: package-private */
        public void cancel(int m) {
            if (this.requests.decrementAndGet(m) == 0) {
                this.cancelled = true;
                this.upstream.cancel();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void drainAsync() {
            Throwable ex;
            int missed = 1;
            SimpleQueue<T> q = this.queue;
            Subscriber<? super T>[] a = this.subscribers;
            AtomicLongArray r = this.requests;
            long[] e = this.emissions;
            int n = e.length;
            int idx = this.index;
            int consumed = this.produced;
            while (true) {
                int notReady = 0;
                while (!this.cancelled) {
                    boolean d = this.done;
                    if (!d || (ex = this.error) == null) {
                        boolean empty = q.isEmpty();
                        if (!d || !empty) {
                            if (!empty) {
                                long requestAtIndex = r.get(idx);
                                long emissionAtIndex = e[idx];
                                if (requestAtIndex == emissionAtIndex || r.get(n + idx) != 0) {
                                    notReady++;
                                } else {
                                    try {
                                        T v = q.poll();
                                        if (v != null) {
                                            a[idx].onNext(v);
                                            e[idx] = 1 + emissionAtIndex;
                                            consumed++;
                                            int c = consumed;
                                            if (c == this.limit) {
                                                consumed = 0;
                                                this.upstream.request((long) c);
                                            }
                                            notReady = 0;
                                        }
                                    } catch (Throwable ex2) {
                                        Exceptions.throwIfFatal(ex2);
                                        this.upstream.cancel();
                                        int length = a.length;
                                        for (int i = 0; i < length; i++) {
                                            a[i].onError(ex2);
                                        }
                                        return;
                                    }
                                }
                                idx++;
                                if (idx == n) {
                                    idx = 0;
                                }
                                if (notReady == n) {
                                }
                            }
                            int w = get();
                            if (w == missed) {
                                this.index = idx;
                                this.produced = consumed;
                                missed = addAndGet(-missed);
                                if (missed == 0) {
                                    return;
                                }
                            } else {
                                missed = w;
                            }
                        } else {
                            int length2 = a.length;
                            for (int i2 = 0; i2 < length2; i2++) {
                                a[i2].onComplete();
                            }
                            return;
                        }
                    } else {
                        q.clear();
                        int length3 = a.length;
                        for (int i3 = 0; i3 < length3; i3++) {
                            a[i3].onError(ex);
                        }
                        return;
                    }
                }
                q.clear();
                return;
            }
        }

        /* access modifiers changed from: package-private */
        public void drainSync() {
            int missed = 1;
            SimpleQueue<T> q = this.queue;
            Subscriber<? super T>[] a = this.subscribers;
            AtomicLongArray r = this.requests;
            long[] e = this.emissions;
            int n = e.length;
            int idx = this.index;
            while (true) {
                int notReady = 0;
                while (!this.cancelled) {
                    if (q.isEmpty()) {
                        int length = a.length;
                        for (int i = 0; i < length; i++) {
                            a[i].onComplete();
                        }
                        return;
                    }
                    long requestAtIndex = r.get(idx);
                    long emissionAtIndex = e[idx];
                    if (requestAtIndex == emissionAtIndex || r.get(n + idx) != 0) {
                        notReady++;
                    } else {
                        try {
                            T v = q.poll();
                            if (v == null) {
                                int length2 = a.length;
                                for (int i2 = 0; i2 < length2; i2++) {
                                    a[i2].onComplete();
                                }
                                return;
                            }
                            a[idx].onNext(v);
                            e[idx] = 1 + emissionAtIndex;
                            notReady = 0;
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            this.upstream.cancel();
                            int length3 = a.length;
                            for (int i3 = 0; i3 < length3; i3++) {
                                a[i3].onError(ex);
                            }
                            return;
                        }
                    }
                    idx++;
                    if (idx == n) {
                        idx = 0;
                        continue;
                    }
                    if (notReady == n) {
                        int w = get();
                        if (w == missed) {
                            this.index = idx;
                            missed = addAndGet(-missed);
                            if (missed == 0) {
                                return;
                            }
                        } else {
                            missed = w;
                        }
                    }
                }
                q.clear();
                return;
            }
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                if (this.sourceMode == 1) {
                    drainSync();
                } else {
                    drainAsync();
                }
            }
        }
    }
}
