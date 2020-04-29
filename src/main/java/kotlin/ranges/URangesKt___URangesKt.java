package kotlin.ranges;

import dji.publics.LogReport.base.Fields;
import java.util.NoSuchElementException;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.random.URandomKt;
import kotlin.ranges.UIntProgression;
import org.bouncycastle.asn1.cmc.BodyPartID;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\n\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004\u001a\u001e\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a\u001e\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0002\u001a\u00020\bH\u0007ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a\u001e\u0010\u0000\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a\u001e\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0004\u001a\u001e\u0010\u000e\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0007\u001a\u001e\u0010\u000e\u001a\u00020\b*\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\n\u001a\u001e\u0010\u000e\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0013\u0010\r\u001a&\u0010\u0014\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a&\u0010\u0014\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a$\u0010\u0014\u001a\u00020\u0005*\u00020\u00052\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u001aH\u0007ø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001c\u001a&\u0010\u0014\u001a\u00020\b*\u00020\b2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0007ø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001e\u001a$\u0010\u0014\u001a\u00020\b*\u00020\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u001aH\u0007ø\u0001\u0000¢\u0006\u0004\b\u001f\u0010 \u001a&\u0010\u0014\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b!\u0010\"\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b'\u0010(\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\b\u0010)\u001a\u0004\u0018\u00010\u0005H\nø\u0001\u0000¢\u0006\u0002\b*\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\bH\u0002ø\u0001\u0000¢\u0006\u0004\b+\u0010,\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\u000bH\u0002ø\u0001\u0000¢\u0006\u0004\b-\u0010.\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b0\u00101\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u0005H\u0002ø\u0001\u0000¢\u0006\u0004\b2\u00103\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\b\u0010)\u001a\u0004\u0018\u00010\bH\nø\u0001\u0000¢\u0006\u0002\b4\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u000bH\u0002ø\u0001\u0000¢\u0006\u0004\b5\u00106\u001a\u001f\u00107\u001a\u000208*\u00020\u00012\u0006\u00109\u001a\u00020\u0001H\u0004ø\u0001\u0000¢\u0006\u0004\b:\u0010;\u001a\u001f\u00107\u001a\u000208*\u00020\u00052\u0006\u00109\u001a\u00020\u0005H\u0004ø\u0001\u0000¢\u0006\u0004\b<\u0010=\u001a\u001f\u00107\u001a\u00020>*\u00020\b2\u0006\u00109\u001a\u00020\bH\u0004ø\u0001\u0000¢\u0006\u0004\b?\u0010@\u001a\u001f\u00107\u001a\u000208*\u00020\u000b2\u0006\u00109\u001a\u00020\u000bH\u0004ø\u0001\u0000¢\u0006\u0004\bA\u0010B\u001a\u0015\u0010C\u001a\u00020\u0005*\u00020%H\bø\u0001\u0000¢\u0006\u0002\u0010D\u001a\u001c\u0010C\u001a\u00020\u0005*\u00020%2\u0006\u0010C\u001a\u00020EH\u0007ø\u0001\u0000¢\u0006\u0002\u0010F\u001a\u0015\u0010C\u001a\u00020\b*\u00020/H\bø\u0001\u0000¢\u0006\u0002\u0010G\u001a\u001c\u0010C\u001a\u00020\b*\u00020/2\u0006\u0010C\u001a\u00020EH\u0007ø\u0001\u0000¢\u0006\u0002\u0010H\u001a\f\u0010I\u001a\u000208*\u000208H\u0007\u001a\f\u0010I\u001a\u00020>*\u00020>H\u0007\u001a\u0015\u0010J\u001a\u000208*\u0002082\u0006\u0010J\u001a\u00020KH\u0004\u001a\u0015\u0010J\u001a\u00020>*\u00020>2\u0006\u0010J\u001a\u00020LH\u0004\u001a\u001f\u0010M\u001a\u00020%*\u00020\u00012\u0006\u00109\u001a\u00020\u0001H\u0004ø\u0001\u0000¢\u0006\u0004\bN\u0010O\u001a\u001f\u0010M\u001a\u00020%*\u00020\u00052\u0006\u00109\u001a\u00020\u0005H\u0004ø\u0001\u0000¢\u0006\u0004\bP\u0010Q\u001a\u001f\u0010M\u001a\u00020/*\u00020\b2\u0006\u00109\u001a\u00020\bH\u0004ø\u0001\u0000¢\u0006\u0004\bR\u0010S\u001a\u001f\u0010M\u001a\u00020%*\u00020\u000b2\u0006\u00109\u001a\u00020\u000bH\u0004ø\u0001\u0000¢\u0006\u0004\bT\u0010U\u0002\u0004\n\u0002\b\u0019¨\u0006V"}, d2 = {"coerceAtLeast", "Lkotlin/UByte;", "minimumValue", "coerceAtLeast-Kr8caGY", "(BB)B", "Lkotlin/UInt;", "coerceAtLeast-J1ME1BU", "(II)I", "Lkotlin/ULong;", "coerceAtLeast-eb3DHEI", "(JJ)J", "Lkotlin/UShort;", "coerceAtLeast-5PvTz6A", "(SS)S", "coerceAtMost", "maximumValue", "coerceAtMost-Kr8caGY", "coerceAtMost-J1ME1BU", "coerceAtMost-eb3DHEI", "coerceAtMost-5PvTz6A", "coerceIn", "coerceIn-b33U2AM", "(BBB)B", "coerceIn-WZ9TVnA", "(III)I", "range", "Lkotlin/ranges/ClosedRange;", "coerceIn-wuiCnnA", "(ILkotlin/ranges/ClosedRange;)I", "coerceIn-sambcqE", "(JJJ)J", "coerceIn-JPwROB0", "(JLkotlin/ranges/ClosedRange;)J", "coerceIn-VKSA0NQ", "(SSS)S", "contains", "", "Lkotlin/ranges/UIntRange;", "value", "contains-68kG9v0", "(Lkotlin/ranges/UIntRange;B)Z", "element", "contains-biwQdVI", "contains-fz5IDCE", "(Lkotlin/ranges/UIntRange;J)Z", "contains-ZsK3CEQ", "(Lkotlin/ranges/UIntRange;S)Z", "Lkotlin/ranges/ULongRange;", "contains-ULb-yJY", "(Lkotlin/ranges/ULongRange;B)Z", "contains-Gab390E", "(Lkotlin/ranges/ULongRange;I)Z", "contains-GYNo2lE", "contains-uhHAxoY", "(Lkotlin/ranges/ULongRange;S)Z", "downTo", "Lkotlin/ranges/UIntProgression;", "to", "downTo-Kr8caGY", "(BB)Lkotlin/ranges/UIntProgression;", "downTo-J1ME1BU", "(II)Lkotlin/ranges/UIntProgression;", "Lkotlin/ranges/ULongProgression;", "downTo-eb3DHEI", "(JJ)Lkotlin/ranges/ULongProgression;", "downTo-5PvTz6A", "(SS)Lkotlin/ranges/UIntProgression;", "random", "(Lkotlin/ranges/UIntRange;)I", "Lkotlin/random/Random;", "(Lkotlin/ranges/UIntRange;Lkotlin/random/Random;)I", "(Lkotlin/ranges/ULongRange;)J", "(Lkotlin/ranges/ULongRange;Lkotlin/random/Random;)J", "reversed", Fields.Dgo_update.STEP, "", "", "until", "until-Kr8caGY", "(BB)Lkotlin/ranges/UIntRange;", "until-J1ME1BU", "(II)Lkotlin/ranges/UIntRange;", "until-eb3DHEI", "(JJ)Lkotlin/ranges/ULongRange;", "until-5PvTz6A", "(SS)Lkotlin/ranges/UIntRange;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 15}, xi = 1, xs = "kotlin/ranges/URangesKt")
/* compiled from: _URanges.kt */
class URangesKt___URangesKt {
    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final int random(@NotNull UIntRange $this$random) {
        return URangesKt.random($this$random, Random.Default);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final long random(@NotNull ULongRange $this$random) {
        return URangesKt.random($this$random, Random.Default);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final int random(@NotNull UIntRange $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        try {
            return URandomKt.nextUInt(random, $this$random);
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final long random(@NotNull ULongRange $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        try {
            return URandomKt.nextULong(random, $this$random);
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @InlineOnly
    /* renamed from: contains-biwQdVI  reason: not valid java name */
    private static final boolean m902containsbiwQdVI(@NotNull UIntRange $this$contains, UInt element) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return element != null && $this$contains.m880containsWZ4Q5Ns(element.m131unboximpl());
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @InlineOnly
    /* renamed from: contains-GYNo2lE  reason: not valid java name */
    private static final boolean m898containsGYNo2lE(@NotNull ULongRange $this$contains, ULong element) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return element != null && $this$contains.m882containsVKZWuLQ(element.m200unboximpl());
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: contains-68kG9v0  reason: not valid java name */
    public static final boolean m897contains68kG9v0(@NotNull UIntRange $this$contains, byte value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.m880containsWZ4Q5Ns(UInt.m88constructorimpl(value & 255));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: contains-ULb-yJY  reason: not valid java name */
    public static final boolean m900containsULbyJY(@NotNull ULongRange $this$contains, byte value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.m882containsVKZWuLQ(ULong.m157constructorimpl(((long) value) & 255));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: contains-Gab390E  reason: not valid java name */
    public static final boolean m899containsGab390E(@NotNull ULongRange $this$contains, int value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.m882containsVKZWuLQ(ULong.m157constructorimpl(((long) value) & BodyPartID.bodyIdMax));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: contains-fz5IDCE  reason: not valid java name */
    public static final boolean m903containsfz5IDCE(@NotNull UIntRange $this$contains, long value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return ULong.m157constructorimpl(value >>> 32) == 0 && $this$contains.m880containsWZ4Q5Ns(UInt.m88constructorimpl((int) value));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: contains-ZsK3CEQ  reason: not valid java name */
    public static final boolean m901containsZsK3CEQ(@NotNull UIntRange $this$contains, short value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.m880containsWZ4Q5Ns(UInt.m88constructorimpl(65535 & value));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: contains-uhHAxoY  reason: not valid java name */
    public static final boolean m904containsuhHAxoY(@NotNull ULongRange $this$contains, short value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.m882containsVKZWuLQ(ULong.m157constructorimpl(((long) value) & 65535));
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: downTo-Kr8caGY  reason: not valid java name */
    public static final UIntProgression m907downToKr8caGY(byte $this$downTo, byte to) {
        return UIntProgression.Companion.m879fromClosedRangeNkh28Cs(UInt.m88constructorimpl($this$downTo & 255), UInt.m88constructorimpl(to & 255), -1);
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: downTo-J1ME1BU  reason: not valid java name */
    public static final UIntProgression m906downToJ1ME1BU(int $this$downTo, int to) {
        return UIntProgression.Companion.m879fromClosedRangeNkh28Cs($this$downTo, to, -1);
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: downTo-eb3DHEI  reason: not valid java name */
    public static final ULongProgression m908downToeb3DHEI(long $this$downTo, long to) {
        return ULongProgression.Companion.m881fromClosedRange7ftBX0g($this$downTo, to, -1);
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: downTo-5PvTz6A  reason: not valid java name */
    public static final UIntProgression m905downTo5PvTz6A(short $this$downTo, short to) {
        return UIntProgression.Companion.m879fromClosedRangeNkh28Cs(UInt.m88constructorimpl($this$downTo & UShort.MAX_VALUE), UInt.m88constructorimpl(65535 & to), -1);
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    public static final UIntProgression reversed(@NotNull UIntProgression $this$reversed) {
        Intrinsics.checkParameterIsNotNull($this$reversed, "$this$reversed");
        return UIntProgression.Companion.m879fromClosedRangeNkh28Cs($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    public static final ULongProgression reversed(@NotNull ULongProgression $this$reversed) {
        Intrinsics.checkParameterIsNotNull($this$reversed, "$this$reversed");
        return ULongProgression.Companion.m881fromClosedRange7ftBX0g($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    public static final UIntProgression step(@NotNull UIntProgression $this$step, int step) {
        Intrinsics.checkParameterIsNotNull($this$step, "$this$step");
        RangesKt.checkStepIsPositive(step > 0, Integer.valueOf(step));
        UIntProgression.Companion companion = UIntProgression.Companion;
        int first = $this$step.getFirst();
        int last = $this$step.getLast();
        if ($this$step.getStep() <= 0) {
            step = -step;
        }
        return companion.m879fromClosedRangeNkh28Cs(first, last, step);
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    public static final ULongProgression step(@NotNull ULongProgression $this$step, long step) {
        Intrinsics.checkParameterIsNotNull($this$step, "$this$step");
        RangesKt.checkStepIsPositive(step > 0, Long.valueOf(step));
        return ULongProgression.Companion.m881fromClosedRange7ftBX0g($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.jvm.internal.Intrinsics.compare(int, int):int
     arg types: [byte, int]
     candidates:
      kotlin.jvm.internal.Intrinsics.compare(long, long):int
      kotlin.jvm.internal.Intrinsics.compare(int, int):int */
    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: until-Kr8caGY  reason: not valid java name */
    public static final UIntRange m911untilKr8caGY(byte $this$until, byte to) {
        if (Intrinsics.compare((int) (to & 255), 0) <= 0) {
            return UIntRange.Companion.getEMPTY();
        }
        return new UIntRange(UInt.m88constructorimpl($this$until & 255), UInt.m88constructorimpl(UInt.m88constructorimpl(to & 255) - 1), null);
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: until-J1ME1BU  reason: not valid java name */
    public static final UIntRange m910untilJ1ME1BU(int $this$until, int to) {
        if (UnsignedKt.uintCompare(to, 0) <= 0) {
            return UIntRange.Companion.getEMPTY();
        }
        return new UIntRange($this$until, UInt.m88constructorimpl(to - 1), null);
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: until-eb3DHEI  reason: not valid java name */
    public static final ULongRange m912untileb3DHEI(long $this$until, long to) {
        if (UnsignedKt.ulongCompare(to, 0) <= 0) {
            return ULongRange.Companion.getEMPTY();
        }
        return new ULongRange($this$until, ULong.m157constructorimpl(to - ULong.m157constructorimpl(((long) 1) & BodyPartID.bodyIdMax)), null);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.jvm.internal.Intrinsics.compare(int, int):int
     arg types: [short, int]
     candidates:
      kotlin.jvm.internal.Intrinsics.compare(long, long):int
      kotlin.jvm.internal.Intrinsics.compare(int, int):int */
    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: until-5PvTz6A  reason: not valid java name */
    public static final UIntRange m909until5PvTz6A(short $this$until, short to) {
        if (Intrinsics.compare((int) (to & UShort.MAX_VALUE), 0) <= 0) {
            return UIntRange.Companion.getEMPTY();
        }
        return new UIntRange(UInt.m88constructorimpl($this$until & UShort.MAX_VALUE), UInt.m88constructorimpl(UInt.m88constructorimpl(65535 & to) - 1), null);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceAtLeast-J1ME1BU  reason: not valid java name */
    public static final int m884coerceAtLeastJ1ME1BU(int $this$coerceAtLeast, int minimumValue) {
        return UnsignedKt.uintCompare($this$coerceAtLeast, minimumValue) < 0 ? minimumValue : $this$coerceAtLeast;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceAtLeast-eb3DHEI  reason: not valid java name */
    public static final long m886coerceAtLeasteb3DHEI(long $this$coerceAtLeast, long minimumValue) {
        return UnsignedKt.ulongCompare($this$coerceAtLeast, minimumValue) < 0 ? minimumValue : $this$coerceAtLeast;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceAtLeast-Kr8caGY  reason: not valid java name */
    public static final byte m885coerceAtLeastKr8caGY(byte $this$coerceAtLeast, byte minimumValue) {
        return Intrinsics.compare($this$coerceAtLeast & 255, minimumValue & 255) < 0 ? minimumValue : $this$coerceAtLeast;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceAtLeast-5PvTz6A  reason: not valid java name */
    public static final short m883coerceAtLeast5PvTz6A(short $this$coerceAtLeast, short minimumValue) {
        return Intrinsics.compare($this$coerceAtLeast & UShort.MAX_VALUE, 65535 & minimumValue) < 0 ? minimumValue : $this$coerceAtLeast;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceAtMost-J1ME1BU  reason: not valid java name */
    public static final int m888coerceAtMostJ1ME1BU(int $this$coerceAtMost, int maximumValue) {
        return UnsignedKt.uintCompare($this$coerceAtMost, maximumValue) > 0 ? maximumValue : $this$coerceAtMost;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceAtMost-eb3DHEI  reason: not valid java name */
    public static final long m890coerceAtMosteb3DHEI(long $this$coerceAtMost, long maximumValue) {
        return UnsignedKt.ulongCompare($this$coerceAtMost, maximumValue) > 0 ? maximumValue : $this$coerceAtMost;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceAtMost-Kr8caGY  reason: not valid java name */
    public static final byte m889coerceAtMostKr8caGY(byte $this$coerceAtMost, byte maximumValue) {
        return Intrinsics.compare($this$coerceAtMost & 255, maximumValue & 255) > 0 ? maximumValue : $this$coerceAtMost;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceAtMost-5PvTz6A  reason: not valid java name */
    public static final short m887coerceAtMost5PvTz6A(short $this$coerceAtMost, short maximumValue) {
        return Intrinsics.compare($this$coerceAtMost & UShort.MAX_VALUE, 65535 & maximumValue) > 0 ? maximumValue : $this$coerceAtMost;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceIn-WZ9TVnA  reason: not valid java name */
    public static final int m893coerceInWZ9TVnA(int $this$coerceIn, int minimumValue, int maximumValue) {
        if (UnsignedKt.uintCompare(minimumValue, maximumValue) > 0) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + UInt.m125toStringimpl(maximumValue) + " is less than minimum " + UInt.m125toStringimpl(minimumValue) + '.');
        } else if (UnsignedKt.uintCompare($this$coerceIn, minimumValue) < 0) {
            return minimumValue;
        } else {
            return UnsignedKt.uintCompare($this$coerceIn, maximumValue) > 0 ? maximumValue : $this$coerceIn;
        }
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceIn-sambcqE  reason: not valid java name */
    public static final long m895coerceInsambcqE(long $this$coerceIn, long minimumValue, long maximumValue) {
        if (UnsignedKt.ulongCompare(minimumValue, maximumValue) > 0) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ULong.m194toStringimpl(maximumValue) + " is less than minimum " + ULong.m194toStringimpl(minimumValue) + '.');
        } else if (UnsignedKt.ulongCompare($this$coerceIn, minimumValue) < 0) {
            return minimumValue;
        } else {
            return UnsignedKt.ulongCompare($this$coerceIn, maximumValue) > 0 ? maximumValue : $this$coerceIn;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.jvm.internal.Intrinsics.compare(int, int):int
     arg types: [byte, byte]
     candidates:
      kotlin.jvm.internal.Intrinsics.compare(long, long):int
      kotlin.jvm.internal.Intrinsics.compare(int, int):int */
    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceIn-b33U2AM  reason: not valid java name */
    public static final byte m894coerceInb33U2AM(byte $this$coerceIn, byte minimumValue, byte maximumValue) {
        if (Intrinsics.compare((int) (minimumValue & 255), (int) (maximumValue & 255)) > 0) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + UByte.m56toStringimpl(maximumValue) + " is less than minimum " + UByte.m56toStringimpl(minimumValue) + '.');
        } else if (Intrinsics.compare((int) ($this$coerceIn & 255), (int) (minimumValue & 255)) < 0) {
            return minimumValue;
        } else {
            return Intrinsics.compare($this$coerceIn & 255, maximumValue & 255) > 0 ? maximumValue : $this$coerceIn;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.jvm.internal.Intrinsics.compare(int, int):int
     arg types: [short, short]
     candidates:
      kotlin.jvm.internal.Intrinsics.compare(long, long):int
      kotlin.jvm.internal.Intrinsics.compare(int, int):int */
    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceIn-VKSA0NQ  reason: not valid java name */
    public static final short m892coerceInVKSA0NQ(short $this$coerceIn, short minimumValue, short maximumValue) {
        if (Intrinsics.compare((int) (minimumValue & UShort.MAX_VALUE), (int) (maximumValue & UShort.MAX_VALUE)) > 0) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + UShort.m289toStringimpl(maximumValue) + " is less than minimum " + UShort.m289toStringimpl(minimumValue) + '.');
        } else if (Intrinsics.compare((int) ($this$coerceIn & UShort.MAX_VALUE), (int) (minimumValue & UShort.MAX_VALUE)) < 0) {
            return minimumValue;
        } else {
            return Intrinsics.compare($this$coerceIn & UShort.MAX_VALUE, maximumValue & UShort.MAX_VALUE) > 0 ? maximumValue : $this$coerceIn;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.ranges.RangesKt___RangesKt.coerceIn(java.lang.Comparable, kotlin.ranges.ClosedFloatingPointRange):T
     arg types: [kotlin.UInt, kotlin.ranges.ClosedFloatingPointRange]
     candidates:
      kotlin.ranges.RangesKt___RangesKt.coerceIn(int, kotlin.ranges.ClosedRange<java.lang.Integer>):int
      kotlin.ranges.RangesKt___RangesKt.coerceIn(long, kotlin.ranges.ClosedRange<java.lang.Long>):long
      kotlin.ranges.RangesKt___RangesKt.coerceIn(java.lang.Comparable, kotlin.ranges.ClosedRange):T
      kotlin.ranges.RangesKt___RangesKt.coerceIn(java.lang.Comparable, kotlin.ranges.ClosedFloatingPointRange):T */
    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceIn-wuiCnnA  reason: not valid java name */
    public static final int m896coerceInwuiCnnA(int $this$coerceIn, @NotNull ClosedRange<UInt> range) {
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range instanceof ClosedFloatingPointRange) {
            return ((UInt) RangesKt.coerceIn((Comparable) UInt.m82boximpl($this$coerceIn), (ClosedFloatingPointRange) range)).m131unboximpl();
        }
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        } else if (UnsignedKt.uintCompare($this$coerceIn, range.getStart().m131unboximpl()) < 0) {
            return range.getStart().m131unboximpl();
        } else {
            if (UnsignedKt.uintCompare($this$coerceIn, range.getEndInclusive().m131unboximpl()) > 0) {
                return range.getEndInclusive().m131unboximpl();
            }
            return $this$coerceIn;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.ranges.RangesKt___RangesKt.coerceIn(java.lang.Comparable, kotlin.ranges.ClosedFloatingPointRange):T
     arg types: [kotlin.ULong, kotlin.ranges.ClosedFloatingPointRange]
     candidates:
      kotlin.ranges.RangesKt___RangesKt.coerceIn(int, kotlin.ranges.ClosedRange<java.lang.Integer>):int
      kotlin.ranges.RangesKt___RangesKt.coerceIn(long, kotlin.ranges.ClosedRange<java.lang.Long>):long
      kotlin.ranges.RangesKt___RangesKt.coerceIn(java.lang.Comparable, kotlin.ranges.ClosedRange):T
      kotlin.ranges.RangesKt___RangesKt.coerceIn(java.lang.Comparable, kotlin.ranges.ClosedFloatingPointRange):T */
    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    /* renamed from: coerceIn-JPwROB0  reason: not valid java name */
    public static final long m891coerceInJPwROB0(long $this$coerceIn, @NotNull ClosedRange<ULong> range) {
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range instanceof ClosedFloatingPointRange) {
            return ((ULong) RangesKt.coerceIn((Comparable) ULong.m151boximpl($this$coerceIn), (ClosedFloatingPointRange) range)).m200unboximpl();
        }
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        } else if (UnsignedKt.ulongCompare($this$coerceIn, range.getStart().m200unboximpl()) < 0) {
            return range.getStart().m200unboximpl();
        } else {
            if (UnsignedKt.ulongCompare($this$coerceIn, range.getEndInclusive().m200unboximpl()) > 0) {
                return range.getEndInclusive().m200unboximpl();
            }
            return $this$coerceIn;
        }
    }
}
