package org.bouncycastle.crypto.engines;

import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import java.lang.reflect.Array;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.util.encoders.Hex;
import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

public class ARIAEngine implements BlockCipher {
    protected static final int BLOCK_SIZE = 16;
    private static final byte[][] C = {Hex.decode("517cc1b727220a94fe13abe8fa9a6ee0"), Hex.decode("6db14acc9e21c820ff28b1d5ef5de2b0"), Hex.decode("db92371d2126e9700324977504e8c90e")};
    private static final byte[] SB1_sbox = {99, 124, 119, 123, -14, 107, 111, MessagePack.Code.BIN16, TarHeader.LF_NORMAL, 1, 103, 43, -2, MessagePack.Code.FIXEXT8, -85, 118, MessagePack.Code.FLOAT32, -126, MessagePack.Code.EXT32, 125, -6, 89, 71, -16, -83, MessagePack.Code.FIXEXT1, -94, -81, -100, -92, 114, MessagePack.Code.NIL, -73, -3, -109, 38, TarHeader.LF_FIFO, 63, -9, MessagePack.Code.UINT8, TarHeader.LF_BLK, -91, -27, -15, 113, MessagePack.Code.FIXEXT16, TarHeader.LF_LINK, 21, 4, MessagePack.Code.EXT8, 35, MessagePack.Code.TRUE, 24, -106, 5, -102, 7, 18, Byte.MIN_VALUE, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, MessagePack.Code.FIXSTR_PREFIX, 82, 59, MessagePack.Code.FIXEXT4, -77, 41, -29, 47, -124, 83, MessagePack.Code.INT16, 0, -19, 32, -4, -79, 91, 106, MessagePack.Code.FLOAT64, -66, 57, 74, 76, 88, MessagePack.Code.UINT64, MessagePack.Code.INT8, -17, -86, -5, 67, 77, TarHeader.LF_CHR, -123, 69, -7, 2, ByteCompanionObject.MAX_VALUE, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, PSSSigner.TRAILER_IMPLICIT, -74, MessagePack.Code.STR16, 33, Tnaf.POW_2_WIDTH, -1, -13, MessagePack.Code.INT32, MessagePack.Code.UINT16, 12, 19, -20, 95, -105, 68, 23, MessagePack.Code.BIN8, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, MessagePack.Code.ARRAY16, 34, 42, MessagePack.Code.FIXARRAY_PREFIX, -120, 70, -18, -72, 20, MessagePack.Code.MAP16, 94, 11, MessagePack.Code.STR32, MessagePack.Code.NEGFIXINT_PREFIX, TarHeader.LF_SYMLINK, 58, 10, 73, 6, 36, 92, MessagePack.Code.FALSE, MessagePack.Code.INT64, -84, 98, -111, -107, -28, 121, -25, MessagePack.Code.EXT16, TarHeader.LF_CONTIG, 109, -115, MessagePack.Code.FIXEXT2, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, MessagePack.Code.BIN32, -24, MessagePack.Code.ARRAY32, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, TarHeader.LF_DIR, 87, -71, -122, MessagePack.Code.NEVER_USED, 29, -98, -31, -8, -104, 17, 105, MessagePack.Code.STR8, -114, -108, -101, 30, -121, -23, MessagePack.Code.UINT32, 85, 40, MessagePack.Code.MAP32, -116, -95, -119, Draft_75.CR, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22};
    private static final byte[] SB2_sbox = {-30, 78, 84, -4, -108, MessagePack.Code.FALSE, 74, MessagePack.Code.UINT8, 98, Draft_75.CR, 106, 70, 60, 77, -117, MessagePack.Code.INT16, 94, -6, 100, MessagePack.Code.FLOAT64, -76, -105, -66, 43, PSSSigner.TRAILER_IMPLICIT, 119, 46, 3, MessagePack.Code.INT64, 25, 89, MessagePack.Code.NEVER_USED, 29, 6, 65, 107, 85, -16, -103, 105, -22, -100, 24, -82, 99, MessagePack.Code.MAP32, -25, -69, 0, 115, 102, -5, -106, 76, -123, -28, 58, 9, 69, -86, 15, -18, Tnaf.POW_2_WIDTH, -21, 45, ByteCompanionObject.MAX_VALUE, -12, 41, -84, MessagePack.Code.UINT64, -83, -111, -115, 120, MessagePack.Code.EXT16, -107, -7, 47, MessagePack.Code.UINT32, MessagePack.Code.UINT16, 8, 122, -120, 56, 92, -125, 42, 40, 71, MessagePack.Code.STR32, -72, MessagePack.Code.EXT8, -109, -92, 18, 83, -1, -121, 14, TarHeader.LF_LINK, TarHeader.LF_FIFO, 33, 88, 72, 1, -114, TarHeader.LF_CONTIG, 116, TarHeader.LF_SYMLINK, MessagePack.Code.FLOAT32, -23, -79, -73, -85, 12, MessagePack.Code.FIXEXT8, MessagePack.Code.BIN8, 86, 66, 38, 7, -104, 96, MessagePack.Code.STR8, -74, -71, 17, 64, -20, 32, -116, -67, MessagePack.Code.FIXSTR_PREFIX, MessagePack.Code.EXT32, -124, 4, 73, 35, -15, 79, 80, 31, 19, MessagePack.Code.ARRAY16, MessagePack.Code.FIXEXT16, MessagePack.Code.NIL, -98, 87, -29, MessagePack.Code.TRUE, 123, 101, 59, 2, -113, 62, -24, 37, -110, -27, 21, MessagePack.Code.ARRAY32, -3, 23, -87, -65, MessagePack.Code.FIXEXT1, -102, 126, MessagePack.Code.BIN16, 57, 103, -2, 118, -99, 67, -89, -31, MessagePack.Code.INT8, -11, 104, -14, 27, TarHeader.LF_BLK, 112, 5, -93, -118, MessagePack.Code.FIXEXT2, 121, -122, -88, TarHeader.LF_NORMAL, MessagePack.Code.BIN32, 81, 75, 30, -90, 39, -10, TarHeader.LF_DIR, MessagePack.Code.INT32, 110, 36, 22, -126, 95, MessagePack.Code.STR16, -26, 117, -94, -17, 44, -78, 28, -97, 93, 111, Byte.MIN_VALUE, 10, 114, 68, -101, 108, MessagePack.Code.FIXARRAY_PREFIX, 11, 91, TarHeader.LF_CHR, 125, 90, 82, -13, 97, -95, -9, -80, MessagePack.Code.FIXEXT4, 63, 124, 109, -19, 20, MessagePack.Code.NEGFIXINT_PREFIX, -91, 61, 34, -77, -8, -119, MessagePack.Code.MAP16, 113, 26, -81, -70, -75, -127};
    private static final byte[] SB3_sbox = {82, 9, 106, MessagePack.Code.FIXEXT2, TarHeader.LF_NORMAL, TarHeader.LF_FIFO, -91, 56, -65, 64, -93, -98, -127, -13, MessagePack.Code.FIXEXT8, -5, 124, -29, 57, -126, -101, 47, -1, -121, TarHeader.LF_BLK, -114, 67, 68, MessagePack.Code.BIN8, MessagePack.Code.MAP16, -23, MessagePack.Code.FLOAT64, 84, 123, -108, TarHeader.LF_SYMLINK, -90, MessagePack.Code.FALSE, 35, 61, -18, 76, -107, 11, 66, -6, MessagePack.Code.TRUE, 78, 8, 46, -95, 102, 40, MessagePack.Code.STR8, 36, -78, 118, 91, -94, 73, 109, -117, MessagePack.Code.INT16, 37, 114, -8, -10, 100, -122, 104, -104, 22, MessagePack.Code.FIXEXT1, -92, 92, MessagePack.Code.UINT8, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, MessagePack.Code.STR16, 94, 21, 70, 87, -89, -115, -99, -124, MessagePack.Code.FIXARRAY_PREFIX, MessagePack.Code.FIXEXT16, -85, 0, -116, PSSSigner.TRAILER_IMPLICIT, MessagePack.Code.INT64, 10, -9, -28, 88, 5, -72, -77, 69, 6, MessagePack.Code.INT8, 44, 30, -113, MessagePack.Code.FLOAT32, 63, 15, 2, MessagePack.Code.NEVER_USED, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, MessagePack.Code.ARRAY16, -22, -105, -14, MessagePack.Code.UINT64, MessagePack.Code.UINT32, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, TarHeader.LF_DIR, -123, -30, -7, TarHeader.LF_CONTIG, -24, 28, 117, MessagePack.Code.MAP32, 110, 71, -15, 26, 113, 29, 41, MessagePack.Code.BIN16, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, MessagePack.Code.BIN32, MessagePack.Code.INT32, 121, 32, -102, MessagePack.Code.STR32, MessagePack.Code.NIL, -2, 120, MessagePack.Code.UINT16, 90, -12, 31, MessagePack.Code.ARRAY32, -88, TarHeader.LF_CHR, -120, 7, MessagePack.Code.EXT8, TarHeader.LF_LINK, -79, 18, Tnaf.POW_2_WIDTH, 89, 39, Byte.MIN_VALUE, -20, 95, 96, 81, ByteCompanionObject.MAX_VALUE, -87, 25, -75, 74, Draft_75.CR, 45, -27, 122, -97, -109, MessagePack.Code.EXT32, -100, -17, MessagePack.Code.FIXSTR_PREFIX, MessagePack.Code.NEGFIXINT_PREFIX, 59, 77, -82, 42, -11, -80, MessagePack.Code.EXT16, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, MessagePack.Code.FIXEXT4, 38, -31, 105, 20, 99, 85, 33, 12, 125};
    private static final byte[] SB4_sbox = {TarHeader.LF_NORMAL, 104, -103, 27, -121, -71, 33, 120, 80, 57, MessagePack.Code.STR32, -31, 114, 9, 98, 60, 62, 126, 94, -114, -15, MessagePack.Code.FIXSTR_PREFIX, MessagePack.Code.UINT8, -93, 42, 29, -5, -74, MessagePack.Code.FIXEXT4, 32, MessagePack.Code.BIN8, -115, -127, 101, -11, -119, MessagePack.Code.FLOAT64, -99, 119, MessagePack.Code.BIN32, 87, 67, 86, 23, MessagePack.Code.FIXEXT1, 64, 26, 77, MessagePack.Code.NIL, 99, 108, -29, -73, MessagePack.Code.EXT16, 100, 106, 83, -86, 56, -104, 12, -12, -101, -19, ByteCompanionObject.MAX_VALUE, 34, 118, -81, MessagePack.Code.ARRAY32, 58, 11, 88, 103, -120, 6, MessagePack.Code.TRUE, TarHeader.LF_DIR, Draft_75.CR, 1, -117, -116, MessagePack.Code.FALSE, -26, 95, 2, 36, 117, -109, 102, 30, -27, -30, 84, MessagePack.Code.FIXEXT16, Tnaf.POW_2_WIDTH, MessagePack.Code.UINT32, 122, -24, 8, 44, 18, -105, TarHeader.LF_SYMLINK, -85, -76, 39, 10, 35, MessagePack.Code.MAP32, -17, MessagePack.Code.FLOAT32, MessagePack.Code.STR8, -72, -6, MessagePack.Code.ARRAY16, TarHeader.LF_LINK, 107, MessagePack.Code.INT16, -83, 25, 73, -67, 81, -106, -18, -28, -88, 65, MessagePack.Code.STR16, -1, MessagePack.Code.UINT16, 85, -122, TarHeader.LF_FIFO, -66, 97, 82, -8, -69, 14, -126, 72, 105, -102, MessagePack.Code.NEGFIXINT_PREFIX, 71, -98, 92, 4, 75, TarHeader.LF_BLK, 21, 121, 38, -89, MessagePack.Code.MAP16, 41, -82, -110, MessagePack.Code.FIXEXT8, -124, -23, MessagePack.Code.INT32, -70, 93, -13, MessagePack.Code.BIN16, -80, -65, -92, 59, 113, 68, 70, 43, -4, -21, 111, MessagePack.Code.FIXEXT2, -10, 20, -2, 124, 112, 90, 125, -3, 47, 24, -125, 22, -91, -111, 31, 5, -107, 116, -87, MessagePack.Code.NEVER_USED, 91, 74, -123, 109, 19, 7, 79, 78, 69, -78, 15, MessagePack.Code.EXT32, 28, -90, PSSSigner.TRAILER_IMPLICIT, -20, 115, MessagePack.Code.FIXARRAY_PREFIX, 123, MessagePack.Code.UINT64, 89, -113, -95, -7, 45, -14, -79, 0, -108, TarHeader.LF_CONTIG, -97, MessagePack.Code.INT8, 46, -100, 110, 40, 63, Byte.MIN_VALUE, -16, 61, MessagePack.Code.INT64, 37, -118, -75, -25, 66, -77, MessagePack.Code.EXT8, -22, -9, 76, 17, TarHeader.LF_CHR, 3, -94, -84, 96};
    private byte[][] roundKeys;

