package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import dji.component.accountcenter.IMemberProtocol;
import java.io.Serializable;

public class Rational extends Number implements Comparable<Rational>, Serializable {
    private static final long serialVersionUID = 510688928138848770L;
    private final long _denominator;
    private final long _numerator;

    public Rational(long numerator, long denominator) {
        this._numerator = numerator;
        this._denominator = denominator;
    }

    public double doubleValue() {
        if (this._numerator == 0) {
            return 0.0d;
        }
        return ((double) this._numerator) / ((double) this._denominator);
    }

    public float floatValue() {
        if (this._numerator == 0) {
            return 0.0f;
        }
        return ((float) this._numerator) / ((float) this._denominator);
    }

    public final byte byteValue() {
        return (byte) ((int) doubleValue());
    }

    public final int intValue() {
        return (int) doubleValue();
    }

    public final long longValue() {
        return (long) doubleValue();
    }

    public final short shortValue() {
        return (short) ((int) doubleValue());
    }

    public final long getDenominator() {
        return this._denominator;
    }

    public final long getNumerator() {
        return this._numerator;
    }

    @NotNull
    public Rational getReciprocal() {
        return new Rational(this._denominator, this._numerator);
    }

    public boolean isInteger() {
        return this._denominator == 1 || (this._denominator != 0 && this._numerator % this._denominator == 0) || (this._denominator == 0 && this._numerator == 0);
    }

    public boolean isZero() {
        return this._numerator == 0 || this._denominator == 0;
    }

    @NotNull
    public String toString() {
        return this._numerator + IMemberProtocol.PARAM_SEPERATOR + this._denominator;
    }

    @NotNull
    public String toSimpleString(boolean allowDecimal) {
        if (this._denominator == 0 && this._numerator != 0) {
            return toString();
        }
        if (isInteger()) {
            return Integer.toString(intValue());
        }
        if (this._numerator != 1 && this._denominator % this._numerator == 0) {
            return new Rational(1, this._denominator / this._numerator).toSimpleString(allowDecimal);
        }
        Rational simplifiedInstance = getSimplifiedInstance();
        if (allowDecimal) {
            String doubleString = Double.toString(simplifiedInstance.doubleValue());
            if (doubleString.length() < 5) {
                return doubleString;
            }
        }
        return simplifiedInstance.toString();
    }

    public int compareTo(@NotNull Rational that) {
        return Double.compare(doubleValue(), that.doubleValue());
    }

    public boolean equals(Rational other) {
        return other.doubleValue() == doubleValue();
    }

    public boolean equalsExact(Rational other) {
        return getDenominator() == other.getDenominator() && getNumerator() == other.getNumerator();
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Rational) || doubleValue() != ((Rational) obj).doubleValue()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((int) this._denominator) * 23) + ((int) this._numerator);
    }

    @NotNull
    public Rational getSimplifiedInstance() {
        long gcd = GCD(this._numerator, this._denominator);
        return new Rational(this._numerator / gcd, this._denominator / gcd);
    }

    private static long GCD(long a, long b) {
        if (a < 0) {
            a = -a;
        }
        if (b < 0) {
            b = -b;
        }
        while (a != 0 && b != 0) {
            if (a > b) {
                a %= b;
            } else {
                b %= a;
            }
        }
        return a == 0 ? b : a;
    }
}
