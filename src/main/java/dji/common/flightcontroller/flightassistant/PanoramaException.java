package dji.common.flightcontroller.flightassistant;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum PanoramaException {
    NORMAL(0),
    AIRCRAFT_ERROR(1),
    GIMBAL_ERROR(2),
    LIGHT_NEEDED(3),
    ABORT_BY_FLYC_LOGIC(4),
    ABORT_BY_APP(5),
    UNKNOWN(255);
    
    private final int data;

    private PanoramaException(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PanoramaException find(int b) {
        PanoramaException result = UNKNOWN;
        PanoramaException[] values = values();
        for (PanoramaException tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
