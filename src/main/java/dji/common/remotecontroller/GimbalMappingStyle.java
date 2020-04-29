package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum GimbalMappingStyle {
    DEFAULT(0),
    CUSTOM(1),
    UNKNOWN(255);
    
    private int value;

    private GimbalMappingStyle(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static GimbalMappingStyle find(int value2) {
        GimbalMappingStyle result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
