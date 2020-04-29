package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowablePublish<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T>, FlowablePublishClassic<T> {
    static final long CANCELLED = Long.MIN_VALUE;
    final int bufferSize;
    final AtomicReference<PublishSubscriber<T>> current;
    final Publisher<T> onSubscribe;
    final Flowable<T> source;

    public static <T> ConnectableFlowable<T> create(Flowable flowable, int bufferSize2) {
        AtomicReference<PublishSubscriber<T>> curr = new AtomicReference<>();
        return RxJavaPlugins.onAssembly((ConnectableFlowable) new FlowablePublish(new FlowablePublisher<>(curr, bufferSize2), flowable, curr, bufferSize2));
    }

    private FlowablePublish(Publisher<T> onSubscribe2, Flowable<T> source2, AtomicReference<PublishSubscriber<T>> current2, int bufferSize2) {
        this.onSubscribe = onSubscribe2;
        this.source = source2;
        this.current = current2;
        this.bufferSize = bufferSize2;
    }

    public Publisher<T> source() {
        return this.source;
    }

    public int publishBufferSize() {
        return this.bufferSize;
    }

    public Publisher<T> publishSource() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.onSubscribe.subscribe(s);
    }

    public void connect(Consumer<? super Disposable> connection) {
        PublishSubscriber<T> ps;
        boolean doConnect = true;
        while (true) {
            ps = this.current.get();
            if (ps != null && !ps.isDisposed()) {
                break;
            }
            PublishSubscriber<T> u = new PublishSubscriber<>(this.current, this.bufferSize);
            if (this.current.compareAndSet(ps, u)) {
                ps = u;
                break;
            }
        }
        if (ps.shouldConnect.get() || !ps.shouldConnect.compareAndSet(false, true)) {
            doConnect = false;
        }
        try {
            connection.accept(ps);
            if (doConnect) {
                this.source.subscribe((FlowableSubscriber) ps);
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    static final class PublishSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Disposable {
        static final InnerSubscriber[] EMPTY = new InnerSubscriber[0];
        static final InnerSubscriber[] TERMINATED = new InnerSubscriber[0];
        private static final long serialVersionUID = -202316842419149694L;
        final int bufferSize;
        final AtomicReference<PublishSubscriber<T>> current;
        volatile SimpleQueue<T> queue;
        final AtomicBoolean shouldConnect;
        int sourceMode;
        final AtomicReference<InnerSubscriber<T>[]> subscribers = new AtomicReference<>(EMPTY);
        volatile Object terminalEvent;
        final AtomicReference<Subscription> upstream = new AtomicReference<>();

        PublishSubscriber(AtomicReference<PublishSubscriber<T>> current2, int bufferSize2) {
            this.current = current2;
            this.shouldConnect = new AtomicBoolean();
            this.bufferSize = bufferSize2;
        }

        public void dispose() {
            if (this.subscribers.get() != TERMINATED && ((InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED)) != TERMINATED) {
                this.current.compareAndSet(this, null);
                SubscriptionHelper.cancel(this.upstream);
            }
        }

        public boolean isDisposed() {
            return this.subscribers.get() == TERMINATED;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this.upstream, s)) {
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> qs = (QueueSubscription) s;
                    int m = qs.requestFusion(7);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = qs;
                        this.terminalEvent = NotificationLite.complete();
                        dispatch();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
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
            if (this.sourceMode != 0 || this.queue.offer(t)) {
                dispatch();
            } else {
                onError(new MissingBackpressureException("Prefetch queue is full?!"));
            }
        }

        public void onError(Throwable e) {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.error(e);
                dispatch();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.complete();
                dispatch();
            }
        }

        /* access modifiers changed from: package-private */
        public boolean add(InnerSubscriber<T> producer) {
            InnerSubscriber<T>[] c;
            InnerSubscriber<T>[] u;
            do {
                c = (InnerSubscriber[]) this.subscribers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerSubscriber[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.subscribers.compareAndSet(c, u));
            return true;
        }

        /* access modifiers changed from: package-private */
        public void remove(InnerSubscriber<T> producer) {
            InnerSubscriber<T>[] c;
            InnerSubscriber<T>[] u;
            do {
                c = (InnerSubscriber[]) this.subscribers.get();
                int len = c.length;
                if (len != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= len) {
                            break;
                        } else if (c[i].equals(producer)) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j < 0) {
                        return;
                    }
                    if (len == 1) {
                        u = EMPTY;
                    } else {
                        u = new InnerSubscriber[(len - 1)];
                        System.arraycopy(c, 0, u, 0, j);
                        System.arraycopy(c, j + 1, u, j, (len - j) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(c, u));
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(Object term, boolean empty) {
            int i = 0;
            if (term != null) {
                if (!NotificationLite.isComplete(term)) {
                    Throwable t = NotificationLite.getError(term);
                    this.current.compareAndSet(this, null);
                    InnerSubscriber<?>[] a = (InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED);
                    if (a.length != 0) {
                        int length = a.length;
                        while (i < length) {
                            a[i].child.onError(t);
                            i++;
                        }
                    } else {
                        RxJavaPlugins.onError(t);
                    }
                    return true;
                } else if (empty) {
                    this.current.compareAndSet(this, null);
                    InnerSubscriber<?>[] innerSubscriberArr = (InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED);
                    int length2 = innerSubscriberArr.length;
                    while (i < length2) {
                        innerSubscriberArr[i].child.onComplete();
                        i++;
                    }
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x01a2, code lost:
            if (r5 == 0) goto L_0x0014;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x01b0, code lost:
            if (r32.sourceMode == 1) goto L_0x0014;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:77:0x01b2, code lost:
            r32.upstream.get().request((long) r5);
         */
        /* JADX WARNING: Removed duplicated region for block: B:37:0x00d3  */
        /* JADX WARNING: Removed duplicated region for block: B:93:0x00ec A[EDGE_INSN: B:93:0x00ec->B:45:0x00ec ?: BREAK  , SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void dispatch() {
            /*
                r32 = this;
                int r25 = r32.getAndIncrement()
                if (r25 == 0) goto L_0x0007
            L_0x0006:
                return
            L_0x0007:
                r13 = 1
                r0 = r32
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowablePublish$InnerSubscriber<T>[]> r0 = r0.subscribers
                r20 = r0
                java.lang.Object r16 = r20.get()
                io.reactivex.internal.operators.flowable.FlowablePublish$InnerSubscriber[] r16 = (io.reactivex.internal.operators.flowable.FlowablePublish.InnerSubscriber[]) r16
            L_0x0014:
                r0 = r32
                java.lang.Object r0 = r0.terminalEvent
                r22 = r0
                r0 = r32
                io.reactivex.internal.fuseable.SimpleQueue<T> r0 = r0.queue
                r17 = r0
                if (r17 == 0) goto L_0x0028
                boolean r25 = r17.isEmpty()
                if (r25 == 0) goto L_0x0066
            L_0x0028:
                r6 = 1
            L_0x0029:
                r0 = r32
                r1 = r22
                boolean r25 = r0.checkTerminated(r1, r6)
                if (r25 != 0) goto L_0x0006
                if (r6 != 0) goto L_0x0116
                r0 = r16
                int r12 = r0.length
                r14 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                r4 = 0
                r0 = r16
                int r0 = r0.length
                r26 = r0
                r25 = 0
            L_0x0045:
                r0 = r25
                r1 = r26
                if (r0 >= r1) goto L_0x006b
                r9 = r16[r25]
                long r18 = r9.get()
                r28 = -9223372036854775808
                int r27 = (r18 > r28 ? 1 : (r18 == r28 ? 0 : -1))
                if (r27 == 0) goto L_0x0068
                long r0 = r9.emitted
                r28 = r0
                long r28 = r18 - r28
                r0 = r28
                long r14 = java.lang.Math.min(r14, r0)
            L_0x0063:
                int r25 = r25 + 1
                goto L_0x0045
            L_0x0066:
                r6 = 0
                goto L_0x0029
            L_0x0068:
                int r4 = r4 + 1
                goto L_0x0063
            L_0x006b:
                if (r12 != r4) goto L_0x00cb
                r0 = r32
                java.lang.Object r0 = r0.terminalEvent
                r22 = r0
                java.lang.Object r23 = r17.poll()     // Catch:{ Throwable -> 0x00a8 }
            L_0x0077:
                if (r23 != 0) goto L_0x00c8
                r25 = 1
            L_0x007b:
                r0 = r32
                r1 = r22
                r2 = r25
                boolean r25 = r0.checkTerminated(r1, r2)
                if (r25 != 0) goto L_0x0006
                r0 = r32
                int r0 = r0.sourceMode
                r25 = r0
                r26 = 1
                r0 = r25
                r1 = r26
                if (r0 == r1) goto L_0x0014
                r0 = r32
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r0 = r0.upstream
                r25 = r0
                java.lang.Object r25 = r25.get()
                org.reactivestreams.Subscription r25 = (org.reactivestreams.Subscription) r25
                r26 = 1
                r25.request(r26)
                goto L_0x0014
            L_0x00a8:
                r7 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r7)
                r0 = r32
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r0 = r0.upstream
                r25 = r0
                java.lang.Object r25 = r25.get()
                org.reactivestreams.Subscription r25 = (org.reactivestreams.Subscription) r25
                r25.cancel()
                java.lang.Object r22 = io.reactivex.internal.util.NotificationLite.error(r7)
                r0 = r22
                r1 = r32
                r1.terminalEvent = r0
                r23 = 0
                goto L_0x0077
            L_0x00c8:
                r25 = 0
                goto L_0x007b
            L_0x00cb:
                r5 = 0
            L_0x00cc:
                long r0 = (long) r5
                r26 = r0
                int r25 = (r26 > r14 ? 1 : (r26 == r14 ? 0 : -1))
                if (r25 >= 0) goto L_0x00ec
                r0 = r32
                java.lang.Object r0 = r0.terminalEvent
                r22 = r0
                java.lang.Object r23 = r17.poll()     // Catch:{ Throwable -> 0x012b }
            L_0x00dd:
                if (r23 != 0) goto L_0x014b
                r6 = 1
            L_0x00e0:
                r0 = r32
                r1 = r22
                boolean r25 = r0.checkTerminated(r1, r6)
                if (r25 != 0) goto L_0x0006
                if (r6 == 0) goto L_0x014d
            L_0x00ec:
                if (r5 == 0) goto L_0x010e
                r0 = r32
                int r0 = r0.sourceMode
                r25 = r0
                r26 = 1
                r0 = r25
                r1 = r26
                if (r0 == r1) goto L_0x010e
                r0 = r32
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r0 = r0.upstream
                r25 = r0
                java.lang.Object r25 = r25.get()
                org.reactivestreams.Subscription r25 = (org.reactivestreams.Subscription) r25
                long r0 = (long) r5
                r26 = r0
                r25.request(r26)
            L_0x010e:
                r26 = 0
                int r25 = (r14 > r26 ? 1 : (r14 == r26 ? 0 : -1))
                if (r25 == 0) goto L_0x0116
                if (r6 == 0) goto L_0x0014
            L_0x0116:
                int r0 = -r13
                r25 = r0
                r0 = r32
                r1 = r25
                int r13 = r0.addAndGet(r1)
                if (r13 == 0) goto L_0x0006
                java.lang.Object r16 = r20.get()
                io.reactivex.internal.operators.flowable.FlowablePublish$InnerSubscriber[] r16 = (io.reactivex.internal.operators.flowable.FlowablePublish.InnerSubscriber[]) r16
                goto L_0x0014
            L_0x012b:
                r7 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r7)
                r0 = r32
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r0 = r0.upstream
                r25 = r0
                java.lang.Object r25 = r25.get()
                org.reactivestreams.Subscription r25 = (org.reactivestreams.Subscription) r25
                r25.cancel()
                java.lang.Object r22 = io.reactivex.internal.util.NotificationLite.error(r7)
                r0 = r22
                r1 = r32
                r1.terminalEvent = r0
                r23 = 0
                goto L_0x00dd
            L_0x014b:
                r6 = 0
                goto L_0x00e0
            L_0x014d:
                java.lang.Object r24 = io.reactivex.internal.util.NotificationLite.getValue(r23)
                r21 = 0
                r0 = r16
                int r0 = r0.length
                r26 = r0
                r25 = 0
            L_0x015a:
                r0 = r25
                r1 = r26
                if (r0 >= r1) goto L_0x0192
                r9 = r16[r25]
                long r10 = r9.get()
                r28 = -9223372036854775808
                int r27 = (r10 > r28 ? 1 : (r10 == r28 ? 0 : -1))
                if (r27 == 0) goto L_0x018f
                r28 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r27 = (r10 > r28 ? 1 : (r10 == r28 ? 0 : -1))
                if (r27 == 0) goto L_0x0181
                long r0 = r9.emitted
                r28 = r0
                r30 = 1
                long r28 = r28 + r30
                r0 = r28
                r9.emitted = r0
            L_0x0181:
                org.reactivestreams.Subscriber<? super T> r0 = r9.child
                r27 = r0
                r0 = r27
                r1 = r24
                r0.onNext(r1)
            L_0x018c:
                int r25 = r25 + 1
                goto L_0x015a
            L_0x018f:
                r21 = 1
                goto L_0x018c
            L_0x0192:
                int r5 = r5 + 1
                java.lang.Object r8 = r20.get()
                io.reactivex.internal.operators.flowable.FlowablePublish$InnerSubscriber[] r8 = (io.reactivex.internal.operators.flowable.FlowablePublish.InnerSubscriber[]) r8
                if (r21 != 0) goto L_0x01a0
                r0 = r16
                if (r8 == r0) goto L_0x00cc
            L_0x01a0:
                r16 = r8
                if (r5 == 0) goto L_0x0014
                r0 = r32
                int r0 = r0.sourceMode
                r25 = r0
                r26 = 1
                r0 = r25
                r1 = r26
                if (r0 == r1) goto L_0x0014
                r0 = r32
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r0 = r0.upstream
                r25 = r0
                java.lang.Object r25 = r25.get()
                org.reactivestreams.Subscription r25 = (org.reactivestreams.Subscription) r25
                long r0 = (long) r5
                r26 = r0
                r25.request(r26)
                goto L_0x0014
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowablePublish.PublishSubscriber.dispatch():void");
        }
    }

    static final class InnerSubscriber<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        long emitted;
        volatile PublishSubscriber<T> parent;

        InnerSubscriber(Subscriber<? super T> child2) {
            this.child = child2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.addCancel(this, n);
                PublishSubscriber<T> p = this.parent;
                if (p != null) {
                    p.dispatch();
                }
            }
        }

        public void cancel() {
            PublishSubscriber<T> p;
            if (get() != Long.MIN_VALUE && getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE && (p = this.parent) != null) {
                p.remove(this);
                p.dispatch();
            }
        }
    }

    static final class FlowablePublisher<T> implements Publisher<T> {
        private final int bufferSize;
        private final AtomicReference<PublishSubscriber<T>> curr;

        FlowablePublisher(AtomicReference<PublishSubscriber<T>> curr2, int bufferSize2) {
            this.curr = curr2;
            this.bufferSize = bufferSize2;
        }

        public void subscribe(Subscriber<? super T> child) {
            PublishSubscriber<T> r;
            InnerSubscriber<T> inner = new InnerSubscriber<>(child);
            child.onSubscribe(inner);
            while (true) {
                r = this.curr.get();
                if (r == null || r.isDisposed()) {
                    PublishSubscriber<T> u = new PublishSubscriber<>(this.curr, this.bufferSize);
                    if (this.curr.compareAndSet(r, u)) {
                        r = u;
                    } else {
                        continue;
                    }
                }
                if (r.add(inner)) {
                    break;
                }
            }
            if (inner.get() == Long.MIN_VALUE) {
                r.remove(inner);
            } else {
                inner.parent = r;
            }
            r.dispatch();
        }
    }
}
