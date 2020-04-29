package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;

public enum MaybeToPublisher implements Function<MaybeSource<Object>, Publisher<Object>> {
    INSTANCE;

    public /* bridge */ /* synthetic */ Object apply(Object obj) throws Exception {
        return apply((MaybeSource<Object>) ((MaybeSource) obj));
    }

    public static <T> Function<MaybeSource<T>, Publisher<T>> instance() {
        return INSTANCE;
    }

    public Publisher<Object> apply(MaybeSource<Object> t) throws Exception {
        return new MaybeToFlowable(t);
    }
}
