package com.dji.csdkcommon;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DeviceEvent {
    public static final int DeviceConnectedStateChange = 4;
    public static final int DeviceFlightControllerConnect = 2;
    public static final int DeviceFlightControllerDisconnect = 3;
    public static final int DeviceLicenseUnlockStateChange = 15;
    public static final int DeviceLocationChange = 5;
    public static final int DeviceMCProtocolVersionChange = 13;
    public static final int DeviceModeStateChange = 14;
    public static final int DeviceMotorOnStateChange = 8;
    public static final int DeviceProductCodeChange = 16;
    public static final int DeviceRCModeStateChange = 6;
    public static final int DeviceRegister = 0;
    public static final int DeviceSerialNumberChange = 7;
    public static final int DeviceSupportDynamicDBStateChange = 9;
    public static final int DeviceTakeOffFailedInFlyLimitZone = 11;
    public static final int DeviceUnlockAreaIdsStateChange = 12;
    public static final int DeviceUnregister = 1;
    public static final int DeviceUseDJIFlightDBStateChange = 10;

    @Retention(RetentionPolicy.CLASS)
    public @interface DeviceEventParam {
    }
}
