package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.exceptions.MissingBackpressureException;
import dji.thirdparty.io.reactivex.flowables.ConnectableFlowable;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.internal.fuseable.HasUpstreamPublisher;
import dji.thirdparty.io.reactivex.internal.fuseable.QueueSubscription;
import dji.thirdparty.io.reactivex.internal.fuseable.SimpleQueue;
import dji.thirdparty.io.reactivex.internal.queue.SpscArrayQueue;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.internal.util.BackpressureHelper;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.internal.util.NotificationLite;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowablePublish<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T> {
    static final long CANCELLED = Long.MIN_VALUE;
    final int bufferSize;
    final AtomicReference<PublishSubscriber<T>> current;
    final Publisher<T> onSubscribe;
    final Publisher<T> source;

    public static <T> ConnectableFlowable<T> create(Flowable flowable, final int bufferSize2) {
        final AtomicReference<PublishSubscriber<T>> curr = new AtomicReference<>();
        return RxJavaPlugins.onAssembly((ConnectableFlowable) new FlowablePublish(new Publisher<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowablePublish.AnonymousClass1 */

            public void subscribe(Subscriber<? super T> child) {
                PublishSubscriber<T> r;
                InnerSubscriber<T> inner = new InnerSubscriber<>(child);
                child.onSubscribe(inner);
                while (true) {
                    r = (PublishSubscriber) curr.get();
                    if (r == null || r.isDisposed()) {
                        PublishSubscriber<T> u = new PublishSubscriber<>(curr, bufferSize2);
                        if (curr.compareAndSet(r, u)) {
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
        }, flowable, curr, bufferSize2));
    }

    private FlowablePublish(Publisher<T> onSubscribe2, Publisher<T> source2, AtomicReference<PublishSubscriber<T>> current2, int bufferSize2) {
        this.onSubscribe = onSubscribe2;
        this.source = source2;
        this.current = current2;
        this.bufferSize = bufferSize2;
    }

    public Publisher<T> source() {
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
                this.source.subscribe(ps);
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    static final class PublishSubscriber<T> extends AtomicInteger implements Subscriber<T>, Disposable {
        static final InnerSubscriber[] EMPTY = new InnerSubscriber[0];
        static final InnerSubscriber[] TERMINATED = new InnerSubscriber[0];
        private static final long serialVersionUID = -202316842419149694L;
        final int bufferSize;
        final AtomicReference<PublishSubscriber<T>> current;
        volatile SimpleQueue<T> queue;
        final AtomicReference<Subscription> s = new AtomicReference<>();
        final AtomicBoolean shouldConnect;
        int sourceMode;
        final AtomicReference<InnerSubscriber[]> subscribers = new AtomicReference<>(EMPTY);
        volatile Object terminalEvent;

        PublishSubscriber(AtomicReference<PublishSubscriber<T>> current2, int bufferSize2) {
            this.current = current2;
            this.shouldConnect = new AtomicBoolean();
            this.bufferSize = bufferSize2;
        }

        public void dispose() {
            if (this.subscribers.get() != TERMINATED && this.subscribers.getAndSet(TERMINATED) != TERMINATED) {
                this.current.compareAndSet(this, null);
                SubscriptionHelper.cancel(this.s);
            }
        }

        public boolean isDisposed() {
            return this.subscribers.get() == TERMINATED;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.setOnce(this.s, s2)) {
                if (s2 instanceof QueueSubscription) {
                    QueueSubscription<T> qs = (QueueSubscription) s2;
                    int m = qs.requestFusion(3);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = qs;
                        this.terminalEvent = NotificationLite.complete();
                        dispatch();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = qs;
                        s2.request((long) this.bufferSize);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.bufferSize);
                s2.request((long) this.bufferSize);
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
            InnerSubscriber[] c;
            InnerSubscriber[] u;
            do {
                c = this.subscribers.get();
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
            InnerSubscriber[] c;
            InnerSubscriber[] u;
            do {
                c = this.subscribers.get();
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
            if (term != null) {
                if (!NotificationLite.isComplete(term)) {
                    Throwable t = NotificationLite.getError(term);
                    this.current.compareAndSet(this, null);
                    InnerSubscriber<?>[] a = (InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED);
                    if (a.length != 0) {
                        for (InnerSubscriber<?> ip : a) {
                            ip.child.onError(t);
                        }
                        return true;
                    }
                    RxJavaPlugins.onError(t);
                    return true;
                } else if (empty) {
                    this.current.compareAndSet(this, null);
                    for (InnerSubscriber<?> ip2 : (InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED)) {
                        ip2.child.onComplete();
                    }
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public void dispatch() {
            T t;
            T t2;
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (true) {
                    Object term = this.terminalEvent;
                    SimpleQueue<T> q = this.queue;
                    boolean empty = q == null || q.isEmpty();
                    if (!checkTerminated(term, empty)) {
                        if (!empty) {
                            InnerSubscriber<T>[] ps = (InnerSubscriber[]) this.subscribers.get();
                            int len = ps.length;
                            long maxRequested = LongCompanionObject.MAX_VALUE;
                            int cancelled = 0;
                            for (InnerSubscriber<T> ip : ps) {
                                long r = ip.get();
                                if (r >= 0) {
                                    maxRequested = Math.min(maxRequested, r);
                                } else if (r == Long.MIN_VALUE) {
                                    cancelled++;
                                }
                            }
                            if (len == cancelled) {
                                Object term2 = this.terminalEvent;
                                try {
                                    t2 = q.poll();
                                } catch (Throwable ex) {
                                    Exceptions.throwIfFatal(ex);
                                    this.s.get().cancel();
                                    term2 = NotificationLite.error(ex);
                                    this.terminalEvent = term2;
                                    t2 = null;
                                }
                                if (checkTerminated(term2, t2 == null)) {
                                    return;
                                }
                                if (this.sourceMode != 1) {
                                    this.s.get().request(1);
                                }
                            } else {
                                int d = 0;
                                while (((long) d) < maxRequested) {
                                    Object term3 = this.terminalEvent;
                                    try {
                                        t = q.poll();
                                    } catch (Throwable ex2) {
                                        Exceptions.throwIfFatal(ex2);
                                        this.s.get().cancel();
                                        term3 = NotificationLite.error(ex2);
                                        this.terminalEvent = term3;
                                        t = null;
                                    }
                                    empty = t == null;
                                    if (checkTerminated(term3, empty)) {
                                        return;
                                    }
                                    if (empty) {
                                        break;
                                    }
                                    T value = NotificationLite.getValue(t);
                                    InnerSubscriber<T>[] arr$ = ps;
                                    for (InnerSubscriber<T> ip2 : arr$) {
                                        if (ip2.get() > 0) {
                                            ip2.child.onNext(value);
                                            ip2.produced(1);
                                        }
                                    }
                                    d++;
                                }
                                if (d > 0 && this.sourceMode != 1) {
                                    this.s.get().request((long) d);
                                }
                                if (maxRequested != 0 && !empty) {
                                }
                            }
                        }
                        missed = addAndGet(-missed);
                        if (missed == 0) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }

    static final class InnerSubscriber<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
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

        public long produced(long n) {
            return BackpressureHelper.producedCancel(this, n);
        }

        public void cancel() {
            PublishSubscriber<T> p;
            if (get() != Long.MIN_VALUE && getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE && (p = this.parent) != null) {
                p.remove(this);
                p.dispatch();
            }
        }
    }
}
