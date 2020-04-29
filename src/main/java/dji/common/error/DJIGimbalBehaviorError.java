package dji.common.error;

import dji.midware.data.config.P3.Ccode;

public class DJIGimbalBehaviorError extends DJIError {
    public static final DJIGimbalBehaviorError UPPER_GIMBAL_NOT_SUPPORT = new DJIGimbalBehaviorError("Simultaneous gimbals control is not supported by the upper gimbal.");
    private static final int UPPER_GIMBAL_NOT_SUPPORT_ERROR_CODE = 224;

    protected DJIGimbalBehaviorError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(Ccode ccode) {
        if (ccode.relValue() == 224) {
            return UPPER_GIMBAL_NOT_SUPPORT;
        }
        return DJIError.getDJIError(ccode);
    }
}
