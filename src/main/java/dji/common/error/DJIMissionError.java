package dji.common.error;

import com.adobe.xmp.XMPError;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import it.sauronsoftware.ftp4j.FTPCodes;
import org.bouncycastle.crypto.tls.CipherSuite;

@EXClassNullAway
public class DJIMissionError extends DJIError {
    public static final DJIMissionError ACTION_REPEAT_TIME_NOT_VALID = new DJIMissionError("Waypoint repeat time provided is invalid");
    public static final DJIMissionError ACTION_TIMEOUT_NOT_VALID = new DJIMissionError("Waypoint action timeout provided is invalid");
    public static final DJIMissionError AIRCRAFT_ALTITUDE_TOO_HIGH = new DJIMissionError("The aircraft's altitude is too high.");
    public static final DJIMissionError AIRCRAFT_ALTITUDE_TOO_LOW = new DJIMissionError("The aircraft's altitude is too low.");
    public static final DJIMissionError AIRCRAFT_GOING_HOME = new DJIMissionError("The aircraft is going home", 242);
    public static final DJIMissionError AIRCRAFT_IN_NO_FLY_ZONE = new DJIMissionError("The aircraft is in the no fly zone", 223);
    public static final DJIMissionError AIRCRAFT_LANDING = new DJIMissionError("The aircraft is landing", 241);
    public static final DJIMissionError AIRCRAFT_NOT_IN_THE_AIR = new DJIMissionError("The aircraft is not in the air", 218);
    public static final DJIMissionError AIRCRAFT_STARTING_MOTOR = new DJIMissionError("The aircraft is starting the motor", 243);
    public static final DJIMissionError AIRCRAFT_TAKING_OFF = new DJIMissionError("The aircraft is taking off", 240);
    public static final DJIMissionError ALTITUDE_LIMIT = new DJIMissionError("The aircraft reach the altitude limit. Please operate the aircraft within the altitude limit.", 5316);
    public static final DJIMissionError ALTITUDE_NOT_VALID = new DJIMissionError("Waypoint altitude provided is invalid");
    public static final DJIMissionError ALTITUDE_TOO_HIGH = new DJIMissionError("The altitude is too high", 192);
    public static final DJIMissionError ALTITUDE_TOO_LOW = new DJIMissionError("The altitude is too low", CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256);
    public static final DJIMissionError AUTO_FLIGHT_SPEED_NOT_VALID = new DJIMissionError("Auto flight speed value provided is invalid");
    public static final DJIMissionError BEGAN = new DJIMissionError("began", 1);
    public static final DJIMissionError CANCELED = new DJIMissionError("canceled", 2);
    public static final DJIMissionError CANNOT_BYPASS_OBSTACLE = new DJIMissionError("The aircraft cannot bypass the obstacle.");
    public static final DJIMissionError COMMON_DISCONNECTED = new DJIMissionError("Disconnected");
    public static final DJIMissionError COMMON_STATE_ERROR = new DJIMissionError("The command is not supported in current state.");
    public static final DJIMissionError COMMON_UNSUPPORTED = new DJIMissionError("Not supported");
    public static final DJIMissionError CONTROL_FAILED = new DJIMissionError("Intelligent Hotpoint mission execute failed, the description is the fail reason", 5317);
    public static final DJIMissionError CORNER_RADIUS_NOT_VALID = new DJIMissionError("Waypoint corner radius provided is invalid");
    public static final DJIMissionError DISTANCE_FROM_MISSION_TARGET_TOO_LONG = new DJIMissionError("The distance from mission target position is too long", XMPError.BADXML);
    public static final DJIMissionError ESTIMATE_RESULT_INVALID = new DJIMissionError("Failed to estimate subject location.", 5313);
    public static final DJIMissionError ESTIMATE_TIMEOUT = new DJIMissionError("Estimate timeout, mission exit.", 5312);
    public static final DJIMissionError EXIT_BY_USER_BUTTON = new DJIMissionError("Current mission exit by user button.", 5314);
    public static final DJIMissionError FAILED = new DJIMissionError("failed", 3);
    public static final DJIMissionError FEATURE_POINT_CANNOT_MATCH = new DJIMissionError("The feature points found by both vision sensors cannot match.");
    public static final DJIMissionError FOLLOW_ME_DISCONNECT_TIME_TOO_LONG = new DJIMissionError("The disconnect time of follow me mission is too long", 177);
    public static final DJIMissionError FOLLOW_ME_DISTANCE_TOO_LARGE = new DJIMissionError("Distance between the aircraft and mobile phone is beyond acceptable limit(must be lower than 20000m)", 176);
    public static final DJIMissionError FOLLOW_ME_GIMBAL_PITCH_ERROR = new DJIMissionError("The initial pitch angle of gimbal is too large", 178);
    public static final DJIMissionError GIMBAL_PITCH_NOT_VALID = new DJIMissionError("Waypoint gimbal pitch provided is invalid");
    public static final DJIMissionError GPS_ABNORMAL = new DJIMissionError("GPS signal weak. Please try again in an open area.", 5309);
    public static final DJIMissionError GPS_NOT_READY = new DJIMissionError("GPS of aircraft is not ready", 6);
    public static final DJIMissionError GPS_SIGNAL_WEAK = new DJIMissionError("The GPS signal of the aircraft is weak", 216);
    public static final DJIMissionError HEADING_NOT_VALID = new DJIMissionError("Waypoint heading provided is invalid");
    public static final DJIMissionError HIGH_PRIORITY_MISSION_EXECUTING = new DJIMissionError("A higher priority mission is executing", FTPCodes.NAME_SYSTEM_TIME);
    public static final DJIMissionError HOME_POINT_DIRECTION_UNKNOWN = new DJIMissionError("The direction of homepoint is unknown", 166);
    public static final DJIMissionError HOME_POINT_LOCATION_INVALID = new DJIMissionError("The latitude and longitude of homepoint are invalid", 163);
    public static final DJIMissionError HOME_POINT_MISSION_NOT_PAUSED = new DJIMissionError("The home-point  mission is not paused", 170);
    public static final DJIMissionError HOME_POINT_MISSION_PAUSED = new DJIMissionError("The home-point mission is paused", 169);
    public static final DJIMissionError HOME_POINT_NOT_RECORDED = new DJIMissionError("The home point of aircraft is not recorded", 222);
    public static final DJIMissionError HOME_POINT_VALUE_INVALID = new DJIMissionError("The home point is not a valid float value", 162);
    public static final DJIMissionError HOTPOINT_ALTITUDE_TOO_LOW = new DJIMissionError("Aircraft's altitude is too low to execute hotpoint mission.", 5322);
    public static final DJIMissionError HOT_POINT_INVALID = new DJIMissionError("Hotpoint parameter do not meet requirements.", 5310);
    public static final DJIMissionError INCOMPLETE_MISSION = new DJIMissionError("Incomplete Mission");
    public static final DJIMissionError INDEX_OUT_OF_RANGE = new DJIMissionError("Waypoint index out of range.", 173);
    public static final DJIMissionError INDICES_ARE_DISCONTINUOUS = new DJIMissionError("Waypoint indices are discontinuous", 172);
    public static final DJIMissionError INSUFFICIENT_FEATURES_IN_TARGET_AREA = new DJIMissionError("Frame selection subject texture not obvious.", 5303);
    public static final DJIMissionError INVALID_DATA_SIZE = new DJIMissionError("Waypoint data size is invalid. It is for internal usage.", 175);
    public static final DJIMissionError INVALID_INTERVAL_SHOOT_PARAM = new DJIMissionError("The time interval or the distance interval is invalid.", 180);
    public static final DJIMissionError IN_NOVICE_MODE = new DJIMissionError("The aircraft is in novice mode now", 202);
    public static final DJIMissionError IOC_TYPE_UNKNOWN = new DJIMissionError("The type of IOC is unknown", 161);
    public static final DJIMissionError IOC_WORKING = new DJIMissionError("The IOC mode is working", 210);
    public static final DJIMissionError IS_FLYING = new DJIMissionError("Aircraft is flying", 9);
    public static final DJIMissionError KEY_LEVEL_LOW = new DJIMissionError("The API key provided to you is not at the correct permission level", 13);
    public static final DJIMissionError LOST_CONNECTION_WITH_REMOTE_CONTROLLER = new DJIMissionError("Lost connection with Remote Controller", 5318);
    public static final DJIMissionError LOST_TARGET_WHILE_EXECUTING = new DJIMissionError("Tracking subject lost while executing.", 5305);
    public static final DJIMissionError LOST_TARGET_WHILE_WATCHING = new DJIMissionError("Tracking subject lost while watching.", 5304);
    public static final DJIMissionError LOW_BATTERY = new DJIMissionError("Low battery level warning", 217);
    public static final DJIMissionError MAX_FLIGHT_SPEED_NOT_VALID = new DJIMissionError("Max Flight speed value provided is invalid");
    public static final DJIMissionError MAX_NUMBER_OF_WAYPOINTS_UPLOAD_LIMIT_REACHED = new DJIMissionError("Waypoint mission has reached the maximum points limit", 11);
    public static final DJIMissionError MISSION_ACROSS_NO_FLY_ZONE = new DJIMissionError("The mission is across the no fly zone", FTPCodes.SERVICE_CLOSING_CONTROL_CONNECTION);
    public static final DJIMissionError MISSION_CONDITION_NOT_SATISFIED = new DJIMissionError("The condition of mission is not satisfied", FTPCodes.SERVICE_READY_FOR_NEW_USER);
    public static final DJIMissionError MISSION_CONFLICT = new DJIMissionError("There is a conflicting setting in the mission", FTPCodes.FILE_STATUS);
    public static final DJIMissionError MISSION_ENTRY_POINT_INVALID = new DJIMissionError("The entry point of mission is invalid", CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256);
    public static final DJIMissionError MISSION_ESTIMATE_TIME_TOO_LONG = new DJIMissionError("The estimated time for the mission is too long", FTPCodes.HELP_MESSAGE);
    public static final DJIMissionError MISSION_HEADING_MODE_INVALID = new DJIMissionError("The heading mode of mission is invalid", CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256);
    public static final DJIMissionError MISSION_INFO_INVALID = new DJIMissionError("The information of mission is invalid", 224);
    public static final DJIMissionError MISSION_NOT_EXIST = new DJIMissionError("The mission does not exist", FTPCodes.DIRECTORY_STATUS);
    public static final DJIMissionError MISSION_NOT_INITIALIZED = new DJIMissionError("The mission is not initialized", 211);
    public static final DJIMissionError MISSION_NOT_STARTED = new DJIMissionError("Mission is not started yet");
    public static final DJIMissionError MISSION_PARAMETERS_INVALID = new DJIMissionError("The parameters of the mission are invalid", 219);
    public static final DJIMissionError MISSION_RADIUS_INVALID = new DJIMissionError("The radius of mission is invalid", CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256);
    public static final DJIMissionError MISSION_RADIUS_OVER_LIMIT = new DJIMissionError("The radius of mission is over the acceptable limit, pls try to login and check radius of waypoint", 199);
    public static final DJIMissionError MISSION_RESUME_FAILED = new DJIMissionError("Failed to resume the mission", 198);
    public static final DJIMissionError MISSION_SPEED_TOO_HIGH = new DJIMissionError("The speed of mission is too large", CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256);
    public static final DJIMissionError MODE_ERROR = new DJIMissionError("The control mode of the aircraft is not in the correct state", 5);
    public static final DJIMissionError MOTORS_DID_NOT_START = new DJIMissionError("The aircraft's motor has not started", 7);
    public static final DJIMissionError MULTI_MODE_DISABLED = new DJIMissionError("The mission cannot start when multi-mode is disabled.", XMPError.BADXMP);
    public static final DJIMissionError MULTI_MODE_IS_OFF = new DJIMissionError("Multi-mode switch is not turned on", XMPError.BADXMP);
    public static final DJIMissionError NAVIGATION_MODE_DISABLED = new DJIMissionError("Navigation is not open", 209);
    public static final DJIMissionError NAVIGATION_MODE_NOT_SUPPORTED = new DJIMissionError("Navigation mode is not supported", 200);
    public static final DJIMissionError NOT_AUTO_MODE = new DJIMissionError("Aircraft is not in auto mode", 10);
    public static final DJIMissionError NO_MISSION_RUNNING = new DJIMissionError("Mission not existed");
    public static final DJIMissionError NO_VIDEO_FEED = new DJIMissionError("No live video feed is captured for the ActiveTrack Mission. ");
    public static final DJIMissionError NULL_MISSION = new DJIMissionError("Null Mission");
    public static final DJIMissionError OBSTACLE_ENCOUNTERED_WHILE_ESTIMATING = new DJIMissionError("Obstacles encountered while estimating, current mission quitted.", 5319);
    public static final DJIMissionError OBSTACLE_ENCOUNTERED_WHILE_SURROUNDING = new DJIMissionError("Obstacles encountered while surrounding, current mission suspended.", 5320);
    public static final DJIMissionError POINTING_AIRCRAFT_NOT_IN_THE_AIR = new DJIMissionError("The aircraft is not in the air. Please take off first.");
    public static final DJIMissionError RADIUS_LIMIT = new DJIMissionError("The aircraft reach the distance limit.Please operate the aircraft within the distance limit.", 5315);
    public static final DJIMissionError RC_MODE_ERROR = new DJIMissionError("Mode error, please make sure the remote controller's mode switch is in 'F' mode.", CanonMakernoteDirectory.TAG_VRD_OFFSET);
    public static final DJIMissionError REACH_ALTITUDE_LOWER_BOUND = new DJIMissionError("The aircraft reaches the altitude lower bound of the TapFly Mission.");
    public static final DJIMissionError REACH_FLIGHT_LIMITATION = new DJIMissionError("The aircraft has reached the flight limitation.");
    public static final DJIMissionError REPEAT_TIME_NOT_VALID = new DJIMissionError("Repeat time value provided is invalid");
    public static final DJIMissionError REPROJECTION_FAILED_WHILE_EXECUTING = new DJIMissionError("Failed to measure, please try again.", 5306);
    public static final DJIMissionError ROTATE_AIRCRAFT_ACTION_NOT_VALID = new DJIMissionError("Waypoint rotate aircraft action param provided is invalid");
    public static final DJIMissionError ROTATE_GIMBAL_ACTION_NOT_VALID = new DJIMissionError("Waypoint rotate gimbal action param provided is invalid");
    public static final DJIMissionError RTK_IS_NOT_READY = new DJIMissionError("RTK is not ready", 205);
    public static final DJIMissionError RUNNING_MISSION = new DJIMissionError("The aircraft is running a mission");
    public static final DJIMissionError SHOOT_PHOTO_NOT_VALID = new DJIMissionError("Waypoint shoot photo distance provided is invalid");
    public static final DJIMissionError STAY_ACTION_NOT_VALID = new DJIMissionError("Waypoint stay action param provided is invalid");
    public static final DJIMissionError STOPPED_BY_USER = new DJIMissionError("Mission was stopped by the user.");
    public static final DJIMissionError SYSTEM_ABNORMAL = new DJIMissionError("System error, please try again. If the error continues, restart the aircraft.", 5301);
    public static final DJIMissionError TAKE_OFF = new DJIMissionError("Aircraft is taking off", 8);
    public static final DJIMissionError TAKE_OFF_FAILURE_CAUSED_BY_ATTITUDE = new DJIMissionError("The aircraft cannot take off because of improper aircraft's attitude.", 179);
    public static final DJIMissionError TARGET_AREA_IS_TOO_SMALL = new DJIMissionError("Frame selection subject too small.", 5302);
    public static final DJIMissionError TARGET_IS_TOO_FAR_AWAY = new DJIMissionError("Target is too far away, the aircraft needs to continue to measure distance", 5321);
    public static final DJIMissionError TARGET_SIZE_CHANGED = new DJIMissionError("Target size changed, please try again. If the error persists, restart the aircraft.", 5308);
    public static final DJIMissionError TARGET_TOO_CLOSE = new DJIMissionError("Frame subject too close.", 5307);
    public static final DJIMissionError TIMEOUT = new DJIMissionError("Execution of this process has timed out", 4);
    public static final DJIMissionError TOO_CLOSE_TO_HOME_POINT = new DJIMissionError("Aircraft is too close to home point", 160);
    public static final DJIMissionError TRACKING_GIMBAL_PITCH_TOO_LOW = new DJIMissionError("The gimbal pitch is too low.");
    public static final DJIMissionError TRACKING_OBSTACLE_DETECTED = new DJIMissionError("Obstacles are detected.");
    public static final DJIMissionError TRACKING_PAUSED_BY_USER = new DJIMissionError("Mission is paused by user.");
    public static final DJIMissionError TRACKING_RECT_TOO_LARGE = new DJIMissionError("The tracking rectangle is too large.");
    public static final DJIMissionError TRACKING_RECT_TOO_SMALL = new DJIMissionError("The tracking rectangle is too small.");
    public static final DJIMissionError TRACKING_TARGET_LOST = new DJIMissionError("The tracking target is lost.");
    public static final DJIMissionError TRACKING_TARGET_LOW_CONFIDENCE = new DJIMissionError("The ActiveTrack mission is too unsure the tracking object and confirmation is required.");
    public static final DJIMissionError TRACKING_TARGET_NOT_ENOUGH_FEATURES = new DJIMissionError("The tracking target doesn't have enough features to lock onto.");
    public static final DJIMissionError TRACKING_TARGET_SHAKING = new DJIMissionError("The tracking target is shaking too much.");
    public static final DJIMissionError TRACKING_TARGET_TOO_CLOSE = new DJIMissionError("The tracking target is too close to the aircraft.");
    public static final DJIMissionError TRACKING_TARGET_TOO_FAR = new DJIMissionError("The tracking target is too far away from the aircraft.");
    public static final DJIMissionError TRACKING_TARGET_TOO_HIGH = new DJIMissionError("The tracking target is too high.");
    public static final DJIMissionError UNKNOWN = new DJIMissionError("Unknown result");
    public static final DJIMissionError UPLOADING_WAYPOINT = new DJIMissionError("Waypoint mission is uploading", 233);
    public static final DJIMissionError USER_CANCEL_UPLOADING_MISSION = new DJIMissionError("Uploading mission has been canceled!");
    public static final DJIMissionError VIDEO_FRAME_RATE_TOO_LOW = new DJIMissionError("The frame rate of the live video feed is too low.");
    public static final DJIMissionError VISION_DATA_ABNORMAL = new DJIMissionError("The data from the vision system is abnormal.");
    public static final DJIMissionError VISION_SENSOR_LOW_QUALITY = new DJIMissionError("The quality of vision sensor is low.");
    public static final DJIMissionError VISION_SENSOR_OVEREXPOSED = new DJIMissionError("The vision sensors are overexposed.");
    public static final DJIMissionError VISION_SENSOR_UNDEREXPOSED = new DJIMissionError("The vision sensors are underexposed.");
    public static final DJIMissionError VISION_SYSTEM_ERROR = new DJIMissionError("The vision system encounters system error.");
    public static final DJIMissionError VISION_SYSTEM_NEEDS_CALIBRATION = new DJIMissionError("The vision system requires calibration.");
    public static final DJIMissionError VISION_SYSTEM_NOT_AUTHORIZED = new DJIMissionError("The vision system cannot get the authorization to control the aircraft. ");
    public static final DJIMissionError WATCH_TARGET_FAILED = new DJIMissionError("Gimbal reached movement limit, subjecting failed. Please try again.", 5311);
    public static final DJIMissionError WAYPOINTS_UPLOADING = new DJIMissionError("The waypoints are still uploading", 233);
    public static final DJIMissionError WAYPOINT_ACTION_PARAMETER_INVALID = new DJIMissionError("The parameter of waypoint action is invalid", 232);
    public static final DJIMissionError WAYPOINT_COORDINATE_NOT_VALID = new DJIMissionError("Waypoint coordinate provided is invalid");
    public static final DJIMissionError WAYPOINT_COUNT_NOT_VALID = new DJIMissionError("Waypoint count is invalid");
    public static final DJIMissionError WAYPOINT_DAMPING_CHECK_FAILED = new DJIMissionError("The damping check is failed", 231);
    public static final DJIMissionError WAYPOINT_DISTANCE_TOO_CLOSE = new DJIMissionError("The waypoint distance is too close", 229);
    public static final DJIMissionError WAYPOINT_DISTANCE_TOO_LONG = new DJIMissionError("The waypoint distance is too long", FTPCodes.USER_LOGGED_IN);
    public static final DJIMissionError WAYPOINT_GET_INTERRUPTION_FAILURE_FOR_COMPLETE_MISSION = new DJIMissionError("The waypoint mission interruption is not available because mission has completed.");
    public static final DJIMissionError WAYPOINT_GET_INTERRUPTION_FAILURE_WITHOUT_REACHING_FIRST_WAYPOINT = new DJIMissionError("The waypoint mission interruption is not available because the aircraft didn't reach the first waypoint in the last mission.");
    public static final DJIMissionError WAYPOINT_IDLE_VELOCITY_INVALID = new DJIMissionError("The idle velocity is invalid", 238);
    public static final DJIMissionError WAYPOINT_INDEX_OVER_RANGE = new DJIMissionError("The index of waypoint is over range", 228);
    public static final DJIMissionError WAYPOINT_INFO_INVALID = new DJIMissionError("The information of waypoint is invalid", FTPCodes.DATA_CONNECTION_OPEN);
    public static final DJIMissionError WAYPOINT_LIST_SIZE_NOT_VALID = new DJIMissionError("Waypoint list size is invalid");
    public static final DJIMissionError WAYPOINT_MISSION_INFO_NOT_UPLOADED = new DJIMissionError("The info of waypoint mission is not completely uploaded", 234);
    public static final DJIMissionError WAYPOINT_NOT_RUNNING = new DJIMissionError("The waypoint mission is not running", 237);
    public static final DJIMissionError WAYPOINT_REQUEST_IS_RUNNING = new DJIMissionError("The waypoint request is running", 236);
    public static final DJIMissionError WAYPOINT_SPEED_NOT_VALID = new DJIMissionError("Waypoint speed provided is invalid");
    public static final DJIMissionError WAYPOINT_TOTAL_TRACE_TOO_LONG = new DJIMissionError("The total trace of waypoint is too long", FTPCodes.ENTER_PASSIVE_MODE);
    public static final DJIMissionError WAYPOINT_TRACE_TOO_LONG = new DJIMissionError("The trace of waypoint is too long", FTPCodes.DATA_CONNECTION_CLOSING);
    public static final DJIMissionError WAYPOINT_UPLOAD_NOT_COMPLETE = new DJIMissionError("The waypoint uploading is not complete", 235);
    public static final DJIMissionError WRONG_CMD = new DJIMissionError("The command is wrong", 244);
    public static final DJIMissionError Z30_ONLY_SUPPORT_SPOTLIGHT_PRO = new DJIMissionError("Current Camera only support spotlight pro mode!");
    private int mErrorCode = -1;

