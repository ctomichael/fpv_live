package dji.publics.LogReport.base;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface Values {

    public interface Dgo_quick_shot_select_target extends Dgo_active_track_select_target {
    }

    public interface FlashType {
        public static final String FLASH_ACTION_AUTO = "2";
        public static final String FLASH_ACTION_KEEP_ON = "4";
        public static final String FLASH_ACTION_OFF = "1";
        public static final String FLASH_ACTION_ON = "3";
    }

    public interface GridType {
        public static final String GRID_CENTER_POINT = "4";
        public static final String GRID_DIAGONAL = "2";
        public static final String GRID_LINE = "3";
        public static final String GRID_NONE = "1";
    }

    public interface PrivacySettingChangeItem {
        public static final String DJI_DEVICE_GPS = "2";
        public static final String DJI_DEVICE_INFO = "3";
        public static final String DJI_DEVICE_LOCATION = "4";
        public static final String MOBILE_GPS = "1";
    }

    public interface PrivacySettingViewType {
        public static final String ACTIVATE = "11";
        public static final String FB_LIVE = "10";
        public static final String FIND_PLANE = "3";
        public static final String FLY_SAFE = "4";
        public static final String FPV = "8";
        public static final String FR_SEE = "7";
        public static final String FR_SYNC = "6";
        public static final String MAIN = "1";
        public static final String MAP = "5";
        public static final String PIC_SHARE = "9";
        public static final String SETTING = "2";
        public static final String SILENT_ACTIVATE = "12";
    }

    public interface QuickShot {
        public static final String ANTI_CLOCKWISE = "2";
        public static final String CIRCLE = "1";
        public static final String CLOCKWISE = "1";
        public static final String COMET = "6";
        public static final String DIAGONAL = "2";
        public static final String DISTANCE_40 = "2";
        public static final String DISTANCE_40_60 = "3";
        public static final String DISTANCE_60_90 = "4";
        public static final String DISTANCE_90_120 = "5";
        public static final String DISTANCE_LOWER_40 = "1";
        public static final String DOLLY_ZOOM = "10";
        public static final String HELIX = "3";
        public static final String PLANET = "8";
        public static final String ROCKET = "4";
    }

    public interface SmartVoice {
        public static final String NOT_SUPPORT_VOICE = "2";
        public static final String SUPPORT_VOICE = "1";
        public static final String SWITCH_CLOSED = "2";
        public static final String SWITCH_NOT_SUPPORT = "0";
        public static final String SWITCH_OPEN = "1";
    }

    public interface TrackingAndMeterType {
        public static final String TRACKING_VALUE_METER = "3";
        public static final String TRACKING_VALUE_METER_LOCK = "4";
        public static final String TRACKING_VALUE_NO_TARGET = "2";
        public static final String TRACKING_VALUE_TARGET = "1";
    }

    public interface Handleset {
        public static final String HANDLESET_TYPE_HANDLE = "2";
        public static final String HANDLESET_TYPE_PHONE = "1";
    }

    public interface NavigationModeEnterException {
        public static final String ADVANCED_GESTURE_NOT_ALLOW = "21";
        public static final String ATTI = "15";
        public static final String BEGINNER = "5";
        public static final String DEBUG = "1";
        public static final String FORBIDDEN = "11";
        public static final String GO_HOME = "8";
        public static final String LANDING = "9";
        public static final String MC_MODE_ERROR = "17";
        public static final String MULTI_MODE_SWITCH = "10";
        public static final String NOT_FLYING = "7";
        public static final String NO_AVOIDANCE = "17";
        public static final String NO_GPS = "6";
        public static final String NO_SD_CARD = "18";
        public static final String NO_VPS = "19";
        public static final String OK = "255";
        public static final String PANO = "2";
        public static final String PANO_SHOOTING = "22";
        public static final String PORTRAIT = "4";
        public static final String QUICK_MOVIE_NOT_RECORD = "20";
        public static final String RECORDING = "12";
        public static final String SHOOTING_PHOTO = "13";
        public static final String SIMULATOR = "3";
        public static final String SMART_CAPTURE = "14";
        public static final String SPORT_MODE = "16";
    }

    public interface Dgo_active_track_mode {
        public static final String FIXED_ANGLE = "2";
        public static final String HEADLESS_FOLLOW = "1";
        public static final String SPOTLIGHT = "3";
    }

    public interface Dgo_active_track_select_target {
        public static final String DOT = "1";
        public static final String RECT = "2";
    }

    public interface FlightModeValue {
        public static final String NOT_FLY = "1";
    }

    public interface PrivacySettingNewValue {
        public static final String CLOSE = "2";
        public static final String OPEN = "1";
    }

    public interface RealNameRetCode {
        public static final String APP_INNER_ERROR = "5";
        public static final String APP_SIGN_FAIL = "7";
        public static final String CHANGE_OLD_PHONE_NUMBER = "2";
        public static final String REGISTER_DEVICE_SUCCESS = "4";
        public static final String SYSTEM_ERROR = "3";
        public static final String TIMEOUT = "1";
        public static final String UNKNOWN_ERROR = "255";
        public static final String USER_DROP = "6";
    }

    public interface SmartCapture {
        public static final String CONFRIM = "2";
        public static final String DISTANCE = "4";
        public static final String LANDING = "7";
        public static final String POSITION = "3";
        public static final String PROP_GUARD_EQUIPED = "2";
        public static final String PROP_GUARD_NONE = "1";
        public static final String RECORD = "6";
        public static final String SHOOT_PHOTO = "5";
        public static final String TAKE_OFF = "1";
    }

    public interface TakePhoto {
        public static final String PHOTO_ACTION_VALUE_COUNTDOWN_10 = "4";
        public static final String PHOTO_ACTION_VALUE_COUNTDOWN_2 = "2";
        public static final String PHOTO_ACTION_VALUE_COUNTDOWN_5 = "3";
        public static final String PHOTO_ACTION_VALUE_HDR = "10";
        public static final String PHOTO_ACTION_VALUE_LONGEXPOSURE_16 = "7";
        public static final String PHOTO_ACTION_VALUE_LONGEXPOSURE_32 = "8";
        public static final String PHOTO_ACTION_VALUE_LONGEXPOSURE_4 = "5";
        public static final String PHOTO_ACTION_VALUE_LONGEXPOSURE_8 = "6";
        public static final String PHOTO_ACTION_VALUE_LONGEXPOSURE_INFINITE = "9";
        public static final String PHOTO_ACTION_VALUE_PANO_180 = "11";
        public static final String PHOTO_ACTION_VALUE_PANO_33 = "13";
        public static final String PHOTO_ACTION_VALUE_PANO_360 = "12";
        public static final String PHOTO_ACTION_VALUE_SIGNAL = "1";
    }

    public interface TimeLapseReport {
        public static final String ANTI_CLOCKWISE = "1";
        public static final String CANCEL = "3";
        public static final String CIRCLE = "3";
        public static final String CLOCKWISE = "2";
        public static final String CONFIRM = "2";
        public static final String COURSE_LOCK = "4";
        public static final String DELETE_WAYPOINT_C2 = "2";
        public static final String DELETE_WAYPOINT_DUSTBIN = "1";
        public static final String DONT_SHOW_AGAIN = "1";
        public static final String ENTER_FAILS_NOT_TAKING_OFF = "1";
        public static final String FREE = "1";
        public static final String GENERATE_VIDEO_START = "1";
        public static final String GO_WITH_PANEL = "2";
        public static final String GO_WITH_RC = "3";
        public static final String GO_WITH_RETENGLE = "1";
        public static final String HIDE_PANEL = "1";
        public static final String IGNORE_DIALOG = "2";
        public static final String NONE = "0";
        public static final String NOT_FLYING = "1";
        public static final String NUMBER_OF_PIC_NOT_ENOUGH = "2";
        public static final String OK = "255";
        public static final String SET_COURSE_LOCK = "1";
        public static final String SET_COURSE_UNLOCK = "2";
        public static final String SET_POINT_MEANS_C1 = "2";
        public static final String SET_POINT_MEANS_PLUS = "1";
        public static final String SHOWING_DIALOG = "1";
        public static final String SHOW_PANEL = "2";
        public static final String WAY_POINT = "2";
    }

    public interface UpgradeSteps {
        public static final String CLICK_BINNER = "2";
        public static final String DOWNLOADING = "3";
        public static final String DOWNLOAD_SUCCEED = "4";
        public static final String SHOW_BINNER = "1";
        public static final String TIP_TO_CONNECT_DEVICE = "12";
        public static final String TIP_TO_CONNECT_INTERNET = "11";
        public static final String TIP_TO_REBOOT_DEVICE = "13";
        public static final String UPGRADE_FAILED = "7";
        public static final String UPGRADE_SERVICE_INIT_EXCEPTION = "14";
        public static final String UPGRADE_SUCCEED = "6";
        public static final String UPGRADING = "5";
    }

    public interface BLE {
        public static final String BLE_CONNECT_AUTO = "6";
        public static final String BLE_CONNECT_CLICKED = "5";
        public static final String BLE_DISCONNECT_CLICKED = "7";
        public static final String BLE_DISCONNECT_UNEXP = "8";
        public static final String BLE_HOME_CLICK_BINNER = "2";
        public static final String BLE_HOME_GOTOPREVIEW = "9";
        public static final String BLE_HOME_SHOW_BINNER = "1";
        public static final String BLE_PAGEID_HONE = "1";
        public static final String BLE_PAGEID_PREVIEW = "2";
        public static final String BLE_SHOW_LIST = "4";
        public static final String BLE__HONE_CLICK_CONNECT = "3";
    }

    public interface PrivacyActionType {
        public static final String AUTHORIZE = "3";
        public static final String DISAGREE = "4";
        public static final String FUZZY_GPS = "5";
        public static final String PRIVACY = "2";
        public static final String USAGE = "1";
    }

    public interface RealNameAction {
        public static final String CALL_WEBVIEW = "5";
        public static final String CLOSE_DIALOG = "2";
        public static final String ENTER_PHONE_PAGE = "3";
        public static final String FAILURE = "9";
        public static final String NO_MORE_REMIND = "7";
        public static final String SEND_VERIFICATION_CODE = "4";
        public static final String SHOW_DIALOG = "1";
        public static final String SKIP = "6";
        public static final String SUCCESS = "8";
    }

    public interface CmaeraId {
        public static final String CAMERA_ID_VALUE_BACK = "1";
        public static final String CAMERA_ID_VALUE_FRONT = "2";
    }

    public interface Duration {
        public static final String APPLICATION = "1";
        public static final String MAINPAGE_VISIBLE = "2";
    }

    public interface GimbalPitchLock {
        public static final String PITCH_ACTION_LOCK = "2";
        public static final String PITCH_ACTION_UNLOCK = "1";
    }

    public interface GimbalSence {
        public static final String SCENE_ACTION_SPORT = "2";
        public static final String SCENE_ACTION_WALK = "1";
    }

    public interface Poi2 {
        public static final String ANTI_CLOCKWISE = "2";
        public static final String C2_TO_CANCEL = "2";
        public static final String CLOCKWISE = "1";
        public static final String GPS_POI = "2";
        public static final String STOP_TO_CANCEL = "1";
        public static final String VISUAL_POI = "1";
    }

    public interface TakeVideo {
        public static final String VIDEO_ACTION_VALUE_4K = "2";
        public static final String VIDEO_ACTION_VALUE_AUTO = "1";
        public static final String VIDEO_ACTION_VALUE_HYPERLAPSE = "3";
        public static final String VIDEO_ACTION_VALUE_MOTIONLAPSE = "5";
        public static final String VIDEO_ACTION_VALUE_TIMELAPSE = "4";
    }
}
