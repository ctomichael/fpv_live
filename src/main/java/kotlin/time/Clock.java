package kotlin.time;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0004"}, d2 = {"Lkotlin/time/Clock;", "", "markNow", "Lkotlin/time/ClockMark;", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
@SinceKotlin(version = "1.3")
@ExperimentalTime
/* compiled from: Clock.kt */
public interface Clock {
    @NotNull
    ClockMark markNow();
}
