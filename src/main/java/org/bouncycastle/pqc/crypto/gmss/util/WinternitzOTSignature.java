package org.bouncycastle.pqc.crypto.gmss.util;

import java.lang.reflect.Array;
import org.bouncycastle.crypto.Digest;

public class WinternitzOTSignature {
    private int checksumsize;
    private GMSSRandom gmssRandom = new GMSSRandom(this.messDigestOTS);
    private int keysize;
    private int mdsize = this.messDigestOTS.getDigestSize();
    private Digest messDigestOTS;
    private int messagesize;
    private byte[][] privateKeyOTS;
    private int w;

    public WinternitzOTSignature(byte[] bArr, Digest digest, int i) {
        this.w = i;
        this.messDigestOTS = digest;
        this.messagesize = (int) Math.ceil(((double) (this.mdsize << 3)) / ((double) i));
        this.checksumsize = getLog((this.messagesize << i) + 1);
        this.keysize = this.messagesize + ((int) Math.ceil(((double) this.checksumsize) / ((double) i)));
        this.privateKeyOTS = (byte[][]) Array.newInstance(Byte.TYPE, this.keysize, this.mdsize);
        byte[] bArr2 = new byte[this.mdsize];
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        for (int i2 = 0; i2 < this.keysize; i2++) {
            this.privateKeyOTS[i2] = this.gmssRandom.nextSeed(bArr2);
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

    public byte[][] getPrivateKey() {
        return this.privateKeyOTS;
    }

    public byte[] getPublicKey() {
        byte[] bArr = new byte[(this.keysize * this.mdsize)];
        byte[] bArr2 = new byte[this.mdsize];
        int i = 1 << this.w;
        for (int i2 = 0; i2 < this.keysize; i2++) {
            this.messDigestOTS.update(this.privateKeyOTS[i2], 0, this.privateKeyOTS[i2].length);
            byte[] bArr3 = new byte[this.messDigestOTS.getDigestSize()];
            this.messDigestOTS.doFinal(bArr3, 0);
            for (int i3 = 2; i3 < i; i3++) {
                this.messDigestOTS.update(bArr3, 0, bArr3.length);
                bArr3 = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(bArr3, 0);
            }
            System.arraycopy(bArr3, 0, bArr, this.mdsize * i2, this.mdsize);
        }
        this.messDigestOTS.update(bArr, 0, bArr.length);
        byte[] bArr4 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr4, 0);
        return bArr4;
    }

    /* JADX WARN: Type inference failed for: r8v24, types: [int], assign insn: 0x005c: ARITH  (r8v24 ? I:int) = (r4v60 byte) + (r6v46 byte) */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] getSignature(byte[] r20) {
        /*
            r19 = this;
            r0 = r19
            int r2 = r0.keysize
            r0 = r19
            int r3 = r0.mdsize
            int r2 = r2 * r3
            byte[] r14 = new byte[r2]
            r0 = r19
            int r2 = r0.mdsize
            byte[] r2 = new byte[r2]
            r5 = 0
            r4 = 0
            r0 = r19
            org.bouncycastle.crypto.Digest r2 = r0.messDigestOTS
            r3 = 0
            r0 = r20
            int r6 = r0.length
            r0 = r20
            r2.update(r0, r3, r6)
            r0 = r19
            org.bouncycastle.crypto.Digest r2 = r0.messDigestOTS
            int r2 = r2.getDigestSize()
            byte[] r15 = new byte[r2]
            r0 = r19
            org.bouncycastle.crypto.Digest r2 = r0.messDigestOTS
            r3 = 0
            r2.doFinal(r15, r3)
            r2 = 8
            r0 = r19
            int r3 = r0.w
            int r2 = r2 % r3
            if (r2 != 0) goto L_0x010c
            r2 = 8
            r0 = r19
            int r3 = r0.w
            int r9 = r2 / r3
            r2 = 1
            r0 = r19
            int r3 = r0.w
            int r2 = r2 << r3
            int r10 = r2 + -1
            r0 = r19
            int r2 = r0.mdsize
            byte[] r3 = new byte[r2]
            r2 = 0
        L_0x0052:
            int r6 = r15.length
            if (r2 >= r6) goto L_0x00af
            r6 = 0
            r7 = r6
        L_0x0057:
            if (r7 >= r9) goto L_0x00ac
            byte r6 = r15[r2]
            r6 = r6 & r10
            int r8 = r4 + r6
            r0 = r19
            byte[][] r4 = r0.privateKeyOTS
            r4 = r4[r5]
            r11 = 0
            r12 = 0
            r0 = r19
            int r13 = r0.mdsize
            java.lang.System.arraycopy(r4, r11, r3, r12, r13)
            r4 = r6
        L_0x006e:
            if (r4 <= 0) goto L_0x008e
            r0 = r19
            org.bouncycastle.crypto.Digest r6 = r0.messDigestOTS
            r11 = 0
            int r12 = r3.length
            r6.update(r3, r11, r12)
            r0 = r19
            org.bouncycastle.crypto.Digest r3 = r0.messDigestOTS
            int r3 = r3.getDigestSize()
            byte[] r3 = new byte[r3]
            r0 = r19
            org.bouncycastle.crypto.Digest r6 = r0.messDigestOTS
            r11 = 0
            r6.doFinal(r3, r11)
            int r4 = r4 + -1
            goto L_0x006e
        L_0x008e:
            r4 = 0
            r0 = r19
            int r6 = r0.mdsize
            int r6 = r6 * r5
            r0 = r19
            int r11 = r0.mdsize
            java.lang.System.arraycopy(r3, r4, r14, r6, r11)
            byte r4 = r15[r2]
            r0 = r19
            int r6 = r0.w
            int r4 = r4 >>> r6
            byte r4 = (byte) r4
            r15[r2] = r4
            int r5 = r5 + 1
            int r6 = r7 + 1
            r7 = r6
            r4 = r8
            goto L_0x0057
        L_0x00ac:
            int r2 = r2 + 1
            goto L_0x0052
        L_0x00af:
            r0 = r19
            int r2 = r0.messagesize
            r0 = r19
            int r6 = r0.w
            int r2 = r2 << r6
            int r4 = r2 - r4
            r2 = 0
        L_0x00bb:
            r0 = r19
            int r6 = r0.checksumsize
            if (r2 >= r6) goto L_0x03f6
            r6 = r4 & r10
            r0 = r19
            byte[][] r7 = r0.privateKeyOTS
            r7 = r7[r5]
            r8 = 0
            r9 = 0
            r0 = r19
            int r11 = r0.mdsize
            java.lang.System.arraycopy(r7, r8, r3, r9, r11)
        L_0x00d2:
            if (r6 <= 0) goto L_0x00f2
            r0 = r19
            org.bouncycastle.crypto.Digest r7 = r0.messDigestOTS
            r8 = 0
            int r9 = r3.length
            r7.update(r3, r8, r9)
            r0 = r19
            org.bouncycastle.crypto.Digest r3 = r0.messDigestOTS
            int r3 = r3.getDigestSize()
            byte[] r3 = new byte[r3]
            r0 = r19
            org.bouncycastle.crypto.Digest r7 = r0.messDigestOTS
            r8 = 0
            r7.doFinal(r3, r8)
            int r6 = r6 + -1
            goto L_0x00d2
        L_0x00f2:
            r6 = 0
            r0 = r19
            int r7 = r0.mdsize
            int r7 = r7 * r5
            r0 = r19
            int r8 = r0.mdsize
            java.lang.System.arraycopy(r3, r6, r14, r7, r8)
            r0 = r19
            int r6 = r0.w
            int r4 = r4 >>> r6
            int r5 = r5 + 1
            r0 = r19
            int r6 = r0.w
            int r2 = r2 + r6
            goto L_0x00bb
        L_0x010c:
            r0 = r19
            int r2 = r0.w
            r3 = 8
            if (r2 >= r3) goto L_0x0283
            r0 = r19
            int r2 = r0.mdsize
            r0 = r19
            int r3 = r0.w
            int r16 = r2 / r3
            r2 = 1
            r0 = r19
            int r3 = r0.w
            int r2 = r2 << r3
            int r17 = r2 + -1
            r0 = r19
            int r2 = r0.mdsize
            byte[] r8 = new byte[r2]
            r3 = 0
            r2 = 0
            r11 = r2
            r9 = r4
            r10 = r5
        L_0x0131:
            r0 = r16
            if (r11 >= r0) goto L_0x01af
            r4 = 0
            r2 = 0
        L_0x0138:
            r0 = r19
            int r6 = r0.w
            if (r2 >= r6) goto L_0x014c
            byte r6 = r15[r3]
            r6 = r6 & 255(0xff, float:3.57E-43)
            int r7 = r2 << 3
            int r6 = r6 << r7
            long r6 = (long) r6
            long r4 = r4 ^ r6
            int r3 = r3 + 1
            int r2 = r2 + 1
            goto L_0x0138
        L_0x014c:
            r6 = 0
            r12 = r4
            r2 = r8
            r7 = r9
        L_0x0150:
            r4 = 8
            if (r6 >= r4) goto L_0x01a9
            r0 = r17
            long r4 = (long) r0
            long r4 = r4 & r12
            int r4 = (int) r4
            int r5 = r7 + r4
            r0 = r19
            byte[][] r7 = r0.privateKeyOTS
            r7 = r7[r10]
            r8 = 0
            r9 = 0
            r0 = r19
            int r0 = r0.mdsize
            r18 = r0
            r0 = r18
            java.lang.System.arraycopy(r7, r8, r2, r9, r0)
        L_0x016e:
            if (r4 <= 0) goto L_0x018e
            r0 = r19
            org.bouncycastle.crypto.Digest r7 = r0.messDigestOTS
            r8 = 0
            int r9 = r2.length
            r7.update(r2, r8, r9)
            r0 = r19
            org.bouncycastle.crypto.Digest r2 = r0.messDigestOTS
            int r2 = r2.getDigestSize()
            byte[] r2 = new byte[r2]
            r0 = r19
            org.bouncycastle.crypto.Digest r7 = r0.messDigestOTS
            r8 = 0
            r7.doFinal(r2, r8)
            int r4 = r4 + -1
            goto L_0x016e
        L_0x018e:
            r4 = 0
            r0 = r19
            int r7 = r0.mdsize
            int r7 = r7 * r10
            r0 = r19
            int r8 = r0.mdsize
            java.lang.System.arraycopy(r2, r4, r14, r7, r8)
            r0 = r19
            int r4 = r0.w
            long r8 = r12 >>> r4
            int r10 = r10 + 1
            int r4 = r6 + 1
            r6 = r4
            r12 = r8
            r7 = r5
            goto L_0x0150
        L_0x01a9:
            int r4 = r11 + 1
            r11 = r4
            r8 = r2
            r9 = r7
            goto L_0x0131
        L_0x01af:
            r0 = r19
            int r2 = r0.mdsize
            r0 = r19
            int r4 = r0.w
            int r4 = r2 % r4
            r6 = 0
            r2 = 0
        L_0x01bc:
            if (r2 >= r4) goto L_0x01cc
            byte r5 = r15[r3]
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r11 = r2 << 3
            int r5 = r5 << r11
            long r12 = (long) r5
            long r6 = r6 ^ r12
            int r3 = r3 + 1
            int r2 = r2 + 1
            goto L_0x01bc
        L_0x01cc:
            int r11 = r4 << 3
            r2 = 0
            r4 = r2
            r3 = r8
            r5 = r10
        L_0x01d2:
            if (r4 >= r11) goto L_0x0226
            r0 = r17
            long r12 = (long) r0
            long r12 = r12 & r6
            int r2 = (int) r12
            int r8 = r9 + r2
            r0 = r19
            byte[][] r9 = r0.privateKeyOTS
            r9 = r9[r5]
            r10 = 0
            r12 = 0
            r0 = r19
            int r13 = r0.mdsize
            java.lang.System.arraycopy(r9, r10, r3, r12, r13)
        L_0x01ea:
            if (r2 <= 0) goto L_0x020a
            r0 = r19
            org.bouncycastle.crypto.Digest r9 = r0.messDigestOTS
            r10 = 0
            int r12 = r3.length
            r9.update(r3, r10, r12)
            r0 = r19
            org.bouncycastle.crypto.Digest r3 = r0.messDigestOTS
            int r3 = r3.getDigestSize()
            byte[] r3 = new byte[r3]
            r0 = r19
            org.bouncycastle.crypto.Digest r9 = r0.messDigestOTS
            r10 = 0
            r9.doFinal(r3, r10)
            int r2 = r2 + -1
            goto L_0x01ea
        L_0x020a:
            r2 = 0
            r0 = r19
            int r9 = r0.mdsize
            int r9 = r9 * r5
            r0 = r19
            int r10 = r0.mdsize
            java.lang.System.arraycopy(r3, r2, r14, r9, r10)
            r0 = r19
            int r2 = r0.w
            long r6 = r6 >>> r2
            int r5 = r5 + 1
            r0 = r19
            int r2 = r0.w
            int r2 = r2 + r4
            r4 = r2
            r9 = r8
            goto L_0x01d2
        L_0x0226:
            r0 = r19
            int r2 = r0.messagesize
            r0 = r19
            int r4 = r0.w
            int r2 = r2 << r4
            int r4 = r2 - r9
            r2 = 0
        L_0x0232:
            r0 = r19
            int r6 = r0.checksumsize
            if (r2 >= r6) goto L_0x03f6
            r6 = r4 & r17
            r0 = r19
            byte[][] r7 = r0.privateKeyOTS
            r7 = r7[r5]
            r8 = 0
            r9 = 0
            r0 = r19
            int r10 = r0.mdsize
            java.lang.System.arraycopy(r7, r8, r3, r9, r10)
        L_0x0249:
            if (r6 <= 0) goto L_0x0269
            r0 = r19
            org.bouncycastle.crypto.Digest r7 = r0.messDigestOTS
            r8 = 0
            int r9 = r3.length
            r7.update(r3, r8, r9)
            r0 = r19
            org.bouncycastle.crypto.Digest r3 = r0.messDigestOTS
            int r3 = r3.getDigestSize()
            byte[] r3 = new byte[r3]
            r0 = r19
            org.bouncycastle.crypto.Digest r7 = r0.messDigestOTS
            r8 = 0
            r7.doFinal(r3, r8)
            int r6 = r6 + -1
            goto L_0x0249
        L_0x0269:
            r6 = 0
            r0 = r19
            int r7 = r0.mdsize
            int r7 = r7 * r5
            r0 = r19
            int r8 = r0.mdsize
            java.lang.System.arraycopy(r3, r6, r14, r7, r8)
            r0 = r19
            int r6 = r0.w
            int r4 = r4 >>> r6
            int r5 = r5 + 1
            r0 = r19
            int r6 = r0.w
            int r2 = r2 + r6
            goto L_0x0232
        L_0x0283:
            r0 = r19
            int r2 = r0.w
            r3 = 57
            if (r2 >= r3) goto L_0x03f6
            r0 = r19
            int r2 = r0.mdsize
            int r2 = r2 << 3
            r0 = r19
            int r3 = r0.w
            int r10 = r2 - r3
            r2 = 1
            r0 = r19
            int r3 = r0.w
            int r2 = r2 << r3
            int r11 = r2 + -1
            r0 = r19
            int r2 = r0.mdsize
            byte[] r6 = new byte[r2]
            r2 = 0
            r3 = r2
            r7 = r4
            r8 = r5
        L_0x02a9:
            if (r3 > r10) goto L_0x0322
            int r2 = r3 >>> 3
            int r12 = r3 % 8
            r0 = r19
            int r4 = r0.w
            int r9 = r3 + r4
            int r3 = r9 + 7
            int r13 = r3 >>> 3
            r4 = 0
            r3 = 0
        L_0x02bc:
            if (r2 >= r13) goto L_0x02d6
            byte r16 = r15[r2]
            r0 = r16
            r0 = r0 & 255(0xff, float:3.57E-43)
            r16 = r0
            int r17 = r3 << 3
            int r16 = r16 << r17
            r0 = r16
            long r0 = (long) r0
            r16 = r0
            long r4 = r4 ^ r16
            int r3 = r3 + 1
            int r2 = r2 + 1
            goto L_0x02bc
        L_0x02d6:
            long r2 = r4 >>> r12
            long r4 = (long) r11
            long r2 = r2 & r4
            long r4 = (long) r7
            long r4 = r4 + r2
            int r7 = (int) r4
            r0 = r19
            byte[][] r4 = r0.privateKeyOTS
            r4 = r4[r8]
            r5 = 0
            r12 = 0
            r0 = r19
            int r13 = r0.mdsize
            java.lang.System.arraycopy(r4, r5, r6, r12, r13)
        L_0x02ec:
            r4 = 0
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x0311
            r0 = r19
            org.bouncycastle.crypto.Digest r4 = r0.messDigestOTS
            r5 = 0
            int r12 = r6.length
            r4.update(r6, r5, r12)
            r0 = r19
            org.bouncycastle.crypto.Digest r4 = r0.messDigestOTS
            int r4 = r4.getDigestSize()
            byte[] r6 = new byte[r4]
            r0 = r19
            org.bouncycastle.crypto.Digest r4 = r0.messDigestOTS
            r5 = 0
            r4.doFinal(r6, r5)
            r4 = 1
            long r2 = r2 - r4
            goto L_0x02ec
        L_0x0311:
            r2 = 0
            r0 = r19
            int r3 = r0.mdsize
            int r3 = r3 * r8
            r0 = r19
            int r4 = r0.mdsize
            java.lang.System.arraycopy(r6, r2, r14, r3, r4)
            int r8 = r8 + 1
            r3 = r9
            goto L_0x02a9
        L_0x0322:
            int r2 = r3 >>> 3
            r0 = r19
            int r4 = r0.mdsize
            if (r2 >= r4) goto L_0x03f7
            int r9 = r3 % 8
            r4 = 0
            r3 = 0
        L_0x032f:
            r0 = r19
            int r10 = r0.mdsize
            if (r2 >= r10) goto L_0x0343
            byte r10 = r15[r2]
            r10 = r10 & 255(0xff, float:3.57E-43)
            int r12 = r3 << 3
            int r10 = r10 << r12
            long r12 = (long) r10
            long r4 = r4 ^ r12
            int r3 = r3 + 1
            int r2 = r2 + 1
            goto L_0x032f
        L_0x0343:
            long r2 = r4 >>> r9
            long r4 = (long) r11
            long r4 = r4 & r2
            long r2 = (long) r7
            long r2 = r2 + r4
            int r3 = (int) r2
            r0 = r19
            byte[][] r2 = r0.privateKeyOTS
            r2 = r2[r8]
            r7 = 0
            r9 = 0
            r0 = r19
            int r10 = r0.mdsize
            java.lang.System.arraycopy(r2, r7, r6, r9, r10)
            r2 = r6
        L_0x035a:
            r6 = 0
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 <= 0) goto L_0x037f
            r0 = r19
            org.bouncycastle.crypto.Digest r6 = r0.messDigestOTS
            r7 = 0
            int r9 = r2.length
            r6.update(r2, r7, r9)
            r0 = r19
            org.bouncycastle.crypto.Digest r2 = r0.messDigestOTS
            int r2 = r2.getDigestSize()
            byte[] r2 = new byte[r2]
            r0 = r19
            org.bouncycastle.crypto.Digest r6 = r0.messDigestOTS
            r7 = 0
            r6.doFinal(r2, r7)
            r6 = 1
            long r4 = r4 - r6
            goto L_0x035a
        L_0x037f:
            r4 = 0
            r0 = r19
            int r5 = r0.mdsize
            int r5 = r5 * r8
            r0 = r19
            int r6 = r0.mdsize
            java.lang.System.arraycopy(r2, r4, r14, r5, r6)
            int r4 = r8 + 1
        L_0x038e:
            r0 = r19
            int r5 = r0.messagesize
            r0 = r19
            int r6 = r0.w
            int r5 = r5 << r6
            int r6 = r5 - r3
            r3 = 0
            r5 = r2
            r7 = r4
        L_0x039c:
            r0 = r19
            int r2 = r0.checksumsize
            if (r3 >= r2) goto L_0x03f6
            r2 = r6 & r11
            long r8 = (long) r2
            r0 = r19
            byte[][] r2 = r0.privateKeyOTS
            r2 = r2[r7]
            r4 = 0
            r10 = 0
            r0 = r19
            int r12 = r0.mdsize
            java.lang.System.arraycopy(r2, r4, r5, r10, r12)
            r4 = r5
        L_0x03b5:
            r12 = 0
            int r2 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r2 <= 0) goto L_0x03da
            r0 = r19
            org.bouncycastle.crypto.Digest r2 = r0.messDigestOTS
            r5 = 0
            int r10 = r4.length
            r2.update(r4, r5, r10)
            r0 = r19
            org.bouncycastle.crypto.Digest r2 = r0.messDigestOTS
            int r2 = r2.getDigestSize()
            byte[] r4 = new byte[r2]
            r0 = r19
            org.bouncycastle.crypto.Digest r2 = r0.messDigestOTS
            r5 = 0
            r2.doFinal(r4, r5)
            r12 = 1
            long r8 = r8 - r12
            goto L_0x03b5
        L_0x03da:
            r2 = 0
            r0 = r19
            int r5 = r0.mdsize
            int r5 = r5 * r7
            r0 = r19
            int r8 = r0.mdsize
            java.lang.System.arraycopy(r4, r2, r14, r5, r8)
            r0 = r19
            int r2 = r0.w
            int r6 = r6 >>> r2
            int r7 = r7 + 1
            r0 = r19
            int r2 = r0.w
            int r2 = r2 + r3
            r3 = r2
            r5 = r4
            goto L_0x039c
        L_0x03f6:
            return r14
        L_0x03f7:
            r2 = r6
            r3 = r7
            r4 = r8
            goto L_0x038e
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSignature.getSignature(byte[]):byte[]");
    }
}
