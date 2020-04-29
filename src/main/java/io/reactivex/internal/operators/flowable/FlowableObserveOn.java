package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableObserveOn<T> extends AbstractFlowableWithUpstream<T, T> {
    final boolean delayError;
    final int prefetch;
    final Scheduler scheduler;

    public FlowableObserveOn(Flowable<T> source, Scheduler scheduler2, boolean delayError2, int prefetch2) {
        super(source);
        this.scheduler = scheduler2;
        this.delayError = delayError2;
        this.prefetch = prefetch2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        Scheduler.Worker worker = this.scheduler.createWorker();
        if (s instanceof ConditionalSubscriber) {
            this.source.subscribe((FlowableSubscriber) new ObserveOnConditionalSubscriber((ConditionalSubscriber) s, worker, this.delayError, this.prefetch));
        } else {
            this.source.subscribe((FlowableSubscriber) new ObserveOnSubscriber(s, worker, this.delayError, this.prefetch));
        }
    }

    static abstract class BaseObserveOnSubscriber<T> extends BasicIntQueueSubscription<T> implements FlowableSubscriber<T>, Runnable {
        private static final long serialVersionUID = -8241002408341274697L;
        volatile boolean cancelled;
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final int limit;
        boolean outputFused;
        final int prefetch;
        long produced;
        SimpleQueue<T> queue;
        final AtomicLong requested = new AtomicLong();
        int sourceMode;
        Subscription upstream;
        final Scheduler.Worker worker;

        /* access modifiers changed from: package-private */
        public abstract void runAsync();

        /* access modifiers changed from: package-private */
        public abstract void runBackfused();

        /* access modifiers changed from: package-private */
        public abstract void runSync();

        BaseObserveOnSubscriber(Scheduler.Worker worker2, boolean delayError2, int prefetch2) {
            this.worker = worker2;
            this.delayError = delayError2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
        }

        public final void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode == 2) {
                    trySchedule();
                    return;
                }
                if (!this.queue.offer(t)) {
                    this.upstream.cancel();
                    this.error = new MissingBackpressureException("Queue is full?!");
                    this.done = true;
                }
                trySchedule();
            }
        }

        public final void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
            this.done = true;
            trySchedule();
        }

        public final void onComplete() {
            if (!this.done) {
                this.done = true;
                trySchedule();
            }
        }

        public final void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                trySchedule();
            }
        }

        public final void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.upstream.cancel();
                this.worker.dispose();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public final void trySchedule() {
            if (getAndIncrement() == 0) {
                this.worker.schedule(this);
            }
        }

        public final void run() {
            if (this.outputFused) {
                runBackfused();
            } else if (this.sourceMode == 1) {
                runSync();
            } else {
                runAsync();
            }
        }

        /* access modifiers changed from: package-private */
        public final boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a) {
            if (this.cancelled) {
                clear();
                return true;
            }
            if (d) {
                if (!this.delayError) {
                    Throwable e = this.error;
                    if (e != null) {
                        this.cancelled = true;
                        clear();
                        a.onError(e);
                        this.worker.dispose();
                        return true;
                    } else if (empty) {
                        this.cancelled = true;
                        a.onComplete();
                        this.worker.dispose();
                        return true;
                    }
                } else if (empty) {
                    this.cancelled = true;
                    Throwable e2 = this.error;
                    if (e2 != null) {
                        a.onError(e2);
                    } else {
                        a.onComplete();
                    }
                    this.worker.dispose();
                    return true;
                }
            }
            return false;
        }

        public final int requestFusion(int requestedMode) {
            if ((requestedMode & 2) == 0) {
                return 0;
            }
            this.outputFused = true;
            return 2;
        }

        public final void clear() {
            this.queue.clear();
        }

        public final boolean isEmpty() {
            return this.queue.isEmpty();
        }
    }

    static final class ObserveOnSubscriber<T> extends BaseObserveOnSubscriber<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -4547113800637756442L;
        final Subscriber<? super T> downstream;

        ObserveOnSubscriber(Subscriber<? super T> actual, Scheduler.Worker worker, boolean delayError, int prefetch) {
            super(worker, delayError, prefetch);
            this.downstream = actual;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> f = (QueueSubscription) s;
                    int m = f.requestFusion(7);
                    if (m == 1) {
                        this.sourceMode = 1;
                        this.queue = f;
                        this.done = true;
                        this.downstream.onSubscribe(this);
                        return;
                    } else if (m == 2) {
                        this.sourceMode = 2;
                        this.queue = f;
                        this.downstream.onSubscribe(this);
                        s.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                this.downstream.onSubscribe(this);
                s.request((long) this.prefetch);
            }
        }

        /* access modifiers changed from: package-private */
        public void runSync() {
            int missed = 1;
            Subscriber<? super T> a = this.downstream;
            SimpleQueue<T> q = this.queue;
            long e = this.produced;
            while (true) {
                long r = this.requested.get();
                while (e != r) {
                    try {
                        T v = q.poll();
                        if (!this.cancelled) {
                            if (v == null) {
                                this.cancelled = true;
                                a.onComplete();
                                this.worker.dispose();
                                return;
                            }
                            a.onNext(v);
                            e++;
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.cancelled = true;
                        this.upstream.cancel();
                        a.onError(ex);
                        this.worker.dispose();
                        return;
                    }
                }
                if (this.cancelled) {
                    return;
                }
                if (q.isEmpty()) {
                    this.cancelled = true;
                    a.onComplete();
                    this.worker.dispose();
                    return;
                }
                int w = get();
                if (missed == w) {
                    this.produced = e;
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else {
                    missed = w;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void runAsync() {
            int missed = 1;
            Subscriber<? super T> a = this.downstream;
            SimpleQueue<T> q = this.queue;
            long e = this.produced;
            while (true) {
                long r = this.requested.get();
                while (e != r) {
                    boolean d = this.done;
                    try {
                        T v = q.poll();
                        boolean empty = v == null;
                        if (!checkTerminated(d, empty, a)) {
                            if (empty) {
                                break;
                            }
                            a.onNext(v);
                            e++;
                            if (e == ((long) this.limit)) {
                                if (r != LongCompanionObject.MAX_VALUE) {
                                    r = this.requested.addAndGet(-e);
                                }
                                this.upstream.request(e);
                                e = 0;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.cancelled = true;
                        this.upstream.cancel();
                        q.clear();
                        a.onError(ex);
                        this.worker.dispose();
                        return;
                    }
                }
                if (e == r) {
                    if (checkTerminated(this.done, q.isEmpty(), a)) {
                        return;
                    }
                }
                int w = get();
                if (missed == w) {
                    this.produced = e;
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else {
                    missed = w;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void runBackfused() {
            int missed = 1;
            while (!this.cancelled) {
                boolean d = this.done;
                this.downstream.onNext(null);
                if (d) {
                    this.cancelled = true;
                    Throwable e = this.error;
                    if (e != null) {
                        this.downstream.onError(e);
                    } else {
                        this.downstream.onComplete();
                    }
                    this.worker.dispose();
                    return;
                }
                missed = addAndGet(-missed);
                if (missed == 0) {
                    return;
                }
            }
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.queue.poll();
            if (!(v == null || this.sourceMode == 1)) {
                long p = this.produced + 1;
                if (p == ((long) this.limit)) {
                    this.produced = 0;
                    this.upstream.request(p);
                } else {
                    this.produced = p;
                }
            }
            return v;
        }
    }

    static final class ObserveOnConditionalSubscriber<T> extends BaseObserveOnSubscriber<T> {
        private static final long serialVersionUID = 644624475404284533L;
        long consumed;
        final ConditionalSubscriber<? super T> downstream;

        ObserveOnConditionalSubscriber(ConditionalSubscriber<? super T> actual, Scheduler.Worker worker, boolean delayError, int prefetch) {
            super(worker, delayError, prefetch);
            this.downstream = actual;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> f = (QueueSubscription) s;
                    int m = f.requestFusion(7);
                    if (m == 1) {
                        this.sourceMode = 1;
                        this.queue = f;
                        this.done = true;
                        this.downstream.onSubscribe(this);
                        return;
                    } else if (m == 2) {
                        this.sourceMode = 2;
                        this.queue = f;
                        this.downstream.onSubscribe(this);
                        s.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                this.downstream.onSubscribe(this);
                s.request((long) this.prefetch);
            }
        }

        /* access modifiers changed from: package-private */
        public void runSync() {
            int missed = 1;
            ConditionalSubscriber<? super T> a = this.downstream;
            SimpleQueue<T> q = this.queue;
            long e = this.produced;
            while (true) {
                long r = this.requested.get();
                while (e != r) {
                    try {
                        T v = q.poll();
                        if (!this.cancelled) {
                            if (v == null) {
                                this.cancelled = true;
                                a.onComplete();
                                this.worker.dispose();
                                return;
                            } else if (a.tryOnNext(v)) {
                                e++;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.cancelled = true;
                        this.upstream.cancel();
                        a.onError(ex);
                        this.worker.dispose();
                        return;
                    }
                }
                if (this.cancelled) {
                    return;
                }
                if (q.isEmpty()) {
                    this.cancelled = true;
                    a.onComplete();
                    this.worker.dispose();
                    return;
                }
                int w = get();
                if (missed == w) {
                    this.produced = e;
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else {
                    missed = w;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void runAsync() {
            int missed = 1;
            ConditionalSubscriber<? super T> a = this.downstream;
            SimpleQueue<T> q = this.queue;
            long emitted = this.produced;
            long polled = this.consumed;
            while (true) {
                long r = this.requested.get();
                while (emitted != r) {
                    boolean d = this.done;
                    try {
                        T v = q.poll();
                        boolean empty = v == null;
                        if (!checkTerminated(d, empty, a)) {
                            if (empty) {
                                break;
                            }
                            if (a.tryOnNext(v)) {
                                emitted++;
                            }
                            polled++;
                            if (polled == ((long) this.limit)) {
                                this.upstream.request(polled);
                                polled = 0;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.cancelled = true;
                        this.upstream.cancel();
                        q.clear();
                        a.onError(ex);
                        this.worker.dispose();
                        return;
                    }
                }
                if (emitted == r) {
                    if (checkTerminated(this.done, q.isEmpty(), a)) {
                        return;
                    }
                }
                int w = get();
                if (missed == w) {
                    this.produced = emitted;
                    this.consumed = polled;
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else {
                    missed = w;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void runBackfused() {
            int missed = 1;
            while (!this.cancelled) {
                boolean d = this.done;
                this.downstream.onNext(null);
                if (d) {
                    this.cancelled = true;
                    Throwable e = this.error;
                    if (e != null) {
                        this.downstream.onError(e);
                    } else {
                        this.downstream.onComplete();
                    }
                    this.worker.dispose();
                    return;
                }
                missed = addAndGet(-missed);
                if (missed == 0) {
                    return;
                }
            }
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.queue.poll();
            if (!(v == null || this.sourceMode == 1)) {
                long p = this.consumed + 1;
                if (p == ((long) this.limit)) {
                    this.consumed = 0;
                    this.upstream.request(p);
                } else {
                    this.consumed = p;
                }
            }
            return v;
        }
    }
}
