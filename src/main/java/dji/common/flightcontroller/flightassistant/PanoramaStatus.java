package dji.common.flightcontroller.flightassistant;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum PanoramaStatus {
    END_NORMALLY(0),
    EXECUTING(1),
    ABORT_BY_EXCEPTION(2),
    UNKNOWN(255);
    
    private final int data;

    private PanoramaStatus(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PanoramaStatus find(int b) {
        PanoramaStatus result = UNKNOWN;
        PanoramaStatus[] values = values();
        for (PanoramaStatus tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
