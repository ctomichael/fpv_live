package org.bouncycastle.crypto.engines;

import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.util.Pack;
import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

public class SM4Engine implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final int[] CK = {462357, 472066609, 943670861, 1415275113, 1886879365, -1936483679, -1464879427, -993275175, -521670923, -66909679, 404694573, 876298825, 1347903077, 1819507329, -2003855715, -1532251463, -1060647211, -589042959, -117504499, 337322537, 808926789, 1280531041, 1752135293, -2071227751, -1599623499, -1128019247, -656414995, -184876535, 269950501, 741554753, 1213159005, 1684763257};
    private static final int[] FK = {-1548633402, 1453994832, 1736282519, -1301273892};
    private static final byte[] Sbox = {MessagePack.Code.FIXEXT4, MessagePack.Code.FIXARRAY_PREFIX, -23, -2, MessagePack.Code.UINT8, -31, 61, -73, 22, -74, 20, MessagePack.Code.FALSE, 40, -5, 44, 5, 43, 103, -102, 118, 42, -66, 4, MessagePack.Code.TRUE, -86, 68, 19, 38, 73, -122, 6, -103, -100, 66, 80, -12, -111, -17, -104, 122, TarHeader.LF_CHR, 84, 11, 67, -19, MessagePack.Code.UINT64, -84, 98, -28, -77, 28, -87, MessagePack.Code.EXT32, 8, -24, -107, Byte.MIN_VALUE, MessagePack.Code.MAP32, -108, -6, 117, -113, 63, -90, 71, 7, -89, -4, -13, 115, 23, -70, -125, 89, 60, 25, -26, -123, 79, -88, 104, 107, -127, -78, 113, 100, MessagePack.Code.STR16, -117, -8, -21, 15, 75, 112, 86, -99, TarHeader.LF_DIR, 30, 36, 14, 94, 99, 88, MessagePack.Code.INT16, -94, 37, 34, 124, 59, 1, 33, 120, -121, MessagePack.Code.FIXEXT1, 0, 70, 87, -97, MessagePack.Code.INT64, 39, 82, 76, TarHeader.LF_FIFO, 2, -25, MessagePack.Code.FIXSTR_PREFIX, MessagePack.Code.BIN8, MessagePack.Code.EXT16, -98, -22, -65, -118, MessagePack.Code.INT32, 64, MessagePack.Code.EXT8, 56, -75, -93, -9, -14, MessagePack.Code.UINT32, -7, 97, 21, -95, MessagePack.Code.NEGFIXINT_PREFIX, -82, 93, -92, -101, TarHeader.LF_BLK, 26, 85, -83, -109, TarHeader.LF_SYMLINK, TarHeader.LF_NORMAL, -11, -116, -79, -29, 29, -10, -30, 46, -126, 102, MessagePack.Code.FLOAT32, 96, MessagePack.Code.NIL, 41, 35, -85, Draft_75.CR, 83, 78, 111, MessagePack.Code.FIXEXT2, MessagePack.Code.STR32, TarHeader.LF_CONTIG, 69, MessagePack.Code.MAP16, -3, -114, 47, 3, -1, 106, 114, 109, 108, 91, 81, -115, 27, -81, -110, -69, MessagePack.Code.ARRAY32, PSSSigner.TRAILER_IMPLICIT, ByteCompanionObject.MAX_VALUE, 17, MessagePack.Code.STR8, 92, 65, 31, Tnaf.POW_2_WIDTH, 90, MessagePack.Code.FIXEXT16, 10, MessagePack.Code.NEVER_USED, TarHeader.LF_LINK, -120, -91, MessagePack.Code.UINT16, 123, -67, 45, 116, MessagePack.Code.INT8, 18, -72, -27, -76, -80, -119, 105, -105, 74, 12, -106, 119, 126, 101, -71, -15, 9, MessagePack.Code.BIN16, 110, MessagePack.Code.BIN32, -124, 24, -16, 125, -20, 58, MessagePack.Code.ARRAY16, 77, 32, 121, -18, 95, 62, MessagePack.Code.FIXEXT8, MessagePack.Code.FLOAT64, 57, 72};
    private final int[] X = new int[4];
    private int[] rk;

    private int F0(int[] iArr, int i) {
        return iArr[0] ^ T(((iArr[1] ^ iArr[2]) ^ iArr[3]) ^ i);
    }

    private int F1(int[] iArr, int i) {
        return iArr[1] ^ T(((iArr[2] ^ iArr[3]) ^ iArr[0]) ^ i);
    }

    private int F2(int[] iArr, int i) {
        return iArr[2] ^ T(((iArr[3] ^ iArr[0]) ^ iArr[1]) ^ i);
    }

    private int F3(int[] iArr, int i) {
        return iArr[3] ^ T(((iArr[0] ^ iArr[1]) ^ iArr[2]) ^ i);
    }

    private int L(int i) {
        return (((rotateLeft(i, 2) ^ i) ^ rotateLeft(i, 10)) ^ rotateLeft(i, 18)) ^ rotateLeft(i, 24);
    }

    private int L_ap(int i) {
        return (rotateLeft(i, 13) ^ i) ^ rotateLeft(i, 23);
    }

    private void R(int[] iArr, int i) {
        int i2 = i + 1;
        int i3 = i + 2;
        int i4 = i + 3;
        iArr[i] = iArr[i] ^ iArr[i4];
        iArr[i4] = iArr[i] ^ iArr[i4];
        iArr[i] = iArr[i4] ^ iArr[i];
        iArr[i2] = iArr[i2] ^ iArr[i3];
        iArr[i3] = iArr[i2] ^ iArr[i3];
        iArr[i2] = iArr[i3] ^ iArr[i2];
    }

    private int T(int i) {
        return L(tau(i));
    }

    private int T_ap(int i) {
        return L_ap(tau(i));
    }

    private int[] expandKey(boolean z, byte[] bArr) {
        int[] iArr = new int[32];
        int[] iArr2 = {Pack.bigEndianToInt(bArr, 0), Pack.bigEndianToInt(bArr, 4), Pack.bigEndianToInt(bArr, 8), Pack.bigEndianToInt(bArr, 12)};
        int[] iArr3 = {iArr2[0] ^ FK[0], iArr2[1] ^ FK[1], iArr2[2] ^ FK[2], iArr2[3] ^ FK[3]};
        if (z) {
            iArr[0] = iArr3[0] ^ T_ap(((iArr3[1] ^ iArr3[2]) ^ iArr3[3]) ^ CK[0]);
            iArr[1] = iArr3[1] ^ T_ap(((iArr3[2] ^ iArr3[3]) ^ iArr[0]) ^ CK[1]);
            iArr[2] = iArr3[2] ^ T_ap(((iArr3[3] ^ iArr[0]) ^ iArr[1]) ^ CK[2]);
            iArr[3] = iArr3[3] ^ T_ap(((iArr[0] ^ iArr[1]) ^ iArr[2]) ^ CK[3]);
            for (int i = 4; i < 32; i++) {
                iArr[i] = iArr[i - 4] ^ T_ap(((iArr[i - 3] ^ iArr[i - 2]) ^ iArr[i - 1]) ^ CK[i]);
            }
        } else {
            iArr[31] = iArr3[0] ^ T_ap(((iArr3[1] ^ iArr3[2]) ^ iArr3[3]) ^ CK[0]);
            iArr[30] = iArr3[1] ^ T_ap(((iArr3[2] ^ iArr3[3]) ^ iArr[31]) ^ CK[1]);
            iArr[29] = iArr3[2] ^ T_ap(((iArr3[3] ^ iArr[31]) ^ iArr[30]) ^ CK[2]);
            iArr[28] = iArr3[3] ^ T_ap(((iArr[31] ^ iArr[30]) ^ iArr[29]) ^ CK[3]);
            for (int i2 = 27; i2 >= 0; i2--) {
                iArr[i2] = iArr[i2 + 4] ^ T_ap(((iArr[i2 + 3] ^ iArr[i2 + 2]) ^ iArr[i2 + 1]) ^ CK[31 - i2]);
            }
        }
        return iArr;
    }

    private int rotateLeft(int i, int i2) {
        return (i << i2) | (i >>> (-i2));
    }

    private int tau(int i) {
        return ((Sbox[(i >> 24) & 255] & 255) << 24) | ((Sbox[(i >> 16) & 255] & 255) << Tnaf.POW_2_WIDTH) | ((Sbox[(i >> 8) & 255] & 255) << 8) | (Sbox[i & 255] & 255);
    }

    public String getAlgorithmName() {
        return "SM4";
    }

    public int getBlockSize() {
        return 16;
    }

    public void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof KeyParameter) {
            byte[] key = ((KeyParameter) cipherParameters).getKey();
            if (key.length != 16) {
                throw new IllegalArgumentException("SM4 requires a 128 bit key");
            }
            this.rk = expandKey(z, key);
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to SM4 init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) throws DataLengthException, IllegalStateException {
        if (this.rk == null) {
            throw new IllegalStateException("SM4 not initialised");
        } else if (i + 16 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + 16 > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            this.X[0] = Pack.bigEndianToInt(bArr, i);
            this.X[1] = Pack.bigEndianToInt(bArr, i + 4);
            this.X[2] = Pack.bigEndianToInt(bArr, i + 8);
            this.X[3] = Pack.bigEndianToInt(bArr, i + 12);
            for (int i3 = 0; i3 < 32; i3 += 4) {
                this.X[0] = F0(this.X, this.rk[i3]);
                this.X[1] = F1(this.X, this.rk[i3 + 1]);
                this.X[2] = F2(this.X, this.rk[i3 + 2]);
                this.X[3] = F3(this.X, this.rk[i3 + 3]);
            }
            R(this.X, 0);
            Pack.intToBigEndian(this.X[0], bArr2, i2);
            Pack.intToBigEndian(this.X[1], bArr2, i2 + 4);
            Pack.intToBigEndian(this.X[2], bArr2, i2 + 8);
            Pack.intToBigEndian(this.X[3], bArr2, i2 + 12);
            return 16;
        }
    }

    public void reset() {
    }
}
