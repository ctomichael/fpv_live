package org.bouncycastle.crypto.agreement;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.SM2KeyExchangePrivateParameters;
import org.bouncycastle.crypto.params.SM2KeyExchangePublicParameters;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public class SM2KeyExchange {
    private int curveLength;
    private final Digest digest;
    private ECDomainParameters ecParams;
    private ECPrivateKeyParameters ephemeralKey;
    private ECPoint ephemeralPubPoint;
    private boolean initiator;
    private ECPrivateKeyParameters staticKey;
    private ECPoint staticPubPoint;
    private byte[] userID;
    private int w;

    public SM2KeyExchange() {
        this(new SM3Digest());
    }

    public SM2KeyExchange(Digest digest2) {
        this.digest = digest2;
    }

    private byte[] S1(Digest digest2, ECPoint eCPoint, byte[] bArr) {
        byte[] bArr2 = new byte[digest2.getDigestSize()];
        digest2.update((byte) 2);
        addFieldElement(digest2, eCPoint.getAffineYCoord());
        digest2.update(bArr, 0, bArr.length);
        digest2.doFinal(bArr2, 0);
        return bArr2;
    }

    private byte[] S2(Digest digest2, ECPoint eCPoint, byte[] bArr) {
        byte[] bArr2 = new byte[digest2.getDigestSize()];
        digest2.update((byte) 3);
        addFieldElement(digest2, eCPoint.getAffineYCoord());
        digest2.update(bArr, 0, bArr.length);
        digest2.doFinal(bArr2, 0);
        return bArr2;
    }

    private void addFieldElement(Digest digest2, ECFieldElement eCFieldElement) {
        byte[] asUnsignedByteArray = BigIntegers.asUnsignedByteArray(this.curveLength, eCFieldElement.toBigInteger());
        digest2.update(asUnsignedByteArray, 0, asUnsignedByteArray.length);
    }

    private void addUserID(Digest digest2, byte[] bArr) {
        int length = bArr.length * 8;
        digest2.update((byte) ((length >> 8) & 255));
        digest2.update((byte) (length & 255));
        digest2.update(bArr, 0, bArr.length);
    }

    private byte[] calculateInnerHash(Digest digest2, ECPoint eCPoint, byte[] bArr, byte[] bArr2, ECPoint eCPoint2, ECPoint eCPoint3) {
        addFieldElement(digest2, eCPoint.getAffineXCoord());
        digest2.update(bArr, 0, bArr.length);
        digest2.update(bArr2, 0, bArr2.length);
        addFieldElement(digest2, eCPoint2.getAffineXCoord());
        addFieldElement(digest2, eCPoint2.getAffineYCoord());
        addFieldElement(digest2, eCPoint3.getAffineXCoord());
        addFieldElement(digest2, eCPoint3.getAffineYCoord());
        byte[] bArr3 = new byte[digest2.getDigestSize()];
        digest2.doFinal(bArr3, 0);
        return bArr3;
    }

    private ECPoint calculateU(SM2KeyExchangePublicParameters sM2KeyExchangePublicParameters) {
        BigInteger mod = this.staticKey.getD().add(reduce(this.ephemeralPubPoint.getAffineXCoord().toBigInteger()).multiply(this.ephemeralKey.getD())).mod(this.ecParams.getN());
        return sM2KeyExchangePublicParameters.getStaticPublicKey().getQ().add(sM2KeyExchangePublicParameters.getEphemeralPublicKey().getQ().multiply(reduce(sM2KeyExchangePublicParameters.getEphemeralPublicKey().getQ().getAffineXCoord().toBigInteger())).normalize()).normalize().multiply(this.ecParams.getH().multiply(mod)).normalize();
    }

    private byte[] getZ(Digest digest2, byte[] bArr, ECPoint eCPoint) {
        addUserID(digest2, bArr);
        addFieldElement(digest2, this.ecParams.getCurve().getA());
        addFieldElement(digest2, this.ecParams.getCurve().getB());
        addFieldElement(digest2, this.ecParams.getG().getAffineXCoord());
        addFieldElement(digest2, this.ecParams.getG().getAffineYCoord());
        addFieldElement(digest2, eCPoint.getAffineXCoord());
        addFieldElement(digest2, eCPoint.getAffineYCoord());
        byte[] bArr2 = new byte[digest2.getDigestSize()];
        digest2.doFinal(bArr2, 0);
        return bArr2;
    }

    private byte[] kdf(ECPoint eCPoint, byte[] bArr, byte[] bArr2, int i) {
        int digestSize = this.digest.getDigestSize() * 8;
        byte[] bArr3 = new byte[this.digest.getDigestSize()];
        byte[] bArr4 = new byte[((i + 7) / 8)];
        int i2 = 1;
        int i3 = 0;
        int i4 = 1;
        while (i2 <= ((i + digestSize) - 1) / digestSize) {
            addFieldElement(this.digest, eCPoint.getAffineXCoord());
            addFieldElement(this.digest, eCPoint.getAffineYCoord());
            this.digest.update(bArr, 0, bArr.length);
            this.digest.update(bArr2, 0, bArr2.length);
            this.digest.update((byte) (i4 >> 24));
            this.digest.update((byte) (i4 >> 16));
            this.digest.update((byte) (i4 >> 8));
            this.digest.update((byte) i4);
            this.digest.doFinal(bArr3, 0);
            if (bArr3.length + i3 < bArr4.length) {
                System.arraycopy(bArr3, 0, bArr4, i3, bArr3.length);
            } else {
                System.arraycopy(bArr3, 0, bArr4, i3, bArr4.length - i3);
            }
            i4++;
            i2++;
            i3 = bArr3.length + i3;
        }
        return bArr4;
    }

    private BigInteger reduce(BigInteger bigInteger) {
        return bigInteger.and(BigInteger.valueOf(1).shiftLeft(this.w).subtract(BigInteger.valueOf(1))).setBit(this.w);
    }

    public byte[] calculateKey(int i, CipherParameters cipherParameters) {
        SM2KeyExchangePublicParameters sM2KeyExchangePublicParameters;
        byte[] bArr;
        if (cipherParameters instanceof ParametersWithID) {
            bArr = ((ParametersWithID) cipherParameters).getID();
            sM2KeyExchangePublicParameters = (SM2KeyExchangePublicParameters) ((ParametersWithID) cipherParameters).getParameters();
        } else {
            sM2KeyExchangePublicParameters = (SM2KeyExchangePublicParameters) cipherParameters;
            bArr = new byte[0];
        }
        byte[] z = getZ(this.digest, this.userID, this.staticPubPoint);
        byte[] z2 = getZ(this.digest, bArr, sM2KeyExchangePublicParameters.getStaticPublicKey().getQ());
        ECPoint calculateU = calculateU(sM2KeyExchangePublicParameters);
        return this.initiator ? kdf(calculateU, z, z2, i) : kdf(calculateU, z2, z, i);
    }

    public byte[][] calculateKeyWithConfirmation(int i, byte[] bArr, CipherParameters cipherParameters) {
        SM2KeyExchangePublicParameters sM2KeyExchangePublicParameters;
        byte[] bArr2;
        if (cipherParameters instanceof ParametersWithID) {
            bArr2 = ((ParametersWithID) cipherParameters).getID();
            sM2KeyExchangePublicParameters = (SM2KeyExchangePublicParameters) ((ParametersWithID) cipherParameters).getParameters();
        } else {
            sM2KeyExchangePublicParameters = (SM2KeyExchangePublicParameters) cipherParameters;
            bArr2 = new byte[0];
        }
        if (!this.initiator || bArr != null) {
            byte[] z = getZ(this.digest, this.userID, this.staticPubPoint);
            byte[] z2 = getZ(this.digest, bArr2, sM2KeyExchangePublicParameters.getStaticPublicKey().getQ());
            ECPoint calculateU = calculateU(sM2KeyExchangePublicParameters);
            if (this.initiator) {
                byte[] kdf = kdf(calculateU, z, z2, i);
                byte[] calculateInnerHash = calculateInnerHash(this.digest, calculateU, z, z2, this.ephemeralPubPoint, sM2KeyExchangePublicParameters.getEphemeralPublicKey().getQ());
                if (!Arrays.constantTimeAreEqual(S1(this.digest, calculateU, calculateInnerHash), bArr)) {
                    throw new IllegalStateException("confirmation tag mismatch");
                }
                return new byte[][]{kdf, S2(this.digest, calculateU, calculateInnerHash)};
            }
            byte[] kdf2 = kdf(calculateU, z2, z, i);
            byte[] calculateInnerHash2 = calculateInnerHash(this.digest, calculateU, z2, z, sM2KeyExchangePublicParameters.getEphemeralPublicKey().getQ(), this.ephemeralPubPoint);
            return new byte[][]{kdf2, S1(this.digest, calculateU, calculateInnerHash2), S2(this.digest, calculateU, calculateInnerHash2)};
        }
        throw new IllegalArgumentException("if initiating, confirmationTag must be set");
    }

    public int getFieldSize() {
        return (this.staticKey.getParameters().getCurve().getFieldSize() + 7) / 8;
    }

    public void init(CipherParameters cipherParameters) {
        SM2KeyExchangePrivateParameters sM2KeyExchangePrivateParameters;
        if (cipherParameters instanceof ParametersWithID) {
            sM2KeyExchangePrivateParameters = (SM2KeyExchangePrivateParameters) ((ParametersWithID) cipherParameters).getParameters();
            this.userID = ((ParametersWithID) cipherParameters).getID();
        } else {
            this.userID = new byte[0];
            sM2KeyExchangePrivateParameters = (SM2KeyExchangePrivateParameters) cipherParameters;
        }
        this.initiator = sM2KeyExchangePrivateParameters.isInitiator();
        this.staticKey = sM2KeyExchangePrivateParameters.getStaticPrivateKey();
        this.ephemeralKey = sM2KeyExchangePrivateParameters.getEphemeralPrivateKey();
        this.ecParams = this.staticKey.getParameters();
        this.staticPubPoint = sM2KeyExchangePrivateParameters.getStaticPublicPoint();
        this.ephemeralPubPoint = sM2KeyExchangePrivateParameters.getEphemeralPublicPoint();
        this.curveLength = (this.ecParams.getCurve().getFieldSize() + 7) / 8;
        this.w = (this.ecParams.getCurve().getFieldSize() / 2) - 1;
    }
}
