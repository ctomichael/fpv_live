package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.error.DJIError;
import dji.common.gimbal.GimbalMode;
import dji.common.util.CallbackUtils;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

public class DJIGimbalPhantom4PSDRAbstraction extends DJIGimbalPhantom4PAbstraction {
    public void setGimbalMode(GimbalMode gimbalMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (gimbalMode == GimbalMode.FREE) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            super.setGimbalMode(gimbalMode, callback);
        }
    }
}
