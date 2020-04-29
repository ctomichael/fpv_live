package dji.common.flightcontroller.imu;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum CalibrationState {
    NONE(0),
    CALIBRATING(1),
    SUCCESSFUL(2),
    FAILED(3),
    UNKNOWN(255);
    
    private int data;

    private CalibrationState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static CalibrationState find(int b) {
        CalibrationState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }

    public static CalibrationState convertIMUCalibrationStatus(int state) {
        if (state == 12 || state == 80 || state == 81) {
            return SUCCESSFUL;
        }
        if (state == 0) {
            return NONE;
        }
        if (state <= 0 || state >= 12) {
            return FAILED;
        }
        return CALIBRATING;
    }
}
