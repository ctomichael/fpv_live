package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum ConnectionFailSafeBehavior {
    HOVER(0),
    LANDING(1),
    GO_HOME(2),
    UNKNOWN(255);
    
    private int data;

    private ConnectionFailSafeBehavior(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static ConnectionFailSafeBehavior find(int b) {
        ConnectionFailSafeBehavior result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
