package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;

@EXClassNullAway
public class DJIGimbalError extends DJIError {
    public static final DJIGimbalError CANNOT_SET_PARAMETERS_IN_THIS_STATE = new DJIGimbalError("Cannot set the parameters in this state");
    public static final DJIGimbalError RESULT_FAILED = new DJIGimbalError("Failed");

    private DJIGimbalError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(Ccode ccode) {
        if (COMMON_UNKNOWN != DJIError.getDJIError(ccode)) {
            return DJIError.getDJIError(ccode);
        }
        switch (ccode) {
            case TIMEOUT:
                return COMMON_UNKNOWN;
            default:
                return COMMON_UNKNOWN;
        }
    }
}
