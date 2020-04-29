package dji.common.flightcontroller.virtualstick;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum VerticalControlMode {
    VELOCITY(0),
    POSITION(1);
    
    private int data;

    private VerticalControlMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static VerticalControlMode find(int b) {
        VerticalControlMode result = VELOCITY;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
