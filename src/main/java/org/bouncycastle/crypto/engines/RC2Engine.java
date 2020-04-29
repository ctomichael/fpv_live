package org.bouncycastle.crypto.engines;

import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.RC2Parameters;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

public class RC2Engine implements BlockCipher {
    private static final int BLOCK_SIZE = 8;
    private static byte[] piTable = {MessagePack.Code.STR8, 120, -7, MessagePack.Code.BIN8, 25, MessagePack.Code.ARRAY32, -75, -19, 40, -23, -3, 121, 74, MessagePack.Code.FIXSTR_PREFIX, MessagePack.Code.FIXEXT16, -99, MessagePack.Code.BIN32, 126, TarHeader.LF_CONTIG, -125, 43, 118, 83, -114, 98, 76, 100, -120, 68, -117, -5, -94, 23, -102, 89, -11, -121, -77, 79, 19, 97, 69, 109, -115, 9, -127, 125, TarHeader.LF_SYMLINK, -67, -113, 64, -21, -122, -73, 123, 11, -16, -107, 33, 34, 92, 107, 78, -126, 84, MessagePack.Code.FIXEXT4, 101, -109, MessagePack.Code.UINT32, 96, -78, 28, 115, 86, MessagePack.Code.NIL, 20, -89, -116, -15, MessagePack.Code.ARRAY16, 18, 117, MessagePack.Code.FLOAT32, 31, 59, -66, -28, MessagePack.Code.INT16, 66, 61, MessagePack.Code.FIXEXT1, TarHeader.LF_NORMAL, -93, 60, -74, 38, 111, -65, 14, MessagePack.Code.STR16, 70, 105, 7, 87, 39, -14, 29, -101, PSSSigner.TRAILER_IMPLICIT, -108, 67, 3, -8, 17, MessagePack.Code.EXT8, -10, MessagePack.Code.FIXARRAY_PREFIX, -17, 62, -25, 6, MessagePack.Code.TRUE, MessagePack.Code.FIXEXT2, 47, MessagePack.Code.EXT16, 102, 30, MessagePack.Code.FIXEXT8, 8, -24, -22, MessagePack.Code.MAP16, Byte.MIN_VALUE, 82, -18, -9, -124, -86, 114, -84, TarHeader.LF_DIR, 77, 106, 42, -106, 26, MessagePack.Code.INT32, 113, 90, 21, 73, 116, 75, -97, MessagePack.Code.INT8, 94, 4, 24, -92, -20, MessagePack.Code.FALSE, MessagePack.Code.NEGFIXINT_PREFIX, 65, 110, 15, 81, MessagePack.Code.FLOAT64, MessagePack.Code.UINT8, 36, -111, -81, 80, -95, -12, 112, 57, -103, 124, 58, -123, 35, -72, -76, 122, -4, 2, TarHeader.LF_FIFO, 91, 37, 85, -105, TarHeader.LF_LINK, 45, 93, -6, -104, -29, -118, -110, -82, 5, MessagePack.Code.MAP32, 41, Tnaf.POW_2_WIDTH, 103, 108, -70, MessagePack.Code.EXT32, MessagePack.Code.INT64, 0, -26, MessagePack.Code.UINT64, -31, -98, -88, 44, 99, 22, 1, 63, 88, -30, -119, -87, Draft_75.CR, 56, TarHeader.LF_BLK, 27, -85, TarHeader.LF_CHR, -1, -80, -69, 72, 12, 95, -71, -79, MessagePack.Code.UINT16, 46, MessagePack.Code.BIN16, -13, MessagePack.Code.STR32, 71, -27, -91, -100, 119, 10, -90, 32, 104, -2, ByteCompanionObject.MAX_VALUE, MessagePack.Code.NEVER_USED, -83};
    private boolean encrypting;
    private int[] workingKey;

