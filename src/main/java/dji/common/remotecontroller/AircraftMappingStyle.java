package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum AircraftMappingStyle {
    STYLE_1(1),
    STYLE_2(2),
    STYLE_3(3),
    STYLE_CUSTOM(4),
    UNKNOWN(255);
    
    private int value;

    private AircraftMappingStyle(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static AircraftMappingStyle find(int value2) {
        AircraftMappingStyle result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
