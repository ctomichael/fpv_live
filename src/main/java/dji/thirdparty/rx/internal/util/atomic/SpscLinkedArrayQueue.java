package dji.thirdparty.rx.internal.util.atomic;

import dji.thirdparty.rx.internal.util.unsafe.Pow2;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class SpscLinkedArrayQueue<T> implements Queue<T> {
    private static final Object HAS_NEXT = new Object();
    static final int MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096).intValue();
    protected AtomicReferenceArray<Object> consumerBuffer;
    protected final AtomicLong consumerIndex = new AtomicLong();
    protected int consumerMask;
    protected AtomicReferenceArray<Object> producerBuffer;
    protected final AtomicLong producerIndex = new AtomicLong();
    protected long producerLookAhead;
    protected int producerLookAheadStep;
    protected int producerMask;

    public SpscLinkedArrayQueue(int bufferSize) {
        int p2capacity = Pow2.roundToPowerOfTwo(bufferSize);
        int mask = p2capacity - 1;
        AtomicReferenceArray<Object> buffer = new AtomicReferenceArray<>(p2capacity + 1);
        this.producerBuffer = buffer;
        this.producerMask = mask;
        adjustLookAheadStep(p2capacity);
        this.consumerBuffer = buffer;
        this.consumerMask = mask;
        this.producerLookAhead = (long) (mask - 1);
    }

    public final boolean offer(T e) {
        AtomicReferenceArray<Object> buffer = this.producerBuffer;
        long index = lpProducerIndex();
        int mask = this.producerMask;
        int offset = calcWrappedOffset(index, mask);
        if (index < this.producerLookAhead) {
            return writeToQueue(buffer, e, index, offset);
        }
        int lookAheadStep = this.producerLookAheadStep;
        if (lvElement(buffer, calcWrappedOffset(((long) lookAheadStep) + index, mask)) == null) {
            this.producerLookAhead = (((long) lookAheadStep) + index) - 1;
            return writeToQueue(buffer, e, index, offset);
        } else if (lvElement(buffer, calcWrappedOffset(1 + index, mask)) == null) {
            return writeToQueue(buffer, e, index, offset);
        } else {
            resize(buffer, index, offset, e, (long) mask);
            return true;
        }
    }

    private boolean writeToQueue(AtomicReferenceArray<Object> buffer, T e, long index, int offset) {
        soProducerIndex(1 + index);
        soElement(buffer, offset, e);
        return true;
    }

    private void resize(AtomicReferenceArray<Object> oldBuffer, long currIndex, int offset, T e, long mask) {
        AtomicReferenceArray<Object> newBuffer = new AtomicReferenceArray<>(oldBuffer.length());
        this.producerBuffer = newBuffer;
        this.producerLookAhead = (currIndex + mask) - 1;
        soProducerIndex(currIndex + 1);
        soElement(newBuffer, offset, e);
        soNext(oldBuffer, newBuffer);
        soElement(oldBuffer, offset, HAS_NEXT);
    }

    private void soNext(AtomicReferenceArray<Object> curr, AtomicReferenceArray<Object> next) {
        soElement(curr, calcDirectOffset(curr.length() - 1), next);
    }

    private AtomicReferenceArray<Object> lvNext(AtomicReferenceArray<Object> curr) {
        return (AtomicReferenceArray) lvElement(curr, calcDirectOffset(curr.length() - 1));
    }

    public final T poll() {
        AtomicReferenceArray<Object> buffer = this.consumerBuffer;
        long index = lpConsumerIndex();
        int mask = this.consumerMask;
        int offset = calcWrappedOffset(index, mask);
        Object e = lvElement(buffer, offset);
        boolean isNextBuffer = e == HAS_NEXT;
        if (e != null && !isNextBuffer) {
            soConsumerIndex(1 + index);
            soElement(buffer, offset, null);
            return e;
        } else if (isNextBuffer) {
            return newBufferPoll(lvNext(buffer), index, mask);
        } else {
            return null;
        }
    }

    private T newBufferPoll(AtomicReferenceArray<Object> nextBuffer, long index, int mask) {
        this.consumerBuffer = nextBuffer;
        int offsetInNew = calcWrappedOffset(index, mask);
        T n = lvElement(nextBuffer, offsetInNew);
        if (n == null) {
            return null;
        }
        soConsumerIndex(1 + index);
        soElement(nextBuffer, offsetInNew, null);
        return n;
    }

    public final T peek() {
        AtomicReferenceArray<Object> buffer = this.consumerBuffer;
        long index = lpConsumerIndex();
        int mask = this.consumerMask;
        Object e = lvElement(buffer, calcWrappedOffset(index, mask));
        if (e == HAS_NEXT) {
            return newBufferPeek(lvNext(buffer), index, mask);
        }
        return e;
    }

    public void clear() {
        while (true) {
            if (poll() == null && isEmpty()) {
                return;
            }
        }
    }

    private T newBufferPeek(AtomicReferenceArray<Object> nextBuffer, long index, int mask) {
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

    public boolean isEmpty() {
        return lvProducerIndex() == lvConsumerIndex();
    }

    private void adjustLookAheadStep(int capacity) {
        this.producerLookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP);
    }

    private long lvProducerIndex() {
        return this.producerIndex.get();
    }

    private long lvConsumerIndex() {
        return this.consumerIndex.get();
    }

    private long lpProducerIndex() {
        return this.producerIndex.get();
    }

    private long lpConsumerIndex() {
        return this.consumerIndex.get();
    }

    private void soProducerIndex(long v) {
        this.producerIndex.lazySet(v);
    }

    private void soConsumerIndex(long v) {
        this.consumerIndex.lazySet(v);
    }

    private static int calcWrappedOffset(long index, int mask) {
        return calcDirectOffset(((int) index) & mask);
    }

    private static int calcDirectOffset(int index) {
        return index;
    }

    private static void soElement(AtomicReferenceArray<Object> buffer, int offset, Object e) {
        buffer.lazySet(offset, e);
    }

    private static <E> Object lvElement(AtomicReferenceArray<Object> buffer, int offset) {
        return buffer.get(offset);
    }

    public final Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public <E> E[] toArray(E[] eArr) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    public T remove() {
        throw new UnsupportedOperationException();
    }

    public T element() {
        throw new UnsupportedOperationException();
    }

    public boolean offer(T first, T second) {
        AtomicReferenceArray<Object> buffer = this.producerBuffer;
        long p = lvProducerIndex();
        int m = this.producerMask;
        if (lvElement(buffer, calcWrappedOffset(p + 2, m)) == null) {
            int pi = calcWrappedOffset(p, m);
            soElement(buffer, pi + 1, second);
            soProducerIndex(p + 2);
            soElement(buffer, pi, first);
            return true;
        }
        AtomicReferenceArray<Object> newBuffer = new AtomicReferenceArray<>(buffer.length());
        this.producerBuffer = newBuffer;
        int pi2 = calcWrappedOffset(p, m);
        soElement(newBuffer, pi2 + 1, second);
        soElement(newBuffer, pi2, first);
        soNext(buffer, newBuffer);
        soProducerIndex(p + 2);
        soElement(buffer, pi2, HAS_NEXT);
        return true;
    }
}