    protected static void A(byte[] bArr) {
        byte b = bArr[0];
        byte b2 = bArr[1];
        byte b3 = bArr[2];
        byte b4 = bArr[3];
        byte b5 = bArr[4];
        byte b6 = bArr[5];
        byte b7 = bArr[6];
        byte b8 = bArr[7];
        byte b9 = bArr[8];
        byte b10 = bArr[9];
        byte b11 = bArr[10];
        byte b12 = bArr[11];
        byte b13 = bArr[12];
        byte b14 = bArr[13];
        byte b15 = bArr[14];
        byte b16 = bArr[15];
        bArr[0] = (byte) ((((((b4 ^ b5) ^ b7) ^ b9) ^ b10) ^ b14) ^ b15);
        bArr[1] = (byte) ((((((b3 ^ b6) ^ b8) ^ b9) ^ b10) ^ b13) ^ b16);
        bArr[2] = (byte) ((((((b2 ^ b5) ^ b7) ^ b11) ^ b12) ^ b13) ^ b16);
        bArr[3] = (byte) ((((((b ^ b6) ^ b8) ^ b11) ^ b12) ^ b14) ^ b15);
        bArr[4] = (byte) ((((((b ^ b3) ^ b6) ^ b9) ^ b12) ^ b15) ^ b16);
        bArr[5] = (byte) ((((((b2 ^ b4) ^ b5) ^ b10) ^ b11) ^ b15) ^ b16);
        bArr[6] = (byte) ((((((b ^ b3) ^ b8) ^ b10) ^ b11) ^ b13) ^ b14);
        bArr[7] = (byte) ((((((b2 ^ b4) ^ b7) ^ b9) ^ b12) ^ b13) ^ b14);
        bArr[8] = (byte) ((((((b ^ b2) ^ b5) ^ b8) ^ b11) ^ b14) ^ b16);
        bArr[9] = (byte) ((((((b ^ b2) ^ b6) ^ b7) ^ b12) ^ b13) ^ b15);
        bArr[10] = (byte) ((((((b3 ^ b4) ^ b6) ^ b7) ^ b9) ^ b14) ^ b16);
        bArr[11] = (byte) ((((((b3 ^ b4) ^ b5) ^ b8) ^ b10) ^ b13) ^ b15);
        bArr[12] = (byte) (b13 ^ (((((b2 ^ b3) ^ b7) ^ b8) ^ b10) ^ b12));
        bArr[13] = (byte) (((((b7 ^ (b ^ b4)) ^ b8) ^ b9) ^ b11) ^ b14);
        bArr[14] = (byte) ((((((b ^ b4) ^ b5) ^ b6) ^ b10) ^ b12) ^ b15);
        bArr[15] = (byte) ((((((b2 ^ b3) ^ b5) ^ b6) ^ b9) ^ b11) ^ b16);
    }

