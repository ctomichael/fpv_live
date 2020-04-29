package dji.dbox.upgrade.p4.statemachine;

import android.content.Context;
import android.os.BatteryManager;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataRcGetPushBatteryInfo;
import java.util.Arrays;

public class DJIUpEnvironmentChecker {
    public static final int ENVIRONMENT_FAILED_INVALID = -1;
    public static final int ENVIRONMENT_FAILED_MOTOR_UP = -2;
    public static final int ENVIRONMENT_SUCCESS = 0;
    private static final int MIN_POWER_LEVEL_10 = 10;
    public static final int MIN_POWER_LEVEL_20 = 20;
    private static final int MIN_POWER_LEVEL_30 = 30;
    private static final int MIN_POWER_LEVEL_40 = 40;
    private static final String TAG = "DJIUpEnvironmentChecker";
    public static boolean isDebugForPower = false;

    static boolean checkMotorUp() {
        return ServiceManager.getInstance().isRemoteOK() && DataOsdGetPushCommon.getInstance().isMotorUp();
    }

    private static int get(int min) {
        if (isDebugForPower) {
            return 10;
        }
        return min;
    }

    public static int getMinPowerLevel(DJIUpDeviceType deviceType) {
        DJIUpDeviceType connectDevice = DJIUpStatusHelper.getConnectDeviceType();
        if (deviceType == DJIUpDeviceType.wm220 || deviceType == DJIUpDeviceType.wm221) {
            if (!ServiceManager.getInstance().isRemoteOK()) {
                return get(30);
            }
            return 40;
        } else if (DJIUpDeviceType.isRcSeries(deviceType)) {
            return get(20);
        } else {
            if (deviceType == DJIUpDeviceType.rc002 || deviceType == DJIUpDeviceType.rc230) {
                return get(30);
            }
            if (!DJIUpDeviceType.isUpTogether(deviceType)) {
                return 40;
            }
            if (connectDevice != deviceType) {
                if (deviceType.isMc()) {
                    return get(40);
                }
                return get(30);
            } else if (deviceType.isMc()) {
                return get(40);
            } else {
                return get(30);
            }
        }
    }

    private static int[] getCheckBatteries(DJIUpDeviceType deviceType, Context context) {
        int battery = DataFlycGetPushSmartBattery.getInstance().getBattery();
        int rcBattery = DataRcGetPushBatteryInfo.getInstance().getBattery();
        if (deviceType == DJIUpDeviceType.rm500) {
            rcBattery = ((BatteryManager) context.getSystemService("batterymanager")).getIntProperty(4);
        }
        DJIUpDeviceType connectDevice = DJIUpStatusHelper.getConnectDeviceType();
        int[] checkBatteries = {-1, -1};
        if (deviceType == DJIUpDeviceType.wm220 || deviceType == DJIUpDeviceType.wm221) {
            if (!ServiceManager.getInstance().isRemoteOK()) {
                checkBatteries[1] = rcBattery;
            } else if (DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null)) {
                checkBatteries[0] = battery;
            } else {
                checkBatteries[0] = battery;
                checkBatteries[1] = rcBattery;
            }
        } else if (DJIUpDeviceType.isRcSeries(deviceType)) {
            checkBatteries[1] = rcBattery;
        } else if (deviceType == DJIUpDeviceType.rc002 || deviceType == DJIUpDeviceType.rc230) {
            checkBatteries[1] = rcBattery;
        } else if (DJIUpDeviceType.isUpTogether(deviceType)) {
            if (connectDevice != deviceType) {
                if (deviceType.isMc()) {
                    checkBatteries[0] = battery;
                    checkBatteries[1] = rcBattery;
                } else {
                    checkBatteries[1] = rcBattery;
                }
            } else if (deviceType.isMc()) {
                checkBatteries[0] = battery;
            } else {
                checkBatteries[1] = rcBattery;
            }
        } else if (ServiceManager.getInstance().isRemoteOK()) {
            checkBatteries[0] = battery;
        }
        return checkBatteries;
    }

    public static int checkEnvironment(DJIUpDeviceType deviceType, Context context) {
        if (deviceType == null) {
            return -1;
        }
        if (checkMotorUp()) {
            return -2;
        }
        DJIUpDeviceType connectDevice = DJIUpStatusHelper.getConnectDeviceType();
        int[] checkBatteries = getCheckBatteries(deviceType, context);
        int minPowerLevel = getMinPowerLevel(deviceType);
        DJIUpConstants.LOGD(TAG, "check battery deviceType=" + deviceType + " connectDevice=" + connectDevice + " minPowerLevel=" + minPowerLevel + ":" + Arrays.toString(checkBatteries));
        if (checkBatteries[0] > 0 && checkBatteries[0] < minPowerLevel) {
            return minPowerLevel;
        }
        if (checkBatteries[1] <= 0 || checkBatteries[1] >= minPowerLevel) {
            return 0;
        }
        return minPowerLevel;
    }
}
