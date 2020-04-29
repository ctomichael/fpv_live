package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum CalibrationState {
    NORMAL(0),
    MIDDLE(1),
    LIMITS(2),
    QUIT(3),
    TIMEOUT(4),
    OTHER(6),
    DISCONNECT(255);
    
    private int data;

    private CalibrationState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static CalibrationState find(int b) {
        CalibrationState result = OTHER;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
