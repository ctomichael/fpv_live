package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.HasUpstreamPublisher;
import org.reactivestreams.Publisher;

abstract class AbstractFlowableWithUpstream<T, R> extends Flowable<R> implements HasUpstreamPublisher<T> {
    protected final Publisher<T> source;

    AbstractFlowableWithUpstream(Publisher<T> source2) {
        this.source = (Publisher) ObjectHelper.requireNonNull(source2, "source is null");
    }

    public final Publisher<T> source() {
        return this.source;
    }
}
