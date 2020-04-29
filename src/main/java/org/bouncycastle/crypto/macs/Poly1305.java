package org.bouncycastle.crypto.macs;

import org.bouncycastle.asn1.cmc.BodyPartID;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Pack;

public class Poly1305 implements Mac {
    private static final int BLOCK_SIZE = 16;
    private final BlockCipher cipher;
    private final byte[] currentBlock;
    private int currentBlockOffset;
    private int h0;
    private int h1;
    private int h2;
    private int h3;
    private int h4;
    private int k0;
    private int k1;
    private int k2;
    private int k3;
    private int r0;
    private int r1;
    private int r2;
    private int r3;
    private int r4;
    private int s1;
    private int s2;
    private int s3;
    private int s4;
    private final byte[] singleByte;

    public Poly1305() {
        this.singleByte = new byte[1];
        this.currentBlock = new byte[16];
        this.currentBlockOffset = 0;
        this.cipher = null;
    }

    public Poly1305(BlockCipher blockCipher) {
        this.singleByte = new byte[1];
        this.currentBlock = new byte[16];
        this.currentBlockOffset = 0;
        if (blockCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("Poly1305 requires a 128 bit block cipher.");
        }
        this.cipher = blockCipher;
    }

    private static final long mul32x32_64(int i, int i2) {
        return (((long) i) & BodyPartID.bodyIdMax) * ((long) i2);
    }

    private void processBlock() {
        if (this.currentBlockOffset < 16) {
            this.currentBlock[this.currentBlockOffset] = 1;
            int i = this.currentBlockOffset;
            while (true) {
                i++;
                if (i >= 16) {
                    break;
                }
                this.currentBlock[i] = 0;
            }
        }
        long littleEndianToInt = BodyPartID.bodyIdMax & ((long) Pack.littleEndianToInt(this.currentBlock, 0));
        long littleEndianToInt2 = BodyPartID.bodyIdMax & ((long) Pack.littleEndianToInt(this.currentBlock, 4));
        long littleEndianToInt3 = BodyPartID.bodyIdMax & ((long) Pack.littleEndianToInt(this.currentBlock, 8));
        long littleEndianToInt4 = BodyPartID.bodyIdMax & ((long) Pack.littleEndianToInt(this.currentBlock, 12));
        this.h0 = (int) (((long) this.h0) + (67108863 & littleEndianToInt));
        this.h1 = (int) ((((littleEndianToInt | (littleEndianToInt2 << 32)) >>> 26) & 67108863) + ((long) this.h1));
        this.h2 = (int) (((long) this.h2) + (((littleEndianToInt2 | (littleEndianToInt3 << 32)) >>> 20) & 67108863));
        this.h3 = (int) (((long) this.h3) + ((((littleEndianToInt4 << 32) | littleEndianToInt3) >>> 14) & 67108863));
        this.h4 = (int) (((long) this.h4) + (littleEndianToInt4 >>> 8));
        if (this.currentBlockOffset == 16) {
            this.h4 += 16777216;
        }
        long mul32x32_64 = mul32x32_64(this.h0, this.r0) + mul32x32_64(this.h1, this.s4) + mul32x32_64(this.h2, this.s3) + mul32x32_64(this.h3, this.s2) + mul32x32_64(this.h4, this.s1);
        long mul32x32_642 = mul32x32_64(this.h0, this.r1) + mul32x32_64(this.h1, this.r0) + mul32x32_64(this.h2, this.s4) + mul32x32_64(this.h3, this.s3) + mul32x32_64(this.h4, this.s2);
        long mul32x32_643 = mul32x32_64(this.h0, this.r2) + mul32x32_64(this.h1, this.r1) + mul32x32_64(this.h2, this.r0) + mul32x32_64(this.h3, this.s4) + mul32x32_64(this.h4, this.s3);
        long mul32x32_644 = mul32x32_64(this.h0, this.r3) + mul32x32_64(this.h1, this.r2) + mul32x32_64(this.h2, this.r1) + mul32x32_64(this.h3, this.r0) + mul32x32_64(this.h4, this.s4);
        long mul32x32_645 = mul32x32_64(this.h0, this.r4) + mul32x32_64(this.h1, this.r3) + mul32x32_64(this.h2, this.r2) + mul32x32_64(this.h3, this.r1) + mul32x32_64(this.h4, this.r0);
        this.h0 = ((int) mul32x32_64) & 67108863;
        long j = (mul32x32_64 >>> 26) + mul32x32_642;
        this.h1 = ((int) j) & 67108863;
        long j2 = (j >>> 26) + mul32x32_643;
        this.h2 = ((int) j2) & 67108863;
        long j3 = (j2 >>> 26) + mul32x32_644;
        this.h3 = ((int) j3) & 67108863;
        long j4 = (j3 >>> 26) + mul32x32_645;
        this.h4 = ((int) j4) & 67108863;
        this.h0 = (((int) (j4 >>> 26)) * 5) + this.h0;
        this.h1 += this.h0 >>> 26;
        this.h0 &= 67108863;
    }

