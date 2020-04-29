package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import dji.fieldAnnotation.EXClassNullAway;
import dji.pilot.publics.model.ICameraResMode;
import it.sauronsoftware.ftp4j.FTPCodes;

@EXClassNullAway
public enum Ccode {
    TIMEOUT(256),
    OK(0),
    SUCCEED(1),
    NOGPS(128),
    CfgVersionNotMatch(205),
    CfgInvalid(206),
    CfgNotExisted(207),
    NOT_SUPPORT_FEATURE(217),
    INVALID_CMD(224),
    TIMEOUT_REMOTE(FTPCodes.DATA_CONNECTION_OPEN),
    OUT_OF_MEMORY(FTPCodes.DATA_CONNECTION_CLOSING),
    INVALID_PARAM(FTPCodes.ENTER_PASSIVE_MODE),
    NOT_SUPPORT_CURRENT_STATE(228),
    TIME_NOT_SYNC(229),
    SET_PARAM_FAILED(FTPCodes.USER_LOGGED_IN),
    GET_PARAM_FAILED(231),
    SDCARD_NOT_INSERTED(232),
    SDCARD_FULL(233),
    SDCARD_ERR(234),
    SENSOR_ERR(235),
    CAMERA_CRITICAL_ERR(236),
    PARAM_NOT_AVAILABLE(237),
    FM_NONSEQUENCE(240),
    FM_LENGTH_WRONG(241),
    DEVICE_LOW_POWER(251),
    FM_CRC_WRONG(242),
    FLASH_C_WRONG(243),
    FLASH_W_WRONG(244),
    UPDATE_WRONG(245),
    FIRM_MATCH_WRONG(246),
    UPDATE_WAIT_FINISH(247),
    UPDATE_MOTOR_WORKING(249),
    UPDATE_NOCONNECT_CAMERA(254),
    FLASH_FLUSHING(ICameraResMode.ICameraVideoResolutionRes.VR_MAX),
    UNDEFINED(255),
    NOCONNECT(FrameMetricsAggregator.EVERY_DURATION),
    NORECEIVE_PUSHDATA(510),
    USER_CANCEL(4095);
    
    private int data;
    private int value;

    private Ccode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public void relValue(int value2) {
        this.value = value2;
    }

    public int relValue() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static Ccode find(int b) {
        Ccode result = UNDEFINED;
        int i = 0;
        while (true) {
            if (i >= values().length) {
                break;
            } else if (values()[i]._equals(b)) {
                result = values()[i];
                break;
            } else {
                i++;
            }
        }
        result.relValue(b);
        return result;
    }
}
