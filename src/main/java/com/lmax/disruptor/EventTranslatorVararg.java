package com.lmax.disruptor;

public interface EventTranslatorVararg<T> {
    void translateTo(T t, long j, Object... objArr);
}
