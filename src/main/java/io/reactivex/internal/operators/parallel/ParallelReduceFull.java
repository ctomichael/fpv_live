package io.reactivex.internal.operators.parallel;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelReduceFull<T> extends Flowable<T> {
    final BiFunction<T, T, T> reducer;
    final ParallelFlowable<? extends T> source;

    public ParallelReduceFull(ParallelFlowable<? extends T> source2, BiFunction<T, T, T> reducer2) {
        this.source = source2;
        this.reducer = reducer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        ParallelReduceFullMainSubscriber<T> parent = new ParallelReduceFullMainSubscriber<>(s, this.source.parallelism(), this.reducer);
        s.onSubscribe(parent);
        this.source.subscribe(parent.subscribers);
    }

    static final class ParallelReduceFullMainSubscriber<T> extends DeferredScalarSubscription<T> {
        private static final long serialVersionUID = -5370107872170712765L;
        final AtomicReference<SlotPair<T>> current = new AtomicReference<>();
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final BiFunction<T, T, T> reducer;
        final AtomicInteger remaining = new AtomicInteger();
        final ParallelReduceFullInnerSubscriber<T>[] subscribers;

        ParallelReduceFullMainSubscriber(Subscriber<? super T> subscriber, int n, BiFunction<T, T, T> reducer2) {
            super(subscriber);
            ParallelReduceFullInnerSubscriber<T>[] a = new ParallelReduceFullInnerSubscriber[n];
            for (int i = 0; i < n; i++) {
                a[i] = new ParallelReduceFullInnerSubscriber<>(this, reducer2);
            }
            this.subscribers = a;
            this.reducer = reducer2;
            this.remaining.lazySet(n);
        }

        /* access modifiers changed from: package-private */
        public SlotPair<T> addValue(T value) {
            SlotPair<T> curr;
            int c;
            while (true) {
                curr = this.current.get();
                if (curr == null) {
                    curr = new SlotPair<>();
                    if (!this.current.compareAndSet(null, curr)) {
                        continue;
                    }
                }
                c = curr.tryAcquireSlot();
                if (c >= 0) {
                    break;
                }
                this.current.compareAndSet(curr, null);
            }
            if (c == 0) {
                curr.first = value;
            } else {
                curr.second = value;
            }
            if (!curr.releaseSlot()) {
                return null;
            }
            this.current.compareAndSet(curr, null);
            return curr;
        }

        public void cancel() {
            for (ParallelReduceFullInnerSubscriber<T> inner : this.subscribers) {
                inner.cancel();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerError(Throwable ex) {
            if (this.error.compareAndSet(null, ex)) {
                cancel();
                this.downstream.onError(ex);
            } else if (ex != this.error.get()) {
                RxJavaPlugins.onError(ex);
            }
        }

        /* access modifiers changed from: package-private */
        public void innerComplete(T value) {
            if (value != null) {
                while (true) {
                    SlotPair<T> sp = addValue(value);
                    if (sp == null) {
                        break;
                    }
                    try {
                        value = ObjectHelper.requireNonNull(this.reducer.apply(sp.first, sp.second), "The reducer returned a null value");
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        innerError(ex);
                        return;
                    }
                }
            }
            if (this.remaining.decrementAndGet() == 0) {
                SlotPair<T> sp2 = this.current.get();
                this.current.lazySet(null);
                if (sp2 != null) {
                    complete(sp2.first);
                } else {
                    this.downstream.onComplete();
                }
            }
        }
    }

    static final class ParallelReduceFullInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -7954444275102466525L;
        boolean done;
        final ParallelReduceFullMainSubscriber<T> parent;
        final BiFunction<T, T, T> reducer;
        T value;

        ParallelReduceFullInnerSubscriber(ParallelReduceFullMainSubscriber<T> parent2, BiFunction<T, T, T> reducer2) {
            this.parent = parent2;
            this.reducer = reducer2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, LongCompanionObject.MAX_VALUE);
        }

        public void onNext(T t) {
            if (!this.done) {
                T v = this.value;
                if (v == null) {
                    this.value = t;
                    return;
                }
                try {
                    this.value = ObjectHelper.requireNonNull(this.reducer.apply(v, t), "The reducer returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    ((Subscription) get()).cancel();
                    onError(ex);
                }
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
                this.parent.innerComplete(this.value);
            }
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
            SubscriptionHelper.cancel(this);
        }
    }

    static final class SlotPair<T> extends AtomicInteger {
        private static final long serialVersionUID = 473971317683868662L;
        T first;
        final AtomicInteger releaseIndex = new AtomicInteger();
        T second;

        SlotPair() {
        }

        /* access modifiers changed from: package-private */
        public int tryAcquireSlot() {
            int acquired;
            do {
                acquired = get();
                if (acquired >= 2) {
                    return -1;
                }
            } while (!compareAndSet(acquired, acquired + 1));
            return acquired;
        }

        /* access modifiers changed from: package-private */
        public boolean releaseSlot() {
            return this.releaseIndex.incrementAndGet() == 2;
        }
    }
}