    protected static void FE(byte[] bArr, byte[] bArr2) {
        xor(bArr, bArr2);
        SL2(bArr);
        A(bArr);
    }

    protected static void FO(byte[] bArr, byte[] bArr2) {
        xor(bArr, bArr2);
        SL1(bArr);
        A(bArr);
    }

    protected static byte SB1(byte b) {
        return SB1_sbox[b & 255];
    }

    protected static byte SB2(byte b) {
        return SB2_sbox[b & 255];
    }

    protected static byte SB3(byte b) {
        return SB3_sbox[b & 255];
    }

    protected static byte SB4(byte b) {
        return SB4_sbox[b & 255];
    }

    protected static void SL1(byte[] bArr) {
        bArr[0] = SB1(bArr[0]);
        bArr[1] = SB2(bArr[1]);
        bArr[2] = SB3(bArr[2]);
        bArr[3] = SB4(bArr[3]);
        bArr[4] = SB1(bArr[4]);
        bArr[5] = SB2(bArr[5]);
        bArr[6] = SB3(bArr[6]);
        bArr[7] = SB4(bArr[7]);
        bArr[8] = SB1(bArr[8]);
        bArr[9] = SB2(bArr[9]);
        bArr[10] = SB3(bArr[10]);
        bArr[11] = SB4(bArr[11]);
        bArr[12] = SB1(bArr[12]);
        bArr[13] = SB2(bArr[13]);
        bArr[14] = SB3(bArr[14]);
        bArr[15] = SB4(bArr[15]);
    }

