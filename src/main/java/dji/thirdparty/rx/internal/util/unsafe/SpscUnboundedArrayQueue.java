package dji.thirdparty.rx.internal.util.unsafe;

import java.util.Iterator;

public class SpscUnboundedArrayQueue<E> extends SpscUnboundedArrayQueueConsumerField<E> implements QueueProgressIndicators {
    private static final long C_INDEX_OFFSET;
    private static final Object HAS_NEXT = new Object();
    static final int MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096).intValue();
    private static final long P_INDEX_OFFSET;
    private static final long REF_ARRAY_BASE = ((long) UnsafeAccess.UNSAFE.arrayBaseOffset(Object[].class));
    private static final int REF_ELEMENT_SHIFT;

    static {
        int scale = UnsafeAccess.UNSAFE.arrayIndexScale(Object[].class);
        if (4 == scale) {
            REF_ELEMENT_SHIFT = 2;
        } else if (8 == scale) {
            REF_ELEMENT_SHIFT = 3;
        } else {
            throw new IllegalStateException("Unknown pointer size");
        }
        try {
            P_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(SpscUnboundedArrayQueueProducerFields.class.getDeclaredField("producerIndex"));
            try {
                C_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(SpscUnboundedArrayQueueConsumerField.class.getDeclaredField("consumerIndex"));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    public SpscUnboundedArrayQueue(int bufferSize) {
        int p2capacity = Pow2.roundToPowerOfTwo(bufferSize);
        long mask = (long) (p2capacity - 1);
        E[] buffer = (Object[]) new Object[(p2capacity + 1)];
        this.producerBuffer = buffer;
        this.producerMask = mask;
        adjustLookAheadStep(p2capacity);
        this.consumerBuffer = buffer;
        this.consumerMask = mask;
        this.producerLookAhead = mask - 1;
        soProducerIndex(0);
    }

    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public final boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException("Null is not a valid element");
        }
        E[] buffer = this.producerBuffer;
        long index = this.producerIndex;
        long mask = this.producerMask;
        long offset = calcWrappedOffset(index, mask);
        if (index < this.producerLookAhead) {
            return writeToQueue(buffer, e, index, offset);
        }
        int lookAheadStep = this.producerLookAheadStep;
        if (lvElement(buffer, calcWrappedOffset(((long) lookAheadStep) + index, mask)) == null) {
            this.producerLookAhead = (((long) lookAheadStep) + index) - 1;
            return writeToQueue(buffer, e, index, offset);
        } else if (lvElement(buffer, calcWrappedOffset(1 + index, mask)) != null) {
            return writeToQueue(buffer, e, index, offset);
        } else {
            resize(buffer, index, offset, e, mask);
            return true;
        }
    }

    private boolean writeToQueue(E[] buffer, E e, long index, long offset) {
        soProducerIndex(1 + index);
        soElement(buffer, offset, e);
        return true;
    }

    private void resize(E[] oldBuffer, long currIndex, long offset, E e, long mask) {
        E[] newBuffer = (Object[]) new Object[oldBuffer.length];
        this.producerBuffer = newBuffer;
        this.producerLookAhead = (currIndex + mask) - 1;
        soProducerIndex(currIndex + 1);
        soElement(newBuffer, offset, e);
        soNext(oldBuffer, newBuffer);
        soElement(oldBuffer, offset, HAS_NEXT);
    }

    private void soNext(E[] curr, E[] next) {
        soElement(curr, calcDirectOffset((long) (curr.length - 1)), next);
    }

    private E[] lvNext(E[] curr) {
        return (Object[]) lvElement(curr, calcDirectOffset((long) (curr.length - 1)));
    }

    public final E poll() {
        E[] buffer = this.consumerBuffer;
        long index = this.consumerIndex;
        long mask = this.consumerMask;
        long offset = calcWrappedOffset(index, mask);
        Object e = lvElement(buffer, offset);
        boolean isNextBuffer = e == HAS_NEXT;
        if (e != null && !isNextBuffer) {
            soConsumerIndex(1 + index);
            soElement(buffer, offset, null);
            return e;
        } else if (!isNextBuffer) {
            return null;
        } else {
            return newBufferPoll(lvNext(buffer), index, mask);
        }
    }

    private E newBufferPoll(E[] nextBuffer, long index, long mask) {
        this.consumerBuffer = nextBuffer;
        long offsetInNew = calcWrappedOffset(index, mask);
        E n = lvElement(nextBuffer, offsetInNew);
        if (n == null) {
            return null;
        }
        soConsumerIndex(1 + index);
        soElement(nextBuffer, offsetInNew, null);
        return n;
    }

    public final E peek() {
        E[] buffer = this.consumerBuffer;
        long index = this.consumerIndex;
        long mask = this.consumerMask;
        Object e = lvElement(buffer, calcWrappedOffset(index, mask));
        if (e != HAS_NEXT) {
            return e;
        }
        return newBufferPeek(lvNext(buffer), index, mask);
    }

    private E newBufferPeek(E[] nextBuffer, long index, long mask) {
        this.consumerBuffer = nextBuffer;
        return lvElement(nextBuffer, calcWrappedOffset(index, mask));
    }

    public final int size() {
        long before;
        long currentProducerIndex;
        long after = lvConsumerIndex();
        do {
            before = after;
            currentProducerIndex = lvProducerIndex();
            after = lvConsumerIndex();
        } while (before != after);
        return (int) (currentProducerIndex - after);
    }

    private void adjustLookAheadStep(int capacity) {
        this.producerLookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP);
    }

    private long lvProducerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, P_INDEX_OFFSET);
    }

    private long lvConsumerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, C_INDEX_OFFSET);
    }

    private void soProducerIndex(long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, P_INDEX_OFFSET, v);
    }

    private void soConsumerIndex(long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, C_INDEX_OFFSET, v);
    }

    private static long calcWrappedOffset(long index, long mask) {
        return calcDirectOffset(index & mask);
    }

    private static long calcDirectOffset(long index) {
        return REF_ARRAY_BASE + (index << REF_ELEMENT_SHIFT);
    }

    private static void soElement(Object[] buffer, long offset, Object e) {
        UnsafeAccess.UNSAFE.putOrderedObject(buffer, offset, e);
    }

    private static <E> Object lvElement(E[] buffer, long offset) {
        return UnsafeAccess.UNSAFE.getObjectVolatile(buffer, offset);
    }

    public long currentProducerIndex() {
        return lvProducerIndex();
    }

    public long currentConsumerIndex() {
        return lvConsumerIndex();
    }
}
