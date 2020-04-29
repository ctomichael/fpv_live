package com.lmax.disruptor;

public final class TimeoutException extends Exception {
    public static final TimeoutException INSTANCE = new TimeoutException();

    private TimeoutException() {
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
