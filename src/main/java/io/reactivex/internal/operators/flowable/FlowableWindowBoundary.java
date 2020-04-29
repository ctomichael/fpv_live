package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowBoundary<T, B> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
    final int capacityHint;
    final Publisher<B> other;

    public FlowableWindowBoundary(Flowable<T> source, Publisher<B> other2, int capacityHint2) {
        super(source);
        this.other = other2;
        this.capacityHint = capacityHint2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Flowable<T>> subscriber) {
        WindowBoundaryMainSubscriber<T, B> parent = new WindowBoundaryMainSubscriber<>(subscriber, this.capacityHint);
        subscriber.onSubscribe(parent);
        parent.innerNext();
        this.other.subscribe(parent.boundarySubscriber);
        this.source.subscribe((FlowableSubscriber) parent);
    }

    static final class WindowBoundaryMainSubscriber<T, B> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
        static final Object NEXT_WINDOW = new Object();
        private static final long serialVersionUID = 2233020065421370272L;
        final WindowBoundaryInnerSubscriber<T, B> boundarySubscriber = new WindowBoundaryInnerSubscriber<>(this);
        final int capacityHint;
        volatile boolean done;
        final Subscriber<? super Flowable<T>> downstream;
        long emitted;
        final AtomicThrowable errors = new AtomicThrowable();
        final MpscLinkedQueue<Object> queue = new MpscLinkedQueue<>();
        final AtomicLong requested = new AtomicLong();
        final AtomicBoolean stopWindows = new AtomicBoolean();
        final AtomicReference<Subscription> upstream = new AtomicReference<>();
        UnicastProcessor<T> window;
        final AtomicInteger windows = new AtomicInteger(1);

        WindowBoundaryMainSubscriber(Subscriber<? super Flowable<T>> downstream2, int capacityHint2) {
            this.downstream = downstream2;
            this.capacityHint = capacityHint2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this.upstream, s, LongCompanionObject.MAX_VALUE);
        }

        public void onNext(T t) {
            this.queue.offer(t);
            drain();
        }

        public void onError(Throwable e) {
            this.boundarySubscriber.dispose();
            if (this.errors.addThrowable(e)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            this.boundarySubscriber.dispose();
            this.done = true;
            drain();
        }

        public void cancel() {
            if (this.stopWindows.compareAndSet(false, true)) {
                this.boundarySubscriber.dispose();
                if (this.windows.decrementAndGet() == 0) {
                    SubscriptionHelper.cancel(this.upstream);
                }
            }
        }

        public void request(long n) {
            BackpressureHelper.add(this.requested, n);
        }

        public void run() {
            if (this.windows.decrementAndGet() == 0) {
                SubscriptionHelper.cancel(this.upstream);
            }
        }

        /* access modifiers changed from: package-private */
        public void innerNext() {
            this.queue.offer(NEXT_WINDOW);
            drain();
        }

        /* access modifiers changed from: package-private */
        public void innerError(Throwable e) {
            SubscriptionHelper.cancel(this.upstream);
            if (this.errors.addThrowable(e)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        /* access modifiers changed from: package-private */
        public void innerComplete() {
            SubscriptionHelper.cancel(this.upstream);
            this.done = true;
            drain();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super Flowable<T>> downstream2 = this.downstream;
                MpscLinkedQueue<Object> queue2 = this.queue;
                AtomicThrowable errors2 = this.errors;
                long emitted2 = this.emitted;
                while (this.windows.get() != 0) {
                    UnicastProcessor<T> w = this.window;
                    boolean d = this.done;
                    if (!d || errors2.get() == null) {
                        Object v = queue2.poll();
                        boolean empty = v == null;
                        if (d && empty) {
                            Throwable ex = errors2.terminate();
                            if (ex == null) {
                                if (w != null) {
                                    this.window = null;
                                    w.onComplete();
                                }
                                downstream2.onComplete();
                                return;
                            }
                            if (w != null) {
                                this.window = null;
                                w.onError(ex);
                            }
                            downstream2.onError(ex);
                            return;
                        } else if (empty) {
                            this.emitted = emitted2;
                            missed = addAndGet(-missed);
                            if (missed == 0) {
                                return;
                            }
                        } else if (v != NEXT_WINDOW) {
                            w.onNext(v);
                        } else {
                            if (w != null) {
                                this.window = null;
                                w.onComplete();
                            }
                            if (!this.stopWindows.get()) {
                                UnicastProcessor<T> w2 = UnicastProcessor.create(this.capacityHint, this);
                                this.window = w2;
                                this.windows.getAndIncrement();
                                if (emitted2 != this.requested.get()) {
                                    emitted2++;
                                    downstream2.onNext(w2);
                                } else {
                                    SubscriptionHelper.cancel(this.upstream);
                                    this.boundarySubscriber.dispose();
                                    errors2.addThrowable(new MissingBackpressureException("Could not deliver a window due to lack of requests"));
                                    this.done = true;
                                }
                            }
                        }
                    } else {
                        queue2.clear();
                        Throwable ex2 = errors2.terminate();
                        if (w != null) {
                            this.window = null;
                            w.onError(ex2);
                        }
                        downstream2.onError(ex2);
                        return;
                    }
                }
                queue2.clear();
                this.window = null;
            }
        }
    }

    static final class WindowBoundaryInnerSubscriber<T, B> extends DisposableSubscriber<B> {
        boolean done;
        final WindowBoundaryMainSubscriber<T, B> parent;

        WindowBoundaryInnerSubscriber(WindowBoundaryMainSubscriber<T, B> parent2) {
            this.parent = parent2;
        }

        public void onNext(B b) {
            if (!this.done) {
                this.parent.innerNext();
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.parent.innerError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.innerComplete();
            }
        }
    }
}
