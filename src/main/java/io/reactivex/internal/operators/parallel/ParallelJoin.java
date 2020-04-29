package io.reactivex.internal.operators.parallel;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelJoin<T> extends Flowable<T> {
    final boolean delayErrors;
    final int prefetch;
    final ParallelFlowable<? extends T> source;

    public ParallelJoin(ParallelFlowable<? extends T> source2, int prefetch2, boolean delayErrors2) {
        this.source = source2;
        this.prefetch = prefetch2;
        this.delayErrors = delayErrors2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        JoinSubscriptionBase<T> parent;
        if (this.delayErrors) {
            parent = new JoinSubscriptionDelayError<>(s, this.source.parallelism(), this.prefetch);
        } else {
            parent = new JoinSubscription<>(s, this.source.parallelism(), this.prefetch);
        }
        s.onSubscribe(parent);
        this.source.subscribe(parent.subscribers);
    }

    static abstract class JoinSubscriptionBase<T> extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = 3100232009247827843L;
        volatile boolean cancelled;
        final AtomicInteger done = new AtomicInteger();
        final Subscriber<? super T> downstream;
        final AtomicThrowable errors = new AtomicThrowable();
        final AtomicLong requested = new AtomicLong();
        final JoinInnerSubscriber<T>[] subscribers;

        /* access modifiers changed from: package-private */
        public abstract void drain();

        /* access modifiers changed from: package-private */
        public abstract void onComplete();

        /* access modifiers changed from: package-private */
        public abstract void onError(Throwable th);

        /* access modifiers changed from: package-private */
        public abstract void onNext(JoinInnerSubscriber<T> joinInnerSubscriber, T t);

        JoinSubscriptionBase(Subscriber<? super T> actual, int n, int prefetch) {
            this.downstream = actual;
            JoinInnerSubscriber<T>[] a = new JoinInnerSubscriber[n];
            for (int i = 0; i < n; i++) {
                a[i] = new JoinInnerSubscriber<>(this, prefetch);
            }
            this.subscribers = a;
            this.done.lazySet(n);
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
                cancelAll();
                if (getAndIncrement() == 0) {
                    cleanup();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void cancelAll() {
            for (JoinInnerSubscriber<T> s : this.subscribers) {
                s.cancel();
            }
        }

        /* access modifiers changed from: package-private */
        public void cleanup() {
            for (JoinInnerSubscriber<T> s : this.subscribers) {
                s.queue = null;
            }
        }
    }

    static final class JoinSubscription<T> extends JoinSubscriptionBase<T> {
        private static final long serialVersionUID = 6312374661811000451L;

        JoinSubscription(Subscriber<? super T> actual, int n, int prefetch) {
            super(actual, n, prefetch);
        }

        public void onNext(JoinInnerSubscriber<T> inner, T value) {
            if (get() == 0 && compareAndSet(0, 1)) {
                if (this.requested.get() != 0) {
                    this.downstream.onNext(value);
                    if (this.requested.get() != LongCompanionObject.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    inner.request(1);
                } else if (!inner.getQueue().offer(value)) {
                    cancelAll();
                    Throwable mbe = new MissingBackpressureException("Queue full?!");
                    if (this.errors.compareAndSet(null, mbe)) {
                        this.downstream.onError(mbe);
                        return;
                    } else {
                        RxJavaPlugins.onError(mbe);
                        return;
                    }
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            } else if (!inner.getQueue().offer(value)) {
                cancelAll();
                onError(new MissingBackpressureException("Queue full?!"));
                return;
            } else if (getAndIncrement() != 0) {
                return;
            }
            drainLoop();
        }

        public void onError(Throwable e) {
            if (this.errors.compareAndSet(null, e)) {
                cancelAll();
                drain();
            } else if (e != this.errors.get()) {
                RxJavaPlugins.onError(e);
            }
        }

        public void onComplete() {
            this.done.decrementAndGet();
            drain();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0089, code lost:
            if (r3 == false) goto L_0x0091;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x008b, code lost:
            if (r6 == false) goto L_0x0091;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x008d, code lost:
            r2.onComplete();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0091, code lost:
            if (r6 == false) goto L_0x001a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainLoop() {
            /*
                r22 = this;
                r10 = 1
                r0 = r22
                io.reactivex.internal.operators.parallel.ParallelJoin$JoinInnerSubscriber[] r0 = r0.subscribers
                r16 = r0
                r0 = r16
                int r11 = r0.length
                r0 = r22
                org.reactivestreams.Subscriber r2 = r0.downstream
            L_0x000e:
                r0 = r22
                java.util.concurrent.atomic.AtomicLong r0 = r0.requested
                r19 = r0
                long r14 = r19.get()
                r4 = 0
            L_0x001a:
                int r19 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1))
                if (r19 == 0) goto L_0x0074
                r0 = r22
                boolean r0 = r0.cancelled
                r19 = r0
                if (r19 == 0) goto L_0x002a
                r22.cleanup()
            L_0x0029:
                return
            L_0x002a:
                r0 = r22
                io.reactivex.internal.util.AtomicThrowable r0 = r0.errors
                r19 = r0
                java.lang.Object r7 = r19.get()
                java.lang.Throwable r7 = (java.lang.Throwable) r7
                if (r7 == 0) goto L_0x003f
                r22.cleanup()
                r2.onError(r7)
                goto L_0x0029
            L_0x003f:
                r0 = r22
                java.util.concurrent.atomic.AtomicInteger r0 = r0.done
                r19 = r0
                int r19 = r19.get()
                if (r19 != 0) goto L_0x0084
                r3 = 1
            L_0x004c:
                r6 = 1
                r8 = 0
            L_0x004e:
                r0 = r16
                int r0 = r0.length
                r19 = r0
                r0 = r19
                if (r8 >= r0) goto L_0x0089
                r9 = r16[r8]
                io.reactivex.internal.fuseable.SimplePlainQueue<T> r12 = r9.queue
                if (r12 == 0) goto L_0x0086
                java.lang.Object r17 = r12.poll()
                if (r17 == 0) goto L_0x0086
                r6 = 0
                r0 = r17
                r2.onNext(r0)
                r9.requestOne()
                r20 = 1
                long r4 = r4 + r20
                int r19 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1))
                if (r19 != 0) goto L_0x0086
            L_0x0074:
                int r19 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1))
                if (r19 != 0) goto L_0x00d5
                r0 = r22
                boolean r0 = r0.cancelled
                r19 = r0
                if (r19 == 0) goto L_0x0094
                r22.cleanup()
                goto L_0x0029
            L_0x0084:
                r3 = 0
                goto L_0x004c
            L_0x0086:
                int r8 = r8 + 1
                goto L_0x004e
            L_0x0089:
                if (r3 == 0) goto L_0x0091
                if (r6 == 0) goto L_0x0091
                r2.onComplete()
                goto L_0x0029
            L_0x0091:
                if (r6 == 0) goto L_0x001a
                goto L_0x0074
            L_0x0094:
                r0 = r22
                io.reactivex.internal.util.AtomicThrowable r0 = r0.errors
                r19 = r0
                java.lang.Object r7 = r19.get()
                java.lang.Throwable r7 = (java.lang.Throwable) r7
                if (r7 == 0) goto L_0x00a9
                r22.cleanup()
                r2.onError(r7)
                goto L_0x0029
            L_0x00a9:
                r0 = r22
                java.util.concurrent.atomic.AtomicInteger r0 = r0.done
                r19 = r0
                int r19 = r19.get()
                if (r19 != 0) goto L_0x00d0
                r3 = 1
            L_0x00b6:
                r6 = 1
                r8 = 0
            L_0x00b8:
                if (r8 >= r11) goto L_0x00c7
                r9 = r16[r8]
                io.reactivex.internal.fuseable.SimplePlainQueue<T> r13 = r9.queue
                if (r13 == 0) goto L_0x00d2
                boolean r19 = r13.isEmpty()
                if (r19 != 0) goto L_0x00d2
                r6 = 0
            L_0x00c7:
                if (r3 == 0) goto L_0x00d5
                if (r6 == 0) goto L_0x00d5
                r2.onComplete()
                goto L_0x0029
            L_0x00d0:
                r3 = 0
                goto L_0x00b6
            L_0x00d2:
                int r8 = r8 + 1
                goto L_0x00b8
            L_0x00d5:
                r20 = 0
                int r19 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1))
                if (r19 == 0) goto L_0x00f0
                r20 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r19 = (r14 > r20 ? 1 : (r14 == r20 ? 0 : -1))
                if (r19 == 0) goto L_0x00f0
                r0 = r22
                java.util.concurrent.atomic.AtomicLong r0 = r0.requested
                r19 = r0
                long r0 = -r4
                r20 = r0
                r19.addAndGet(r20)
            L_0x00f0:
                int r18 = r22.get()
                r0 = r18
                if (r0 != r10) goto L_0x0107
                int r0 = -r10
                r19 = r0
                r0 = r22
                r1 = r19
                int r10 = r0.addAndGet(r1)
                if (r10 != 0) goto L_0x000e
                goto L_0x0029
            L_0x0107:
                r10 = r18
                goto L_0x000e
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.parallel.ParallelJoin.JoinSubscription.drainLoop():void");
        }
    }

    static final class JoinSubscriptionDelayError<T> extends JoinSubscriptionBase<T> {
        private static final long serialVersionUID = -5737965195918321883L;

        JoinSubscriptionDelayError(Subscriber<? super T> actual, int n, int prefetch) {
            super(actual, n, prefetch);
        }

        /* access modifiers changed from: package-private */
        public void onNext(JoinInnerSubscriber<T> inner, T value) {
            if (get() != 0 || !compareAndSet(0, 1)) {
                if (!inner.getQueue().offer(value) && inner.cancel()) {
                    this.errors.addThrowable(new MissingBackpressureException("Queue full?!"));
                    this.done.decrementAndGet();
                }
                if (getAndIncrement() != 0) {
                    return;
                }
            } else {
                if (this.requested.get() != 0) {
                    this.downstream.onNext(value);
                    if (this.requested.get() != LongCompanionObject.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    inner.request(1);
                } else if (!inner.getQueue().offer(value)) {
                    inner.cancel();
                    this.errors.addThrowable(new MissingBackpressureException("Queue full?!"));
                    this.done.decrementAndGet();
                    drainLoop();
                    return;
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            }
            drainLoop();
        }

        /* access modifiers changed from: package-private */
        public void onError(Throwable e) {
            this.errors.addThrowable(e);
            this.done.decrementAndGet();
            drain();
        }

        /* access modifiers changed from: package-private */
        public void onComplete() {
            this.done.decrementAndGet();
            drain();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x006d, code lost:
            if (r3 == false) goto L_0x0093;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x006f, code lost:
            if (r6 == false) goto L_0x0093;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x007d, code lost:
            if (((java.lang.Throwable) r22.errors.get()) == null) goto L_0x008f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x007f, code lost:
            r2.onError(r22.errors.terminate());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x008f, code lost:
            r2.onComplete();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0093, code lost:
            if (r6 == false) goto L_0x001a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:83:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:84:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainLoop() {
            /*
                r22 = this;
                r10 = 1
                r0 = r22
                io.reactivex.internal.operators.parallel.ParallelJoin$JoinInnerSubscriber[] r0 = r0.subscribers
                r16 = r0
                r0 = r16
                int r11 = r0.length
                r0 = r22
                org.reactivestreams.Subscriber r2 = r0.downstream
            L_0x000e:
                r0 = r22
                java.util.concurrent.atomic.AtomicLong r0 = r0.requested
                r19 = r0
                long r14 = r19.get()
                r4 = 0
            L_0x001a:
                int r19 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1))
                if (r19 == 0) goto L_0x0058
                r0 = r22
                boolean r0 = r0.cancelled
                r19 = r0
                if (r19 == 0) goto L_0x002a
                r22.cleanup()
            L_0x0029:
                return
            L_0x002a:
                r0 = r22
                java.util.concurrent.atomic.AtomicInteger r0 = r0.done
                r19 = r0
                int r19 = r19.get()
                if (r19 != 0) goto L_0x0068
                r3 = 1
            L_0x0037:
                r6 = 1
                r8 = 0
            L_0x0039:
                if (r8 >= r11) goto L_0x006d
                r9 = r16[r8]
                io.reactivex.internal.fuseable.SimplePlainQueue<T> r12 = r9.queue
                if (r12 == 0) goto L_0x006a
                java.lang.Object r17 = r12.poll()
                if (r17 == 0) goto L_0x006a
                r6 = 0
                r0 = r17
                r2.onNext(r0)
                r9.requestOne()
                r20 = 1
                long r4 = r4 + r20
                int r19 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1))
                if (r19 != 0) goto L_0x006a
            L_0x0058:
                int r19 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1))
                if (r19 != 0) goto L_0x00e1
                r0 = r22
                boolean r0 = r0.cancelled
                r19 = r0
                if (r19 == 0) goto L_0x0096
                r22.cleanup()
                goto L_0x0029
            L_0x0068:
                r3 = 0
                goto L_0x0037
            L_0x006a:
                int r8 = r8 + 1
                goto L_0x0039
            L_0x006d:
                if (r3 == 0) goto L_0x0093
                if (r6 == 0) goto L_0x0093
                r0 = r22
                io.reactivex.internal.util.AtomicThrowable r0 = r0.errors
                r19 = r0
                java.lang.Object r7 = r19.get()
                java.lang.Throwable r7 = (java.lang.Throwable) r7
                if (r7 == 0) goto L_0x008f
                r0 = r22
                io.reactivex.internal.util.AtomicThrowable r0 = r0.errors
                r19 = r0
                java.lang.Throwable r19 = r19.terminate()
                r0 = r19
                r2.onError(r0)
                goto L_0x0029
            L_0x008f:
                r2.onComplete()
                goto L_0x0029
            L_0x0093:
                if (r6 == 0) goto L_0x001a
                goto L_0x0058
            L_0x0096:
                r0 = r22
                java.util.concurrent.atomic.AtomicInteger r0 = r0.done
                r19 = r0
                int r19 = r19.get()
                if (r19 != 0) goto L_0x00d7
                r3 = 1
            L_0x00a3:
                r6 = 1
                r8 = 0
            L_0x00a5:
                if (r8 >= r11) goto L_0x00b4
                r9 = r16[r8]
                io.reactivex.internal.fuseable.SimplePlainQueue<T> r13 = r9.queue
                if (r13 == 0) goto L_0x00d9
                boolean r19 = r13.isEmpty()
                if (r19 != 0) goto L_0x00d9
                r6 = 0
            L_0x00b4:
                if (r3 == 0) goto L_0x00e1
                if (r6 == 0) goto L_0x00e1
                r0 = r22
                io.reactivex.internal.util.AtomicThrowable r0 = r0.errors
                r19 = r0
                java.lang.Object r7 = r19.get()
                java.lang.Throwable r7 = (java.lang.Throwable) r7
                if (r7 == 0) goto L_0x00dc
                r0 = r22
                io.reactivex.internal.util.AtomicThrowable r0 = r0.errors
                r19 = r0
                java.lang.Throwable r19 = r19.terminate()
                r0 = r19
                r2.onError(r0)
                goto L_0x0029
            L_0x00d7:
                r3 = 0
                goto L_0x00a3
            L_0x00d9:
                int r8 = r8 + 1
                goto L_0x00a5
            L_0x00dc:
                r2.onComplete()
                goto L_0x0029
            L_0x00e1:
                r20 = 0
                int r19 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1))
                if (r19 == 0) goto L_0x00fc
                r20 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r19 = (r14 > r20 ? 1 : (r14 == r20 ? 0 : -1))
                if (r19 == 0) goto L_0x00fc
                r0 = r22
                java.util.concurrent.atomic.AtomicLong r0 = r0.requested
                r19 = r0
                long r0 = -r4
                r20 = r0
                r19.addAndGet(r20)
            L_0x00fc:
                int r18 = r22.get()
                r0 = r18
                if (r0 != r10) goto L_0x0113
                int r0 = -r10
                r19 = r0
                r0 = r22
                r1 = r19
                int r10 = r0.addAndGet(r1)
                if (r10 != 0) goto L_0x000e
                goto L_0x0029
            L_0x0113:
                r10 = r18
                goto L_0x000e
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.parallel.ParallelJoin.JoinSubscriptionDelayError.drainLoop():void");
        }
    }

    static final class JoinInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = 8410034718427740355L;
        final int limit;
        final JoinSubscriptionBase<T> parent;
        final int prefetch;
        long produced;
        volatile SimplePlainQueue<T> queue;

        JoinInnerSubscriber(JoinSubscriptionBase<T> parent2, int prefetch2) {
            this.parent = parent2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, (long) this.prefetch);
        }

        public void onNext(T t) {
            this.parent.onNext(this, t);
        }

        public void onError(Throwable t) {
            this.parent.onError(t);
        }

        public void onComplete() {
            this.parent.onComplete();
        }

        public void requestOne() {
            long p = this.produced + 1;
            if (p == ((long) this.limit)) {
                this.produced = 0;
                ((Subscription) get()).request(p);
                return;
            }
            this.produced = p;
        }

        public void request(long n) {
            long p = this.produced + n;
            if (p >= ((long) this.limit)) {
                this.produced = 0;
                ((Subscription) get()).request(p);
                return;
            }
            this.produced = p;
        }

        public boolean cancel() {
            return SubscriptionHelper.cancel(this);
        }

        /* access modifiers changed from: package-private */
        public SimplePlainQueue<T> getQueue() {
            SimplePlainQueue<T> q = this.queue;
            if (q != null) {
                return q;
            }
            SimplePlainQueue<T> q2 = new SpscArrayQueue<>(this.prefetch);
            this.queue = q2;
            return q2;
        }
    }
}
