package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum AdvancedGoHomeState {
    NONE(0),
    TURNING_YAW(1),
    EXECUTING_GO_HOME(2),
    HOVERING_AT_SAFE_POINT(3),
    PLANNING(4),
    UNKNOWN(255);
    
    private int data;

    private AdvancedGoHomeState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static AdvancedGoHomeState find(int b) {
        AdvancedGoHomeState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
