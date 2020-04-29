package org.bouncycastle.crypto.digests;

import com.adobe.xmp.XMPError;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import dji.pilot.publics.model.ICameraResMode;
import it.sauronsoftware.ftp4j.FTPCodes;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Memoable;

public final class WhirlpoolDigest implements ExtendedDigest, Memoable {
    private static final int BITCOUNT_ARRAY_SIZE = 32;
    private static final int BYTE_LENGTH = 64;
    private static final long[] C0 = new long[256];
    private static final long[] C1 = new long[256];
    private static final long[] C2 = new long[256];
    private static final long[] C3 = new long[256];
    private static final long[] C4 = new long[256];
    private static final long[] C5 = new long[256];
    private static final long[] C6 = new long[256];
    private static final long[] C7 = new long[256];
    private static final int DIGEST_LENGTH_BYTES = 64;
    private static final short[] EIGHT = new short[32];
    private static final int REDUCTION_POLYNOMIAL = 285;
    private static final int ROUNDS = 10;
    private static final int[] SBOX = {24, 35, 198, 232, 135, 184, 1, 79, 54, 166, 210, 245, PanasonicMakernoteDirectory.TAG_INTELLIGENT_D_RANGE, 111, 145, 82, 96, 188, 155, 142, 163, 12, 123, 53, 29, 224, FTPCodes.NAME_SYSTEM_TIME, CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256, 46, 75, 254, 87, 21, 119, 55, 229, 159, 240, 74, 218, 88, XMPError.BADXML, 41, 10, 177, 160, 107, 133, 189, 93, 16, 244, XMPError.BADXMP, 62, 5, 103, 228, 39, 65, 139, 167, FTPCodes.DATA_CONNECTION_ALREADY_OPEN, 149, 216, 251, 238, PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH, 102, FTPCodes.SERVICE_CLOSING_CONTROL_CONNECTION, 23, 71, 158, 202, 45, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256, 7, 173, 90, 131, 51, 99, 2, 170, 113, 200, 25, 73, 217, 242, FTPCodes.ENTER_PASSIVE_MODE, 91, 136, 154, 38, 50, 176, 233, 15, FTPCodes.FILE_STATUS, 128, CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256, 205, 52, 72, 255, 122, 144, 95, 32, 104, 26, 174, 180, 84, 147, 34, 100, 241, 115, 18, 64, 8, CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256, 236, 219, 161, 141, 61, 151, 0, 207, 43, 118, NikonType2MakernoteDirectory.TAG_ADAPTER, FTPCodes.HELP_MESSAGE, 27, 181, 175, 106, 80, 69, 243, 48, 239, 63, 85, 162, 234, 101, CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256, 47, 192, 222, 28, ICameraResMode.ICameraVideoResolutionRes.VR_MAX, 77, 146, 117, 6, 138, 178, FTPCodes.USER_LOGGED_IN, 14, 31, 98, FTPCodes.DIRECTORY_STATUS, 168, 150, 249, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256, 37, 89, 132, 114, 57, 76, 94, 120, 56, 140, 209, 165, FTPCodes.DATA_CONNECTION_CLOSING, 97, 179, 33, 156, 30, 67, 199, 252, 4, 81, 153, 109, 13, 250, 223, 126, 36, 59, 171, 206, 17, 143, 78, 183, 235, 60, 129, 148, 247, 185, 19, 44, 211, 231, 110, CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256, 3, 86, 68, CertificateBody.profileType, 169, 42, 187, CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256, 83, FTPCodes.SERVICE_READY_FOR_NEW_USER, 11, 157, 108, 49, 116, 246, 70, 172, 137, 20, FTPCodes.DATA_CONNECTION_OPEN, 22, 58, 105, 9, 112, 182, CanonMakernoteDirectory.TAG_VRD_OFFSET, 237, XMPError.BADSTREAM, 66, 152, 164, 40, 92, 248, 134};
    private long[] _K = new long[8];
    private long[] _L = new long[8];
    private short[] _bitCount = new short[32];
    private long[] _block = new long[8];
    private byte[] _buffer = new byte[64];
    private int _bufferPos = 0;
    private long[] _hash = new long[8];
    private final long[] _rc = new long[11];
    private long[] _state = new long[8];

