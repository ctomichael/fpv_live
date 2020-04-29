package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.midware.data.config.P3.Ccode;

@EXClassNullAway
public class DJIError {
    public static final DJIError BATTERY_GET_SMART_BATTERY_INFO_FAILED = new DJIError("Get smart battery info failed");
    public static final DJIError BATTERY_PAIR_FAILED = new DJIError("Unable to pair the batteries");
    public static final DJIError CANNOT_PAUSE_STABILIZATION = new DJIError("Can't pause stabilization.");
    public static final DJIError COMMAND_NOT_SUPPORTED_BY_FIRMWARE = new DJIError("The command is not supported by the current firmware version.");
    public static final DJIError COMMAND_NOT_SUPPORTED_BY_HARDWARE = new DJIError("The command is not supported by the current hardware.");
    public static final DJIError COMMON_DISCONNECTED = new DJIError("Disconnected");
    public static final DJIError COMMON_EXECUTION_FAILED = new DJIError("The execution could not be executed.");
    public static final DJIError COMMON_PARAM_ILLEGAL = new DJIError("Param Illegal");
    public static final DJIError COMMON_PARAM_INVALID = new DJIError("Param Invalid");
    public static final DJIError COMMON_SYSTEM_BUSY = new DJIError("The system is too busy to execute the action");
    public static final DJIError COMMON_TIMEOUT = new DJIError("Execution of this process has timed out");
    public static final DJIError COMMON_UNDEFINED = new DJIError("Undefined Error");
    public static final DJIError COMMON_UNKNOWN = new DJIError("SDK error, please contact <dev@dji.com> for help.");
    public static final DJIError COMMON_UNSUPPORTED = new DJIError("Not supported");
    public static final DJIError DATABASE_IS_NOT_READY = new DJIError("Database is not ready");
    public static final DJIError FIRMWARE_CRC_WRONG = new DJIError("Firmware crc value invalid");
    public static final DJIError FIRMWARE_LENGTH_WRONG = new DJIError("Firmware length invalid");
    public static final DJIError FIRMWARE_MATCH_WRONG = new DJIError("Firmware match error ");
    public static final DJIError FIRMWARE_NON_SEQUENCE = new DJIError("Firmware pattern number not continuous");
    public static final DJIError FLASH_CLEAR_WRONG = new DJIError("Flash clear error ");
    public static final DJIError FLASH_FLUSHING = new DJIError("Firmware flushing");
    public static final DJIError FLASH_WRITE_WRONG = new DJIError("Flash write error ");
    public static final DJIError IMAGE_TRANSMITTER_INVALID_PARAMETER = new DJIError("The input parameter is out of bound or invalid.");
    public static final DJIError MEDIA_INVALID_REQUEST_TYPE = new DJIError("Media download result: media downloading request type is invalid");
    public static final DJIError MEDIA_NO_SUCH_FILE = new DJIError("Media download result: no such file");
    public static final DJIError MEDIA_REQUEST_CLIENT_ABORT = new DJIError("Media download result: the client aborts the downloading");
    public static final DJIError MEDIA_REQUEST_DISCONNECT = new DJIError("Media download result: the downloading link disconnects");
    public static final DJIError MEDIA_REQUEST_SERVER_ABORT = new DJIError("Media download result: the server aborts the downloading");
    public static final DJIError NO_NETWORK = new DJIError("No network");
    public static final DJIError SET_PARAM_FAILED = new DJIError("set param failed");
    public static final DJIError UNABLE_TO_GET_FIRMWARE_VERSION = new DJIError("Unable to get the firmware version. Note: The sdk will fetch the firmware version from the server so, please ensure you have Internet connectivity before you invoke getVersion().");
    public static final DJIError UNABLE_TO_GET_FLAGS = new DJIError("Unable to get the analytics flags from server. Please ensure you have Internet connectivity before you invoke this method.");
    public static final DJIError UNABLE_TO_GET_FLAG_BUT_RETRY = new DJIError("Unable to get the analytics flags from server, but retrying.  Please ensure you have Internet connectivity before invoking this method");
    public static final DJIError UPDATE_WRONG = new DJIError("Update error");
    private String description;
    private int errorCode = 255;

    protected DJIError(String desc) {
        this.description = desc;
    }

    protected DJIError(String desc, int errorCode2) {
        this.description = desc;
        this.errorCode = errorCode2;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public static DJIError getDJIError(Ccode ccode) {
        switch (ccode) {
            case TIMEOUT:
                return COMMON_TIMEOUT;
            case INVALID_CMD:
                return COMMON_UNSUPPORTED;
            case TIMEOUT_REMOTE:
                return COMMON_TIMEOUT;
            case OUT_OF_MEMORY:
                return COMMON_UNKNOWN;
            case INVALID_PARAM:
                return COMMON_PARAM_ILLEGAL;
            case NOT_SUPPORT_CURRENT_STATE:
                return COMMON_UNSUPPORTED;
            case TIME_NOT_SYNC:
                return COMMON_UNKNOWN;
            case SET_PARAM_FAILED:
                return SET_PARAM_FAILED;
            case UNDEFINED:
                return COMMON_UNDEFINED;
            default:
                return COMMON_UNKNOWN;
        }
    }

    public static DJIError getDJIError(DJIAlbumPullErrorType code) {
        switch (code) {
            case ERROR_REQ:
                return MEDIA_INVALID_REQUEST_TYPE;
            case NO_SUCH_FILE:
                return MEDIA_NO_SUCH_FILE;
            case DATA_NOMATCH:
                return COMMON_UNKNOWN;
            case TIMEOUT:
                return COMMON_TIMEOUT;
            case CLIENT_ABORT:
                return MEDIA_REQUEST_CLIENT_ABORT;
            case SERVER_ABORT:
                return MEDIA_REQUEST_SERVER_ABORT;
            case DISCONNECT:
                return MEDIA_REQUEST_DISCONNECT;
            case UNKNOW:
                return COMMON_UNKNOWN;
            default:
                return COMMON_UNKNOWN;
        }
    }

    public String toString() {
        if (this.description != null) {
            return this.description;
        }
        return super.toString();
    }
}
