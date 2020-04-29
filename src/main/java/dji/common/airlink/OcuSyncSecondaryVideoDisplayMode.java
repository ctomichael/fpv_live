package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum OcuSyncSecondaryVideoDisplayMode {
    SOURCE_1_ONLY(0),
    SOURCE_2_ONLY(1),
    SOURCE_1_MAIN(2),
    SOURCE_2_MAIN(3),
    UNKNOWN(4);
    
    private int value;

    private OcuSyncSecondaryVideoDisplayMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static OcuSyncSecondaryVideoDisplayMode find(int value2) {
        OcuSyncSecondaryVideoDisplayMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
