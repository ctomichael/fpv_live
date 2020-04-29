package io.reactivex.internal.operators.flowable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.ResettableConnectable;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowablePublishAlt<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T>, ResettableConnectable {
    final int bufferSize;
    final AtomicReference<PublishConnection<T>> current = new AtomicReference<>();
    final Publisher<T> source;

    public FlowablePublishAlt(Publisher<T> source2, int bufferSize2) {
        this.source = source2;
        this.bufferSize = bufferSize2;
    }

    public Publisher<T> source() {
        return this.source;
    }

    public int publishBufferSize() {
        return this.bufferSize;
    }

    public void connect(Consumer<? super Disposable> connection) {
        PublishConnection<T> conn;
        while (true) {
            conn = this.current.get();
            if (conn != null && !conn.isDisposed()) {
                break;
            }
            PublishConnection<T> fresh = new PublishConnection<>(this.current, this.bufferSize);
            if (this.current.compareAndSet(conn, fresh)) {
                conn = fresh;
                break;
            }
        }
        boolean doConnect = !conn.connect.get() && conn.connect.compareAndSet(false, true);
        try {
            connection.accept(conn);
            if (doConnect) {
                this.source.subscribe(conn);
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        PublishConnection<T> conn;
        while (true) {
            conn = this.current.get();
            if (conn != null) {
                break;
            }
            PublishConnection<T> fresh = new PublishConnection<>(this.current, this.bufferSize);
            if (this.current.compareAndSet(conn, fresh)) {
                conn = fresh;
                break;
            }
        }
        InnerSubscription<T> inner = new InnerSubscription<>(s, conn);
        s.onSubscribe(inner);
        if (!conn.add(inner)) {
            Throwable ex = conn.error;
            if (ex != null) {
                s.onError(ex);
            } else {
                s.onComplete();
            }
        } else if (inner.isCancelled()) {
            conn.remove(inner);
        } else {
            conn.drain();
        }
    }

    public void resetIf(Disposable connection) {
        this.current.compareAndSet((PublishConnection) connection, null);
    }

    static final class PublishConnection<T> extends AtomicInteger implements FlowableSubscriber<T>, Disposable {
        static final InnerSubscription[] EMPTY = new InnerSubscription[0];
        static final InnerSubscription[] TERMINATED = new InnerSubscription[0];
        private static final long serialVersionUID = -1672047311619175801L;
        final int bufferSize;
        final AtomicBoolean connect = new AtomicBoolean();
        int consumed;
        final AtomicReference<PublishConnection<T>> current;
        volatile boolean done;
        Throwable error;
        volatile SimpleQueue<T> queue;
        int sourceMode;
        final AtomicReference<InnerSubscription<T>[]> subscribers;
        final AtomicReference<Subscription> upstream = new AtomicReference<>();

        PublishConnection(AtomicReference<PublishConnection<T>> current2, int bufferSize2) {
            this.current = current2;
            this.bufferSize = bufferSize2;
            this.subscribers = new AtomicReference<>(EMPTY);
        }

        public void dispose() {
            this.subscribers.getAndSet(TERMINATED);
            this.current.compareAndSet(this, null);
            SubscriptionHelper.cancel(this.upstream);
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
                        this.done = true;
                        drain();
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
                drain();
            } else {
                onError(new MissingBackpressureException("Prefetch queue is full?!"));
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
            this.done = true;
            drain();
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x00a1, code lost:
            if (checkTerminated(r26.done, r16.isEmpty()) != false) goto L_0x0006;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r26 = this;
                int r20 = r26.getAndIncrement()
                if (r20 == 0) goto L_0x0007
            L_0x0006:
                return
            L_0x0007:
                r13 = 1
                r0 = r26
                io.reactivex.internal.fuseable.SimpleQueue<T> r0 = r0.queue
                r16 = r0
                r0 = r26
                int r5 = r0.consumed
                r0 = r26
                int r0 = r0.bufferSize
                r20 = r0
                r0 = r26
                int r0 = r0.bufferSize
                r21 = r0
                int r21 = r21 >> 2
                int r12 = r20 - r21
                r0 = r26
                int r0 = r0.sourceMode
                r20 = r0
                r21 = 1
                r0 = r20
                r1 = r21
                if (r0 == r1) goto L_0x006c
                r4 = 1
            L_0x0031:
                if (r16 == 0) goto L_0x00a3
                r14 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                r9 = 0
                r0 = r26
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowablePublishAlt$InnerSubscription<T>[]> r0 = r0.subscribers
                r20 = r0
                java.lang.Object r11 = r20.get()
                io.reactivex.internal.operators.flowable.FlowablePublishAlt$InnerSubscription[] r11 = (io.reactivex.internal.operators.flowable.FlowablePublishAlt.InnerSubscription[]) r11
                int r0 = r11.length
                r21 = r0
                r20 = 0
            L_0x004a:
                r0 = r20
                r1 = r21
                if (r0 >= r1) goto L_0x006e
                r10 = r11[r20]
                long r18 = r10.get()
                r22 = -9223372036854775808
                int r22 = (r18 > r22 ? 1 : (r18 == r22 ? 0 : -1))
                if (r22 == 0) goto L_0x0069
                r9 = 1
                long r0 = r10.emitted
                r22 = r0
                long r22 = r18 - r22
                r0 = r22
                long r14 = java.lang.Math.min(r0, r14)
            L_0x0069:
                int r20 = r20 + 1
                goto L_0x004a
            L_0x006c:
                r4 = 0
                goto L_0x0031
            L_0x006e:
                if (r9 != 0) goto L_0x0072
                r14 = 0
            L_0x0072:
                r20 = 0
                int r20 = (r14 > r20 ? 1 : (r14 == r20 ? 0 : -1))
                if (r20 == 0) goto L_0x008d
                r0 = r26
                boolean r6 = r0.done
                java.lang.Object r17 = r16.poll()     // Catch:{ Throwable -> 0x00be }
                if (r17 != 0) goto L_0x00e3
                r7 = 1
            L_0x0083:
                r0 = r26
                boolean r20 = r0.checkTerminated(r6, r7)
                if (r20 != 0) goto L_0x0006
                if (r7 == 0) goto L_0x00e5
            L_0x008d:
                r0 = r26
                boolean r0 = r0.done
                r20 = r0
                boolean r21 = r16.isEmpty()
                r0 = r26
                r1 = r20
                r2 = r21
                boolean r20 = r0.checkTerminated(r1, r2)
                if (r20 != 0) goto L_0x0006
            L_0x00a3:
                r0 = r26
                r0.consumed = r5
                int r0 = -r13
                r20 = r0
                r0 = r26
                r1 = r20
                int r13 = r0.addAndGet(r1)
                if (r13 == 0) goto L_0x0006
                if (r16 != 0) goto L_0x0031
                r0 = r26
                io.reactivex.internal.fuseable.SimpleQueue<T> r0 = r0.queue
                r16 = r0
                goto L_0x0031
            L_0x00be:
                r8 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r8)
                r0 = r26
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r0 = r0.upstream
                r20 = r0
                java.lang.Object r20 = r20.get()
                org.reactivestreams.Subscription r20 = (org.reactivestreams.Subscription) r20
                r20.cancel()
                r16.clear()
                r20 = 1
                r0 = r20
                r1 = r26
                r1.done = r0
                r0 = r26
                r0.signalError(r8)
                goto L_0x0006
            L_0x00e3:
                r7 = 0
                goto L_0x0083
            L_0x00e5:
                int r0 = r11.length
                r21 = r0
                r20 = 0
            L_0x00ea:
                r0 = r20
                r1 = r21
                if (r0 >= r1) goto L_0x0112
                r10 = r11[r20]
                boolean r22 = r10.isCancelled()
                if (r22 != 0) goto L_0x010f
                org.reactivestreams.Subscriber<? super T> r0 = r10.downstream
                r22 = r0
                r0 = r22
                r1 = r17
                r0.onNext(r1)
                long r0 = r10.emitted
                r22 = r0
                r24 = 1
                long r22 = r22 + r24
                r0 = r22
                r10.emitted = r0
            L_0x010f:
                int r20 = r20 + 1
                goto L_0x00ea
            L_0x0112:
                if (r4 == 0) goto L_0x012f
                int r5 = r5 + 1
                if (r5 != r12) goto L_0x012f
                r5 = 0
                r0 = r26
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r0 = r0.upstream
                r20 = r0
                java.lang.Object r20 = r20.get()
                org.reactivestreams.Subscription r20 = (org.reactivestreams.Subscription) r20
                long r0 = (long) r12
                r22 = r0
                r0 = r20
                r1 = r22
                r0.request(r1)
            L_0x012f:
                r20 = 1
                long r14 = r14 - r20
                r0 = r26
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowablePublishAlt$InnerSubscription<T>[]> r0 = r0.subscribers
                r20 = r0
                java.lang.Object r20 = r20.get()
                r0 = r20
                if (r11 == r0) goto L_0x0072
                goto L_0x0031
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowablePublishAlt.PublishConnection.drain():void");
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean isDone, boolean isEmpty) {
            if (!isDone || !isEmpty) {
                return false;
            }
            Throwable ex = this.error;
            if (ex != null) {
                signalError(ex);
            } else {
                InnerSubscription<T>[] innerSubscriptionArr = (InnerSubscription[]) this.subscribers.getAndSet(TERMINATED);
                for (InnerSubscription<T> inner : innerSubscriptionArr) {
                    if (!inner.isCancelled()) {
                        inner.downstream.onComplete();
                    }
                }
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public void signalError(Throwable ex) {
            InnerSubscription<T>[] innerSubscriptionArr = (InnerSubscription[]) this.subscribers.getAndSet(TERMINATED);
            for (InnerSubscription<T> inner : innerSubscriptionArr) {
                if (!inner.isCancelled()) {
                    inner.downstream.onError(ex);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean add(InnerSubscription<T> inner) {
            InnerSubscription<T>[] c;
            InnerSubscription<T>[] u;
            do {
                c = (InnerSubscription[]) this.subscribers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerSubscription[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = inner;
            } while (!this.subscribers.compareAndSet(c, u));
            return true;
        }

        /* access modifiers changed from: package-private */
        public void remove(InnerSubscription<T> inner) {
            InnerSubscription<T>[] c;
            InnerSubscription<T>[] u;
            do {
                c = (InnerSubscription[]) this.subscribers.get();
                int len = c.length;
                if (len != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= len) {
                            break;
                        } else if (c[i] == inner) {
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
                        u = new InnerSubscription[(len - 1)];
                        System.arraycopy(c, 0, u, 0, j);
                        System.arraycopy(c, j + 1, u, j, (len - j) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(c, u));
        }
    }

    static final class InnerSubscription<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = 2845000326761540265L;
        final Subscriber<? super T> downstream;
        long emitted;
        final PublishConnection<T> parent;

        InnerSubscription(Subscriber<? super T> downstream2, PublishConnection<T> parent2) {
            this.downstream = downstream2;
            this.parent = parent2;
        }

        public void request(long n) {
            BackpressureHelper.addCancel(this, n);
            this.parent.drain();
        }

        public void cancel() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
                this.parent.drain();
            }
        }

        public boolean isCancelled() {
            return get() == Long.MIN_VALUE;
        }
    }
}
