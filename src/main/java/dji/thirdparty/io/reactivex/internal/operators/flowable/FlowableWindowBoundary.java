package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.MissingBackpressureException;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.SimplePlainQueue;
import dji.thirdparty.io.reactivex.internal.queue.MpscLinkedQueue;
import dji.thirdparty.io.reactivex.internal.subscribers.QueueDrainSubscriber;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.internal.util.NotificationLite;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.processors.UnicastProcessor;
import dji.thirdparty.io.reactivex.subscribers.DisposableSubscriber;
import dji.thirdparty.io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowBoundary<T, B> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
    final int bufferSize;
    final Publisher<B> other;

    public FlowableWindowBoundary(Publisher<T> source, Publisher<B> other2, int bufferSize2) {
        super(source);
        this.other = other2;
        this.bufferSize = bufferSize2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Flowable<T>> s) {
        this.source.subscribe(new WindowBoundaryMainSubscriber(new SerializedSubscriber(s), this.other, this.bufferSize));
    }

    static final class WindowBoundaryMainSubscriber<T, B> extends QueueDrainSubscriber<T, Object, Flowable<T>> implements Subscription {
        static final Object NEXT = new Object();
        final AtomicReference<Disposable> boundary = new AtomicReference<>();
        final int bufferSize;
        final Publisher<B> other;
        Subscription s;
        UnicastProcessor<T> window;
        final AtomicLong windows = new AtomicLong();

        WindowBoundaryMainSubscriber(Subscriber<? super Flowable<T>> actual, Publisher<B> other2, int bufferSize2) {
            super(actual, new MpscLinkedQueue());
            this.other = other2;
            this.bufferSize = bufferSize2;
            this.windows.lazySet(1);
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                Subscriber<? super Flowable<T>> a = this.actual;
                a.onSubscribe(this);
                if (!this.cancelled) {
                    UnicastProcessor<T> w = UnicastProcessor.create(this.bufferSize);
                    long r = requested();
                    if (r != 0) {
                        a.onNext(w);
                        if (r != LongCompanionObject.MAX_VALUE) {
                            produced(1);
                        }
                        this.window = w;
                        WindowBoundaryInnerSubscriber<T, B> inner = new WindowBoundaryInnerSubscriber<>(this);
                        if (this.boundary.compareAndSet(null, inner)) {
                            this.windows.getAndIncrement();
                            s2.request(LongCompanionObject.MAX_VALUE);
                            this.other.subscribe(inner);
                            return;
                        }
                        return;
                    }
                    a.onError(new MissingBackpressureException("Could not deliver first window due to lack of requests"));
                }
            }
        }

        public void onNext(T t) {
            if (fastEnter()) {
                this.window.onNext(t);
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                this.queue.offer(NotificationLite.next(t));
                if (!enter()) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            if (this.windows.decrementAndGet() == 0) {
                DisposableHelper.dispose(this.boundary);
            }
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                if (enter()) {
                    drainLoop();
                }
                if (this.windows.decrementAndGet() == 0) {
                    DisposableHelper.dispose(this.boundary);
                }
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            this.cancelled = true;
        }

        /* access modifiers changed from: package-private */
        public void drainLoop() {
            SimplePlainQueue<Object> q = this.queue;
            Subscriber<? super Flowable<T>> a = this.actual;
            int missed = 1;
            UnicastProcessor<T> w = this.window;
            while (true) {
                boolean d = this.done;
                Object o = q.poll();
                boolean empty = o == null;
                if (d && empty) {
                    DisposableHelper.dispose(this.boundary);
                    Throwable e = this.error;
                    if (e != null) {
                        w.onError(e);
                        return;
                    } else {
                        w.onComplete();
                        return;
                    }
                } else if (empty) {
                    missed = leave(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else if (o == NEXT) {
                    w.onComplete();
                    if (this.windows.decrementAndGet() == 0) {
                        DisposableHelper.dispose(this.boundary);
                        return;
                    } else if (!this.cancelled) {
                        w = UnicastProcessor.create(this.bufferSize);
                        long r = requested();
                        if (r != 0) {
                            this.windows.getAndIncrement();
                            a.onNext(w);
                            if (r != LongCompanionObject.MAX_VALUE) {
                                produced(1);
                            }
                            this.window = w;
                        } else {
                            this.cancelled = true;
                            a.onError(new MissingBackpressureException("Could not deliver new window due to lack of requests"));
                        }
                    }
                } else {
                    w.onNext(NotificationLite.getValue(o));
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void next() {
            this.queue.offer(NEXT);
            if (enter()) {
                drainLoop();
            }
        }

        public boolean accept(Subscriber<? super Flowable<T>> subscriber, Object v) {
            return false;
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
                this.parent.next();
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.parent.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.onComplete();
            }
        }
    }
}
