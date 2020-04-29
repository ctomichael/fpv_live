package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum VisionSystemWarning {
    SAFE(0),
    DANGEROUS(3),
    INVALID(15),
    UNKNOWN(255);
    
    private int value;

    private VisionSystemWarning(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static VisionSystemWarning find(int value2) {
        VisionSystemWarning visionSystemWarning = UNKNOWN;
        switch (value2) {
            case 0:
            case 1:
            case 2:
                return SAFE;
            case 3:
                return DANGEROUS;
            case 15:
                return INVALID;
            default:
                return UNKNOWN;
        }
    }
}
