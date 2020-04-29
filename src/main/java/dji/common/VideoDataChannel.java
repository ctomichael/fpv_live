package dji.common;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum VideoDataChannel {
    FPV_CAMERA(0),
    HD_GIMBAL(1),
    HDMI(2),
    AV(3),
    UNKNOWN(255);
    
    private int value;

    private VideoDataChannel(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static VideoDataChannel find(int value2) {
        VideoDataChannel result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
