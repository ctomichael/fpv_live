package com.lmax.disruptor;

import com.lmax.disruptor.util.Util;
import java.util.concurrent.locks.LockSupport;
import sun.misc.Unsafe;

public final class MultiProducerSequencer extends AbstractSequencer {
    private static final long BASE = ((long) UNSAFE.arrayBaseOffset(int[].class));
    private static final long SCALE = ((long) UNSAFE.arrayIndexScale(int[].class));
    private static final Unsafe UNSAFE = Util.getUnsafe();
    private final int[] availableBuffer;
    private final Sequence gatingSequenceCache = new Sequence(-1);
    private final int indexMask;
    private final int indexShift;

    public MultiProducerSequencer(int bufferSize, WaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
        this.availableBuffer = new int[bufferSize];
        this.indexMask = bufferSize - 1;
        this.indexShift = Util.log2(bufferSize);
        initialiseAvailableBuffer();
    }

    public boolean hasAvailableCapacity(int requiredCapacity) {
        return hasAvailableCapacity(this.gatingSequences, requiredCapacity, this.cursor.get());
    }

    private boolean hasAvailableCapacity(Sequence[] gatingSequences, int requiredCapacity, long cursorValue) {
        long wrapPoint = (((long) requiredCapacity) + cursorValue) - ((long) this.bufferSize);
        long cachedGatingSequence = this.gatingSequenceCache.get();
        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > cursorValue) {
            long minSequence = Util.getMinimumSequence(gatingSequences, cursorValue);
            this.gatingSequenceCache.set(minSequence);
            if (wrapPoint > minSequence) {
                return false;
            }
        }
        return true;
    }

    public void claim(long sequence) {
        this.cursor.set(sequence);
    }

    public long next() {
        return next(1);
    }

    public long next(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }
        while (true) {
            long current = this.cursor.get();
            long next = current + ((long) n);
            long wrapPoint = next - ((long) this.bufferSize);
            long cachedGatingSequence = this.gatingSequenceCache.get();
            if (wrapPoint > cachedGatingSequence || cachedGatingSequence > current) {
                long gatingSequence = Util.getMinimumSequence(this.gatingSequences, current);
                if (wrapPoint > gatingSequence) {
                    LockSupport.parkNanos(1);
                } else {
                    this.gatingSequenceCache.set(gatingSequence);
                }
            } else if (this.cursor.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    public long tryNext() throws InsufficientCapacityException {
        return tryNext(1);
    }

    public long tryNext(int n) throws InsufficientCapacityException {
        long current;
        long next;
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }
        do {
            current = this.cursor.get();
            next = current + ((long) n);
            if (!hasAvailableCapacity(this.gatingSequences, n, current)) {
                throw InsufficientCapacityException.INSTANCE;
            }
        } while (!this.cursor.compareAndSet(current, next));
        return next;
    }

    public long remainingCapacity() {
        long consumed = Util.getMinimumSequence(this.gatingSequences, this.cursor.get());
        return ((long) getBufferSize()) - (this.cursor.get() - consumed);
    }

    private void initialiseAvailableBuffer() {
        for (int i = this.availableBuffer.length - 1; i != 0; i--) {
            setAvailableBufferValue(i, -1);
        }
        setAvailableBufferValue(0, -1);
    }

    public void publish(long sequence) {
        setAvailable(sequence);
        this.waitStrategy.signalAllWhenBlocking();
    }

    public void publish(long lo, long hi) {
        for (long l = lo; l <= hi; l++) {
            setAvailable(l);
        }
        this.waitStrategy.signalAllWhenBlocking();
    }

    private void setAvailable(long sequence) {
        setAvailableBufferValue(calculateIndex(sequence), calculateAvailabilityFlag(sequence));
    }

    private void setAvailableBufferValue(int index, int flag) {
        UNSAFE.putOrderedInt(this.availableBuffer, (((long) index) * SCALE) + BASE, flag);
    }

    public boolean isAvailable(long sequence) {
        return UNSAFE.getIntVolatile(this.availableBuffer, (((long) calculateIndex(sequence)) * SCALE) + BASE) == calculateAvailabilityFlag(sequence);
    }

    public long getHighestPublishedSequence(long lowerBound, long availableSequence) {
        for (long sequence = lowerBound; sequence <= availableSequence; sequence++) {
            if (!isAvailable(sequence)) {
                return sequence - 1;
            }
        }
        return availableSequence;
    }

    private int calculateAvailabilityFlag(long sequence) {
        return (int) (sequence >>> this.indexShift);
    }

    private int calculateIndex(long sequence) {
        return ((int) sequence) & this.indexMask;
    }
}
