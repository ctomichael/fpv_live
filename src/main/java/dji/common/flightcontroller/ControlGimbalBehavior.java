package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum ControlGimbalBehavior {
    ONLY_LEFT_MOVE(0),
    ONLY_RIGHT_MOVE(1),
    BOTH_MOVE(2),
    UNKNOWN(255);
    
    private int data;

    private ControlGimbalBehavior(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static ControlGimbalBehavior find(int value) {
        ControlGimbalBehavior result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value)) {
                return values()[i];
            }
        }
        return result;
    }
}
