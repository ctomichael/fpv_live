package com.lmax.disruptor;

public interface ExceptionHandler<T> {
    void handleEventException(Throwable th, long j, T t);

    void handleOnShutdownException(Throwable th);

    void handleOnStartException(Throwable th);
}
