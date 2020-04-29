package com.lmax.disruptor;

public final class InsufficientCapacityException extends Exception {
    public static final InsufficientCapacityException INSTANCE = new InsufficientCapacityException();

    private InsufficientCapacityException() {
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
