package dji.common.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum CameraRecordingState {
    NOT_RECORDING(0),
    PREPARING(1),
    RECORDING(2),
    STOPPING(3),
    RECORDING_TO_CACHE(4),
    UNKNOWN(7);
    
    private int data;

    private CameraRecordingState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static CameraRecordingState find(int b) {
        CameraRecordingState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
