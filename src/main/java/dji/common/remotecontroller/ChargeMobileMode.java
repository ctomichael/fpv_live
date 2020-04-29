package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum ChargeMobileMode {
    NEVER(0),
    ALWAYS(1),
    INTELLIGENT(2),
    Unknown(3);
    
    private int value;

    private ChargeMobileMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static ChargeMobileMode find(int value2) {
        ChargeMobileMode result = Unknown;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
