package org.bouncycastle.pqc.crypto.gmss;

import java.lang.reflect.Array;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.util.encoders.Hex;

public class GMSSRootSig {
    private long big8;
    private int checksum;
    private int counter;
    private GMSSRandom gmssRandom;
    private byte[] hash;
    private int height;
    private int ii;
    private int k;
    private int keysize;
    private int mdsize;
    private Digest messDigestOTS;
    private int messagesize;
    private byte[] privateKeyOTS;
    private int r;
    private byte[] seed;
    private byte[] sign;
    private int steps;
    private int test;
    private long test8;
    private int w;

    public GMSSRootSig(Digest digest, int i, int i2) {
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.w = i;
        this.height = i2;
        this.k = (1 << i) - 1;
        this.messagesize = (int) Math.ceil(((double) (this.mdsize << 3)) / ((double) i));
    }

    public GMSSRootSig(Digest digest, byte[][] bArr, int[] iArr) {
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.counter = iArr[0];
        this.test = iArr[1];
        this.ii = iArr[2];
        this.r = iArr[3];
        this.steps = iArr[4];
        this.keysize = iArr[5];
        this.height = iArr[6];
        this.w = iArr[7];
        this.checksum = iArr[8];
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.k = (1 << this.w) - 1;
        this.messagesize = (int) Math.ceil(((double) (this.mdsize << 3)) / ((double) this.w));
        this.privateKeyOTS = bArr[0];
        this.seed = bArr[1];
        this.hash = bArr[2];
        this.sign = bArr[3];
        this.test8 = ((long) (bArr[4][0] & 255)) | (((long) (bArr[4][1] & 255)) << 8) | (((long) (bArr[4][2] & 255)) << 16) | (((long) (bArr[4][3] & 255)) << 24) | (((long) (bArr[4][4] & 255)) << 32) | (((long) (bArr[4][5] & 255)) << 40) | (((long) (bArr[4][6] & 255)) << 48) | (((long) (bArr[4][7] & 255)) << 56);
        this.big8 = ((long) (bArr[4][8] & 255)) | (((long) (bArr[4][9] & 255)) << 8) | (((long) (bArr[4][10] & 255)) << 16) | (((long) (bArr[4][11] & 255)) << 24) | (((long) (bArr[4][12] & 255)) << 32) | (((long) (bArr[4][13] & 255)) << 40) | (((long) (bArr[4][14] & 255)) << 48) | (((long) (bArr[4][15] & 255)) << 56);
    }

