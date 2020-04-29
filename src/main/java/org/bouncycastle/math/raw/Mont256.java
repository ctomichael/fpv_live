package org.bouncycastle.math.raw;

public abstract class Mont256 {
    private static final long M = 4294967295L;

    public static int inverse32(int i) {
        int i2 = (2 - (i * i)) * i;
        int i3 = i2 * (2 - (i * i2));
        int i4 = i3 * (2 - (i * i3));
        return i4 * (2 - (i * i4));
    }

    public static void multAdd(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i) {
        int i2;
        int i3 = 0;
        long j = ((long) iArr2[0]) & 4294967295L;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            i2 = i3;
            if (i5 >= 8) {
                break;
            }
            long j2 = ((long) iArr[i5]) & 4294967295L;
            long j3 = j2 * j;
            long j4 = (((long) iArr3[0]) & 4294967295L) + (4294967295L & j3);
            long j5 = ((long) (((int) j4) * i)) & 4294967295L;
            long j6 = (((long) iArr4[0]) & 4294967295L) * j5;
            long j7 = ((j4 + (4294967295L & j6)) >>> 32) + (j3 >>> 32) + (j6 >>> 32);
            for (int i6 = 1; i6 < 8; i6++) {
                long j8 = (((long) iArr2[i6]) & 4294967295L) * j2;
                long j9 = (((long) iArr4[i6]) & 4294967295L) * j5;
                long j10 = j7 + (4294967295L & j8) + (4294967295L & j9) + (((long) iArr3[i6]) & 4294967295L);
                iArr3[i6 - 1] = (int) j10;
                j7 = (j10 >>> 32) + (j8 >>> 32) + (j9 >>> 32);
            }
            long j11 = j7 + (((long) i2) & 4294967295L);
            iArr3[7] = (int) j11;
            i3 = (int) (j11 >>> 32);
            i4 = i5 + 1;
        }
        if (i2 != 0 || Nat256.gte(iArr3, iArr4)) {
            Nat256.sub(iArr3, iArr4, iArr3);
        }
    }

    public static void multAddXF(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4) {
        int i;
        int i2 = 0;
        long j = ((long) iArr2[0]) & 4294967295L;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            i = i2;
            if (i4 >= 8) {
                break;
            }
            long j2 = 4294967295L & ((long) iArr[i4]);
            long j3 = (j2 * j) + (((long) iArr3[0]) & 4294967295L);
            long j4 = 4294967295L & j3;
            long j5 = (j3 >>> 32) + j4;
            for (int i5 = 1; i5 < 8; i5++) {
                long j6 = (((long) iArr2[i5]) & 4294967295L) * j2;
                long j7 = (((long) iArr4[i5]) & 4294967295L) * j4;
                long j8 = j5 + (4294967295L & j6) + (4294967295L & j7) + (((long) iArr3[i5]) & 4294967295L);
                iArr3[i5 - 1] = (int) j8;
                j5 = (j8 >>> 32) + (j6 >>> 32) + (j7 >>> 32);
            }
            long j9 = j5 + (((long) i) & 4294967295L);
            iArr3[7] = (int) j9;
            i2 = (int) (j9 >>> 32);
            i3 = i4 + 1;
        }
        if (i != 0 || Nat256.gte(iArr3, iArr4)) {
            Nat256.sub(iArr3, iArr4, iArr3);
        }
    }

    public static void reduce(int[] iArr, int[] iArr2, int i) {
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= 8) {
                break;
            }
            int i4 = iArr[0];
            long j = 4294967295L & ((long) (i4 * i));
            long j2 = (((((long) iArr2[0]) & 4294967295L) * j) + (((long) i4) & 4294967295L)) >>> 32;
            for (int i5 = 1; i5 < 8; i5++) {
                long j3 = j2 + ((((long) iArr2[i5]) & 4294967295L) * j) + (((long) iArr[i5]) & 4294967295L);
                iArr[i5 - 1] = (int) j3;
                j2 = j3 >>> 32;
            }
            iArr[7] = (int) j2;
            i2 = i3 + 1;
        }
        if (Nat256.gte(iArr, iArr2)) {
            Nat256.sub(iArr, iArr2, iArr);
        }
    }

    public static void reduceXF(int[] iArr, int[] iArr2) {
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 8) {
                break;
            }
            long j = 4294967295L & ((long) iArr[0]);
            long j2 = j;
            for (int i3 = 1; i3 < 8; i3++) {
                long j3 = j2 + ((((long) iArr2[i3]) & 4294967295L) * j) + (((long) iArr[i3]) & 4294967295L);
                iArr[i3 - 1] = (int) j3;
                j2 = j3 >>> 32;
            }
            iArr[7] = (int) j2;
            i = i2 + 1;
        }
        if (Nat256.gte(iArr, iArr2)) {
            Nat256.sub(iArr, iArr2, iArr);
        }
    }
}
