package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum LightbridgeSecondaryVideoDisplayMode {
    SOURCE_1_ONLY(0),
    SOURCE_2_ONLY(1),
    SOURCE_1_MAIN(2),
    SOURCE_2_MAIN(3),
    UNKNOWN(4);
    
    private int value;

    private LightbridgeSecondaryVideoDisplayMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static LightbridgeSecondaryVideoDisplayMode find(int value2) {
        LightbridgeSecondaryVideoDisplayMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
