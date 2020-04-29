package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICommonDataBase;
import dji.midware.data.packages.P3.Pack;

@Keep
@EXClassNullAway
public class Data1860GetPushCheckStatus extends DJICommonDataBase {
    private static Data1860GetPushCheckStatus instance = null;

    public static synchronized Data1860GetPushCheckStatus getInstance() {
        Data1860GetPushCheckStatus data1860GetPushCheckStatus;
        synchronized (Data1860GetPushCheckStatus.class) {
            if (instance == null) {
                instance = new Data1860GetPushCheckStatus();
            }
            data1860GetPushCheckStatus = instance;
        }
        return data1860GetPushCheckStatus;
    }

    public boolean hasException(boolean inner) {
        if (isSystemUpgradeAbnormal()) {
            return false;
        }
        if (isRebootStatusAbnormal() || isThreadMonitorAbnormal() || isVideoEncodeAbnormal() || isSystemStoreAbnormal() || isHPIAbnormal() || isCPLDI2CAbnormal() || isSwaveUARTAbnormal() || isVisualUSBAbnormal() || isVisualSPIAbnormal() || isUSBOTGAbnormal() || isUSBHubAbnormal() || isMCUSBAbnormal() || isMCUARTAbnormal() || isCameraUSBAbnormal() || isCameraMIPIAbnormal() || isThermalAbnormal() || (inner && isInnerAbnormal())) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (pack == null || pack.data == null || pack.data.length != 21) {
            super.setPushRecPack(pack);
        }
    }

    public int getStatus() {
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public boolean isRebootStatusAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isThreadMonitorAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isSystemUpgradeAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 4) != 0;
    }

    public boolean isVideoEncodeAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 8) != 0;
    }

    public boolean isSystemStoreAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 16) != 0;
    }

    public boolean isHPIAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 32) != 0;
    }

    public boolean isCPLDI2CAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 64) != 0;
    }

    public boolean isSwaveUARTAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 128) != 0;
    }

    public boolean isVisualUSBAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1792) != 0;
    }

    public boolean isVisualSPIAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 2048) != 0;
    }

    public boolean isUSBOTGAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 28672) != 0;
    }

    public boolean isUSBHubAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 32768) != 0;
    }

    public boolean isMCUSBAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 458752) != 0;
    }

    public boolean isMCUARTAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 524288) != 0;
    }

    public boolean isCameraDisconnected() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1048576) != 0;
    }

    public boolean isCameraUSBAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 7340032) != 0;
    }

    public boolean isCameraMIPIAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 8388608) != 0;
    }

    public boolean isThermalAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 16777216) != 0;
    }

    public int getThermalZone() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 50331648) >> 24;
    }

    public int getDeviceSubType() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 26) & 15;
    }

    public boolean isInnerAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1073741824) != 0;
    }

    public boolean isBatteryReleased() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & Integer.MIN_VALUE) != 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
