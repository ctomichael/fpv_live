package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum GimbalAxis {
    PITCH(0),
    ROLL(1),
    YAW(2);
    
    private int value;

    private GimbalAxis(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static GimbalAxis find(int value2) {
        GimbalAxis result = PITCH;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
