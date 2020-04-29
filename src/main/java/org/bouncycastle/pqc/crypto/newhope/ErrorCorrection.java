package org.bouncycastle.pqc.crypto.newhope;

import dji.component.accountcenter.IMemberProtocol;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.util.Arrays;

class ErrorCorrection {
    ErrorCorrection() {
    }

    static short LDDecode(int i, int i2, int i3, int i4) {
        return (short) (((((g(i) + g(i2)) + g(i3)) + g(i4)) - 98312) >>> 31);
    }

    static int abs(int i) {
        int i2 = i >> 31;
        return (i ^ i2) - i2;
    }

    static int f(int[] iArr, int i, int i2, int i3) {
        int i4 = (i3 * 2730) >> 25;
        int i5 = i4 - ((12288 - (i3 - (i4 * 12289))) >> 31);
        iArr[i] = (i5 & 1) + (i5 >> 1);
        int i6 = i5 - 1;
        iArr[i2] = (i6 >> 1) + (i6 & 1);
        return abs(i3 - ((iArr[i] * 2) * 12289));
    }

    static int g(int i) {
        int i2 = (i * 2730) >> 27;
        int i3 = i2 - ((CipherSuite.TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA - (i - (CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA * i2))) >> 31);
        return abs((((i3 >> 1) + (i3 & 1)) * 98312) - i);
    }

    static void helpRec(short[] sArr, short[] sArr2, byte[] bArr, byte b) {
        byte[] bArr2 = new byte[8];
        bArr2[0] = b;
        byte[] bArr3 = new byte[32];
        ChaCha20.process(bArr, bArr2, bArr3, 0, bArr3.length);
        int[] iArr = new int[8];
        int[] iArr2 = new int[4];
        for (int i = 0; i < 256; i++) {
            int i2 = (bArr3[i >>> 3] >>> (i & 7)) & 1;
            int f = (24577 - (f(iArr, 3, 7, (i2 * 4) + (sArr2[i + 768] * 8)) + ((f(iArr, 0, 4, (sArr2[i + 0] * 8) + (i2 * 4)) + f(iArr, 1, 5, (sArr2[i + 256] * 8) + (i2 * 4))) + f(iArr, 2, 6, (sArr2[i + 512] * 8) + (i2 * 4))))) >> 31;
            iArr2[0] = ((f ^ -1) & iArr[0]) ^ (iArr[4] & f);
            iArr2[1] = ((f ^ -1) & iArr[1]) ^ (iArr[5] & f);
            iArr2[2] = ((f ^ -1) & iArr[2]) ^ (iArr[6] & f);
            iArr2[3] = ((f ^ -1) & iArr[3]) ^ (iArr[7] & f);
            sArr[i + 0] = (short) ((iArr2[0] - iArr2[3]) & 3);
            sArr[i + 256] = (short) ((iArr2[1] - iArr2[3]) & 3);
            sArr[i + 512] = (short) ((iArr2[2] - iArr2[3]) & 3);
            sArr[i + 768] = (short) (((-f) + (iArr2[3] * 2)) & 3);
        }
    }

    static void rec(byte[] bArr, short[] sArr, short[] sArr2) {
        Arrays.fill(bArr, (byte) 0);
        int[] iArr = new int[4];
        for (int i = 0; i < 256; i++) {
            iArr[0] = ((sArr[i + 0] * 8) + IMemberProtocol.CMDID_ACCOUNT_SIGNUP) - (((sArr2[i + 0] * 2) + sArr2[i + 768]) * 12289);
            iArr[1] = ((sArr[i + 256] * 8) + IMemberProtocol.CMDID_ACCOUNT_SIGNUP) - (((sArr2[i + 256] * 2) + sArr2[i + 768]) * 12289);
            iArr[2] = ((sArr[i + 512] * 8) + IMemberProtocol.CMDID_ACCOUNT_SIGNUP) - (((sArr2[i + 512] * 2) + sArr2[i + 768]) * 12289);
            iArr[3] = ((sArr[i + 768] * 8) + IMemberProtocol.CMDID_ACCOUNT_SIGNUP) - (sArr2[i + 768] * 12289);
            int i2 = i >>> 3;
            bArr[i2] = (byte) (bArr[i2] | (LDDecode(iArr[0], iArr[1], iArr[2], iArr[3]) << (i & 7)));
        }
    }
}
