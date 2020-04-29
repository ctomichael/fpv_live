package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.operators.flowable.FlowableSequenceEqual;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;

public final class FlowableSequenceEqualSingle<T> extends Single<Boolean> implements FuseToFlowable<Boolean> {
    final BiPredicate<? super T, ? super T> comparer;
    final Publisher<? extends T> first;
    final int prefetch;
    final Publisher<? extends T> second;

    public FlowableSequenceEqualSingle(Publisher<? extends T> first2, Publisher<? extends T> second2, BiPredicate<? super T, ? super T> comparer2, int prefetch2) {
        this.first = first2;
        this.second = second2;
        this.comparer = comparer2;
        this.prefetch = prefetch2;
    }

    public void subscribeActual(SingleObserver<? super Boolean> observer) {
        EqualCoordinator<T> parent = new EqualCoordinator<>(observer, this.prefetch, this.comparer);
        observer.onSubscribe(parent);
        parent.subscribe(this.first, this.second);
    }

    public Flowable<Boolean> fuseToFlowable() {
        return RxJavaPlugins.onAssembly(new FlowableSequenceEqual(this.first, this.second, this.comparer, this.prefetch));
    }

    static final class EqualCoordinator<T> extends AtomicInteger implements Disposable, FlowableSequenceEqual.EqualCoordinatorHelper {
        private static final long serialVersionUID = -6178010334400373240L;
        final BiPredicate<? super T, ? super T> comparer;
        final SingleObserver<? super Boolean> downstream;
        final AtomicThrowable error = new AtomicThrowable();
        final FlowableSequenceEqual.EqualSubscriber<T> first;
        final FlowableSequenceEqual.EqualSubscriber<T> second;
        T v1;
        T v2;

        EqualCoordinator(SingleObserver<? super Boolean> actual, int prefetch, BiPredicate<? super T, ? super T> comparer2) {
            this.downstream = actual;
            this.comparer = comparer2;
            this.first = new FlowableSequenceEqual.EqualSubscriber<>(this, prefetch);
            this.second = new FlowableSequenceEqual.EqualSubscriber<>(this, prefetch);
        }

        /* access modifiers changed from: package-private */
        public void subscribe(Publisher<? extends T> source1, Publisher<? extends T> source2) {
            source1.subscribe(this.first);
            source2.subscribe(this.second);
        }

        public void dispose() {
            this.first.cancel();
            this.second.cancel();
            if (getAndIncrement() == 0) {
                this.first.clear();
                this.second.clear();
            }
        }

        public boolean isDisposed() {
            return this.first.get() == SubscriptionHelper.CANCELLED;
        }

        /* access modifiers changed from: package-private */
        public void cancelAndClear() {
            this.first.cancel();
            this.first.clear();
            this.second.cancel();
            this.second.clear();
        }

        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                do {
                    SimpleQueue<T> q1 = this.first.queue;
                    SimpleQueue<T> q2 = this.second.queue;
                    if (q1 != null && q2 != null) {
                        while (!isDisposed()) {
                            if (((Throwable) this.error.get()) != null) {
                                cancelAndClear();
                                this.downstream.onError(this.error.terminate());
                                return;
                            }
                            boolean d1 = this.first.done;
                            T a = this.v1;
                            if (a == null) {
                                try {
                                    a = q1.poll();
                                    this.v1 = a;
                                } catch (Throwable exc) {
                                    Exceptions.throwIfFatal(exc);
                                    cancelAndClear();
                                    this.error.addThrowable(exc);
                                    this.downstream.onError(this.error.terminate());
                                    return;
                                }
                            }
                            boolean e1 = a == null;
                            boolean d2 = this.second.done;
                            T b = this.v2;
                            if (b == null) {
                                try {
                                    b = q2.poll();
                                    this.v2 = b;
                                } catch (Throwable exc2) {
                                    Exceptions.throwIfFatal(exc2);
                                    cancelAndClear();
                                    this.error.addThrowable(exc2);
                                    this.downstream.onError(this.error.terminate());
                                    return;
                                }
                            }
                            boolean e2 = b == null;
                            if (d1 && d2 && e1 && e2) {
                                this.downstream.onSuccess(true);
                                return;
                            } else if (d1 && d2 && e1 != e2) {
                                cancelAndClear();
                                this.downstream.onSuccess(false);
                                return;
                            } else if (!e1 && !e2) {
                                try {
                                    if (!this.comparer.test(a, b)) {
                                        cancelAndClear();
                                        this.downstream.onSuccess(false);
                                        return;
                                    }
                                    this.v1 = null;
                                    this.v2 = null;
                                    this.first.request();
                                    this.second.request();
                                } catch (Throwable exc3) {
                                    Exceptions.throwIfFatal(exc3);
                                    cancelAndClear();
                                    this.error.addThrowable(exc3);
                                    this.downstream.onError(this.error.terminate());
                                    return;
                                }
                            }
                        }
                        this.first.clear();
                        this.second.clear();
                        return;
                    } else if (isDisposed()) {
                        this.first.clear();
                        this.second.clear();
                        return;
                    } else if (((Throwable) this.error.get()) != null) {
                        cancelAndClear();
                        this.downstream.onError(this.error.terminate());
                        return;
                    }
                    missed = addAndGet(-missed);
                } while (missed != 0);
            }
        }

        public void innerError(Throwable t) {
            if (this.error.addThrowable(t)) {
                drain();
            } else {
                RxJavaPlugins.onError(t);
            }
        }
    }
}
