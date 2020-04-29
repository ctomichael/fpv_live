package com.lmax.disruptor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class BlockingWaitStrategy implements WaitStrategy {
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = this.lock.newCondition();

    public long waitFor(long sequence, Sequence cursorSequence, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException, InterruptedException {
        if (cursorSequence.get() < sequence) {
            this.lock.lock();
            while (cursorSequence.get() < sequence) {
                try {
                    barrier.checkAlert();
                    this.processorNotifyCondition.await();
                } finally {
                    this.lock.unlock();
                }
            }
        }
        while (true) {
            long availableSequence = dependentSequence.get();
            if (availableSequence >= sequence) {
                return availableSequence;
            }
            barrier.checkAlert();
        }
    }

    public void signalAllWhenBlocking() {
        this.lock.lock();
        try {
            this.processorNotifyCondition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    public String toString() {
        return "BlockingWaitStrategy{processorNotifyCondition=" + this.processorNotifyCondition + '}';
    }
}
