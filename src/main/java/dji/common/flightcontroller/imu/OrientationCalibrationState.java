package dji.common.flightcontroller.imu;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum OrientationCalibrationState {
    UNKNOWN(255),
    CALIBRATING(0),
    COMPLETED(1);
    
    private int data;

    private OrientationCalibrationState(int data2) {
        this.data = data2;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static OrientationCalibrationState find(int b) {
        OrientationCalibrationState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                result = values()[i];
            }
        }
        return result;
    }
}