    protected static void SL2(byte[] bArr) {
        bArr[0] = SB3(bArr[0]);
        bArr[1] = SB4(bArr[1]);
        bArr[2] = SB1(bArr[2]);
        bArr[3] = SB2(bArr[3]);
        bArr[4] = SB3(bArr[4]);
        bArr[5] = SB4(bArr[5]);
        bArr[6] = SB1(bArr[6]);
        bArr[7] = SB2(bArr[7]);
        bArr[8] = SB3(bArr[8]);
        bArr[9] = SB4(bArr[9]);
        bArr[10] = SB1(bArr[10]);
        bArr[11] = SB2(bArr[11]);
        bArr[12] = SB3(bArr[12]);
        bArr[13] = SB4(bArr[13]);
        bArr[14] = SB1(bArr[14]);
        bArr[15] = SB2(bArr[15]);
    }

    protected static byte[][] keySchedule(boolean z, byte[] bArr) {
        int length = bArr.length;
        if (length < 16 || length > 32 || (length & 7) != 0) {
            throw new IllegalArgumentException("Key length not 128/192/256 bits.");
        }
        int i = (length >>> 3) - 2;
        byte[] bArr2 = C[i];
        byte[] bArr3 = C[(i + 1) % 3];
        byte[] bArr4 = C[(i + 2) % 3];
        byte[] bArr5 = new byte[16];
        byte[] bArr6 = new byte[16];
        System.arraycopy(bArr, 0, bArr5, 0, 16);
        System.arraycopy(bArr, 16, bArr6, 0, length - 16);
        byte[] bArr7 = new byte[16];
        byte[] bArr8 = new byte[16];
        byte[] bArr9 = new byte[16];
        byte[] bArr10 = new byte[16];
        System.arraycopy(bArr5, 0, bArr7, 0, 16);
        System.arraycopy(bArr7, 0, bArr8, 0, 16);
        FO(bArr8, bArr2);
        xor(bArr8, bArr6);
        System.arraycopy(bArr8, 0, bArr9, 0, 16);
        FE(bArr9, bArr3);
        xor(bArr9, bArr7);
        System.arraycopy(bArr9, 0, bArr10, 0, 16);
        FO(bArr10, bArr4);
        xor(bArr10, bArr8);
        int i2 = (i * 2) + 12;
        byte[][] bArr11 = (byte[][]) Array.newInstance(Byte.TYPE, i2 + 1, 16);
        keyScheduleRound(bArr11[0], bArr7, bArr8, 19);
        keyScheduleRound(bArr11[1], bArr8, bArr9, 19);
        keyScheduleRound(bArr11[2], bArr9, bArr10, 19);
        keyScheduleRound(bArr11[3], bArr10, bArr7, 19);
        keyScheduleRound(bArr11[4], bArr7, bArr8, 31);
        keyScheduleRound(bArr11[5], bArr8, bArr9, 31);
        keyScheduleRound(bArr11[6], bArr9, bArr10, 31);
        keyScheduleRound(bArr11[7], bArr10, bArr7, 31);
        keyScheduleRound(bArr11[8], bArr7, bArr8, 67);
        keyScheduleRound(bArr11[9], bArr8, bArr9, 67);
        keyScheduleRound(bArr11[10], bArr9, bArr10, 67);
        keyScheduleRound(bArr11[11], bArr10, bArr7, 67);
        keyScheduleRound(bArr11[12], bArr7, bArr8, 97);
        if (i2 > 12) {
            keyScheduleRound(bArr11[13], bArr8, bArr9, 97);
            keyScheduleRound(bArr11[14], bArr9, bArr10, 97);
            if (i2 > 14) {
                keyScheduleRound(bArr11[15], bArr10, bArr7, 97);
                keyScheduleRound(bArr11[16], bArr7, bArr8, 109);
            }
        }
        if (!z) {
            reverseKeys(bArr11);
            for (int i3 = 1; i3 < i2; i3++) {
                A(bArr11[i3]);
            }
        }
        return bArr11;
    }

