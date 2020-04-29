package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum CalibrationOrientation {
    UNKNOWN(255),
    NOSE_DOWN(0),
    TAIL_DOWN(1),
    RIGHT_DOWN(2),
    LEFT_DOWN(3),
    BOTTOM_DOWN(4),
    TOP_DOWN(5);
    
    private int data;

    private CalibrationOrientation(int data2) {
        this.data = data2;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static CalibrationOrientation find(int b) {
        CalibrationOrientation result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                result = values()[i];
            }
        }
        return result;
    }
}
