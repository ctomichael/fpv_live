package dji.common.flightcontroller.flightassistant;

public enum TimeLapseException {
    NORMAL(0),
    EXITED_BY_STICK(1),
    RC_LOST(2),
    APP_LOST(3),
    CAN_NOT_TAKE_CONTROL(4),
    CTRL_LOST(5),
    FC_MODE_ERROR(6),
    FORBIDDEN(7),
    RADIUS_LIMIT(8),
    HEIGHT_LIMIT(9),
    HEIGHT_TOO_LOW(10),
    PLUS_SEC_FAILURE(11),
    LOAD_TASK_FAILURE(11),
    LOAD_TASK_FAILURE_GPS(11),
    LOAD_TASK_FAILURE_FAR(11),
    CAMERA_LOST(16),
    CAMERA_CUR_INDEX_ERROR(17),
    CAMERA_TOTAL_INDEX_ERROR(18),
    CAMERA_STATUS_ERROR(19),
    CAMERA_NO_SOURCE(20),
    CAMERA_PARAMETER_ERROR(21),
    CAMERA_UNKNOWN_ERROR(22),
    CAMERA_CAN_NOT_ENTRY(23),
    CAMERA_NO_MEMORY(24),
    PHOTOS_TOO_LITTLE(25),
    CAMERA_GEN_VIDEO_FAILURE(26),
    PATH_GENERATE_FAILURE(28),
    GIMBAL_ATTI_CHANGE_TOO_SMALL(29),
    GIMBAL_ATTI_OVERRANGE_LIMIT(30),
    ENCOUNTER_OBSTACLE(31),
    TOTAL_TIME_TOO_SHORT(32),
    TOTAL_TIME_TOO_LONG(33),
    PATH_TOO_SHORT(34),
    HAS_NOT_SET_ORIENTATION(36),
    UNKNOWN(255);
    
    private final int data;

    private TimeLapseException(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static TimeLapseException find(int b) {
        TimeLapseException result = UNKNOWN;
        TimeLapseException[] values = values();
        for (TimeLapseException tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
