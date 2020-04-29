package com.lmax.disruptor;

import java.util.concurrent.locks.LockSupport;

public final class SleepingWaitStrategy implements WaitStrategy {
    private static final int DEFAULT_RETRIES = 200;
    private static final long DEFAULT_SLEEP = 100;
    private final int retries;
    private final long sleepTimeNs;

    public SleepingWaitStrategy() {
        this(200, DEFAULT_SLEEP);
    }

    public SleepingWaitStrategy(int retries2) {
        this(retries2, DEFAULT_SLEEP);
    }

    public SleepingWaitStrategy(int retries2, long sleepTimeNs2) {
        this.retries = retries2;
        this.sleepTimeNs = sleepTimeNs2;
    }

    public long waitFor(long sequence, Sequence cursor, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException {
        int counter = this.retries;
        while (true) {
            long availableSequence = dependentSequence.get();
            if (availableSequence >= sequence) {
                return availableSequence;
            }
            counter = applyWaitMethod(barrier, counter);
        }
    }

    public void signalAllWhenBlocking() {
    }

    private int applyWaitMethod(SequenceBarrier barrier, int counter) throws AlertException {
        barrier.checkAlert();
        if (counter > 100) {
            return counter - 1;
        }
        if (counter > 0) {
            int counter2 = counter - 1;
            Thread.yield();
            return counter2;
        }
        LockSupport.parkNanos(this.sleepTimeNs);
        return counter;
    }
}
