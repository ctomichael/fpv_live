package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum LightbridgeSecondaryVideoOutputPort {
    HDMI(0),
    SDI(1),
    Unknown(255);
    
    private int value;

    private LightbridgeSecondaryVideoOutputPort(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static LightbridgeSecondaryVideoOutputPort find(int value2) {
        LightbridgeSecondaryVideoOutputPort result = Unknown;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
