package org.bouncycastle.crypto.params;

import org.bouncycastle.math.ec.ECPoint;

public class ECPublicKeyParameters extends ECKeyParameters {
    private final ECPoint Q;

    public ECPublicKeyParameters(ECPoint eCPoint, ECDomainParameters eCDomainParameters) {
        super(false, eCDomainParameters);
        this.Q = validate(eCPoint);
    }

    private ECPoint validate(ECPoint eCPoint) {
        if (eCPoint == null) {
            throw new IllegalArgumentException("point has null value");
        } else if (eCPoint.isInfinity()) {
            throw new IllegalArgumentException("point at infinity");
        } else {
            ECPoint normalize = eCPoint.normalize();
            if (normalize.isValid()) {
                return normalize;
            }
            throw new IllegalArgumentException("point not on curve");
        }
    }

    public ECPoint getQ() {
        return this.Q;
    }
}
