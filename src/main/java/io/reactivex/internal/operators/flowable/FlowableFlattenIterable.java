package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFlattenIterable<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    final int prefetch;

    public FlowableFlattenIterable(Flowable<T> source, Function<? super T, ? extends Iterable<? extends R>> mapper2, int prefetch2) {
        super(source);
        this.mapper = mapper2;
        this.prefetch = prefetch2;
    }

    public void subscribeActual(Subscriber<? super R> s) {
        if (this.source instanceof Callable) {
            try {
                T v = ((Callable) this.source).call();
                if (v == null) {
                    EmptySubscription.complete(s);
                    return;
                }
                try {
                    FlowableFromIterable.subscribe(s, ((Iterable) this.mapper.apply(v)).iterator());
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    EmptySubscription.error(ex, s);
                }
            } catch (Throwable ex2) {
                Exceptions.throwIfFatal(ex2);
                EmptySubscription.error(ex2, s);
            }
        } else {
            this.source.subscribe((FlowableSubscriber) new FlattenIterableSubscriber(s, this.mapper, this.prefetch));
        }
    }

    static final class FlattenIterableSubscriber<T, R> extends BasicIntQueueSubscription<R> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -3096000382929934955L;
        volatile boolean cancelled;
        int consumed;
        Iterator<? extends R> current;
        volatile boolean done;
        final Subscriber<? super R> downstream;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        int fusionMode;
        final int limit;
        final Function<? super T, ? extends Iterable<? extends R>> mapper;
        final int prefetch;
        SimpleQueue<T> queue;
        final AtomicLong requested = new AtomicLong();
        Subscription upstream;

        FlattenIterableSubscriber(Subscriber<? super R> actual, Function<? super T, ? extends Iterable<? extends R>> mapper2, int prefetch2) {
            this.downstream = actual;
            this.mapper = mapper2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> qs = (QueueSubscription) s;
                    int m = qs.requestFusion(3);
                    if (m == 1) {
                        this.fusionMode = m;
                        this.queue = qs;
                        this.done = true;
                        this.downstream.onSubscribe(this);
                        return;
                    } else if (m == 2) {
                        this.fusionMode = m;
                        this.queue = qs;
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

        public void onNext(T t) {
            if (!this.done) {
                if (this.fusionMode != 0 || this.queue.offer(t)) {
                    drain();
                } else {
                    onError(new MissingBackpressureException("Queue is full?!"));
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done || !ExceptionHelper.addThrowable(this.error, t)) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            drain();
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
            if (!this.cancelled) {
                this.cancelled = true;
                this.upstream.cancel();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                Subscriber<? super R> a = this.downstream;
                SimpleQueue<T> q = this.queue;
                boolean replenish = this.fusionMode != 1;
                int missed = 1;
                Iterator<? extends R> it2 = this.current;
                while (true) {
                    if (it2 == null) {
                        boolean d = this.done;
                        try {
                            T t = q.poll();
                            if (checkTerminated(d, t == null, a, q)) {
                                return;
                            }
                            if (t != null) {
                                try {
                                    it2 = ((Iterable) this.mapper.apply(t)).iterator();
                                    if (!it2.hasNext()) {
                                        it2 = null;
                                        consumedOne(replenish);
                                    } else {
                                        this.current = it2;
                                    }
                                } catch (Throwable ex) {
                                    Exceptions.throwIfFatal(ex);
                                    this.upstream.cancel();
                                    ExceptionHelper.addThrowable(this.error, ex);
                                    a.onError(ExceptionHelper.terminate(this.error));
                                    return;
                                }
                            }
                        } catch (Throwable ex2) {
                            Exceptions.throwIfFatal(ex2);
                            this.upstream.cancel();
                            ExceptionHelper.addThrowable(this.error, ex2);
                            Throwable ex3 = ExceptionHelper.terminate(this.error);
                            this.current = null;
                            q.clear();
                            a.onError(ex3);
                            return;
                        }
                    }
                    if (it2 != null) {
                        long r = this.requested.get();
                        long e = 0;
                        while (true) {
                            if (e == r) {
                                break;
                            }
                            if (!checkTerminated(this.done, false, a, q)) {
                                try {
                                    a.onNext(ObjectHelper.requireNonNull(it2.next(), "The iterator returned a null value"));
                                    if (!checkTerminated(this.done, false, a, q)) {
                                        e++;
                                        try {
                                            if (!it2.hasNext()) {
                                                consumedOne(replenish);
                                                it2 = null;
                                                this.current = null;
                                                break;
                                            }
                                        } catch (Throwable ex4) {
                                            Exceptions.throwIfFatal(ex4);
                                            this.current = null;
                                            this.upstream.cancel();
                                            ExceptionHelper.addThrowable(this.error, ex4);
                                            a.onError(ExceptionHelper.terminate(this.error));
                                            return;
                                        }
                                    } else {
                                        return;
                                    }
                                } catch (Throwable ex5) {
                                    Exceptions.throwIfFatal(ex5);
                                    this.current = null;
                                    this.upstream.cancel();
                                    ExceptionHelper.addThrowable(this.error, ex5);
                                    a.onError(ExceptionHelper.terminate(this.error));
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                        if (e == r) {
                            if (checkTerminated(this.done, q.isEmpty() && it2 == null, a, q)) {
                                return;
                            }
                        }
                        if (!(e == 0 || r == LongCompanionObject.MAX_VALUE)) {
                            this.requested.addAndGet(-e);
                        }
                        if (it2 == null) {
                            continue;
                        }
                    }
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void consumedOne(boolean enabled) {
            if (enabled) {
                int c = this.consumed + 1;
                if (c == this.limit) {
                    this.consumed = 0;
                    this.upstream.request((long) c);
                    return;
                }
                this.consumed = c;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SimpleQueue<?> q) {
            if (this.cancelled) {
                this.current = null;
                q.clear();
                return true;
            }
            if (d) {
                if (this.error.get() != null) {
                    Throwable ex = ExceptionHelper.terminate(this.error);
                    this.current = null;
                    q.clear();
                    a.onError(ex);
                    return true;
                } else if (empty) {
                    a.onComplete();
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            this.current = null;
            this.queue.clear();
        }

        public boolean isEmpty() {
            return this.current == null && this.queue.isEmpty();
        }

        @Nullable
        public R poll() throws Exception {
            Iterator<? extends R> it2 = this.current;
            while (true) {
                if (it2 == null) {
                    T v = this.queue.poll();
                    if (v != null) {
                        it2 = ((Iterable) this.mapper.apply(v)).iterator();
                        if (it2.hasNext()) {
                            this.current = it2;
                            break;
                        }
                        it2 = null;
                    } else {
                        return null;
                    }
                } else {
                    break;
                }
            }
            R requireNonNull = ObjectHelper.requireNonNull(it2.next(), "The iterator returned a null value");
            if (it2.hasNext()) {
                return requireNonNull;
            }
            this.current = null;
            return requireNonNull;
        }

        public int requestFusion(int requestedMode) {
            if ((requestedMode & 1) == 0 || this.fusionMode != 1) {
                return 0;
            }
            return 1;
        }
    }
}
