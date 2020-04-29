package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.exceptions.MissingBackpressureException;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.SimpleQueue;
import dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue;
import dji.thirdparty.io.reactivex.internal.subscribers.InnerQueuedSubscriber;
import dji.thirdparty.io.reactivex.internal.subscribers.InnerQueuedSubscriberSupport;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.internal.util.AtomicThrowable;
import dji.thirdparty.io.reactivex.internal.util.BackpressureHelper;
import dji.thirdparty.io.reactivex.internal.util.ErrorMode;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableConcatMapEager<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final ErrorMode errorMode;
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    final int maxConcurrency;
    final int prefetch;

    public FlowableConcatMapEager(Publisher<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper2, int maxConcurrency2, int prefetch2, ErrorMode errorMode2) {
        super(source);
        this.mapper = mapper2;
        this.maxConcurrency = maxConcurrency2;
        this.prefetch = prefetch2;
        this.errorMode = errorMode2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        this.source.subscribe(new ConcatMapEagerDelayErrorSubscriber(s, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode));
    }

    static final class ConcatMapEagerDelayErrorSubscriber<T, R> extends AtomicInteger implements Subscriber<T>, Subscription, InnerQueuedSubscriberSupport<R> {
        private static final long serialVersionUID = -4255299542215038287L;
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        volatile InnerQueuedSubscriber<R> current;
        volatile boolean done;
        final ErrorMode errorMode;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function<? super T, ? extends Publisher<? extends R>> mapper;
        final int maxConcurrency;
        final int prefetch;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final SpscLinkedArrayQueue<InnerQueuedSubscriber<R>> subscribers;

        ConcatMapEagerDelayErrorSubscriber(Subscriber<? super R> actual2, Function<? super T, ? extends Publisher<? extends R>> mapper2, int maxConcurrency2, int prefetch2, ErrorMode errorMode2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.maxConcurrency = maxConcurrency2;
            this.prefetch = prefetch2;
            this.errorMode = errorMode2;
            this.subscribers = new SpscLinkedArrayQueue<>(Math.min(prefetch2, maxConcurrency2));
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                s2.request(this.maxConcurrency == Integer.MAX_VALUE ? LongCompanionObject.MAX_VALUE : (long) this.maxConcurrency);
            }
        }

        public void onNext(T t) {
            try {
                Publisher<? extends R> p = (Publisher) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
                InnerQueuedSubscriber<R> inner = new InnerQueuedSubscriber<>(this, this.prefetch);
                if (!this.cancelled) {
                    this.subscribers.offer(inner);
                    if (!this.cancelled) {
                        p.subscribe(inner);
                        if (this.cancelled) {
                            inner.cancel();
                            drainAndCancel();
                        }
                    }
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.s.cancel();
                onError(ex);
            }
        }

        public void onError(Throwable t) {
            if (this.errors.addThrowable(t)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.cancel();
                drainAndCancel();
            }
        }

        /* access modifiers changed from: package-private */
        public void drainAndCancel() {
            if (getAndIncrement() == 0) {
                do {
                    cancelAll();
                } while (decrementAndGet() != 0);
            }
        }

        /* access modifiers changed from: package-private */
        public void cancelAll() {
            while (true) {
                InnerQueuedSubscriber<R> inner = this.subscribers.poll();
                if (inner != null) {
                    inner.cancel();
                } else {
                    return;
                }
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void innerNext(InnerQueuedSubscriber<R> inner, R value) {
            if (inner.queue().offer(value)) {
                drain();
                return;
            }
            inner.cancel();
            innerError(inner, new MissingBackpressureException());
        }

        public void innerError(InnerQueuedSubscriber<R> inner, Throwable e) {
            if (this.errors.addThrowable(e)) {
                inner.setDone();
                if (this.errorMode != ErrorMode.END) {
                    this.s.cancel();
                }
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void innerComplete(InnerQueuedSubscriber<R> inner) {
            inner.setDone();
            drain();
        }

        public void drain() {
            SimpleQueue<R> q;
            if (getAndIncrement() == 0) {
                int missed = 1;
                InnerQueuedSubscriber<R> inner = this.current;
                Subscriber<? super R> a = this.actual;
                ErrorMode em = this.errorMode;
                while (true) {
                    long r = this.requested.get();
                    long e = 0;
                    if (inner == null) {
                        if (em == ErrorMode.END || ((Throwable) this.errors.get()) == null) {
                            boolean outerDone = this.done;
                            inner = this.subscribers.poll();
                            if (outerDone && inner == null) {
                                Throwable ex = this.errors.terminate();
                                if (ex != null) {
                                    a.onError(ex);
                                    return;
                                } else {
                                    a.onComplete();
                                    return;
                                }
                            } else if (inner != null) {
                                this.current = inner;
                            }
                        } else {
                            cancelAll();
                            a.onError(this.errors.terminate());
                            return;
                        }
                    }
                    boolean continueNextSource = false;
                    if (!(inner == null || (q = inner.queue()) == null)) {
                        while (true) {
                            if (e == r) {
                                break;
                            } else if (this.cancelled) {
                                cancelAll();
                                return;
                            } else if (em != ErrorMode.IMMEDIATE || ((Throwable) this.errors.get()) == null) {
                                boolean d = inner.isDone();
                                try {
                                    R v = q.poll();
                                    boolean empty = v == null;
                                    if (!d || !empty) {
                                        if (empty) {
                                            break;
                                        }
                                        a.onNext(v);
                                        e++;
                                        inner.requestOne();
                                    } else {
                                        inner = null;
                                        this.current = null;
                                        this.s.request(1);
                                        continueNextSource = true;
                                        break;
                                    }
                                } catch (Throwable ex2) {
                                    Exceptions.throwIfFatal(ex2);
                                    this.current = null;
                                    inner.cancel();
                                    cancelAll();
                                    a.onError(ex2);
                                    return;
                                }
                            } else {
                                this.current = null;
                                inner.cancel();
                                cancelAll();
                                a.onError(this.errors.terminate());
                                return;
                            }
                        }
                        if (e == r) {
                            if (this.cancelled) {
                                cancelAll();
                                return;
                            } else if (em != ErrorMode.IMMEDIATE || ((Throwable) this.errors.get()) == null) {
                                boolean d2 = inner.isDone();
                                boolean empty2 = q.isEmpty();
                                if (d2 && empty2) {
                                    inner = null;
                                    this.current = null;
                                    this.s.request(1);
                                    continueNextSource = true;
                                }
                            } else {
                                this.current = null;
                                inner.cancel();
                                cancelAll();
                                a.onError(this.errors.terminate());
                                return;
                            }
                        }
                    }
                    if (!(e == 0 || r == LongCompanionObject.MAX_VALUE)) {
                        this.requested.addAndGet(-e);
                    }
                    if (!continueNextSource && (missed = addAndGet(-missed)) == 0) {
                        return;
                    }
                }
            }
        }
    }
}
