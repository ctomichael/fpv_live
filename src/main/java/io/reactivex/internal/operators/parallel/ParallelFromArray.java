package io.reactivex.internal.operators.parallel;

import io.reactivex.parallel.ParallelFlowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class ParallelFromArray<T> extends ParallelFlowable<T> {
    final Publisher<T>[] sources;

    public ParallelFromArray(Publisher<T>[] sources2) {
        this.sources = sources2;
    }

    public int parallelism() {
        return this.sources.length;
    }

    public void subscribe(Subscriber<? super T>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            for (int i = 0; i < n; i++) {
                this.sources[i].subscribe(subscribers[i]);
            }
        }
    }
}
