package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum VisionSensorPosition {
    NOSE(0),
    TAIL(1),
    RIGHT(2),
    LEFT(3),
    UNKNOWN(255);
    
    private final int value;

    private VisionSensorPosition(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static VisionSensorPosition find(int value2) {
        VisionSensorPosition result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
