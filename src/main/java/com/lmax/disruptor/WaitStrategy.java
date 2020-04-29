package com.lmax.disruptor;

public interface WaitStrategy {
    void signalAllWhenBlocking();

    long waitFor(long j, Sequence sequence, Sequence sequence2, SequenceBarrier sequenceBarrier) throws AlertException, InterruptedException, TimeoutException;
}