    private void oneStep() {
        int i;
        if (8 % this.w == 0) {
            if (this.test == 0) {
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
                if (this.ii < this.mdsize) {
                    this.test = this.hash[this.ii] & this.k;
                    this.hash[this.ii] = (byte) (this.hash[this.ii] >>> this.w);
                } else {
                    this.test = this.checksum & this.k;
                    this.checksum >>>= this.w;
                }
            } else if (this.test > 0) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                this.test--;
            }
            if (this.test == 0) {
                System.arraycopy(this.privateKeyOTS, 0, this.sign, this.counter * this.mdsize, this.mdsize);
                this.counter++;
                if (this.counter % (8 / this.w) == 0) {
                    this.ii++;
                }
            }
        } else if (this.w < 8) {
            if (this.test == 0) {
                if (this.counter % 8 == 0 && this.ii < this.mdsize) {
                    this.big8 = 0;
                    if (this.counter < ((this.mdsize / this.w) << 3)) {
                        for (int i2 = 0; i2 < this.w; i2++) {
                            this.big8 ^= (long) ((this.hash[this.ii] & 255) << (i2 << 3));
                            this.ii++;
                        }
                    } else {
                        for (int i3 = 0; i3 < this.mdsize % this.w; i3++) {
                            this.big8 ^= (long) ((this.hash[this.ii] & 255) << (i3 << 3));
                            this.ii++;
                        }
                    }
                }
                if (this.counter == this.messagesize) {
                    this.big8 = (long) this.checksum;
                }
                this.test = (int) (this.big8 & ((long) this.k));
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            } else if (this.test > 0) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                this.test--;
            }
            if (this.test == 0) {
                System.arraycopy(this.privateKeyOTS, 0, this.sign, this.counter * this.mdsize, this.mdsize);
                this.big8 >>>= this.w;
                this.counter++;
            }
        } else if (this.w < 57) {
            if (this.test8 == 0) {
                this.big8 = 0;
                this.ii = 0;
                int i4 = this.r % 8;
                int i5 = this.r >>> 3;
                if (i5 < this.mdsize) {
                    if (this.r <= (this.mdsize << 3) - this.w) {
                        this.r += this.w;
                        i = (this.r + 7) >>> 3;
                    } else {
                        i = this.mdsize;
                        this.r += this.w;
                    }
                    while (i5 < i) {
                        this.big8 ^= (long) ((this.hash[i5] & 255) << (this.ii << 3));
                        this.ii++;
                        i5++;
                    }
                    this.big8 >>>= i4;
                    this.test8 = this.big8 & ((long) this.k);
                } else {
                    this.test8 = (long) (this.checksum & this.k);
                    this.checksum >>>= this.w;
                }
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            } else if (this.test8 > 0) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                this.test8--;
            }
            if (this.test8 == 0) {
                System.arraycopy(this.privateKeyOTS, 0, this.sign, this.counter * this.mdsize, this.mdsize);
                this.counter++;
            }
        }
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

    public byte[] getSig() {
        return this.sign;
    }

    public byte[][] getStatByte() {
        byte[][] bArr = (byte[][]) Array.newInstance(Byte.TYPE, 5, this.mdsize);
        bArr[0] = this.privateKeyOTS;
        bArr[1] = this.seed;
        bArr[2] = this.hash;
        bArr[3] = this.sign;
        bArr[4] = getStatLong();
        return bArr;
    }

    public int[] getStatInt() {
        return new int[]{this.counter, this.test, this.ii, this.r, this.steps, this.keysize, this.height, this.w, this.checksum};
    }

    public byte[] getStatLong() {
        return new byte[]{(byte) ((int) (this.test8 & 255)), (byte) ((int) ((this.test8 >> 8) & 255)), (byte) ((int) ((this.test8 >> 16) & 255)), (byte) ((int) ((this.test8 >> 24) & 255)), (byte) ((int) ((this.test8 >> 32) & 255)), (byte) ((int) ((this.test8 >> 40) & 255)), (byte) ((int) ((this.test8 >> 48) & 255)), (byte) ((int) ((this.test8 >> 56) & 255)), (byte) ((int) (this.big8 & 255)), (byte) ((int) ((this.big8 >> 8) & 255)), (byte) ((int) ((this.big8 >> 16) & 255)), (byte) ((int) ((this.big8 >> 24) & 255)), (byte) ((int) ((this.big8 >> 32) & 255)), (byte) ((int) ((this.big8 >> 40) & 255)), (byte) ((int) ((this.big8 >> 48) & 255)), (byte) ((int) ((this.big8 >> 56) & 255))};
    }

    public void initSign(byte[] bArr, byte[] bArr2) {
        this.hash = new byte[this.mdsize];
        this.messDigestOTS.update(bArr2, 0, bArr2.length);
        this.hash = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(this.hash, 0);
        byte[] bArr3 = new byte[this.mdsize];
        System.arraycopy(this.hash, 0, bArr3, 0, this.mdsize);
        int i = 0;
        int log = getLog((this.messagesize << this.w) + 1);
        if (8 % this.w == 0) {
            int i2 = 8 / this.w;
            for (int i3 = 0; i3 < this.mdsize; i3++) {
                for (int i4 = 0; i4 < i2; i4++) {
                    i += bArr3[i3] & this.k;
                    bArr3[i3] = (byte) (bArr3[i3] >>> this.w);
                }
            }
            this.checksum = (this.messagesize << this.w) - i;
            int i5 = this.checksum;
            int i6 = 0;
            while (i6 < log) {
                i += this.k & i5;
                i5 >>>= this.w;
                i6 += this.w;
            }
        } else if (this.w < 8) {
            int i7 = 0;
            int i8 = this.mdsize / this.w;
            int i9 = 0;
            int i10 = 0;
            while (i9 < i8) {
                long j = 0;
                int i11 = 0;
                int i12 = i7;
                while (i11 < this.w) {
                    i12++;
                    i11++;
                    j = ((long) ((bArr3[i12] & 255) << (i11 << 3))) ^ j;
                }
                for (int i13 = 0; i13 < 8; i13++) {
                    i10 += (int) (((long) this.k) & j);
                    j >>>= this.w;
                }
                i9++;
                i7 = i12;
            }
            int i14 = this.mdsize % this.w;
            long j2 = 0;
            for (int i15 = 0; i15 < i14; i15++) {
                j2 ^= (long) ((bArr3[i7] & 255) << (i15 << 3));
                i7++;
            }
            int i16 = i14 << 3;
            int i17 = 0;
            long j3 = j2;
            i = i10;
            while (i17 < i16) {
                i += (int) (((long) this.k) & j3);
                j3 >>>= this.w;
                i17 += this.w;
            }
            this.checksum = (this.messagesize << this.w) - i;
            int i18 = this.checksum;
            int i19 = 0;
            while (i19 < log) {
                i += this.k & i18;
                i18 >>>= this.w;
                i19 += this.w;
            }
        } else if (this.w < 57) {
            int i20 = 0;
            while (i20 <= (this.mdsize << 3) - this.w) {
                int i21 = i20 % 8;
                i20 += this.w;
                int i22 = (i20 + 7) >>> 3;
                long j4 = 0;
                int i23 = 0;
                for (int i24 = i20 >>> 3; i24 < i22; i24++) {
                    j4 ^= (long) ((bArr3[i24] & 255) << (i23 << 3));
                    i23++;
                }
                i = (int) (((j4 >>> i21) & ((long) this.k)) + ((long) i));
            }
            int i25 = i20 >>> 3;
            if (i25 < this.mdsize) {
                int i26 = i20 % 8;
                long j5 = 0;
                int i27 = 0;
                int i28 = i25;
                while (i28 < this.mdsize) {
                    j5 ^= (long) ((bArr3[i28] & 255) << (i27 << 3));
                    i28++;
                    i27++;
                }
                i = (int) (((long) i) + ((j5 >>> i26) & ((long) this.k)));
            }
            this.checksum = (this.messagesize << this.w) - i;
            int i29 = this.checksum;
            int i30 = 0;
            while (i30 < log) {
                i += this.k & i29;
                i29 >>>= this.w;
                i30 += this.w;
            }
        }
        this.keysize = this.messagesize + ((int) Math.ceil(((double) log) / ((double) this.w)));
        this.steps = (int) Math.ceil(((double) (this.keysize + i)) / ((double) (1 << this.height)));
        this.sign = new byte[(this.keysize * this.mdsize)];
        this.counter = 0;
        this.test = 0;
        this.ii = 0;
        this.test8 = 0;
        this.r = 0;
        this.privateKeyOTS = new byte[this.mdsize];
        this.seed = new byte[this.mdsize];
        System.arraycopy(bArr, 0, this.seed, 0, this.mdsize);
    }

    public String toString() {
        String str = "" + this.big8 + "  ";
        int[] iArr = new int[9];
        int[] statInt = getStatInt();
        byte[][] bArr = (byte[][]) Array.newInstance(Byte.TYPE, 5, this.mdsize);
        byte[][] statByte = getStatByte();
        for (int i = 0; i < 9; i++) {
            str = str + statInt[i] + " ";
        }
        int i2 = 0;
        while (i2 < 5) {
            i2++;
            str = str + new String(Hex.encode(statByte[i2])) + " ";
        }
        return str;
    }

    public boolean updateSign() {
        for (int i = 0; i < this.steps; i++) {
            if (this.counter < this.keysize) {
                oneStep();
            }
            if (this.counter == this.keysize) {
                return true;
            }
        }
        return false;
    }
}
