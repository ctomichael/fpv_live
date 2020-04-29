package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum VisionDrawHeadingMode {
    FREE(0),
    FORWARD(1),
    UNKNOWN(255);
    
    private int value;

    private VisionDrawHeadingMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static VisionDrawHeadingMode find(int value2) {
        VisionDrawHeadingMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
