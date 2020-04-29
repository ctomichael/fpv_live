package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum OcuSyncBandwidth {
    Bandwidth20MHz(0),
    Bandwidth10MHz(1),
    Unknown(255);
    
    private int value;

    private OcuSyncBandwidth(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static OcuSyncBandwidth find(int value2) {
        OcuSyncBandwidth result = Unknown;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
