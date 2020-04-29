package dji.midware.data.config.P3;

import com.adobe.xmp.XMPError;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import it.sauronsoftware.ftp4j.FTPCodes;
import org.bouncycastle.crypto.tls.CipherSuite;

@EXClassNullAway
public enum NavigationErrCode {
    ERR_TooCloseToHomePoint(160),
    ERR_IOC_UNKNOW_TYPE(161),
    ERR_HP_INVALID_FLOAT_VALUE(162),
    ERR_HP_INVALID_LAT_LONTI(163),
    ERR_HP_UNKNOW_DIRECTION(166),
    ERR_HP_IS_PAUSED(169),
    ERR_HP_NOT_PAUSED(170),
    ERR_RCModeError(CanonMakernoteDirectory.TAG_VRD_OFFSET),
    ERR_MCModeError(209),
    ERR_IOCWorking(210),
    ERR_MissionNotInit(211),
    ERR_MissionNotExist(FTPCodes.DIRECTORY_STATUS),
    ERR_MissionConflict(FTPCodes.FILE_STATUS),
    ERR_MissionEstimateTimeTooLong(FTPCodes.HELP_MESSAGE),
    ERR_HighPriorityMissionExecuting(FTPCodes.NAME_SYSTEM_TIME),
    ERR_GpsSignalWeak(216),
    ERR_LowBattery(217),
    ERR_AircraftNotInTheAir(218),
    ERR_MissionParamInvalid(219),
    ERR_MissionConditionNotSatisfied(FTPCodes.SERVICE_READY_FOR_NEW_USER),
    ERR_MissionAcrossNoFlyZone(FTPCodes.SERVICE_CLOSING_CONTROL_CONNECTION),
    ERR_HomePointNotRecorded(222),
    ERR_AircraftInNoFlyZone(223),
    ERR_AltitudeTooHigh(192),
    ERR_AltitudeTooLow(CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256),
    ERR_MissionRadiusInvalid(CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256),
    ERR_MissionSpeedTooLarge(CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256),
    ERR_MissionEntryPointInvalid(CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256),
    ERR_MissionHeadingModeInvalid(CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256),
    ERR_MissionResumeFailed(198),
    ERR_MissionRadiusOverLimited(199),
    ERR_INVALID_PRODUCT(200),
    ERR_DISTANCE_TOO_LONG(XMPError.BADXML),
    ERR_FOR_IN_NOVICE_MODE(202),
    ERR_BAD_RTK_SIGNAL(205),
    ERR_FM_DIST_TOO_LARGE(176),
    ERR_FM_UL_DISCONNECT(177),
    ERR_FM_ERROR_GIMBAL_PITCH(178),
    ERR_WP_INFO_INVALID(224),
    ERR_WP_DATA_INVALID(FTPCodes.DATA_CONNECTION_OPEN),
    ERR_WP_TRACE_TOO_LONG(FTPCodes.DATA_CONNECTION_CLOSING),
    ERR_WP_TOTAL_TOO_LONG(FTPCodes.ENTER_PASSIVE_MODE),
    ERR_WP_DATA_INDEX_OVER_RANGE(228),
    ERR_WP_DIST_TOO_CLOSE(229),
    ERR_WP_DIST_TOO_FAR(FTPCodes.USER_LOGGED_IN),
    ERR_WP_DATA_DAMPING_CHECK_FAILED(231),
    ERR_WP_ACTION_DATA_INVALID(232),
    ERR_WP_HAVING_REMAINING_WP(233),
    ERR_WP_INFO_NOT_UPLOADED(234),
    ERR_WP_DATA_NOT_UPLOADED(235),
    ERR_WP_REQUEST_IS_RUNNING(236),
    ERR_WP_NOT_RUNNING_WP_FUNC(237),
    ERR_WP_IDLE_VAL_INVALID(238),
    ERR_FOR_TAKING_OFF(240),
    ERR_FOR_LANDING(241),
    ERR_FOR_GOING_HOME(242),
    ERR_FOR_ENGINE_STARTING(243),
    ERR_FOR_WRONG_CMD(244),
    ERR_FOR_NO_RELATIVE_POSITION(247),
    ERR_FOR_NO_HORIZONTAL_VELOCITY(248),
    ERR_UNDEFINED(255);
    
    private static NavigationErrCode[] items = values();
    private int data;

    private NavigationErrCode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static NavigationErrCode find(int b) {
        NavigationErrCode result = ERR_UNDEFINED;
        for (int i = 0; i < items.length; i++) {
            if (items[i]._equals(b)) {
                return items[i];
            }
        }
        return result;
    }
}
