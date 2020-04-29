package kotlin.internal;

import dji.publics.LogReport.base.Fields;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UnsignedKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a*\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a*\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0006\u001a*\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\t\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012"}, d2 = {"differenceModulo", "Lkotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", Fields.Dgo_update.STEP, "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
/* compiled from: UProgressionUtil.kt */
public final class UProgressionUtilKt {
    /* renamed from: differenceModulo-WZ9TVnA  reason: not valid java name */
    private static final int m862differenceModuloWZ9TVnA(int a, int b, int c) {
        int ac = UnsignedKt.m315uintRemainderJ1ME1BU(a, c);
        int bc = UnsignedKt.m315uintRemainderJ1ME1BU(b, c);
        return UnsignedKt.uintCompare(ac, bc) >= 0 ? UInt.m88constructorimpl(ac - bc) : UInt.m88constructorimpl(UInt.m88constructorimpl(ac - bc) + c);
    }

    /* renamed from: differenceModulo-sambcqE  reason: not valid java name */
    private static final long m863differenceModulosambcqE(long a, long b, long c) {
        long ac = UnsignedKt.m317ulongRemaindereb3DHEI(a, c);
        long bc = UnsignedKt.m317ulongRemaindereb3DHEI(b, c);
        return UnsignedKt.ulongCompare(ac, bc) >= 0 ? ULong.m157constructorimpl(ac - bc) : ULong.m157constructorimpl(ULong.m157constructorimpl(ac - bc) + c);
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    /* renamed from: getProgressionLastElement-Nkh28Cs  reason: not valid java name */
    public static final int m865getProgressionLastElementNkh28Cs(int start, int end, int step) {
        if (step > 0) {
            if (UnsignedKt.uintCompare(start, end) >= 0) {
                return end;
            }
            return UInt.m88constructorimpl(end - m862differenceModuloWZ9TVnA(end, start, UInt.m88constructorimpl(step)));
        } else if (step >= 0) {
            throw new IllegalArgumentException("Step is zero.");
        } else if (UnsignedKt.uintCompare(start, end) > 0) {
            return UInt.m88constructorimpl(m862differenceModuloWZ9TVnA(start, end, UInt.m88constructorimpl(-step)) + end);
        } else {
            return end;
        }
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    /* renamed from: getProgressionLastElement-7ftBX0g  reason: not valid java name */
    public static final long m864getProgressionLastElement7ftBX0g(long start, long end, long step) {
        if (step > 0) {
            if (UnsignedKt.ulongCompare(start, end) >= 0) {
                return end;
            }
            return ULong.m157constructorimpl(end - m863differenceModulosambcqE(end, start, ULong.m157constructorimpl(step)));
        } else if (step >= 0) {
            throw new IllegalArgumentException("Step is zero.");
        } else if (UnsignedKt.ulongCompare(start, end) <= 0) {
            return end;
        } else {
            return ULong.m157constructorimpl(m863differenceModulosambcqE(start, end, ULong.m157constructorimpl(-step)) + end);
        }
    }
}
