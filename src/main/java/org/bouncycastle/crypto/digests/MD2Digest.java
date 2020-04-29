package org.bouncycastle.crypto.digests;

import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.util.Memoable;
import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

public class MD2Digest implements ExtendedDigest, Memoable {
    private static final int DIGEST_LENGTH = 16;
    private static final byte[] S = {41, 46, 67, MessagePack.Code.EXT32, -94, MessagePack.Code.FIXEXT16, 124, 1, 61, TarHeader.LF_FIFO, 84, -95, -20, -16, 6, 19, 98, -89, 5, -13, MessagePack.Code.NIL, MessagePack.Code.EXT8, 115, -116, -104, -109, 43, MessagePack.Code.STR8, PSSSigner.TRAILER_IMPLICIT, 76, -126, MessagePack.Code.FLOAT32, 30, -101, 87, 60, -3, MessagePack.Code.FIXEXT1, MessagePack.Code.NEGFIXINT_PREFIX, 22, 103, 66, 111, 24, -118, 23, -27, 18, -66, 78, MessagePack.Code.BIN8, MessagePack.Code.FIXEXT4, MessagePack.Code.STR16, -98, MessagePack.Code.MAP16, 73, MessagePack.Code.FIXSTR_PREFIX, -5, -11, -114, -69, 47, -18, 122, -87, 104, 121, -111, 21, -78, 7, 63, -108, MessagePack.Code.FALSE, Tnaf.POW_2_WIDTH, -119, 11, 34, 95, 33, Byte.MIN_VALUE, ByteCompanionObject.MAX_VALUE, 93, -102, 90, MessagePack.Code.FIXARRAY_PREFIX, TarHeader.LF_SYMLINK, 39, TarHeader.LF_DIR, 62, MessagePack.Code.UINT8, -25, -65, -9, -105, 3, -1, 25, TarHeader.LF_NORMAL, -77, 72, -91, -75, MessagePack.Code.INT16, MessagePack.Code.FIXEXT8, 94, -110, 42, -84, 86, -86, MessagePack.Code.BIN32, 79, -72, 56, MessagePack.Code.INT32, -106, -92, 125, -74, 118, -4, 107, -30, -100, 116, 4, -15, 69, -99, 112, 89, 100, 113, -121, 32, -122, 91, MessagePack.Code.UINT64, 101, -26, 45, -88, 2, 27, 96, 37, -83, -82, -80, -71, -10, 28, 70, 97, 105, TarHeader.LF_BLK, 64, 126, 15, 85, 71, -93, 35, MessagePack.Code.ARRAY32, 81, -81, 58, MessagePack.Code.TRUE, 92, -7, MessagePack.Code.UINT32, -70, MessagePack.Code.BIN16, -22, 38, 44, 83, Draft_75.CR, 110, -123, 40, -124, 9, MessagePack.Code.INT64, MessagePack.Code.MAP32, MessagePack.Code.UINT16, -12, 65, -127, 77, 82, 106, MessagePack.Code.ARRAY16, TarHeader.LF_CONTIG, MessagePack.Code.EXT16, 108, MessagePack.Code.NEVER_USED, -85, -6, 36, -31, 123, 8, 12, -67, -79, 74, 120, -120, -107, -117, -29, 99, -24, 109, -23, MessagePack.Code.FLOAT64, MessagePack.Code.FIXEXT2, -2, 59, 0, 29, 57, -14, -17, -73, 14, 102, 88, MessagePack.Code.INT8, -28, -90, 119, 114, -8, -21, 117, 75, 10, TarHeader.LF_LINK, 68, 80, -76, -113, -19, 31, 26, MessagePack.Code.STR32, -103, -115, TarHeader.LF_CHR, -97, 17, -125, 20};
    private byte[] C = new byte[16];
    private int COff;
    private byte[] M = new byte[16];
    private byte[] X = new byte[48];
    private int mOff;
    private int xOff;

    public MD2Digest() {
        reset();
    }

    public MD2Digest(MD2Digest mD2Digest) {
        copyIn(mD2Digest);
    }

    private void copyIn(MD2Digest mD2Digest) {
        System.arraycopy(mD2Digest.X, 0, this.X, 0, mD2Digest.X.length);
        this.xOff = mD2Digest.xOff;
        System.arraycopy(mD2Digest.M, 0, this.M, 0, mD2Digest.M.length);
        this.mOff = mD2Digest.mOff;
        System.arraycopy(mD2Digest.C, 0, this.C, 0, mD2Digest.C.length);
        this.COff = mD2Digest.COff;
    }

    public Memoable copy() {
        return new MD2Digest(this);
    }

    public int doFinal(byte[] bArr, int i) {
        byte length = (byte) (this.M.length - this.mOff);
        for (int i2 = this.mOff; i2 < this.M.length; i2++) {
            this.M[i2] = length;
        }
        processCheckSum(this.M);
        processBlock(this.M);
        processBlock(this.C);
        System.arraycopy(this.X, this.xOff, bArr, i, 16);
        reset();
        return 16;
    }

    public String getAlgorithmName() {
        return "MD2";
    }

    public int getByteLength() {
        return 16;
    }

    public int getDigestSize() {
        return 16;
    }

    /* access modifiers changed from: protected */
    public void processBlock(byte[] bArr) {
        for (int i = 0; i < 16; i++) {
            this.X[i + 16] = bArr[i];
            this.X[i + 32] = (byte) (bArr[i] ^ this.X[i]);
        }
        int i2 = 0;
        for (int i3 = 0; i3 < 18; i3++) {
            for (int i4 = 0; i4 < 48; i4++) {
                byte[] bArr2 = this.X;
                byte b = (byte) (S[i2] ^ bArr2[i4]);
                bArr2[i4] = b;
                i2 = b & 255;
            }
            i2 = (i2 + i3) % 256;
        }
    }

    /* access modifiers changed from: protected */
    public void processCheckSum(byte[] bArr) {
        byte b = this.C[15];
        for (int i = 0; i < 16; i++) {
            byte[] bArr2 = this.C;
            bArr2[i] = (byte) (S[(b ^ bArr[i]) & 255] ^ bArr2[i]);
            b = this.C[i];
        }
    }

    public void reset() {
        this.xOff = 0;
        for (int i = 0; i != this.X.length; i++) {
            this.X[i] = 0;
        }
        this.mOff = 0;
        for (int i2 = 0; i2 != this.M.length; i2++) {
            this.M[i2] = 0;
        }
        this.COff = 0;
        for (int i3 = 0; i3 != this.C.length; i3++) {
            this.C[i3] = 0;
        }
    }

    public void reset(Memoable memoable) {
        copyIn((MD2Digest) memoable);
    }

    public void update(byte b) {
        byte[] bArr = this.M;
        int i = this.mOff;
        this.mOff = i + 1;
        bArr[i] = b;
        if (this.mOff == 16) {
            processCheckSum(this.M);
            processBlock(this.M);
            this.mOff = 0;
        }
    }

    public void update(byte[] bArr, int i, int i2) {
        while (this.mOff != 0 && i2 > 0) {
            update(bArr[i]);
            i++;
            i2--;
        }
        int i3 = i2;
        int i4 = i;
        while (i3 > 16) {
            System.arraycopy(bArr, i4, this.M, 0, 16);
            processCheckSum(this.M);
            processBlock(this.M);
            i3 -= 16;
            i4 += 16;
        }
        while (i3 > 0) {
            update(bArr[i4]);
            i4++;
            i3--;
        }
    }
}
