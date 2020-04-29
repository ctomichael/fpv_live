package com.lmax.disruptor.dsl;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.FatalExceptionHandler;

public class ExceptionHandlerWrapper<T> implements ExceptionHandler<T> {
    private ExceptionHandler<? super T> delegate = new FatalExceptionHandler();

    public void switchTo(ExceptionHandler<? super T> exceptionHandler) {
        this.delegate = exceptionHandler;
    }

    public void handleEventException(Throwable ex, long sequence, T event) {
        this.delegate.handleEventException(ex, sequence, event);
    }

    public void handleOnStartException(Throwable ex) {
        this.delegate.handleOnStartException(ex);
    }

    public void handleOnShutdownException(Throwable ex) {
        this.delegate.handleOnShutdownException(ex);
    }
}
