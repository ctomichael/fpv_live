package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class DJIGimbalPhantom4PAbstraction extends DJIGimbalPhantom4Abstraction {
    public void init(String parentPath, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(parentPath, index, storeLayer, onValueChangeListener);
    }
}