    private void setKey(byte[] bArr, byte[] bArr2) {
        int i = 16;
        if (bArr.length != 32) {
            throw new IllegalArgumentException("Poly1305 key must be 256 bits.");
        } else if (this.cipher == null || (bArr2 != null && bArr2.length == 16)) {
            int littleEndianToInt = Pack.littleEndianToInt(bArr, 0);
            int littleEndianToInt2 = Pack.littleEndianToInt(bArr, 4);
            int littleEndianToInt3 = Pack.littleEndianToInt(bArr, 8);
            int littleEndianToInt4 = Pack.littleEndianToInt(bArr, 12);
            this.r0 = 67108863 & littleEndianToInt;
            this.r1 = ((littleEndianToInt >>> 26) | (littleEndianToInt2 << 6)) & 67108611;
            this.r2 = ((littleEndianToInt2 >>> 20) | (littleEndianToInt3 << 12)) & 67092735;
            this.r3 = ((littleEndianToInt3 >>> 14) | (littleEndianToInt4 << 18)) & 66076671;
            this.r4 = (littleEndianToInt4 >>> 8) & 1048575;
            this.s1 = this.r1 * 5;
            this.s2 = this.r2 * 5;
            this.s3 = this.r3 * 5;
            this.s4 = this.r4 * 5;
            if (this.cipher != null) {
                byte[] bArr3 = new byte[16];
                this.cipher.init(true, new KeyParameter(bArr, 16, 16));
                this.cipher.processBlock(bArr2, 0, bArr3, 0);
                i = 0;
                bArr = bArr3;
            }
            this.k0 = Pack.littleEndianToInt(bArr, i + 0);
            this.k1 = Pack.littleEndianToInt(bArr, i + 4);
            this.k2 = Pack.littleEndianToInt(bArr, i + 8);
            this.k3 = Pack.littleEndianToInt(bArr, i + 12);
        } else {
            throw new IllegalArgumentException("Poly1305 requires a 128 bit IV.");
        }
    }

    public int doFinal(byte[] bArr, int i) throws DataLengthException, IllegalStateException {
        if (i + 16 > bArr.length) {
            throw new DataLengthException("Output buffer is too short.");
        }
        if (this.currentBlockOffset > 0) {
            processBlock();
        }
        this.h1 += this.h0 >>> 26;
        this.h0 &= 67108863;
        this.h2 += this.h1 >>> 26;
        this.h1 &= 67108863;
        this.h3 += this.h2 >>> 26;
        this.h2 &= 67108863;
        this.h4 += this.h3 >>> 26;
        this.h3 &= 67108863;
        this.h0 += (this.h4 >>> 26) * 5;
        this.h4 &= 67108863;
        this.h1 += this.h0 >>> 26;
        this.h0 &= 67108863;
        int i2 = this.h0 + 5;
        int i3 = (i2 >>> 26) + this.h1;
        int i4 = (i3 >>> 26) + this.h2;
        int i5 = (i4 >>> 26) + this.h3;
        int i6 = ((i5 >>> 26) + this.h4) - 67108864;
        int i7 = (i6 >>> 31) - 1;
        int i8 = i7 ^ -1;
        this.h0 = (i2 & 67108863 & i7) | (this.h0 & i8);
        this.h1 = (this.h1 & i8) | (i3 & 67108863 & i7);
        this.h2 = (this.h2 & i8) | (i4 & 67108863 & i7);
        this.h3 = (this.h3 & i8) | (i5 & 67108863 & i7);
        this.h4 = (this.h4 & i8) | (i6 & i7);
        long j = (((long) (this.h0 | (this.h1 << 26))) & BodyPartID.bodyIdMax) + (((long) this.k0) & BodyPartID.bodyIdMax);
        long j2 = (((long) ((this.h1 >>> 6) | (this.h2 << 20))) & BodyPartID.bodyIdMax) + (((long) this.k1) & BodyPartID.bodyIdMax);
        long j3 = (((long) ((this.h2 >>> 12) | (this.h3 << 14))) & BodyPartID.bodyIdMax) + (((long) this.k2) & BodyPartID.bodyIdMax);
        long j4 = (((long) ((this.h3 >>> 18) | (this.h4 << 8))) & BodyPartID.bodyIdMax) + (((long) this.k3) & BodyPartID.bodyIdMax);
        Pack.intToLittleEndian((int) j, bArr, i);
        long j5 = (j >>> 32) + j2;
        Pack.intToLittleEndian((int) j5, bArr, i + 4);
        long j6 = (j5 >>> 32) + j3;
        Pack.intToLittleEndian((int) j6, bArr, i + 8);
        Pack.intToLittleEndian((int) ((j6 >>> 32) + j4), bArr, i + 12);
        reset();
        return 16;
    }

    public String getAlgorithmName() {
        return this.cipher == null ? "Poly1305" : "Poly1305-" + this.cipher.getAlgorithmName();
    }

    public int getMacSize() {
        return 16;
    }

    public void init(CipherParameters cipherParameters) throws IllegalArgumentException {
        CipherParameters cipherParameters2;
        byte[] bArr = null;
        if (this.cipher == null) {
            cipherParameters2 = cipherParameters;
        } else if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("Poly1305 requires an IV when used with a block cipher.");
        } else {
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            bArr = parametersWithIV.getIV();
            cipherParameters2 = parametersWithIV.getParameters();
        }
        if (!(cipherParameters2 instanceof KeyParameter)) {
            throw new IllegalArgumentException("Poly1305 requires a key.");
        }
        setKey(((KeyParameter) cipherParameters2).getKey(), bArr);
        reset();
    }

    public void reset() {
        this.currentBlockOffset = 0;
        this.h4 = 0;
        this.h3 = 0;
        this.h2 = 0;
        this.h1 = 0;
        this.h0 = 0;
    }

    public void update(byte b) throws IllegalStateException {
        this.singleByte[0] = b;
        update(this.singleByte, 0, 1);
    }

    public void update(byte[] bArr, int i, int i2) throws DataLengthException, IllegalStateException {
        int i3 = 0;
        while (i2 > i3) {
            if (this.currentBlockOffset == 16) {
                processBlock();
                this.currentBlockOffset = 0;
            }
            int min = Math.min(i2 - i3, 16 - this.currentBlockOffset);
            System.arraycopy(bArr, i3 + i, this.currentBlock, this.currentBlockOffset, min);
            i3 += min;
            this.currentBlockOffset = min + this.currentBlockOffset;
        }
    }
}
