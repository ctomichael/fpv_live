package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;

@EXClassNullAway
public class DJIFlightControllerError extends DJIError {
    public static final DJIFlightControllerError ALREADY_IN_THE_AIR = new DJIFlightControllerError("If the motors are already turned on or the aircraft is already flying, the takeoff() did not execute.");
    public static final DJIFlightControllerError BASE_STATION_FELL = new DJIFlightControllerError("The base station fell. Land the aircraft to re-establish the connection to the ground station.");
    public static final DJIFlightControllerError BASE_STATION_IS_MOVED = new DJIFlightControllerError("The base station is moved. Land the aircraft to re-establish the connection to the ground station.");
    public static final DJIFlightControllerError BASE_STATION_NOT_ACTIVATED = new DJIFlightControllerError(" The base station isn't activated.");
    public static final DJIFlightControllerError CANNOT_TURN_OFF_MOTORS_WHILE_AIRCRAFT_FLYING = new DJIFlightControllerError("The aircraft is flying and the motors could not be shut down.");
    public static final DJIFlightControllerError COULD_NOT_ENTER_TRANSPORT_MODE = new DJIFlightControllerError("Aircraft could not enter transport mode, since the gimbal is still connected");
    public static final DJIFlightControllerError DATA_VALIDATION_FAILED = new DJIFlightControllerError("Data validation failed.");
    public static final DJIFlightControllerError DIFFERENT_FLY_ZONES_COVERED = new DJIFlightControllerError("At least one No-fly zone covers a fly zone or vice versa.");
    public static final DJIFlightControllerError DIFFERENT_FLY_ZONES_OVERLAPPING = new DJIFlightControllerError("No-fly zones and fly zones has overlapping area.");
    public static final DJIFlightControllerError FAIL_TO_ENTER_TRANSPORT_MODE_WHEN_MOTORS_ON = new DJIFlightControllerError("When the motors are on, the aircraft could not get into transport mode.");
    public static final DJIFlightControllerError FLY_ZONE_COVERED = new DJIFlightControllerError("At least one fly zone is covered by the other fly zone.");
    public static final DJIFlightControllerError FLY_ZONE_OVERLAPPING = new DJIFlightControllerError("Fly zones with overlapping area.");
    public static final DJIFlightControllerError GO_HOME_ALTITUDE_HIGHER_THAN_MAX_FLIGHT_HEIGHT = new DJIFlightControllerError("The go home altitude is too high (higher than max flight height).");
    public static final DJIFlightControllerError GO_HOME_ALTITUDE_TOO_HIGH = new DJIFlightControllerError("The go home altitude is too high.");
    public static final DJIFlightControllerError GO_HOME_ALTITUDE_TOO_LOW = new DJIFlightControllerError("The go home altitude is too low (lower than 20m).");
    public static final DJIFlightControllerError GPS_SIGNAL_WEAK = new DJIFlightControllerError("GPS level is too weak to allow flight controller to obtain accurate location.");
    public static final DJIFlightControllerError HOME_POINT_GPS_SIGNAL_WEAK = new DJIFlightControllerError("Aircraft GPS signal weak. Refreshing home point failed. Check and try again.");
    public static final DJIFlightControllerError HOME_POINT_INITIALIZING = new DJIFlightControllerError("Refreshing initial home point. Wait and try again later.");
    public static final DJIFlightControllerError HOME_POINT_INVALID_COORDINATE = new DJIFlightControllerError("New home point latitude and longitude invalid. Updating home point failed.");
    public static final DJIFlightControllerError HOME_POINT_TOO_FAR = new DJIFlightControllerError("Location is not within 30M of initial take-off location OR current RC location.");
    public static final DJIFlightControllerError HOME_POINT_UNKNOWN_FAILED_REASON = new DJIFlightControllerError("Updating home point failed.");
    public static final DJIFlightControllerError IMU_CALIBRATION_ERROR_IN_THE_AIR_OR_MOTORS_ON = new DJIFlightControllerError("IMU calibration is now allowed if the aircraft's motors are on or the aircraft is in the air.");
    public static final DJIFlightControllerError INVALID_PARAMETER = new DJIFlightControllerError("FlightController received invalid parameters");
    public static final DJIFlightControllerError NO_FLY_ZONE_COVERED = new DJIFlightControllerError("At least one no-fly zone is covered by the other no-fly-zone.");
    public static final DJIFlightControllerError NO_FLY_ZONE_OVERLAPPING = new DJIFlightControllerError("No-fly zones with overlapping area.");
    public static final DJIFlightControllerError PRECISION_TAKE_OFF_NOT_SUPPORT = new DJIFlightControllerError("Precision takeoff not support");
    public static final DJIFlightControllerError RTK_BS_ANTENNA_ERROR = new DJIFlightControllerError("The RTK base station antenna is broken.");
    public static final DJIFlightControllerError RTK_BS_COORDINATE_RESET = new DJIFlightControllerError("The RTK base station location has been reset");
    public static final DJIFlightControllerError RTK_CANNOT_START = new DJIFlightControllerError("The RTK starting is failed.");
    public static final DJIFlightControllerError RTK_CONNECTION_BROKEN = new DJIFlightControllerError("The RTK connection is lost.");
    public static final DJIFlightControllerError RTK_INTIALIZING = new DJIFlightControllerError("The RTK is initializing.");
    public static final DJIFlightControllerError RTK_NOT_ACTIVATED = new DJIFlightControllerError("The RTK base Station not activated.");
    public static final DJIFlightControllerError RTK_RTCM_TYPE_CHANGE = new DJIFlightControllerError("The RTK rtcm type change");
    public static final DJIFlightControllerError RTK_STATION_OBLIQUE = new DJIFlightControllerError("The RTK station oblique");
    public static final DJIFlightControllerError RTK_STATION_POSITON_CHANGE = new DJIFlightControllerError("The RTK station position change");
    public static final DJIFlightControllerError VIRTUAL_FENCE_DATA_NOT_AVAILABLE = new DJIFlightControllerError("Please set virtual fence area before enabling it.");
    public static final DJIFlightControllerError VIRTUAL_FENCE_DISABLED_ALREADY = new DJIFlightControllerError("Virtual fence is disabled already");
    public static final DJIFlightControllerError VIRTUAL_FENCE_ENABLED_ALREADY = new DJIFlightControllerError("Virtual fence is enabled already");
    public static final DJIFlightControllerError WHITE_LIST_FAILED_TO_CHECK_SN = new DJIFlightControllerError("SN is not matching.");
    public static final DJIFlightControllerError WHITE_LIST_FAIL_TO_CHECK_SN = new DJIFlightControllerError("SN is not matching.");
    public static final DJIFlightControllerError WHITE_LIST_FILE_SIZE_ERROR = new DJIFlightControllerError("The file size pushed to aircraft is wrong");
    public static final DJIFlightControllerError WHITE_LIST_FLYC_VERSION_NOT_MATCH = new DJIFlightControllerError("The flight controller firmware is not support this feature.");
    public static final DJIFlightControllerError WHITE_LIST_FLYC_VERSION_NOT_MATCHED = new DJIFlightControllerError("The flight controller firmware is not support this feature.");
    public static final DJIFlightControllerError WHITE_LIST_ILLEGAL_INDEX = new DJIFlightControllerError("Illegal index");
    public static final DJIFlightControllerError WHITE_LIST_INCORRECT_CRC = new DJIFlightControllerError("CRC is not correct");
    public static final DJIFlightControllerError WHITE_LIST_LICENSE_IS_INVALID = new DJIFlightControllerError("The license is invalid");
    public static final DJIFlightControllerError WHITE_LIST_LICENSE_NOT_SUPPORT = new DJIFlightControllerError("The license is not support");
    public static final DJIFlightControllerError WHITE_LIST_LICENSE_NOT_SUPPORTED = new DJIFlightControllerError("The license is not support");
    public static final DJIFlightControllerError WHITE_LIST_NEED_TO_CONNECT_NEWTORK = new DJIFlightControllerError("The license need to be verified through network");
    public static final DJIFlightControllerError WHITE_LIST_NOT_FETCHED_LICENSE = new DJIFlightControllerError("Fetch the white list license from aircraft before calling getWhiteListLicense()");
    public static final DJIFlightControllerError WHITE_LIST_NO_FETCHED_LICENSE = new DJIFlightControllerError("Fetch the white list license from aircraft before calling getWhiteListLicense()");
    public static final DJIFlightControllerError WHITE_LIST_OPERATE_CODE_ERROR = new DJIFlightControllerError("Operation code error");
    public static final DJIFlightControllerError WHITE_LIST_OPERATION_CODE_ERROR = new DJIFlightControllerError("Operation code error");
    public static final DJIFlightControllerError WHITE_LIST_REQ_ID_NOT_MATCH = new DJIFlightControllerError("Request ID does not match");
    public static final DJIFlightControllerError WHITE_LIST_REQ_ID_NOT_MATCHED = new DJIFlightControllerError("Request ID does not match");

