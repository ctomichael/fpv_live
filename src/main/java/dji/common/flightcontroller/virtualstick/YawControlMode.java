package dji.common.flightcontroller.virtualstick;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum YawControlMode {
    ANGLE(0),
    ANGULAR_VELOCITY(1);
    
    private int data;

    private YawControlMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static YawControlMode find(int b) {
        YawControlMode result = ANGLE;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
