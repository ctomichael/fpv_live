package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WiFiMagneticInterferenceLevel {
    LOW(0),
    MEDIUM(1),
    HIGH(2),
    UNKNOWN(100);
    
    private int value;

    private WiFiMagneticInterferenceLevel(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static WiFiMagneticInterferenceLevel find(int value2) {
        WiFiMagneticInterferenceLevel result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
