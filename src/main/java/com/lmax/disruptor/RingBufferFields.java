package com.lmax.disruptor;

import com.lmax.disruptor.util.Util;
import sun.misc.Unsafe;

/* compiled from: RingBuffer */
abstract class RingBufferFields<E> extends RingBufferPad {
    private static final int BUFFER_PAD;
    private static final long REF_ARRAY_BASE = ((long) (UNSAFE.arrayBaseOffset(Object[].class) + (BUFFER_PAD << REF_ELEMENT_SHIFT)));
    private static final int REF_ELEMENT_SHIFT;
    private static final Unsafe UNSAFE = Util.getUnsafe();
    protected final int bufferSize;
    private final Object[] entries;
    private final long indexMask;
    protected final Sequencer sequencer;

    static {
        int scale = UNSAFE.arrayIndexScale(Object[].class);
        if (4 == scale) {
            REF_ELEMENT_SHIFT = 2;
        } else if (8 == scale) {
            REF_ELEMENT_SHIFT = 3;
        } else {
            throw new IllegalStateException("Unknown pointer size");
        }
        BUFFER_PAD = 128 / scale;
    }

    RingBufferFields(EventFactory<E> eventFactory, Sequencer sequencer2) {
        this.sequencer = sequencer2;
        this.bufferSize = sequencer2.getBufferSize();
        if (this.bufferSize < 1) {
            throw new IllegalArgumentException("bufferSize must not be less than 1");
        } else if (Integer.bitCount(this.bufferSize) != 1) {
            throw new IllegalArgumentException("bufferSize must be a power of 2");
        } else {
            this.indexMask = (long) (this.bufferSize - 1);
            this.entries = new Object[(sequencer2.getBufferSize() + (BUFFER_PAD * 2))];
            fill(eventFactory);
        }
    }

    private void fill(EventFactory<E> eventFactory) {
        for (int i = 0; i < this.bufferSize; i++) {
            this.entries[BUFFER_PAD + i] = eventFactory.newInstance();
        }
    }

    /* access modifiers changed from: protected */
    public final E elementAt(long sequence) {
        return UNSAFE.getObject(this.entries, REF_ARRAY_BASE + ((this.indexMask & sequence) << REF_ELEMENT_SHIFT));
    }
}
