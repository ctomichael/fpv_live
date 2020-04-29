package dji.common.flightcontroller.flightassistant;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum PalmControlState {
    FOLLOWING(1),
    STARTING_FOLLOW(2),
    ADJUSTING_POSITION(3),
    PALM_CONTROL_STOPPED(4),
    INITIALIZING(5),
    RECOGNIZING_PALM(6),
    UNKNOWN(255);
    
    private final int data;

    public interface Callback {
        void onUpdate(PalmControlState palmControlState);
    }

    private PalmControlState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PalmControlState find(int b) {
        PalmControlState result = UNKNOWN;
        PalmControlState[] values = values();
        for (PalmControlState tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
