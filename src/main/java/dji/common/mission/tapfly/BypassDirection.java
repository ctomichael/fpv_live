package dji.common.mission.tapfly;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum BypassDirection {
    NONE(0),
    RIGHT(1),
    LEFT(2),
    OVER(3),
    UNKNOWN(255);
    
    private int value;

    private BypassDirection(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static BypassDirection find(int value2) {
        BypassDirection result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
