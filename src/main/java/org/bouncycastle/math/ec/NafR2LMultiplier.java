package org.bouncycastle.math.ec;

import java.math.BigInteger;

public class NafR2LMultiplier extends AbstractECMultiplier {
    /* access modifiers changed from: protected */
    public ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        int i = 0;
        int[] generateCompactNaf = WNafUtil.generateCompactNaf(bigInteger);
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        int i2 = 0;
        while (true) {
            int i3 = i;
            if (i2 >= generateCompactNaf.length) {
                return infinity;
            }
            int i4 = generateCompactNaf[i2];
            int i5 = i4 >> 16;
            eCPoint = eCPoint.timesPow2((i4 & 65535) + i3);
            infinity = infinity.add(i5 < 0 ? eCPoint.negate() : eCPoint);
            i = 1;
            i2++;
        }
    }
}
