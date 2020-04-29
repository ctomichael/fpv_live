package dji.common.flightcontroller.flightassistant;

public enum PoiException {
    NONE(0),
    PER_CONF_VISION_ERR(1),
    PER_CONF_SYSTEM_ERR(2),
    PER_CONF_TARGET_TOO_SMALL(3),
    PER_CONG_NO_ENOUGH_FEATURE(4),
    PER_CONF_TRACKING_LOST(5),
    PER_ESTI_VISION_ERR(6),
    PER_ESTI_TRACKING_LOST(7),
    PER_ESTI_RE_PROJ_ERR(8),
    PER_ESTI_OUT_OF_RANGE(9),
    PER_ESTI_IMAGE_SZ_CHANGE(10),
    STA_NAVIGATION_ERR(11),
    STA_GPS_ERR(12),
    PRO_PERC_CONN_TIMEOUT(13),
    PRO_HOT_POINT_INVALID(14),
    PRO_WATCH_FAILED(15),
    PRO_ESTIMATE_TIMEOUT(16),
    PRO_ESTIMATE_RES_INVALID(17),
    PRO_PATH_GENERATE_ERR(18),
    PRO_PATH_FOLLOWER_ERR(19),
    USER_BUTTON_STOP(20),
    RADIUS_LIMIT(21),
    HEIGHT_LIMIT(22),
    CTRL_MODE_INVALID(23),
    RC_LOST(24),
    APP_LOST(25),
    OBSTACLE_EST(26),
    OBSTACLE_PRO(27),
    TARGET_TOO_FAR(28),
    HEIGHT_TOO_LOW(30),
    UNKNOWN(255);
    
    private final int data;

    private PoiException(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PoiException find(int b) {
        PoiException result = UNKNOWN;
        PoiException[] values = values();
        for (PoiException tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
