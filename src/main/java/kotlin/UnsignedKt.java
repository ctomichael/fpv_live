package kotlin;

import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.LongCompanionObject;
import kotlin.text.CharsKt;
import org.bouncycastle.asn1.cmc.BodyPartID;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u0004\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0001\u001a\"\u0010\f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b\r\u0010\u000e\u001a\"\u0010\u000f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u000e\u001a\u0010\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\tH\u0001\u001a\u0018\u0010\u0012\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\u0013H\u0001\u001a\"\u0010\u0014\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a\"\u0010\u0017\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0018\u0010\u0016\u001a\u0010\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0013H\u0001\u001a\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u0013H\u0000\u001a\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\tH\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u001d"}, d2 = {"doubleToUInt", "Lkotlin/UInt;", "v", "", "(D)I", "doubleToULong", "Lkotlin/ULong;", "(D)J", "uintCompare", "", "v1", "v2", "uintDivide", "uintDivide-J1ME1BU", "(II)I", "uintRemainder", "uintRemainder-J1ME1BU", "uintToDouble", "ulongCompare", "", "ulongDivide", "ulongDivide-eb3DHEI", "(JJ)J", "ulongRemainder", "ulongRemainder-eb3DHEI", "ulongToDouble", "ulongToString", "", "base", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
@JvmName(name = "UnsignedKt")
/* compiled from: UnsignedUtils.kt */
public final class UnsignedKt {
    @PublishedApi
    public static final int uintCompare(int v1, int v2) {
        return Intrinsics.compare(v1 ^ Integer.MIN_VALUE, Integer.MIN_VALUE ^ v2);
    }

    @PublishedApi
    public static final int ulongCompare(long v1, long v2) {
        return ((v1 ^ Long.MIN_VALUE) > (Long.MIN_VALUE ^ v2) ? 1 : ((v1 ^ Long.MIN_VALUE) == (Long.MIN_VALUE ^ v2) ? 0 : -1));
    }

    @PublishedApi
    /* renamed from: uintDivide-J1ME1BU  reason: not valid java name */
    public static final int m314uintDivideJ1ME1BU(int v1, int v2) {
        return UInt.m88constructorimpl((int) ((((long) v1) & BodyPartID.bodyIdMax) / (((long) v2) & BodyPartID.bodyIdMax)));
    }

    @PublishedApi
    /* renamed from: uintRemainder-J1ME1BU  reason: not valid java name */
    public static final int m315uintRemainderJ1ME1BU(int v1, int v2) {
        return UInt.m88constructorimpl((int) ((((long) v1) & BodyPartID.bodyIdMax) % (((long) v2) & BodyPartID.bodyIdMax)));
    }

    @PublishedApi
    /* renamed from: ulongDivide-eb3DHEI  reason: not valid java name */
    public static final long m316ulongDivideeb3DHEI(long v1, long v2) {
        long dividend = v1;
        long divisor = v2;
        if (divisor < 0) {
            if (ulongCompare(v1, v2) < 0) {
                return ULong.m157constructorimpl(0);
            }
            return ULong.m157constructorimpl(1);
        } else if (dividend >= 0) {
            return ULong.m157constructorimpl(dividend / divisor);
        } else {
            long quotient = ((dividend >>> 1) / divisor) << 1;
            return ULong.m157constructorimpl(((long) (ulongCompare(ULong.m157constructorimpl(dividend - (quotient * divisor)), ULong.m157constructorimpl(divisor)) >= 0 ? 1 : 0)) + quotient);
        }
    }

    @PublishedApi
    /* renamed from: ulongRemainder-eb3DHEI  reason: not valid java name */
    public static final long m317ulongRemaindereb3DHEI(long v1, long v2) {
        long dividend = v1;
        long divisor = v2;
        if (divisor < 0) {
            if (ulongCompare(v1, v2) < 0) {
                return v1;
            }
            return ULong.m157constructorimpl(v1 - v2);
        } else if (dividend >= 0) {
            return ULong.m157constructorimpl(dividend % divisor);
        } else {
            long rem = dividend - ((((dividend >>> 1) / divisor) << 1) * divisor);
            if (ulongCompare(ULong.m157constructorimpl(rem), ULong.m157constructorimpl(divisor)) < 0) {
                divisor = 0;
            }
            return ULong.m157constructorimpl(rem - divisor);
        }
    }

    @PublishedApi
    public static final int doubleToUInt(double v) {
        if (Double.isNaN(v) || v <= uintToDouble(0)) {
            return 0;
        }
        if (v >= uintToDouble(-1)) {
            return -1;
        }
        if (v <= ((double) Integer.MAX_VALUE)) {
            return UInt.m88constructorimpl((int) v);
        }
        return UInt.m88constructorimpl(UInt.m88constructorimpl((int) (v - ((double) Integer.MAX_VALUE))) + UInt.m88constructorimpl(Integer.MAX_VALUE));
    }

    @PublishedApi
    public static final long doubleToULong(double v) {
        if (Double.isNaN(v) || v <= ulongToDouble(0)) {
            return 0;
        }
        if (v >= ulongToDouble(-1)) {
            return -1;
        }
        if (v < ((double) LongCompanionObject.MAX_VALUE)) {
            return ULong.m157constructorimpl((long) v);
        }
        return ULong.m157constructorimpl(ULong.m157constructorimpl((long) (v - 9.223372036854776E18d)) - Long.MIN_VALUE);
    }

    @PublishedApi
    public static final double uintToDouble(int v) {
        return ((double) (Integer.MAX_VALUE & v)) + (((double) ((v >>> 31) << 30)) * ((double) 2));
    }

    @PublishedApi
    public static final double ulongToDouble(long v) {
        return (((double) (v >>> 11)) * ((double) 2048)) + ((double) (2047 & v));
    }

    @NotNull
    public static final String ulongToString(long v) {
        return ulongToString(v, 10);
    }

    @NotNull
    public static final String ulongToString(long v, int base) {
        if (v >= 0) {
            String l = Long.toString(v, CharsKt.checkRadix(base));
            Intrinsics.checkExpressionValueIsNotNull(l, "java.lang.Long.toString(this, checkRadix(radix))");
            return l;
        }
        long quotient = ((v >>> 1) / ((long) base)) << 1;
        long rem = v - (((long) base) * quotient);
        if (rem >= ((long) base)) {
            rem -= (long) base;
            quotient++;
        }
        StringBuilder sb = new StringBuilder();
        String l2 = Long.toString(quotient, CharsKt.checkRadix(base));
        Intrinsics.checkExpressionValueIsNotNull(l2, "java.lang.Long.toString(this, checkRadix(radix))");
        StringBuilder append = sb.append(l2);
        String l3 = Long.toString(rem, CharsKt.checkRadix(base));
        Intrinsics.checkExpressionValueIsNotNull(l3, "java.lang.Long.toString(this, checkRadix(radix))");
        return append.append(l3).toString();
    }
}