    static {
        EIGHT[31] = 8;
    }

    public WhirlpoolDigest() {
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 256) {
                break;
            }
            int i3 = SBOX[i2];
            int maskWithReductionPolynomial = maskWithReductionPolynomial(i3 << 1);
            int maskWithReductionPolynomial2 = maskWithReductionPolynomial(maskWithReductionPolynomial << 1);
            int i4 = maskWithReductionPolynomial2 ^ i3;
            int maskWithReductionPolynomial3 = maskWithReductionPolynomial(maskWithReductionPolynomial2 << 1);
            int i5 = maskWithReductionPolynomial3 ^ i3;
            C0[i2] = packIntoLong(i3, i3, maskWithReductionPolynomial2, i3, maskWithReductionPolynomial3, i4, maskWithReductionPolynomial, i5);
            C1[i2] = packIntoLong(i5, i3, i3, maskWithReductionPolynomial2, i3, maskWithReductionPolynomial3, i4, maskWithReductionPolynomial);
            C2[i2] = packIntoLong(maskWithReductionPolynomial, i5, i3, i3, maskWithReductionPolynomial2, i3, maskWithReductionPolynomial3, i4);
            C3[i2] = packIntoLong(i4, maskWithReductionPolynomial, i5, i3, i3, maskWithReductionPolynomial2, i3, maskWithReductionPolynomial3);
            C4[i2] = packIntoLong(maskWithReductionPolynomial3, i4, maskWithReductionPolynomial, i5, i3, i3, maskWithReductionPolynomial2, i3);
            C5[i2] = packIntoLong(i3, maskWithReductionPolynomial3, i4, maskWithReductionPolynomial, i5, i3, i3, maskWithReductionPolynomial2);
            C6[i2] = packIntoLong(maskWithReductionPolynomial2, i3, maskWithReductionPolynomial3, i4, maskWithReductionPolynomial, i5, i3, i3);
            C7[i2] = packIntoLong(i3, maskWithReductionPolynomial2, i3, maskWithReductionPolynomial3, i4, maskWithReductionPolynomial, i5, i3);
            i = i2 + 1;
        }
        this._rc[0] = 0;
        for (int i6 = 1; i6 <= 10; i6++) {
            int i7 = (i6 - 1) * 8;
            this._rc[i6] = (((((((C0[i7] & -72057594037927936L) ^ (C1[i7 + 1] & 71776119061217280L)) ^ (C2[i7 + 2] & 280375465082880L)) ^ (C3[i7 + 3] & 1095216660480L)) ^ (C4[i7 + 4] & 4278190080L)) ^ (C5[i7 + 5] & 16711680)) ^ (C6[i7 + 6] & 65280)) ^ (C7[i7 + 7] & 255);
        }
    }

    public WhirlpoolDigest(WhirlpoolDigest whirlpoolDigest) {
        reset(whirlpoolDigest);
    }

    private long bytesToLongFromBuffer(byte[] bArr, int i) {
        return ((((long) bArr[i + 0]) & 255) << 56) | ((((long) bArr[i + 1]) & 255) << 48) | ((((long) bArr[i + 2]) & 255) << 40) | ((((long) bArr[i + 3]) & 255) << 32) | ((((long) bArr[i + 4]) & 255) << 24) | ((((long) bArr[i + 5]) & 255) << 16) | ((((long) bArr[i + 6]) & 255) << 8) | (((long) bArr[i + 7]) & 255);
    }

    private void convertLongToByteArray(long j, byte[] bArr, int i) {
        for (int i2 = 0; i2 < 8; i2++) {
            bArr[i + i2] = (byte) ((int) ((j >> (56 - (i2 * 8))) & 255));
        }
    }

    private byte[] copyBitLength() {
        byte[] bArr = new byte[32];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (this._bitCount[i] & 255);
        }
        return bArr;
    }

    private void finish() {
        byte[] copyBitLength = copyBitLength();
        byte[] bArr = this._buffer;
        int i = this._bufferPos;
        this._bufferPos = i + 1;
        bArr[i] = (byte) (bArr[i] | 128);
        if (this._bufferPos == this._buffer.length) {
            processFilledBuffer(this._buffer, 0);
        }
        if (this._bufferPos > 32) {
            while (this._bufferPos != 0) {
                update((byte) 0);
            }
        }
        while (this._bufferPos <= 32) {
            update((byte) 0);
        }
        System.arraycopy(copyBitLength, 0, this._buffer, 32, copyBitLength.length);
        processFilledBuffer(this._buffer, 0);
    }

    private void increment() {
        int i = 0;
        for (int length = this._bitCount.length - 1; length >= 0; length--) {
            int i2 = (this._bitCount[length] & 255) + EIGHT[length] + i;
            i = i2 >>> 8;
            this._bitCount[length] = (short) (i2 & 255);
        }
    }

    private int maskWithReductionPolynomial(int i) {
        return ((long) i) >= 256 ? i ^ 285 : i;
    }

    private long packIntoLong(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        return (((((((((long) i) << 56) ^ (((long) i2) << 48)) ^ (((long) i3) << 40)) ^ (((long) i4) << 32)) ^ (((long) i5) << 24)) ^ (((long) i6) << 16)) ^ (((long) i7) << 8)) ^ ((long) i8);
    }

    private void processFilledBuffer(byte[] bArr, int i) {
        for (int i2 = 0; i2 < this._state.length; i2++) {
            this._block[i2] = bytesToLongFromBuffer(this._buffer, i2 * 8);
        }
        processBlock();
        this._bufferPos = 0;
        Arrays.fill(this._buffer, (byte) 0);
    }

    public Memoable copy() {
        return new WhirlpoolDigest(this);
    }

    public int doFinal(byte[] bArr, int i) {
        finish();
        for (int i2 = 0; i2 < 8; i2++) {
            convertLongToByteArray(this._hash[i2], bArr, (i2 * 8) + i);
        }
        reset();
        return getDigestSize();
    }

    public String getAlgorithmName() {
        return "Whirlpool";
    }

    public int getByteLength() {
        return 64;
    }

    public int getDigestSize() {
        return 64;
    }

    /* access modifiers changed from: protected */
    public void processBlock() {
        for (int i = 0; i < 8; i++) {
            long[] jArr = this._state;
            long j = this._block[i];
            long[] jArr2 = this._K;
            long j2 = this._hash[i];
            jArr2[i] = j2;
            jArr[i] = j ^ j2;
        }
        int i2 = 1;
        while (true) {
            int i3 = i2;
            if (i3 > 10) {
                break;
            }
            for (int i4 = 0; i4 < 8; i4++) {
                this._L[i4] = 0;
                long[] jArr3 = this._L;
                jArr3[i4] = jArr3[i4] ^ C0[((int) (this._K[(i4 + 0) & 7] >>> 56)) & 255];
                long[] jArr4 = this._L;
                jArr4[i4] = jArr4[i4] ^ C1[((int) (this._K[(i4 - 1) & 7] >>> 48)) & 255];
                long[] jArr5 = this._L;
                jArr5[i4] = jArr5[i4] ^ C2[((int) (this._K[(i4 - 2) & 7] >>> 40)) & 255];
                long[] jArr6 = this._L;
                jArr6[i4] = jArr6[i4] ^ C3[((int) (this._K[(i4 - 3) & 7] >>> 32)) & 255];
                long[] jArr7 = this._L;
                jArr7[i4] = jArr7[i4] ^ C4[((int) (this._K[(i4 - 4) & 7] >>> 24)) & 255];
                long[] jArr8 = this._L;
                jArr8[i4] = jArr8[i4] ^ C5[((int) (this._K[(i4 - 5) & 7] >>> 16)) & 255];
                long[] jArr9 = this._L;
                jArr9[i4] = jArr9[i4] ^ C6[((int) (this._K[(i4 - 6) & 7] >>> 8)) & 255];
                long[] jArr10 = this._L;
                jArr10[i4] = jArr10[i4] ^ C7[((int) this._K[(i4 - 7) & 7]) & 255];
            }
            System.arraycopy(this._L, 0, this._K, 0, this._K.length);
            long[] jArr11 = this._K;
            jArr11[0] = jArr11[0] ^ this._rc[i3];
            for (int i5 = 0; i5 < 8; i5++) {
                this._L[i5] = this._K[i5];
                long[] jArr12 = this._L;
                jArr12[i5] = jArr12[i5] ^ C0[((int) (this._state[(i5 + 0) & 7] >>> 56)) & 255];
                long[] jArr13 = this._L;
                jArr13[i5] = jArr13[i5] ^ C1[((int) (this._state[(i5 - 1) & 7] >>> 48)) & 255];
                long[] jArr14 = this._L;
                jArr14[i5] = jArr14[i5] ^ C2[((int) (this._state[(i5 - 2) & 7] >>> 40)) & 255];
                long[] jArr15 = this._L;
                jArr15[i5] = jArr15[i5] ^ C3[((int) (this._state[(i5 - 3) & 7] >>> 32)) & 255];
                long[] jArr16 = this._L;
                jArr16[i5] = jArr16[i5] ^ C4[((int) (this._state[(i5 - 4) & 7] >>> 24)) & 255];
                long[] jArr17 = this._L;
                jArr17[i5] = jArr17[i5] ^ C5[((int) (this._state[(i5 - 5) & 7] >>> 16)) & 255];
                long[] jArr18 = this._L;
                jArr18[i5] = jArr18[i5] ^ C6[((int) (this._state[(i5 - 6) & 7] >>> 8)) & 255];
                long[] jArr19 = this._L;
                jArr19[i5] = jArr19[i5] ^ C7[((int) this._state[(i5 - 7) & 7]) & 255];
            }
            System.arraycopy(this._L, 0, this._state, 0, this._state.length);
            i2 = i3 + 1;
        }
        for (int i6 = 0; i6 < 8; i6++) {
            long[] jArr20 = this._hash;
            jArr20[i6] = jArr20[i6] ^ (this._state[i6] ^ this._block[i6]);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.util.Arrays.fill(short[], short):void
     arg types: [short[], int]
     candidates:
      org.bouncycastle.util.Arrays.fill(byte[], byte):void
      org.bouncycastle.util.Arrays.fill(char[], char):void
      org.bouncycastle.util.Arrays.fill(int[], int):void
      org.bouncycastle.util.Arrays.fill(long[], long):void
      org.bouncycastle.util.Arrays.fill(short[], short):void */
    public void reset() {
        this._bufferPos = 0;
        Arrays.fill(this._bitCount, (short) 0);
        Arrays.fill(this._buffer, (byte) 0);
        Arrays.fill(this._hash, 0);
        Arrays.fill(this._K, 0);
        Arrays.fill(this._L, 0);
        Arrays.fill(this._block, 0);
        Arrays.fill(this._state, 0);
    }

    public void reset(Memoable memoable) {
        WhirlpoolDigest whirlpoolDigest = (WhirlpoolDigest) memoable;
        System.arraycopy(whirlpoolDigest._rc, 0, this._rc, 0, this._rc.length);
        System.arraycopy(whirlpoolDigest._buffer, 0, this._buffer, 0, this._buffer.length);
        this._bufferPos = whirlpoolDigest._bufferPos;
        System.arraycopy(whirlpoolDigest._bitCount, 0, this._bitCount, 0, this._bitCount.length);
        System.arraycopy(whirlpoolDigest._hash, 0, this._hash, 0, this._hash.length);
        System.arraycopy(whirlpoolDigest._K, 0, this._K, 0, this._K.length);
        System.arraycopy(whirlpoolDigest._L, 0, this._L, 0, this._L.length);
        System.arraycopy(whirlpoolDigest._block, 0, this._block, 0, this._block.length);
        System.arraycopy(whirlpoolDigest._state, 0, this._state, 0, this._state.length);
    }

    public void update(byte b) {
        this._buffer[this._bufferPos] = b;
        this._bufferPos++;
        if (this._bufferPos == this._buffer.length) {
            processFilledBuffer(this._buffer, 0);
        }
        increment();
    }

    public void update(byte[] bArr, int i, int i2) {
        while (i2 > 0) {
            update(bArr[i]);
            i++;
            i2--;
        }
    }
}
