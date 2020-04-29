package com.lmax.disruptor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimeoutBlockingWaitStrategy implements WaitStrategy {
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = this.lock.newCondition();
    private final long timeoutInNanos;

    public TimeoutBlockingWaitStrategy(long timeout, TimeUnit units) {
        this.timeoutInNanos = units.toNanos(timeout);
    }

    public long waitFor(long sequence, Sequence cursorSequence, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException, InterruptedException, TimeoutException {
        long nanos = this.timeoutInNanos;
        if (cursorSequence.get() < sequence) {
            this.lock.lock();
            do {
                try {
                    if (cursorSequence.get() < sequence) {
                        barrier.checkAlert();
                        nanos = this.processorNotifyCondition.awaitNanos(nanos);
                    }
                } finally {
                    this.lock.unlock();
                }
            } while (nanos > 0);
            throw TimeoutException.INSTANCE;
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
        return "TimeoutBlockingWaitStrategy{processorNotifyCondition=" + this.processorNotifyCondition + '}';
    }
}
