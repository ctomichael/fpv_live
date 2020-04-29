package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@SinceKotlin(version = "1.3")
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b'\u0018\u00002\u00020\u0001:\u0001\fB\u0011\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH$R\u0018\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u0002\u0004\n\u0002\b\u0019¨\u0006\r"}, d2 = {"Lkotlin/time/AbstractLongClock;", "Lkotlin/time/Clock;", "unit", "Ljava/util/concurrent/TimeUnit;", "Lkotlin/time/DurationUnit;", "(Ljava/util/concurrent/TimeUnit;)V", "getUnit", "()Ljava/util/concurrent/TimeUnit;", "markNow", "Lkotlin/time/ClockMark;", "read", "", "LongClockMark", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
@ExperimentalTime
/* compiled from: Clocks.kt */
public abstract class AbstractLongClock implements Clock {
    @NotNull
    private final TimeUnit unit;

    /* access modifiers changed from: protected */
    public abstract long read();

    public AbstractLongClock(@NotNull TimeUnit unit2) {
        Intrinsics.checkParameterIsNotNull(unit2, "unit");
        this.unit = unit2;
    }

    /* access modifiers changed from: protected */
    @NotNull
    public final TimeUnit getUnit() {
        return this.unit;
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0002\u0018\u00002\u00020\u0001B \u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007ø\u0001\u0000¢\u0006\u0002\u0010\bJ\u0010\u0010\n\u001a\u00020\u0007H\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0007H\u0002ø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0013\u0010\u0006\u001a\u00020\u0007X\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u0010"}, d2 = {"Lkotlin/time/AbstractLongClock$LongClockMark;", "Lkotlin/time/ClockMark;", "startedAt", "", "clock", "Lkotlin/time/AbstractLongClock;", "offset", "Lkotlin/time/Duration;", "(JLkotlin/time/AbstractLongClock;DLkotlin/jvm/internal/DefaultConstructorMarker;)V", "D", "elapsedNow", "()D", "plus", "duration", "plus-LRDsOJo", "(D)Lkotlin/time/ClockMark;", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
    /* compiled from: Clocks.kt */
    private static final class LongClockMark extends ClockMark {
        private final AbstractLongClock clock;
        private final double offset;
        private final long startedAt;

        private LongClockMark(long startedAt2, AbstractLongClock clock2, double offset2) {
            this.startedAt = startedAt2;
            this.clock = clock2;
            this.offset = offset2;
        }

        public /* synthetic */ LongClockMark(long startedAt2, AbstractLongClock clock2, double offset2, DefaultConstructorMarker $constructor_marker) {
            this(startedAt2, clock2, offset2);
        }

        public double elapsedNow() {
            return Duration.m951minusLRDsOJo(DurationKt.toDuration(this.clock.read() - this.startedAt, this.clock.getUnit()), this.offset);
        }

        @NotNull
        /* renamed from: plus-LRDsOJo  reason: not valid java name */
        public ClockMark m922plusLRDsOJo(double duration) {
            return new LongClockMark(this.startedAt, this.clock, Duration.m952plusLRDsOJo(this.offset, duration), null);
        }
    }

    @NotNull
    public ClockMark markNow() {
        return new LongClockMark(read(), this, Duration.Companion.getZERO(), null);
    }
}
