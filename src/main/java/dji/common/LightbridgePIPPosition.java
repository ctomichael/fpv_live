package dji.common;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum LightbridgePIPPosition {
    TOP_LEFT(0),
    TOP_RIGHT(1),
    BOTTOM_LEFT(2),
    BOTTOM_RIGHT(3),
    UNKNOWN(255);
    
    private int value;

    private LightbridgePIPPosition(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static LightbridgePIPPosition find(int value2) {
        LightbridgePIPPosition result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
