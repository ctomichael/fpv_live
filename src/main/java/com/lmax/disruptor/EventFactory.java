package com.lmax.disruptor;

public interface EventFactory<T> {
    T newInstance();
}
