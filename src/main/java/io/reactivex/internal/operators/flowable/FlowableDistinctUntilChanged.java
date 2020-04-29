package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import org.reactivestreams.Subscriber;

public final class FlowableDistinctUntilChanged<T, K> extends AbstractFlowableWithUpstream<T, T> {
    final BiPredicate<? super K, ? super K> comparer;
    final Function<? super T, K> keySelector;

    public FlowableDistinctUntilChanged(Flowable<T> source, Function<? super T, K> keySelector2, BiPredicate<? super K, ? super K> comparer2) {
        super(source);
        this.keySelector = keySelector2;
        this.comparer = comparer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (s instanceof ConditionalSubscriber) {
            this.source.subscribe((FlowableSubscriber) new DistinctUntilChangedConditionalSubscriber((ConditionalSubscriber) s, this.keySelector, this.comparer));
        } else {
            this.source.subscribe((FlowableSubscriber) new DistinctUntilChangedSubscriber(s, this.keySelector, this.comparer));
        }
    }

    static final class DistinctUntilChangedSubscriber<T, K> extends BasicFuseableSubscriber<T, T> implements ConditionalSubscriber<T> {
        final BiPredicate<? super K, ? super K> comparer;
        boolean hasValue;
        final Function<? super T, K> keySelector;
        K last;

        DistinctUntilChangedSubscriber(Subscriber<? super T> actual, Function<? super T, K> keySelector2, BiPredicate<? super K, ? super K> comparer2) {
            super(actual);
            this.keySelector = keySelector2;
            this.comparer = comparer2;
        }

        public void onNext(T t) {
            if (!tryOnNext(t)) {
                this.upstream.request(1);
            }
        }

        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            if (this.sourceMode != 0) {
                this.downstream.onNext(t);
                return true;
            }
            try {
                K key = this.keySelector.apply(t);
                if (this.hasValue) {
                    boolean equal = this.comparer.test(this.last, key);
                    this.last = key;
                    if (equal) {
                        return false;
                    }
                } else {
                    this.hasValue = true;
                    this.last = key;
                }
                this.downstream.onNext(t);
                return true;
            } catch (Throwable ex) {
                fail(ex);
                return true;
            }
        }

        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        @Nullable
        public T poll() throws Exception {
            while (true) {
                T v = this.qs.poll();
                if (v == null) {
                    return null;
                }
                K key = this.keySelector.apply(v);
                if (!this.hasValue) {
                    this.hasValue = true;
                    this.last = key;
                    return v;
                } else if (!this.comparer.test(this.last, key)) {
                    this.last = key;
                    return v;
                } else {
                    this.last = key;
                    if (this.sourceMode != 1) {
                        this.upstream.request(1);
                    }
                }
            }
        }
    }

    static final class DistinctUntilChangedConditionalSubscriber<T, K> extends BasicFuseableConditionalSubscriber<T, T> {
        final BiPredicate<? super K, ? super K> comparer;
        boolean hasValue;
        final Function<? super T, K> keySelector;
        K last;

        DistinctUntilChangedConditionalSubscriber(ConditionalSubscriber<? super T> actual, Function<? super T, K> keySelector2, BiPredicate<? super K, ? super K> comparer2) {
            super(actual);
            this.keySelector = keySelector2;
            this.comparer = comparer2;
        }

        public void onNext(T t) {
            if (!tryOnNext(t)) {
                this.upstream.request(1);
            }
        }

        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            if (this.sourceMode != 0) {
                return this.downstream.tryOnNext(t);
            }
            try {
                K key = this.keySelector.apply(t);
                if (this.hasValue) {
                    boolean equal = this.comparer.test(this.last, key);
                    this.last = key;
                    if (equal) {
                        return false;
                    }
                } else {
                    this.hasValue = true;
                    this.last = key;
                }
                this.downstream.onNext(t);
                return true;
            } catch (Throwable ex) {
                fail(ex);
                return true;
            }
        }

        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        @Nullable
        public T poll() throws Exception {
            while (true) {
                T v = this.qs.poll();
                if (v == null) {
                    return null;
                }
                K key = this.keySelector.apply(v);
                if (!this.hasValue) {
                    this.hasValue = true;
                    this.last = key;
                    return v;
                } else if (!this.comparer.test(this.last, key)) {
                    this.last = key;
                    return v;
                } else {
                    this.last = key;
                    if (this.sourceMode != 1) {
                        this.upstream.request(1);
                    }
                }
            }
        }
    }
}
