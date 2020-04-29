package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.MissingBackpressureException;
import dji.thirdparty.rx.internal.util.atomic.SpscAtomicArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.SpscArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class OnSubscribePublishMulticast<T> extends AtomicInteger implements Observable.OnSubscribe<T>, Observer<T>, Subscription {
    static final PublishProducer<?>[] EMPTY = new PublishProducer[0];
    static final PublishProducer<?>[] TERMINATED = new PublishProducer[0];
    private static final long serialVersionUID = -3741892510772238743L;
    final boolean delayError;
    volatile boolean done;
    Throwable error;
    final ParentSubscriber<T> parent;
    final int prefetch;
    volatile Producer producer;
    final Queue<T> queue;
    volatile PublishProducer<T>[] subscribers;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public OnSubscribePublishMulticast(int prefetch2, boolean delayError2) {
        if (prefetch2 <= 0) {
            throw new IllegalArgumentException("prefetch > 0 required but it was " + prefetch2);
        }
        this.prefetch = prefetch2;
        this.delayError = delayError2;
        if (UnsafeAccess.isUnsafeAvailable()) {
            this.queue = new SpscArrayQueue(prefetch2);
        } else {
            this.queue = new SpscAtomicArrayQueue(prefetch2);
        }
        this.subscribers = (PublishProducer[]) EMPTY;
        this.parent = new ParentSubscriber<>(this);
    }

    public void call(Subscriber<? super T> t) {
        PublishProducer<T> pp = new PublishProducer<>(t, this);
        t.add(pp);
        t.setProducer(pp);
        if (!add(pp)) {
            Throwable e = this.error;
            if (e != null) {
                t.onError(e);
            } else {
                t.onCompleted();
            }
        } else if (pp.isUnsubscribed()) {
            remove(pp);
        } else {
            drain();
        }
    }

    public void onNext(T t) {
        if (!this.queue.offer(t)) {
            this.parent.unsubscribe();
            this.error = new MissingBackpressureException("Queue full?!");
            this.done = true;
        }
        drain();
    }

    public void onError(Throwable e) {
        this.error = e;
        this.done = true;
        drain();
    }

    public void onCompleted() {
        this.done = true;
        drain();
    }

    /* access modifiers changed from: package-private */
    public void setProducer(Producer p) {
        this.producer = p;
        p.request((long) this.prefetch);
    }

    /* access modifiers changed from: package-private */
    public void drain() {
        if (getAndIncrement() == 0) {
            Queue<T> q = this.queue;
            int missed = 0;
            do {
                long r = LongCompanionObject.MAX_VALUE;
                PublishProducer<T>[] a = this.subscribers;
                int n = a.length;
                for (PublishProducer<T> inner : a) {
                    r = Math.min(r, inner.get());
                }
                if (n != 0) {
                    long e = 0;
                    while (e != r) {
                        boolean d = this.done;
                        T v = q.poll();
                        boolean empty = v == null;
                        if (checkTerminated(d, empty)) {
                            return;
                        }
                        if (empty) {
                            break;
                        }
                        for (PublishProducer<T> inner2 : a) {
                            inner2.actual.onNext(v);
                        }
                        e++;
                    }
                    if (e == r) {
                        if (checkTerminated(this.done, q.isEmpty())) {
                            return;
                        }
                    }
                    if (e != 0) {
                        Producer p = this.producer;
                        if (p != null) {
                            p.request(e);
                        }
                        for (PublishProducer<T> inner3 : a) {
                            BackpressureUtils.produced(inner3, e);
                        }
                    }
                }
                missed = addAndGet(-missed);
            } while (missed != 0);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean checkTerminated(boolean d, boolean empty) {
        if (d) {
            if (!this.delayError) {
                Throwable ex = this.error;
                if (ex != null) {
                    this.queue.clear();
                    for (PublishProducer<T> inner : terminate()) {
                        inner.actual.onError(ex);
                    }
                    return true;
                } else if (empty) {
                    for (PublishProducer<T> inner2 : terminate()) {
                        inner2.actual.onCompleted();
                    }
                    return true;
                }
            } else if (empty) {
                PublishProducer<T>[] a = terminate();
                Throwable ex2 = this.error;
                if (ex2 != null) {
                    for (PublishProducer<T> inner3 : a) {
                        inner3.actual.onError(ex2);
                    }
                    return true;
                }
                for (PublishProducer<T> inner4 : a) {
                    inner4.actual.onCompleted();
                }
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public PublishProducer<T>[] terminate() {
        PublishProducer<T>[] a = this.subscribers;
        if (a != TERMINATED) {
            synchronized (this) {
                a = this.subscribers;
                if (a != TERMINATED) {
                    this.subscribers = (PublishProducer[]) TERMINATED;
                }
            }
        }
        return a;
    }

    /* access modifiers changed from: package-private */
    public boolean add(PublishProducer<T> inner) {
        boolean z = false;
        if (this.subscribers != TERMINATED) {
            synchronized (this) {
                PublishProducer<T>[] a = this.subscribers;
                if (a != TERMINATED) {
                    int n = a.length;
                    PublishProducer<T>[] b = new PublishProducer[(n + 1)];
                    System.arraycopy(a, 0, b, 0, n);
                    b[n] = inner;
                    this.subscribers = b;
                    z = true;
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast.PublishProducer<T> r8) {
        /*
            r7 = this;
            dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<T>[] r0 = r7.subscribers
            dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<?>[] r5 = dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast.TERMINATED
            if (r0 == r5) goto L_0x000a
            dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<?>[] r5 = dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast.EMPTY
            if (r0 != r5) goto L_0x000b
        L_0x000a:
            return
        L_0x000b:
            monitor-enter(r7)
            dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<T>[] r0 = r7.subscribers     // Catch:{ all -> 0x0018 }
            dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<?>[] r5 = dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast.TERMINATED     // Catch:{ all -> 0x0018 }
            if (r0 == r5) goto L_0x0016
            dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<?>[] r5 = dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast.EMPTY     // Catch:{ all -> 0x0018 }
            if (r0 != r5) goto L_0x001b
        L_0x0016:
            monitor-exit(r7)     // Catch:{ all -> 0x0018 }
            goto L_0x000a
        L_0x0018:
            r5 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0018 }
            throw r5
        L_0x001b:
            r3 = -1
            int r4 = r0.length     // Catch:{ all -> 0x0018 }
            r2 = 0
        L_0x001e:
            if (r2 >= r4) goto L_0x0025
            r5 = r0[r2]     // Catch:{ all -> 0x0018 }
            if (r5 != r8) goto L_0x0029
            r3 = r2
        L_0x0025:
            if (r3 >= 0) goto L_0x002c
            monitor-exit(r7)     // Catch:{ all -> 0x0018 }
            goto L_0x000a
        L_0x0029:
            int r2 = r2 + 1
            goto L_0x001e
        L_0x002c:
            r5 = 1
            if (r4 != r5) goto L_0x0037
            dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<?>[] r1 = dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast.EMPTY     // Catch:{ all -> 0x0018 }
            dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast$PublishProducer[] r1 = (dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast.PublishProducer[]) r1     // Catch:{ all -> 0x0018 }
        L_0x0033:
            r7.subscribers = r1     // Catch:{ all -> 0x0018 }
            monitor-exit(r7)     // Catch:{ all -> 0x0018 }
            goto L_0x000a
        L_0x0037:
            int r5 = r4 + -1
            dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast$PublishProducer[] r1 = new dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast.PublishProducer[r5]     // Catch:{ all -> 0x0018 }
            r5 = 0
            r6 = 0
            java.lang.System.arraycopy(r0, r5, r1, r6, r3)     // Catch:{ all -> 0x0018 }
            int r5 = r3 + 1
            int r6 = r4 - r3
            int r6 = r6 + -1
            java.lang.System.arraycopy(r0, r5, r1, r3, r6)     // Catch:{ all -> 0x0018 }
            goto L_0x0033
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast.remove(dji.thirdparty.rx.internal.operators.OnSubscribePublishMulticast$PublishProducer):void");
    }

    static final class ParentSubscriber<T> extends Subscriber<T> {
        final OnSubscribePublishMulticast<T> state;

        public ParentSubscriber(OnSubscribePublishMulticast<T> state2) {
            this.state = state2;
        }

        public void onNext(T t) {
            this.state.onNext(t);
        }

        public void onError(Throwable e) {
            this.state.onError(e);
        }

        public void onCompleted() {
            this.state.onCompleted();
        }

        public void setProducer(Producer p) {
            this.state.setProducer(p);
        }
    }

    public Subscriber<T> subscriber() {
        return this.parent;
    }

    public void unsubscribe() {
        this.parent.unsubscribe();
    }

    public boolean isUnsubscribed() {
        return this.parent.isUnsubscribed();
    }

    static final class PublishProducer<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = 960704844171597367L;
        final Subscriber<? super T> actual;
        final AtomicBoolean once = new AtomicBoolean();
        final OnSubscribePublishMulticast<T> parent;

        public PublishProducer(Subscriber<? super T> actual2, OnSubscribePublishMulticast<T> parent2) {
            this.actual = actual2;
            this.parent = parent2;
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required but it was " + n);
            } else if (n != 0) {
                BackpressureUtils.getAndAddRequest(this, n);
                this.parent.drain();
            }
        }

        public boolean isUnsubscribed() {
            return this.once.get();
        }

        public void unsubscribe() {
            if (this.once.compareAndSet(false, true)) {
                this.parent.remove(this);
            }
        }
    }
}
