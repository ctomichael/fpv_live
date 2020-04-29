package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.flowables.ConnectableFlowable;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.HasUpstreamPublisher;
import dji.thirdparty.io.reactivex.internal.subscribers.SubscriberResourceWrapper;
import dji.thirdparty.io.reactivex.internal.subscriptions.EmptySubscription;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.internal.util.BackpressureHelper;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.internal.util.NotificationLite;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.schedulers.Timed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableReplay<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T> {
    static final Callable DEFAULT_UNBOUNDED_FACTORY = new Callable() {
        /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.AnonymousClass1 */

        public Object call() {
            return new UnboundedReplayBuffer(16);
        }
    };
    final Callable<? extends ReplayBuffer<T>> bufferFactory;
    final AtomicReference<ReplaySubscriber<T>> current;
    final Publisher<T> onSubscribe;
    final Publisher<T> source;

    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        void next(T t);

        void replay(InnerSubscription<T> innerSubscription);
    }

    public static <U, R> Flowable<R> multicastSelector(final Callable<? extends ConnectableFlowable<U>> connectableFactory, final Function<? super Flowable<U>, ? extends Publisher<R>> selector) {
        return Flowable.unsafeCreate(new Publisher<R>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.AnonymousClass2 */

            public void subscribe(Subscriber<? super R> child) {
                try {
                    ConnectableFlowable<U> co = (ConnectableFlowable) ObjectHelper.requireNonNull(connectableFactory.call(), "The connectableFactory returned null");
                    try {
                        Publisher<R> observable = (Publisher) ObjectHelper.requireNonNull(selector.apply(co), "The selector returned a null Publisher");
                        final SubscriberResourceWrapper<R> srw = new SubscriberResourceWrapper<>(child);
                        observable.subscribe(srw);
                        co.connect(new Consumer<Disposable>() {
                            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.AnonymousClass2.AnonymousClass1 */

                            public void accept(Disposable r) {
                                srw.setResource(r);
                            }
                        });
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        EmptySubscription.error(e, child);
                    }
                } catch (Throwable e2) {
                    Exceptions.throwIfFatal(e2);
                    EmptySubscription.error(e2, child);
                }
            }
        });
    }

    public static <T> ConnectableFlowable<T> observeOn(final ConnectableFlowable connectableFlowable, Scheduler scheduler) {
        final Flowable<T> observable = connectableFlowable.observeOn(scheduler);
        return RxJavaPlugins.onAssembly((ConnectableFlowable) new ConnectableFlowable<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.AnonymousClass3 */

            public void connect(Consumer<? super Disposable> connection) {
                connectableFlowable.connect(connection);
            }

            /* access modifiers changed from: protected */
            public void subscribeActual(Subscriber<? super T> s) {
                observable.subscribe(s);
            }
        });
    }

    public static <T> ConnectableFlowable<T> createFrom(Publisher<? extends T> source2) {
        return create(source2, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static <T> ConnectableFlowable<T> create(Publisher publisher, final int bufferSize) {
        if (bufferSize == Integer.MAX_VALUE) {
            return createFrom(publisher);
        }
        return create(publisher, new Callable<ReplayBuffer<T>>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.AnonymousClass4 */

            public ReplayBuffer<T> call() {
                return new SizeBoundReplayBuffer(bufferSize);
            }
        });
    }

    public static <T> ConnectableFlowable<T> create(Publisher<T> source2, long maxAge, TimeUnit unit, Scheduler scheduler) {
        return create(source2, maxAge, unit, scheduler, Integer.MAX_VALUE);
    }

    public static <T> ConnectableFlowable<T> create(Publisher<T> source2, long maxAge, TimeUnit unit, Scheduler scheduler, int bufferSize) {
        final int i = bufferSize;
        final long j = maxAge;
        final TimeUnit timeUnit = unit;
        final Scheduler scheduler2 = scheduler;
        return create(source2, new Callable<ReplayBuffer<T>>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.AnonymousClass5 */

            public ReplayBuffer<T> call() {
                return new SizeAndTimeBoundReplayBuffer(i, j, timeUnit, scheduler2);
            }
        });
    }

    static <T> ConnectableFlowable<T> create(Publisher publisher, final Callable callable) {
        final AtomicReference<ReplaySubscriber<T>> curr = new AtomicReference<>();
        return RxJavaPlugins.onAssembly((ConnectableFlowable) new FlowableReplay(new Publisher<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.AnonymousClass6 */

            public void subscribe(Subscriber<? super T> child) {
                ReplaySubscriber<T> r;
                while (true) {
                    r = (ReplaySubscriber) curr.get();
                    if (r != null) {
                        break;
                    }
                    try {
                        ReplaySubscriber<T> u = new ReplaySubscriber<>((ReplayBuffer) callable.call());
                        if (curr.compareAndSet(null, u)) {
                            r = u;
                            break;
                        }
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        throw ExceptionHelper.wrapOrThrow(ex);
                    }
                }
                InnerSubscription<T> inner = new InnerSubscription<>(r, child);
                child.onSubscribe(inner);
                r.add(inner);
                if (inner.isDisposed()) {
                    r.remove(inner);
                    return;
                }
                r.manageRequests();
                r.buffer.replay(inner);
            }
        }, publisher, curr, callable));
    }

    private FlowableReplay(Publisher<T> onSubscribe2, Publisher<T> source2, AtomicReference<ReplaySubscriber<T>> current2, Callable<? extends ReplayBuffer<T>> bufferFactory2) {
        this.onSubscribe = onSubscribe2;
        this.source = source2;
        this.current = current2;
        this.bufferFactory = bufferFactory2;
    }

    public Publisher<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.onSubscribe.subscribe(s);
    }

    public void connect(Consumer<? super Disposable> connection) {
        ReplaySubscriber<T> ps;
        boolean doConnect;
        while (true) {
            ps = this.current.get();
            if (ps != null && !ps.isDisposed()) {
                break;
            }
            try {
                ReplaySubscriber<T> u = new ReplaySubscriber<>((ReplayBuffer) this.bufferFactory.call());
                if (this.current.compareAndSet(ps, u)) {
                    ps = u;
                    break;
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        if (ps.shouldConnect.get() || !ps.shouldConnect.compareAndSet(false, true)) {
            doConnect = false;
        } else {
            doConnect = true;
        }
        try {
            connection.accept(ps);
            if (doConnect) {
                this.source.subscribe(ps);
            }
        } catch (Throwable ex2) {
            if (doConnect) {
                ps.shouldConnect.compareAndSet(true, false);
            }
            Exceptions.throwIfFatal(ex2);
            throw ExceptionHelper.wrapOrThrow(ex2);
        }
    }

    static final class ReplaySubscriber<T> implements Subscriber<T>, Disposable {
        static final InnerSubscription[] EMPTY = new InnerSubscription[0];
        static final InnerSubscription[] TERMINATED = new InnerSubscription[0];
        final ReplayBuffer<T> buffer;
        boolean done;
        final AtomicInteger management = new AtomicInteger();
        long maxChildRequested;
        long maxUpstreamRequested;
        final AtomicBoolean shouldConnect = new AtomicBoolean();
        final AtomicReference<InnerSubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);
        volatile Subscription subscription;

        ReplaySubscriber(ReplayBuffer<T> buffer2) {
            this.buffer = buffer2;
        }

        public boolean isDisposed() {
            return this.subscribers.get() == TERMINATED;
        }

        public void dispose() {
            this.subscribers.set(TERMINATED);
            this.subscription.cancel();
        }

        /* access modifiers changed from: package-private */
        public boolean add(InnerSubscription<T> producer) {
            InnerSubscription<T>[] c;
            InnerSubscription<T>[] u;
            if (producer == null) {
                throw new NullPointerException();
            }
            do {
                c = (InnerSubscription[]) this.subscribers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerSubscription[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.subscribers.compareAndSet(c, u));
            return true;
        }

        /* access modifiers changed from: package-private */
        public void remove(InnerSubscription<T> p) {
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
                        } else if (c[i].equals(p)) {
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

        public void onSubscribe(Subscription p) {
            if (SubscriptionHelper.validate(this.subscription, p)) {
                this.subscription = p;
                manageRequests();
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.get()) {
                    this.buffer.replay(rp);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                this.buffer.next(t);
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.get()) {
                    this.buffer.replay(rp);
                }
            }
        }

        public void onError(Throwable e) {
            if (!this.done) {
                this.done = true;
                this.buffer.error(e);
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                    this.buffer.replay(rp);
                }
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.buffer.complete();
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                    this.buffer.replay(rp);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void manageRequests() {
            if (this.management.getAndIncrement() == 0) {
                int missed = 1;
                while (!isDisposed()) {
                    long ri = this.maxChildRequested;
                    long maxTotalRequests = ri;
                    for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.get()) {
                        maxTotalRequests = Math.max(maxTotalRequests, rp.totalRequested.get());
                    }
                    long ur = this.maxUpstreamRequested;
                    Subscription p = this.subscription;
                    long diff = maxTotalRequests - ri;
                    if (diff != 0) {
                        this.maxChildRequested = maxTotalRequests;
                        if (p == null) {
                            long u = ur + diff;
                            if (u < 0) {
                                u = LongCompanionObject.MAX_VALUE;
                            }
                            this.maxUpstreamRequested = u;
                        } else if (ur != 0) {
                            this.maxUpstreamRequested = 0;
                            p.request(ur + diff);
                        } else {
                            p.request(diff);
                        }
                    } else if (!(ur == 0 || p == null)) {
                        this.maxUpstreamRequested = 0;
                        p.request(ur);
                    }
                    missed = this.management.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }
        }
    }

    static final class InnerSubscription<T> extends AtomicLong implements Subscription, Disposable {
        static final long CANCELLED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        boolean emitting;
        Object index;
        boolean missed;
        final ReplaySubscriber<T> parent;
        final AtomicLong totalRequested = new AtomicLong();

        InnerSubscription(ReplaySubscriber<T> parent2, Subscriber<? super T> child2) {
            this.parent = parent2;
            this.child = child2;
        }

        public void request(long n) {
            long r;
            if (SubscriptionHelper.validate(n)) {
                do {
                    r = get();
                    if (r != Long.MIN_VALUE) {
                        if (r >= 0 && n == 0) {
                            return;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, BackpressureHelper.addCap(r, n)));
                BackpressureHelper.add(this.totalRequested, n);
                this.parent.manageRequests();
                this.parent.buffer.replay(this);
            }
        }

        public long produced(long n) {
            return BackpressureHelper.producedCancel(this, n);
        }

        public boolean isDisposed() {
            return get() == Long.MIN_VALUE;
        }

        public void cancel() {
            dispose();
        }

        public void dispose() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
                this.parent.manageRequests();
            }
        }

        /* access modifiers changed from: package-private */
        public <U> U index() {
            return this.index;
        }
    }

    static final class UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 7063189396499112664L;
        volatile int size;

        UnboundedReplayBuffer(int capacityHint) {
            super(capacityHint);
        }

        public void next(T value) {
            add(NotificationLite.next(value));
            this.size++;
        }

        public void error(Throwable e) {
            add(NotificationLite.error(e));
            this.size++;
        }

        public void complete() {
            add(NotificationLite.complete());
            this.size++;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0014, code lost:
            if (r15.isDisposed() != false) goto L_0x0009;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0016, code lost:
            r7 = r14.size;
            r2 = (java.lang.Integer) r15.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
            if (r2 == null) goto L_0x004f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
            r1 = r2.intValue();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0024, code lost:
            r8 = r15.get();
            r10 = r8;
            r4 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
            if (r8 == 0) goto L_0x0068;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0031, code lost:
            if (r1 >= r7) goto L_0x0068;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0033, code lost:
            r6 = get(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x003b, code lost:
            if (dji.thirdparty.io.reactivex.internal.util.NotificationLite.accept(r6, r0) != false) goto L_0x0009;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0041, code lost:
            if (r15.isDisposed() != false) goto L_0x0009;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0043, code lost:
            r1 = r1 + 1;
            r8 = r8 - 1;
            r4 = r4 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x004f, code lost:
            r1 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0051, code lost:
            r3 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0052, code lost:
            dji.thirdparty.io.reactivex.exceptions.Exceptions.throwIfFatal(r3);
            r15.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x005c, code lost:
            if (dji.thirdparty.io.reactivex.internal.util.NotificationLite.isError(r6) != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0064, code lost:
            r0.onError(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x006c, code lost:
            if (r4 == 0) goto L_0x0080;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x006e, code lost:
            r15.index = java.lang.Integer.valueOf(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x007b, code lost:
            if (r10 == kotlin.jvm.internal.LongCompanionObject.MAX_VALUE) goto L_0x0080;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x007d, code lost:
            r15.produced(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0080, code lost:
            monitor-enter(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0083, code lost:
            if (r15.missed != false) goto L_0x008d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0085, code lost:
            r15.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0088, code lost:
            monitor-exit(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
            r15.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0090, code lost:
            monitor-exit(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x000e, code lost:
            r0 = r15.child;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay(dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.InnerSubscription<T> r15) {
            /*
                r14 = this;
                monitor-enter(r15)
                boolean r12 = r15.emitting     // Catch:{ all -> 0x004c }
                if (r12 == 0) goto L_0x000a
                r12 = 1
                r15.missed = r12     // Catch:{ all -> 0x004c }
                monitor-exit(r15)     // Catch:{ all -> 0x004c }
            L_0x0009:
                return
            L_0x000a:
                r12 = 1
                r15.emitting = r12     // Catch:{ all -> 0x004c }
                monitor-exit(r15)     // Catch:{ all -> 0x004c }
                org.reactivestreams.Subscriber<? super T> r0 = r15.child
            L_0x0010:
                boolean r12 = r15.isDisposed()
                if (r12 != 0) goto L_0x0009
                int r7 = r14.size
                java.lang.Object r2 = r15.index()
                java.lang.Integer r2 = (java.lang.Integer) r2
                if (r2 == 0) goto L_0x004f
                int r1 = r2.intValue()
            L_0x0024:
                long r8 = r15.get()
                r10 = r8
                r4 = 0
            L_0x002b:
                r12 = 0
                int r12 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
                if (r12 == 0) goto L_0x0068
                if (r1 >= r7) goto L_0x0068
                java.lang.Object r6 = r14.get(r1)
                boolean r12 = dji.thirdparty.io.reactivex.internal.util.NotificationLite.accept(r6, r0)     // Catch:{ Throwable -> 0x0051 }
                if (r12 != 0) goto L_0x0009
                boolean r12 = r15.isDisposed()
                if (r12 != 0) goto L_0x0009
                int r1 = r1 + 1
                r12 = 1
                long r8 = r8 - r12
                r12 = 1
                long r4 = r4 + r12
                goto L_0x002b
            L_0x004c:
                r12 = move-exception
                monitor-exit(r15)     // Catch:{ all -> 0x004c }
                throw r12
            L_0x004f:
                r1 = 0
                goto L_0x0024
            L_0x0051:
                r3 = move-exception
                dji.thirdparty.io.reactivex.exceptions.Exceptions.throwIfFatal(r3)
                r15.dispose()
                boolean r12 = dji.thirdparty.io.reactivex.internal.util.NotificationLite.isError(r6)
                if (r12 != 0) goto L_0x0009
                boolean r12 = dji.thirdparty.io.reactivex.internal.util.NotificationLite.isComplete(r6)
                if (r12 != 0) goto L_0x0009
                r0.onError(r3)
                goto L_0x0009
            L_0x0068:
                r12 = 0
                int r12 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r12 == 0) goto L_0x0080
                java.lang.Integer r12 = java.lang.Integer.valueOf(r1)
                r15.index = r12
                r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r12 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
                if (r12 == 0) goto L_0x0080
                r15.produced(r4)
            L_0x0080:
                monitor-enter(r15)
                boolean r12 = r15.missed     // Catch:{ all -> 0x008a }
                if (r12 != 0) goto L_0x008d
                r12 = 0
                r15.emitting = r12     // Catch:{ all -> 0x008a }
                monitor-exit(r15)     // Catch:{ all -> 0x008a }
                goto L_0x0009
            L_0x008a:
                r12 = move-exception
                monitor-exit(r15)     // Catch:{ all -> 0x008a }
                throw r12
            L_0x008d:
                r12 = 0
                r15.missed = r12     // Catch:{ all -> 0x008a }
                monitor-exit(r15)     // Catch:{ all -> 0x008a }
                goto L_0x0010
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.UnboundedReplayBuffer.replay(dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription):void");
        }
    }

    static final class Node extends AtomicReference<Node> {
        private static final long serialVersionUID = 245354315435971818L;
        final long index;
        final Object value;

        Node(Object value2, long index2) {
            this.value = value2;
            this.index = index2;
        }
    }

    static class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 2346567790059478686L;
        long index;
        int size;
        Node tail;

        BoundedReplayBuffer() {
            Node n = new Node(null, 0);
            this.tail = n;
            set(n);
        }

        /* access modifiers changed from: package-private */
        public final void addLast(Node n) {
            this.tail.set(n);
            this.tail = n;
            this.size++;
        }

        /* access modifiers changed from: package-private */
        public final void removeFirst() {
            Node next = (Node) ((Node) get()).get();
            if (next == null) {
                throw new IllegalStateException("Empty list!");
            }
            this.size--;
            setFirst(next);
        }

        /* access modifiers changed from: package-private */
        public final void removeSome(int n) {
            Node head = (Node) get();
            while (n > 0) {
                head = (Node) head.get();
                n--;
                this.size--;
            }
            setFirst(head);
        }

        /* access modifiers changed from: package-private */
        public final void setFirst(Node n) {
            set(n);
        }

        public final void next(T value) {
            Object o = enterTransform(NotificationLite.next(value));
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncate();
        }

        public final void error(Throwable e) {
            Object o = enterTransform(NotificationLite.error(e));
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncateFinal();
        }

        public final void complete() {
            Object o = enterTransform(NotificationLite.complete());
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncateFinal();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0014, code lost:
            r6 = r13.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001f, code lost:
            if (r6 != kotlin.jvm.internal.LongCompanionObject.MAX_VALUE) goto L_0x007a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0021, code lost:
            r5 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0022, code lost:
            r0 = 0;
            r3 = (dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.Node) r13.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x002a, code lost:
            if (r3 != null) goto L_0x003b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x002c, code lost:
            r3 = (dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.Node) get();
            r13.index = r3;
            dji.thirdparty.io.reactivex.internal.util.BackpressureHelper.add(r13.totalRequested, r3.index);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x003f, code lost:
            if (r6 == 0) goto L_0x008a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0041, code lost:
            r8 = (dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.Node) r3.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0047, code lost:
            if (r8 == null) goto L_0x008a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0049, code lost:
            r4 = leaveTransform(r8.value);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0055, code lost:
            if (dji.thirdparty.io.reactivex.internal.util.NotificationLite.accept(r4, r13.child) == false) goto L_0x007c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0057, code lost:
            r13.index = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x005b, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x005c, code lost:
            dji.thirdparty.io.reactivex.exceptions.Exceptions.throwIfFatal(r2);
            r13.index = null;
            r13.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0069, code lost:
            if (dji.thirdparty.io.reactivex.internal.util.NotificationLite.isError(r4) != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0071, code lost:
            r13.child.onError(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x007a, code lost:
            r5 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x007c, code lost:
            r0 = r0 + 1;
            r6 = r6 - 1;
            r3 = r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0087, code lost:
            if (r13.isDisposed() == false) goto L_0x003b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x008e, code lost:
            if (r0 == 0) goto L_0x0097;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0090, code lost:
            r13.index = r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0092, code lost:
            if (r5 != false) goto L_0x0097;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x0094, code lost:
            r13.produced(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0097, code lost:
            monitor-enter(r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x009a, code lost:
            if (r13.missed != false) goto L_0x00a5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x009c, code lost:
            r13.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x009f, code lost:
            monitor-exit(r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
            r13.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a8, code lost:
            monitor-exit(r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
            if (r13.isDisposed() != false) goto L_0x0009;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void replay(dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.InnerSubscription<T> r13) {
            /*
                r12 = this;
                monitor-enter(r13)
                boolean r9 = r13.emitting     // Catch:{ all -> 0x0077 }
                if (r9 == 0) goto L_0x000a
                r9 = 1
                r13.missed = r9     // Catch:{ all -> 0x0077 }
                monitor-exit(r13)     // Catch:{ all -> 0x0077 }
            L_0x0009:
                return
            L_0x000a:
                r9 = 1
                r13.emitting = r9     // Catch:{ all -> 0x0077 }
                monitor-exit(r13)     // Catch:{ all -> 0x0077 }
            L_0x000e:
                boolean r9 = r13.isDisposed()
                if (r9 != 0) goto L_0x0009
                long r6 = r13.get()
                r10 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r9 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
                if (r9 != 0) goto L_0x007a
                r5 = 1
            L_0x0022:
                r0 = 0
                java.lang.Object r3 = r13.index()
                dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay$Node r3 = (dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.Node) r3
                if (r3 != 0) goto L_0x003b
                java.lang.Object r3 = r12.get()
                dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay$Node r3 = (dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.Node) r3
                r13.index = r3
                java.util.concurrent.atomic.AtomicLong r9 = r13.totalRequested
                long r10 = r3.index
                dji.thirdparty.io.reactivex.internal.util.BackpressureHelper.add(r9, r10)
            L_0x003b:
                r10 = 0
                int r9 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
                if (r9 == 0) goto L_0x008a
                java.lang.Object r8 = r3.get()
                dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay$Node r8 = (dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.Node) r8
                if (r8 == 0) goto L_0x008a
                java.lang.Object r9 = r8.value
                java.lang.Object r4 = r12.leaveTransform(r9)
                org.reactivestreams.Subscriber<? super T> r9 = r13.child     // Catch:{ Throwable -> 0x005b }
                boolean r9 = dji.thirdparty.io.reactivex.internal.util.NotificationLite.accept(r4, r9)     // Catch:{ Throwable -> 0x005b }
                if (r9 == 0) goto L_0x007c
                r9 = 0
                r13.index = r9     // Catch:{ Throwable -> 0x005b }
                goto L_0x0009
            L_0x005b:
                r2 = move-exception
                dji.thirdparty.io.reactivex.exceptions.Exceptions.throwIfFatal(r2)
                r9 = 0
                r13.index = r9
                r13.dispose()
                boolean r9 = dji.thirdparty.io.reactivex.internal.util.NotificationLite.isError(r4)
                if (r9 != 0) goto L_0x0009
                boolean r9 = dji.thirdparty.io.reactivex.internal.util.NotificationLite.isComplete(r4)
                if (r9 != 0) goto L_0x0009
                org.reactivestreams.Subscriber<? super T> r9 = r13.child
                r9.onError(r2)
                goto L_0x0009
            L_0x0077:
                r9 = move-exception
                monitor-exit(r13)     // Catch:{ all -> 0x0077 }
                throw r9
            L_0x007a:
                r5 = 0
                goto L_0x0022
            L_0x007c:
                r10 = 1
                long r0 = r0 + r10
                r10 = 1
                long r6 = r6 - r10
                r3 = r8
                boolean r9 = r13.isDisposed()
                if (r9 == 0) goto L_0x003b
                goto L_0x0009
            L_0x008a:
                r10 = 0
                int r9 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1))
                if (r9 == 0) goto L_0x0097
                r13.index = r3
                if (r5 != 0) goto L_0x0097
                r13.produced(r0)
            L_0x0097:
                monitor-enter(r13)
                boolean r9 = r13.missed     // Catch:{ all -> 0x00a2 }
                if (r9 != 0) goto L_0x00a5
                r9 = 0
                r13.emitting = r9     // Catch:{ all -> 0x00a2 }
                monitor-exit(r13)     // Catch:{ all -> 0x00a2 }
                goto L_0x0009
            L_0x00a2:
                r9 = move-exception
                monitor-exit(r13)     // Catch:{ all -> 0x00a2 }
                throw r9
            L_0x00a5:
                r9 = 0
                r13.missed = r9     // Catch:{ all -> 0x00a2 }
                monitor-exit(r13)     // Catch:{ all -> 0x00a2 }
                goto L_0x000e
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay.BoundedReplayBuffer.replay(dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription):void");
        }

        /* access modifiers changed from: package-private */
        public Object enterTransform(Object value) {
            return value;
        }

        /* access modifiers changed from: package-private */
        public Object leaveTransform(Object value) {
            return value;
        }

        /* access modifiers changed from: package-private */
        public void truncate() {
        }

        /* access modifiers changed from: package-private */
        public void truncateFinal() {
        }

        /* access modifiers changed from: package-private */
        public final void collect(Collection<? super T> output) {
            Node n = (Node) get();
            while (true) {
                Node next = (Node) n.get();
                if (next != null) {
                    Object v = leaveTransform(next.value);
                    if (!NotificationLite.isComplete(v) && !NotificationLite.isError(v)) {
                        output.add(NotificationLite.getValue(v));
                        n = next;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean hasError() {
            return this.tail.value != null && NotificationLite.isError(leaveTransform(this.tail.value));
        }

        /* access modifiers changed from: package-private */
        public boolean hasCompleted() {
            return this.tail.value != null && NotificationLite.isComplete(leaveTransform(this.tail.value));
        }
    }

    static final class SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = -5898283885385201806L;
        final int limit;

        SizeBoundReplayBuffer(int limit2) {
            this.limit = limit2;
        }

        /* access modifiers changed from: package-private */
        public void truncate() {
            if (this.size > this.limit) {
                removeFirst();
            }
        }
    }

    static final class SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = 3457957419649567404L;
        final int limit;
        final long maxAge;
        final Scheduler scheduler;
        final TimeUnit unit;

        SizeAndTimeBoundReplayBuffer(int limit2, long maxAge2, TimeUnit unit2, Scheduler scheduler2) {
            this.scheduler = scheduler2;
            this.limit = limit2;
            this.maxAge = maxAge2;
            this.unit = unit2;
        }

        /* access modifiers changed from: package-private */
        public Object enterTransform(Object value) {
            return new Timed(value, this.scheduler.now(this.unit), this.unit);
        }

        /* access modifiers changed from: package-private */
        public Object leaveTransform(Object value) {
            return ((Timed) value).value();
        }

        /* access modifiers changed from: package-private */
        public void truncate() {
            long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            int e = 0;
            while (next != null) {
                if (this.size <= this.limit) {
                    if (((Timed) next.value).time() > timeLimit) {
                        break;
                    }
                    e++;
                    this.size--;
                    prev = next;
                    next = (Node) next.get();
                } else {
                    e++;
                    this.size--;
                    prev = next;
                    next = (Node) next.get();
                }
            }
            if (e != 0) {
                setFirst(prev);
            }
        }

        /* access modifiers changed from: package-private */
        public void truncateFinal() {
            long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            int e = 0;
            while (next != null && this.size > 1 && ((Timed) next.value).time() <= timeLimit) {
                e++;
                this.size--;
                prev = next;
                next = (Node) next.get();
            }
            if (e != 0) {
                setFirst(prev);
            }
        }
    }
}
