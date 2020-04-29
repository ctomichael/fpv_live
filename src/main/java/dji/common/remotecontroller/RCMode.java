package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum RCMode {
    MASTER(0),
    SLAVE(1),
    MASTER_SUB(2),
    SLAVE_SUB(3),
    NORMAL(10),
    UNKNOWN(15);
    
    private int value;

    private RCMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static RCMode find(int value2) {
        RCMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
