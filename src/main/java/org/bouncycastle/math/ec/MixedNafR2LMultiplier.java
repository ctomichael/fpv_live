package org.bouncycastle.math.ec;

import java.math.BigInteger;

public class MixedNafR2LMultiplier extends AbstractECMultiplier {
    protected int additionCoord;
    protected int doublingCoord;

    public MixedNafR2LMultiplier() {
        this(2, 4);
    }

    public MixedNafR2LMultiplier(int i, int i2) {
        this.additionCoord = i;
        this.doublingCoord = i2;
    }

    /* access modifiers changed from: protected */
    public ECCurve configureCurve(ECCurve eCCurve, int i) {
        if (eCCurve.getCoordinateSystem() == i) {
            return eCCurve;
        }
        if (eCCurve.supportsCoordinateSystem(i)) {
            return eCCurve.configure().setCoordinateSystem(i).create();
        }
        throw new IllegalArgumentException("Coordinate system " + i + " not supported by this curve");
    }

    /* access modifiers changed from: protected */
    public ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        int i = 0;
        ECCurve curve = eCPoint.getCurve();
        ECCurve configureCurve = configureCurve(curve, this.additionCoord);
        ECCurve configureCurve2 = configureCurve(curve, this.doublingCoord);
        int[] generateCompactNaf = WNafUtil.generateCompactNaf(bigInteger);
        ECPoint infinity = configureCurve.getInfinity();
        ECPoint importPoint = configureCurve2.importPoint(eCPoint);
        int i2 = 0;
        while (true) {
            int i3 = i;
            if (i2 >= generateCompactNaf.length) {
                return curve.importPoint(infinity);
            }
            int i4 = generateCompactNaf[i2];
            int i5 = i4 >> 16;
            importPoint = importPoint.timesPow2((i4 & 65535) + i3);
            ECPoint importPoint2 = configureCurve.importPoint(importPoint);
            if (i5 < 0) {
                importPoint2 = importPoint2.negate();
            }
            infinity = infinity.add(importPoint2);
            i = 1;
            i2++;
        }
    }
}
