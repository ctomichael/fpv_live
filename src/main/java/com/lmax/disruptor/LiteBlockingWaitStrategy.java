package com.lmax.disruptor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class LiteBlockingWaitStrategy implements WaitStrategy {
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = this.lock.newCondition();
    private final AtomicBoolean signalNeeded = new AtomicBoolean(false);

    public long waitFor(long sequence, Sequence cursorSequence, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException, InterruptedException {
        if (cursorSequence.get() < sequence) {
            this.lock.lock();
            do {
                try {
                    this.signalNeeded.getAndSet(true);
                    if (cursorSequence.get() >= sequence) {
                        break;
                    }
                    barrier.checkAlert();
                    this.processorNotifyCondition.await();
                } finally {
                    this.lock.unlock();
                }
            } while (cursorSequence.get() < sequence);
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
        if (this.signalNeeded.getAndSet(false)) {
            this.lock.lock();
            try {
                this.processorNotifyCondition.signalAll();
            } finally {
                this.lock.unlock();
            }
        }
    }

    public String toString() {
        return "LiteBlockingWaitStrategy{processorNotifyCondition=" + this.processorNotifyCondition + '}';
    }
}
