package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.subscribers.FullArbiterSubscriber;
import dji.thirdparty.io.reactivex.internal.subscriptions.FullArbiter;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.subscribers.DisposableSubscriber;
import dji.thirdparty.io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTimeout<T, U, V> extends AbstractFlowableWithUpstream<T, T> {
    final Publisher<U> firstTimeoutIndicator;
    final Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator;
    final Publisher<? extends T> other;

    interface OnTimeout {
        void onError(Throwable th);

        void timeout(long j);
    }

    public FlowableTimeout(Publisher<T> source, Publisher<U> firstTimeoutIndicator2, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator2, Publisher<? extends T> other2) {
        super(source);
        this.firstTimeoutIndicator = firstTimeoutIndicator2;
        this.itemTimeoutIndicator = itemTimeoutIndicator2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (this.other == null) {
            this.source.subscribe(new TimeoutSubscriber(new SerializedSubscriber(s), this.firstTimeoutIndicator, this.itemTimeoutIndicator));
        } else {
            this.source.subscribe(new TimeoutOtherSubscriber(s, this.firstTimeoutIndicator, this.itemTimeoutIndicator, this.other));
        }
    }

    static final class TimeoutSubscriber<T, U, V> implements Subscriber<T>, Subscription, OnTimeout {
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final Publisher<U> firstTimeoutIndicator;
        volatile long index;
        final Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator;
        Subscription s;
        final AtomicReference<Disposable> timeout = new AtomicReference<>();

        TimeoutSubscriber(Subscriber<? super T> actual2, Publisher<U> firstTimeoutIndicator2, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator2) {
            this.actual = actual2;
            this.firstTimeoutIndicator = firstTimeoutIndicator2;
            this.itemTimeoutIndicator = itemTimeoutIndicator2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                if (!this.cancelled) {
                    Subscriber<? super T> a = this.actual;
                    Publisher<U> p = this.firstTimeoutIndicator;
                    if (p != null) {
                        TimeoutInnerSubscriber<T, U, V> tis = new TimeoutInnerSubscriber<>(this, 0);
                        if (this.timeout.compareAndSet(null, tis)) {
                            a.onSubscribe(this);
                            p.subscribe(tis);
                            return;
                        }
                        return;
                    }
                    a.onSubscribe(this);
                }
            }
        }

        public void onNext(T t) {
            long idx = this.index + 1;
            this.index = idx;
            this.actual.onNext(t);
            Disposable d = this.timeout.get();
            if (d != null) {
                d.dispose();
            }
            try {
                Publisher<V> p = (Publisher) ObjectHelper.requireNonNull(this.itemTimeoutIndicator.apply(t), "The publisher returned is null");
                TimeoutInnerSubscriber<T, U, V> tis = new TimeoutInnerSubscriber<>(this, idx);
                if (this.timeout.compareAndSet(d, tis)) {
                    p.subscribe(tis);
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                cancel();
                this.actual.onError(e);
            }
        }

        public void onError(Throwable t) {
            cancel();
            this.actual.onError(t);
        }

        public void onComplete() {
            cancel();
            this.actual.onComplete();
        }

        public void request(long n) {
            this.s.request(n);
        }

        public void cancel() {
            this.cancelled = true;
            this.s.cancel();
            DisposableHelper.dispose(this.timeout);
        }

        public void timeout(long idx) {
            if (idx == this.index) {
                cancel();
                this.actual.onError(new TimeoutException());
            }
        }
    }

    static final class TimeoutInnerSubscriber<T, U, V> extends DisposableSubscriber<Object> {
        boolean done;
        final long index;
        final OnTimeout parent;

        TimeoutInnerSubscriber(OnTimeout parent2, long index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onNext(Object t) {
            if (!this.done) {
                this.done = true;
                cancel();
                this.parent.timeout(this.index);
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
                this.parent.timeout(this.index);
            }
        }
    }

    static final class TimeoutOtherSubscriber<T, U, V> implements Subscriber<T>, Disposable, OnTimeout {
        final Subscriber<? super T> actual;
        final FullArbiter<T> arbiter;
        volatile boolean cancelled;
        boolean done;
        final Publisher<U> firstTimeoutIndicator;
        volatile long index;
        final Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator;
        final Publisher<? extends T> other;
        Subscription s;
        final AtomicReference<Disposable> timeout = new AtomicReference<>();

        TimeoutOtherSubscriber(Subscriber<? super T> actual2, Publisher<U> firstTimeoutIndicator2, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator2, Publisher<? extends T> other2) {
            this.actual = actual2;
            this.firstTimeoutIndicator = firstTimeoutIndicator2;
            this.itemTimeoutIndicator = itemTimeoutIndicator2;
            this.other = other2;
            this.arbiter = new FullArbiter<>(actual2, this, 8);
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                if (this.arbiter.setSubscription(s2)) {
                    Subscriber<? super T> a = this.actual;
                    Publisher<U> p = this.firstTimeoutIndicator;
                    if (p != null) {
                        TimeoutInnerSubscriber<T, U, V> tis = new TimeoutInnerSubscriber<>(this, 0);
                        if (this.timeout.compareAndSet(null, tis)) {
                            a.onSubscribe(this.arbiter);
                            p.subscribe(tis);
                            return;
                        }
                        return;
                    }
                    a.onSubscribe(this.arbiter);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long idx = this.index + 1;
                this.index = idx;
                if (this.arbiter.onNext(t, this.s)) {
                    Disposable d = this.timeout.get();
                    if (d != null) {
                        d.dispose();
                    }
                    try {
                        Publisher<V> p = (Publisher) ObjectHelper.requireNonNull(this.itemTimeoutIndicator.apply(t), "The publisher returned is null");
                        TimeoutInnerSubscriber<T, U, V> tis = new TimeoutInnerSubscriber<>(this, idx);
                        if (this.timeout.compareAndSet(d, tis)) {
                            p.subscribe(tis);
                        }
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        this.actual.onError(e);
                    }
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            dispose();
            this.arbiter.onError(t, this.s);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                dispose();
                this.arbiter.onComplete(this.s);
            }
        }

        public void dispose() {
            this.cancelled = true;
            this.s.cancel();
            DisposableHelper.dispose(this.timeout);
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void timeout(long idx) {
            if (idx == this.index) {
                dispose();
                this.other.subscribe(new FullArbiterSubscriber(this.arbiter));
            }
        }
    }
}
