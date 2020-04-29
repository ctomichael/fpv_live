package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;

@EXClassNullAway
public class DJINarrowBandModuleError extends DJIError {
    public static final DJINarrowBandModuleError NARROW_BAND_AUTHENTICATE_ERROR = new DJINarrowBandModuleError("Request Declined, authenticate code not correct");
    public static final DJINarrowBandModuleError NARROW_BAND_EXCHANGED_DECLINED = new DJINarrowBandModuleError("Narrow Band Exchange Declined, One Exchange is Doing");
    public static final DJINarrowBandModuleError NARROW_BAND_INSERTED = new DJINarrowBandModuleError("Narrow Band inserted");
    public static final DJINarrowBandModuleError NARROW_BAND_MASTER_ID_NOT_FOUND = new DJINarrowBandModuleError("Request Declined, can not found the request master");
    public static final DJINarrowBandModuleError NARROW_BAND_SLAVE_CHARACTER_OCCUPIED = new DJINarrowBandModuleError("Request Declined, the desire character has been occupied");

    private DJINarrowBandModuleError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(Ccode ccode) {
        switch (ccode.relValue()) {
            case 241:
                return NARROW_BAND_MASTER_ID_NOT_FOUND;
            case 242:
                return NARROW_BAND_AUTHENTICATE_ERROR;
            case 243:
                return NARROW_BAND_SLAVE_CHARACTER_OCCUPIED;
            case 244:
            case 245:
            case 246:
            default:
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
                        return DJIError.getDJIError(ccode);
                }
            case 247:
                return NARROW_BAND_EXCHANGED_DECLINED;
        }
    }
}
