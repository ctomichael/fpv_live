package com.lmax.disruptor;

public interface EventTranslatorThreeArg<T, A, B, C> {
    void translateTo(T t, long j, A a, B b, C c);
}
