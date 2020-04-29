package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.error.DJIError;
import dji.common.gimbal.CapabilityKey;
import dji.common.gimbal.GimbalMode;
import dji.common.util.CallbackUtils;
import dji.midware.data.model.P3.DataGimbalControl;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;

public class DJIGimbalWM230Abstraction extends DJIGimbalAircraftAbstraction {
    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_PITCH, -90, 17);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT, 0, 100);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SMOOTHING_FACTOR, 0, 30);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_MAX_SPEED, 0, 100);
        addIsSupportedToCapability(CapabilityKey.PITCH_RANGE_EXTENSION, true);
    }

    /* access modifiers changed from: protected */
    public boolean checkGimbalModeValid(GimbalMode gimbalMode) {
        if (gimbalMode == null || gimbalMode == GimbalMode.FREE || gimbalMode == GimbalMode.UNKNOWN) {
            return false;
        }
        return true;
    }

    @Setter("Mode")
    public void setGimbalMode(GimbalMode gimbalMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!checkGimbalModeValid(gimbalMode)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataSpecialControl.getInstance().setGimbalMode(DataGimbalControl.MODE.find(gimbalMode.value())).start(20);
    }
}
