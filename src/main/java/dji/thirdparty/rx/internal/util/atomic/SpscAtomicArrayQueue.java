package dji.thirdparty.rx.internal.util.atomic;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class SpscAtomicArrayQueue<E> extends AtomicReferenceArrayQueue<E> {
    private static final Integer MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096);
    final AtomicLong consumerIndex = new AtomicLong();
    final int lookAheadStep;
    final AtomicLong producerIndex = new AtomicLong();
    protected long producerLookAhead;

    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    public SpscAtomicArrayQueue(int capacity) {
        super(capacity);
        this.lookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP.intValue());
    }

    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException("Null is not a valid element");
        }
        AtomicReferenceArray<E> buffer = this.buffer;
        int mask = this.mask;
        long index = this.producerIndex.get();
        int offset = calcElementOffset(index, mask);
        if (index >= this.producerLookAhead) {
            int step = this.lookAheadStep;
            if (lvElement(buffer, calcElementOffset(((long) step) + index, mask)) == null) {
                this.producerLookAhead = ((long) step) + index;
            } else if (lvElement(buffer, offset) != null) {
                return false;
            }
        }
        soProducerIndex(1 + index);
        soElement(buffer, offset, e);
        return true;
    }

    public E poll() {
        long index = this.consumerIndex.get();
        int offset = calcElementOffset(index);
        AtomicReferenceArray<E> lElementBuffer = this.buffer;
        E e = lvElement(lElementBuffer, offset);
        if (e == null) {
            return null;
        }
        soConsumerIndex(1 + index);
        soElement(lElementBuffer, offset, null);
        return e;
    }

    public E peek() {
        return lvElement(calcElementOffset(this.consumerIndex.get()));
    }

    public int size() {
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

    private void soProducerIndex(long newIndex) {
        this.producerIndex.lazySet(newIndex);
    }

    private void soConsumerIndex(long newIndex) {
        this.consumerIndex.lazySet(newIndex);
    }

    private long lvConsumerIndex() {
        return this.consumerIndex.get();
    }

    private long lvProducerIndex() {
        return this.producerIndex.get();
    }
}
