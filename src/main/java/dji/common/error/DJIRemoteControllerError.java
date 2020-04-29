package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;

@EXClassNullAway
public class DJIRemoteControllerError extends DJIError {
    public static final DJIRemoteControllerError FIRMWARE_CRC_WRONG = new DJIRemoteControllerError("Firmware CRC value invalid");
    public static final DJIRemoteControllerError FIRMWARE_LENGTH_WRONG = new DJIRemoteControllerError("Firmware length invalid");
    public static final DJIRemoteControllerError FIRMWARE_MATCH_ERROR = new DJIRemoteControllerError("Firmware match error");
    public static final DJIRemoteControllerError FIRMWARE_NON_SEQUENCE = new DJIRemoteControllerError("Firmware not pattern");
    public static final DJIRemoteControllerError FLASH_CLEAR_WRONG = new DJIRemoteControllerError("Flash clear error");
    public static final DJIRemoteControllerError FLASH_FLUSHING = new DJIRemoteControllerError("Firmware flushing");
    public static final DJIRemoteControllerError FLASH_WRITE_WRONG = new DJIRemoteControllerError("Flash write error");
    public static final DJIRemoteControllerError UPDATE_WRONG = new DJIRemoteControllerError("Update error");

    private DJIRemoteControllerError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(Ccode ccode) {
        if (COMMON_UNKNOWN != DJIError.getDJIError(ccode)) {
            return DJIError.getDJIError(ccode);
        }
        switch (ccode) {
            case TIMEOUT:
                return COMMON_TIMEOUT;
            case INVALID_CMD:
                return COMMON_UNKNOWN;
            case TIMEOUT_REMOTE:
                return COMMON_TIMEOUT;
            case OUT_OF_MEMORY:
                return COMMON_UNKNOWN;
            case INVALID_PARAM:
                return COMMON_PARAM_ILLEGAL;
            case NOT_SUPPORT_CURRENT_STATE:
                return COMMON_UNKNOWN;
            case TIME_NOT_SYNC:
                return COMMON_UNKNOWN;
            case FM_NONSEQUENCE:
                return FIRMWARE_NON_SEQUENCE;
            case FM_LENGTH_WRONG:
                return FIRMWARE_LENGTH_WRONG;
            case FM_CRC_WRONG:
                return FIRMWARE_CRC_WRONG;
            case FLASH_C_WRONG:
                return FLASH_CLEAR_WRONG;
            case FLASH_W_WRONG:
                return FLASH_WRITE_WRONG;
            case UPDATE_WRONG:
                return UPDATE_WRONG;
            case FLASH_FLUSHING:
                return FLASH_FLUSHING;
            case UNDEFINED:
                return COMMON_UNDEFINED;
            default:
                return COMMON_UNKNOWN;
        }
    }
}
