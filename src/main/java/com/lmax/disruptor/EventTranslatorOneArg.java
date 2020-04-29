package com.lmax.disruptor;

public interface EventTranslatorOneArg<T, A> {
    void translateTo(Object obj, long j, Object obj2);
}
