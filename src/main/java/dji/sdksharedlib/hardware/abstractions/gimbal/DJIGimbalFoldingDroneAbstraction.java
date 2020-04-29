package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.gimbal.CapabilityKey;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIGimbalFoldingDroneAbstraction extends DJIGimbalAircraftAbstraction {
    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_PITCH, -90, 30);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT, 0, 50);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SMOOTHING_FACTOR, 0, 30);
        addIsSupportedToCapability(CapabilityKey.PITCH_RANGE_EXTENSION, true);
    }
}
