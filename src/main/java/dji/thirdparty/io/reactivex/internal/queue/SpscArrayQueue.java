package dji.thirdparty.io.reactivex.internal.queue;

import dji.thirdparty.io.reactivex.internal.fuseable.SimplePlainQueue;
import dji.thirdparty.io.reactivex.internal.util.Pow2;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class SpscArrayQueue<E> extends AtomicReferenceArray<E> implements SimplePlainQueue<E> {
    private static final Integer MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096);
    private static final long serialVersionUID = -1296597691183856449L;
    final AtomicLong consumerIndex = new AtomicLong();
    final int lookAheadStep;
    final int mask = (length() - 1);
    final AtomicLong producerIndex = new AtomicLong();
    long producerLookAhead;

    public SpscArrayQueue(int capacity) {
        super(Pow2.roundToPowerOfTwo(capacity));
        this.lookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP.intValue());
    }

    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException("Null is not a valid element");
        }
        int mask2 = this.mask;
        long index = this.producerIndex.get();
        int offset = calcElementOffset(index, mask2);
        if (index >= this.producerLookAhead) {
            int step = this.lookAheadStep;
            if (lvElement(calcElementOffset(((long) step) + index, mask2)) == null) {
                this.producerLookAhead = ((long) step) + index;
            } else if (lvElement(offset) != null) {
                return false;
            }
        }
        soElement(offset, e);
        soProducerIndex(1 + index);
        return true;
    }

    public boolean offer(E v1, E v2) {
        return offer(v1) && offer(v2);
    }

    public E poll() {
        long index = this.consumerIndex.get();
        int offset = calcElementOffset(index);
        E e = lvElement(offset);
        if (e == null) {
            return null;
        }
        soConsumerIndex(1 + index);
        soElement(offset, null);
        return e;
    }

    public boolean isEmpty() {
        return this.producerIndex.get() == this.consumerIndex.get();
    }

    /* access modifiers changed from: package-private */
    public void soProducerIndex(long newIndex) {
        this.producerIndex.lazySet(newIndex);
    }

    /* access modifiers changed from: package-private */
    public void soConsumerIndex(long newIndex) {
        this.consumerIndex.lazySet(newIndex);
    }

    public void clear() {
        while (true) {
            if (poll() == null && isEmpty()) {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int calcElementOffset(long index, int mask2) {
        return ((int) index) & mask2;
    }

    /* access modifiers changed from: package-private */
    public int calcElementOffset(long index) {
        return ((int) index) & this.mask;
    }

    /* access modifiers changed from: package-private */
    public void soElement(int offset, E value) {
        lazySet(offset, value);
    }

    /* access modifiers changed from: package-private */
    public E lvElement(int offset) {
        return get(offset);
    }
}
