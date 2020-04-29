package dji.common.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum SSDCapacity {
    CAPACITY_256_GB(0),
    CAPACITY_512_GB(1),
    CAPACITY_1_TB(2),
    UNKNOWN(255);
    
    private int value;

    private SSDCapacity(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static SSDCapacity find(int value2) {
        SSDCapacity result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
