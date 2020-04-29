package dji.common.flightcontroller;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum CompassCalibrationState {
    NOT_CALIBRATING(4),
    HORIZONTAL(0),
    VERTICAL(1),
    SUCCESSFUL(2),
    FAILED(3),
    UNKNOWN(255);
    
    private int data;

    public interface Callback {
        void onUpdate(@NonNull CompassCalibrationState compassCalibrationState);
    }

    private CompassCalibrationState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static CompassCalibrationState find(int b) {
        CompassCalibrationState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
