package dji.common.error;

import dji.midware.data.config.P3.Ccode;

public class DJIAccessoryAggregationError extends DJIError {
    public static final DJIAccessoryAggregationError ACCESSORY_NOT_CONNECTED_ERROR = new DJIAccessoryAggregationError("The accessory is not connected.");
    public static final DJIAccessoryAggregationError CANCELED_BY_USER = new DJIAccessoryAggregationError("The ongoing data transmission is cancelled.");
    public static final DJIAccessoryAggregationError DATA_CORRUPTION_ERROR = new DJIAccessoryAggregationError("Data validation failed. Data is corrupted during the transmission.");
    public static final DJIAccessoryAggregationError DATA_TRANSMISSION_DISCONNECTION_ERROR = new DJIAccessoryAggregationError("The connection of the speaker is broken and data transmission cannot start.");
    public static final DJIAccessoryAggregationError DUPLICATION_OF_FILE_NAME = new DJIAccessoryAggregationError("The file name is already taken in the aircraft. Choose a different file name.");
    public static final DJIAccessoryAggregationError FILE_CREATED_ERROR = new DJIAccessoryAggregationError("An error occurs when creating the file.");
    public static final DJIAccessoryAggregationError FILE_INDEX_UNAVAIABLE_ERROR = new DJIAccessoryAggregationError("There is no more file index available for the file.");
    public static final DJIAccessoryAggregationError FILE_NAME_EMPTY_ERROR = new DJIAccessoryAggregationError("A file name cannot be empty.");
    public static final DJIAccessoryAggregationError FILE_NAME_LENGTH_INVALID = new DJIAccessoryAggregationError("The file name exceeds the maximum length (20 characters).");
    public static final DJIAccessoryAggregationError FILE_NOT_EXIST_ERROR = new DJIAccessoryAggregationError("The file does not exist.");
    public static final DJIAccessoryAggregationError INTERRUPTED_BY_TIMEOUT_ERROR = new DJIAccessoryAggregationError("The ongoing data transmission is interrupted by timeout error.");
    public static final DJIAccessoryAggregationError REJECTED_BY_FIRMWARE = new DJIAccessoryAggregationError("The operation is rejected by the firmware because state error.");
    public static final DJIAccessoryAggregationError RENAME_FILE_ERROR = new DJIAccessoryAggregationError("Error occurs when renaming the file.");
    public static final DJIAccessoryAggregationError STORAGE_FULL = new DJIAccessoryAggregationError("The storage is full.");
    public static final DJIAccessoryAggregationError WRONG_DATA_TRANSIMISSION_STATE = new DJIAccessoryAggregationError("The data transimission operation cannot be executed in the current state.");

    private DJIAccessoryAggregationError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(Ccode ccode) {
        switch (ccode) {
            case SENSOR_ERR:
                return DUPLICATION_OF_FILE_NAME;
            case FM_LENGTH_WRONG:
                return STORAGE_FULL;
            case UPDATE_WRONG:
                return FILE_CREATED_ERROR;
            case FIRM_MATCH_WRONG:
                return ACCESSORY_NOT_CONNECTED_ERROR;
            case CAMERA_CRITICAL_ERR:
                return FILE_INDEX_UNAVAIABLE_ERROR;
            case PARAM_NOT_AVAILABLE:
                return FILE_NOT_EXIST_ERROR;
            case FLASH_W_WRONG:
                return RENAME_FILE_ERROR;
            case FM_CRC_WRONG:
                return DATA_CORRUPTION_ERROR;
            case USER_CANCEL:
                return CANCELED_BY_USER;
            case NOT_SUPPORT_CURRENT_STATE:
                return REJECTED_BY_FIRMWARE;
            default:
                if (COMMON_UNKNOWN != DJIError.getDJIError(ccode)) {
                    return DJIError.getDJIError(ccode);
                }
                return COMMON_UNKNOWN;
        }
    }
}
