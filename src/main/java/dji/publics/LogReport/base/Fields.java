package dji.publics.LogReport.base;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface Fields {

    public interface Dgo_activate {
        public static final String ACTION_CONNECT_DRONE = "1";
        public static final String ACTION_FINISH = "104";
        public static final String ACTION_FLIGHT_INFO = "102";
        public static final String ACTION_SCAN_QR = "2";
        public static final String ACTION_SIMULATION = "101";
        public static final String ACTION_UPGRADE = "103";
        public static final String action_type = "action_type";
    }

    public interface Dgo_active_track_enter_mode {
        public static final String enter_result = "enter_result";
    }

    public interface Dgo_active_track_exception {
        public static final String result = "result";
    }

    public interface Dgo_active_track_running_stop {
        public static final String action_type = "action_type";
        public static final String submode_type = "submode_type";
    }

    public interface Dgo_active_track_select_target {
        public static final String set_result = "set_result";
    }

    public interface Dgo_camera_set {
        public static final String anti_flicker = "anti_flicker";
        public static final String format_emmc = "format_emmc";
        public static final String pano_save_all = "pano_save_all";
        public static final String smart_LEDs = "smart_LEDs";
        public static final String storage_location = "storage_location";
    }

    public interface Dgo_hyperlapse_end {
        public static final String reason = "reason";
    }

    public interface Dgo_hyperlapse_generate_video_end {
        public static final String result = "result";
    }

    public interface Dgo_hyperlapse_set_course {
        public static final String result = "result";
    }

    public interface Dgo_landing extends Dgo_takeoff {
        public static final String EVENT_TAKEOFF_LINK_STATE = "link_state";
        public static final String EVENT_TAKEOFF_MAX_DISTANCE = "max_distance";
        public static final String EVENT_TAKEOFF_MAX_DURATION = "max_duration";
        public static final String EVENT_TAKEOFF_MAX_HEIGHT = "max_height";
        public static final String EVENT_TAKEOFF_MAX_SPEED_HOR = "max_speed_hor";
        public static final String EVENT_TAKEOFF_MAX_SPEED_VER = "max_speed_ver";
        public static final String EVENT_TAKEOFF_TOTAL_MILEAGE = "total_mileage";
    }

    public interface Dgo_poi_2_running_click_pause {
    }

    public interface Dgo_poi_2_tutorial_leave_tutorial {
        public static final String watch_time = "watch_time";
    }

    public interface Dgo_quick_shot_running_stop {
        public static final String type = "type";
    }

    public interface Dgo_quick_shot_select_target {
        public static final String set_result = "set_result";
    }

    public interface Dgo_quick_shot_select_target_cancel_selection {
        public static final String cancel_selection = "";
    }

    public interface Dgo_quick_shot_select_target_set_param {
        public static final String sub_mode = "sub_mode";
    }

    public interface Dgo_smartmode extends ReportCommon {
        public static final int VALUE_SMART_CINEMATIC = 13;
        public static final int VALUE_SMART_CL = 12;
        public static final int VALUE_SMART_DRAW = 2;
        public static final int VALUE_SMART_ENTER = 1;
        public static final int VALUE_SMART_FIXED_WING = 15;
        public static final int VALUE_SMART_FM = 9;
        public static final int VALUE_SMART_GESTURE = 3;
        public static final int VALUE_SMART_HL = 11;
        public static final int VALUE_SMART_HYPER_LAPSE = 18;
        public static final int VALUE_SMART_NONE = 0;
        public static final int VALUE_SMART_NORMAL = 1;
        public static final int VALUE_SMART_POI = 8;
        public static final int VALUE_SMART_POI2 = 19;
        public static final int VALUE_SMART_QUICK_SHOT = 14;
        public static final int VALUE_SMART_SMART_CAPTURE = 17;
        public static final int VALUE_SMART_SPOT_LIGHT = 16;
        public static final int VALUE_SMART_START = 2;
        public static final int VALUE_SMART_STOP = 3;
        public static final int VALUE_SMART_TAPFLY = 5;
        public static final int VALUE_SMART_TERRAIN_FOLLOW = 7;
        public static final int VALUE_SMART_TRACK = 4;
        public static final int VALUE_SMART_TRIPOD = 6;
        public static final int VALUE_SMART_WP = 10;
        public static final int VALUE_SMART_WP2 = 20;
    }

    public interface Dgo_support_service {
        public static final String ACTION_EMAIL = "3";
        public static final String ACTION_MANUAL_SERVICE = "4";
        public static final String ACTION_PROGRESS_INQUIRY = "2";
        public static final String ACTION_REPAIR = "1";
        public static final String ACTION_RETURN = "5";
    }

    public interface Dgo_takeoff extends ReportCommon {
        public static final String EVENT_TAKEOFF_BATTERY = "battery";
        public static final String EVENT_TAKEOFF_OBSTACLE_ = "obstacle";
        public static final String EVENT_TAKEOFF_RC_VERSION = "rc_ver";
        public static final String EVENT_TAKEOFF_SIGNAL_GPS = "signal_gps";
        public static final String EVENT_TAKEOFF_SIGNAL_RC = "signal_rc";
        public static final String EVENT_TAKEOFF_SIGNAL_TC = "signal_tc";
    }

    public interface Dgo_takevideo extends Dgo_takephoto {
        public static final String EVENT_ACTION_TYPE = "action_type";
        public static final String EVENT_CAMERA_TYPE = "camera_type";
        public static final String EVENT_CODEC = "codec";
        public static final String EVENT_RATE = "rate";
        public static final String EVENT_RESOLUTION = "resolution";
        public static final String RECORD_AE_LOCK = "record_ae_lock";
        public static final String RECORD_AE_MODE = "record_ae_mode";
        public static final String RECORD_DIGITAL_FILTER = "record_digital_filter";
        public static final String RECORD_FOCUS_MODE = "record_focus_mode";
        public static final String RECORD_FORMAT = "record_format";
        public static final String RECORD_FOV_TYPE = "record_fov_type";
        public static final String RECORD_STYLE = "record_style";
        public static final String RECORD_VIDEO_CACHE_AUTO_CLEAN = "record_video_cache_auto_clean";
        public static final String RECORD_VIDEO_CACHE_ENABLE = "record_video_cache_enable";
        public static final String RECORD_VIDEO_CACHE_SIZE = "record_video_cache_size";
        public static final String RECORD_VIDEO_CACHE_VOICE = "record_video_cache_voice";
        public static final String RECORD_VIDEO_DURATION = "record_video_duration";
        public static final String RECORD_VIDEO_SUBTITLE = "record_video_subtitle";
        public static final String RECORD_WHITE_BALANCE = "record_white_balance";
        public static final String STR_ACTION_TYPE_PHONE = "1";
        public static final String STR_ACTION_TYPE_RC = "4";
        public static final String STR_ACTION_TYPE_RC_DPAD = "3";
    }

    public interface Dgo_videoeditor_output extends ReportCommon {
        public static final String DJI_GO_EDITOR_OUTPUT_EDIT_DURATION = "edit_duration";
        public static final String DJI_GO_EDITOR_OUTPUT_EDIT_LOADING = "edit_loading";
        public static final String DJI_GO_EDITOR_OUTPUT_EDIT_MODE = "edit_mode";
        public static final String DJI_GO_EDITOR_OUTPUT_EFFECT_FILTER = "effect_filter";
        public static final String DJI_GO_EDITOR_OUTPUT_EFFECT_SINGLE = "effect_single";
        public static final String DJI_GO_EDITOR_OUTPUT_EFFECT_TITLE = "effect_title";
        public static final String DJI_GO_EDITOR_OUTPUT_EFFECT_TRANSITION = "effect_transition";
        public static final String DJI_GO_EDITOR_OUTPUT_LANGUAGE = "language";
        public static final String DJI_GO_EDITOR_OUTPUT_MUSIC_CATEGORY = "music_category";
        public static final String DJI_GO_EDITOR_OUTPUT_MUSIC_CLOUD = "music_cloud";
        public static final String DJI_GO_EDITOR_OUTPUT_MUSIC_MODE = "music_mode";
        public static final String DJI_GO_EDITOR_OUTPUT_MUSIC_NAME = "music_name";
        public static final String DJI_GO_EDITOR_OUTPUT_PART_NUMBER = "part_number";
        public static final String DJI_GO_EDITOR_OUTPUT_WORK_DURATION = "work_duration";
    }

    public interface Dgo_voice extends ReportCommon {
        public static final String app_ver = "app_ver";
        public static final String flyaction_switch = "flyaction_switch";
        public static final String function_switch = "function_switch";
        public static final String order = "order";
        public static final String phone_type = "phone_type";
        public static final String support_voice = "support_voice";
    }

    public interface Dgo_wp_2_set_waypoint_param_cancel_hotpoint {
        public static final String cancel_type = "cancel_type";
    }

    public interface Dgo_wp_2_set_waypoint_param_set_waypoint {
        public static final String set_type = "set_type";
    }

    public interface Dgo_wp_2_set_waypoint_param_set_waypoint_result {
        public static final String set_result = "set_result";
    }

    public interface ReportCommon {
        public static final String DEVICE_SN = "device_sn";
        public static final String STR_ACTION = "action";
        public static final String STR_CREATE_TIME = "createtime";
        public static final String STR_DEVICE_TYPE = "device_type";
        public static final String STR_DEVICE_TYPE_SPEC = "device_type_spec";
        public static final String STR_DEVICE_VER = "device_ver";
        public static final String STR_RC_VER = "rc_ver";
        public static final String STR_SN_SIGN = "firmware_sign";
        public static final String STR_TYPE = "type";
        public static final String battery_ver = "battery_ver";
        public static final String flight_sessionid = "flight_sessionid";
    }

    public interface Dgo_active_track_tutorial_leave_tutorial {
        public static final String watch_time = "watch_time";
    }

    public interface Dgo_active_track_tutorial_selected_mode {
        public static final String submode_type = "submode_type";
    }

    public interface Dgo_droneset_gimbal_pitch_exp {
        public static final String edit_result = "edit_result";
    }

    public interface Dgo_droneset_hotkey {
        public static final String EDIT_RESULT = "edit_result";
        public static final String EDIT_RESULT_OTHER = "999";
        public static final String HOTKEY_TYPE = "hotkey_type";
        public static final String HOTKEY_TYPE_C1 = "1";
        public static final String HOTKEY_TYPE_C2 = "2";
    }

    public interface Dgo_droneset_transmission_quality {
        public static final String EDIT_RESULT_10_MBPS = "4";
        public static final String EDIT_RESULT_1_MBPS = "5";
        public static final String EDIT_RESULT_2_MBPS = "6";
        public static final String EDIT_RESULT_4_MBPS = "1";
        public static final String EDIT_RESULT_6_MBPS = "2";
        public static final String EDIT_RESULT_8_MBPS = "3";
    }

    public interface Dgo_homeclick {
        public static final String ACTION_ACADEMY = "5";
        public static final String ACTION_DEVICE_PICTURE = "19";
        public static final String ACTION_DEVICE_SPINNER = "18";
        public static final String ACTION_DIRECTION = "15";
        public static final String ACTION_EDIT = "1";
        public static final String ACTION_ENTER_DEVICE = "21";
        public static final String ACTION_FIND_DRONE = "9";
        public static final String ACTION_FLIGHT_BOOK = "14";
        public static final String ACTION_FLIGHT_RECODER = "7";
        public static final String ACTION_FLIGHT_RECORD_ICON = "20";
        public static final String ACTION_GO_FLY = "22";
        public static final String ACTION_LIMIT = "10";
        public static final String ACTION_MAP = "6";
        public static final String ACTION_ME = "3";
        public static final String ACTION_MENU = "17";
        public static final String ACTION_NEED_HELP = "11";
        public static final String ACTION_SCAN_QR = "4";
        public static final String ACTION_SIM = "12";
        public static final String ACTION_SKY_PIXEL = "2";
        public static final String ACTION_STORE = "8";
        public static final String ACTION_VIDEO_ACADEMY = "13";
    }

    public interface Dgo_hyperlapse_click_go {
        public static final String duration = "duration";
        public static final String flighttime = "flighttime";
        public static final String frames = "frames";
        public static final String interval = "interval";
        public static final String means = "means";
        public static final String pathlength = "pathlength";
        public static final String videolength = "videolength";
        public static final String waypoint_number = "waypoint_number";
    }

    public interface Dgo_hyperlapse_library_load {
        public static final String load = "load";
        public static final String success = "1";
    }

    public interface Dgo_hyperlapse_running_exception {
        public static final String result = "result";
    }

    public interface Dgo_hyperlapse_set_clockwise {
        public static final String result = "result";
    }

    public interface Dgo_hyperlapse_submode_dialog {
        public static final String action_type = "action_type";
    }

    public interface Dgo_low_fpv {
        public static final int lowThreshold = 40;
    }

    public interface Dgo_poi_2_running_click_gimbal_back_center {
    }

    public interface Dgo_poi_2_running_click_go {
        public static final String course = "course";
        public static final String height = "height";
        public static final String radius = "radius";
        public static final String rth_height = "rth_height";
        public static final String speed = "speed";
    }

    public interface Dgo_quick_shot_tutorial_selected_mode {
        public static final String sub_mode = "sub_mode";
    }

    public interface Dgo_takephoto extends ReportCommon {
        public static final String CAPTURE_AE_LOCK = "capture_ae_lock";
        public static final String CAPTURE_AE_MODE = "capture_ae_mode";
        public static final String CAPTURE_DIGITAL_FILTER = "capture_digital_filter";
        public static final String CAPTURE_FOCUS_MODE = "capture_focus_mode";
        public static final String CAPTURE_GIMBAL_LOCK = "capture_gimbal_lock";
        public static final String CAPTURE_STYLE = "capture_style";
        public static final String EVENT_ASPECT_RADIO = "capture_aspect_ratio";
        public static final String EVENT_FORMT = "format";
        public static final String EVENT_HANDLEST = "handleset";
        public static final String EVENT_WHITE_BALANCE = "capture_white_balance";
        public static final String GENERAL_AFC = "general_afc";
        public static final String GENERAL_ANTI_FLICKER = "general_anti_flicker";
        public static final String GENERAL_APERTURE = "general_aperture";
        public static final String GENERAL_EV = "general_ev";
        public static final String GENERAL_FILE_INDEX_MODE = "general_file_index_mode";
        public static final String GENERAL_FOCUS_PEAKING_THRESHOLD = "general_focus_peaking_threshold";
        public static final String GENERAL_FRONT_LED = "general_front_led";
        public static final String GENERAL_GRID = "general_grid";
        public static final String GENERAL_HISTOGRAM = "general_histogram";
        public static final String GENERAL_INFINITY_FOCUS = "general_infinity_focus";
        public static final String GENERAL_ISO = "general_iso";
        public static final String GENERAL_KEEP_ORIGIN_HYPERLAPSE = "general_keep_origin_hyperlapse";
        public static final String GENERAL_KEEP_ORIGIN_PANO = "general_keep_origin_pano";
        public static final String GENERAL_NAVIGATION_MODE = "general_navigation_mode";
        public static final String GENERAL_OVEREXPOSURE = "general_overexposure";
        public static final String GENERAL_SHUTTER = "general_shutter";
        public static final String GENERAL_STORAGE_TYPE = "general_storage_type";
        public static final String GENERAL_SYNC_PHOTO = "general_sync_photo";
        public static final String GENERAL_ZOOM = "general_zoom";
        public static final String GENERA_CENTER_POINT = "genera_center_point";
    }

    public interface Dgo_tracking extends ReportCommon {
        public static final String CIRCLE = "circle";
        public static final String MODE = "mode";
        public static final String STATUS = "status";
    }

    public interface Dgo_uas_realname_step extends ReportCommon {
        public static final String device_uas = "device_uas";
    }

    public interface Dgo_videoeditor_share extends ReportCommon {
        public static final String DJI_GO_EDITOR_VIDEO_SHARE_API_MODE = "api_mode";
        public static final String DJI_GO_EDITOR_VIDEO_SHARE_DURATION = "share_duration";
        public static final String DJI_GO_EDITOR_VIDEO_SHARE_FILE_SIZE = "file_size";
        public static final String DJI_GO_EDITOR_VIDEO_SHARE_LANGUAGE = "share_language";
        public static final String DJI_GO_EDITOR_VIDEO_SHARE_MEDIA = "share_media";
        public static final String DJI_GO_EDITOR_VIDEO_SHARE_NETWORK = "share_network";
        public static final String DJI_GO_EDITOR_VIDEO_SHARE_SOURCE = "share_source";
    }

    public interface Dgo_vision_duration {
        public static final String AVOID_STATUS = "avoid_status";
        public static final String BACK_OBSTACLE = "back_obstacle";
        public static final String DOWN_OBSTACLE = "down_obstacle";
        public static final String FRONT_OBSTACLE = "front_obstacle";
        public static final String GPS_P_STATUS = "gps_p_status";
        public static final String INTERVAL = "interval";
        public static final String LEFT_OBSTACLE = "left_obstacle";
        public static final String RIGHT_OBSTACLE = "right obstacle";
        public static final String UP_OBSTACLE = "up_obstacle";
        public static final String VISION_P_STATUS = "vision_p_status";
    }

    public interface Dgo_wp_2_set_hotpoint_param_set_hotpoint {
        public static final String set_type = "set_type";
    }

    public interface Dgo_wp_2_start_misson {
        public static final String result = "result";
    }

    public interface Dgo_active_track_running_set_circle_speed_param {
        public static final String submode_type = "submode_type";
        public static final String value = "value";
    }

    public interface Dgo_decoder_event {
        public static final String drop_frame_num = "drop_frame_num";
        public static final String process_cpu_usage = "process_cpu_usage";
        public static final String recorder = "recorder";
        public static final String total_cpu_usage = "total_cpu_usage";
    }

    public interface Dgo_droneset_gimbal_pitch_delay {
        public static final String edit_result = "edit_result";
    }

    public interface Dgo_droneset_hotkey2 {
        public static final String EDIT_RESULT = "edit_result";
        public static final String EDIT_RESULT_AE_AE_LOCK = "6";
        public static final String EDIT_RESULT_GIMBAL_FORWARDS_DOWN = "1";
        public static final String EDIT_RESULT_NAVIGATION = "7";
        public static final String EDIT_RESULT_OTHER = "999";
        public static final String EDIT_RESULT_POINT_FOCUS = "5";
        public static final String EDIT_RESULT_PORTRAIT = "4";
        public static final String EDIT_RESULT_ZOOM_IN = "3";
        public static final String EDIT_RESULT_ZOOM_OUT = "2";
        public static final String HOTKEY_TYPE = "hotkey_type";
        public static final String HOTKEY_TYPE_FIVE_DOWN = "2";
        public static final String HOTKEY_TYPE_FIVE_LEFT = "3";
        public static final String HOTKEY_TYPE_FIVE_RIGHT = "4";
        public static final String HOTKEY_TYPE_FIVE_UP = "1";
    }

    public interface Dgo_droneset_hotkey_use {
        public static final String USE_TIMES = "use_times";
        public static final String USE_TIMES_AE_LOCK = "6";
        public static final String USE_TIMES_CAMERA_FORWARD_DOWN = "1";
        public static final String USE_TIMES_DECREASE_APERTURE = "11";
        public static final String USE_TIMES_DECREASE_EV = "9";
        public static final String USE_TIMES_INCREASE_APERTURE = "10";
        public static final String USE_TIMES_INCREASE_EV = "8";
        public static final String USE_TIMES_NAVIGATION = "7";
        public static final String USE_TIMES_OTHER = "999";
        public static final String USE_TIMES_POINT_FOCUS = "5";
        public static final String USE_TIMES_PORTRAIT = "4";
        public static final String USE_TIMES_ZOOM_IN = "2";
        public static final String USE_TIMES_ZOOM_OUT = "3";
    }

    public interface Dgo_hyperlapse_delete_waypoint {
        public static final String means = "means";
        public static final String result = "result";
    }

    public interface Dgo_hyperlapse_library_save_name {
        public static final String cancel = "3";
        public static final String name = "name";
        public static final String not_rename = "2";
        public static final String rename = "1";
    }

    public interface Dgo_hyperlapse_library_save_result {
        public static final String failure = "2";
        public static final String result = "result";
        public static final String success = "1";
    }

    public interface Dgo_in2_battery {
        public static final String RM = "RM";
        public static final String barcode = "barcode";
        public static final String cell1_voltage = "cell1_voltage";
        public static final String cell2_voltage = "cell2_voltage";
        public static final String cell3_voltage = "cell3_voltage";
        public static final String cell4_voltage = "cell4_voltage";
        public static final String cell5_voltage = "cell5_voltage";
        public static final String cell6_voltage = "cell6_voltage";
        public static final String current = "current";
        public static final String flag = "flag";
        public static final String record_value = "record_value";
        public static final String self_discharge_rate = "self_discharge_rate";
        public static final String soc = "soc";
        public static final String temperature1 = "temperature1";
        public static final String temperature2 = "temperature2";
        public static final String voltage = "voltage";
    }

    public interface Dgo_meclick {
        public static final String ACTION_AFFILIATE_ACCOUNT = "5";
        public static final String ACTION_FLIGHT_RECORD = "6";
        public static final String ACTION_FORUM = "7";
        public static final String ACTION_MESSAGE = "1";
        public static final String ACTION_MORE = "9";
        public static final String ACTION_SETTING = "2";
        public static final String ACTION_STORE = "4";
        public static final String ACTION_SUPPORT = "8";
        public static final String ACTION_USER_INFO = "3";
    }

    public interface Dgo_phone_su {
        public static final String PHONE_IS_SU = "is_su";
        public static final String PHONE_SU_NO = "1";
        public static final String PHONE_SU_YES = "2";
    }

    public interface Dgo_poi_2_running_estimate {
        public static final String radius = "radius";
        public static final String result = "result";
    }

    public interface Dgo_poi_2_set_param_set_point {
        public static final String set_type = "set_type";
    }

    public interface Dgo_poi_2_tutorial_select_tutorial {
        public static final String tutorial_type = "tutorial_type";
    }

    public interface Dgo_quickshot {
        public static final String direction = "direction";
        public static final String distance_setting = "distance_setting";
        public static final String type = "type";
    }

    public interface Dgo_smartcapture {
        public static final String type = "type";
        public static final String with_prop_guard = "with_prop_guard";
    }

    public interface Dgo_support_click {
        public static final String ACTION_PROBLEM_REPORT = "1";
        public static final String ACTION_PRODUCT_SUGGESTION = "2";
        public static final String ACTION_RETURN = "4";
        public static final String ACTION_SUPPORT = "3";
    }

    public interface Dgo_uas_realname extends ReportCommon {
        public static final String device_uas = "device_uas";
        public static final String errorcode = "errorcode";
        public static final String result = "result";
        public static final String retcode = "retcode";
    }

    public interface Dgo_update extends ReportCommon {
        public static final String STEP = "step";
    }

    public interface Dgo_wp_2_exit {
        public static final String type = "type";
    }

    public interface Dgo_wp_2_set_waypoint_param_cancel_waypoint {
        public static final String cancel_type = "cancel_type";
    }

    public interface Dgo_wp_2_upload_misson {
        public static final String aircraft_towards = "aircraft_towards";
        public static final String hotpoint_number = "hotpoint_number";
        public static final String link_waypoint_number = "link_waypoint_number";
        public static final String mission_finish = "mission_finish";
        public static final String out_of_control = "out_of_control";
        public static final String point_height_max = "point_height_max";
        public static final String point_height_mid = "point_height_mid";
        public static final String point_height_min = "point_height_min";
        public static final String point_photo_num = "point_photo_num";
        public static final String point_start_record_num = "point_start_record_num";
        public static final String point_stop_record_num = "point_stop_record_num";
        public static final String route_type = "route_type";
        public static final String rth_height = "rth_height";
        public static final String total_flght_mileage = "total_flght_mileage";
        public static final String total_flight_time = "total_flight_time";
        public static final String upload_result = "upload_result";
        public static final String upload_time = "upload_time";
        public static final String way_speed = "way_speed";
        public static final String waypoint_number = "waypoint_number";
    }

    public interface Dgo_active_track_running_click_go {
    }

    public interface Dgo_active_track_running_set_max_speed_param {
        public static final String submode_type = "submode_type";
        public static final String value = "value";
    }

    public interface Dgo_apas {
        public static final String type = "type";
    }

    public interface Dgo_appset {
        public static final String EVENT_CACHE = "cache";
        public static final String EVENT_IS_ENABLE_CELL = "mdata";
        public static final String EVENT_IS_ENABLE_REPORT = "report";
        public static final String EVENT_IS_LOGIN = "login";
        public static final String EVENT_RC_TYPE = "rc_type";
    }

    public interface Dgo_connect extends ReportCommon {
        public static final String battery_cycle = "battery_cycle";
        public static final String camera_cali = "camera_cali";
        public static final String camera_type = "camera_type";
        public static final String compass_cali = "compass_cali";
        public static final String device_mode = "device_mode";
        public static final String imu_cali = "imu_cali";
        public static final String update = "update";
        public static final String vision_cali = "vision_cali";
    }

    public interface Dgo_droneset_common {
        public static final String DRONESET_CLOSED = "1";
        public static final String DRONESET_OPEN = "2";
        public static final String EDIT_RESULT = "edit_result";
    }

    public interface Dgo_fpv_click {
        public static final String ACTION_CAMERA_SETTING = "4";
        public static final String ACTION_CHECKLIST_FROM_TOPBAR = "7";
        public static final String ACTION_GENERAL_SETTINGS = "6";
        public static final String ACTION_INTELLIGENT_FLIGHT_ENTRANCE = "5";
        public static final String ACTION_PHOTO_RECORD_SWITCH = "3";
        public static final String ACTION_PLAYBACK = "1";
        public static final String ACTION_SMALL_MAP = "2";
    }

    public interface Dgo_fpvcontrol_gimbal {
        public static final int minAngel = 120;
        public static final int no = 0;
        public static final String pitch = "pitch";
        public static final String yaw = "yaw";
        public static final int yes = 1;
    }

    public interface Dgo_hyperlapse_library_delete {
        public static final String cancel = "2";
        public static final String delete = "delete";
        public static final String done = "1";
    }

    public interface Dgo_hyperlapse_select_submode {
        public static final String result = "result";
        public static final String type = "type";
    }

    public interface Dgo_hyperlapse_set_waypoint {
        public static final String means = "means";
        public static final String result = "result";
    }

    public interface Dgo_hyperlapse_view_frame {
        public static final String close = "2";
        public static final String frame = "frame";
        public static final String open = "1";
    }

    public interface Dgo_poi_2_running_stop {
    }

    public interface Dgo_poi_2_set_param_cancel_point {
        public static final String cancel_point = "cancel_point";
    }

    public interface Dgo_quiz {
        public static final String ACTION = "action";
        public static final String COUNTRY = "country";
        public static final String FROM = "from";
        public static final String WHETHER = "whether";
    }

    public interface Dgo_rc_hotkey_use {
        public static final String c1 = "1";
        public static final String c2 = "2";
        public static final String hotkey_type = "hotkey_type";
        public static final String use_function = "use_function";
    }

    public interface Dgo_support_product_advice {
        public static final String ACTION_RETURN = "2";
        public static final String ACTION_SUBMIT = "1";
    }

    public interface Dgo_without_rc_smart_capture {
        public static final String event_time = "event_time";
        public static final String times = "times";
    }

    public interface Dgo_Upgrade extends ReportCommon {
        public static final String UPGRADE_SERVICE_INIT_TIMES = "upgrade_service_init_times";
    }

    public interface Dgo_bleconnect extends ReportCommon {
        public static final String PAGEID = "pageid";
    }

    public interface Dgo_connection_help {
        public static final String action = "action";
        public static final String type = "type";
    }

    public interface Dgo_discover extends ReportCommon {
        public static final String MODULE_TYPE = "module_type";
    }

    public interface Dgo_duration {
        public static final String STR_TIME = "time";
    }

    public interface Dgo_flight_recorder_clean {
        public static final String ACTION_CANCEL = "3";
        public static final String ACTION_CLEAR_ALL_CACHED_RECORDS = "2";
        public static final String ACTION_ONLY_CACHE_FAVORITED_RECORDS = "1";
    }

    public interface Dgo_flight_recorder_result {
        public static final String ACTION_CANCEL = "3";
        public static final String ACTION_CLEAR_LOCALLY_CACHED_RECORDS = "2";
        public static final String ACTION_START_SYNC = "1";
        public static final String TIME_RESULT_ALL = "3";
        public static final String TIME_RESULT_ONE_MONTH = "1";
        public static final String TIME_RESULT_SIX_MONTH = "2";
        public static final String action_type = "action_type";
        public static final String time_result = "time_result";
    }

    public interface Dgo_fpv_map extends ReportCommon {
        public static final String AMAP = "3";
        public static final String DOWNLOAD_EMPTY = "3";
        public static final String DOWNLOAD_NOT_EMPTY = "2";
        public static final String ERROR_OR_NOT_PROVIDE = "255";
        public static final String GOOGLE = "1";
        public static final String GOOGLE_NO_OFFLINE = "0";
        public static final String HEREMAP = "4";
        public static final String IN_NO_FLY_ZONE = "1";
        public static final String MAPBOX = "2";
        public static final String NEVER_DOWNLOAD = "1";
        public static final String NOT_CONNECT_DRONE = "3";
        public static final String OFFLINE = "2";
        public static final String ONLINE = "1";
        public static final String OUT_NO_FLY_ZONE = "2";
        public static final String download_status = "download_status";
        public static final String download_tiles = "download_tiles";
        public static final String geo_status = "geo_status";
        public static final String map_source = "map_source";
        public static final String network_status = "network_status";
    }

    public interface Dgo_hyperlapse_common {
        public static final String camera_firmware_edition = "camera_firmware_edition";
        public static final String navi_firmware_edition = "navi_firmware_edition";
        public static final String submode_type = "submode_type";
    }

    public interface Dgo_hyperlapse_estimate_end {
        public static final String radius = "radius";
        public static final String result = "result";
    }

    public interface Dgo_hyperlapse_generate_video_start {
        public static final String result = "result";
    }

    public interface Dgo_hyperlapse_hide_panel {
        public static final String result = "result";
    }

    public interface Dgo_hyperlapse_library_save_type {
        public static final String new_task = "1";
        public static final String old_task = "2";
        public static final String type = "type";
    }

    public interface Dgo_imuswitch {
        public static final String IMU_ACTION_BREAKDOWN = "2";
        public static final String IMU_ACTION_SWITCH = "1";
    }

    public interface Dgo_live_param {
        public static final String PARAM_CHAT = "chat";
        public static final String PARAM_LIVE_ID = "live_id";
        public static final String PARAM_MIC = "mic";
    }

    public interface Dgo_poi_2_exception {
        public static final String exception = "exception";
    }

    public interface Dgo_poi_2_running_click_resume {
    }

    public interface Dgo_poi_2_tutorial_ignore_video {
    }

    public interface Dgo_privacy extends ReportCommon {
        public static final String action_type = "action_type";
        public static final String changed_item = "changed_item";
        public static final String enable_auth_after_change = "enable_auth_after_change";
        public static final String new_value = "new_value";
        public static final String view_type = "view_type";
    }

    public interface Dgo_quick_shot_enter_mode {
        public static final String enter_result = "enter_result";
    }

    public interface Dgo_quick_shot_exception {
        public static final String exception = "exception";
    }

    public interface Dgo_quick_shot_running_click_go {
        public static final String settings_distance = "settings_distance";
        public static final String settings_rotation = "settings_rotation";
        public static final String sub_mode = "sub_mode";
    }

    public interface Dgo_quick_shot_tutorial_leave_tutorial {
        public static final String watch_time = "watch_time";
    }

    public interface Dgo_smartmode_enter_mode {
        public static final String result = "result";
        public static final String smartmode = "smartmode";
    }

    public interface Dgo_support_bug_result {
        public static final String ACTION_RETURN = "2";
        public static final String ACTION_SUBMIT_REPORT = "1";
    }

    public interface Dgo_wp_2_mission_task_enter_task {
        public static final String mission_number = "mission_number";
    }

    public interface Dgo_wp_2_tutorial_leave_tutorial {
        public static final String watch_time = "watch_time";
    }
}