    private DJIFlightControllerError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(Ccode ccode) {
        if (COMMON_UNKNOWN != DJIError.getDJIError(ccode)) {
            return DJIError.getDJIError(ccode);
        }
        switch (ccode) {
            case TIMEOUT:
                return COMMON_TIMEOUT;
            default:
                return COMMON_UNKNOWN;
        }
    }

    public static DJIError getSetVirtualFenceErrorCode(int errorCode) {
        switch (errorCode) {
            case 1:
                return DATA_VALIDATION_FAILED;
            case 2:
                return NO_FLY_ZONE_OVERLAPPING;
            case 3:
                return NO_FLY_ZONE_COVERED;
            case 4:
                return FLY_ZONE_OVERLAPPING;
            case 5:
                return FLY_ZONE_COVERED;
            case 6:
                return DIFFERENT_FLY_ZONES_OVERLAPPING;
            case 7:
                return DIFFERENT_FLY_ZONES_COVERED;
            default:
                return COMMON_UNKNOWN;
        }
    }

    public static DJIError getSetVirtualFenceEnabledErrorCode(int errorCode) {
        switch (errorCode) {
            case 1:
                return VIRTUAL_FENCE_DATA_NOT_AVAILABLE;
            case 2:
                return VIRTUAL_FENCE_ENABLED_ALREADY;
            case 3:
                return VIRTUAL_FENCE_DISABLED_ALREADY;
            default:
                return COMMON_UNKNOWN;
        }
    }

