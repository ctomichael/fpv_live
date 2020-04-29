package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIBatteryError extends DJIError {
    public static final DJIBatteryError GET_SMART_BATTERY_INFO_FAILED = new DJIBatteryError("Get smart battery info failed");
    public static final DJIBatteryError UPDATE_WRONG = new DJIBatteryError("Update error");

    private DJIBatteryError(String desc) {
        super(desc);
    }
}
