package dji.common.error;

public class DJIDataUpgradeError extends DJIError {
    public static final DJIDataUpgradeError COMMON_ERROR = new DJIDataUpgradeError("Database upgrade common error", -1);
    public static final DJIDataUpgradeError NO_ERROR = new DJIDataUpgradeError("no error", 0);

    protected DJIDataUpgradeError(String desc) {
        super(desc);
    }

    private DJIDataUpgradeError(String desc, int errorCode) {
        super(desc, errorCode);
    }

    public static DJIError getDJIErrorByCode(int code) {
        if (code != 0) {
            return COMMON_ERROR;
        }
        return NO_ERROR;
    }
}
