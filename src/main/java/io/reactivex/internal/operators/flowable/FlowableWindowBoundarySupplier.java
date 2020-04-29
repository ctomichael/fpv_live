package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowBoundarySupplier<T, B> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
    final int capacityHint;
    final Callable<? extends Publisher<B>> other;

    public FlowableWindowBoundarySupplier(Flowable<T> source, Callable<? extends Publisher<B>> other2, int capacityHint2) {
        super(source);
        this.other = other2;
        this.capacityHint = capacityHint2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Flowable<T>> subscriber) {
        this.source.subscribe((FlowableSubscriber) new WindowBoundaryMainSubscriber<>(subscriber, this.capacityHint, this.other));
    }

    static final class WindowBoundaryMainSubscriber<T, B> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
        static final WindowBoundaryInnerSubscriber<Object, Object> BOUNDARY_DISPOSED = new WindowBoundaryInnerSubscriber<>(null);
        static final Object NEXT_WINDOW = new Object();
        private static final long serialVersionUID = 2233020065421370272L;
        final AtomicReference<WindowBoundaryInnerSubscriber<T, B>> boundarySubscriber = new AtomicReference<>();
        final int capacityHint;
        volatile boolean done;
        final Subscriber<? super Flowable<T>> downstream;
        long emitted;
        final AtomicThrowable errors = new AtomicThrowable();
        final Callable<? extends Publisher<B>> other;
        final MpscLinkedQueue<Object> queue = new MpscLinkedQueue<>();
        final AtomicLong requested;
        final AtomicBoolean stopWindows = new AtomicBoolean();
        Subscription upstream;
        UnicastProcessor<T> window;
        final AtomicInteger windows = new AtomicInteger(1);

        WindowBoundaryMainSubscriber(Subscriber<? super Flowable<T>> downstream2, int capacityHint2, Callable<? extends Publisher<B>> other2) {
            this.downstream = downstream2;
            this.capacityHint = capacityHint2;
            this.other = other2;
            this.requested = new AtomicLong();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                this.downstream.onSubscribe(this);
                this.queue.offer(NEXT_WINDOW);
                drain();
                s.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            this.queue.offer(t);
            drain();
        }

        public void onError(Throwable e) {
            disposeBoundary();
            if (this.errors.addThrowable(e)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            disposeBoundary();
            this.done = true;
            drain();
        }

        public void cancel() {
            if (this.stopWindows.compareAndSet(false, true)) {
                disposeBoundary();
                if (this.windows.decrementAndGet() == 0) {
                    this.upstream.cancel();
                }
            }
        }

        public void request(long n) {
            BackpressureHelper.add(this.requested, n);
        }

        /* access modifiers changed from: package-private */
        public void disposeBoundary() {
            Disposable d = this.boundarySubscriber.getAndSet(BOUNDARY_DISPOSED);
            if (d != null && d != BOUNDARY_DISPOSED) {
                d.dispose();
            }
        }

        public void run() {
            if (this.windows.decrementAndGet() == 0) {
                this.upstream.cancel();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerNext(WindowBoundaryInnerSubscriber<T, B> sender) {
            this.boundarySubscriber.compareAndSet(sender, null);
            this.queue.offer(NEXT_WINDOW);
            drain();
        }

        /* access modifiers changed from: package-private */
        public void innerError(Throwable e) {
            this.upstream.cancel();
            if (this.errors.addThrowable(e)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        /* access modifiers changed from: package-private */
        public void innerComplete() {
            this.upstream.cancel();
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
                                if (emitted2 != this.requested.get()) {
                                    UnicastProcessor<T> w2 = UnicastProcessor.create(this.capacityHint, this);
                                    this.window = w2;
                                    this.windows.getAndIncrement();
                                    try {
                                        Publisher<B> otherSource = (Publisher) ObjectHelper.requireNonNull(this.other.call(), "The other Callable returned a null Publisher");
                                        WindowBoundaryInnerSubscriber<T, B> bo = new WindowBoundaryInnerSubscriber<>(this);
                                        if (this.boundarySubscriber.compareAndSet(null, bo)) {
                                            otherSource.subscribe(bo);
                                            emitted2++;
                                            downstream2.onNext(w2);
                                        }
                                    } catch (Throwable ex2) {
                                        Exceptions.throwIfFatal(ex2);
                                        errors2.addThrowable(ex2);
                                        this.done = true;
                                    }
                                } else {
                                    this.upstream.cancel();
                                    disposeBoundary();
                                    errors2.addThrowable(new MissingBackpressureException("Could not deliver a window due to lack of requests"));
                                    this.done = true;
                                }
                            }
                        }
                    } else {
                        queue2.clear();
                        Throwable ex3 = errors2.terminate();
                        if (w != null) {
                            this.window = null;
                            w.onError(ex3);
                        }
                        downstream2.onError(ex3);
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
                this.done = true;
                dispose();
                this.parent.innerNext(this);
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
