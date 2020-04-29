package dji.sdksharedlib.hardware.abstractions.battery;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetDynamicData;
import dji.sdksharedlib.hardware.abstractions.battery.merge.M600MergeGetSingleStaticData;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class BatteryM600Abstraction extends BatterySmartAbstraction {
    private static String TAG = "DJISDKCacheM600BatteryAbstraction";

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        this.numberOfCell = 6;
        this.isSmartBattery = true;
        this.mergeGetDynamicData = new M600MergeGetDynamicData(index);
        this.mergeGetSingleStaticData = new M600MergeGetSingleStaticData(index);
    }
}
