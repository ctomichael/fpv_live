package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.DSAParameterGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAValidationParameters;
import org.bouncycastle.crypto.util.DigestFactory;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.encoders.Hex;

public class DSAParametersGenerator {
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger ZERO = BigInteger.valueOf(0);
    private int L;
    private int N;
    private int certainty;
    private Digest digest;
    private int iterations;
    private SecureRandom random;
    private int usageIndex;
    private boolean use186_3;

    public DSAParametersGenerator() {
        this(DigestFactory.createSHA1());
    }

    public DSAParametersGenerator(Digest digest2) {
        this.digest = digest2;
    }

    private static BigInteger calculateGenerator_FIPS186_2(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        BigInteger modPow;
        BigInteger divide = bigInteger.subtract(ONE).divide(bigInteger2);
        BigInteger subtract = bigInteger.subtract(TWO);
        do {
            modPow = BigIntegers.createRandomInRange(TWO, subtract, secureRandom).modPow(divide, bigInteger);
        } while (modPow.bitLength() <= 1);
        return modPow;
    }

    private static BigInteger calculateGenerator_FIPS186_3_Unverifiable(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        return calculateGenerator_FIPS186_2(bigInteger, bigInteger2, secureRandom);
    }

    private static BigInteger calculateGenerator_FIPS186_3_Verifiable(Digest digest2, BigInteger bigInteger, BigInteger bigInteger2, byte[] bArr, int i) {
        BigInteger divide = bigInteger.subtract(ONE).divide(bigInteger2);
        byte[] decode = Hex.decode("6767656E");
        byte[] bArr2 = new byte[(bArr.length + decode.length + 1 + 2)];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        System.arraycopy(decode, 0, bArr2, bArr.length, decode.length);
        bArr2[bArr2.length - 3] = (byte) i;
        byte[] bArr3 = new byte[digest2.getDigestSize()];
        for (int i2 = 1; i2 < 65536; i2++) {
            inc(bArr2);
            hash(digest2, bArr2, bArr3, 0);
            BigInteger modPow = new BigInteger(1, bArr3).modPow(divide, bigInteger);
            if (modPow.compareTo(TWO) >= 0) {
                return modPow;
            }
        }
        return null;
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:58)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: CFG modification limit reached, blocks count: 125 */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x005c A[LOOP:2: B:7:0x0059->B:9:0x005c, LOOP_END] */
    private org.bouncycastle.crypto.params.DSAParameters generateParameters_FIPS186_2() {
        /*
            r15 = this;
            r0 = 20
            r2 = 1
            r1 = 0
            byte[] r4 = new byte[r0]
            byte[] r5 = new byte[r0]
            byte[] r6 = new byte[r0]
            byte[] r7 = new byte[r0]
            int r0 = r15.L
            int r0 = r0 + -1
            int r8 = r0 / 160
            int r0 = r15.L
            int r0 = r0 / 8
            byte[] r9 = new byte[r0]
            org.bouncycastle.crypto.Digest r0 = r15.digest
            boolean r0 = r0 instanceof org.bouncycastle.crypto.digests.SHA1Digest
            if (r0 != 0) goto L_0x0042
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "can only use SHA-1 for generating FIPS 186-2 parameters"
            r0.<init>(r1)
            throw r0
        L_0x0027:
            byte r0 = r7[r1]
            r0 = r0 | -128(0xffffffffffffff80, float:NaN)
            byte r0 = (byte) r0
            r7[r1] = r0
            r0 = 19
            byte r3 = r7[r0]
            r3 = r3 | 1
            byte r3 = (byte) r3
            r7[r0] = r3
            java.math.BigInteger r10 = new java.math.BigInteger
            r10.<init>(r2, r7)
            boolean r0 = r15.isProbablePrime(r10)
            if (r0 != 0) goto L_0x0067
        L_0x0042:
            java.security.SecureRandom r0 = r15.random
            r0.nextBytes(r4)
            org.bouncycastle.crypto.Digest r0 = r15.digest
            hash(r0, r4, r5, r1)
            int r0 = r4.length
            java.lang.System.arraycopy(r4, r1, r6, r1, r0)
            inc(r6)
            org.bouncycastle.crypto.Digest r0 = r15.digest
            hash(r0, r6, r6, r1)
            r0 = r1
        L_0x0059:
            int r3 = r7.length
            if (r0 == r3) goto L_0x0027
            byte r3 = r5[r0]
            byte r10 = r6[r0]
            r3 = r3 ^ r10
            byte r3 = (byte) r3
            r7[r0] = r3
            int r0 = r0 + 1
            goto L_0x0059
        L_0x0067:
            byte[] r11 = org.bouncycastle.util.Arrays.clone(r4)
            inc(r11)
            r3 = r1
        L_0x006f:
            r0 = 4096(0x1000, float:5.74E-42)
            if (r3 >= r0) goto L_0x0042
            r0 = r2
        L_0x0074:
            if (r0 > r8) goto L_0x0085
            inc(r11)
            org.bouncycastle.crypto.Digest r12 = r15.digest
            int r13 = r9.length
            int r14 = r5.length
            int r14 = r14 * r0
            int r13 = r13 - r14
            hash(r12, r11, r9, r13)
            int r0 = r0 + 1
            goto L_0x0074
        L_0x0085:
            int r0 = r9.length
            int r12 = r5.length
            int r12 = r12 * r8
            int r0 = r0 - r12
            inc(r11)
            org.bouncycastle.crypto.Digest r12 = r15.digest
            hash(r12, r11, r5, r1)
            int r12 = r5.length
            int r12 = r12 - r0
            java.lang.System.arraycopy(r5, r12, r9, r1, r0)
            byte r0 = r9[r1]
            r0 = r0 | -128(0xffffffffffffff80, float:NaN)
            byte r0 = (byte) r0
            r9[r1] = r0
            java.math.BigInteger r0 = new java.math.BigInteger
            r0.<init>(r2, r9)
            java.math.BigInteger r12 = r10.shiftLeft(r2)
            java.math.BigInteger r12 = r0.mod(r12)
            java.math.BigInteger r13 = org.bouncycastle.crypto.generators.DSAParametersGenerator.ONE
            java.math.BigInteger r12 = r12.subtract(r13)
            java.math.BigInteger r0 = r0.subtract(r12)
            int r12 = r0.bitLength()
            int r13 = r15.L
            if (r12 == r13) goto L_0x00c0
        L_0x00bc:
            int r0 = r3 + 1
            r3 = r0
            goto L_0x006f
        L_0x00c0:
            boolean r12 = r15.isProbablePrime(r0)
            if (r12 == 0) goto L_0x00bc
            java.security.SecureRandom r1 = r15.random
            java.math.BigInteger r1 = calculateGenerator_FIPS186_2(r0, r10, r1)
            org.bouncycastle.crypto.params.DSAParameters r2 = new org.bouncycastle.crypto.params.DSAParameters
            org.bouncycastle.crypto.params.DSAValidationParameters r5 = new org.bouncycastle.crypto.params.DSAValidationParameters
            r5.<init>(r4, r3)
            r2.<init>(r0, r10, r1, r5)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.crypto.generators.DSAParametersGenerator.generateParameters_FIPS186_2():org.bouncycastle.crypto.params.DSAParameters");
    }

    private DSAParameters generateParameters_FIPS186_3() {
        BigInteger bit;
        int i;
        BigInteger subtract;
        BigInteger calculateGenerator_FIPS186_3_Verifiable;
        Digest digest2 = this.digest;
        int digestSize = digest2.getDigestSize() * 8;
        byte[] bArr = new byte[(this.N / 8)];
        int i2 = (this.L - 1) / digestSize;
        int i3 = (this.L - 1) % digestSize;
        byte[] bArr2 = new byte[(this.L / 8)];
        byte[] bArr3 = new byte[digest2.getDigestSize()];
        loop0:
        while (true) {
            this.random.nextBytes(bArr);
            hash(digest2, bArr, bArr3, 0);
            bit = new BigInteger(1, bArr3).mod(ONE.shiftLeft(this.N - 1)).setBit(0).setBit(this.N - 1);
            if (isProbablePrime(bit)) {
                byte[] clone = Arrays.clone(bArr);
                int i4 = this.L * 4;
                i = 0;
                while (i < i4) {
                    for (int i5 = 1; i5 <= i2; i5++) {
                        inc(clone);
                        hash(digest2, clone, bArr2, bArr2.length - (bArr3.length * i5));
                    }
                    int length = bArr2.length - (bArr3.length * i2);
                    inc(clone);
                    hash(digest2, clone, bArr3, 0);
                    System.arraycopy(bArr3, bArr3.length - length, bArr2, 0, length);
                    bArr2[0] = (byte) (bArr2[0] | Byte.MIN_VALUE);
                    BigInteger bigInteger = new BigInteger(1, bArr2);
                    subtract = bigInteger.subtract(bigInteger.mod(bit.shiftLeft(1)).subtract(ONE));
                    if (subtract.bitLength() == this.L && isProbablePrime(subtract)) {
                        break loop0;
                    }
                    i++;
                }
                continue;
            }
        }
        return (this.usageIndex < 0 || (calculateGenerator_FIPS186_3_Verifiable = calculateGenerator_FIPS186_3_Verifiable(digest2, subtract, bit, bArr, this.usageIndex)) == null) ? new DSAParameters(subtract, bit, calculateGenerator_FIPS186_3_Unverifiable(subtract, bit, this.random), new DSAValidationParameters(bArr, i)) : new DSAParameters(subtract, bit, calculateGenerator_FIPS186_3_Verifiable, new DSAValidationParameters(bArr, i, this.usageIndex));
    }

    private static int getDefaultN(int i) {
        return i > 1024 ? 256 : 160;
    }

    private static int getMinimumIterations(int i) {
        if (i <= 1024) {
            return 40;
        }
        return (((i - 1) / 1024) * 8) + 48;
    }

    private static void hash(Digest digest2, byte[] bArr, byte[] bArr2, int i) {
        digest2.update(bArr, 0, bArr.length);
        digest2.doFinal(bArr2, i);
    }

    private static void inc(byte[] bArr) {
        int length = bArr.length - 1;
        while (length >= 0) {
            byte b = (byte) ((bArr[length] + 1) & 255);
            bArr[length] = b;
            if (b == 0) {
                length--;
            } else {
                return;
            }
        }
    }

    private boolean isProbablePrime(BigInteger bigInteger) {
        return bigInteger.isProbablePrime(this.certainty);
    }

    public DSAParameters generateParameters() {
        return this.use186_3 ? generateParameters_FIPS186_3() : generateParameters_FIPS186_2();
    }

    public void init(int i, int i2, SecureRandom secureRandom) {
        this.L = i;
        this.N = getDefaultN(i);
        this.certainty = i2;
        this.iterations = Math.max(getMinimumIterations(this.L), (i2 + 1) / 2);
        this.random = secureRandom;
        this.use186_3 = false;
        this.usageIndex = -1;
    }

    public void init(DSAParameterGenerationParameters dSAParameterGenerationParameters) {
        int l = dSAParameterGenerationParameters.getL();
        int n = dSAParameterGenerationParameters.getN();
        if (l < 1024 || l > 3072 || l % 1024 != 0) {
            throw new IllegalArgumentException("L values must be between 1024 and 3072 and a multiple of 1024");
        } else if (l == 1024 && n != 160) {
            throw new IllegalArgumentException("N must be 160 for L = 1024");
        } else if (l == 2048 && n != 224 && n != 256) {
            throw new IllegalArgumentException("N must be 224 or 256 for L = 2048");
        } else if (l == 3072 && n != 256) {
            throw new IllegalArgumentException("N must be 256 for L = 3072");
        } else if (this.digest.getDigestSize() * 8 < n) {
            throw new IllegalStateException("Digest output size too small for value of N");
        } else {
            this.L = l;
            this.N = n;
            this.certainty = dSAParameterGenerationParameters.getCertainty();
            this.iterations = Math.max(getMinimumIterations(l), (this.certainty + 1) / 2);
            this.random = dSAParameterGenerationParameters.getRandom();
            this.use186_3 = true;
            this.usageIndex = dSAParameterGenerationParameters.getUsageIndex();
        }
    }
}
