package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum PositioningSolution {
    NONE(0),
    SINGLE_POINT(16),
    FLOAT(34),
    FIXED_POINT(50),
    UNKNOWN(51);
    
    private int data;

    private PositioningSolution(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PositioningSolution find(int b) {
        PositioningSolution result = NONE;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
