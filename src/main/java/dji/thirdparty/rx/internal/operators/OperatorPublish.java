package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.MissingBackpressureException;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.internal.util.RxRingBuffer;
import dji.thirdparty.rx.internal.util.SynchronizedQueue;
import dji.thirdparty.rx.internal.util.unsafe.SpscArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess;
import dji.thirdparty.rx.observables.ConnectableObservable;
import dji.thirdparty.rx.subscriptions.Subscriptions;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorPublish<T> extends ConnectableObservable<T> {
    final AtomicReference<PublishSubscriber<T>> current;
    final Observable<? extends T> source;

    public static <T> ConnectableObservable<T> create(Observable observable) {
        final AtomicReference<PublishSubscriber<T>> curr = new AtomicReference<>();
        return new OperatorPublish(new Observable.OnSubscribe<T>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorPublish.AnonymousClass1 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((Subscriber) ((Subscriber) x0));
            }

            public void call(Subscriber<? super T> child) {
                while (true) {
                    PublishSubscriber<T> r = (PublishSubscriber) curr.get();
                    if (r == null || r.isUnsubscribed()) {
                        PublishSubscriber<T> u = new PublishSubscriber<>(curr);
                        u.init();
                        if (curr.compareAndSet(r, u)) {
                            r = u;
                        } else {
                            continue;
                        }
                    }
                    InnerProducer<T> inner = new InnerProducer<>(r, child);
                    if (r.add((InnerProducer) inner)) {
                        child.add(inner);
                        child.setProducer(inner);
                        return;
                    }
                }
            }
        }, observable, curr);
    }

    public static <T, R> Observable<R> create(Observable<? extends T> source2, Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return create(source2, selector, false);
    }

    public static <T, R> Observable<R> create(final Observable<? extends T> source2, final Func1<? super Observable<T>, ? extends Observable<R>> selector, final boolean delayError) {
        return create(new Observable.OnSubscribe<R>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorPublish.AnonymousClass2 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((Subscriber) ((Subscriber) x0));
            }

            public void call(final Subscriber<? super R> child) {
                final OnSubscribePublishMulticast<T> op = new OnSubscribePublishMulticast<>(RxRingBuffer.SIZE, delayError);
                Subscriber<R> subscriber = new Subscriber<R>() {
                    /* class dji.thirdparty.rx.internal.operators.OperatorPublish.AnonymousClass2.AnonymousClass1 */

                    public void onNext(R t) {
                        child.onNext(t);
                    }

                    public void onError(Throwable e) {
                        op.unsubscribe();
                        child.onError(e);
                    }

                    public void onCompleted() {
                        op.unsubscribe();
                        child.onCompleted();
                    }

                    public void setProducer(Producer p) {
                        child.setProducer(p);
                    }
                };
                child.add(op);
                child.add(subscriber);
                ((Observable) selector.call(Observable.create(op))).unsafeSubscribe(subscriber);
                source2.unsafeSubscribe(op.subscriber());
            }
        });
    }

    private OperatorPublish(Observable.OnSubscribe<T> onSubscribe, Observable<? extends T> source2, AtomicReference<PublishSubscriber<T>> current2) {
        super(onSubscribe);
        this.source = source2;
        this.current = current2;
    }

    public void connect(Action1<? super Subscription> connection) {
        PublishSubscriber<T> ps;
        boolean doConnect;
        while (true) {
            ps = this.current.get();
            if (ps != null && !ps.isUnsubscribed()) {
                break;
            }
            PublishSubscriber<T> u = new PublishSubscriber<>(this.current);
            u.init();
            if (this.current.compareAndSet(ps, u)) {
                ps = u;
                break;
            }
        }
        if (ps.shouldConnect.get() || !ps.shouldConnect.compareAndSet(false, true)) {
            doConnect = false;
        } else {
            doConnect = true;
        }
        connection.call(ps);
        if (doConnect) {
            this.source.unsafeSubscribe(ps);
        }
    }

    static final class PublishSubscriber<T> extends Subscriber<T> implements Subscription {
        static final InnerProducer[] EMPTY = new InnerProducer[0];
        static final InnerProducer[] TERMINATED = new InnerProducer[0];
        final AtomicReference<PublishSubscriber<T>> current;
        boolean emitting;
        boolean missed;
        final NotificationLite<T> nl;
        final AtomicReference<InnerProducer[]> producers;
        final Queue<Object> queue;
        final AtomicBoolean shouldConnect;
        volatile Object terminalEvent;

        public PublishSubscriber(AtomicReference<PublishSubscriber<T>> current2) {
            this.queue = UnsafeAccess.isUnsafeAvailable() ? new SpscArrayQueue<>(RxRingBuffer.SIZE) : new SynchronizedQueue<>(RxRingBuffer.SIZE);
            this.nl = NotificationLite.instance();
            this.producers = new AtomicReference<>(EMPTY);
            this.current = current2;
            this.shouldConnect = new AtomicBoolean();
        }

        /* access modifiers changed from: package-private */
        public void init() {
            add(Subscriptions.create(new Action0() {
                /* class dji.thirdparty.rx.internal.operators.OperatorPublish.PublishSubscriber.AnonymousClass1 */

                public void call() {
                    PublishSubscriber.this.producers.getAndSet(PublishSubscriber.TERMINATED);
                    PublishSubscriber.this.current.compareAndSet(PublishSubscriber.this, null);
                }
            }));
        }

        public void onStart() {
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            if (!this.queue.offer(this.nl.next(t))) {
                onError(new MissingBackpressureException());
            } else {
                dispatch();
            }
        }

        public void onError(Throwable e) {
            if (this.terminalEvent == null) {
                this.terminalEvent = this.nl.error(e);
                dispatch();
            }
        }

        public void onCompleted() {
            if (this.terminalEvent == null) {
                this.terminalEvent = this.nl.completed();
                dispatch();
            }
        }

        /* access modifiers changed from: package-private */
        public boolean add(InnerProducer<T> producer) {
            InnerProducer[] c;
            InnerProducer[] u;
            if (producer == null) {
                throw new NullPointerException();
            }
            do {
                c = this.producers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerProducer[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.producers.compareAndSet(c, u));
            return true;
        }

        /* access modifiers changed from: package-private */
        public void remove(InnerProducer<T> producer) {
            InnerProducer[] c;
            InnerProducer[] u;
            do {
                c = this.producers.get();
                if (c != EMPTY && c != TERMINATED) {
                    int j = -1;
                    int len = c.length;
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
                        u = new InnerProducer[(len - 1)];
                        System.arraycopy(c, 0, u, 0, j);
                        System.arraycopy(c, j + 1, u, j, (len - j) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.producers.compareAndSet(c, u));
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(Object term, boolean empty) {
            if (term != null) {
                if (!this.nl.isCompleted(term)) {
                    Throwable t = this.nl.getError(term);
                    this.current.compareAndSet(this, null);
                    try {
                        for (InnerProducer<?> ip : (InnerProducer[]) this.producers.getAndSet(TERMINATED)) {
                            ip.child.onError(t);
                        }
                        return true;
                    } finally {
                        unsubscribe();
                    }
                } else if (empty) {
                    this.current.compareAndSet(this, null);
                    try {
                        for (InnerProducer<?> ip2 : (InnerProducer[]) this.producers.getAndSet(TERMINATED)) {
                            ip2.child.onCompleted();
                        }
                        return true;
                    } finally {
                        unsubscribe();
                    }
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:100:0x012d, code lost:
            if (r6 == false) goto L_0x0026;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:101:0x012f, code lost:
            monitor-enter(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:105:0x0136, code lost:
            if (r26.missed != false) goto L_0x019e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:106:0x0138, code lost:
            r26.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:107:0x0142, code lost:
            monitor-exit(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:108:0x0143, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:109:0x0145, code lost:
            monitor-enter(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:112:?, code lost:
            r26.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:113:0x014e, code lost:
            monitor-exit(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:120:?, code lost:
            r21 = r26.nl.getValue(r20);
            r4 = r11;
            r10 = r4.length;
            r7 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:121:0x0165, code lost:
            if (r7 >= r10) goto L_0x019a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:122:0x0167, code lost:
            r8 = r4[r7];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:124:0x0171, code lost:
            if (r8.get() <= 0) goto L_0x0185;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:126:?, code lost:
            r8.child.onNext(r21);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:129:?, code lost:
            r8.produced(1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
            r18 = r26.terminalEvent;
            r6 = r26.queue.isEmpty();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:131:0x0188, code lost:
            r17 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:132:0x0189, code lost:
            r8.unsubscribe();
            dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r17, r8.child, r21);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:133:0x019a, code lost:
            r5 = r5 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:136:?, code lost:
            r26.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:137:0x01a6, code lost:
            monitor-exit(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x003e, code lost:
            if (checkTerminated(r18, r6) == false) goto L_0x0055;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0042, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0044, code lost:
            monitor-enter(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:172:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:173:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:174:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:175:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:176:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:177:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:178:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:179:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            r26.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x004d, code lost:
            monitor-exit(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0055, code lost:
            if (r6 != false) goto L_0x012f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            r11 = (dji.thirdparty.rx.internal.operators.OperatorPublish.InnerProducer[]) r26.producers.get();
            r9 = r11.length;
            r12 = kotlin.jvm.internal.LongCompanionObject.MAX_VALUE;
            r19 = 0;
            r4 = r11;
            r10 = r4.length;
            r7 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x006e, code lost:
            if (r7 >= r10) goto L_0x008c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0070, code lost:
            r14 = r4[r7].get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x007a, code lost:
            if (r14 < 0) goto L_0x0083;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x007c, code lost:
            r12 = java.lang.Math.min(r12, r14);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0080, code lost:
            r7 = r7 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0087, code lost:
            if (r14 != Long.MIN_VALUE) goto L_0x0080;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0089, code lost:
            r19 = r19 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x008e, code lost:
            if (r9 != r19) goto L_0x00df;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0090, code lost:
            r18 = r26.terminalEvent;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x00a0, code lost:
            if (r26.queue.poll() != null) goto L_0x00c3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x00a2, code lost:
            r22 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x00ae, code lost:
            if (checkTerminated(r18, r22) == false) goto L_0x00c6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x00b2, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x00b4, code lost:
            monitor-enter(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
            r26.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00bd, code lost:
            monitor-exit(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x00c3, code lost:
            r22 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
            request(1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x00d1, code lost:
            r22 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x00d2, code lost:
            if (0 == 0) goto L_0x00d4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:0x00d4, code lost:
            monitor-enter(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
            r26.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:0x00de, code lost:
            throw r22;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x00df, code lost:
            r5 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x00e5, code lost:
            if (((long) r5) >= r12) goto L_0x011b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:?, code lost:
            r18 = r26.terminalEvent;
            r20 = r26.queue.poll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:77:0x00f7, code lost:
            if (r20 != null) goto L_0x0117;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x00f9, code lost:
            r6 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:80:0x0102, code lost:
            if (checkTerminated(r18, r6) == false) goto L_0x0119;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:82:0x0106, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:83:0x0108, code lost:
            monitor-enter(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:?, code lost:
            r26.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:0x0111, code lost:
            monitor-exit(r26);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:92:0x0117, code lost:
            r6 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:93:0x0119, code lost:
            if (r6 == false) goto L_0x0154;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:0x011b, code lost:
            if (r5 <= 0) goto L_0x0127;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:97:?, code lost:
            request((long) r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:0x012b, code lost:
            if (r12 == 0) goto L_0x012f;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void dispatch() {
            /*
                r26 = this;
                monitor-enter(r26)
                r0 = r26
                boolean r0 = r0.emitting     // Catch:{ all -> 0x0052 }
                r22 = r0
                if (r22 == 0) goto L_0x0013
                r22 = 1
                r0 = r22
                r1 = r26
                r1.missed = r0     // Catch:{ all -> 0x0052 }
                monitor-exit(r26)     // Catch:{ all -> 0x0052 }
            L_0x0012:
                return
            L_0x0013:
                r22 = 1
                r0 = r22
                r1 = r26
                r1.emitting = r0     // Catch:{ all -> 0x0052 }
                r22 = 0
                r0 = r22
                r1 = r26
                r1.missed = r0     // Catch:{ all -> 0x0052 }
                monitor-exit(r26)     // Catch:{ all -> 0x0052 }
                r16 = 0
            L_0x0026:
                r0 = r26
                java.lang.Object r0 = r0.terminalEvent     // Catch:{ all -> 0x00d1 }
                r18 = r0
                r0 = r26
                java.util.Queue<java.lang.Object> r0 = r0.queue     // Catch:{ all -> 0x00d1 }
                r22 = r0
                boolean r6 = r22.isEmpty()     // Catch:{ all -> 0x00d1 }
                r0 = r26
                r1 = r18
                boolean r22 = r0.checkTerminated(r1, r6)     // Catch:{ all -> 0x00d1 }
                if (r22 == 0) goto L_0x0055
                r16 = 1
                if (r16 != 0) goto L_0x0012
                monitor-enter(r26)
                r22 = 0
                r0 = r22
                r1 = r26
                r1.emitting = r0     // Catch:{ all -> 0x004f }
                monitor-exit(r26)     // Catch:{ all -> 0x004f }
                goto L_0x0012
            L_0x004f:
                r22 = move-exception
                monitor-exit(r26)     // Catch:{ all -> 0x004f }
                throw r22
            L_0x0052:
                r22 = move-exception
                monitor-exit(r26)     // Catch:{ all -> 0x0052 }
                throw r22
            L_0x0055:
                if (r6 != 0) goto L_0x012f
                r0 = r26
                java.util.concurrent.atomic.AtomicReference<dji.thirdparty.rx.internal.operators.OperatorPublish$InnerProducer[]> r0 = r0.producers     // Catch:{ all -> 0x00d1 }
                r22 = r0
                java.lang.Object r11 = r22.get()     // Catch:{ all -> 0x00d1 }
                dji.thirdparty.rx.internal.operators.OperatorPublish$InnerProducer[] r11 = (dji.thirdparty.rx.internal.operators.OperatorPublish.InnerProducer[]) r11     // Catch:{ all -> 0x00d1 }
                int r9 = r11.length     // Catch:{ all -> 0x00d1 }
                r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                r19 = 0
                r4 = r11
                int r10 = r4.length     // Catch:{ all -> 0x00d1 }
                r7 = 0
            L_0x006e:
                if (r7 >= r10) goto L_0x008c
                r8 = r4[r7]     // Catch:{ all -> 0x00d1 }
                long r14 = r8.get()     // Catch:{ all -> 0x00d1 }
                r22 = 0
                int r22 = (r14 > r22 ? 1 : (r14 == r22 ? 0 : -1))
                if (r22 < 0) goto L_0x0083
                long r12 = java.lang.Math.min(r12, r14)     // Catch:{ all -> 0x00d1 }
            L_0x0080:
                int r7 = r7 + 1
                goto L_0x006e
            L_0x0083:
                r22 = -9223372036854775808
                int r22 = (r14 > r22 ? 1 : (r14 == r22 ? 0 : -1))
                if (r22 != 0) goto L_0x0080
                int r19 = r19 + 1
                goto L_0x0080
            L_0x008c:
                r0 = r19
                if (r9 != r0) goto L_0x00df
                r0 = r26
                java.lang.Object r0 = r0.terminalEvent     // Catch:{ all -> 0x00d1 }
                r18 = r0
                r0 = r26
                java.util.Queue<java.lang.Object> r0 = r0.queue     // Catch:{ all -> 0x00d1 }
                r22 = r0
                java.lang.Object r20 = r22.poll()     // Catch:{ all -> 0x00d1 }
                if (r20 != 0) goto L_0x00c3
                r22 = 1
            L_0x00a4:
                r0 = r26
                r1 = r18
                r2 = r22
                boolean r22 = r0.checkTerminated(r1, r2)     // Catch:{ all -> 0x00d1 }
                if (r22 == 0) goto L_0x00c6
                r16 = 1
                if (r16 != 0) goto L_0x0012
                monitor-enter(r26)
                r22 = 0
                r0 = r22
                r1 = r26
                r1.emitting = r0     // Catch:{ all -> 0x00c0 }
                monitor-exit(r26)     // Catch:{ all -> 0x00c0 }
                goto L_0x0012
            L_0x00c0:
                r22 = move-exception
                monitor-exit(r26)     // Catch:{ all -> 0x00c0 }
                throw r22
            L_0x00c3:
                r22 = 0
                goto L_0x00a4
            L_0x00c6:
                r22 = 1
                r0 = r26
                r1 = r22
                r0.request(r1)     // Catch:{ all -> 0x00d1 }
                goto L_0x0026
            L_0x00d1:
                r22 = move-exception
                if (r16 != 0) goto L_0x00de
                monitor-enter(r26)
                r23 = 0
                r0 = r23
                r1 = r26
                r1.emitting = r0     // Catch:{ all -> 0x01ac }
                monitor-exit(r26)     // Catch:{ all -> 0x01ac }
            L_0x00de:
                throw r22
            L_0x00df:
                r5 = 0
            L_0x00e0:
                long r0 = (long) r5
                r22 = r0
                int r22 = (r22 > r12 ? 1 : (r22 == r12 ? 0 : -1))
                if (r22 >= 0) goto L_0x011b
                r0 = r26
                java.lang.Object r0 = r0.terminalEvent     // Catch:{ all -> 0x00d1 }
                r18 = r0
                r0 = r26
                java.util.Queue<java.lang.Object> r0 = r0.queue     // Catch:{ all -> 0x00d1 }
                r22 = r0
                java.lang.Object r20 = r22.poll()     // Catch:{ all -> 0x00d1 }
                if (r20 != 0) goto L_0x0117
                r6 = 1
            L_0x00fa:
                r0 = r26
                r1 = r18
                boolean r22 = r0.checkTerminated(r1, r6)     // Catch:{ all -> 0x00d1 }
                if (r22 == 0) goto L_0x0119
                r16 = 1
                if (r16 != 0) goto L_0x0012
                monitor-enter(r26)
                r22 = 0
                r0 = r22
                r1 = r26
                r1.emitting = r0     // Catch:{ all -> 0x0114 }
                monitor-exit(r26)     // Catch:{ all -> 0x0114 }
                goto L_0x0012
            L_0x0114:
                r22 = move-exception
                monitor-exit(r26)     // Catch:{ all -> 0x0114 }
                throw r22
            L_0x0117:
                r6 = 0
                goto L_0x00fa
            L_0x0119:
                if (r6 == 0) goto L_0x0154
            L_0x011b:
                if (r5 <= 0) goto L_0x0127
                long r0 = (long) r5
                r22 = r0
                r0 = r26
                r1 = r22
                r0.request(r1)     // Catch:{ all -> 0x00d1 }
            L_0x0127:
                r22 = 0
                int r22 = (r12 > r22 ? 1 : (r12 == r22 ? 0 : -1))
                if (r22 == 0) goto L_0x012f
                if (r6 == 0) goto L_0x0026
            L_0x012f:
                monitor-enter(r26)     // Catch:{ all -> 0x00d1 }
                r0 = r26
                boolean r0 = r0.missed     // Catch:{ all -> 0x01a9 }
                r22 = r0
                if (r22 != 0) goto L_0x019e
                r22 = 0
                r0 = r22
                r1 = r26
                r1.emitting = r0     // Catch:{ all -> 0x01a9 }
                r16 = 1
                monitor-exit(r26)     // Catch:{ all -> 0x01a9 }
                if (r16 != 0) goto L_0x0012
                monitor-enter(r26)
                r22 = 0
                r0 = r22
                r1 = r26
                r1.emitting = r0     // Catch:{ all -> 0x0151 }
                monitor-exit(r26)     // Catch:{ all -> 0x0151 }
                goto L_0x0012
            L_0x0151:
                r22 = move-exception
                monitor-exit(r26)     // Catch:{ all -> 0x0151 }
                throw r22
            L_0x0154:
                r0 = r26
                dji.thirdparty.rx.internal.operators.NotificationLite<T> r0 = r0.nl     // Catch:{ all -> 0x00d1 }
                r22 = r0
                r0 = r22
                r1 = r20
                java.lang.Object r21 = r0.getValue(r1)     // Catch:{ all -> 0x00d1 }
                r4 = r11
                int r10 = r4.length     // Catch:{ all -> 0x00d1 }
                r7 = 0
            L_0x0165:
                if (r7 >= r10) goto L_0x019a
                r8 = r4[r7]     // Catch:{ all -> 0x00d1 }
                long r22 = r8.get()     // Catch:{ all -> 0x00d1 }
                r24 = 0
                int r22 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1))
                if (r22 <= 0) goto L_0x0185
                dji.thirdparty.rx.Subscriber<? super T> r0 = r8.child     // Catch:{ Throwable -> 0x0188 }
                r22 = r0
                r0 = r22
                r1 = r21
                r0.onNext(r1)     // Catch:{ Throwable -> 0x0188 }
                r22 = 1
                r0 = r22
                r8.produced(r0)     // Catch:{ all -> 0x00d1 }
            L_0x0185:
                int r7 = r7 + 1
                goto L_0x0165
            L_0x0188:
                r17 = move-exception
                r8.unsubscribe()     // Catch:{ all -> 0x00d1 }
                dji.thirdparty.rx.Subscriber<? super T> r0 = r8.child     // Catch:{ all -> 0x00d1 }
                r22 = r0
                r0 = r17
                r1 = r22
                r2 = r21
                dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r0, r1, r2)     // Catch:{ all -> 0x00d1 }
                goto L_0x0185
            L_0x019a:
                int r5 = r5 + 1
                goto L_0x00e0
            L_0x019e:
                r22 = 0
                r0 = r22
                r1 = r26
                r1.missed = r0     // Catch:{ all -> 0x01a9 }
                monitor-exit(r26)     // Catch:{ all -> 0x01a9 }
                goto L_0x0026
            L_0x01a9:
                r22 = move-exception
                monitor-exit(r26)     // Catch:{ all -> 0x01a9 }
                throw r22     // Catch:{ all -> 0x00d1 }
            L_0x01ac:
                r22 = move-exception
                monitor-exit(r26)     // Catch:{ all -> 0x01ac }
                throw r22
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorPublish.PublishSubscriber.dispatch():void");
        }
    }

    static final class InnerProducer<T> extends AtomicLong implements Producer, Subscription {
        static final long NOT_REQUESTED = -4611686018427387904L;
        static final long UNSUBSCRIBED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        final PublishSubscriber<T> parent;

        public InnerProducer(PublishSubscriber<T> parent2, Subscriber<? super T> child2) {
            this.parent = parent2;
            this.child = child2;
            lazySet(NOT_REQUESTED);
        }

        public void request(long n) {
            long r;
            long u;
            if (n >= 0) {
                do {
                    r = get();
                    if (r == Long.MIN_VALUE) {
                        return;
                    }
                    if (r >= 0 && n == 0) {
                        return;
                    }
                    if (r == NOT_REQUESTED) {
                        u = n;
                    } else {
                        u = r + n;
                        if (u < 0) {
                            u = LongCompanionObject.MAX_VALUE;
                        }
                    }
                } while (!compareAndSet(r, u));
                this.parent.dispatch();
            }
        }

        public long produced(long n) {
            long r;
            long u;
            if (n <= 0) {
                throw new IllegalArgumentException("Cant produce zero or less");
            }
            do {
                r = get();
                if (r == NOT_REQUESTED) {
                    throw new IllegalStateException("Produced without request");
                } else if (r == Long.MIN_VALUE) {
                    return Long.MIN_VALUE;
                } else {
                    u = r - n;
                    if (u < 0) {
                        throw new IllegalStateException("More produced (" + n + ") than requested (" + r + ")");
                    }
                }
            } while (!compareAndSet(r, u));
            return u;
        }

        public boolean isUnsubscribed() {
            return get() == Long.MIN_VALUE;
        }

        public void unsubscribe() {
            if (get() != Long.MIN_VALUE && getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
                this.parent.dispatch();
            }
        }
    }
}