    private void decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = ((bArr[i + 7] & 255) << 8) + (bArr[i + 6] & 255);
        int i4 = ((bArr[i + 5] & 255) << 8) + (bArr[i + 4] & 255);
        int i5 = ((bArr[i + 3] & 255) << 8) + (bArr[i + 2] & 255);
        int i6 = (bArr[i + 0] & 255) + ((bArr[i + 1] & 255) << 8);
        for (int i7 = 60; i7 >= 44; i7 -= 4) {
            i3 = rotateWordLeft(i3, 11) - ((((i4 ^ -1) & i6) + (i5 & i4)) + this.workingKey[i7 + 3]);
            i4 = rotateWordLeft(i4, 13) - ((((i5 ^ -1) & i3) + (i6 & i5)) + this.workingKey[i7 + 2]);
            i5 = rotateWordLeft(i5, 14) - ((((i6 ^ -1) & i4) + (i3 & i6)) + this.workingKey[i7 + 1]);
            i6 = rotateWordLeft(i6, 15) - ((((i3 ^ -1) & i5) + (i4 & i3)) + this.workingKey[i7]);
        }
        int i8 = i3 - this.workingKey[i4 & 63];
        int i9 = i4 - this.workingKey[i5 & 63];
        int i10 = i5 - this.workingKey[i6 & 63];
        int i11 = i6 - this.workingKey[i8 & 63];
        for (int i12 = 40; i12 >= 20; i12 -= 4) {
            i8 = rotateWordLeft(i8, 11) - ((((i9 ^ -1) & i11) + (i10 & i9)) + this.workingKey[i12 + 3]);
            i9 = rotateWordLeft(i9, 13) - ((((i10 ^ -1) & i8) + (i11 & i10)) + this.workingKey[i12 + 2]);
            i10 = rotateWordLeft(i10, 14) - ((((i11 ^ -1) & i9) + (i8 & i11)) + this.workingKey[i12 + 1]);
            i11 = rotateWordLeft(i11, 15) - ((((i8 ^ -1) & i10) + (i9 & i8)) + this.workingKey[i12]);
        }
        int i13 = i8 - this.workingKey[i9 & 63];
        int i14 = i9 - this.workingKey[i10 & 63];
        int i15 = i10 - this.workingKey[i11 & 63];
        int i16 = i11 - this.workingKey[i13 & 63];
        for (int i17 = 16; i17 >= 0; i17 -= 4) {
            i13 = rotateWordLeft(i13, 11) - ((((i14 ^ -1) & i16) + (i15 & i14)) + this.workingKey[i17 + 3]);
            i14 = rotateWordLeft(i14, 13) - ((((i15 ^ -1) & i13) + (i16 & i15)) + this.workingKey[i17 + 2]);
            i15 = rotateWordLeft(i15, 14) - ((((i16 ^ -1) & i14) + (i13 & i16)) + this.workingKey[i17 + 1]);
            i16 = rotateWordLeft(i16, 15) - ((((i13 ^ -1) & i15) + (i14 & i13)) + this.workingKey[i17]);
        }
        bArr2[i2 + 0] = (byte) i16;
        bArr2[i2 + 1] = (byte) (i16 >> 8);
        bArr2[i2 + 2] = (byte) i15;
        bArr2[i2 + 3] = (byte) (i15 >> 8);
        bArr2[i2 + 4] = (byte) i14;
        bArr2[i2 + 5] = (byte) (i14 >> 8);
        bArr2[i2 + 6] = (byte) i13;
        bArr2[i2 + 7] = (byte) (i13 >> 8);
    }

    private void encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = ((bArr[i + 7] & 255) << 8) + (bArr[i + 6] & 255);
        int i4 = ((bArr[i + 5] & 255) << 8) + (bArr[i + 4] & 255);
        int i5 = ((bArr[i + 3] & 255) << 8) + (bArr[i + 2] & 255);
        int i6 = (bArr[i + 0] & 255) + ((bArr[i + 1] & 255) << 8);
        for (int i7 = 0; i7 <= 16; i7 += 4) {
            i6 = rotateWordLeft(i6 + ((i3 ^ -1) & i5) + (i4 & i3) + this.workingKey[i7], 1);
            i5 = rotateWordLeft(i5 + ((i6 ^ -1) & i4) + (i3 & i6) + this.workingKey[i7 + 1], 2);
            i4 = rotateWordLeft(i4 + ((i5 ^ -1) & i3) + (i6 & i5) + this.workingKey[i7 + 2], 3);
            i3 = rotateWordLeft(i3 + ((i4 ^ -1) & i6) + (i5 & i4) + this.workingKey[i7 + 3], 5);
        }
        int i8 = i6 + this.workingKey[i3 & 63];
        int i9 = i5 + this.workingKey[i8 & 63];
        int i10 = i4 + this.workingKey[i9 & 63];
        int i11 = i3 + this.workingKey[i10 & 63];
        for (int i12 = 20; i12 <= 40; i12 += 4) {
            i8 = rotateWordLeft(i8 + ((i11 ^ -1) & i9) + (i10 & i11) + this.workingKey[i12], 1);
            i9 = rotateWordLeft(i9 + ((i8 ^ -1) & i10) + (i11 & i8) + this.workingKey[i12 + 1], 2);
            i10 = rotateWordLeft(i10 + ((i9 ^ -1) & i11) + (i8 & i9) + this.workingKey[i12 + 2], 3);
            i11 = rotateWordLeft(i11 + ((i10 ^ -1) & i8) + (i9 & i10) + this.workingKey[i12 + 3], 5);
        }
        int i13 = i8 + this.workingKey[i11 & 63];
        int i14 = i9 + this.workingKey[i13 & 63];
        int i15 = i10 + this.workingKey[i14 & 63];
        int i16 = i11 + this.workingKey[i15 & 63];
        for (int i17 = 44; i17 < 64; i17 += 4) {
            i13 = rotateWordLeft(i13 + ((i16 ^ -1) & i14) + (i15 & i16) + this.workingKey[i17], 1);
            i14 = rotateWordLeft(i14 + ((i13 ^ -1) & i15) + (i16 & i13) + this.workingKey[i17 + 1], 2);
            i15 = rotateWordLeft(i15 + ((i14 ^ -1) & i16) + (i13 & i14) + this.workingKey[i17 + 2], 3);
            i16 = rotateWordLeft(i16 + ((i15 ^ -1) & i13) + (i14 & i15) + this.workingKey[i17 + 3], 5);
        }
        bArr2[i2 + 0] = (byte) i13;
        bArr2[i2 + 1] = (byte) (i13 >> 8);
        bArr2[i2 + 2] = (byte) i14;
        bArr2[i2 + 3] = (byte) (i14 >> 8);
        bArr2[i2 + 4] = (byte) i15;
        bArr2[i2 + 5] = (byte) (i15 >> 8);
        bArr2[i2 + 6] = (byte) i16;
        bArr2[i2 + 7] = (byte) (i16 >> 8);
    }

    private int[] generateWorkingKey(byte[] bArr, int i) {
        int[] iArr = new int[128];
        for (int i2 = 0; i2 != bArr.length; i2++) {
            iArr[i2] = bArr[i2] & 255;
        }
        int length = bArr.length;
        if (length < 128) {
            int i3 = 0;
            int i4 = length;
            int i5 = iArr[length - 1];
            while (true) {
                int i6 = i3 + 1;
                i5 = piTable[(iArr[i3] + i5) & 255] & 255;
                int i7 = i4 + 1;
                iArr[i4] = i5;
                if (i7 >= 128) {
                    break;
                }
                i3 = i6;
                i4 = i7;
            }
        }
        int i8 = (i + 7) >> 3;
        int i9 = piTable[iArr[128 - i8] & (255 >> ((-i) & 7))] & 255;
        iArr[128 - i8] = i9;
        for (int i10 = (128 - i8) - 1; i10 >= 0; i10--) {
            i9 = piTable[i9 ^ iArr[i10 + i8]] & 255;
            iArr[i10] = i9;
        }
        int[] iArr2 = new int[64];
        for (int i11 = 0; i11 != iArr2.length; i11++) {
            iArr2[i11] = iArr[i11 * 2] + (iArr[(i11 * 2) + 1] << 8);
        }
        return iArr2;
    }

    private int rotateWordLeft(int i, int i2) {
        int i3 = 65535 & i;
        return (i3 >> (16 - i2)) | (i3 << i2);
    }

    public String getAlgorithmName() {
        return "RC2";
    }

    public int getBlockSize() {
        return 8;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.encrypting = z;
        if (cipherParameters instanceof RC2Parameters) {
            RC2Parameters rC2Parameters = (RC2Parameters) cipherParameters;
            this.workingKey = generateWorkingKey(rC2Parameters.getKey(), rC2Parameters.getEffectiveKeyBits());
        } else if (cipherParameters instanceof KeyParameter) {
            byte[] key = ((KeyParameter) cipherParameters).getKey();
            this.workingKey = generateWorkingKey(key, key.length * 8);
        } else {
            throw new IllegalArgumentException("invalid parameter passed to RC2 init - " + cipherParameters.getClass().getName());
        }
    }

    public final int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.workingKey == null) {
            throw new IllegalStateException("RC2 engine not initialised");
        } else if (i + 8 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + 8 > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else if (this.encrypting) {
            encryptBlock(bArr, i, bArr2, i2);
            return 8;
        } else {
            decryptBlock(bArr, i, bArr2, i2);
            return 8;
        }
    }

    public void reset() {
    }
}
