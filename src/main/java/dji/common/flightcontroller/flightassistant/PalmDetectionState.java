package dji.common.flightcontroller.flightassistant;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum PalmDetectionState {
    IDLE(0),
    RECOGNIZING(1),
    CONTROLLING(2),
    OTHER(100);
    
    private final int data;

    private PalmDetectionState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PalmDetectionState find(int b) {
        PalmDetectionState result = IDLE;
        PalmDetectionState[] values = values();
        for (PalmDetectionState tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
