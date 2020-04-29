package dji.common.flightcontroller.virtualstick;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum RollPitchControlMode {
    ANGLE(0),
    VELOCITY(1);
    
    private int data;

    private RollPitchControlMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static RollPitchControlMode find(int b) {
        RollPitchControlMode result = ANGLE;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
