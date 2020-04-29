package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;

@EXClassNullAway
public class DJICameraError extends DJIError {
    public static final DJICameraError CANNOT_SET_PARAMETERS_IN_THIS_STATE = new DJICameraError("Cannot set the parameters in this state");
    public static final DJICameraError CHECK_PERMISSION_LEVEL1_IS_INVALID = new DJICameraError("Level 1 API permission is invalid");
    public static final DJICameraError COULD_NOT_DELETE_ALL_FILES = new DJICameraError("Could not delete all files");
    public static final DJICameraError EXEC_TIMEOUT = new DJICameraError("Camera's execution of this action has timed out");
    public static final DJICameraError GET_REMOTE_MEDIA_FAILED = new DJICameraError("Get remote media failed");
    public static final DJICameraError GET_THUMBNAIL_FAILED = new DJICameraError("Failed to get the thumbnail");
    public static final DJICameraError INVALID_PARAMETERS = new DJICameraError("Camera received invalid parameters");
    public static final DJICameraError MEDIA_FILE_RESET = new DJICameraError("Media file is reset. The operation cannot be executed.");
    public static final DJICameraError MEDIA_INVALID_REQUEST_TYPE = new DJICameraError("Media download result: media downloading request type is invalid");
    public static final DJICameraError MEDIA_NO_SUBMEDIA_FILES = new DJICameraError("Sub media fetching result: No sub media files");
    public static final DJICameraError MEDIA_REQUEST_CLIENT_ABORT = new DJICameraError("Media download result: the client aborted the download");
    public static final DJICameraError MEDIA_REQUEST_DISCONNECT = new DJICameraError("Media download result: the download link disconnected");
    public static final DJICameraError MEDIA_REQUEST_SERVER_ABORT = new DJICameraError("Media download result: the server aborted the download");
    public static final DJICameraError NOT_CONNECTED = new DJICameraError("Connection is not ok");
    public static final DJICameraError NO_SUCH_MEDIA_FILE = new DJICameraError("Media download result: no such file");
    public static final DJICameraError PARAMETERS_GET_FAILED = new DJICameraError("Camera param get failed");
    public static final DJICameraError PARAMETERS_NOT_AVAILABLE = new DJICameraError("Camera received invalid parameters");
    public static final DJICameraError PARAMETERS_SET_FAILED = new DJICameraError("Camera failed to set the parameters it received");
    public static final DJICameraError SD_CARD_ERROR = new DJICameraError("Error accessing the SD Card");
    public static final DJICameraError SD_CARD_FULL = new DJICameraError("The Camera's SD Card is full");
    public static final DJICameraError SD_CARD_NOT_INSERTED = new DJICameraError("Camera has no SD Card");
    public static final DJICameraError SENSOR_ERROR = new DJICameraError("Camera sensor error");
    public static final DJICameraError SYSTEM_ERROR = new DJICameraError("Camera system error ");
    public static final DJICameraError UNKNOWN_ERROR = new DJICameraError("Server error, please contact <dev@dji.com> for help.");
    public static final DJICameraError UNSUPPORTED_CMD_STATE = new DJICameraError("Camera is busy or the command is not supported in the Camera's current state");

    private DJICameraError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(Ccode ccode) {
        if (COMMON_UNKNOWN != DJIError.getDJIError(ccode)) {
            return DJIError.getDJIError(ccode);
        }
        switch (ccode) {
            case SET_PARAM_FAILED:
                return PARAMETERS_SET_FAILED;
            case GET_PARAM_FAILED:
                return PARAMETERS_GET_FAILED;
            case SDCARD_NOT_INSERTED:
                return SD_CARD_ERROR;
            case SDCARD_FULL:
                return SD_CARD_FULL;
            case SDCARD_ERR:
                return SD_CARD_ERROR;
            case CAMERA_CRITICAL_ERR:
                return UNKNOWN_ERROR;
            case PARAM_NOT_AVAILABLE:
                return PARAMETERS_NOT_AVAILABLE;
            case INVALID_PARAM:
                return INVALID_PARAMETERS;
            case NOT_SUPPORT_CURRENT_STATE:
                return UNSUPPORTED_CMD_STATE;
            default:
                return UNKNOWN_ERROR;
        }
    }
}
