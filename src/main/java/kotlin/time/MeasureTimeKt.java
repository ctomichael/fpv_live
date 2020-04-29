package kotlin.time;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a,\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\bø\u0001\u0000\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u0005\u001a0\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\b\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a0\u0010\u0000\u001a\u00020\u0001*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\bø\u0001\u0000\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\n\u001a4\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\b\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u0002\u0004\n\u0002\b\u0019¨\u0006\u000b"}, d2 = {"measureTime", "Lkotlin/time/Duration;", "block", "Lkotlin/Function0;", "", "(Lkotlin/jvm/functions/Function0;)D", "measureTimedValue", "Lkotlin/time/TimedValue;", "T", "Lkotlin/time/Clock;", "(Lkotlin/time/Clock;Lkotlin/jvm/functions/Function0;)D", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
/* compiled from: measureTime.kt */
public final class MeasureTimeKt {
    @SinceKotlin(version = "1.3")
    @ExperimentalTime
    public static final double measureTime(@NotNull Function0<Unit> block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        ClockMark mark$iv = MonoClock.INSTANCE.markNow();
        block.invoke();
        return mark$iv.elapsedNow();
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalTime
    public static final double measureTime(@NotNull Clock $this$measureTime, @NotNull Function0<Unit> block) {
        Intrinsics.checkParameterIsNotNull($this$measureTime, "$this$measureTime");
        Intrinsics.checkParameterIsNotNull(block, "block");
        ClockMark mark = $this$measureTime.markNow();
        block.invoke();
        return mark.elapsedNow();
    }

    @SinceKotlin(version = "1.3")
    @NotNull
    @ExperimentalTime
    public static final <T> TimedValue<T> measureTimedValue(@NotNull Function0<? extends T> block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        return new TimedValue<>(block.invoke(), MonoClock.INSTANCE.markNow().elapsedNow(), null);
    }

    @SinceKotlin(version = "1.3")
    @NotNull
    @ExperimentalTime
    public static final <T> TimedValue<T> measureTimedValue(@NotNull Clock $this$measureTimedValue, @NotNull Function0<? extends T> block) {
        Intrinsics.checkParameterIsNotNull($this$measureTimedValue, "$this$measureTimedValue");
        Intrinsics.checkParameterIsNotNull(block, "block");
        return new TimedValue<>(block.invoke(), $this$measureTimedValue.markNow().elapsedNow(), null);
    }
}
