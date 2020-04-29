package com.lmax.disruptor;

public interface EventTranslator<T> {
    void translateTo(T t, long j);
}
