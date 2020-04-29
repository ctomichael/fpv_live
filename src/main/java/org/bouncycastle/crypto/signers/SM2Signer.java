package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.BigIntegers;

public class SM2Signer implements DSA, ECConstants {
    private int curveLength;
    private ECKeyParameters ecKey;
    private ECDomainParameters ecParams;
    private final DSAKCalculator kCalculator = new RandomDSAKCalculator();
    private ECPoint pubPoint;
    private SecureRandom random;
    private byte[] userID;

    private void addFieldElement(Digest digest, ECFieldElement eCFieldElement) {
        byte[] asUnsignedByteArray = BigIntegers.asUnsignedByteArray(this.curveLength, eCFieldElement.toBigInteger());
        digest.update(asUnsignedByteArray, 0, asUnsignedByteArray.length);
    }

    private void addUserID(Digest digest, byte[] bArr) {
        int length = bArr.length * 8;
        digest.update((byte) ((length >> 8) & 255));
        digest.update((byte) (length & 255));
        digest.update(bArr, 0, bArr.length);
    }

    private byte[] getZ(Digest digest) {
        addUserID(digest, this.userID);
        addFieldElement(digest, this.ecParams.getCurve().getA());
        addFieldElement(digest, this.ecParams.getCurve().getB());
        addFieldElement(digest, this.ecParams.getG().getAffineXCoord());
        addFieldElement(digest, this.ecParams.getG().getAffineYCoord());
        addFieldElement(digest, this.pubPoint.getAffineXCoord());
        addFieldElement(digest, this.pubPoint.getAffineYCoord());
        byte[] bArr = new byte[digest.getDigestSize()];
        digest.doFinal(bArr, 0);
        return bArr;
    }

    /* access modifiers changed from: protected */
    public BigInteger calculateE(byte[] bArr) {
        return new BigInteger(1, bArr);
    }

    /* access modifiers changed from: protected */
    public ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    public BigInteger[] generateSignature(byte[] bArr) {
        SM3Digest sM3Digest = new SM3Digest();
        byte[] z = getZ(sM3Digest);
        sM3Digest.update(z, 0, z.length);
        sM3Digest.update(bArr, 0, bArr.length);
        byte[] bArr2 = new byte[sM3Digest.getDigestSize()];
        sM3Digest.doFinal(bArr2, 0);
        BigInteger n = this.ecParams.getN();
        BigInteger calculateE = calculateE(bArr2);
        BigInteger d = ((ECPrivateKeyParameters) this.ecKey).getD();
        ECMultiplier createBasePointMultiplier = createBasePointMultiplier();
        while (true) {
            BigInteger nextK = this.kCalculator.nextK();
            BigInteger mod = calculateE.add(createBasePointMultiplier.multiply(this.ecParams.getG(), nextK).normalize().getAffineXCoord().toBigInteger()).mod(n);
            if (!mod.equals(ZERO) && !mod.add(nextK).equals(n)) {
                BigInteger mod2 = d.add(ONE).modInverse(n).multiply(nextK.subtract(mod.multiply(d)).mod(n)).mod(n);
                if (!mod2.equals(ZERO)) {
                    return new BigInteger[]{mod, mod2};
                }
            }
        }
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        CipherParameters cipherParameters2;
        if (cipherParameters instanceof ParametersWithID) {
            cipherParameters2 = ((ParametersWithID) cipherParameters).getParameters();
            this.userID = ((ParametersWithID) cipherParameters).getID();
        } else {
            this.userID = new byte[0];
            cipherParameters2 = cipherParameters;
        }
        if (z) {
            if (cipherParameters2 instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters2;
                this.ecKey = (ECKeyParameters) parametersWithRandom.getParameters();
                this.ecParams = this.ecKey.getParameters();
                this.kCalculator.init(this.ecParams.getN(), parametersWithRandom.getRandom());
            } else {
                this.ecKey = (ECKeyParameters) cipherParameters2;
                this.ecParams = this.ecKey.getParameters();
                this.kCalculator.init(this.ecParams.getN(), new SecureRandom());
            }
            this.pubPoint = this.ecParams.getG().multiply(((ECPrivateKeyParameters) this.ecKey).getD()).normalize();
        } else {
            this.ecKey = (ECKeyParameters) cipherParameters2;
            this.ecParams = this.ecKey.getParameters();
            this.pubPoint = ((ECPublicKeyParameters) this.ecKey).getQ();
        }
        this.curveLength = (this.ecParams.getCurve().getFieldSize() + 7) / 8;
    }

    public boolean verifySignature(byte[] bArr, BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger n = this.ecParams.getN();
        if (bigInteger.compareTo(ONE) < 0 || bigInteger.compareTo(n) > 0) {
            return false;
        }
        if (bigInteger2.compareTo(ONE) < 0 || bigInteger2.compareTo(n) > 0) {
            return false;
        }
        ECPoint q = ((ECPublicKeyParameters) this.ecKey).getQ();
        SM3Digest sM3Digest = new SM3Digest();
        byte[] z = getZ(sM3Digest);
        sM3Digest.update(z, 0, z.length);
        sM3Digest.update(bArr, 0, bArr.length);
        byte[] bArr2 = new byte[sM3Digest.getDigestSize()];
        sM3Digest.doFinal(bArr2, 0);
        BigInteger calculateE = calculateE(bArr2);
        BigInteger mod = bigInteger.add(bigInteger2).mod(n);
        if (mod.equals(ZERO)) {
            return false;
        }
        return bigInteger.equals(calculateE.add(this.ecParams.getG().multiply(bigInteger2).add(q.multiply(mod)).normalize().getAffineXCoord().toBigInteger()).mod(n));
    }
}
