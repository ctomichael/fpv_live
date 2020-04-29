package kotlin.time;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\n\u001a\u001d\u0010\u0004\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\nø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007"}, d2 = {"compareTo", "", "Lkotlin/time/ClockMark;", "other", "minus", "Lkotlin/time/Duration;", "(Lkotlin/time/ClockMark;Lkotlin/time/ClockMark;)D", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
/* compiled from: Clock.kt */
public final class ClockKt {
    @SinceKotlin(version = "1.3")
    @Deprecated(level = DeprecationLevel.ERROR, message = "Subtracting one ClockMark from another is not a well defined operation because these clock marks could have been obtained from the different clocks.")
    @InlineOnly
    @ExperimentalTime
    private static final double minus(@NotNull ClockMark $this$minus, ClockMark other) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        throw new Error("Operation is disallowed.");
    }

    @SinceKotlin(version = "1.3")
    @Deprecated(level = DeprecationLevel.ERROR, message = "Comparing one ClockMark to another is not a well defined operation because these clock marks could have been obtained from the different clocks.")
    @InlineOnly
    @ExperimentalTime
    private static final int compareTo(@NotNull ClockMark $this$compareTo, ClockMark other) {
        Intrinsics.checkParameterIsNotNull($this$compareTo, "$this$compareTo");
        throw new Error("Operation is disallowed.");
    }
}
