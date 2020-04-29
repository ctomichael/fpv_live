package io.reactivex.internal.operators.parallel;

import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.flowable.FlowableConcatMap;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.parallel.ParallelFlowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class ParallelConcatMap<T, R> extends ParallelFlowable<R> {
    final ErrorMode errorMode;
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    final int prefetch;
    final ParallelFlowable<T> source;

    public ParallelConcatMap(ParallelFlowable<T> source2, Function<? super T, ? extends Publisher<? extends R>> mapper2, int prefetch2, ErrorMode errorMode2) {
        this.source = source2;
        this.mapper = (Function) ObjectHelper.requireNonNull(mapper2, "mapper");
        this.prefetch = prefetch2;
        this.errorMode = (ErrorMode) ObjectHelper.requireNonNull(errorMode2, "errorMode");
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    public void subscribe(Subscriber<? super R>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<T>[] parents = new Subscriber[n];
            for (int i = 0; i < n; i++) {
                parents[i] = FlowableConcatMap.subscribe(subscribers[i], this.mapper, this.prefetch, this.errorMode);
            }
            this.source.subscribe(parents);
        }
    }
}
