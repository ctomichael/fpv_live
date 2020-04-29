package com.lmax.disruptor;

import com.lmax.disruptor.util.Util;
import java.util.concurrent.locks.LockSupport;

public final class SingleProducerSequencer extends SingleProducerSequencerFields {
    protected long p1;
    protected long p2;
    protected long p3;
    protected long p4;
    protected long p5;
    protected long p6;
    protected long p7;

    public SingleProducerSequencer(int bufferSize, WaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
    }

    public boolean hasAvailableCapacity(int requiredCapacity) {
        return hasAvailableCapacity(requiredCapacity, false);
    }

    private boolean hasAvailableCapacity(int requiredCapacity, boolean doStore) {
        long nextValue = this.nextValue;
        long wrapPoint = (((long) requiredCapacity) + nextValue) - ((long) this.bufferSize);
        long cachedGatingSequence = this.cachedValue;
        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > nextValue) {
            if (doStore) {
                this.cursor.setVolatile(nextValue);
            }
            long minSequence = Util.getMinimumSequence(this.gatingSequences, nextValue);
            this.cachedValue = minSequence;
            if (wrapPoint > minSequence) {
                return false;
            }
        }
        return true;
    }

    public long next() {
        return next(1);
    }

    public long next(int n) {
        long minSequence;
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }
        long nextValue = this.nextValue;
        long nextSequence = nextValue + ((long) n);
        long wrapPoint = nextSequence - ((long) this.bufferSize);
        long cachedGatingSequence = this.cachedValue;
        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > nextValue) {
            this.cursor.setVolatile(nextValue);
            while (true) {
                minSequence = Util.getMinimumSequence(this.gatingSequences, nextValue);
                if (wrapPoint <= minSequence) {
                    break;
                }
                LockSupport.parkNanos(1);
            }
            this.cachedValue = minSequence;
        }
        this.nextValue = nextSequence;
        return nextSequence;
    }

    public long tryNext() throws InsufficientCapacityException {
        return tryNext(1);
    }

    public long tryNext(int n) throws InsufficientCapacityException {
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        } else if (!hasAvailableCapacity(n, true)) {
            throw InsufficientCapacityException.INSTANCE;
        } else {
            long nextSequence = this.nextValue + ((long) n);
            this.nextValue = nextSequence;
            return nextSequence;
        }
    }

    public long remainingCapacity() {
        long nextValue = this.nextValue;
        return ((long) getBufferSize()) - (nextValue - Util.getMinimumSequence(this.gatingSequences, nextValue));
    }

    public void claim(long sequence) {
        this.nextValue = sequence;
    }

    public void publish(long sequence) {
        this.cursor.set(sequence);
        this.waitStrategy.signalAllWhenBlocking();
    }

    public void publish(long lo, long hi) {
        publish(hi);
    }

    public boolean isAvailable(long sequence) {
        return sequence <= this.cursor.get();
    }

    public long getHighestPublishedSequence(long lowerBound, long availableSequence) {
        return availableSequence;
    }
}
