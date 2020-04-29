package dji.common;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum LightbridgeSecondaryVideoFormat {
    RESOLUTION_720P_60FPS(0),
    RESOLUTION_720P_50FPS(1),
    RESOLUTION_1080I_60FPS(2),
    RESOLUTION_1080I_50FPS(3),
    RESOLUTION_1080P_60FPS(4),
    RESOLUTION_1080P_50FPS(5),
    RESOLUTION_1080P_30FPS(6),
    RESOLUTION_1080P_24FPS(7),
    RESOLUTION_1080P_25FPS(8),
    RESOLUTION_720P_30FPS(9),
    RESOLUTION_720P_25FPS(10),
    RESOLUTION_720P_24FPS(11),
    UNKNOWN(255);
    
    private int value;

    private LightbridgeSecondaryVideoFormat(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static LightbridgeSecondaryVideoFormat find(int value2) {
        LightbridgeSecondaryVideoFormat result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
