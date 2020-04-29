package dji.common.flightcontroller.flightassistant;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum FaceAwareState {
    IDLE(0),
    ACTIVATING(1),
    ACTIVATED(2),
    FAILED_NO_FACE_DETECTED(3),
    UNKNOWN(100);
    
    private final int data;

    public interface Callback {
        void onUpdate(FaceAwareState faceAwareState);
    }

    private FaceAwareState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static FaceAwareState find(int b) {
        FaceAwareState result = IDLE;
        FaceAwareState[] values = values();
        for (FaceAwareState tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
