package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.error.DJIError;
import dji.common.gimbal.CapabilityKey;
import dji.common.gimbal.GimbalMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataGimbalControl;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;

@EXClassNullAway
public class DJIGimbalSparkAbstraction extends DJIGimbalAircraftAbstraction {
    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_PITCH, -85, 0);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT, 0, 100);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SMOOTHING_FACTOR, 0, 30);
        addIsSupportedToCapability(CapabilityKey.PITCH_RANGE_EXTENSION, true);
    }

    @Setter("Mode")
    public void setGimbalMode(GimbalMode gimbalMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (gimbalMode == null || gimbalMode.equals(GimbalMode.UNKNOWN) || gimbalMode == GimbalMode.FREE) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataSpecialControl.getInstance().setGimbalMode(DataGimbalControl.MODE.find(gimbalMode.value())).start(20);
    }
}
