package dji.thirdparty.rx.internal.util.unsafe;

import java.util.Iterator;

public abstract class ConcurrentCircularArrayQueue<E> extends ConcurrentCircularArrayQueueL0Pad<E> {
    protected static final int BUFFER_PAD = 32;
    private static final long REF_ARRAY_BASE = ((long) (UnsafeAccess.UNSAFE.arrayBaseOffset(Object[].class) + (32 << (REF_ELEMENT_SHIFT - SPARSE_SHIFT))));
    private static final int REF_ELEMENT_SHIFT;
    protected static final int SPARSE_SHIFT = Integer.getInteger("sparse.shift", 0).intValue();
    protected final E[] buffer;
    protected final long mask;

    static {
        int scale = UnsafeAccess.UNSAFE.arrayIndexScale(Object[].class);
        if (4 == scale) {
            REF_ELEMENT_SHIFT = SPARSE_SHIFT + 2;
        } else if (8 == scale) {
            REF_ELEMENT_SHIFT = SPARSE_SHIFT + 3;
        } else {
            throw new IllegalStateException("Unknown pointer size");
        }
    }

    public ConcurrentCircularArrayQueue(int capacity) {
        int actualCapacity = Pow2.roundToPowerOfTwo(capacity);
        this.mask = (long) (actualCapacity - 1);
        this.buffer = (Object[]) new Object[((actualCapacity << SPARSE_SHIFT) + 64)];
    }

    /* access modifiers changed from: protected */
    public final long calcElementOffset(long index) {
        return calcElementOffset(index, this.mask);
    }

    /* access modifiers changed from: protected */
    public final long calcElementOffset(long index, long mask2) {
        return REF_ARRAY_BASE + ((index & mask2) << REF_ELEMENT_SHIFT);
    }

    /* access modifiers changed from: protected */
    public final void spElement(long offset, E e) {
        spElement(this.buffer, offset, e);
    }

    /* access modifiers changed from: protected */
    public final void spElement(E[] buffer2, long offset, E e) {
        UnsafeAccess.UNSAFE.putObject(buffer2, offset, e);
    }

    /* access modifiers changed from: protected */
    public final void soElement(long offset, E e) {
        soElement(this.buffer, offset, e);
    }

    /* access modifiers changed from: protected */
    public final void soElement(E[] buffer2, long offset, E e) {
        UnsafeAccess.UNSAFE.putOrderedObject(buffer2, offset, e);
    }

    /* access modifiers changed from: protected */
    public final E lpElement(long offset) {
        return lpElement(this.buffer, offset);
    }

    /* access modifiers changed from: protected */
    public final E lpElement(E[] buffer2, long offset) {
        return UnsafeAccess.UNSAFE.getObject(buffer2, offset);
    }

    /* access modifiers changed from: protected */
    public final E lvElement(long offset) {
        return lvElement(this.buffer, offset);
    }

    /* access modifiers changed from: protected */
    public final E lvElement(E[] buffer2, long offset) {
        return UnsafeAccess.UNSAFE.getObjectVolatile(buffer2, offset);
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
}
