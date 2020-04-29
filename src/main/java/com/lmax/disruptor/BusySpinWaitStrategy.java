package com.lmax.disruptor;

public final class BusySpinWaitStrategy implements WaitStrategy {
    public long waitFor(long sequence, Sequence cursor, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException, InterruptedException {
        while (true) {
            long availableSequence = dependentSequence.get();
            if (availableSequence >= sequence) {
                return availableSequence;
            }
            barrier.checkAlert();
        }
    }

    public void signalAllWhenBlocking() {
    }
}
