package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.gimbal.CapabilityKey;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class DJIGimbalPhantom3Abstraction extends DJIGimbalAircraftAbstraction {
    public void init(String parentPath, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        initGimbalCapability();
        super.init(parentPath, index, storeLayer, onValueChangeListener);
    }

    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_PITCH, -90, 30);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT, 0, 100);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SMOOTHING_FACTOR, 0, 30);
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }
}
