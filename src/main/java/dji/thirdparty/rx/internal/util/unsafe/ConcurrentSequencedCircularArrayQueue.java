package dji.thirdparty.rx.internal.util.unsafe;

public abstract class ConcurrentSequencedCircularArrayQueue<E> extends ConcurrentCircularArrayQueue<E> {
    private static final long ARRAY_BASE = ((long) (UnsafeAccess.UNSAFE.arrayBaseOffset(long[].class) + (32 << (ELEMENT_SHIFT - SPARSE_SHIFT))));
    private static final int ELEMENT_SHIFT = (SPARSE_SHIFT + 3);
    protected final long[] sequenceBuffer;

    static {
        if (8 == UnsafeAccess.UNSAFE.arrayIndexScale(long[].class)) {
            return;
        }
        throw new IllegalStateException("Unexpected long[] element size");
    }

    public ConcurrentSequencedCircularArrayQueue(int capacity) {
        super(capacity);
        int actualCapacity = (int) (this.mask + 1);
        this.sequenceBuffer = new long[((actualCapacity << SPARSE_SHIFT) + 64)];
        for (long i = 0; i < ((long) actualCapacity); i++) {
            soSequence(this.sequenceBuffer, calcSequenceOffset(i), i);
        }
    }

    /* access modifiers changed from: protected */
    public final long calcSequenceOffset(long index) {
        return ARRAY_BASE + ((this.mask & index) << ELEMENT_SHIFT);
    }

    /* access modifiers changed from: protected */
    public final void soSequence(long[] buffer, long offset, long e) {
        UnsafeAccess.UNSAFE.putOrderedLong(buffer, offset, e);
    }

    /* access modifiers changed from: protected */
    public final long lvSequence(long[] buffer, long offset) {
        return UnsafeAccess.UNSAFE.getLongVolatile(buffer, offset);
    }
}
