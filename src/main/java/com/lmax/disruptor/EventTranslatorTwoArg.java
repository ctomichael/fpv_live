package com.lmax.disruptor;

public interface EventTranslatorTwoArg<T, A, B> {
    void translateTo(T t, long j, A a, B b);
}
