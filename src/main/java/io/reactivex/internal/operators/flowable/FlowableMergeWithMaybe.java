package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableMergeWithMaybe<T> extends AbstractFlowableWithUpstream<T, T> {
    final MaybeSource<? extends T> other;

    public FlowableMergeWithMaybe(Flowable<T> source, MaybeSource<? extends T> other2) {
        super(source);
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        MergeWithObserver<T> parent = new MergeWithObserver<>(subscriber);
        subscriber.onSubscribe(parent);
        this.source.subscribe((FlowableSubscriber) parent);
        this.other.subscribe(parent.otherObserver);
    }

    static final class MergeWithObserver<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
        static final int OTHER_STATE_CONSUMED_OR_EMPTY = 2;
        static final int OTHER_STATE_HAS_VALUE = 1;
        private static final long serialVersionUID = -4592979584110982903L;
        volatile boolean cancelled;
        int consumed;
        final Subscriber<? super T> downstream;
        long emitted;
        final AtomicThrowable error = new AtomicThrowable();
        final int limit = (this.prefetch - (this.prefetch >> 2));
        volatile boolean mainDone;
        final AtomicReference<Subscription> mainSubscription = new AtomicReference<>();
        final OtherObserver<T> otherObserver = new OtherObserver<>(this);
        volatile int otherState;
        final int prefetch = Flowable.bufferSize();
        volatile SimplePlainQueue<T> queue;
        final AtomicLong requested = new AtomicLong();
        T singleItem;

        MergeWithObserver(Subscriber<? super T> downstream2) {
            this.downstream = downstream2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this.mainSubscription, s, (long) this.prefetch);
        }

        public void onNext(T t) {
            if (compareAndSet(0, 1)) {
                long e = this.emitted;
                if (this.requested.get() != e) {
                    SimplePlainQueue<T> q = this.queue;
                    if (q == null || q.isEmpty()) {
                        this.emitted = 1 + e;
                        this.downstream.onNext(t);
                        int c = this.consumed + 1;
                        if (c == this.limit) {
                            this.consumed = 0;
                            this.mainSubscription.get().request((long) c);
                        } else {
                            this.consumed = c;
                        }
                    } else {
                        q.offer(t);
                    }
                } else {
                    getOrCreateQueue().offer(t);
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            } else {
                getOrCreateQueue().offer(t);
                if (getAndIncrement() != 0) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable ex) {
            if (this.error.addThrowable(ex)) {
                DisposableHelper.dispose(this.otherObserver);
                drain();
                return;
            }
            RxJavaPlugins.onError(ex);
        }

        public void onComplete() {
            this.mainDone = true;
            drain();
        }

        public void request(long n) {
            BackpressureHelper.add(this.requested, n);
            drain();
        }

        public void cancel() {
            this.cancelled = true;
            SubscriptionHelper.cancel(this.mainSubscription);
            DisposableHelper.dispose(this.otherObserver);
            if (getAndIncrement() == 0) {
                this.queue = null;
                this.singleItem = null;
            }
        }

        /* access modifiers changed from: package-private */
        public void otherSuccess(T value) {
            if (compareAndSet(0, 1)) {
                long e = this.emitted;
                if (this.requested.get() != e) {
                    this.emitted = 1 + e;
                    this.downstream.onNext(value);
                    this.otherState = 2;
                } else {
                    this.singleItem = value;
                    this.otherState = 1;
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
            } else {
                this.singleItem = value;
                this.otherState = 1;
                if (getAndIncrement() != 0) {
                    return;
                }
            }
            drainLoop();
        }

        /* access modifiers changed from: package-private */
        public void otherError(Throwable ex) {
            if (this.error.addThrowable(ex)) {
                SubscriptionHelper.cancel(this.mainSubscription);
                drain();
                return;
            }
            RxJavaPlugins.onError(ex);
        }

        /* access modifiers changed from: package-private */
        public void otherComplete() {
            this.otherState = 2;
            drain();
        }

        /* access modifiers changed from: package-private */
        public SimplePlainQueue<T> getOrCreateQueue() {
            SimplePlainQueue<T> q = this.queue;
            if (q != null) {
                return q;
            }
            SimplePlainQueue<T> q2 = new SpscArrayQueue<>(Flowable.bufferSize());
            this.queue = q2;
            return q2;
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: package-private */
        public void drainLoop() {
            Subscriber<? super T> actual = this.downstream;
            int missed = 1;
            long e = this.emitted;
            int c = this.consumed;
            int lim = this.limit;
            do {
                long r = this.requested.get();
                while (e != r) {
                    if (this.cancelled) {
                        this.singleItem = null;
                        this.queue = null;
                        return;
                    } else if (this.error.get() != null) {
                        this.singleItem = null;
                        this.queue = null;
                        actual.onError(this.error.terminate());
                        return;
                    } else {
                        int os = this.otherState;
                        if (os == 1) {
                            T v = this.singleItem;
                            this.singleItem = null;
                            this.otherState = 2;
                            actual.onNext(v);
                            e++;
                        } else {
                            boolean d = this.mainDone;
                            SimplePlainQueue<T> q = this.queue;
                            T v2 = q != null ? q.poll() : null;
                            boolean empty = v2 == null;
                            if (d && empty && os == 2) {
                                this.queue = null;
                                actual.onComplete();
                                return;
                            } else if (empty) {
                                break;
                            } else {
                                actual.onNext(v2);
                                e++;
                                c++;
                                if (c == lim) {
                                    c = 0;
                                    this.mainSubscription.get().request((long) lim);
                                }
                            }
                        }
                    }
                }
                if (e == r) {
                    if (this.cancelled) {
                        this.singleItem = null;
                        this.queue = null;
                        return;
                    } else if (this.error.get() != null) {
                        this.singleItem = null;
                        this.queue = null;
                        actual.onError(this.error.terminate());
                        return;
                    } else {
                        boolean d2 = this.mainDone;
                        SimplePlainQueue<T> q2 = this.queue;
                        boolean empty2 = q2 == null || q2.isEmpty();
                        if (d2 && empty2 && this.otherState == 2) {
                            this.queue = null;
                            actual.onComplete();
                            return;
                        }
                    }
                }
                this.emitted = e;
                this.consumed = c;
                missed = addAndGet(-missed);
            } while (missed != 0);
        }

        static final class OtherObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T> {
            private static final long serialVersionUID = -2935427570954647017L;
            final MergeWithObserver<T> parent;

            OtherObserver(MergeWithObserver<T> parent2) {
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onSuccess(T t) {
                this.parent.otherSuccess(t);
            }

            public void onError(Throwable e) {
                this.parent.otherError(e);
            }

            public void onComplete() {
                this.parent.otherComplete();
            }
        }
    }
}
