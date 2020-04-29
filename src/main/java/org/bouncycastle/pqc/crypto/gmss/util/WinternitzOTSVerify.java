package org.bouncycastle.pqc.crypto.gmss.util;

import org.bouncycastle.crypto.Digest;

public class WinternitzOTSVerify {
    private Digest messDigestOTS;
    private int w;

    public WinternitzOTSVerify(Digest digest, int i) {
        this.w = i;
        this.messDigestOTS = digest;
    }

    public byte[] Verify(byte[] bArr, byte[] bArr2) {
        int i;
        int digestSize = this.messDigestOTS.getDigestSize();
        byte[] bArr3 = new byte[digestSize];
        this.messDigestOTS.update(bArr, 0, bArr.length);
        byte[] bArr4 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr4, 0);
        int i2 = ((digestSize << 3) + (this.w - 1)) / this.w;
        int log = getLog((i2 << this.w) + 1);
        int i3 = ((((this.w + log) - 1) / this.w) + i2) * digestSize;
        if (i3 != bArr2.length) {
            return null;
        }
        byte[] bArr5 = new byte[i3];
        byte b = 0;
        int i4 = 0;
        if (8 % this.w == 0) {
            int i5 = 8 / this.w;
            int i6 = (1 << this.w) - 1;
            byte[] bArr6 = new byte[digestSize];
            for (int i7 = 0; i7 < bArr4.length; i7++) {
                int i8 = 0;
                while (i8 < i5) {
                    byte b2 = bArr4[i7] & i6;
                    int i9 = b + b2;
                    System.arraycopy(bArr2, i4 * digestSize, bArr6, 0, digestSize);
                    for (int i10 = b2; i10 < i6; i10++) {
                        this.messDigestOTS.update(bArr6, 0, bArr6.length);
                        bArr6 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(bArr6, 0);
                    }
                    System.arraycopy(bArr6, 0, bArr5, i4 * digestSize, digestSize);
                    bArr4[i7] = (byte) (bArr4[i7] >>> this.w);
                    i4++;
                    i8++;
                    b = i9;
                }
            }
            int i11 = (i2 << this.w) - b;
            int i12 = 0;
            byte[] bArr7 = bArr6;
            while (i12 < log) {
                System.arraycopy(bArr2, i4 * digestSize, bArr7, 0, digestSize);
                for (int i13 = i11 & i6; i13 < i6; i13++) {
                    this.messDigestOTS.update(bArr7, 0, bArr7.length);
                    bArr7 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr7, 0);
                }
                System.arraycopy(bArr7, 0, bArr5, i4 * digestSize, digestSize);
                i11 >>>= this.w;
                i4++;
                i12 = this.w + i12;
            }
        } else if (this.w < 8) {
            int i14 = digestSize / this.w;
            int i15 = (1 << this.w) - 1;
            byte[] bArr8 = new byte[digestSize];
            int i16 = 0;
            int i17 = 0;
            int i18 = 0;
            int i19 = 0;
            while (i17 < i14) {
                long j = 0;
                for (int i20 = 0; i20 < this.w; i20++) {
                    j ^= (long) ((bArr4[i16] & 255) << (i20 << 3));
                    i16++;
                }
                int i21 = 0;
                long j2 = j;
                byte[] bArr9 = bArr8;
                int i22 = i18;
                while (i21 < 8) {
                    int i23 = (int) (((long) i15) & j2);
                    i19 += i23;
                    System.arraycopy(bArr2, i22 * digestSize, bArr9, 0, digestSize);
                    while (i23 < i15) {
                        this.messDigestOTS.update(bArr9, 0, bArr9.length);
                        bArr9 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(bArr9, 0);
                        i23++;
                    }
                    System.arraycopy(bArr9, 0, bArr5, i22 * digestSize, digestSize);
                    i21++;
                    j2 >>>= this.w;
                    i22++;
                }
                i17++;
                bArr8 = bArr9;
                i18 = i22;
            }
            int i24 = digestSize % this.w;
            long j3 = 0;
            for (int i25 = 0; i25 < i24; i25++) {
                j3 ^= (long) ((bArr4[i16] & 255) << (i25 << 3));
                i16++;
            }
            int i26 = i24 << 3;
            int i27 = 0;
            byte[] bArr10 = bArr8;
            int i28 = i18;
            while (i27 < i26) {
                int i29 = (int) (((long) i15) & j3);
                int i30 = i19 + i29;
                System.arraycopy(bArr2, i28 * digestSize, bArr10, 0, digestSize);
                while (i29 < i15) {
                    this.messDigestOTS.update(bArr10, 0, bArr10.length);
                    bArr10 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr10, 0);
                    i29++;
                }
                System.arraycopy(bArr10, 0, bArr5, i28 * digestSize, digestSize);
                j3 >>>= this.w;
                i28++;
                i27 = this.w + i27;
                i19 = i30;
            }
            int i31 = (i2 << this.w) - i19;
            int i32 = 0;
            while (true) {
                int i33 = i32;
                if (i33 >= log) {
                    break;
                }
                System.arraycopy(bArr2, i28 * digestSize, bArr10, 0, digestSize);
                for (int i34 = i31 & i15; i34 < i15; i34++) {
                    this.messDigestOTS.update(bArr10, 0, bArr10.length);
                    bArr10 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr10, 0);
                }
                System.arraycopy(bArr10, 0, bArr5, i28 * digestSize, digestSize);
                i31 >>>= this.w;
                i28++;
                i32 = this.w + i33;
            }
        } else if (this.w < 57) {
            int i35 = (digestSize << 3) - this.w;
            int i36 = (1 << this.w) - 1;
            byte[] bArr11 = new byte[digestSize];
            int i37 = 0;
            int i38 = 0;
            while (i37 <= i35) {
                int i39 = i37 % 8;
                int i40 = i37 + this.w;
                int i41 = (i40 + 7) >>> 3;
                long j4 = 0;
                int i42 = 0;
                for (int i43 = i37 >>> 3; i43 < i41; i43++) {
                    j4 ^= (long) ((bArr4[i43] & 255) << (i42 << 3));
                    i42++;
                }
                long j5 = (j4 >>> i39) & ((long) i36);
                int i44 = (int) (((long) b) + j5);
                System.arraycopy(bArr2, i38 * digestSize, bArr11, 0, digestSize);
                for (long j6 = j5; j6 < ((long) i36); j6++) {
                    this.messDigestOTS.update(bArr11, 0, bArr11.length);
                    bArr11 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr11, 0);
                }
                System.arraycopy(bArr11, 0, bArr5, i38 * digestSize, digestSize);
                i38++;
                i37 = i40;
                b = i44;
            }
            int i45 = i37 >>> 3;
            if (i45 < digestSize) {
                int i46 = i37 % 8;
                long j7 = 0;
                int i47 = 0;
                while (i45 < digestSize) {
                    j7 ^= (long) ((bArr4[i45] & 255) << (i47 << 3));
                    i47++;
                    i45++;
                }
                long j8 = (j7 >>> i46) & ((long) i36);
                i = (int) (((long) b) + j8);
                System.arraycopy(bArr2, i38 * digestSize, bArr11, 0, digestSize);
                while (j8 < ((long) i36)) {
                    this.messDigestOTS.update(bArr11, 0, bArr11.length);
                    bArr11 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr11, 0);
                    j8++;
                }
                System.arraycopy(bArr11, 0, bArr5, i38 * digestSize, digestSize);
                i38++;
            } else {
                i = b;
            }
            int i48 = (i2 << this.w) - i;
            int i49 = 0;
            byte[] bArr12 = bArr11;
            while (true) {
                int i50 = i38;
                if (i49 >= log) {
                    break;
                }
                System.arraycopy(bArr2, i50 * digestSize, bArr12, 0, digestSize);
                for (long j9 = (long) (i48 & i36); j9 < ((long) i36); j9++) {
                    this.messDigestOTS.update(bArr12, 0, bArr12.length);
                    bArr12 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr12, 0);
                }
                System.arraycopy(bArr12, 0, bArr5, i50 * digestSize, digestSize);
                i48 >>>= this.w;
                i38 = i50 + 1;
                i49 = this.w + i49;
            }
        }
        byte[] bArr13 = new byte[digestSize];
        this.messDigestOTS.update(bArr5, 0, bArr5.length);
        byte[] bArr14 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr14, 0);
        return bArr14;
    }

    public int getLog(int i) {
        int i2 = 1;
        int i3 = 2;
        while (i3 < i) {
            i3 <<= 1;
            i2++;
        }
        return i2;
    }

    public int getSignatureLength() {
        int digestSize = this.messDigestOTS.getDigestSize();
        int i = ((digestSize << 3) + (this.w - 1)) / this.w;
        return digestSize * (i + (((getLog((i << this.w) + 1) + this.w) - 1) / this.w));
    }
}
