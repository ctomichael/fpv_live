package dji.thirdparty.rx.internal.util.atomic;

import dji.thirdparty.rx.internal.util.unsafe.Pow2;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReferenceArray;

abstract class AtomicReferenceArrayQueue<E> extends AbstractQueue<E> {
    protected final AtomicReferenceArray<E> buffer;
    protected final int mask;

    public AtomicReferenceArrayQueue(int capacity) {
        int actualCapacity = Pow2.roundToPowerOfTwo(capacity);
        this.mask = actualCapacity - 1;
        this.buffer = new AtomicReferenceArray<>(actualCapacity);
    }

    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        while (true) {
            if (poll() == null && isEmpty()) {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public final int calcElementOffset(long index, int mask2) {
        return ((int) index) & mask2;
    }

    /* access modifiers changed from: protected */
    public final int calcElementOffset(long index) {
        return ((int) index) & this.mask;
    }

    /* access modifiers changed from: protected */
    public final E lvElement(AtomicReferenceArray<E> buffer2, int offset) {
        return buffer2.get(offset);
    }

    /* access modifiers changed from: protected */
    public final E lpElement(AtomicReferenceArray<E> buffer2, int offset) {
        return buffer2.get(offset);
    }

    /* access modifiers changed from: protected */
    public final E lpElement(int offset) {
        return this.buffer.get(offset);
    }

    /* access modifiers changed from: protected */
    public final void spElement(AtomicReferenceArray<E> buffer2, int offset, E value) {
        buffer2.lazySet(offset, value);
    }

    /* access modifiers changed from: protected */
    public final void spElement(int offset, E value) {
        this.buffer.lazySet(offset, value);
    }

    /* access modifiers changed from: protected */
    public final void soElement(AtomicReferenceArray<E> buffer2, int offset, E value) {
        buffer2.lazySet(offset, value);
    }

    /* access modifiers changed from: protected */
    public final void soElement(int offset, E value) {
        this.buffer.lazySet(offset, value);
    }

    /* access modifiers changed from: protected */
    public final void svElement(AtomicReferenceArray<E> buffer2, int offset, E value) {
        buffer2.set(offset, value);
    }

    /* access modifiers changed from: protected */
    public final E lvElement(int offset) {
        return lvElement(this.buffer, offset);
    }
}
