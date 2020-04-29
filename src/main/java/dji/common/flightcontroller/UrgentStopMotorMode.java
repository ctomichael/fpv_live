package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum UrgentStopMotorMode {
    CSC(0),
    NEVER(1),
    IN_OUT_ALWAYS(2),
    IN_OUT_WHEN_BREAKDOWN(3),
    UNKNOWN(255);
    
    private int data;

    private UrgentStopMotorMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static UrgentStopMotorMode find(int value) {
        UrgentStopMotorMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value)) {
                return values()[i];
            }
        }
        return result;
    }
}
