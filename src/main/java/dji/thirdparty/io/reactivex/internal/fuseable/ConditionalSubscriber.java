package dji.thirdparty.io.reactivex.internal.fuseable;

import org.reactivestreams.Subscriber;

public interface ConditionalSubscriber<T> extends Subscriber<T> {
    boolean tryOnNext(T t);
}
