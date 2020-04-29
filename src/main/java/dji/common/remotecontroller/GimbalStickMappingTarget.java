package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum GimbalStickMappingTarget {
    NONE(0),
    PITCH(1),
    ROLL(2),
    YAW(3);
    
    private int value;

    private GimbalStickMappingTarget(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static GimbalStickMappingTarget find(int value2) {
        GimbalStickMappingTarget result = NONE;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
