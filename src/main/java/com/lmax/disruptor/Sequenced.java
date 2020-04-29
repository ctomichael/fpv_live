package com.lmax.disruptor;

public interface Sequenced {
    int getBufferSize();

    boolean hasAvailableCapacity(int i);

    long next();

    long next(int i);

    void publish(long j);

    void publish(long j, long j2);

    long remainingCapacity();

    long tryNext() throws InsufficientCapacityException;

    long tryNext(int i) throws InsufficientCapacityException;
}
