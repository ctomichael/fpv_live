package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.base.DJICommonDataBase;
import dji.midware.data.packages.P3.Pack;
import dji.midware.util.NewCheckStatusHelper;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;

@Keep
@EXClassNullAway
public class Data2100GetPushCheckStatus extends DJICommonDataBase {
    private static Data2100GetPushCheckStatus instance = null;

    public static synchronized Data2100GetPushCheckStatus getInstance() {
        Data2100GetPushCheckStatus data2100GetPushCheckStatus;
        synchronized (Data2100GetPushCheckStatus.class) {
            if (instance == null) {
                instance = new Data2100GetPushCheckStatus();
            }
            data2100GetPushCheckStatus = instance;
        }
        return data2100GetPushCheckStatus;
    }

    public boolean hasException() {
        if (isForeSightLeftAbnormal() || isForeSightRightAbnormal() || isDownSightLeftAbnormal() || isDownSightRightAbnormal() || isDownSightDemarkAbnormal() || isForeSightDemarkAbnormal() || isBackSightLeftAbnormal() || isBackSightRightAbnormal() || isBackSightDemarkAbnormal() || isStoreAbnormal() || is1860UsbAbnormal() || isMCUARTAbnormal() || isSwaveAbnormal() || isInnerAbnormal() || isAutoExpAbnormal() || isDepthImageAbnormal() || isVOAbnormal() || isAvoidanceAbnormal() || isCPLDConnAbnormal() || isMCUARTSendAbnormal() || isLRTAbnormal() || isPropellerCover() || isEasySelfCalResult() || needPcCalibrate() || isObstacleDetectedFromLeft() || isObstacleDetectedFromRight()) {
            return true;
        }
        return false;
    }

    public boolean hasVisionError() {
        return isAutoExpAbnormal() || isDepthImageAbnormal() || isVOAbnormal() || isAvoidanceAbnormal() || isStoreAbnormal() || isInnerAbnormal() || isLRTAbnormal();
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (pack != null && pack.data != null && pack.data.length == 21) {
            return;
        }
        if (pack != null && pack.cmdId == CmdIdCommon.CmdIdType.GetPushCheckStatus.value()) {
            super.setPushRecPack(pack);
        } else if (pack != null && pack.cmdId == CmdIdCommon.CmdIdType.GetPushCheckStatusV2.value()) {
            NewCheckStatusHelper.findNewCheckStatus(pack);
        }
    }

    public boolean isDownSightLeftAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isDownSightRightAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isForeSightLeftAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 4) != 0;
    }

    public boolean isForeSightRightAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 8) != 0;
    }

    public boolean isDownUltraTOFAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 16) != 0;
    }

    public boolean isUpUltraTOFAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 32) != 0;
    }

    public boolean isBackSightLeftAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 64) != 0;
    }

    public boolean isBackSightRightAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 128) != 0;
    }

    public boolean isLeft3DTOFSensorAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 256) != 0;
    }

    public boolean isRight3DTOFSensorAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 512) != 0;
    }

    public boolean isDownSightDemarkAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1024) != 0;
    }

    public boolean isForeSightDemarkAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 2048) != 0;
    }

    public boolean leftLSCStatus() {
        if (DJIProductManager.getInstance().getType() == ProductType.WM240 || DJIProductManager.getInstance().getType() == ProductType.WM245) {
            return false;
        }
        return (((Integer) get(0, 4, Integer.class)).intValue() & 4096) != 0;
    }

    public boolean isBackSightDemarkAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 8192) != 0;
    }

    public boolean doesBottomTofHaveCalibrationError() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 64) != 0;
    }

    public boolean isStoreAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 32768) != 0;
    }

    public boolean rightLSCStatus() {
        if (DJIProductManager.getInstance().getType() == ProductType.WM240 || DJIProductManager.getInstance().getType() == ProductType.WM245) {
            return false;
        }
        return (((Integer) get(0, 4, Integer.class)).intValue() & 32768) != 0;
    }

    public boolean is1860UsbAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 65536) != 0;
    }

    public boolean isMCUARTAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 131072) != 0;
    }

    public boolean isSwaveAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 262144) != 0;
    }

    public boolean tofVersionStatus() {
        if (DJIProductManager.getInstance().getType() == ProductType.WM240 || DJIProductManager.getInstance().getType() == ProductType.WM245) {
            return false;
        }
        return (((Integer) get(0, 4, Integer.class)).intValue() & 262144) != 0;
    }

    public boolean isInnerAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 524288) != 0;
    }

    public boolean isAutoExpAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1048576) != 0;
    }

    public boolean isDepthImageAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 2097152) != 0;
    }

    public boolean isVOAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 4194304) != 0;
    }

    public boolean isAvoidanceAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 8388608) != 0;
    }

    public boolean isCPLDConnAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 16777216) != 0;
    }

    public boolean isMCUARTSendAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 33554432) != 0;
    }

    public boolean isLRTAbnormal() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 67108864) != 0;
    }

    public boolean isPropellerCover() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 134217728) != 0;
    }

    public boolean isEasySelfCalResult() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & SQLiteDatabase.CREATE_IF_NECESSARY) != 0;
    }

    public boolean needPcCalibrate() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 536870912) != 0;
    }

    public boolean isObstacleDetectedFromLeft() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1073741824) != 0;
    }

    public boolean isObstacleDetectedFromRight() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & Integer.MIN_VALUE) != 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
