package org.bouncycastle.pqc.crypto.sphincs;

class Wots {
    static final int WOTS_L = 67;
    static final int WOTS_L1 = 64;
    static final int WOTS_LOGW = 4;
    static final int WOTS_LOG_L = 7;
    static final int WOTS_SIGBYTES = 2144;
    static final int WOTS_W = 16;

    Wots() {
    }

    private static void clear(byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 != i2; i3++) {
            bArr[i3 + i] = 0;
        }
    }

    static void expand_seed(byte[] bArr, int i, byte[] bArr2, int i2) {
        clear(bArr, i, WOTS_SIGBYTES);
        Seed.prg(bArr, i, 2144, bArr2, i2);
    }

    static void gen_chain(HashFunctions hashFunctions, byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int i3, int i4) {
        for (int i5 = 0; i5 < 32; i5++) {
            bArr[i5 + i] = bArr2[i5 + i2];
        }
        int i6 = 0;
        while (i6 < i4 && i6 < 16) {
            hashFunctions.hash_n_n_mask(bArr, i, bArr, i, bArr3, i3 + (i6 * 32));
            i6++;
        }
    }

    /* access modifiers changed from: package-private */
    public void wots_pkgen(HashFunctions hashFunctions, byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int i3) {
        expand_seed(bArr, i, bArr2, i2);
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < 67) {
                gen_chain(hashFunctions, bArr, i + (i5 * 32), bArr, i + (i5 * 32), bArr3, i3, 15);
                i4 = i5 + 1;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void wots_sign(HashFunctions hashFunctions, byte[] bArr, int i, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        int[] iArr = new int[67];
        int i2 = 0;
        int i3 = 0;
        while (i3 < 64) {
            iArr[i3] = bArr2[i3 / 2] & 15;
            iArr[i3 + 1] = (bArr2[i3 / 2] & 255) >>> 4;
            i2 = i2 + (15 - iArr[i3]) + (15 - iArr[i3 + 1]);
            i3 += 2;
        }
        while (i3 < 67) {
            iArr[i3] = i2 & 15;
            i2 >>>= 4;
            i3++;
        }
        expand_seed(bArr, i, bArr3, 0);
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < 67) {
                gen_chain(hashFunctions, bArr, i + (i5 * 32), bArr, i + (i5 * 32), bArr4, 0, iArr[i5]);
                i4 = i5 + 1;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void wots_verify(HashFunctions hashFunctions, byte[] bArr, byte[] bArr2, int i, byte[] bArr3, byte[] bArr4) {
        int[] iArr = new int[67];
        int i2 = 0;
        int i3 = 0;
        while (i3 < 64) {
            iArr[i3] = bArr3[i3 / 2] & 15;
            iArr[i3 + 1] = (bArr3[i3 / 2] & 255) >>> 4;
            i2 = i2 + (15 - iArr[i3]) + (15 - iArr[i3 + 1]);
            i3 += 2;
        }
        while (i3 < 67) {
            iArr[i3] = i2 & 15;
            i2 >>>= 4;
            i3++;
        }
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < 67) {
                HashFunctions hashFunctions2 = hashFunctions;
                byte[] bArr5 = bArr;
                gen_chain(hashFunctions2, bArr5, i5 * 32, bArr2, i + (i5 * 32), bArr4, iArr[i5] * 32, 15 - iArr[i5]);
                i4 = i5 + 1;
            } else {
                return;
            }
        }
    }
}
