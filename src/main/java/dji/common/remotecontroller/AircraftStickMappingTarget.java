package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum AircraftStickMappingTarget {
    THROTTLE(3),
    PITCH(2),
    ROLL(1),
    YAW(4),
    NONE(255);
    
    private int value;

    private AircraftStickMappingTarget(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static AircraftStickMappingTarget find(int value2) {
        AircraftStickMappingTarget result = NONE;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
