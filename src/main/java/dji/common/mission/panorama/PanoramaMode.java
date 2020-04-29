package dji.common.mission.panorama;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum PanoramaMode {
    FULL_CIRCLE(1),
    HALF_CIRCLE(3),
    UNKNOWN(255);
    
    private int value;

    private PanoramaMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public static PanoramaMode find(int value2) {
        PanoramaMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i].value == value2) {
                return values()[i];
            }
        }
        return result;
    }
}
