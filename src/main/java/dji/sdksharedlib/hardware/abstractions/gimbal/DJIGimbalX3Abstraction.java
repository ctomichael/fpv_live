package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.gimbal.CapabilityKey;
import dji.common.mission.waypoint.Waypoint;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIGimbalX3Abstraction extends DJIGimbalAircraftAbstraction {
    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_PITCH, -90, 30);
        addMinMaxToCapability(CapabilityKey.ADJUST_YAW, Integer.valueOf((int) Waypoint.MIN_HEADING), 180);
        addMinMaxToCapability(CapabilityKey.ADJUST_ROLL, -15, 15);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT, 0, 100);
        addMinMaxToCapability(CapabilityKey.YAW_CONTROLLER_SPEED_COEFFICIENT, 0, 100);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SMOOTHING_FACTOR, 0, 30);
        addMinMaxToCapability(CapabilityKey.YAW_CONTROLLER_SMOOTHING_FACTOR, 0, 30);
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }
}
