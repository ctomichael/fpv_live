package dji.thirdparty.rx.internal.util.atomic;

import dji.thirdparty.rx.internal.util.unsafe.Pow2;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class SpscExactAtomicArrayQueue<T> extends AtomicReferenceArray<T> implements Queue<T> {
    static final AtomicLongFieldUpdater<SpscExactAtomicArrayQueue> CONSUMER_INDEX = AtomicLongFieldUpdater.newUpdater(SpscExactAtomicArrayQueue.class, "consumerIndex");
    static final AtomicLongFieldUpdater<SpscExactAtomicArrayQueue> PRODUCER_INDEX = AtomicLongFieldUpdater.newUpdater(SpscExactAtomicArrayQueue.class, "producerIndex");
    private static final long serialVersionUID = 6210984603741293445L;
    final int capacitySkip;
    volatile long consumerIndex;
    final int mask;
    volatile long producerIndex;

    public SpscExactAtomicArrayQueue(int capacity) {
        super(Pow2.roundToPowerOfTwo(capacity));
        int len = length();
        this.mask = len - 1;
        this.capacitySkip = len - capacity;
    }

    public boolean offer(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        long pi = this.producerIndex;
        int m = this.mask;
        if (get(((int) (((long) this.capacitySkip) + pi)) & m) != null) {
            return false;
        }
        PRODUCER_INDEX.lazySet(this, 1 + pi);
        lazySet(((int) pi) & m, value);
        return true;
    }

    public T poll() {
        long ci = this.consumerIndex;
        int offset = ((int) ci) & this.mask;
        T value = get(offset);
        if (value == null) {
            return null;
        }
        CONSUMER_INDEX.lazySet(this, 1 + ci);
        lazySet(offset, null);
        return value;
    }

    public T peek() {
        return get(((int) this.consumerIndex) & this.mask);
    }

    public void clear() {
        while (true) {
            if (poll() == null && isEmpty()) {
                return;
            }
        }
    }

    public boolean isEmpty() {
        return this.producerIndex == this.consumerIndex;
    }

    public int size() {
        long ci = this.consumerIndex;
        while (true) {
            long pi = this.producerIndex;
            long ci2 = this.consumerIndex;
            if (ci == ci2) {
                return (int) (pi - ci2);
            }
            ci = ci2;
        }
    }

    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator() {
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
}