    public enum ErrorCode {
        ERROR_MOTOR_ON(1),
        ERROR_MOTOR_OFF(2),
        ERROR_IN_AIR(3),
        ERROR_NOT_IN_AIR(4),
        ERROR_HOMEPOINT_NOT_SET(5),
        ERROR_BAD_GPS(6),
        ERROR_IN_SIMULATION(7),
        ERROR_CMD_IS_RUNNING(8),
        ERROR_CMD_NOT_RUNNING(9),
        ERROR_CMD_INVALID(10),
        ERROR_NO_GEAR(11),
        ERROR_PACK_GIMBAL_MOUNTED(12),
        ERROR_PACK_US_NOT_HEALTHY(13),
        ERROR_PACK_HAS_PACKED(14),
        ERROR_NOT_PACKED(15),
        ERROR_NOT_SUPPORTED(16),
        ERROR_CANNOT_START_MOTOR(17),
        ERROR_LOW_VOLTAGE(18),
        ERROR_IN_HOVER(19),
        ERROR_SPEED_TOO_LARGE(20),
        ERROR_NO_HORIZ_VEL_INFO(21),
        OTHER(255);
        
        int mValue;

        private ErrorCode(int value) {
            this.mValue = value;
        }

        /* access modifiers changed from: package-private */
        public ErrorCode find(int value) {
            ErrorCode target = OTHER;
            ErrorCode[] values = values();
            for (ErrorCode error : values) {
                if (error.mValue == value) {
                    target = error;
                }
            }
            return target;
        }
    }
}
