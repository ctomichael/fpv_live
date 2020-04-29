package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.BatteryKeys;

@EXClassNullAway
public class BatteryWM240Abstraction extends BatteryFoldingDroneAbstraction {
    @Setter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void setSelfDischargeInDays(Integer value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
    }
}
