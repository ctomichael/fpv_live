package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.Pack;
import dji.midware.util.NewCheckStatusHelper;

@Keep
@EXClassNullAway
public class DataOsdGetPushCheckStatus extends DataBase {
    private static DataOsdGetPushCheckStatus instance = null;

    public static synchronized DataOsdGetPushCheckStatus getInstance() {
        DataOsdGetPushCheckStatus dataOsdGetPushCheckStatus;
        synchronized (DataOsdGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataOsdGetPushCheckStatus();
            }
            dataOsdGetPushCheckStatus = instance;
        }
        return dataOsdGetPushCheckStatus;
    }

    public boolean isOK() {
        return getFPGAinitStatus() || get58GinitStatus() || getF330initStatus() || getGPSinitStatus() || getEncryptStatus() || getStickMiddleStatus() || getPowerStatus() || getTimeoutStatus() || getResetStatus() || isInHighTemperature();
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (pack != null && pack.cmdId == CmdIdCommon.CmdIdType.GetPushCheckStatus.value()) {
            super.setPushRecPack(pack);
        } else if (pack != null && pack.cmdId == CmdIdCommon.CmdIdType.GetPushCheckStatusV2.value()) {
            NewCheckStatusHelper.findNewCheckStatus(pack);
        }
    }

    public boolean getStickMiddleStatus() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1) == 1;
    }

    public boolean isSenderRC() {
        return this.recvPack.senderType == DeviceType.OSD.value();
    }

    public boolean isRCRadioQualityLow() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 2) & 1) == 1;
    }

    public boolean isRCRadioQualityHigh() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 3) & 1) == 1;
    }

    public boolean isRCRadioQualityMedium() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 9) & 1) == 1;
    }

    public boolean getFPGAinitStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 1) & 1) == 1;
    }

    public boolean get58GinitStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 2) & 1) == 1;
    }

    public boolean getF330initStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 3) & 1) == 1;
    }

    public boolean getGPSinitStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 4) & 1) == 1;
    }

    public boolean getEncryptStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 5) & 1) == 1;
    }

    public boolean getResetStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 6) & 1) == 1;
    }

    public boolean getPowerStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 7) & 1) == 1;
    }

    public boolean getTimeoutStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 8) & 1) == 1;
    }

    public boolean isInHighTemperature() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 9) & 1) == 1;
    }

    public boolean getEncryptStatusWifiRc() {
        if (DJIProductManager.getInstance().getType() != ProductType.WM160 && DJIProductManager.getInstance().getType() != ProductType.WM230 && DJIProductManager.getInstance().getType() != ProductType.Mammoth) {
            return false;
        }
        return (((Integer) get(1, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean getStickMiddleStatusWifiRc() {
        if (DJIProductManager.getInstance().getType() != ProductType.WM160 && DJIProductManager.getInstance().getType() != ProductType.WM230 && DJIProductManager.getInstance().getType() != ProductType.Mammoth) {
            return false;
        }
        return (((Integer) get(2, 1, Integer.class)).intValue() & 4) != 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
