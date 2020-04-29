package dji.common.battery;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum LowVoltageBehavior {
    FLASH_LED(0),
    GO_HOME(1),
    LAND(2),
    UNKNOWN(255);
    
    private int data;

    private LowVoltageBehavior(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static LowVoltageBehavior find(int value) {
        LowVoltageBehavior result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value)) {
                return values()[i];
            }
        }
        return result;
    }
}
