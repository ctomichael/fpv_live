package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum CompassSensorState {
    UNKNOWN(255),
    DISCONNECTED(1),
    CALIBRATING(2),
    UNCALIBRATED(3),
    DATA_EXCEPTION(4),
    NORMAL_MODULUS(5),
    WEAK_MODULUS(6),
    SERIOUS_MODULUS(7),
    CALIBRATION_FAILED(8),
    DIRECTION_EXCEPTION(9);
    
    private int data;

    private CompassSensorState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static CompassSensorState find(int b) {
        CompassSensorState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
