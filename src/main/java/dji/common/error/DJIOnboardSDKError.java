package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIOnboardSDKError extends DJIError {
    public static final DJIOnboardSDKError SDK_CONFIG_CFG_NULL = new DJIOnboardSDKError("Internal error.");
    public static final DJIOnboardSDKError SDK_CONFIG_FREQ_ERROR = new DJIOnboardSDKError("Invalid frequency: Frequencies range from 1Hz to 1kHz.");
    public static final DJIOnboardSDKError SDK_CONFIG_MODE_ERROR = new DJIOnboardSDKError("Invalid Mode: The mode does not exist or is not supported by the port.");
    public static final DJIOnboardSDKError SDK_CONFIG_PORT_ERROR = new DJIOnboardSDKError("The SDK port does not exist.");

    protected DJIOnboardSDKError(String desc) {
        super(desc);
    }

    public static DJIError getDJIOnboardSDKError(int ccode) {
        switch (ccode) {
            case 1:
                return SDK_CONFIG_CFG_NULL;
            case 2:
                return SDK_CONFIG_PORT_ERROR;
            case 3:
                return SDK_CONFIG_MODE_ERROR;
            case 4:
                return SDK_CONFIG_FREQ_ERROR;
            default:
                return COMMON_UNKNOWN;
        }
    }
}
