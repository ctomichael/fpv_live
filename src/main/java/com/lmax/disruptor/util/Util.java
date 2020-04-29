package com.lmax.disruptor.util;

import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.Sequence;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import kotlin.jvm.internal.LongCompanionObject;
import sun.misc.Unsafe;

public final class Util {
    private static final Unsafe THE_UNSAFE;

    public static int ceilingNextPowerOfTwo(int x) {
        return 1 << (32 - Integer.numberOfLeadingZeros(x - 1));
    }

    public static long getMinimumSequence(Sequence[] sequences) {
        return getMinimumSequence(sequences, LongCompanionObject.MAX_VALUE);
    }

    public static long getMinimumSequence(Sequence[] sequences, long minimum) {
        for (Sequence sequence : sequences) {
            minimum = Math.min(minimum, sequence.get());
        }
        return minimum;
    }

    public static Sequence[] getSequencesFor(EventProcessor... processors) {
        Sequence[] sequences = new Sequence[processors.length];
        for (int i = 0; i < sequences.length; i++) {
            sequences[i] = processors[i].getSequence();
        }
        return sequences;
    }

    static {
        try {
            THE_UNSAFE = (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() {
                /* class com.lmax.disruptor.util.Util.AnonymousClass1 */

                public Unsafe run() throws Exception {
                    Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    return (Unsafe) theUnsafe.get(null);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }

    public static Unsafe getUnsafe() {
        return THE_UNSAFE;
    }

    @Deprecated
    public static long getAddressFromDirectByteBuffer(ByteBuffer buffer) {
        try {
            Field addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            return addressField.getLong(buffer);
        } catch (Exception e) {
            throw new RuntimeException("Unable to address field from ByteBuffer", e);
        }
    }

    public static int log2(int i) {
        int r = 0;
        while (true) {
            i >>= 1;
            if (i == 0) {
                return r;
            }
            r++;
        }
    }
}
