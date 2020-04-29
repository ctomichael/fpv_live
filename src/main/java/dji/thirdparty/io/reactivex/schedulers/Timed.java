package dji.thirdparty.io.reactivex.schedulers;

import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.TimeUnit;

public final class Timed<T> {
    final long time;
    final TimeUnit unit;
    final T value;

    public Timed(T value2, long time2, TimeUnit unit2) {
        this.value = value2;
        this.time = time2;
        this.unit = (TimeUnit) ObjectHelper.requireNonNull(unit2, "unit is null");
    }

    public T value() {
        return this.value;
    }

    public TimeUnit unit() {
        return this.unit;
    }

    public long time() {
        return this.time;
    }

    public long time(TimeUnit unit2) {
        return unit2.convert(this.time, this.unit);
    }

    public boolean equals(Object other) {
        if (!(other instanceof Timed)) {
            return false;
        }
        Timed<?> o = (Timed) other;
        if (!ObjectHelper.equals(this.value, o.value) || this.time != o.time || !ObjectHelper.equals(this.unit, o.unit)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((((this.value != null ? this.value.hashCode() : 0) * 31) + ((int) ((this.time >>> 31) ^ this.time))) * 31) + this.unit.hashCode();
    }

    public String toString() {
        return "Timed[time=" + this.time + ", unit=" + this.unit + ", value=" + ((Object) this.value) + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }
}
