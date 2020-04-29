package dji.thirdparty.io.reactivex.internal.util;

import dji.thirdparty.io.reactivex.Observer;

public interface ObservableQueueDrain<T, U> {
    void accept(Observer observer, Object obj);

    boolean cancelled();

    boolean done();

    boolean enter();

    Throwable error();

    int leave(int i);
}
