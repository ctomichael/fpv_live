package dji.common.flightcontroller.imu;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum SensorState {
    UNKNOWN(255),
    DISCONNECTED(1),
    CALIBRATING(2),
    CALIBRATION_FAILED(3),
    DATA_EXCEPTION(4),
    WARMING_UP(5),
    IN_MOTION(6),
    NORMAL_BIAS(7),
    MEDIUM_BIAS(8),
    LARGE_BIAS(9);
    
    private int data;

    private SensorState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static SensorState find(int b) {
        SensorState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
