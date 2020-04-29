package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.exceptions.MissingBackpressureException;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.QueueSubscription;
import dji.thirdparty.io.reactivex.internal.fuseable.SimplePlainQueue;
import dji.thirdparty.io.reactivex.internal.fuseable.SimpleQueue;
import dji.thirdparty.io.reactivex.internal.queue.SpscArrayQueue;
import dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.internal.util.AtomicThrowable;
import dji.thirdparty.io.reactivex.internal.util.BackpressureHelper;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFlatMap<T, U> extends AbstractFlowableWithUpstream<T, U> {
    final int bufferSize;
    final boolean delayErrors;
    final Function<? super T, ? extends Publisher<? extends U>> mapper;
    final int maxConcurrency;

    public FlowableFlatMap(Publisher<T> source, Function<? super T, ? extends Publisher<? extends U>> mapper2, boolean delayErrors2, int maxConcurrency2, int bufferSize2) {
        super(source);
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
        this.maxConcurrency = maxConcurrency2;
        this.bufferSize = bufferSize2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> s) {
        if (!FlowableScalarXMap.tryScalarXMapSubscribe(this.source, s, this.mapper)) {
            this.source.subscribe(new MergeSubscriber(s, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize));
        }
    }

    static final class MergeSubscriber<T, U> extends AtomicInteger implements Subscription, Subscriber<T> {
        static final InnerSubscriber<?, ?>[] CANCELLED = new InnerSubscriber[0];
        static final InnerSubscriber<?, ?>[] EMPTY = new InnerSubscriber[0];
        private static final long serialVersionUID = -2117620485640801370L;
        final Subscriber<? super U> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errs = new AtomicThrowable();
        long lastId;
        int lastIndex;
        final Function<? super T, ? extends Publisher<? extends U>> mapper;
        final int maxConcurrency;
        volatile SimplePlainQueue<U> queue;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        int scalarEmitted;
        final int scalarLimit;
        final AtomicReference<InnerSubscriber<?, ?>[]> subscribers = new AtomicReference<>();
        long uniqueId;

        MergeSubscriber(Subscriber<? super U> actual2, Function<? super T, ? extends Publisher<? extends U>> mapper2, boolean delayErrors2, int maxConcurrency2, int bufferSize2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.delayErrors = delayErrors2;
            this.maxConcurrency = maxConcurrency2;
            this.bufferSize = bufferSize2;
            this.scalarLimit = Math.max(1, maxConcurrency2 >> 1);
            this.subscribers.lazySet(EMPTY);
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                if (this.cancelled) {
                    return;
                }
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    s2.request(LongCompanionObject.MAX_VALUE);
                } else {
                    s2.request((long) this.maxConcurrency);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    Publisher<? extends U> p = (Publisher) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
                    if (p instanceof Callable) {
                        try {
                            U u = ((Callable) p).call();
                            if (u != null) {
                                tryEmitScalar(u);
                            } else if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled) {
                                int i = this.scalarEmitted + 1;
                                this.scalarEmitted = i;
                                if (i == this.scalarLimit) {
                                    this.scalarEmitted = 0;
                                    this.s.request((long) this.scalarLimit);
                                }
                            }
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            this.errs.addThrowable(ex);
                            drain();
                        }
                    } else {
                        long j = this.uniqueId;
                        this.uniqueId = 1 + j;
                        InnerSubscriber<T, U> inner = new InnerSubscriber<>(this, j);
                        addInner(inner);
                        p.subscribe(inner);
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.s.cancel();
                    onError(e);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void addInner(InnerSubscriber<T, U> inner) {
            InnerSubscriber<?, ?>[] a;
            InnerSubscriber[] innerSubscriberArr;
            do {
                a = (InnerSubscriber[]) this.subscribers.get();
                if (a == CANCELLED) {
                    inner.dispose();
                    return;
                }
                int n = a.length;
                innerSubscriberArr = new InnerSubscriber[(n + 1)];
                System.arraycopy(a, 0, innerSubscriberArr, 0, n);
                innerSubscriberArr[n] = inner;
            } while (!this.subscribers.compareAndSet(a, innerSubscriberArr));
        }

        /* access modifiers changed from: package-private */
        public void removeInner(InnerSubscriber<T, U> inner) {
            InnerSubscriber<?, ?>[] a;
            InnerSubscriber<?, ?>[] b;
            do {
                a = (InnerSubscriber[]) this.subscribers.get();
                if (a != CANCELLED && a != EMPTY) {
                    int n = a.length;
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
                    if (n == 1) {
                        b = EMPTY;
                    } else {
                        b = new InnerSubscriber[(n - 1)];
                        System.arraycopy(a, 0, b, 0, j);
                        System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(a, b));
        }

        /* access modifiers changed from: package-private */
        public SimpleQueue<U> getMainQueue() {
            SimplePlainQueue<U> q = this.queue;
            if (q == null) {
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    q = new SpscLinkedArrayQueue<>(this.bufferSize);
                } else {
                    q = new SpscArrayQueue<>(this.maxConcurrency);
                }
                this.queue = q;
            }
            return q;
        }

        /* access modifiers changed from: package-private */
        public void tryEmitScalar(U value) {
            if (get() == 0 && compareAndSet(0, 1)) {
                long r = this.requested.get();
                SimpleQueue<U> q = this.queue;
                if (r == 0 || (q != null && !q.isEmpty())) {
                    if (q == null) {
                        q = getMainQueue();
                    }
                    if (!q.offer(value)) {
                        onError(new IllegalStateException("Scalar queue full?!"));
                        return;
                    }
                } else {
                    this.actual.onNext(value);
                    if (r != LongCompanionObject.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled) {
                        int i = this.scalarEmitted + 1;
                        this.scalarEmitted = i;
                        if (i == this.scalarLimit) {
                            this.scalarEmitted = 0;
                            this.s.request((long) this.scalarLimit);
                        }
                    }
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            } else if (!getMainQueue().offer(value)) {
                onError(new IllegalStateException("Scalar queue full?!"));
                return;
            } else if (getAndIncrement() != 0) {
                return;
            }
            drainLoop();
        }

        /* access modifiers changed from: package-private */
        public SimpleQueue<U> getInnerQueue(InnerSubscriber<T, U> inner) {
            SimpleQueue<U> q = inner.queue;
            if (q != null) {
                return q;
            }
            SimpleQueue<U> q2 = new SpscArrayQueue<>(this.bufferSize);
            inner.queue = q2;
            return q2;
        }

        /* access modifiers changed from: package-private */
        public void tryEmit(U value, InnerSubscriber<T, U> inner) {
            if (get() != 0 || !compareAndSet(0, 1)) {
                SimpleQueue<U> q = inner.queue;
                if (q == null) {
                    q = new SpscArrayQueue<>(this.bufferSize);
                    inner.queue = q;
                }
                if (!q.offer(value)) {
                    onError(new MissingBackpressureException("Inner queue full?!"));
                    return;
                } else if (getAndIncrement() != 0) {
                    return;
                }
            } else {
                long r = this.requested.get();
                SimpleQueue<U> q2 = inner.queue;
                if (r == 0 || (q2 != null && !q2.isEmpty())) {
                    if (q2 == null) {
                        q2 = getInnerQueue(inner);
                    }
                    if (!q2.offer(value)) {
                        onError(new MissingBackpressureException("Inner queue full?!"));
                        return;
                    }
                } else {
                    this.actual.onNext(value);
                    if (r != LongCompanionObject.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    inner.requestMore(1);
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
            } else if (this.errs.addThrowable(t)) {
                this.done = true;
                drain();
            } else {
                RxJavaPlugins.onError(t);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void cancel() {
            SimpleQueue<U> q;
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.cancel();
                disposeAll();
                if (getAndIncrement() == 0 && (q = this.queue) != null) {
                    q.clear();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: package-private */
        public void drainLoop() {
            long r;
            U o;
            Subscriber<? super U> child = this.actual;
            int missed = 1;
            while (!checkTerminate()) {
                SimplePlainQueue<U> svq = this.queue;
                long r2 = this.requested.get();
                boolean unbounded = r2 == LongCompanionObject.MAX_VALUE;
                long replenishMain = 0;
                if (svq != null) {
                    do {
                        long scalarEmission = 0;
                        o = null;
                        while (r2 != 0) {
                            o = svq.poll();
                            if (checkTerminate()) {
                                return;
                            }
                            if (o == null) {
                                break;
                            }
                            child.onNext(o);
                            replenishMain++;
                            scalarEmission++;
                            r2--;
                        }
                        if (scalarEmission != 0) {
                            if (unbounded) {
                                r2 = LongCompanionObject.MAX_VALUE;
                            } else {
                                r2 = this.requested.addAndGet(-scalarEmission);
                            }
                        }
                        if (r2 == 0) {
                            break;
                        }
                    } while (o != null);
                }
                boolean d = this.done;
                SimplePlainQueue<U> svq2 = this.queue;
                InnerSubscriber<T, U>[] inner = (InnerSubscriber[]) this.subscribers.get();
                int n = inner.length;
                if (!d || ((svq2 != null && !svq2.isEmpty()) || n != 0)) {
                    boolean innerCompleted = false;
                    if (n != 0) {
                        long startId = this.lastId;
                        int index = this.lastIndex;
                        if (n <= index || inner[index].id != startId) {
                            if (n <= index) {
                                index = 0;
                            }
                            int j = index;
                            for (int i = 0; i < n && inner[j].id != startId; i++) {
                                j++;
                                if (j == n) {
                                    j = 0;
                                }
                            }
                            index = j;
                            this.lastIndex = j;
                            this.lastId = inner[j].id;
                        }
                        int j2 = index;
                        int i2 = 0;
                        while (i2 < n) {
                            if (!checkTerminate()) {
                                InnerSubscriber<T, U> is = inner[j2];
                                U o2 = null;
                                while (!checkTerminate()) {
                                    SimpleQueue<U> q = is.queue;
                                    if (q != null) {
                                        long produced = 0;
                                        while (r != 0) {
                                            try {
                                                o2 = q.poll();
                                                if (o2 == null) {
                                                    break;
                                                }
                                                child.onNext(o2);
                                                if (!checkTerminate()) {
                                                    r--;
                                                    produced++;
                                                } else {
                                                    return;
                                                }
                                            } catch (Throwable ex) {
                                                Exceptions.throwIfFatal(ex);
                                                is.dispose();
                                                this.errs.addThrowable(ex);
                                                if (!checkTerminate()) {
                                                    removeInner(is);
                                                    innerCompleted = true;
                                                    i2++;
                                                } else {
                                                    return;
                                                }
                                            }
                                        }
                                        if (produced != 0) {
                                            if (!unbounded) {
                                                r = this.requested.addAndGet(-produced);
                                            } else {
                                                r = LongCompanionObject.MAX_VALUE;
                                            }
                                            is.requestMore(produced);
                                        }
                                        if (r != 0) {
                                            if (o2 == null) {
                                            }
                                        }
                                    }
                                    boolean innerDone = is.done;
                                    SimpleQueue<U> innerQueue = is.queue;
                                    if (innerDone && (innerQueue == null || innerQueue.isEmpty())) {
                                        removeInner(is);
                                        if (!checkTerminate()) {
                                            replenishMain++;
                                            innerCompleted = true;
                                        } else {
                                            return;
                                        }
                                    }
                                    if (r == 0) {
                                        break;
                                    }
                                    j2++;
                                    if (j2 == n) {
                                        j2 = 0;
                                    }
                                    i2++;
                                }
                                return;
                            }
                            return;
                        }
                        this.lastIndex = j2;
                        this.lastId = inner[j2].id;
                    }
                    if (replenishMain != 0 && !this.cancelled) {
                        this.s.request(replenishMain);
                    }
                    if (!innerCompleted && (missed = addAndGet(-missed)) == 0) {
                        return;
                    }
                } else {
                    Throwable ex2 = this.errs.terminate();
                    if (ex2 == null) {
                        child.onComplete();
                        return;
                    } else {
                        child.onError(ex2);
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminate() {
            if (this.cancelled) {
                SimpleQueue<U> q = this.queue;
                if (q == null) {
                    return true;
                }
                q.clear();
                return true;
            } else if (this.delayErrors || this.errs.get() == null) {
                return false;
            } else {
                this.actual.onError(this.errs.terminate());
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public void disposeAll() {
            InnerSubscriber<?, ?>[] a;
            if (((InnerSubscriber[]) this.subscribers.get()) != CANCELLED && (a = (InnerSubscriber[]) this.subscribers.getAndSet(CANCELLED)) != CANCELLED) {
                for (InnerSubscriber<?, ?> inner : a) {
                    inner.dispose();
                }
                Throwable ex = this.errs.terminate();
                if (ex != null && ex != ExceptionHelper.TERMINATED) {
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }

    static final class InnerSubscriber<T, U> extends AtomicReference<Subscription> implements Subscriber<U>, Disposable {
        private static final long serialVersionUID = -4606175640614850599L;
        final int bufferSize;
        volatile boolean done;
        int fusionMode;
        final long id;
        final int limit = (this.bufferSize >> 2);
        final MergeSubscriber<T, U> parent;
        long produced;
        volatile SimpleQueue<U> queue;

        InnerSubscriber(MergeSubscriber<T, U> parent2, long id2) {
            this.id = id2;
            this.parent = parent2;
            this.bufferSize = parent2.bufferSize;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this, s)) {
                if (s instanceof QueueSubscription) {
                    QueueSubscription<U> qs = (QueueSubscription) s;
                    int m = qs.requestFusion(3);
                    if (m == 1) {
                        this.fusionMode = m;
                        this.queue = qs;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (m == 2) {
                        this.fusionMode = m;
                        this.queue = qs;
                    }
                }
                s.request((long) this.bufferSize);
            }
        }

        public void onNext(U t) {
            if (this.fusionMode != 2) {
                this.parent.tryEmit(t, this);
            } else {
                this.parent.drain();
            }
        }

        public void onError(Throwable t) {
            if (this.parent.errs.addThrowable(t)) {
                this.done = true;
                this.parent.drain();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        /* access modifiers changed from: package-private */
        public void requestMore(long n) {
            if (this.fusionMode != 1) {
                long p = this.produced + n;
                if (p >= ((long) this.limit)) {
                    this.produced = 0;
                    ((Subscription) get()).request(p);
                    return;
                }
                this.produced = p;
            }
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return get() == SubscriptionHelper.CANCELLED;
        }
    }
}
