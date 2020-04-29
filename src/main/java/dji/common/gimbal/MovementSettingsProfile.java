package dji.common.gimbal;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum MovementSettingsProfile {
    CUSTOM_1(0),
    CUSTOM_2(1),
    FAST(3),
    MEDIUM(4),
    SLOW(5),
    UNKNOWN(255);
    
    private int value;

    private MovementSettingsProfile(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static MovementSettingsProfile find(int value2) {
        MovementSettingsProfile result = CUSTOM_1;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
