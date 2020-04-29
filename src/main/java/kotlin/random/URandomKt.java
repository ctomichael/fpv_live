package kotlin.random;

import dji.publics.LogReport.base.Fields;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.UIntRange;
import kotlin.ranges.ULongRange;
import org.bouncycastle.asn1.cmc.BodyPartID;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\"\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0001ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a\u001c\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0010\u001a\u001e\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013\u001a2\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0016\u0010\u0017\u001a\u0014\u0010\u0018\u001a\u00020\u0003*\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0019\u001a\u001e\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u001b\u001a&\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u001c\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007ø\u0001\u0000¢\u0006\u0002\u0010 \u001a\u0014\u0010!\u001a\u00020\b*\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\"\u001a\u001e\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0004\u001a\u00020\bH\u0007ø\u0001\u0000¢\u0006\u0004\b#\u0010$\u001a&\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0007ø\u0001\u0000¢\u0006\u0004\b%\u0010&\u001a\u001c\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u001e\u001a\u00020'H\u0007ø\u0001\u0000¢\u0006\u0002\u0010(\u0002\u0004\n\u0002\b\u0019¨\u0006)"}, d2 = {"checkUIntRangeBounds", "", Fields.Dgo_quiz.FROM, "Lkotlin/UInt;", "until", "checkUIntRangeBounds-J1ME1BU", "(II)V", "checkULongRangeBounds", "Lkotlin/ULong;", "checkULongRangeBounds-eb3DHEI", "(JJ)V", "nextUBytes", "Lkotlin/UByteArray;", "Lkotlin/random/Random;", "size", "", "(Lkotlin/random/Random;I)[B", "array", "nextUBytes-EVgfTAA", "(Lkotlin/random/Random;[B)[B", "fromIndex", "toIndex", "nextUBytes-Wvrt4B4", "(Lkotlin/random/Random;[BII)[B", "nextUInt", "(Lkotlin/random/Random;)I", "nextUInt-qCasIEU", "(Lkotlin/random/Random;I)I", "nextUInt-a8DCA5k", "(Lkotlin/random/Random;II)I", "range", "Lkotlin/ranges/UIntRange;", "(Lkotlin/random/Random;Lkotlin/ranges/UIntRange;)I", "nextULong", "(Lkotlin/random/Random;)J", "nextULong-V1Xi4fY", "(Lkotlin/random/Random;J)J", "nextULong-jmpaW-c", "(Lkotlin/random/Random;JJ)J", "Lkotlin/ranges/ULongRange;", "(Lkotlin/random/Random;Lkotlin/ranges/ULongRange;)J", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
/* compiled from: URandom.kt */
public final class URandomKt {
    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final int nextUInt(@NotNull Random $this$nextUInt) {
        Intrinsics.checkParameterIsNotNull($this$nextUInt, "$this$nextUInt");
        return UInt.m88constructorimpl($this$nextUInt.nextInt());
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: nextUInt-qCasIEU  reason: not valid java name */
    public static final int m876nextUIntqCasIEU(@NotNull Random $this$nextUInt, int until) {
        Intrinsics.checkParameterIsNotNull($this$nextUInt, "$this$nextUInt");
        return m875nextUInta8DCA5k($this$nextUInt, 0, until);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: nextUInt-a8DCA5k  reason: not valid java name */
    public static final int m875nextUInta8DCA5k(@NotNull Random $this$nextUInt, int from, int until) {
        Intrinsics.checkParameterIsNotNull($this$nextUInt, "$this$nextUInt");
        m870checkUIntRangeBoundsJ1ME1BU(from, until);
        return UInt.m88constructorimpl($this$nextUInt.nextInt(from ^ Integer.MIN_VALUE, until ^ Integer.MIN_VALUE) ^ Integer.MIN_VALUE);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final int nextUInt(@NotNull Random $this$nextUInt, @NotNull UIntRange range) {
        Intrinsics.checkParameterIsNotNull($this$nextUInt, "$this$nextUInt");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + range);
        } else if (UnsignedKt.uintCompare(range.getLast(), -1) < 0) {
            return m875nextUInta8DCA5k($this$nextUInt, range.getFirst(), UInt.m88constructorimpl(range.getLast() + 1));
        } else {
            if (UnsignedKt.uintCompare(range.getFirst(), 0) > 0) {
                return UInt.m88constructorimpl(m875nextUInta8DCA5k($this$nextUInt, UInt.m88constructorimpl(range.getFirst() - 1), range.getLast()) + 1);
            }
            return nextUInt($this$nextUInt);
        }
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final long nextULong(@NotNull Random $this$nextULong) {
        Intrinsics.checkParameterIsNotNull($this$nextULong, "$this$nextULong");
        return ULong.m157constructorimpl($this$nextULong.nextLong());
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: nextULong-V1Xi4fY  reason: not valid java name */
    public static final long m877nextULongV1Xi4fY(@NotNull Random $this$nextULong, long until) {
        Intrinsics.checkParameterIsNotNull($this$nextULong, "$this$nextULong");
        return m878nextULongjmpaWc($this$nextULong, 0, until);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: nextULong-jmpaW-c  reason: not valid java name */
    public static final long m878nextULongjmpaWc(@NotNull Random $this$nextULong, long from, long until) {
        Intrinsics.checkParameterIsNotNull($this$nextULong, "$this$nextULong");
        m871checkULongRangeBoundseb3DHEI(from, until);
        return ULong.m157constructorimpl($this$nextULong.nextLong(from ^ Long.MIN_VALUE, until ^ Long.MIN_VALUE) ^ Long.MIN_VALUE);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final long nextULong(@NotNull Random $this$nextULong, @NotNull ULongRange range) {
        Intrinsics.checkParameterIsNotNull($this$nextULong, "$this$nextULong");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + range);
        } else if (UnsignedKt.ulongCompare(range.getLast(), -1) < 0) {
            return m878nextULongjmpaWc($this$nextULong, range.getFirst(), ULong.m157constructorimpl(range.getLast() + ULong.m157constructorimpl(((long) 1) & BodyPartID.bodyIdMax)));
        } else {
            if (UnsignedKt.ulongCompare(range.getFirst(), 0) > 0) {
                return ULong.m157constructorimpl(m878nextULongjmpaWc($this$nextULong, ULong.m157constructorimpl(range.getFirst() - ULong.m157constructorimpl(((long) 1) & BodyPartID.bodyIdMax)), range.getLast()) + ULong.m157constructorimpl(((long) 1) & BodyPartID.bodyIdMax));
            }
            return nextULong($this$nextULong);
        }
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: nextUBytes-EVgfTAA  reason: not valid java name */
    public static final byte[] m872nextUBytesEVgfTAA(@NotNull Random $this$nextUBytes, @NotNull byte[] array) {
        Intrinsics.checkParameterIsNotNull($this$nextUBytes, "$this$nextUBytes");
        Intrinsics.checkParameterIsNotNull(array, "array");
        $this$nextUBytes.nextBytes(array);
        return array;
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    public static final byte[] nextUBytes(@NotNull Random $this$nextUBytes, int size) {
        Intrinsics.checkParameterIsNotNull($this$nextUBytes, "$this$nextUBytes");
        return UByteArray.m65constructorimpl($this$nextUBytes.nextBytes(size));
    }

    /* renamed from: nextUBytes-Wvrt4B4$default  reason: not valid java name */
    public static /* synthetic */ byte[] m874nextUBytesWvrt4B4$default(Random random, byte[] bArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = UByteArray.m71getSizeimpl(bArr);
        }
        return m873nextUBytesWvrt4B4(random, bArr, i, i2);
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: nextUBytes-Wvrt4B4  reason: not valid java name */
    public static final byte[] m873nextUBytesWvrt4B4(@NotNull Random $this$nextUBytes, @NotNull byte[] array, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$nextUBytes, "$this$nextUBytes");
        Intrinsics.checkParameterIsNotNull(array, "array");
        $this$nextUBytes.nextBytes(array, fromIndex, toIndex);
        return array;
    }

    @ExperimentalUnsignedTypes
    /* renamed from: checkUIntRangeBounds-J1ME1BU  reason: not valid java name */
    public static final void m870checkUIntRangeBoundsJ1ME1BU(int from, int until) {
        if (!(UnsignedKt.uintCompare(until, from) > 0)) {
            throw new IllegalArgumentException(RandomKt.boundsErrorMessage(UInt.m82boximpl(from), UInt.m82boximpl(until)).toString());
        }
    }

    @ExperimentalUnsignedTypes
    /* renamed from: checkULongRangeBounds-eb3DHEI  reason: not valid java name */
    public static final void m871checkULongRangeBoundseb3DHEI(long from, long until) {
        if (!(UnsignedKt.ulongCompare(until, from) > 0)) {
            throw new IllegalArgumentException(RandomKt.boundsErrorMessage(ULong.m151boximpl(from), ULong.m151boximpl(until)).toString());
        }
    }
}
