package dji.component.flysafe;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class FlyForbidProtocol {
    public static final int AIRPORT_WARNING_MAXHEIGHT = 120;
    public static final long CHECK_DATA_UPDATE_INTERVAL = 300000;
    public static final double COORD_ENLARGE_FACTOR = 1000000.0d;
    public static final int COUNTRY_CODE_CHINA = 156;
    public static final double DATABASE_UPDATE_DIST = 50000.0d;
    public static final String FORMAT_KEY_VERSION = "kv%d";
    public static final double GO_SEARCH_RADIUS_CONNECTED = 10000.0d;
    public static final String KEY_SHOW_LIMIT_CIRCLE_WHEN_SPECIAL_UNLOCK = "key_show_limit_circle_when_special_unlock";
    public static final String PLATFORM_NAME_FOR_JNI = "android";
    public static final String PREFIX_KEY_VERSION = "kv";
    public static final int RADIUS_AREA_UPLOAD_TO_FLYC = 10000;
    public static double SEARCH_RADIUS = 50000.0d;
    public static final double STRONG_WARNING_CHECK_RADIUS = 5000.0d;
    public static final int SUPPORT_UNLOCK_FLYC_PROTOCOL_VERSION = 9;
    public static final long UNLIMIT_AREA_EXPIRED_TIME = 86400;
    public static final double UNLOCK_RADIUS = 2000.0d;
    public static final double UPDATE_CACHE_DIST = 10000.0d;
    public static final double UPDATE_TFR_DIST = 5000.0d;

    public enum ShowLimitCircleSUEvent {
        TURE
    }

    @Keep
    public enum LevelType {
        WARNING(0),
        CAN_UNLIMIT(1),
        CAN_NOT_UNLIMIT(2, 4),
        STRONG_WARNING(3);
        
        private static volatile LevelType[] sValues = null;
        private int data;
        private int mSubType = -1;

        private LevelType(int _data) {
            this.data = _data;
        }

        private LevelType(int _data, int _subType) {
            this.data = _data;
            this.mSubType = _subType;
        }

        public int value() {
            return this.data;
        }

        public int getSubType() {
            return this.mSubType;
        }

        public static LevelType ofValue(int value) {
            if (sValues == null) {
                sValues = values();
            }
            LevelType[] levelTypeArr = sValues;
            for (LevelType ts : levelTypeArr) {
                if (ts.data == value) {
                    return ts;
                }
            }
            return WARNING;
        }
    }

    @Keep
    public enum DJIWarningAreaState {
        None(0),
        NearLimit(1),
        InnerLimit(4);
        
        private int data;

        private DJIWarningAreaState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }
    }
}
