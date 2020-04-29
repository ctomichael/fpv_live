package dji.thirdparty.sanselan.common;

public abstract class RationalNumberUtilities extends Number {
    private static final double TOLERANCE = 1.0E-8d;

    private static class Option {
        public final double error;
        public final RationalNumber rationalNumber;

        private Option(RationalNumber rationalNumber2, double error2) {
            this.rationalNumber = rationalNumber2;
            this.error = error2;
        }

        public static final Option factory(RationalNumber rationalNumber2, double value) {
            return new Option(rationalNumber2, Math.abs(rationalNumber2.doubleValue() - value));
        }

        public String toString() {
            return this.rationalNumber.toString();
        }
    }

    public static final RationalNumber getRationalNumber(double value) {
        RationalNumber l;
        RationalNumber h;
        Option bestOption;
        if (value >= 2.147483647E9d) {
            return new RationalNumber(Integer.MAX_VALUE, 1);
        }
        if (value <= -2.147483647E9d) {
            return new RationalNumber(-2147483647, 1);
        }
        boolean negative = false;
        if (value < 0.0d) {
            negative = true;
            value = Math.abs(value);
        }
        if (value == 0.0d) {
            return new RationalNumber(0, 1);
        }
        if (value >= 1.0d) {
            int approx = (int) value;
            if (((double) approx) < value) {
                l = new RationalNumber(approx, 1);
                h = new RationalNumber(approx + 1, 1);
            } else {
                l = new RationalNumber(approx - 1, 1);
                h = new RationalNumber(approx, 1);
            }
        } else {
            int approx2 = (int) (1.0d / value);
            if (1.0d / ((double) approx2) < value) {
                l = new RationalNumber(1, approx2);
                h = new RationalNumber(1, approx2 - 1);
            } else {
                l = new RationalNumber(1, approx2 + 1);
                h = new RationalNumber(1, approx2);
            }
        }
        Option low = Option.factory(l, value);
        Option high = Option.factory(h, value);
        if (low.error < high.error) {
            bestOption = low;
        } else {
            bestOption = high;
        }
        int count = 0;
        while (bestOption.error > TOLERANCE && count < 100) {
            RationalNumber mediant = RationalNumber.factoryMethod(((long) low.rationalNumber.numerator) + ((long) high.rationalNumber.numerator), ((long) low.rationalNumber.divisor) + ((long) high.rationalNumber.divisor));
            Option mediantOption = Option.factory(mediant, value);
            if (value >= mediant.doubleValue()) {
                if (low.error <= mediantOption.error) {
                    break;
                }
                low = mediantOption;
            } else if (high.error <= mediantOption.error) {
                break;
            } else {
                high = mediantOption;
            }
            if (mediantOption.error < bestOption.error) {
                bestOption = mediantOption;
            }
            count++;
        }
        return negative ? bestOption.rationalNumber.negate() : bestOption.rationalNumber;
    }
}
