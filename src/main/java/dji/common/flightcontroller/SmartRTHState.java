package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;

@EXClassNullAway
public enum SmartRTHState {
    IDLE,
    COUNTING_DOWN,
    EXECUTED,
    CANCELLED,
    UNKNOWN;

    public static SmartRTHState getSmartRTHStateFromBattery(DataFlycGetPushSmartBattery params) {
        if (params == null) {
            return UNKNOWN;
        }
        switch (params.getGoHomeStatus().value()) {
            case 0:
                if (params.getGoHomeCountDown() > 0) {
                    return COUNTING_DOWN;
                }
                return IDLE;
            case 1:
                return EXECUTED;
            case 2:
                return CANCELLED;
            default:
                return UNKNOWN;
        }
    }
}
