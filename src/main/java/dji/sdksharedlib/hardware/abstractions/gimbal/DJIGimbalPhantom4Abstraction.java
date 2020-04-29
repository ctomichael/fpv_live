package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.gimbal.CapabilityKey;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class DJIGimbalPhantom4Abstraction extends DJIGimbalPhantom3Abstraction {
    public void init(String parentPath, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(parentPath, index, storeLayer, onValueChangeListener);
        initGimbalCapability();
    }

    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_YAW, -15, 15);
        addIsSupportedToCapability(CapabilityKey.PITCH_RANGE_EXTENSION, true);
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }
}
