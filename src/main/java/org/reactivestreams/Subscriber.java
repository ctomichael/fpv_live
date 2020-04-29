package org.reactivestreams;

public interface Subscriber<T> {
    void onComplete();

    void onError(Throwable th);

    void onNext(Object obj);

    void onSubscribe(Subscription subscription);
}
