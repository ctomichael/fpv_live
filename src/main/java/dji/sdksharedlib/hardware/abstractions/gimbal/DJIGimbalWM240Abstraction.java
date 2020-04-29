package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.gimbal.CapabilityKey;

public class DJIGimbalWM240Abstraction extends DJIGimbalWM230Abstraction {
    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_YAW, -80, 80);
        addMinMaxToCapability(CapabilityKey.ADJUST_ROLL, -20, 20);
    }
}
