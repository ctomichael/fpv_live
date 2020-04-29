package dji.common.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum ThermalMeasurementMode {
    DISABLED(0),
    SPOT_METERING(1),
    AREA_METERING(2),
    UNKNOWN(255);
    
    private int value;

    private ThermalMeasurementMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static ThermalMeasurementMode find(int value2) {
        ThermalMeasurementMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
