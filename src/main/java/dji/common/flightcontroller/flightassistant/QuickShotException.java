package dji.common.flightcontroller.flightassistant;

public enum QuickShotException {
    NONE(0),
    QM_NORMAL_COMEBACK(1),
    QM_FLYLIMIT_COMEBACK(2),
    QM_DISTANCE_COMEBACK(3),
    QM_TIMEOUT_COMEBACK(4),
    QM_GPS_UNVALID_COMEBACK(5),
    QM_RC_INTERRUPT_COMEBACK(6),
    QM_INIT_FAILED_COMEBACK(7),
    QM_CAMERA_WRONG_COMEBACK(8),
    QM_NOT_IN_AIR(9),
    QM_FC_MODE_ERROR(10),
    QM_OBSTACLE_AVOID(11),
    QM_USER_PAUSE(12),
    PANO_SHOT_GIMBAL_STUCK(13),
    PANO_SHOT_FAIL_TAKE_CONTROL(14),
    PANO_SHOT_STORAGE_NOT_ENOUGH(15),
    TRACKIING_SHOT_TARGET_LOST(16),
    PANO_SHOT_TAKE_PHOTO_FAILED(17),
    PANO_SHOT_PHOTO_STITCHING_FAILED(18),
    PANO_SHOT_LOADING_CALIBRATION_FAILED(19),
    PANO_SHOT_ADJUST_CAMERA_PARAMETER_FAILED(20),
    PANO_SHOT_TIME_OUT(21),
    UNKNOWN(255);
    
    int data;

    private QuickShotException(int value) {
        this.data = value;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static QuickShotException find(int b) {
        QuickShotException result = UNKNOWN;
        QuickShotException[] values = values();
        for (QuickShotException tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
