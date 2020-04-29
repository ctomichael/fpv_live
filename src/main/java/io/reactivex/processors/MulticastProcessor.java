package io.reactivex.processors;

import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@BackpressureSupport(BackpressureKind.FULL)
@SchedulerSupport("none")
public final class MulticastProcessor<T> extends FlowableProcessor<T> {
    static final MulticastSubscription[] EMPTY = new MulticastSubscription[0];
    static final MulticastSubscription[] TERMINATED = new MulticastSubscription[0];
    final int bufferSize;
    int consumed;
    volatile boolean done;
    volatile Throwable error;
    int fusionMode;
    final int limit;
    final AtomicBoolean once;
    volatile SimpleQueue<T> queue;
    final boolean refcount;
    final AtomicReference<MulticastSubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);
    final AtomicReference<Subscription> upstream = new AtomicReference<>();
    final AtomicInteger wip = new AtomicInteger();

    @CheckReturnValue
    @NonNull
    public static <T> MulticastProcessor<T> create() {
        return new MulticastProcessor<>(bufferSize(), false);
    }

    @CheckReturnValue
    @NonNull
    public static <T> MulticastProcessor<T> create(boolean refCount) {
        return new MulticastProcessor<>(bufferSize(), refCount);
    }

    @CheckReturnValue
    @NonNull
    public static <T> MulticastProcessor<T> create(int bufferSize2) {
        return new MulticastProcessor<>(bufferSize2, false);
    }

    @CheckReturnValue
    @NonNull
    public static <T> MulticastProcessor<T> create(int bufferSize2, boolean refCount) {
        return new MulticastProcessor<>(bufferSize2, refCount);
    }

    MulticastProcessor(int bufferSize2, boolean refCount) {
        ObjectHelper.verifyPositive(bufferSize2, "bufferSize");
        this.bufferSize = bufferSize2;
        this.limit = bufferSize2 - (bufferSize2 >> 2);
        this.refcount = refCount;
        this.once = new AtomicBoolean();
    }

    public void start() {
        if (SubscriptionHelper.setOnce(this.upstream, EmptySubscription.INSTANCE)) {
            this.queue = new SpscArrayQueue(this.bufferSize);
        }
    }

    public void startUnbounded() {
        if (SubscriptionHelper.setOnce(this.upstream, EmptySubscription.INSTANCE)) {
            this.queue = new SpscLinkedArrayQueue(this.bufferSize);
        }
    }

    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.setOnce(this.upstream, s)) {
            if (s instanceof QueueSubscription) {
                QueueSubscription<T> qs = (QueueSubscription) s;
                int m = qs.requestFusion(3);
                if (m == 1) {
                    this.fusionMode = m;
                    this.queue = qs;
                    this.done = true;
                    drain();
                    return;
                } else if (m == 2) {
                    this.fusionMode = m;
                    this.queue = qs;
                    s.request((long) this.bufferSize);
                    return;
                }
            }
            this.queue = new SpscArrayQueue(this.bufferSize);
            s.request((long) this.bufferSize);
        }
    }

    public void onNext(T t) {
        if (!this.once.get()) {
            if (this.fusionMode == 0) {
                ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
                if (!this.queue.offer(t)) {
                    SubscriptionHelper.cancel(this.upstream);
                    onError(new MissingBackpressureException());
                    return;
                }
            }
            drain();
        }
    }

    public boolean offer(T t) {
        if (this.once.get()) {
            return false;
        }
        ObjectHelper.requireNonNull(t, "offer called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.fusionMode != 0 || !this.queue.offer(t)) {
            return false;
        }
        drain();
        return true;
    }

    public void onError(Throwable t) {
        ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.once.compareAndSet(false, true)) {
            this.error = t;
            this.done = true;
            drain();
            return;
        }
        RxJavaPlugins.onError(t);
    }

    public void onComplete() {
        if (this.once.compareAndSet(false, true)) {
            this.done = true;
            drain();
        }
    }

    public boolean hasSubscribers() {
        return ((MulticastSubscription[]) this.subscribers.get()).length != 0;
    }

    public boolean hasThrowable() {
        return this.once.get() && this.error != null;
    }

    public boolean hasComplete() {
        return this.once.get() && this.error == null;
    }

    public Throwable getThrowable() {
        if (this.once.get()) {
            return this.error;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        Throwable ex;
        MulticastSubscription<T> ms = new MulticastSubscription<>(s, this);
        s.onSubscribe(ms);
        if (add(ms)) {
            if (ms.get() == Long.MIN_VALUE) {
                remove(ms);
            } else {
                drain();
            }
        } else if ((this.once.get() || !this.refcount) && (ex = this.error) != null) {
            s.onError(ex);
        } else {
            s.onComplete();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean add(MulticastSubscription<T> inner) {
        MulticastSubscription<T>[] a;
        MulticastSubscription<T>[] b;
        do {
            a = (MulticastSubscription[]) this.subscribers.get();
            if (a == TERMINATED) {
                return false;
            }
            int n = a.length;
            b = new MulticastSubscription[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = inner;
        } while (!this.subscribers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: package-private */
    public void remove(MulticastSubscription<T> inner) {
        while (true) {
            MulticastSubscription<T>[] a = (MulticastSubscription[]) this.subscribers.get();
            int n = a.length;
            if (n != 0) {
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (a[i] == inner) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j < 0) {
                    return;
                }
                if (n != 1) {
                    MulticastSubscription<T>[] b = new MulticastSubscription[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    if (this.subscribers.compareAndSet(a, b)) {
                        return;
                    }
                } else if (this.refcount) {
                    if (this.subscribers.compareAndSet(a, TERMINATED)) {
                        SubscriptionHelper.cancel(this.upstream);
                        this.once.set(true);
                        return;
                    }
                } else if (this.subscribers.compareAndSet(a, EMPTY)) {
                    return;
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: CFG modification limit reached, blocks count: 188 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void drain() {
        /*
            r28 = this;
            r0 = r28
            java.util.concurrent.atomic.AtomicInteger r0 = r0.wip
            r23 = r0
            int r23 = r23.getAndIncrement()
            if (r23 == 0) goto L_0x000d
        L_0x000c:
            return
        L_0x000d:
            r14 = 1
            r0 = r28
            java.util.concurrent.atomic.AtomicReference<io.reactivex.processors.MulticastProcessor$MulticastSubscription<T>[]> r0 = r0.subscribers
            r17 = r0
            r0 = r28
            int r7 = r0.consumed
            r0 = r28
            int r13 = r0.limit
            r0 = r28
            int r11 = r0.fusionMode
        L_0x0020:
            r0 = r28
            io.reactivex.internal.fuseable.SimpleQueue<T> r0 = r0.queue
            r16 = r0
            if (r16 == 0) goto L_0x019e
            java.lang.Object r5 = r17.get()
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r5 = (io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r5
            int r15 = r5.length
            if (r15 == 0) goto L_0x019e
            r18 = -1
            int r0 = r5.length
            r24 = r0
            r23 = 0
        L_0x0038:
            r0 = r23
            r1 = r24
            if (r0 >= r1) goto L_0x0089
            r4 = r5[r23]
            long r20 = r4.get()
            r26 = 0
            int r25 = (r20 > r26 ? 1 : (r20 == r26 ? 0 : -1))
            if (r25 < 0) goto L_0x0056
            r26 = -1
            int r25 = (r18 > r26 ? 1 : (r18 == r26 ? 0 : -1))
            if (r25 != 0) goto L_0x0059
            long r0 = r4.emitted
            r26 = r0
            long r18 = r20 - r26
        L_0x0056:
            int r23 = r23 + 1
            goto L_0x0038
        L_0x0059:
            long r0 = r4.emitted
            r26 = r0
            long r26 = r20 - r26
            r0 = r18
            r2 = r26
            long r18 = java.lang.Math.min(r0, r2)
            goto L_0x0056
        L_0x0068:
            r24 = 1
            long r18 = r18 - r24
            r23 = 1
            r0 = r23
            if (r11 == r0) goto L_0x0089
            int r7 = r7 + 1
            if (r7 != r13) goto L_0x0089
            r7 = 0
            r0 = r28
            java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r0 = r0.upstream
            r23 = r0
            java.lang.Object r23 = r23.get()
            org.reactivestreams.Subscription r23 = (org.reactivestreams.Subscription) r23
            long r0 = (long) r13
            r24 = r0
            r23.request(r24)
        L_0x0089:
            r24 = 0
            int r23 = (r18 > r24 ? 1 : (r18 == r24 ? 0 : -1))
            if (r23 <= 0) goto L_0x011a
            java.lang.Object r6 = r17.get()
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r6 = (io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r6
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r23 = io.reactivex.processors.MulticastProcessor.TERMINATED
            r0 = r23
            if (r6 != r0) goto L_0x00a0
            r16.clear()
            goto L_0x000c
        L_0x00a0:
            if (r5 != r6) goto L_0x0020
            r0 = r28
            boolean r8 = r0.done
            java.lang.Object r22 = r16.poll()     // Catch:{ Throwable -> 0x00d8 }
        L_0x00aa:
            if (r22 != 0) goto L_0x00f5
            r9 = 1
        L_0x00ad:
            if (r8 == 0) goto L_0x0118
            if (r9 == 0) goto L_0x0118
            r0 = r28
            java.lang.Throwable r10 = r0.error
            if (r10 == 0) goto L_0x00f7
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r23 = io.reactivex.processors.MulticastProcessor.TERMINATED
            r0 = r17
            r1 = r23
            java.lang.Object r23 = r0.getAndSet(r1)
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r23 = (io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r23
            r0 = r23
            int r0 = r0.length
            r25 = r0
            r24 = 0
        L_0x00ca:
            r0 = r24
            r1 = r25
            if (r0 >= r1) goto L_0x000c
            r12 = r23[r24]
            r12.onError(r10)
            int r24 = r24 + 1
            goto L_0x00ca
        L_0x00d8:
            r10 = move-exception
            io.reactivex.exceptions.Exceptions.throwIfFatal(r10)
            r0 = r28
            java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r0 = r0.upstream
            r23 = r0
            io.reactivex.internal.subscriptions.SubscriptionHelper.cancel(r23)
            r8 = 1
            r22 = 0
            r0 = r28
            r0.error = r10
            r23 = 1
            r0 = r23
            r1 = r28
            r1.done = r0
            goto L_0x00aa
        L_0x00f5:
            r9 = 0
            goto L_0x00ad
        L_0x00f7:
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r23 = io.reactivex.processors.MulticastProcessor.TERMINATED
            r0 = r17
            r1 = r23
            java.lang.Object r23 = r0.getAndSet(r1)
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r23 = (io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r23
            r0 = r23
            int r0 = r0.length
            r25 = r0
            r24 = 0
        L_0x010a:
            r0 = r24
            r1 = r25
            if (r0 >= r1) goto L_0x000c
            r12 = r23[r24]
            r12.onComplete()
            int r24 = r24 + 1
            goto L_0x010a
        L_0x0118:
            if (r9 == 0) goto L_0x0131
        L_0x011a:
            r24 = 0
            int r23 = (r18 > r24 ? 1 : (r18 == r24 ? 0 : -1))
            if (r23 != 0) goto L_0x019e
            java.lang.Object r6 = r17.get()
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r6 = (io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r6
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r23 = io.reactivex.processors.MulticastProcessor.TERMINATED
            r0 = r23
            if (r6 != r0) goto L_0x0146
            r16.clear()
            goto L_0x000c
        L_0x0131:
            int r0 = r5.length
            r24 = r0
            r23 = 0
        L_0x0136:
            r0 = r23
            r1 = r24
            if (r0 >= r1) goto L_0x0068
            r12 = r5[r23]
            r0 = r22
            r12.onNext(r0)
            int r23 = r23 + 1
            goto L_0x0136
        L_0x0146:
            if (r5 != r6) goto L_0x0020
            r0 = r28
            boolean r0 = r0.done
            r23 = r0
            if (r23 == 0) goto L_0x019e
            boolean r23 = r16.isEmpty()
            if (r23 == 0) goto L_0x019e
            r0 = r28
            java.lang.Throwable r10 = r0.error
            if (r10 == 0) goto L_0x017d
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r23 = io.reactivex.processors.MulticastProcessor.TERMINATED
            r0 = r17
            r1 = r23
            java.lang.Object r23 = r0.getAndSet(r1)
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r23 = (io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r23
            r0 = r23
            int r0 = r0.length
            r25 = r0
            r24 = 0
        L_0x016f:
            r0 = r24
            r1 = r25
            if (r0 >= r1) goto L_0x000c
            r12 = r23[r24]
            r12.onError(r10)
            int r24 = r24 + 1
            goto L_0x016f
        L_0x017d:
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r23 = io.reactivex.processors.MulticastProcessor.TERMINATED
            r0 = r17
            r1 = r23
            java.lang.Object r23 = r0.getAndSet(r1)
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r23 = (io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r23
            r0 = r23
            int r0 = r0.length
            r25 = r0
            r24 = 0
        L_0x0190:
            r0 = r24
            r1 = r25
            if (r0 >= r1) goto L_0x000c
            r12 = r23[r24]
            r12.onComplete()
            int r24 = r24 + 1
            goto L_0x0190
        L_0x019e:
            r0 = r28
            java.util.concurrent.atomic.AtomicInteger r0 = r0.wip
            r23 = r0
            int r0 = -r14
            r24 = r0
            int r14 = r23.addAndGet(r24)
            if (r14 != 0) goto L_0x0020
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.processors.MulticastProcessor.drain():void");
    }

    static final class MulticastSubscription<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = -363282618957264509L;
        final Subscriber<? super T> downstream;
        long emitted;
        final MulticastProcessor<T> parent;

        MulticastSubscription(Subscriber<? super T> actual, MulticastProcessor<T> parent2) {
            this.downstream = actual;
            this.parent = parent2;
        }

        public void request(long n) {
            long r;
            long u;
            if (SubscriptionHelper.validate(n)) {
                do {
                    r = get();
                    if (r != Long.MIN_VALUE && r != LongCompanionObject.MAX_VALUE) {
                        u = r + n;
                        if (u < 0) {
                            u = LongCompanionObject.MAX_VALUE;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, u));
                this.parent.drain();
            }
        }

        public void cancel() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
            }
        }

        /* access modifiers changed from: package-private */
        public void onNext(T t) {
            if (get() != Long.MIN_VALUE) {
                this.emitted++;
                this.downstream.onNext(t);
            }
        }

        /* access modifiers changed from: package-private */
        public void onError(Throwable t) {
            if (get() != Long.MIN_VALUE) {
                this.downstream.onError(t);
            }
        }

        /* access modifiers changed from: package-private */
        public void onComplete() {
            if (get() != Long.MIN_VALUE) {
                this.downstream.onComplete();
            }
        }
    }
}
