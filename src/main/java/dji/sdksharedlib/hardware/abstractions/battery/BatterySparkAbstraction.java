package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class BatterySparkAbstraction extends BatteryFoldingDroneAbstraction {
    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
    }

    @Setter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void setSelfDischargeInDays(Integer value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
    }
}
