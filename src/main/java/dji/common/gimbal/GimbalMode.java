package dji.common.gimbal;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum GimbalMode {
    FREE(0),
    FPV(1),
    YAW_FOLLOW(2),
    UNKNOWN(255);
    
    private int value;

    private GimbalMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static GimbalMode find(int value2) {
        GimbalMode result = FREE;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
