package dji.common.util;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.Objects;

@EXClassNullAway
public class DJIParamMinMaxCapability extends DJIParamCapability {
    protected Number max;
    protected Number min;

    public DJIParamMinMaxCapability(boolean isSupported, Number min2, Number max2) {
        super(isSupported);
        this.min = min2;
        this.max = max2;
    }

    public Number getMin() {
        return this.min;
    }

    public Number getMax() {
        return this.max;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DJIParamMinMaxCapability)) {
            return false;
        }
        DJIParamMinMaxCapability that = (DJIParamMinMaxCapability) o;
        if (!Objects.equals(getMin(), that.getMin()) || !Objects.equals(getMax(), that.getMax())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(getMin(), getMax());
    }

    public String toString() {
        return "DJIParamMinMaxCapability{min=" + this.min + ", max=" + this.max + ", isSupported=" + this.isSupported + '}';
    }
}
