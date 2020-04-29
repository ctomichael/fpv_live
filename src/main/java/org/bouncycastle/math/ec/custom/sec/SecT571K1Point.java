package org.bouncycastle.math.ec.custom.sec;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;

public class SecT571K1Point extends ECPoint.AbstractF2m {
    public SecT571K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SecT571K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
        super(eCCurve, eCFieldElement, eCFieldElement2);
        boolean z2 = true;
        if ((eCFieldElement == null) != (eCFieldElement2 != null ? false : z2)) {
            throw new IllegalArgumentException("Exactly one of the field elements is null");
        }
        this.withCompression = z;
    }

    SecT571K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
        super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
        this.withCompression = z;
    }

    /* JADX WARN: Type inference failed for: r5v10, types: [org.bouncycastle.math.ec.ECFieldElement], assign insn: 0x012f: INVOKE  (r5v10 ? I:org.bouncycastle.math.ec.ECFieldElement) = (r3v0 org.bouncycastle.math.ec.ECCurve), (r5v9 java.math.BigInteger) type: VIRTUAL call: org.bouncycastle.math.ec.ECCurve.fromBigInteger(java.math.BigInteger):org.bouncycastle.math.ec.ECFieldElement */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.bouncycastle.math.ec.ECPoint add(org.bouncycastle.math.ec.ECPoint r18) {
        /*
            r17 = this;
            boolean r2 = r17.isInfinity()
            if (r2 == 0) goto L_0x0007
        L_0x0006:
            return r18
        L_0x0007:
            boolean r2 = r18.isInfinity()
            if (r2 == 0) goto L_0x0010
            r18 = r17
            goto L_0x0006
        L_0x0010:
            org.bouncycastle.math.ec.ECCurve r3 = r17.getCurve()
            r0 = r17
            org.bouncycastle.math.ec.ECFieldElement r2 = r0.x
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r2 = (org.bouncycastle.math.ec.custom.sec.SecT571FieldElement) r2
            org.bouncycastle.math.ec.ECFieldElement r4 = r18.getRawXCoord()
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r4 = (org.bouncycastle.math.ec.custom.sec.SecT571FieldElement) r4
            boolean r5 = r2.isZero()
            if (r5 == 0) goto L_0x003a
            boolean r2 = r4.isZero()
            if (r2 == 0) goto L_0x0031
            org.bouncycastle.math.ec.ECPoint r18 = r3.getInfinity()
            goto L_0x0006
        L_0x0031:
            r0 = r18
            r1 = r17
            org.bouncycastle.math.ec.ECPoint r18 = r0.add(r1)
            goto L_0x0006
        L_0x003a:
            r0 = r17
            org.bouncycastle.math.ec.ECFieldElement r5 = r0.y
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r5 = (org.bouncycastle.math.ec.custom.sec.SecT571FieldElement) r5
            r0 = r17
            org.bouncycastle.math.ec.ECFieldElement[] r6 = r0.zs
            r7 = 0
            r6 = r6[r7]
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r6 = (org.bouncycastle.math.ec.custom.sec.SecT571FieldElement) r6
            org.bouncycastle.math.ec.ECFieldElement r7 = r18.getRawYCoord()
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r7 = (org.bouncycastle.math.ec.custom.sec.SecT571FieldElement) r7
            r8 = 0
            r0 = r18
            org.bouncycastle.math.ec.ECFieldElement r8 = r0.getZCoord(r8)
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r8 = (org.bouncycastle.math.ec.custom.sec.SecT571FieldElement) r8
            long[] r14 = org.bouncycastle.math.raw.Nat576.create64()
            long[] r12 = org.bouncycastle.math.raw.Nat576.create64()
            long[] r13 = org.bouncycastle.math.raw.Nat576.create64()
            long[] r10 = org.bouncycastle.math.raw.Nat576.create64()
            boolean r9 = r6.isOne()
            if (r9 == 0) goto L_0x009d
            r9 = 0
            r16 = r9
        L_0x0071:
            if (r16 != 0) goto L_0x00a6
            long[] r11 = r4.x
            long[] r9 = r7.x
        L_0x0077:
            boolean r15 = r8.isOne()
            if (r15 == 0) goto L_0x00b7
            r8 = 0
            r15 = r8
        L_0x007f:
            if (r15 != 0) goto L_0x00bf
            long[] r8 = r2.x
            long[] r2 = r5.x
        L_0x0085:
            org.bouncycastle.math.ec.custom.sec.SecT571Field.add(r2, r9, r13)
            org.bouncycastle.math.ec.custom.sec.SecT571Field.add(r8, r11, r10)
            boolean r2 = org.bouncycastle.math.raw.Nat576.isZero64(r10)
            if (r2 == 0) goto L_0x00d2
            boolean r2 = org.bouncycastle.math.raw.Nat576.isZero64(r13)
            if (r2 == 0) goto L_0x00cc
            org.bouncycastle.math.ec.ECPoint r18 = r17.twice()
            goto L_0x0006
        L_0x009d:
            long[] r9 = r6.x
            long[] r9 = org.bouncycastle.math.ec.custom.sec.SecT571Field.precompMultiplicand(r9)
            r16 = r9
            goto L_0x0071
        L_0x00a6:
            long[] r9 = r4.x
            r0 = r16
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiplyPrecomp(r9, r0, r12)
            long[] r9 = r7.x
            r0 = r16
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiplyPrecomp(r9, r0, r10)
            r9 = r10
            r11 = r12
            goto L_0x0077
        L_0x00b7:
            long[] r8 = r8.x
            long[] r8 = org.bouncycastle.math.ec.custom.sec.SecT571Field.precompMultiplicand(r8)
            r15 = r8
            goto L_0x007f
        L_0x00bf:
            long[] r2 = r2.x
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiplyPrecomp(r2, r15, r14)
            long[] r2 = r5.x
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiplyPrecomp(r2, r15, r13)
            r2 = r13
            r8 = r14
            goto L_0x0085
        L_0x00cc:
            org.bouncycastle.math.ec.ECPoint r18 = r3.getInfinity()
            goto L_0x0006
        L_0x00d2:
            boolean r2 = r4.isZero()
            if (r2 == 0) goto L_0x014b
            org.bouncycastle.math.ec.ECPoint r4 = r17.normalize()
            org.bouncycastle.math.ec.ECFieldElement r2 = r4.getXCoord()
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r2 = (org.bouncycastle.math.ec.custom.sec.SecT571FieldElement) r2
            org.bouncycastle.math.ec.ECFieldElement r5 = r4.getYCoord()
            org.bouncycastle.math.ec.ECFieldElement r4 = r5.add(r7)
            org.bouncycastle.math.ec.ECFieldElement r6 = r4.divide(r2)
            org.bouncycastle.math.ec.ECFieldElement r4 = r6.square()
            org.bouncycastle.math.ec.ECFieldElement r4 = r4.add(r6)
            org.bouncycastle.math.ec.ECFieldElement r4 = r4.add(r2)
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r4 = (org.bouncycastle.math.ec.custom.sec.SecT571FieldElement) r4
            boolean r7 = r4.isZero()
            if (r7 == 0) goto L_0x0113
            org.bouncycastle.math.ec.custom.sec.SecT571K1Point r18 = new org.bouncycastle.math.ec.custom.sec.SecT571K1Point
            org.bouncycastle.math.ec.ECFieldElement r2 = r3.getB()
            r0 = r17
            boolean r5 = r0.withCompression
            r0 = r18
            r0.<init>(r3, r4, r2, r5)
            goto L_0x0006
        L_0x0113:
            org.bouncycastle.math.ec.ECFieldElement r2 = r2.add(r4)
            org.bouncycastle.math.ec.ECFieldElement r2 = r6.multiply(r2)
            org.bouncycastle.math.ec.ECFieldElement r2 = r2.add(r4)
            org.bouncycastle.math.ec.ECFieldElement r2 = r2.add(r5)
            org.bouncycastle.math.ec.ECFieldElement r2 = r2.divide(r4)
            org.bouncycastle.math.ec.ECFieldElement r2 = r2.add(r4)
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r2 = (org.bouncycastle.math.ec.custom.sec.SecT571FieldElement) r2
            java.math.BigInteger r5 = org.bouncycastle.math.ec.ECConstants.ONE
            org.bouncycastle.math.ec.ECFieldElement r5 = r3.fromBigInteger(r5)
            r6 = r5
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r6 = (org.bouncycastle.math.ec.custom.sec.SecT571FieldElement) r6
            r7 = r6
            r5 = r2
        L_0x0138:
            org.bouncycastle.math.ec.custom.sec.SecT571K1Point r2 = new org.bouncycastle.math.ec.custom.sec.SecT571K1Point
            r6 = 1
            org.bouncycastle.math.ec.ECFieldElement[] r6 = new org.bouncycastle.math.ec.ECFieldElement[r6]
            r8 = 0
            r6[r8] = r7
            r0 = r17
            boolean r7 = r0.withCompression
            r2.<init>(r3, r4, r5, r6, r7)
            r18 = r2
            goto L_0x0006
        L_0x014b:
            org.bouncycastle.math.ec.custom.sec.SecT571Field.square(r10, r10)
            long[] r7 = org.bouncycastle.math.ec.custom.sec.SecT571Field.precompMultiplicand(r13)
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiplyPrecomp(r8, r7, r14)
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiplyPrecomp(r11, r7, r12)
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r4 = new org.bouncycastle.math.ec.custom.sec.SecT571FieldElement
            r4.<init>(r14)
            long[] r2 = r4.x
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiply(r14, r12, r2)
            boolean r2 = r4.isZero()
            if (r2 == 0) goto L_0x0179
            org.bouncycastle.math.ec.custom.sec.SecT571K1Point r18 = new org.bouncycastle.math.ec.custom.sec.SecT571K1Point
            org.bouncycastle.math.ec.ECFieldElement r2 = r3.getB()
            r0 = r17
            boolean r5 = r0.withCompression
            r0 = r18
            r0.<init>(r3, r4, r2, r5)
            goto L_0x0006
        L_0x0179:
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r2 = new org.bouncycastle.math.ec.custom.sec.SecT571FieldElement
            r2.<init>(r13)
            long[] r8 = r2.x
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiplyPrecomp(r10, r7, r8)
            if (r15 == 0) goto L_0x018c
            long[] r7 = r2.x
            long[] r8 = r2.x
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiplyPrecomp(r7, r15, r8)
        L_0x018c:
            long[] r7 = org.bouncycastle.math.raw.Nat576.createExt64()
            org.bouncycastle.math.ec.custom.sec.SecT571Field.add(r12, r10, r10)
            org.bouncycastle.math.ec.custom.sec.SecT571Field.squareAddToExt(r10, r7)
            long[] r5 = r5.x
            long[] r6 = r6.x
            org.bouncycastle.math.ec.custom.sec.SecT571Field.add(r5, r6, r10)
            long[] r5 = r2.x
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiplyAddToExt(r10, r5, r7)
            org.bouncycastle.math.ec.custom.sec.SecT571FieldElement r5 = new org.bouncycastle.math.ec.custom.sec.SecT571FieldElement
            r5.<init>(r10)
            long[] r6 = r5.x
            org.bouncycastle.math.ec.custom.sec.SecT571Field.reduce(r7, r6)
            if (r16 == 0) goto L_0x01b7
            long[] r6 = r2.x
            long[] r7 = r2.x
            r0 = r16
            org.bouncycastle.math.ec.custom.sec.SecT571Field.multiplyPrecomp(r6, r0, r7)
        L_0x01b7:
            r7 = r2
            goto L_0x0138
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.math.ec.custom.sec.SecT571K1Point.add(org.bouncycastle.math.ec.ECPoint):org.bouncycastle.math.ec.ECPoint");
    }

    /* access modifiers changed from: protected */
    public ECPoint detach() {
        return new SecT571K1Point(null, getAffineXCoord(), getAffineYCoord());
    }

    /* access modifiers changed from: protected */
    public boolean getCompressionYTilde() {
        ECFieldElement rawXCoord = getRawXCoord();
        return !rawXCoord.isZero() && getRawYCoord().testBitZero() != rawXCoord.testBitZero();
    }

    public ECFieldElement getYCoord() {
        ECFieldElement eCFieldElement = this.x;
        ECFieldElement eCFieldElement2 = this.y;
        if (isInfinity() || eCFieldElement.isZero()) {
            return eCFieldElement2;
        }
        ECFieldElement multiply = eCFieldElement2.add(eCFieldElement).multiply(eCFieldElement);
        ECFieldElement eCFieldElement3 = this.zs[0];
        return !eCFieldElement3.isOne() ? multiply.divide(eCFieldElement3) : multiply;
    }

    public ECPoint negate() {
        if (isInfinity()) {
            return this;
        }
        ECFieldElement eCFieldElement = this.x;
        if (eCFieldElement.isZero()) {
            return this;
        }
        ECFieldElement eCFieldElement2 = this.y;
        ECFieldElement eCFieldElement3 = this.zs[0];
        return new SecT571K1Point(this.curve, eCFieldElement, eCFieldElement2.add(eCFieldElement3), new ECFieldElement[]{eCFieldElement3}, this.withCompression);
    }

    public ECPoint twice() {
        if (isInfinity()) {
            return this;
        }
        ECCurve curve = getCurve();
        ECFieldElement eCFieldElement = this.x;
        if (eCFieldElement.isZero()) {
            return curve.getInfinity();
        }
        ECFieldElement eCFieldElement2 = this.y;
        ECFieldElement eCFieldElement3 = this.zs[0];
        boolean isOne = eCFieldElement3.isOne();
        ECFieldElement square = isOne ? eCFieldElement3 : eCFieldElement3.square();
        ECFieldElement add = isOne ? eCFieldElement2.square().add(eCFieldElement2) : eCFieldElement2.add(eCFieldElement3).multiply(eCFieldElement2);
        if (add.isZero()) {
            return new SecT571K1Point(curve, add, curve.getB(), this.withCompression);
        }
        ECFieldElement square2 = add.square();
        ECFieldElement multiply = isOne ? add : add.multiply(square);
        ECFieldElement square3 = eCFieldElement2.add(eCFieldElement).square();
        return new SecT571K1Point(curve, square2, square3.add(add).add(square).multiply(square3).add(isOne ? eCFieldElement3 : square.square()).add(square2).add(multiply), new ECFieldElement[]{multiply}, this.withCompression);
    }

    public ECPoint twicePlus(ECPoint eCPoint) {
        if (isInfinity()) {
            return eCPoint;
        }
        if (eCPoint.isInfinity()) {
            return twice();
        }
        ECCurve curve = getCurve();
        ECFieldElement eCFieldElement = this.x;
        if (eCFieldElement.isZero()) {
            return eCPoint;
        }
        ECFieldElement rawXCoord = eCPoint.getRawXCoord();
        ECFieldElement zCoord = eCPoint.getZCoord(0);
        if (rawXCoord.isZero() || !zCoord.isOne()) {
            return twice().add(eCPoint);
        }
        ECFieldElement eCFieldElement2 = this.y;
        ECFieldElement eCFieldElement3 = this.zs[0];
        ECFieldElement rawYCoord = eCPoint.getRawYCoord();
        ECFieldElement square = eCFieldElement.square();
        ECFieldElement square2 = eCFieldElement2.square();
        ECFieldElement square3 = eCFieldElement3.square();
        ECFieldElement add = square2.add(eCFieldElement2.multiply(eCFieldElement3));
        ECFieldElement addOne = rawYCoord.addOne();
        ECFieldElement multiplyPlusProduct = addOne.multiply(square3).add(square2).multiplyPlusProduct(add, square, square3);
        ECFieldElement multiply = rawXCoord.multiply(square3);
        ECFieldElement square4 = multiply.add(add).square();
        if (square4.isZero()) {
            return multiplyPlusProduct.isZero() ? eCPoint.twice() : curve.getInfinity();
        }
        if (multiplyPlusProduct.isZero()) {
            return new SecT571K1Point(curve, multiplyPlusProduct, curve.getB(), this.withCompression);
        }
        ECFieldElement multiply2 = multiplyPlusProduct.square().multiply(multiply);
        ECFieldElement multiply3 = multiplyPlusProduct.multiply(square4).multiply(square3);
        return new SecT571K1Point(curve, multiply2, multiplyPlusProduct.add(square4).square().multiplyPlusProduct(add, addOne, multiply3), new ECFieldElement[]{multiply3}, this.withCompression);
    }
}
