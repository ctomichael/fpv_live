package dji.thirdparty.io.reactivex.internal.operators.maybe;

import dji.thirdparty.io.reactivex.MaybeSource;
import dji.thirdparty.io.reactivex.functions.Function;
import org.reactivestreams.Publisher;

public enum MaybeToPublisher implements Function<MaybeSource<Object>, Publisher<Object>> {
    INSTANCE;

    public /* bridge */ /* synthetic */ Object apply(Object x0) throws Exception {
        return apply((MaybeSource<Object>) ((MaybeSource) x0));
    }

    public static <T> Function<MaybeSource<T>, Publisher<T>> instance() {
        return INSTANCE;
    }

    public Publisher<Object> apply(MaybeSource<Object> t) throws Exception {
        return new MaybeToFlowable(t);
    }
}
