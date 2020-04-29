package io.reactivex.internal.operators.parallel;

import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableFlatMap;
import io.reactivex.parallel.ParallelFlowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class ParallelFlatMap<T, R> extends ParallelFlowable<R> {
    final boolean delayError;
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    final int maxConcurrency;
    final int prefetch;
    final ParallelFlowable<T> source;

    public ParallelFlatMap(ParallelFlowable<T> source2, Function<? super T, ? extends Publisher<? extends R>> mapper2, boolean delayError2, int maxConcurrency2, int prefetch2) {
        this.source = source2;
        this.mapper = mapper2;
        this.delayError = delayError2;
        this.maxConcurrency = maxConcurrency2;
        this.prefetch = prefetch2;
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    public void subscribe(Subscriber<? super R>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<T>[] parents = new Subscriber[n];
            for (int i = 0; i < n; i++) {
                parents[i] = FlowableFlatMap.subscribe(subscribers[i], this.mapper, this.delayError, this.maxConcurrency, this.prefetch);
            }
            this.source.subscribe(parents);
        }
    }
}
