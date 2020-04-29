package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;

@EXClassNullAway
public class DJIAirLinkError extends DJIError {
    public static final DJIAirLinkError IMAGE_TRANSMITTER_CANNOT_SET_PARAMETERS_IN_THIS_STATE = new DJIAirLinkError("Cannot set the parameters in this state");
    public static final DJIAirLinkError IMAGE_TRANSMITTER_INVALID_PARAMETER = new DJIAirLinkError("The input parameter is out of bound or invalid.");

    private DJIAirLinkError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(Ccode ccode) {
        if (COMMON_UNKNOWN != DJIError.getDJIError(ccode)) {
            return DJIError.getDJIError(ccode);
        }
        switch (ccode) {
            case TIMEOUT:
                return COMMON_TIMEOUT;
            default:
                return COMMON_UNKNOWN;
        }
    }
}
