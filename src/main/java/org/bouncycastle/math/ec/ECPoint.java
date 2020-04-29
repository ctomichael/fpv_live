package org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Hashtable;
import org.bouncycastle.math.ec.ECFieldElement;

public abstract class ECPoint {
    protected static ECFieldElement[] EMPTY_ZS = new ECFieldElement[0];
    protected ECCurve curve;
    protected Hashtable preCompTable;
    protected boolean withCompression;
    protected ECFieldElement x;
    protected ECFieldElement y;
    protected ECFieldElement[] zs;

    public static abstract class AbstractF2m extends ECPoint {
        protected AbstractF2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
        }

        protected AbstractF2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr) {
            super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
        }

        /* access modifiers changed from: protected */
        public boolean satisfiesCurveEquation() {
            ECFieldElement multiplyPlusProduct;
            ECFieldElement squarePlusProduct;
            ECCurve curve = getCurve();
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement a = curve.getA();
            ECFieldElement b = curve.getB();
            int coordinateSystem = curve.getCoordinateSystem();
            if (coordinateSystem == 6) {
                ECFieldElement eCFieldElement2 = this.zs[0];
                boolean isOne = eCFieldElement2.isOne();
                if (eCFieldElement.isZero()) {
                    ECFieldElement square = this.y.square();
                    if (!isOne) {
                        b = b.multiply(eCFieldElement2.square());
                    }
                    return square.equals(b);
                }
                ECFieldElement eCFieldElement3 = this.y;
                ECFieldElement square2 = eCFieldElement.square();
                if (isOne) {
                    multiplyPlusProduct = eCFieldElement3.square().add(eCFieldElement3).add(a);
                    squarePlusProduct = square2.square().add(b);
                } else {
                    ECFieldElement square3 = eCFieldElement2.square();
                    ECFieldElement square4 = square3.square();
                    multiplyPlusProduct = eCFieldElement3.add(eCFieldElement2).multiplyPlusProduct(eCFieldElement3, a, square3);
                    squarePlusProduct = square2.squarePlusProduct(b, square4);
                }
                return multiplyPlusProduct.multiply(square2).equals(squarePlusProduct);
            }
            ECFieldElement eCFieldElement4 = this.y;
            ECFieldElement multiply = eCFieldElement4.add(eCFieldElement).multiply(eCFieldElement4);
            switch (coordinateSystem) {
                case 0:
                    break;
                default:
                    throw new IllegalStateException("unsupported coordinate system");
                case 1:
                    ECFieldElement eCFieldElement5 = this.zs[0];
                    if (!eCFieldElement5.isOne()) {
                        ECFieldElement multiply2 = eCFieldElement5.multiply(eCFieldElement5.square());
                        multiply = multiply.multiply(eCFieldElement5);
                        a = a.multiply(eCFieldElement5);
                        b = b.multiply(multiply2);
                        break;
                    }
                    break;
            }
            return multiply.equals(eCFieldElement.add(a).multiply(eCFieldElement.square()).add(b));
        }

        public ECPoint scaleX(ECFieldElement eCFieldElement) {
            if (isInfinity()) {
                return this;
            }
            switch (getCurveCoordinateSystem()) {
                case 5:
                    ECFieldElement rawXCoord = getRawXCoord();
                    ECFieldElement rawYCoord = getRawYCoord();
                    return getCurve().createRawPoint(rawXCoord, rawYCoord.add(rawXCoord).divide(eCFieldElement).add(rawXCoord.multiply(eCFieldElement)), getRawZCoords(), this.withCompression);
                case 6:
                    ECFieldElement rawXCoord2 = getRawXCoord();
                    ECFieldElement rawYCoord2 = getRawYCoord();
                    ECFieldElement eCFieldElement2 = getRawZCoords()[0];
                    ECFieldElement multiply = rawXCoord2.multiply(eCFieldElement.square());
                    ECFieldElement add = rawYCoord2.add(rawXCoord2).add(multiply);
                    ECFieldElement multiply2 = eCFieldElement2.multiply(eCFieldElement);
                    return getCurve().createRawPoint(multiply, add, new ECFieldElement[]{multiply2}, this.withCompression);
                default:
                    return ECPoint.super.scaleX(eCFieldElement);
            }
        }

        public ECPoint scaleY(ECFieldElement eCFieldElement) {
            if (isInfinity()) {
                return this;
            }
            switch (getCurveCoordinateSystem()) {
                case 5:
                case 6:
                    ECFieldElement rawXCoord = getRawXCoord();
                    return getCurve().createRawPoint(rawXCoord, getRawYCoord().add(rawXCoord).multiply(eCFieldElement).add(rawXCoord), getRawZCoords(), this.withCompression);
                default:
                    return ECPoint.super.scaleY(eCFieldElement);
            }
        }

        public ECPoint subtract(ECPoint eCPoint) {
            return eCPoint.isInfinity() ? this : add(eCPoint.negate());
        }

        public AbstractF2m tau() {
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement = this.x;
            switch (coordinateSystem) {
                case 0:
                case 5:
                    return (AbstractF2m) curve.createRawPoint(eCFieldElement.square(), this.y.square(), this.withCompression);
                case 1:
                case 6:
                    ECFieldElement eCFieldElement2 = this.y;
                    ECFieldElement eCFieldElement3 = this.zs[0];
                    return (AbstractF2m) curve.createRawPoint(eCFieldElement.square(), eCFieldElement2.square(), new ECFieldElement[]{eCFieldElement3.square()}, this.withCompression);
                case 2:
                case 3:
                case 4:
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }

        public AbstractF2m tauPow(int i) {
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement = this.x;
            switch (coordinateSystem) {
                case 0:
                case 5:
                    return (AbstractF2m) curve.createRawPoint(eCFieldElement.squarePow(i), this.y.squarePow(i), this.withCompression);
                case 1:
                case 6:
                    ECFieldElement eCFieldElement2 = this.y;
                    ECFieldElement eCFieldElement3 = this.zs[0];
                    return (AbstractF2m) curve.createRawPoint(eCFieldElement.squarePow(i), eCFieldElement2.squarePow(i), new ECFieldElement[]{eCFieldElement3.squarePow(i)}, this.withCompression);
                case 2:
                case 3:
                case 4:
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }
    }

    public static abstract class AbstractFp extends ECPoint {
        protected AbstractFp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
        }

        protected AbstractFp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr) {
            super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
        }

        /* access modifiers changed from: protected */
        public boolean getCompressionYTilde() {
            return getAffineYCoord().testBitZero();
        }

        /* access modifiers changed from: protected */
        public boolean satisfiesCurveEquation() {
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement eCFieldElement2 = this.y;
            ECFieldElement a = this.curve.getA();
            ECFieldElement b = this.curve.getB();
            ECFieldElement square = eCFieldElement2.square();
            switch (getCurveCoordinateSystem()) {
                case 0:
                    break;
                case 1:
                    ECFieldElement eCFieldElement3 = this.zs[0];
                    if (!eCFieldElement3.isOne()) {
                        ECFieldElement square2 = eCFieldElement3.square();
                        ECFieldElement multiply = eCFieldElement3.multiply(square2);
                        square = square.multiply(eCFieldElement3);
                        a = a.multiply(square2);
                        b = b.multiply(multiply);
                        break;
                    }
                    break;
                case 2:
                case 3:
                case 4:
                    ECFieldElement eCFieldElement4 = this.zs[0];
                    if (!eCFieldElement4.isOne()) {
                        ECFieldElement square3 = eCFieldElement4.square();
                        ECFieldElement square4 = square3.square();
                        ECFieldElement multiply2 = square3.multiply(square4);
                        a = a.multiply(square4);
                        b = b.multiply(multiply2);
                        break;
                    }
                    break;
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
            return square.equals(eCFieldElement.square().add(a).multiply(eCFieldElement).add(b));
        }

        public ECPoint subtract(ECPoint eCPoint) {
            return eCPoint.isInfinity() ? this : add(eCPoint.negate());
        }
    }

    public static class F2m extends AbstractF2m {
        public F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(eCCurve, eCFieldElement, eCFieldElement2, false);
        }

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
            boolean z2 = true;
            if ((eCFieldElement == null) != (eCFieldElement2 != null ? false : z2)) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            if (eCFieldElement != null) {
                ECFieldElement.F2m.checkFieldElements(this.x, this.y);
                if (eCCurve != null) {
                    ECFieldElement.F2m.checkFieldElements(this.x, this.curve.getA());
                }
            }
            this.withCompression = z;
        }

        F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
            super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
            this.withCompression = z;
        }

        public ECPoint add(ECPoint eCPoint) {
            ECFieldElement eCFieldElement;
            ECFieldElement eCFieldElement2;
            ECFieldElement eCFieldElement3;
            ECFieldElement eCFieldElement4;
            ECFieldElement multiply;
            ECFieldElement squarePlusProduct;
            ECFieldElement multiply2;
            if (isInfinity()) {
                return eCPoint;
            }
            if (eCPoint.isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement5 = this.x;
            ECFieldElement eCFieldElement6 = eCPoint.x;
            switch (coordinateSystem) {
                case 0:
                    ECFieldElement eCFieldElement7 = this.y;
                    ECFieldElement eCFieldElement8 = eCPoint.y;
                    ECFieldElement add = eCFieldElement5.add(eCFieldElement6);
                    ECFieldElement add2 = eCFieldElement7.add(eCFieldElement8);
                    if (add.isZero()) {
                        return add2.isZero() ? twice() : curve.getInfinity();
                    }
                    ECFieldElement divide = add2.divide(add);
                    ECFieldElement add3 = divide.square().add(divide).add(add).add(curve.getA());
                    return new F2m(curve, add3, divide.multiply(eCFieldElement5.add(add3)).add(add3).add(eCFieldElement7), this.withCompression);
                case 1:
                    ECFieldElement eCFieldElement9 = this.y;
                    ECFieldElement eCFieldElement10 = this.zs[0];
                    ECFieldElement eCFieldElement11 = eCPoint.y;
                    ECFieldElement eCFieldElement12 = eCPoint.zs[0];
                    boolean isOne = eCFieldElement12.isOne();
                    ECFieldElement add4 = eCFieldElement10.multiply(eCFieldElement11).add(isOne ? eCFieldElement9 : eCFieldElement9.multiply(eCFieldElement12));
                    ECFieldElement add5 = eCFieldElement10.multiply(eCFieldElement6).add(isOne ? eCFieldElement5 : eCFieldElement5.multiply(eCFieldElement12));
                    if (add5.isZero()) {
                        return add4.isZero() ? twice() : curve.getInfinity();
                    }
                    ECFieldElement square = add5.square();
                    ECFieldElement multiply3 = square.multiply(add5);
                    ECFieldElement multiply4 = isOne ? eCFieldElement10 : eCFieldElement10.multiply(eCFieldElement12);
                    ECFieldElement add6 = add4.add(add5);
                    ECFieldElement add7 = add6.multiplyPlusProduct(add4, square, curve.getA()).multiply(multiply4).add(multiply3);
                    ECFieldElement multiply5 = add5.multiply(add7);
                    if (!isOne) {
                        square = square.multiply(eCFieldElement12);
                    }
                    return new F2m(curve, multiply5, add4.multiplyPlusProduct(eCFieldElement5, add5, eCFieldElement9).multiplyPlusProduct(square, add6, add7), new ECFieldElement[]{multiply3.multiply(multiply4)}, this.withCompression);
                case 6:
                    if (eCFieldElement5.isZero()) {
                        return eCFieldElement6.isZero() ? curve.getInfinity() : eCPoint.add(this);
                    }
                    ECFieldElement eCFieldElement13 = this.y;
                    ECFieldElement eCFieldElement14 = this.zs[0];
                    ECFieldElement eCFieldElement15 = eCPoint.y;
                    ECFieldElement eCFieldElement16 = eCPoint.zs[0];
                    boolean isOne2 = eCFieldElement14.isOne();
                    if (!isOne2) {
                        eCFieldElement2 = eCFieldElement6.multiply(eCFieldElement14);
                        eCFieldElement = eCFieldElement15.multiply(eCFieldElement14);
                    } else {
                        eCFieldElement = eCFieldElement15;
                        eCFieldElement2 = eCFieldElement6;
                    }
                    boolean isOne3 = eCFieldElement16.isOne();
                    if (!isOne3) {
                        eCFieldElement4 = eCFieldElement5.multiply(eCFieldElement16);
                        eCFieldElement3 = eCFieldElement13.multiply(eCFieldElement16);
                    } else {
                        eCFieldElement3 = eCFieldElement13;
                        eCFieldElement4 = eCFieldElement5;
                    }
                    ECFieldElement add8 = eCFieldElement3.add(eCFieldElement);
                    ECFieldElement add9 = eCFieldElement4.add(eCFieldElement2);
                    if (add9.isZero()) {
                        return add8.isZero() ? twice() : curve.getInfinity();
                    }
                    if (eCFieldElement6.isZero()) {
                        ECPoint normalize = normalize();
                        ECFieldElement xCoord = normalize.getXCoord();
                        ECFieldElement yCoord = normalize.getYCoord();
                        ECFieldElement divide2 = yCoord.add(eCFieldElement15).divide(xCoord);
                        multiply = divide2.square().add(divide2).add(xCoord).add(curve.getA());
                        if (multiply.isZero()) {
                            return new F2m(curve, multiply, curve.getB().sqrt(), this.withCompression);
                        }
                        squarePlusProduct = divide2.multiply(xCoord.add(multiply)).add(multiply).add(yCoord).divide(multiply).add(multiply);
                        multiply2 = curve.fromBigInteger(ECConstants.ONE);
                    } else {
                        ECFieldElement square2 = add9.square();
                        ECFieldElement multiply6 = add8.multiply(eCFieldElement4);
                        ECFieldElement multiply7 = add8.multiply(eCFieldElement2);
                        multiply = multiply6.multiply(multiply7);
                        if (multiply.isZero()) {
                            return new F2m(curve, multiply, curve.getB().sqrt(), this.withCompression);
                        }
                        ECFieldElement multiply8 = add8.multiply(square2);
                        if (!isOne3) {
                            multiply8 = multiply8.multiply(eCFieldElement16);
                        }
                        squarePlusProduct = multiply7.add(square2).squarePlusProduct(multiply8, eCFieldElement13.add(eCFieldElement14));
                        multiply2 = !isOne2 ? multiply8.multiply(eCFieldElement14) : multiply8;
                    }
                    return new F2m(curve, multiply, squarePlusProduct, new ECFieldElement[]{multiply2}, this.withCompression);
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }

        /* access modifiers changed from: protected */
        public ECPoint detach() {
            return new F2m(null, getAffineXCoord(), getAffineYCoord());
        }

        /* access modifiers changed from: protected */
        public boolean getCompressionYTilde() {
            ECFieldElement rawXCoord = getRawXCoord();
            if (rawXCoord.isZero()) {
                return false;
            }
            ECFieldElement rawYCoord = getRawYCoord();
            switch (getCurveCoordinateSystem()) {
                case 5:
                case 6:
                    return rawYCoord.testBitZero() != rawXCoord.testBitZero();
                default:
                    return rawYCoord.divide(rawXCoord).testBitZero();
            }
        }

        public ECFieldElement getYCoord() {
            int curveCoordinateSystem = getCurveCoordinateSystem();
            switch (curveCoordinateSystem) {
                case 5:
                case 6:
                    ECFieldElement eCFieldElement = this.x;
                    ECFieldElement eCFieldElement2 = this.y;
                    if (isInfinity() || eCFieldElement.isZero()) {
                        return eCFieldElement2;
                    }
                    ECFieldElement multiply = eCFieldElement2.add(eCFieldElement).multiply(eCFieldElement);
                    if (6 != curveCoordinateSystem) {
                        return multiply;
                    }
                    ECFieldElement eCFieldElement3 = this.zs[0];
                    return !eCFieldElement3.isOne() ? multiply.divide(eCFieldElement3) : multiply;
                default:
                    return this.y;
            }
        }

        public ECPoint negate() {
            if (isInfinity()) {
                return this;
            }
            ECFieldElement eCFieldElement = this.x;
            if (eCFieldElement.isZero()) {
                return this;
            }
            switch (getCurveCoordinateSystem()) {
                case 0:
                    return new F2m(this.curve, eCFieldElement, this.y.add(eCFieldElement), this.withCompression);
                case 1:
                    ECFieldElement eCFieldElement2 = this.y;
                    ECFieldElement eCFieldElement3 = this.zs[0];
                    return new F2m(this.curve, eCFieldElement, eCFieldElement2.add(eCFieldElement), new ECFieldElement[]{eCFieldElement3}, this.withCompression);
                case 2:
                case 3:
                case 4:
                default:
                    throw new IllegalStateException("unsupported coordinate system");
                case 5:
                    return new F2m(this.curve, eCFieldElement, this.y.addOne(), this.withCompression);
                case 6:
                    ECFieldElement eCFieldElement4 = this.y;
                    ECFieldElement eCFieldElement5 = this.zs[0];
                    return new F2m(this.curve, eCFieldElement, eCFieldElement4.add(eCFieldElement5), new ECFieldElement[]{eCFieldElement5}, this.withCompression);
            }
        }

        public ECPoint twice() {
            ECFieldElement add;
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            ECFieldElement eCFieldElement = this.x;
            if (eCFieldElement.isZero()) {
                return curve.getInfinity();
            }
            switch (curve.getCoordinateSystem()) {
                case 0:
                    ECFieldElement add2 = this.y.divide(eCFieldElement).add(eCFieldElement);
                    ECFieldElement add3 = add2.square().add(add2).add(curve.getA());
                    return new F2m(curve, add3, eCFieldElement.squarePlusProduct(add3, add2.addOne()), this.withCompression);
                case 1:
                    ECFieldElement eCFieldElement2 = this.y;
                    ECFieldElement eCFieldElement3 = this.zs[0];
                    boolean isOne = eCFieldElement3.isOne();
                    ECFieldElement multiply = isOne ? eCFieldElement : eCFieldElement.multiply(eCFieldElement3);
                    if (!isOne) {
                        eCFieldElement2 = eCFieldElement2.multiply(eCFieldElement3);
                    }
                    ECFieldElement square = eCFieldElement.square();
                    ECFieldElement add4 = square.add(eCFieldElement2);
                    ECFieldElement square2 = multiply.square();
                    ECFieldElement add5 = add4.add(multiply);
                    ECFieldElement multiplyPlusProduct = add5.multiplyPlusProduct(add4, square2, curve.getA());
                    return new F2m(curve, multiply.multiply(multiplyPlusProduct), square.square().multiplyPlusProduct(multiply, multiplyPlusProduct, add5), new ECFieldElement[]{multiply.multiply(square2)}, this.withCompression);
                case 6:
                    ECFieldElement eCFieldElement4 = this.y;
                    ECFieldElement eCFieldElement5 = this.zs[0];
                    boolean isOne2 = eCFieldElement5.isOne();
                    ECFieldElement multiply2 = isOne2 ? eCFieldElement4 : eCFieldElement4.multiply(eCFieldElement5);
                    ECFieldElement square3 = isOne2 ? eCFieldElement5 : eCFieldElement5.square();
                    ECFieldElement a = curve.getA();
                    ECFieldElement multiply3 = isOne2 ? a : a.multiply(square3);
                    ECFieldElement add6 = eCFieldElement4.square().add(multiply2).add(multiply3);
                    if (add6.isZero()) {
                        return new F2m(curve, add6, curve.getB().sqrt(), this.withCompression);
                    }
                    ECFieldElement square4 = add6.square();
                    ECFieldElement multiply4 = isOne2 ? add6 : add6.multiply(square3);
                    ECFieldElement b = curve.getB();
                    if (b.bitLength() < (curve.getFieldSize() >> 1)) {
                        ECFieldElement square5 = eCFieldElement4.add(eCFieldElement).square();
                        ECFieldElement add7 = square5.add(add6).add(square3).multiply(square5).add(b.isOne() ? multiply3.add(square3).square() : multiply3.squarePlusProduct(b, square3.square())).add(square4);
                        if (a.isZero()) {
                            add7 = add7.add(multiply4);
                        } else if (!a.isOne()) {
                            add7 = add7.add(a.addOne().multiply(multiply4));
                        }
                        add = add7;
                    } else {
                        add = (isOne2 ? eCFieldElement : eCFieldElement.multiply(eCFieldElement5)).squarePlusProduct(add6, multiply2).add(square4).add(multiply4);
                    }
                    return new F2m(curve, square4, add, new ECFieldElement[]{multiply4}, this.withCompression);
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
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
            switch (curve.getCoordinateSystem()) {
                case 6:
                    ECFieldElement eCFieldElement2 = eCPoint.x;
                    ECFieldElement eCFieldElement3 = eCPoint.zs[0];
                    if (eCFieldElement2.isZero() || !eCFieldElement3.isOne()) {
                        return twice().add(eCPoint);
                    }
                    ECFieldElement eCFieldElement4 = this.y;
                    ECFieldElement eCFieldElement5 = this.zs[0];
                    ECFieldElement eCFieldElement6 = eCPoint.y;
                    ECFieldElement square = eCFieldElement.square();
                    ECFieldElement square2 = eCFieldElement4.square();
                    ECFieldElement square3 = eCFieldElement5.square();
                    ECFieldElement add = curve.getA().multiply(square3).add(square2).add(eCFieldElement4.multiply(eCFieldElement5));
                    ECFieldElement addOne = eCFieldElement6.addOne();
                    ECFieldElement multiplyPlusProduct = curve.getA().add(addOne).multiply(square3).add(square2).multiplyPlusProduct(add, square, square3);
                    ECFieldElement multiply = eCFieldElement2.multiply(square3);
                    ECFieldElement square4 = multiply.add(add).square();
                    if (square4.isZero()) {
                        return multiplyPlusProduct.isZero() ? eCPoint.twice() : curve.getInfinity();
                    }
                    if (multiplyPlusProduct.isZero()) {
                        return new F2m(curve, multiplyPlusProduct, curve.getB().sqrt(), this.withCompression);
                    }
                    ECFieldElement multiply2 = multiplyPlusProduct.square().multiply(multiply);
                    ECFieldElement multiply3 = multiplyPlusProduct.multiply(square4).multiply(square3);
                    return new F2m(curve, multiply2, multiplyPlusProduct.add(square4).square().multiplyPlusProduct(add, addOne, multiply3), new ECFieldElement[]{multiply3}, this.withCompression);
                default:
                    return twice().add(eCPoint);
            }
        }
    }

    public static class Fp extends AbstractFp {
        public Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(eCCurve, eCFieldElement, eCFieldElement2, false);
        }

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
            boolean z2 = true;
            if ((eCFieldElement == null) != (eCFieldElement2 != null ? false : z2)) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            this.withCompression = z;
        }

        Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
            super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
            this.withCompression = z;
        }

        public ECPoint add(ECPoint eCPoint) {
            ECFieldElement multiply;
            ECFieldElement multiply2;
            ECFieldElement subtract;
            ECFieldElement multiplyMinusProduct;
            ECFieldElement multiply3;
            ECFieldElement eCFieldElement;
            if (isInfinity()) {
                return eCPoint;
            }
            if (eCPoint.isInfinity()) {
                return this;
            }
            if (this == eCPoint) {
                return twice();
            }
            ECCurve curve = getCurve();
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement2 = this.x;
            ECFieldElement eCFieldElement3 = this.y;
            ECFieldElement eCFieldElement4 = eCPoint.x;
            ECFieldElement eCFieldElement5 = eCPoint.y;
            switch (coordinateSystem) {
                case 0:
                    ECFieldElement subtract2 = eCFieldElement4.subtract(eCFieldElement2);
                    ECFieldElement subtract3 = eCFieldElement5.subtract(eCFieldElement3);
                    if (subtract2.isZero()) {
                        return subtract3.isZero() ? twice() : curve.getInfinity();
                    }
                    ECFieldElement divide = subtract3.divide(subtract2);
                    ECFieldElement subtract4 = divide.square().subtract(eCFieldElement2).subtract(eCFieldElement4);
                    return new Fp(curve, subtract4, divide.multiply(eCFieldElement2.subtract(subtract4)).subtract(eCFieldElement3), this.withCompression);
                case 1:
                    ECFieldElement eCFieldElement6 = this.zs[0];
                    ECFieldElement eCFieldElement7 = eCPoint.zs[0];
                    boolean isOne = eCFieldElement6.isOne();
                    boolean isOne2 = eCFieldElement7.isOne();
                    if (!isOne) {
                        eCFieldElement5 = eCFieldElement5.multiply(eCFieldElement6);
                    }
                    if (!isOne2) {
                        eCFieldElement3 = eCFieldElement3.multiply(eCFieldElement7);
                    }
                    ECFieldElement subtract5 = eCFieldElement5.subtract(eCFieldElement3);
                    if (!isOne) {
                        eCFieldElement4 = eCFieldElement4.multiply(eCFieldElement6);
                    }
                    if (!isOne2) {
                        eCFieldElement2 = eCFieldElement2.multiply(eCFieldElement7);
                    }
                    ECFieldElement subtract6 = eCFieldElement4.subtract(eCFieldElement2);
                    if (subtract6.isZero()) {
                        return subtract5.isZero() ? twice() : curve.getInfinity();
                    }
                    if (isOne) {
                        eCFieldElement6 = eCFieldElement7;
                    } else if (!isOne2) {
                        eCFieldElement6 = eCFieldElement6.multiply(eCFieldElement7);
                    }
                    ECFieldElement square = subtract6.square();
                    ECFieldElement multiply4 = square.multiply(subtract6);
                    ECFieldElement multiply5 = square.multiply(eCFieldElement2);
                    ECFieldElement subtract7 = subtract5.square().multiply(eCFieldElement6).subtract(multiply4).subtract(two(multiply5));
                    return new Fp(curve, subtract6.multiply(subtract7), multiply5.subtract(subtract7).multiplyMinusProduct(subtract5, eCFieldElement3, multiply4), new ECFieldElement[]{multiply4.multiply(eCFieldElement6)}, this.withCompression);
                case 2:
                case 4:
                    ECFieldElement eCFieldElement8 = this.zs[0];
                    ECFieldElement eCFieldElement9 = eCPoint.zs[0];
                    boolean isOne3 = eCFieldElement8.isOne();
                    if (isOne3 || !eCFieldElement8.equals(eCFieldElement9)) {
                        if (isOne3) {
                            multiply2 = eCFieldElement5;
                            multiply = eCFieldElement4;
                        } else {
                            ECFieldElement square2 = eCFieldElement8.square();
                            multiply = square2.multiply(eCFieldElement4);
                            multiply2 = square2.multiply(eCFieldElement8).multiply(eCFieldElement5);
                        }
                        boolean isOne4 = eCFieldElement9.isOne();
                        if (!isOne4) {
                            ECFieldElement square3 = eCFieldElement9.square();
                            eCFieldElement2 = square3.multiply(eCFieldElement2);
                            eCFieldElement3 = square3.multiply(eCFieldElement9).multiply(eCFieldElement3);
                        }
                        ECFieldElement subtract8 = eCFieldElement2.subtract(multiply);
                        ECFieldElement subtract9 = eCFieldElement3.subtract(multiply2);
                        if (subtract8.isZero()) {
                            return subtract9.isZero() ? twice() : curve.getInfinity();
                        }
                        ECFieldElement square4 = subtract8.square();
                        ECFieldElement multiply6 = square4.multiply(subtract8);
                        ECFieldElement multiply7 = square4.multiply(eCFieldElement2);
                        subtract = subtract9.square().add(multiply6).subtract(two(multiply7));
                        multiplyMinusProduct = multiply7.subtract(subtract).multiplyMinusProduct(subtract9, multiply6, eCFieldElement3);
                        multiply3 = !isOne3 ? subtract8.multiply(eCFieldElement8) : subtract8;
                        if (!isOne4) {
                            multiply3 = multiply3.multiply(eCFieldElement9);
                        }
                        eCFieldElement = multiply3 == subtract8 ? square4 : null;
                    } else {
                        ECFieldElement subtract10 = eCFieldElement2.subtract(eCFieldElement4);
                        ECFieldElement subtract11 = eCFieldElement3.subtract(eCFieldElement5);
                        if (subtract10.isZero()) {
                            return subtract11.isZero() ? twice() : curve.getInfinity();
                        }
                        ECFieldElement square5 = subtract10.square();
                        ECFieldElement multiply8 = eCFieldElement2.multiply(square5);
                        ECFieldElement multiply9 = eCFieldElement4.multiply(square5);
                        ECFieldElement multiply10 = multiply8.subtract(multiply9).multiply(eCFieldElement3);
                        subtract = subtract11.square().subtract(multiply8).subtract(multiply9);
                        multiplyMinusProduct = multiply8.subtract(subtract).multiply(subtract11).subtract(multiply10);
                        eCFieldElement = null;
                        multiply3 = subtract10.multiply(eCFieldElement8);
                    }
                    return new Fp(curve, subtract, multiplyMinusProduct, coordinateSystem == 4 ? new ECFieldElement[]{multiply3, calculateJacobianModifiedW(multiply3, eCFieldElement)} : new ECFieldElement[]{multiply3}, this.withCompression);
                case 3:
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }

        /* access modifiers changed from: protected */
        public ECFieldElement calculateJacobianModifiedW(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            ECFieldElement a = getCurve().getA();
            if (a.isZero() || eCFieldElement.isOne()) {
                return a;
            }
            if (eCFieldElement2 == null) {
                eCFieldElement2 = eCFieldElement.square();
            }
            ECFieldElement square = eCFieldElement2.square();
            ECFieldElement negate = a.negate();
            return negate.bitLength() < a.bitLength() ? square.multiply(negate).negate() : square.multiply(a);
        }

        /* access modifiers changed from: protected */
        public ECPoint detach() {
            return new Fp(null, getAffineXCoord(), getAffineYCoord());
        }

        /* access modifiers changed from: protected */
        public ECFieldElement doubleProductFromSquares(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3, ECFieldElement eCFieldElement4) {
            return eCFieldElement.add(eCFieldElement2).square().subtract(eCFieldElement3).subtract(eCFieldElement4);
        }

        /* access modifiers changed from: protected */
        public ECFieldElement eight(ECFieldElement eCFieldElement) {
            return four(two(eCFieldElement));
        }

        /* access modifiers changed from: protected */
        public ECFieldElement four(ECFieldElement eCFieldElement) {
            return two(two(eCFieldElement));
        }

        /* access modifiers changed from: protected */
        public ECFieldElement getJacobianModifiedW() {
            ECFieldElement eCFieldElement = this.zs[1];
            if (eCFieldElement != null) {
                return eCFieldElement;
            }
            ECFieldElement[] eCFieldElementArr = this.zs;
            ECFieldElement calculateJacobianModifiedW = calculateJacobianModifiedW(this.zs[0], null);
            eCFieldElementArr[1] = calculateJacobianModifiedW;
            return calculateJacobianModifiedW;
        }

        public ECFieldElement getZCoord(int i) {
            return (i == 1 && 4 == getCurveCoordinateSystem()) ? getJacobianModifiedW() : super.getZCoord(i);
        }

        public ECPoint negate() {
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            return curve.getCoordinateSystem() != 0 ? new Fp(curve, this.x, this.y.negate(), this.zs, this.withCompression) : new Fp(curve, this.x, this.y.negate(), this.withCompression);
        }

        /* access modifiers changed from: protected */
        public ECFieldElement three(ECFieldElement eCFieldElement) {
            return two(eCFieldElement).add(eCFieldElement);
        }

        public ECPoint threeTimes() {
            if (isInfinity()) {
                return this;
            }
            ECFieldElement eCFieldElement = this.y;
            if (eCFieldElement.isZero()) {
                return this;
            }
            ECCurve curve = getCurve();
            switch (curve.getCoordinateSystem()) {
                case 0:
                    ECFieldElement eCFieldElement2 = this.x;
                    ECFieldElement two = two(eCFieldElement);
                    ECFieldElement square = two.square();
                    ECFieldElement add = three(eCFieldElement2.square()).add(getCurve().getA());
                    ECFieldElement subtract = three(eCFieldElement2).multiply(square).subtract(add.square());
                    if (subtract.isZero()) {
                        return getCurve().getInfinity();
                    }
                    ECFieldElement invert = subtract.multiply(two).invert();
                    ECFieldElement multiply = subtract.multiply(invert).multiply(add);
                    ECFieldElement subtract2 = square.square().multiply(invert).subtract(multiply);
                    ECFieldElement add2 = subtract2.subtract(multiply).multiply(multiply.add(subtract2)).add(eCFieldElement2);
                    return new Fp(curve, add2, eCFieldElement2.subtract(add2).multiply(subtract2).subtract(eCFieldElement), this.withCompression);
                case 4:
                    return twiceJacobianModified(false).add(this);
                default:
                    return twice().add(this);
            }
        }

        public ECPoint timesPow2(int i) {
            ECFieldElement eCFieldElement;
            ECFieldElement eCFieldElement2;
            if (i < 0) {
                throw new IllegalArgumentException("'e' cannot be negative");
            } else if (i == 0 || isInfinity()) {
                return this;
            } else {
                if (i == 1) {
                    return twice();
                }
                ECCurve curve = getCurve();
                ECFieldElement eCFieldElement3 = this.y;
                if (eCFieldElement3.isZero()) {
                    return curve.getInfinity();
                }
                int coordinateSystem = curve.getCoordinateSystem();
                ECFieldElement a = curve.getA();
                ECFieldElement eCFieldElement4 = this.x;
                ECFieldElement fromBigInteger = this.zs.length < 1 ? curve.fromBigInteger(ECConstants.ONE) : this.zs[0];
                if (!fromBigInteger.isOne()) {
                    switch (coordinateSystem) {
                        case 0:
                            eCFieldElement = a;
                            break;
                        case 1:
                            ECFieldElement square = fromBigInteger.square();
                            eCFieldElement4 = eCFieldElement4.multiply(fromBigInteger);
                            eCFieldElement3 = eCFieldElement3.multiply(square);
                            eCFieldElement = calculateJacobianModifiedW(fromBigInteger, square);
                            break;
                        case 2:
                            eCFieldElement = calculateJacobianModifiedW(fromBigInteger, null);
                            break;
                        case 3:
                        default:
                            throw new IllegalStateException("unsupported coordinate system");
                        case 4:
                            eCFieldElement = getJacobianModifiedW();
                            break;
                    }
                } else {
                    eCFieldElement = a;
                }
                int i2 = 0;
                ECFieldElement eCFieldElement5 = fromBigInteger;
                ECFieldElement eCFieldElement6 = eCFieldElement;
                ECFieldElement eCFieldElement7 = eCFieldElement3;
                while (i2 < i) {
                    if (eCFieldElement7.isZero()) {
                        return curve.getInfinity();
                    }
                    ECFieldElement three = three(eCFieldElement4.square());
                    ECFieldElement two = two(eCFieldElement7);
                    ECFieldElement multiply = two.multiply(eCFieldElement7);
                    ECFieldElement two2 = two(eCFieldElement4.multiply(multiply));
                    ECFieldElement two3 = two(multiply.square());
                    if (!eCFieldElement6.isZero()) {
                        three = three.add(eCFieldElement6);
                        eCFieldElement2 = two(two3.multiply(eCFieldElement6));
                    } else {
                        eCFieldElement2 = eCFieldElement6;
                    }
                    eCFieldElement4 = three.square().subtract(two(two2));
                    eCFieldElement7 = three.multiply(two2.subtract(eCFieldElement4)).subtract(two3);
                    i2++;
                    eCFieldElement5 = eCFieldElement5.isOne() ? two : two.multiply(eCFieldElement5);
                    eCFieldElement6 = eCFieldElement2;
                }
                switch (coordinateSystem) {
                    case 0:
                        ECFieldElement invert = eCFieldElement5.invert();
                        ECFieldElement square2 = invert.square();
                        return new Fp(curve, eCFieldElement4.multiply(square2), eCFieldElement7.multiply(square2.multiply(invert)), this.withCompression);
                    case 1:
                        return new Fp(curve, eCFieldElement4.multiply(eCFieldElement5), eCFieldElement7, new ECFieldElement[]{eCFieldElement5.multiply(eCFieldElement5.square())}, this.withCompression);
                    case 2:
                        return new Fp(curve, eCFieldElement4, eCFieldElement7, new ECFieldElement[]{eCFieldElement5}, this.withCompression);
                    case 3:
                    default:
                        throw new IllegalStateException("unsupported coordinate system");
                    case 4:
                        return new Fp(curve, eCFieldElement4, eCFieldElement7, new ECFieldElement[]{eCFieldElement5, eCFieldElement6}, this.withCompression);
                }
            }
        }

        public ECPoint twice() {
            ECFieldElement four;
            ECFieldElement eCFieldElement;
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            ECFieldElement eCFieldElement2 = this.y;
            if (eCFieldElement2.isZero()) {
                return curve.getInfinity();
            }
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement3 = this.x;
            switch (coordinateSystem) {
                case 0:
                    ECFieldElement divide = three(eCFieldElement3.square()).add(getCurve().getA()).divide(two(eCFieldElement2));
                    ECFieldElement subtract = divide.square().subtract(two(eCFieldElement3));
                    return new Fp(curve, subtract, divide.multiply(eCFieldElement3.subtract(subtract)).subtract(eCFieldElement2), this.withCompression);
                case 1:
                    ECFieldElement eCFieldElement4 = this.zs[0];
                    boolean isOne = eCFieldElement4.isOne();
                    ECFieldElement a = curve.getA();
                    if (!a.isZero() && !isOne) {
                        a = a.multiply(eCFieldElement4.square());
                    }
                    ECFieldElement add = a.add(three(eCFieldElement3.square()));
                    ECFieldElement multiply = isOne ? eCFieldElement2 : eCFieldElement2.multiply(eCFieldElement4);
                    ECFieldElement square = isOne ? eCFieldElement2.square() : multiply.multiply(eCFieldElement2);
                    ECFieldElement four2 = four(eCFieldElement3.multiply(square));
                    ECFieldElement subtract2 = add.square().subtract(two(four2));
                    ECFieldElement two = two(multiply);
                    ECFieldElement multiply2 = subtract2.multiply(two);
                    ECFieldElement two2 = two(square);
                    return new Fp(curve, multiply2, four2.subtract(subtract2).multiply(add).subtract(two(two2.square())), new ECFieldElement[]{two(isOne ? two(two2) : two.square()).multiply(multiply)}, this.withCompression);
                case 2:
                    ECFieldElement eCFieldElement5 = this.zs[0];
                    boolean isOne2 = eCFieldElement5.isOne();
                    ECFieldElement square2 = eCFieldElement2.square();
                    ECFieldElement square3 = square2.square();
                    ECFieldElement a2 = curve.getA();
                    ECFieldElement negate = a2.negate();
                    if (negate.toBigInteger().equals(BigInteger.valueOf(3))) {
                        ECFieldElement square4 = isOne2 ? eCFieldElement5 : eCFieldElement5.square();
                        ECFieldElement three = three(eCFieldElement3.add(square4).multiply(eCFieldElement3.subtract(square4)));
                        four = four(square2.multiply(eCFieldElement3));
                        eCFieldElement = three;
                    } else {
                        ECFieldElement three2 = three(eCFieldElement3.square());
                        if (isOne2) {
                            three2 = three2.add(a2);
                        } else if (!a2.isZero()) {
                            ECFieldElement square5 = eCFieldElement5.square().square();
                            three2 = negate.bitLength() < a2.bitLength() ? three2.subtract(square5.multiply(negate)) : three2.add(square5.multiply(a2));
                        }
                        four = four(eCFieldElement3.multiply(square2));
                        eCFieldElement = three2;
                    }
                    ECFieldElement subtract3 = eCFieldElement.square().subtract(two(four));
                    ECFieldElement subtract4 = four.subtract(subtract3).multiply(eCFieldElement).subtract(eight(square3));
                    ECFieldElement two3 = two(eCFieldElement2);
                    return new Fp(curve, subtract3, subtract4, new ECFieldElement[]{!isOne2 ? two3.multiply(eCFieldElement5) : two3}, this.withCompression);
                case 3:
                default:
                    throw new IllegalStateException("unsupported coordinate system");
                case 4:
                    return twiceJacobianModified(true);
            }
        }

        /* access modifiers changed from: protected */
        public Fp twiceJacobianModified(boolean z) {
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement eCFieldElement2 = this.y;
            ECFieldElement eCFieldElement3 = this.zs[0];
            ECFieldElement jacobianModifiedW = getJacobianModifiedW();
            ECFieldElement add = three(eCFieldElement.square()).add(jacobianModifiedW);
            ECFieldElement two = two(eCFieldElement2);
            ECFieldElement multiply = two.multiply(eCFieldElement2);
            ECFieldElement two2 = two(eCFieldElement.multiply(multiply));
            ECFieldElement subtract = add.square().subtract(two(two2));
            ECFieldElement two3 = two(multiply.square());
            return new Fp(getCurve(), subtract, add.multiply(two2.subtract(subtract)).subtract(two3), new ECFieldElement[]{eCFieldElement3.isOne() ? two : two.multiply(eCFieldElement3), z ? two(two3.multiply(jacobianModifiedW)) : null}, this.withCompression);
        }

        public ECPoint twicePlus(ECPoint eCPoint) {
            if (this == eCPoint) {
                return threeTimes();
            }
            if (isInfinity()) {
                return eCPoint;
            }
            if (eCPoint.isInfinity()) {
                return twice();
            }
            ECFieldElement eCFieldElement = this.y;
            if (eCFieldElement.isZero()) {
                return eCPoint;
            }
            ECCurve curve = getCurve();
            switch (curve.getCoordinateSystem()) {
                case 0:
                    ECFieldElement eCFieldElement2 = this.x;
                    ECFieldElement eCFieldElement3 = eCPoint.x;
                    ECFieldElement eCFieldElement4 = eCPoint.y;
                    ECFieldElement subtract = eCFieldElement3.subtract(eCFieldElement2);
                    ECFieldElement subtract2 = eCFieldElement4.subtract(eCFieldElement);
                    if (subtract.isZero()) {
                        return subtract2.isZero() ? threeTimes() : this;
                    }
                    ECFieldElement square = subtract.square();
                    ECFieldElement subtract3 = square.multiply(two(eCFieldElement2).add(eCFieldElement3)).subtract(subtract2.square());
                    if (subtract3.isZero()) {
                        return curve.getInfinity();
                    }
                    ECFieldElement invert = subtract3.multiply(subtract).invert();
                    ECFieldElement multiply = subtract3.multiply(invert).multiply(subtract2);
                    ECFieldElement subtract4 = two(eCFieldElement).multiply(square).multiply(subtract).multiply(invert).subtract(multiply);
                    ECFieldElement add = subtract4.subtract(multiply).multiply(multiply.add(subtract4)).add(eCFieldElement3);
                    return new Fp(curve, add, eCFieldElement2.subtract(add).multiply(subtract4).subtract(eCFieldElement), this.withCompression);
                case 4:
                    return twiceJacobianModified(false).add(eCPoint);
                default:
                    return twice().add(eCPoint);
            }
        }

        /* access modifiers changed from: protected */
        public ECFieldElement two(ECFieldElement eCFieldElement) {
            return eCFieldElement.add(eCFieldElement);
        }
    }

    protected ECPoint(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, getInitialZCoords(eCCurve));
    }

    protected ECPoint(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr) {
        this.preCompTable = null;
        this.curve = eCCurve;
        this.x = eCFieldElement;
        this.y = eCFieldElement2;
        this.zs = eCFieldElementArr;
    }

    protected static ECFieldElement[] getInitialZCoords(ECCurve eCCurve) {
        int coordinateSystem = eCCurve == null ? 0 : eCCurve.getCoordinateSystem();
        switch (coordinateSystem) {
            case 0:
            case 5:
                return EMPTY_ZS;
            default:
                ECFieldElement fromBigInteger = eCCurve.fromBigInteger(ECConstants.ONE);
                switch (coordinateSystem) {
                    case 1:
                    case 2:
                    case 6:
                        return new ECFieldElement[]{fromBigInteger};
                    case 3:
                        return new ECFieldElement[]{fromBigInteger, fromBigInteger, fromBigInteger};
                    case 4:
                        return new ECFieldElement[]{fromBigInteger, eCCurve.getA()};
                    case 5:
                    default:
                        throw new IllegalArgumentException("unknown coordinate system");
                }
        }
    }

    public abstract ECPoint add(ECPoint eCPoint);

    /* access modifiers changed from: protected */
    public void checkNormalized() {
        if (!isNormalized()) {
            throw new IllegalStateException("point not in normal form");
        }
    }

    /* access modifiers changed from: protected */
    public ECPoint createScaledPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return getCurve().createRawPoint(getRawXCoord().multiply(eCFieldElement), getRawYCoord().multiply(eCFieldElement2), this.withCompression);
    }

    /* access modifiers changed from: protected */
    public abstract ECPoint detach();

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ECPoint)) {
            return false;
        }
        return equals((ECPoint) obj);
    }

    public boolean equals(ECPoint eCPoint) {
        boolean z = true;
        if (eCPoint == null) {
            return false;
        }
        ECCurve curve2 = getCurve();
        ECCurve curve3 = eCPoint.getCurve();
        boolean z2 = curve2 == null;
        boolean z3 = curve3 == null;
        boolean isInfinity = isInfinity();
        boolean isInfinity2 = eCPoint.isInfinity();
        if (isInfinity || isInfinity2) {
            if (!isInfinity || !isInfinity2 || (!z2 && !z3 && !curve2.equals(curve3))) {
                z = false;
            }
            return z;
        }
        if (!z2 || !z3) {
            if (z2) {
                eCPoint = eCPoint.normalize();
            } else if (z3) {
                this = normalize();
            } else if (!curve2.equals(curve3)) {
                return false;
            } else {
                ECPoint[] eCPointArr = {this, curve2.importPoint(eCPoint)};
                curve2.normalizeAll(eCPointArr);
                this = eCPointArr[0];
                eCPoint = eCPointArr[1];
            }
        }
        if (!this.getXCoord().equals(eCPoint.getXCoord()) || !this.getYCoord().equals(eCPoint.getYCoord())) {
            z = false;
        }
        return z;
    }

    public ECFieldElement getAffineXCoord() {
        checkNormalized();
        return getXCoord();
    }

    public ECFieldElement getAffineYCoord() {
        checkNormalized();
        return getYCoord();
    }

    /* access modifiers changed from: protected */
    public abstract boolean getCompressionYTilde();

    public ECCurve getCurve() {
        return this.curve;
    }

    /* access modifiers changed from: protected */
    public int getCurveCoordinateSystem() {
        if (this.curve == null) {
            return 0;
        }
        return this.curve.getCoordinateSystem();
    }

    public final ECPoint getDetachedPoint() {
        return normalize().detach();
    }

    public byte[] getEncoded() {
        return getEncoded(this.withCompression);
    }

    public byte[] getEncoded(boolean z) {
        if (isInfinity()) {
            return new byte[1];
        }
        ECPoint normalize = normalize();
        byte[] encoded = normalize.getXCoord().getEncoded();
        if (z) {
            byte[] bArr = new byte[(encoded.length + 1)];
            bArr[0] = (byte) (normalize.getCompressionYTilde() ? 3 : 2);
            System.arraycopy(encoded, 0, bArr, 1, encoded.length);
            return bArr;
        }
        byte[] encoded2 = normalize.getYCoord().getEncoded();
        byte[] bArr2 = new byte[(encoded.length + encoded2.length + 1)];
        bArr2[0] = 4;
        System.arraycopy(encoded, 0, bArr2, 1, encoded.length);
        System.arraycopy(encoded2, 0, bArr2, encoded.length + 1, encoded2.length);
        return bArr2;
    }

    public final ECFieldElement getRawXCoord() {
        return this.x;
    }

    public final ECFieldElement getRawYCoord() {
        return this.y;
    }

    /* access modifiers changed from: protected */
    public final ECFieldElement[] getRawZCoords() {
        return this.zs;
    }

    public ECFieldElement getX() {
        return normalize().getXCoord();
    }

    public ECFieldElement getXCoord() {
        return this.x;
    }

    public ECFieldElement getY() {
        return normalize().getYCoord();
    }

    public ECFieldElement getYCoord() {
        return this.y;
    }

    public ECFieldElement getZCoord(int i) {
        if (i < 0 || i >= this.zs.length) {
            return null;
        }
        return this.zs[i];
    }

    public ECFieldElement[] getZCoords() {
        int length = this.zs.length;
        if (length == 0) {
            return EMPTY_ZS;
        }
        ECFieldElement[] eCFieldElementArr = new ECFieldElement[length];
        System.arraycopy(this.zs, 0, eCFieldElementArr, 0, length);
        return eCFieldElementArr;
    }

    public int hashCode() {
        ECCurve curve2 = getCurve();
        int hashCode = curve2 == null ? 0 : curve2.hashCode() ^ -1;
        if (isInfinity()) {
            return hashCode;
        }
        ECPoint normalize = normalize();
        return (hashCode ^ (normalize.getXCoord().hashCode() * 17)) ^ (normalize.getYCoord().hashCode() * 257);
    }

    public boolean isCompressed() {
        return this.withCompression;
    }

    public boolean isInfinity() {
        return this.x == null || this.y == null || (this.zs.length > 0 && this.zs[0].isZero());
    }

    public boolean isNormalized() {
        int curveCoordinateSystem = getCurveCoordinateSystem();
        return curveCoordinateSystem == 0 || curveCoordinateSystem == 5 || isInfinity() || this.zs[0].isOne();
    }

    public boolean isValid() {
        if (isInfinity() || getCurve() == null) {
            return true;
        }
        if (!satisfiesCurveEquation()) {
            return false;
        }
        return satisfiesCofactor();
    }

    public ECPoint multiply(BigInteger bigInteger) {
        return getCurve().getMultiplier().multiply(this, bigInteger);
    }

    public abstract ECPoint negate();

    public ECPoint normalize() {
        if (isInfinity()) {
            return this;
        }
        switch (getCurveCoordinateSystem()) {
            case 0:
            case 5:
                return this;
            default:
                ECFieldElement zCoord = getZCoord(0);
                return !zCoord.isOne() ? normalize(zCoord.invert()) : this;
        }
    }

    /* access modifiers changed from: package-private */
    public ECPoint normalize(ECFieldElement eCFieldElement) {
        switch (getCurveCoordinateSystem()) {
            case 1:
            case 6:
                return createScaledPoint(eCFieldElement, eCFieldElement);
            case 2:
            case 3:
            case 4:
                ECFieldElement square = eCFieldElement.square();
                return createScaledPoint(square, square.multiply(eCFieldElement));
            case 5:
            default:
                throw new IllegalStateException("not a projective coordinate system");
        }
    }

    /* access modifiers changed from: protected */
    public boolean satisfiesCofactor() {
        BigInteger cofactor = this.curve.getCofactor();
        return cofactor == null || cofactor.equals(ECConstants.ONE) || !ECAlgorithms.referenceMultiply(this, cofactor).isInfinity();
    }

    /* access modifiers changed from: protected */
    public abstract boolean satisfiesCurveEquation();

    public ECPoint scaleX(ECFieldElement eCFieldElement) {
        return isInfinity() ? this : getCurve().createRawPoint(getRawXCoord().multiply(eCFieldElement), getRawYCoord(), getRawZCoords(), this.withCompression);
    }

    public ECPoint scaleY(ECFieldElement eCFieldElement) {
        return isInfinity() ? this : getCurve().createRawPoint(getRawXCoord(), getRawYCoord().multiply(eCFieldElement), getRawZCoords(), this.withCompression);
    }

    public abstract ECPoint subtract(ECPoint eCPoint);

    public ECPoint threeTimes() {
        return twicePlus(this);
    }

    public ECPoint timesPow2(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("'e' cannot be negative");
        }
        while (true) {
            i--;
            if (i < 0) {
                return this;
            }
            this = this.twice();
        }
    }

    public String toString() {
        if (isInfinity()) {
            return "INF";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        stringBuffer.append(getRawXCoord());
        stringBuffer.append(',');
        stringBuffer.append(getRawYCoord());
        for (int i = 0; i < this.zs.length; i++) {
            stringBuffer.append(',');
            stringBuffer.append(this.zs[i]);
        }
        stringBuffer.append(')');
        return stringBuffer.toString();
    }

    public abstract ECPoint twice();

    public ECPoint twicePlus(ECPoint eCPoint) {
        return twice().add(eCPoint);
    }
}