    /* renamed from: dji.common.error.DJIMissionError$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$dji$midware$data$config$P3$Ccode = new int[Ccode.values().length];
    }

    private DJIMissionError(String desc) {
        super(desc);
    }

    private DJIMissionError(String desc, int errorCode) {
        super(desc, errorCode);
    }

    public static DJIError getDJIError(Ccode ccode) {
        if (COMMON_UNKNOWN != DJIError.getDJIError(ccode)) {
            return DJIError.getDJIError(ccode);
        }
        int i = AnonymousClass1.$SwitchMap$dji$midware$data$config$P3$Ccode[ccode.ordinal()];
        return COMMON_UNKNOWN;
    }

    public static DJIError getDJIErrorByCode(int code) {
        switch (code) {
            case 0:
                return null;
            case 1:
                return BEGAN;
            case 2:
                return CANCELED;
            case 3:
                return FAILED;
            case 4:
                return TIMEOUT;
            case 5:
                return MODE_ERROR;
            case 6:
                return GPS_NOT_READY;
            case 7:
                return MOTORS_DID_NOT_START;
            case 8:
                return TAKE_OFF;
            case 9:
                return IS_FLYING;
            case 10:
                return NOT_AUTO_MODE;
            case 11:
                return MAX_NUMBER_OF_WAYPOINTS_UPLOAD_LIMIT_REACHED;
            case 12:
                return UPLOADING_WAYPOINT;
            case 13:
                return KEY_LEVEL_LOW;
            case 15:
                return NAVIGATION_MODE_DISABLED;
            case 160:
                return TOO_CLOSE_TO_HOME_POINT;
            case 161:
                return IOC_TYPE_UNKNOWN;
            case 162:
                return HOME_POINT_VALUE_INVALID;
            case 163:
                return HOME_POINT_LOCATION_INVALID;
            case 166:
                return HOME_POINT_DIRECTION_UNKNOWN;
            case 169:
                return HOME_POINT_MISSION_PAUSED;
            case 170:
                return HOME_POINT_MISSION_NOT_PAUSED;
            case 172:
                return INDICES_ARE_DISCONTINUOUS;
            case 173:
                return INDEX_OUT_OF_RANGE;
            case 175:
                return INVALID_DATA_SIZE;
            case 176:
                return FOLLOW_ME_DISTANCE_TOO_LARGE;
            case 177:
                return FOLLOW_ME_DISCONNECT_TIME_TOO_LONG;
            case 178:
                return FOLLOW_ME_GIMBAL_PITCH_ERROR;
            case 179:
                return TAKE_OFF_FAILURE_CAUSED_BY_ATTITUDE;
            case 180:
                return INVALID_INTERVAL_SHOOT_PARAM;
            case 192:
                return ALTITUDE_TOO_HIGH;
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*193*/:
                return ALTITUDE_TOO_LOW;
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*194*/:
                return MISSION_RADIUS_INVALID;
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*195*/:
                return MISSION_SPEED_TOO_HIGH;
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*196*/:
                return MISSION_ENTRY_POINT_INVALID;
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256 /*197*/:
                return MISSION_HEADING_MODE_INVALID;
            case 198:
                return MISSION_RESUME_FAILED;
            case 199:
                return MISSION_RADIUS_OVER_LIMIT;
            case 200:
                return NAVIGATION_MODE_NOT_SUPPORTED;
            case XMPError.BADXML /*201*/:
                return DISTANCE_FROM_MISSION_TARGET_TOO_LONG;
            case 202:
                return IN_NOVICE_MODE;
            case XMPError.BADXMP /*203*/:
                return MULTI_MODE_DISABLED;
            case 205:
                return RTK_IS_NOT_READY;
            case CanonMakernoteDirectory.TAG_VRD_OFFSET /*208*/:
                return RC_MODE_ERROR;
            case 209:
                return NAVIGATION_MODE_DISABLED;
            case 210:
                return IOC_WORKING;
            case 211:
                return MISSION_NOT_INITIALIZED;
            case FTPCodes.DIRECTORY_STATUS /*212*/:
                return MISSION_NOT_EXIST;
            case FTPCodes.FILE_STATUS /*213*/:
                return MISSION_CONFLICT;
            case FTPCodes.HELP_MESSAGE /*214*/:
                return MISSION_ESTIMATE_TIME_TOO_LONG;
            case FTPCodes.NAME_SYSTEM_TIME /*215*/:
                return HIGH_PRIORITY_MISSION_EXECUTING;
            case 216:
                return GPS_SIGNAL_WEAK;
            case 217:
                return LOW_BATTERY;
            case 218:
                return AIRCRAFT_NOT_IN_THE_AIR;
            case 219:
                return MISSION_PARAMETERS_INVALID;
            case FTPCodes.SERVICE_READY_FOR_NEW_USER /*220*/:
                return MISSION_CONDITION_NOT_SATISFIED;
            case FTPCodes.SERVICE_CLOSING_CONTROL_CONNECTION /*221*/:
                return MISSION_ACROSS_NO_FLY_ZONE;
            case 222:
                return HOME_POINT_NOT_RECORDED;
            case 223:
                return AIRCRAFT_IN_NO_FLY_ZONE;
            case 224:
                return MISSION_INFO_INVALID;
            case FTPCodes.DATA_CONNECTION_OPEN /*225*/:
                return WAYPOINT_INFO_INVALID;
            case FTPCodes.DATA_CONNECTION_CLOSING /*226*/:
                return WAYPOINT_TRACE_TOO_LONG;
            case FTPCodes.ENTER_PASSIVE_MODE /*227*/:
                return WAYPOINT_TOTAL_TRACE_TOO_LONG;
            case 228:
                return WAYPOINT_INDEX_OVER_RANGE;
            case 229:
                return WAYPOINT_DISTANCE_TOO_CLOSE;
            case FTPCodes.USER_LOGGED_IN /*230*/:
                return WAYPOINT_DISTANCE_TOO_LONG;
            case 231:
                return WAYPOINT_DAMPING_CHECK_FAILED;
            case 232:
                return WAYPOINT_ACTION_PARAMETER_INVALID;
            case 233:
                return UPLOADING_WAYPOINT;
            case 234:
                return WAYPOINT_MISSION_INFO_NOT_UPLOADED;
            case 235:
                return WAYPOINT_UPLOAD_NOT_COMPLETE;
            case 236:
                return WAYPOINT_REQUEST_IS_RUNNING;
            case 237:
                return WAYPOINT_NOT_RUNNING;
            case 238:
                return WAYPOINT_IDLE_VELOCITY_INVALID;
            case 240:
                return AIRCRAFT_TAKING_OFF;
            case 241:
                return AIRCRAFT_LANDING;
            case 242:
                return AIRCRAFT_GOING_HOME;
            case 243:
                return AIRCRAFT_STARTING_MOTOR;
            case 244:
                return WRONG_CMD;
            case 255:
                return UNKNOWN;
            case 5301:
                return SYSTEM_ABNORMAL;
            case 5302:
                return TARGET_AREA_IS_TOO_SMALL;
            case 5303:
                return INSUFFICIENT_FEATURES_IN_TARGET_AREA;
            case 5304:
                return LOST_TARGET_WHILE_WATCHING;
            case 5305:
                return LOST_TARGET_WHILE_EXECUTING;
            case 5306:
                return REPROJECTION_FAILED_WHILE_EXECUTING;
            case 5307:
                return TARGET_TOO_CLOSE;
            case 5308:
                return TARGET_SIZE_CHANGED;
            case 5309:
                return GPS_ABNORMAL;
            case 5310:
                return HOT_POINT_INVALID;
            case 5311:
                return WATCH_TARGET_FAILED;
            case 5312:
                return ESTIMATE_TIMEOUT;
            case 5313:
                return ESTIMATE_RESULT_INVALID;
            case 5314:
                return EXIT_BY_USER_BUTTON;
            case 5315:
                return RADIUS_LIMIT;
            case 5316:
                return ALTITUDE_LIMIT;
            case 5317:
                return CONTROL_FAILED;
            case 5318:
                return LOST_CONNECTION_WITH_REMOTE_CONTROLLER;
            case 5319:
                return OBSTACLE_ENCOUNTERED_WHILE_ESTIMATING;
            case 5320:
                return OBSTACLE_ENCOUNTERED_WHILE_SURROUNDING;
            case 5321:
                return TARGET_IS_TOO_FAR_AWAY;
            case 5322:
                return HOTPOINT_ALTITUDE_TOO_LOW;
            default:
                return UNKNOWN;
        }
    }
}
