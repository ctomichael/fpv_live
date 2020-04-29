package dji.midware.stat;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public abstract class StatBase {
    private final String name;

    public abstract void addEvent(double d);

    public abstract double getValue();

    public abstract double getValueAndReset();

    public StatBase(String name2) {
        if (name2 == null) {
            throw new RuntimeException("name should not be null");
        }
        this.name = name2;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof StatBase)) {
            return false;
        }
        return ((StatBase) obj).name.equals(this.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}
