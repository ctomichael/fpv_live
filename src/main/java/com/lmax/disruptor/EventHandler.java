package com.lmax.disruptor;

public interface EventHandler<T> {
    void onEvent(Object obj, long j, boolean z) throws Exception;
}
