package com.lmax.disruptor;

public final class YieldingWaitStrategy implements WaitStrategy {
    private static final int SPIN_TRIES = 100;

    public long waitFor(long sequence, Sequence cursor, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException, InterruptedException {
        int counter = 100;
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
        if (counter != 0) {
            return counter - 1;
        }
        Thread.yield();
        return counter;
    }
}
