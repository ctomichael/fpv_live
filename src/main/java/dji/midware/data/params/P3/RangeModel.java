package dji.midware.data.params.P3;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class RangeModel {
    public Number defaultValue;
    public Number maxValue;
    public Number minValue;

    public RangeModel() {
    }

    public RangeModel(Number minValue2, Number maxValue2, Number defaultValue2) {
        this.minValue = minValue2;
        this.maxValue = maxValue2;
        this.defaultValue = defaultValue2;
    }

    public String toString() {
        return "minValue=" + this.minValue + " maxValue=" + this.maxValue + " defaultValue=" + this.defaultValue;
    }
}
