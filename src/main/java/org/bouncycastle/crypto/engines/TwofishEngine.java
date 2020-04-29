package org.bouncycastle.crypto.engines;

import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

public final class TwofishEngine implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final int GF256_FDBK = 361;
    private static final int GF256_FDBK_2 = 180;
    private static final int GF256_FDBK_4 = 90;
    private static final int INPUT_WHITEN = 0;
    private static final int MAX_KEY_BITS = 256;
    private static final int MAX_ROUNDS = 16;
    private static final int OUTPUT_WHITEN = 4;
    private static final byte[][] P = {new byte[]{-87, 103, -77, -24, 4, -3, -93, 118, -102, -110, Byte.MIN_VALUE, 120, -28, MessagePack.Code.ARRAY32, MessagePack.Code.INT16, 56, Draft_75.CR, MessagePack.Code.BIN32, TarHeader.LF_DIR, -104, 24, -9, -20, 108, 67, 117, TarHeader.LF_CONTIG, 38, -6, 19, -108, 72, -14, MessagePack.Code.INT8, -117, TarHeader.LF_NORMAL, -124, 84, MessagePack.Code.MAP32, 35, 25, 91, 61, 89, -13, -82, -94, -126, 99, 1, -125, 46, MessagePack.Code.STR8, 81, -101, 124, -90, -21, -91, -66, 22, 12, -29, 97, MessagePack.Code.NIL, -116, 58, -11, 115, 44, 37, 11, -69, 78, -119, 107, 83, 106, -76, -15, -31, -26, -67, 69, -30, -12, -74, 102, MessagePack.Code.UINT8, -107, 3, 86, MessagePack.Code.FIXEXT1, 28, 30, MessagePack.Code.FIXEXT8, -5, MessagePack.Code.TRUE, -114, -75, -23, MessagePack.Code.UINT64, -65, -70, -22, 119, 57, -81, TarHeader.LF_CHR, MessagePack.Code.EXT32, 98, 113, -127, 121, 9, -83, 36, MessagePack.Code.UINT16, -7, MessagePack.Code.FIXEXT16, -27, MessagePack.Code.BIN16, -71, 77, 68, 8, -122, -25, -95, 29, -86, -19, 6, 112, -78, MessagePack.Code.INT32, 65, 123, MessagePack.Code.FIXSTR_PREFIX, 17, TarHeader.LF_LINK, MessagePack.Code.FALSE, 39, MessagePack.Code.FIXARRAY_PREFIX, 32, -10, 96, -1, -106, 92, -79, -85, -98, -100, 82, 27, 95, -109, 10, -17, -111, -123, 73, -18, 45, 79, -113, 59, 71, -121, 109, 70, MessagePack.Code.FIXEXT4, 62, 105, 100, 42, MessagePack.Code.UINT32, MessagePack.Code.FLOAT64, 47, -4, -105, 5, 122, -84, ByteCompanionObject.MAX_VALUE, MessagePack.Code.FIXEXT2, 26, 75, 14, -89, 90, 40, 20, 63, 41, -120, 60, 76, 2, -72, MessagePack.Code.STR16, -80, 23, 85, 31, -118, 125, 87, MessagePack.Code.EXT8, -115, 116, -73, MessagePack.Code.BIN8, -97, 114, 126, 21, 34, 18, 88, 7, -103, TarHeader.LF_BLK, 110, 80, MessagePack.Code.MAP16, 104, 101, PSSSigner.TRAILER_IMPLICIT, MessagePack.Code.STR32, -8, MessagePack.Code.EXT16, -88, 43, 64, MessagePack.Code.ARRAY16, -2, TarHeader.LF_SYMLINK, -92, MessagePack.Code.FLOAT32, Tnaf.POW_2_WIDTH, 33, -16, MessagePack.Code.INT64, 93, 15, 0, 111, -99, TarHeader.LF_FIFO, 66, 74, 94, MessagePack.Code.NEVER_USED, MessagePack.Code.NEGFIXINT_PREFIX}, new byte[]{117, -13, MessagePack.Code.BIN32, -12, MessagePack.Code.STR32, 123, -5, MessagePack.Code.EXT16, 74, MessagePack.Code.INT64, -26, 107, 69, 125, -24, 75, MessagePack.Code.FIXEXT4, TarHeader.LF_SYMLINK, MessagePack.Code.FIXEXT16, -3, TarHeader.LF_CONTIG, 113, -15, -31, TarHeader.LF_NORMAL, 15, -8, 27, -121, -6, 6, 63, 94, -70, -82, 91, -118, 0, PSSSigner.TRAILER_IMPLICIT, -99, 109, MessagePack.Code.NEVER_USED, -79, 14, Byte.MIN_VALUE, 93, MessagePack.Code.INT32, MessagePack.Code.FIXEXT2, MessagePack.Code.FIXSTR_PREFIX, -124, 7, 20, -75, MessagePack.Code.FIXARRAY_PREFIX, 44, -93, -78, 115, 76, 84, -110, 116, TarHeader.LF_FIFO, 81, 56, -80, -67, 90, -4, 96, 98, -106, 108, 66, -9, Tnaf.POW_2_WIDTH, 124, 40, 39, -116, 19, -107, -100, MessagePack.Code.EXT8, 36, 70, 59, 112, MessagePack.Code.FLOAT32, -29, -123, MessagePack.Code.FLOAT64, 17, MessagePack.Code.INT8, -109, -72, -90, -125, 32, -1, -97, 119, MessagePack.Code.TRUE, MessagePack.Code.UINT8, 3, 111, 8, -65, 64, -25, 43, -30, 121, 12, -86, -126, 65, 58, -22, -71, -28, -102, -92, -105, 126, MessagePack.Code.STR16, 122, 23, 102, -108, -95, 29, 61, -16, MessagePack.Code.MAP16, -77, 11, 114, -89, 28, -17, MessagePack.Code.INT16, 83, 62, -113, TarHeader.LF_CHR, 38, 95, -20, 118, 42, 73, -127, -120, -18, 33, MessagePack.Code.BIN8, 26, -21, MessagePack.Code.STR8, MessagePack.Code.BIN16, 57, -103, MessagePack.Code.UINT16, -83, TarHeader.LF_LINK, -117, 1, 24, 35, MessagePack.Code.ARRAY32, 31, 78, 45, -7, 72, 79, -14, 101, -114, 120, 92, 88, 25, -115, -27, -104, 87, 103, ByteCompanionObject.MAX_VALUE, 5, 100, -81, 99, -74, -2, -11, -73, 60, -91, MessagePack.Code.UINT32, -23, 104, 68, MessagePack.Code.NEGFIXINT_PREFIX, 77, 67, 105, 41, 46, -84, 21, 89, -88, 10, -98, 110, 71, MessagePack.Code.MAP32, TarHeader.LF_BLK, TarHeader.LF_DIR, 106, MessagePack.Code.UINT64, MessagePack.Code.ARRAY16, 34, MessagePack.Code.EXT32, MessagePack.Code.NIL, -101, -119, MessagePack.Code.FIXEXT1, -19, -85, 18, -94, Draft_75.CR, 82, -69, 2, 47, -87, MessagePack.Code.FIXEXT8, 97, 30, -76, 80, 4, -10, MessagePack.Code.FALSE, 22, 37, -122, 86, 85, 9, -66, -111}};
    private static final int P_00 = 1;
    private static final int P_01 = 0;
    private static final int P_02 = 0;
    private static final int P_03 = 1;
    private static final int P_04 = 1;
    private static final int P_10 = 0;
    private static final int P_11 = 0;
    private static final int P_12 = 1;
    private static final int P_13 = 1;
    private static final int P_14 = 0;
    private static final int P_20 = 1;
    private static final int P_21 = 1;
    private static final int P_22 = 0;
    private static final int P_23 = 0;
    private static final int P_24 = 0;
    private static final int P_30 = 0;
    private static final int P_31 = 1;
    private static final int P_32 = 1;
    private static final int P_33 = 0;
    private static final int P_34 = 1;
    private static final int ROUNDS = 16;
    private static final int ROUND_SUBKEYS = 8;
    private static final int RS_GF_FDBK = 333;
    private static final int SK_BUMP = 16843009;
    private static final int SK_ROTL = 9;
    private static final int SK_STEP = 33686018;
    private static final int TOTAL_SUBKEYS = 40;
    private boolean encrypting = false;
    private int[] gMDS0 = new int[256];
    private int[] gMDS1 = new int[256];
    private int[] gMDS2 = new int[256];
    private int[] gMDS3 = new int[256];
    private int[] gSBox;
    private int[] gSubKeys;
    private int k64Cnt = 0;
    private byte[] workingKey = null;

    public TwofishEngine() {
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        int[] iArr3 = new int[2];
        for (int i = 0; i < 256; i++) {
            int i2 = P[0][i] & 255;
            iArr[0] = i2;
            iArr2[0] = Mx_X(i2) & 255;
            iArr3[0] = Mx_Y(i2) & 255;
            int i3 = P[1][i] & 255;
            iArr[1] = i3;
            iArr2[1] = Mx_X(i3) & 255;
            iArr3[1] = Mx_Y(i3) & 255;
            this.gMDS0[i] = iArr[1] | (iArr2[1] << 8) | (iArr3[1] << 16) | (iArr3[1] << 24);
            this.gMDS1[i] = iArr3[0] | (iArr3[0] << 8) | (iArr2[0] << 16) | (iArr[0] << 24);
            this.gMDS2[i] = iArr2[1] | (iArr3[1] << 8) | (iArr[1] << 16) | (iArr3[1] << 24);
            this.gMDS3[i] = iArr2[0] | (iArr[0] << 8) | (iArr3[0] << 16) | (iArr2[0] << 24);
        }
    }

    private void Bits32ToBytes(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
        bArr[i2 + 1] = (byte) (i >> 8);
        bArr[i2 + 2] = (byte) (i >> 16);
        bArr[i2 + 3] = (byte) (i >> 24);
    }

    private int BytesTo32Bits(byte[] bArr, int i) {
        return (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << Tnaf.POW_2_WIDTH) | ((bArr[i + 3] & 255) << 24);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private int F32(int i, int[] iArr) {
        int b0 = b0(i);
        int b1 = b1(i);
        int b2 = b2(i);
        int b3 = b3(i);
        int i2 = iArr[0];
        int i3 = iArr[1];
        int i4 = iArr[2];
        int i5 = iArr[3];
        switch (this.k64Cnt & 3) {
            case 0:
                b0 = (P[1][b0] & 255) ^ b0(i5);
                b1 = (P[0][b1] & 255) ^ b1(i5);
                b2 = (P[0][b2] & 255) ^ b2(i5);
                b3 = (P[1][b3] & 255) ^ b3(i5);
                b0 = (P[1][b0] & 255) ^ b0(i4);
                b1 = (P[1][b1] & 255) ^ b1(i4);
                b2 = (P[0][b2] & 255) ^ b2(i4);
                b3 = (P[0][b3] & 255) ^ b3(i4);
                break;
            case 1:
                return this.gMDS3[(P[1][b3] & 255) ^ b3(i2)] ^ (this.gMDS2[(P[1][b2] & 255) ^ b2(i2)] ^ (this.gMDS1[(P[0][b1] & 255) ^ b1(i2)] ^ this.gMDS0[(P[0][b0] & 255) ^ b0(i2)]));
            case 2:
                break;
            case 3:
                b0 = (P[1][b0] & 255) ^ b0(i4);
                b1 = (P[1][b1] & 255) ^ b1(i4);
                b2 = (P[0][b2] & 255) ^ b2(i4);
                b3 = (P[0][b3] & 255) ^ b3(i4);
                break;
            default:
                return 0;
        }
        return this.gMDS3[(P[1][(P[1][b3] & 255) ^ b3(i3)] & 255) ^ b3(i2)] ^ (this.gMDS2[(P[1][(P[0][b2] & 255) ^ b2(i3)] & 255) ^ b2(i2)] ^ (this.gMDS1[(P[0][(P[1][b1] & 255) ^ b1(i3)] & 255) ^ b1(i2)] ^ this.gMDS0[(P[0][(P[0][b0] & 255) ^ b0(i3)] & 255) ^ b0(i2)]));
    }

    private int Fe32_0(int i) {
        return ((this.gSBox[((i & 255) * 2) + 0] ^ this.gSBox[(((i >>> 8) & 255) * 2) + 1]) ^ this.gSBox[(((i >>> 16) & 255) * 2) + 512]) ^ this.gSBox[(((i >>> 24) & 255) * 2) + 513];
    }

    private int Fe32_3(int i) {
        return ((this.gSBox[(((i >>> 24) & 255) * 2) + 0] ^ this.gSBox[((i & 255) * 2) + 1]) ^ this.gSBox[(((i >>> 8) & 255) * 2) + 512]) ^ this.gSBox[(((i >>> 16) & 255) * 2) + 513];
    }

    private int LFSR1(int i) {
        return ((i & 1) != 0 ? 180 : 0) ^ (i >> 1);
    }

    private int LFSR2(int i) {
        int i2 = 0;
        int i3 = ((i & 2) != 0 ? 180 : 0) ^ (i >> 2);
        if ((i & 1) != 0) {
            i2 = 90;
        }
        return i2 ^ i3;
    }

    private int Mx_X(int i) {
        return LFSR2(i) ^ i;
    }

    private int Mx_Y(int i) {
        return (LFSR1(i) ^ i) ^ LFSR2(i);
    }

    private int RS_MDS_Encode(int i, int i2) {
        for (int i3 = 0; i3 < 4; i3++) {
            i2 = RS_rem(i2);
        }
        int i4 = i2 ^ i;
        for (int i5 = 0; i5 < 4; i5++) {
            i4 = RS_rem(i4);
        }
        return i4;
    }

    private int RS_rem(int i) {
        int i2 = 0;
        int i3 = (i >>> 24) & 255;
        int i4 = (((i3 & 128) != 0 ? RS_GF_FDBK : 0) ^ (i3 << 1)) & 255;
        int i5 = i3 >>> 1;
        if ((i3 & 1) != 0) {
            i2 = 166;
        }
        int i6 = (i2 ^ i5) ^ i4;
        return ((i6 << 8) ^ ((i4 << 16) ^ ((i << 8) ^ (i6 << 24)))) ^ i3;
    }

    private int b0(int i) {
        return i & 255;
    }

    private int b1(int i) {
        return (i >>> 8) & 255;
    }

    private int b2(int i) {
        return (i >>> 16) & 255;
    }

    private int b3(int i) {
        return (i >>> 24) & 255;
    }

    private void decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int BytesTo32Bits = BytesTo32Bits(bArr, i) ^ this.gSubKeys[4];
        int BytesTo32Bits2 = BytesTo32Bits(bArr, i + 4) ^ this.gSubKeys[5];
        int BytesTo32Bits3 = BytesTo32Bits(bArr, i + 8) ^ this.gSubKeys[6];
        int BytesTo32Bits4 = BytesTo32Bits(bArr, i + 12) ^ this.gSubKeys[7];
        int i3 = 39;
        for (int i4 = 0; i4 < 16; i4 += 2) {
            int Fe32_0 = Fe32_0(BytesTo32Bits);
            int Fe32_3 = Fe32_3(BytesTo32Bits2);
            int i5 = i3 - 1;
            int i6 = (this.gSubKeys[i3] + ((Fe32_3 * 2) + Fe32_0)) ^ BytesTo32Bits4;
            int i7 = (BytesTo32Bits3 << 1) | (BytesTo32Bits3 >>> 31);
            int i8 = Fe32_0 + Fe32_3;
            int i9 = i5 - 1;
            BytesTo32Bits3 = (i8 + this.gSubKeys[i5]) ^ i7;
            BytesTo32Bits4 = (i6 >>> 1) | (i6 << 31);
            int Fe32_02 = Fe32_0(BytesTo32Bits3);
            int Fe32_32 = Fe32_3(BytesTo32Bits4);
            int i10 = i9 - 1;
            int i11 = BytesTo32Bits2 ^ (this.gSubKeys[i9] + ((Fe32_32 * 2) + Fe32_02));
            int i12 = Fe32_32 + Fe32_02;
            i3 = i10 - 1;
            BytesTo32Bits = ((BytesTo32Bits >>> 31) | (BytesTo32Bits << 1)) ^ (i12 + this.gSubKeys[i10]);
            BytesTo32Bits2 = (i11 << 31) | (i11 >>> 1);
        }
        Bits32ToBytes(this.gSubKeys[0] ^ BytesTo32Bits3, bArr2, i2);
        Bits32ToBytes(this.gSubKeys[1] ^ BytesTo32Bits4, bArr2, i2 + 4);
        Bits32ToBytes(this.gSubKeys[2] ^ BytesTo32Bits, bArr2, i2 + 8);
        Bits32ToBytes(this.gSubKeys[3] ^ BytesTo32Bits2, bArr2, i2 + 12);
    }

    private void encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int BytesTo32Bits = BytesTo32Bits(bArr, i) ^ this.gSubKeys[0];
        int BytesTo32Bits2 = BytesTo32Bits(bArr, i + 4) ^ this.gSubKeys[1];
        int BytesTo32Bits3 = BytesTo32Bits(bArr, i + 8) ^ this.gSubKeys[2];
        int BytesTo32Bits4 = this.gSubKeys[3] ^ BytesTo32Bits(bArr, i + 12);
        int i3 = 8;
        for (int i4 = 0; i4 < 16; i4 += 2) {
            int Fe32_0 = Fe32_0(BytesTo32Bits);
            int Fe32_3 = Fe32_3(BytesTo32Bits2);
            int i5 = i3 + 1;
            int i6 = (this.gSubKeys[i3] + (Fe32_0 + Fe32_3)) ^ BytesTo32Bits3;
            BytesTo32Bits3 = (i6 >>> 1) | (i6 << 31);
            int i7 = i5 + 1;
            BytesTo32Bits4 = (((Fe32_3 * 2) + Fe32_0) + this.gSubKeys[i5]) ^ ((BytesTo32Bits4 << 1) | (BytesTo32Bits4 >>> 31));
            int Fe32_02 = Fe32_0(BytesTo32Bits3);
            int Fe32_32 = Fe32_3(BytesTo32Bits4);
            int i8 = i7 + 1;
            int i9 = BytesTo32Bits ^ (this.gSubKeys[i7] + (Fe32_02 + Fe32_32));
            BytesTo32Bits = (i9 << 31) | (i9 >>> 1);
            int i10 = (Fe32_32 * 2) + Fe32_02;
            i3 = i8 + 1;
            BytesTo32Bits2 = ((BytesTo32Bits2 >>> 31) | (BytesTo32Bits2 << 1)) ^ (i10 + this.gSubKeys[i8]);
        }
        Bits32ToBytes(this.gSubKeys[4] ^ BytesTo32Bits3, bArr2, i2);
        Bits32ToBytes(this.gSubKeys[5] ^ BytesTo32Bits4, bArr2, i2 + 4);
        Bits32ToBytes(this.gSubKeys[6] ^ BytesTo32Bits, bArr2, i2 + 8);
        Bits32ToBytes(this.gSubKeys[7] ^ BytesTo32Bits2, bArr2, i2 + 12);
    }

    private void setKey(byte[] bArr) {
        byte b;
        byte b2;
        byte b3;
        byte b4;
        byte b5;
        byte b6;
        byte b7;
        byte b8;
        int[] iArr = new int[4];
        int[] iArr2 = new int[4];
        int[] iArr3 = new int[4];
        this.gSubKeys = new int[40];
        if (this.k64Cnt < 1) {
            throw new IllegalArgumentException("Key size less than 64 bits");
        } else if (this.k64Cnt > 4) {
            throw new IllegalArgumentException("Key size larger than 256 bits");
        } else {
            for (int i = 0; i < this.k64Cnt; i++) {
                int i2 = i * 8;
                iArr[i] = BytesTo32Bits(bArr, i2);
                iArr2[i] = BytesTo32Bits(bArr, i2 + 4);
                iArr3[(this.k64Cnt - 1) - i] = RS_MDS_Encode(iArr[i], iArr2[i]);
            }
            for (int i3 = 0; i3 < 20; i3++) {
                int i4 = SK_STEP * i3;
                int F32 = F32(i4, iArr);
                int F322 = F32(i4 + SK_BUMP, iArr2);
                int i5 = (F322 >>> 24) | (F322 << 8);
                int i6 = F32 + i5;
                this.gSubKeys[i3 * 2] = i6;
                int i7 = i5 + i6;
                this.gSubKeys[(i3 * 2) + 1] = (i7 >>> 23) | (i7 << 9);
            }
            int i8 = iArr3[0];
            int i9 = iArr3[1];
            int i10 = iArr3[2];
            int i11 = iArr3[3];
            this.gSBox = new int[1024];
            for (int i12 = 0; i12 < 256; i12++) {
                switch (this.k64Cnt & 3) {
                    case 0:
                        b8 = (P[1][i12] & 255) ^ b0(i11);
                        b7 = (P[0][i12] & 255) ^ b1(i11);
                        b6 = b2(i11) ^ (P[0][i12] & 255);
                        b5 = (P[1][i12] & 255) ^ b3(i11);
                        b4 = (P[1][b8] & 255) ^ b0(i10);
                        b3 = (P[1][b7] & 255) ^ b1(i10);
                        b2 = (P[0][b6] & 255) ^ b2(i10);
                        b = (P[0][b5] & 255) ^ b3(i10);
                        break;
                    case 1:
                        this.gSBox[i12 * 2] = this.gMDS0[(P[0][i12] & 255) ^ b0(i8)];
                        this.gSBox[(i12 * 2) + 1] = this.gMDS1[(P[0][i12] & 255) ^ b1(i8)];
                        this.gSBox[(i12 * 2) + 512] = this.gMDS2[(P[1][i12] & 255) ^ b2(i8)];
                        this.gSBox[(i12 * 2) + 513] = this.gMDS3[(P[1][i12] & 255) ^ b3(i8)];
                        continue;
                    case 2:
                        b = i12;
                        b2 = i12;
                        b3 = i12;
                        b4 = i12;
                        break;
                    case 3:
                        b5 = i12;
                        b6 = i12;
                        b7 = i12;
                        b8 = i12;
                        b4 = (P[1][b8] & 255) ^ b0(i10);
                        b3 = (P[1][b7] & 255) ^ b1(i10);
                        b2 = (P[0][b6] & 255) ^ b2(i10);
                        b = (P[0][b5] & 255) ^ b3(i10);
                        break;
                    default:
                }
                this.gSBox[i12 * 2] = this.gMDS0[(P[0][(P[0][b4] & 255) ^ b0(i9)] & 255) ^ b0(i8)];
                this.gSBox[(i12 * 2) + 1] = this.gMDS1[(P[0][(P[1][b3] & 255) ^ b1(i9)] & 255) ^ b1(i8)];
                this.gSBox[(i12 * 2) + 512] = this.gMDS2[(P[1][(P[0][b2] & 255) ^ b2(i9)] & 255) ^ b2(i8)];
                this.gSBox[(i12 * 2) + 513] = this.gMDS3[(P[1][(P[1][b] & 255) ^ b3(i9)] & 255) ^ b3(i8)];
            }
        }
    }

    public String getAlgorithmName() {
        return "Twofish";
    }

    public int getBlockSize() {
        return 16;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.encrypting = z;
            this.workingKey = ((KeyParameter) cipherParameters).getKey();
            this.k64Cnt = this.workingKey.length / 8;
            setKey(this.workingKey);
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to Twofish init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.workingKey == null) {
            throw new IllegalStateException("Twofish not initialised");
        } else if (i + 16 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + 16 > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else if (this.encrypting) {
            encryptBlock(bArr, i, bArr2, i2);
            return 16;
        } else {
            decryptBlock(bArr, i, bArr2, i2);
            return 16;
        }
    }

    public void reset() {
        if (this.workingKey != null) {
            setKey(this.workingKey);
        }
    }
}
