package org.reactivestreams;

public interface Publisher<T> {
    void subscribe(Subscriber subscriber);
}