    protected static void keyScheduleRound(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        int i2 = i >>> 3;
        byte b = i & 7;
        int i3 = 8 - b;
        byte b2 = bArr3[15 - i2] & 255;
        int i4 = 0;
        while (i4 < 16) {
            byte b3 = bArr3[(i4 - i2) & 15] & 255;
            bArr[i4] = (byte) (((b2 << i3) | (b3 >>> b)) ^ (bArr2[i4] & 255));
            i4++;
            b2 = b3;
        }
    }

    protected static void reverseKeys(byte[][] bArr) {
        int length = bArr.length;
        int i = length / 2;
        int i2 = length - 1;
        for (int i3 = 0; i3 < i; i3++) {
            byte[] bArr2 = bArr[i3];
            bArr[i3] = bArr[i2 - i3];
            bArr[i2 - i3] = bArr2;
        }
    }

    protected static void xor(byte[] bArr, byte[] bArr2) {
        for (int i = 0; i < 16; i++) {
            bArr[i] = (byte) (bArr[i] ^ bArr2[i]);
        }
    }

    public String getAlgorithmName() {
        return "ARIA";
    }

    public int getBlockSize() {
        return 16;
    }

    public void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to ARIA init - " + cipherParameters.getClass().getName());
        }
        this.roundKeys = keySchedule(z, ((KeyParameter) cipherParameters).getKey());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) throws DataLengthException, IllegalStateException {
        if (this.roundKeys == null) {
            throw new IllegalStateException("ARIA engine not initialised");
        } else if (i > bArr.length - 16) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 > bArr2.length - 16) {
            throw new OutputLengthException("output buffer too short");
        } else {
            byte[] bArr3 = new byte[16];
            System.arraycopy(bArr, i, bArr3, 0, 16);
            int length = this.roundKeys.length - 3;
            int i3 = 0;
            while (i3 < length) {
                int i4 = i3 + 1;
                FO(bArr3, this.roundKeys[i3]);
                i3 = i4 + 1;
                FE(bArr3, this.roundKeys[i4]);
            }
            int i5 = i3 + 1;
            FO(bArr3, this.roundKeys[i3]);
            xor(bArr3, this.roundKeys[i5]);
            SL2(bArr3);
            xor(bArr3, this.roundKeys[i5 + 1]);
            System.arraycopy(bArr3, 0, bArr2, i2, 16);
            return 16;
        }
    }

    public void reset() {
    }
}
