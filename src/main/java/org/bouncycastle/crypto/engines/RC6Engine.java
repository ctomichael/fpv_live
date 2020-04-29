package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class RC6Engine implements BlockCipher {
    private static final int LGW = 5;
    private static final int P32 = -1209970333;
    private static final int Q32 = -1640531527;
    private static final int _noRounds = 20;
    private static final int bytesPerWord = 4;
    private static final int wordSize = 32;
    private int[] _S = null;
    private boolean forEncryption;

    private int bytesToWord(byte[] bArr, int i) {
        int i2 = 0;
        for (int i3 = 3; i3 >= 0; i3--) {
            i2 = (i2 << 8) + (bArr[i3 + i] & 255);
        }
        return i2;
    }

    private int decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int bytesToWord = bytesToWord(bArr, i);
        int bytesToWord2 = bytesToWord(bArr, i + 4);
        int bytesToWord3 = bytesToWord(bArr, i + 8);
        int bytesToWord4 = bytesToWord(bArr, i + 12);
        int i3 = bytesToWord3 - this._S[43];
        int i4 = bytesToWord - this._S[42];
        int i5 = 20;
        int i6 = bytesToWord2;
        while (i5 >= 1) {
            int rotateLeft = rotateLeft(((i4 * 2) + 1) * i4, 5);
            int rotateLeft2 = rotateLeft(((i3 * 2) + 1) * i3, 5);
            int rotateRight = rotateLeft ^ rotateRight(bytesToWord4 - this._S[i5 * 2], rotateLeft2);
            i5--;
            bytesToWord4 = i3;
            i6 = i4;
            i3 = rotateRight(i6 - this._S[(i5 * 2) + 1], rotateLeft) ^ rotateLeft2;
            i4 = rotateRight;
        }
        int i7 = bytesToWord4 - this._S[1];
        wordToBytes(i4, bArr2, i2);
        wordToBytes(i6 - this._S[0], bArr2, i2 + 4);
        wordToBytes(i3, bArr2, i2 + 8);
        wordToBytes(i7, bArr2, i2 + 12);
        return 16;
    }

    private int encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 1;
        int bytesToWord = bytesToWord(bArr, i);
        int bytesToWord2 = bytesToWord(bArr, i + 4);
        int bytesToWord3 = bytesToWord(bArr, i + 8);
        int bytesToWord4 = bytesToWord(bArr, i + 12);
        int i4 = this._S[0] + bytesToWord2;
        int i5 = this._S[1] + bytesToWord4;
        int i6 = bytesToWord3;
        int i7 = bytesToWord;
        while (i3 <= 20) {
            int rotateLeft = rotateLeft(((i4 * 2) + 1) * i4, 5);
            int rotateLeft2 = rotateLeft(((i5 * 2) + 1) * i5, 5);
            int rotateLeft3 = rotateLeft(i7 ^ rotateLeft, rotateLeft2) + this._S[i3 * 2];
            int rotateLeft4 = this._S[(i3 * 2) + 1] + rotateLeft(i6 ^ rotateLeft2, rotateLeft);
            i3++;
            i6 = i5;
            i7 = i4;
            i4 = rotateLeft4;
            i5 = rotateLeft3;
        }
        wordToBytes(this._S[42] + i7, bArr2, i2);
        wordToBytes(i4, bArr2, i2 + 4);
        wordToBytes(this._S[43] + i6, bArr2, i2 + 8);
        wordToBytes(i5, bArr2, i2 + 12);
        return 16;
    }

    private int rotateLeft(int i, int i2) {
        return (i << i2) | (i >>> (-i2));
    }

    private int rotateRight(int i, int i2) {
        return (i >>> i2) | (i << (-i2));
    }

    private void setKey(byte[] bArr) {
        if ((bArr.length + 3) / 4 == 0) {
        }
        int[] iArr = new int[(((bArr.length + 4) - 1) / 4)];
        for (int length = bArr.length - 1; length >= 0; length--) {
            iArr[length / 4] = (iArr[length / 4] << 8) + (bArr[length] & 255);
        }
        this._S = new int[44];
        this._S[0] = P32;
        for (int i = 1; i < this._S.length; i++) {
            this._S[i] = this._S[i - 1] + Q32;
        }
        int length2 = iArr.length > this._S.length ? iArr.length * 3 : this._S.length * 3;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i2 < length2) {
            int[] iArr2 = this._S;
            i6 = rotateLeft(i6 + this._S[i4] + i5, 3);
            iArr2[i4] = i6;
            i5 = rotateLeft(iArr[i3] + i6 + i5, i5 + i6);
            iArr[i3] = i5;
            i4 = (i4 + 1) % this._S.length;
            i2++;
            i3 = (i3 + 1) % iArr.length;
        }
    }

    private void wordToBytes(int i, byte[] bArr, int i2) {
        for (int i3 = 0; i3 < 4; i3++) {
            bArr[i3 + i2] = (byte) i;
            i >>>= 8;
        }
    }

    public String getAlgorithmName() {
        return "RC6";
    }

    public int getBlockSize() {
        return 16;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to RC6 init - " + cipherParameters.getClass().getName());
        }
        this.forEncryption = z;
        setKey(((KeyParameter) cipherParameters).getKey());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int blockSize = getBlockSize();
        if (this._S == null) {
            throw new IllegalStateException("RC6 engine not initialised");
        } else if (i + blockSize > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (blockSize + i2 <= bArr2.length) {
            return this.forEncryption ? encryptBlock(bArr, i, bArr2, i2) : decryptBlock(bArr, i, bArr2, i2);
        } else {
            throw new OutputLengthException("output buffer too short");
        }
    }

    public void reset() {
    }
}
