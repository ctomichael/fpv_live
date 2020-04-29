package dji.common.error;

public class DJIAccessLockerError extends DJIError {
    public static final DJIAccessLockerError ALREADY_UNLOCKED = new DJIAccessLockerError("The aircraft is already unlocked.");
    public static final DJIAccessLockerError FIRMWARE_READ_ERROR = new DJIAccessLockerError("Read failure when accessing data in the firmware.");
    public static final DJIAccessLockerError FIRMWARE_WRITE_ERROR = new DJIAccessLockerError("Write failure when updating data in the firmware.");
    public static final DJIAccessLockerError INVALID_STATE = new DJIAccessLockerError("The command is not valid in current state.");
    public static final DJIAccessLockerError NOT_SET_UP_ERROR = new DJIAccessLockerError("The user account is not set up for the security feature yet.");
    public static final DJIAccessLockerError SECURITY_CODE_FORMAT_INVALID = new DJIAccessLockerError("The new security code is not valid.A valid security code should contain only numbers and letters and its length is not less than 6 characters and not longer than 8 characters.");
    public static final DJIAccessLockerError SECURITY_CODE_INCORRECT = new DJIAccessLockerError("The security code is incorrect.");
    public static final DJIAccessLockerError SECURITY_CODE_INCORRECT_FIVE_TIMES = new DJIAccessLockerError("Attempt with wrong security codes more than 5 times. The aircraft is disable and try again in 1 minute.");
    public static final DJIAccessLockerError SECURITY_CODE_INCORRECT_TWENTY_TIMES = new DJIAccessLockerError("Attempt with wrong security codes more than 20 times. The aircraft is disable and try again in 24 hours.");
    public static final DJIAccessLockerError USERNAME_NOT_EXIST = new DJIAccessLockerError("The username does not exist.");

    protected DJIAccessLockerError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(int code) {
        switch (code) {
            case 241:
                return INVALID_STATE;
            case 242:
                return FIRMWARE_WRITE_ERROR;
            case 243:
                return FIRMWARE_READ_ERROR;
            case 244:
            case 245:
                return SECURITY_CODE_INCORRECT;
            case 246:
            default:
                return COMMON_UNKNOWN;
            case 247:
                return SECURITY_CODE_INCORRECT_FIVE_TIMES;
            case 248:
                return SECURITY_CODE_INCORRECT_TWENTY_TIMES;
        }
    }
}
