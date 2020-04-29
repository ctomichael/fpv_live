package dji.thirdparty.sanselan.common;

import dji.component.accountcenter.IMemberProtocol;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RationalNumber extends Number {
    private static final long serialVersionUID = -1;
    public final int divisor;
    public final int numerator;

    public RationalNumber(int numerator2, int divisor2) {
        this.numerator = numerator2;
        this.divisor = divisor2;
    }

    public static final RationalNumber factoryMethod(long n, long d) {
        if (n > 2147483647L || n < -2147483648L || d > 2147483647L || d < -2147483648L) {
            while (true) {
                if ((n > 2147483647L || n < -2147483648L || d > 2147483647L || d < -2147483648L) && Math.abs(n) > 1 && Math.abs(d) > 1) {
                    n >>= 1;
                    d >>= 1;
                }
            }
            if (d == 0) {
                throw new NumberFormatException("Invalid value, numerator: " + n + ", divisor: " + d);
            }
        }
        long gcd = gcd(n, d);
        return new RationalNumber((int) (n / gcd), (int) (d / gcd));
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public RationalNumber negate() {
        return new RationalNumber(-this.numerator, this.divisor);
    }

    public double doubleValue() {
        return ((double) this.numerator) / ((double) this.divisor);
    }

    public float floatValue() {
        return ((float) this.numerator) / ((float) this.divisor);
    }

    public int intValue() {
        return this.numerator / this.divisor;
    }

    public long longValue() {
        return ((long) this.numerator) / ((long) this.divisor);
    }

    public boolean isValid() {
        return this.divisor != 0;
    }

    public String toString() {
        NumberFormat nf = DecimalFormat.getInstance();
        if (this.divisor == 0) {
            return "Invalid rational (" + this.numerator + IMemberProtocol.PARAM_SEPERATOR + this.divisor + ")";
        }
        if (this.numerator % this.divisor == 0) {
            return nf.format((long) (this.numerator / this.divisor));
        }
        return this.numerator + IMemberProtocol.PARAM_SEPERATOR + this.divisor + " (" + nf.format(((double) this.numerator) / ((double) this.divisor)) + ")";
    }

    public String toDisplayString() {
        if (this.numerator % this.divisor == 0) {
            return "" + (this.numerator / this.divisor);
        }
        NumberFormat nf = DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        return nf.format(((double) this.numerator) / ((double) this.divisor));
    }
}
