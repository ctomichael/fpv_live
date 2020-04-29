package dji.common.mission.tapfly;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum TapFlyMode {
    FORWARD(0),
    BACKWARD(1),
    FREE(3),
    UNKNOWN(255);
    
    private final int value;

    private TapFlyMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static TapFlyMode find(int value2) {
        TapFlyMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
