package org.bouncycastle.pqc.crypto.xmss;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Xof;

public final class KeyedHashFunctions {
    private final Digest digest;
    private final int digestSize;

    protected KeyedHashFunctions(Digest digest2, int i) {
        if (digest2 == null) {
            throw new NullPointerException("digest == null");
        }
        this.digest = digest2;
        this.digestSize = i;
    }

    private byte[] coreDigest(int i, byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[(this.digestSize + bArr.length + bArr2.length)];
        byte[] bytesBigEndian = XMSSUtil.toBytesBigEndian((long) i, this.digestSize);
        for (int i2 = 0; i2 < bytesBigEndian.length; i2++) {
            bArr3[i2] = bytesBigEndian[i2];
        }
        for (int i3 = 0; i3 < bArr.length; i3++) {
            bArr3[bytesBigEndian.length + i3] = bArr[i3];
        }
        for (int i4 = 0; i4 < bArr2.length; i4++) {
            bArr3[bytesBigEndian.length + bArr.length + i4] = bArr2[i4];
        }
        this.digest.update(bArr3, 0, bArr3.length);
        byte[] bArr4 = new byte[this.digestSize];
        if (this.digest instanceof Xof) {
            ((Xof) this.digest).doFinal(bArr4, 0, this.digestSize);
        } else {
            this.digest.doFinal(bArr4, 0);
        }
        return bArr4;
    }

    /* access modifiers changed from: protected */
    public byte[] F(byte[] bArr, byte[] bArr2) {
        if (bArr.length != this.digestSize) {
            throw new IllegalArgumentException("wrong key length");
        } else if (bArr2.length == this.digestSize) {
            return coreDigest(0, bArr, bArr2);
        } else {
            throw new IllegalArgumentException("wrong in length");
        }
    }

    /* access modifiers changed from: protected */
    public byte[] H(byte[] bArr, byte[] bArr2) {
        if (bArr.length != this.digestSize) {
            throw new IllegalArgumentException("wrong key length");
        } else if (bArr2.length == this.digestSize * 2) {
            return coreDigest(1, bArr, bArr2);
        } else {
            throw new IllegalArgumentException("wrong in length");
        }
    }

    /* access modifiers changed from: protected */
    public byte[] HMsg(byte[] bArr, byte[] bArr2) {
        if (bArr.length == this.digestSize * 3) {
            return coreDigest(2, bArr, bArr2);
        }
        throw new IllegalArgumentException("wrong key length");
    }

    /* access modifiers changed from: protected */
    public byte[] PRF(byte[] bArr, byte[] bArr2) {
        if (bArr.length != this.digestSize) {
            throw new IllegalArgumentException("wrong key length");
        } else if (bArr2.length == 32) {
            return coreDigest(3, bArr, bArr2);
        } else {
            throw new IllegalArgumentException("wrong address length");
        }
    }
}
