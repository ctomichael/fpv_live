package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.internal.util.atomic.SpscAtomicArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.SpscArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess;
import dji.thirdparty.rx.subscriptions.Subscriptions;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorEagerConcatMap<T, R> implements Observable.Operator<R, T> {
    final int bufferSize;
    final Func1<? super T, ? extends Observable<? extends R>> mapper;
    private final int maxConcurrent;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorEagerConcatMap(Func1<? super T, ? extends Observable<? extends R>> mapper2, int bufferSize2, int maxConcurrent2) {
        this.mapper = mapper2;
        this.bufferSize = bufferSize2;
        this.maxConcurrent = maxConcurrent2;
    }

    public Subscriber<? super T> call(Subscriber<? super R> t) {
        EagerOuterSubscriber<T, R> outer = new EagerOuterSubscriber<>(this.mapper, this.bufferSize, this.maxConcurrent, t);
        outer.init();
        return outer;
    }

    static final class EagerOuterProducer extends AtomicLong implements Producer {
        private static final long serialVersionUID = -657299606803478389L;
        final EagerOuterSubscriber<?, ?> parent;

        public EagerOuterProducer(EagerOuterSubscriber<?, ?> parent2) {
            this.parent = parent2;
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalStateException("n >= 0 required but it was " + n);
            } else if (n > 0) {
                BackpressureUtils.getAndAddRequest(this, n);
                this.parent.drain();
            }
        }
    }

    static final class EagerOuterSubscriber<T, R> extends Subscriber<T> {
        final Subscriber<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        volatile boolean done;
        Throwable error;
        final Func1<? super T, ? extends Observable<? extends R>> mapper;
        private EagerOuterProducer sharedProducer;
        final LinkedList<EagerInnerSubscriber<R>> subscribers = new LinkedList<>();
        final AtomicInteger wip = new AtomicInteger();

        public EagerOuterSubscriber(Func1<? super T, ? extends Observable<? extends R>> mapper2, int bufferSize2, int maxConcurrent, Subscriber<? super R> actual2) {
            this.mapper = mapper2;
            this.bufferSize = bufferSize2;
            this.actual = actual2;
            request(maxConcurrent == Integer.MAX_VALUE ? LongCompanionObject.MAX_VALUE : (long) maxConcurrent);
        }

        /* access modifiers changed from: package-private */
        public void init() {
            this.sharedProducer = new EagerOuterProducer(this);
            add(Subscriptions.create(new Action0() {
                /* class dji.thirdparty.rx.internal.operators.OperatorEagerConcatMap.EagerOuterSubscriber.AnonymousClass1 */

                public void call() {
                    EagerOuterSubscriber.this.cancelled = true;
                    if (EagerOuterSubscriber.this.wip.getAndIncrement() == 0) {
                        EagerOuterSubscriber.this.cleanup();
                    }
                }
            }));
            this.actual.add(this);
            this.actual.setProducer(this.sharedProducer);
        }

        /* access modifiers changed from: package-private */
        public void cleanup() {
            List<Subscription> list;
            synchronized (this.subscribers) {
                list = new ArrayList<>(this.subscribers);
                this.subscribers.clear();
            }
            for (Subscription s : list) {
                s.unsubscribe();
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:20:0x002f, code lost:
            if (r5.cancelled != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0031, code lost:
            r2.unsafeSubscribe(r1);
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r6) {
            /*
                r5 = this;
                dji.thirdparty.rx.functions.Func1<? super T, ? extends dji.thirdparty.rx.Observable<? extends R>> r3 = r5.mapper     // Catch:{ Throwable -> 0x0014 }
                java.lang.Object r2 = r3.call(r6)     // Catch:{ Throwable -> 0x0014 }
                dji.thirdparty.rx.Observable r2 = (dji.thirdparty.rx.Observable) r2     // Catch:{ Throwable -> 0x0014 }
                dji.thirdparty.rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber r1 = new dji.thirdparty.rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber
                int r3 = r5.bufferSize
                r1.<init>(r5, r3)
                boolean r3 = r5.cancelled
                if (r3 == 0) goto L_0x001b
            L_0x0013:
                return
            L_0x0014:
                r0 = move-exception
                dji.thirdparty.rx.Subscriber<? super R> r3 = r5.actual
                dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r0, r3, r6)
                goto L_0x0013
            L_0x001b:
                java.util.LinkedList<dji.thirdparty.rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber<R>> r4 = r5.subscribers
                monitor-enter(r4)
                boolean r3 = r5.cancelled     // Catch:{ all -> 0x0024 }
                if (r3 == 0) goto L_0x0027
                monitor-exit(r4)     // Catch:{ all -> 0x0024 }
                goto L_0x0013
            L_0x0024:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0024 }
                throw r3
            L_0x0027:
                java.util.LinkedList<dji.thirdparty.rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber<R>> r3 = r5.subscribers     // Catch:{ all -> 0x0024 }
                r3.add(r1)     // Catch:{ all -> 0x0024 }
                monitor-exit(r4)     // Catch:{ all -> 0x0024 }
                boolean r3 = r5.cancelled
                if (r3 != 0) goto L_0x0013
                r2.unsafeSubscribe(r1)
                r5.drain()
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorEagerConcatMap.EagerOuterSubscriber.onNext(java.lang.Object):void");
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
        public void drain() {
            EagerInnerSubscriber<R> innerSubscriber;
            if (this.wip.getAndIncrement() == 0) {
                int missed = 1;
                AtomicLong requested = this.sharedProducer;
                Subscriber<? super R> actualSubscriber = this.actual;
                NotificationLite<R> nl = NotificationLite.instance();
                while (!this.cancelled) {
                    boolean outerDone = this.done;
                    synchronized (this.subscribers) {
                        innerSubscriber = this.subscribers.peek();
                    }
                    boolean empty = innerSubscriber == null;
                    if (outerDone) {
                        Throwable error2 = this.error;
                        if (error2 != null) {
                            cleanup();
                            actualSubscriber.onError(error2);
                            return;
                        } else if (empty) {
                            actualSubscriber.onCompleted();
                            return;
                        }
                    }
                    if (!empty) {
                        long requestedAmount = requested.get();
                        long emittedAmount = 0;
                        boolean unbounded = requestedAmount == LongCompanionObject.MAX_VALUE;
                        Queue<Object> innerQueue = innerSubscriber.queue;
                        boolean innerDone = false;
                        while (true) {
                            boolean outerDone2 = innerSubscriber.done;
                            Object v = innerQueue.peek();
                            boolean empty2 = v == null;
                            if (outerDone2) {
                                Throwable innerError = innerSubscriber.error;
                                if (innerError == null) {
                                    if (empty2) {
                                        synchronized (this.subscribers) {
                                            this.subscribers.poll();
                                        }
                                        innerSubscriber.unsubscribe();
                                        innerDone = true;
                                        request(1);
                                        break;
                                    }
                                } else {
                                    cleanup();
                                    actualSubscriber.onError(innerError);
                                    return;
                                }
                            }
                            if (empty2 || requestedAmount == 0) {
                                break;
                            }
                            innerQueue.poll();
                            try {
                                actualSubscriber.onNext(nl.getValue(v));
                                requestedAmount--;
                                emittedAmount--;
                            } catch (Throwable ex) {
                                Exceptions.throwOrReport(ex, actualSubscriber, v);
                                return;
                            }
                        }
                        if (emittedAmount != 0) {
                            if (!unbounded) {
                                requested.addAndGet(emittedAmount);
                            }
                            if (!innerDone) {
                                innerSubscriber.requestMore(-emittedAmount);
                            }
                        }
                        if (innerDone) {
                            continue;
                        }
                    }
                    missed = this.wip.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
                cleanup();
            }
        }
    }

    static final class EagerInnerSubscriber<T> extends Subscriber<T> {
        volatile boolean done;
        Throwable error;
        final NotificationLite<T> nl;
        final EagerOuterSubscriber<?, T> parent;
        final Queue<Object> queue;

        public EagerInnerSubscriber(EagerOuterSubscriber<?, T> parent2, int bufferSize) {
            Queue<Object> q;
            this.parent = parent2;
            if (UnsafeAccess.isUnsafeAvailable()) {
                q = new SpscArrayQueue<>(bufferSize);
            } else {
                q = new SpscAtomicArrayQueue<>(bufferSize);
            }
            this.queue = q;
            this.nl = NotificationLite.instance();
            request((long) bufferSize);
        }

        public void onNext(T t) {
            this.queue.offer(this.nl.next(t));
            this.parent.drain();
        }

        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            this.parent.drain();
        }

        public void onCompleted() {
            this.done = true;
            this.parent.drain();
        }

        /* access modifiers changed from: package-private */
        public void requestMore(long n) {
            request(n);
        }
    }
}
