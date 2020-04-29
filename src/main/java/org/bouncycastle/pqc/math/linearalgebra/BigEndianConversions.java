package org.bouncycastle.pqc.math.linearalgebra;

public final class BigEndianConversions {
    private BigEndianConversions() {
    }

    public static void I2OSP(int i, byte[] bArr, int i2) {
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 24);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i >>> 16);
        bArr[i4] = (byte) (i >>> 8);
        bArr[i4 + 1] = (byte) i;
    }

    public static void I2OSP(int i, byte[] bArr, int i2, int i3) {
        for (int i4 = i3 - 1; i4 >= 0; i4--) {
            bArr[i2 + i4] = (byte) (i >>> (((i3 - 1) - i4) * 8));
        }
    }

    public static void I2OSP(long j, byte[] bArr, int i) {
        int i2 = i + 1;
        bArr[i] = (byte) ((int) (j >>> 56));
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((int) (j >>> 48));
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((int) (j >>> 40));
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((int) (j >>> 32));
        int i6 = i5 + 1;
        bArr[i5] = (byte) ((int) (j >>> 24));
        int i7 = i6 + 1;
        bArr[i6] = (byte) ((int) (j >>> 16));
        bArr[i7] = (byte) ((int) (j >>> 8));
        bArr[i7 + 1] = (byte) ((int) j);
    }

    public static byte[] I2OSP(int i) {
        return new byte[]{(byte) (i >>> 24), (byte) (i >>> 16), (byte) (i >>> 8), (byte) i};
    }

    public static byte[] I2OSP(int i, int i2) throws ArithmeticException {
        if (i < 0) {
            return null;
        }
        int ceilLog256 = IntegerFunctions.ceilLog256(i);
        if (ceilLog256 > i2) {
            throw new ArithmeticException("Cannot encode given integer into specified number of octets.");
        }
        byte[] bArr = new byte[i2];
        for (int i3 = i2 - 1; i3 >= i2 - ceilLog256; i3--) {
            bArr[i3] = (byte) (i >>> (((i2 - 1) - i3) * 8));
        }
        return bArr;
    }

    public static byte[] I2OSP(long j) {
        return new byte[]{(byte) ((int) (j >>> 56)), (byte) ((int) (j >>> 48)), (byte) ((int) (j >>> 40)), (byte) ((int) (j >>> 32)), (byte) ((int) (j >>> 24)), (byte) ((int) (j >>> 16)), (byte) ((int) (j >>> 8)), (byte) ((int) j)};
    }

    public static int OS2IP(byte[] bArr) {
        int i = 0;
        if (bArr.length > 4) {
            throw new ArithmeticException("invalid input length");
        } else if (bArr.length == 0) {
            return 0;
        } else {
            int i2 = 0;
            while (true) {
                int i3 = i;
                if (i2 >= bArr.length) {
                    return i3;
                }
                i = ((bArr[i2] & 255) << (((bArr.length - 1) - i2) * 8)) | i3;
                i2++;
            }
        }
    }

    public static int OS2IP(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        return ((bArr[i2] & 255) << Tnaf.POW_2_WIDTH) | ((bArr[i] & 255) << 24) | ((bArr[i3] & 255) << 8) | (bArr[i3 + 1] & 255);
    }

    public static int OS2IP(byte[] bArr, int i, int i2) {
        if (bArr.length == 0 || bArr.length < (i + i2) - 1) {
            return 0;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            i3 |= (bArr[i + i4] & 255) << (((i2 - i4) - 1) * 8);
        }
        return i3;
    }

    public static long OS2LIP(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        int i4 = i3 + 1;
        int i5 = i4 + 1;
        int i6 = i5 + 1;
        int i7 = i6 + 1;
        return ((long) (bArr[i7 + 1] & 255)) | ((((long) bArr[i]) & 255) << 56) | ((((long) bArr[i2]) & 255) << 48) | ((((long) bArr[i3]) & 255) << 40) | ((((long) bArr[i4]) & 255) << 32) | ((((long) bArr[i5]) & 255) << 24) | ((long) ((bArr[i6] & 255) << Tnaf.POW_2_WIDTH)) | ((long) ((bArr[i7] & 255) << 8));
    }

    public static byte[] toByteArray(int[] iArr) {
        byte[] bArr = new byte[(iArr.length << 2)];
        for (int i = 0; i < iArr.length; i++) {
            I2OSP(iArr[i], bArr, i << 2);
        }
        return bArr;
    }

    public static byte[] toByteArray(int[] iArr, int i) {
        int i2 = 0;
        int length = iArr.length;
        byte[] bArr = new byte[i];
        int i3 = 0;
        while (true) {
            int i4 = i2;
            if (i3 <= length - 2) {
                I2OSP(iArr[i3], bArr, i4);
                i3++;
                i2 = i4 + 4;
            } else {
                I2OSP(iArr[length - 1], bArr, i4, i - i4);
                return bArr;
            }
        }
    }

    public static int[] toIntArray(byte[] bArr) {
        int i;
        int i2 = 0;
        int length = (bArr.length + 3) / 4;
        int length2 = bArr.length & 3;
        int[] iArr = new int[length];
        int i3 = 0;
        while (true) {
            i = i2;
            if (i3 > length - 2) {
                break;
            }
            iArr[i3] = OS2IP(bArr, i);
            i3++;
            i2 = i + 4;
        }
        if (length2 != 0) {
            iArr[length - 1] = OS2IP(bArr, i, length2);
        } else {
            iArr[length - 1] = OS2IP(bArr, i);
        }
        return iArr;
    }
}
