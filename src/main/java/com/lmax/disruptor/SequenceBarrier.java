package com.lmax.disruptor;

public interface SequenceBarrier {
    void alert();

    void checkAlert() throws AlertException;

    void clearAlert();

    long getCursor();

    boolean isAlerted();

    long waitFor(long j) throws AlertException, InterruptedException, TimeoutException;
}
