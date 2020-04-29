package dji.common.gimbal;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum Axis {
    PITCH(0),
    YAW(1),
    ROLL(2);
    
    private int value;

    private Axis(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static Axis find(int value2) {
        Axis result = PITCH;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
