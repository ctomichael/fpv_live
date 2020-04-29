package io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;

public interface FlowablePublishClassic<T> {
    int publishBufferSize();

    Publisher<T> publishSource();
}
