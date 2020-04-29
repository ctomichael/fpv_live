package dji.midware.data.model.P3;

import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;

public class DataOsdGetPushCheckStatusV2 extends DataBase {
    private static DataOsdGetPushCheckStatusV2 instance = null;

    public static synchronized DataOsdGetPushCheckStatusV2 getInstance() {
        DataOsdGetPushCheckStatusV2 dataOsdGetPushCheckStatusV2;
        synchronized (DataOsdGetPushCheckStatusV2.class) {
            if (instance == null) {
                instance = new DataOsdGetPushCheckStatusV2();
            }
            dataOsdGetPushCheckStatusV2 = instance;
        }
        return dataOsdGetPushCheckStatusV2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
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
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 6) & 1) == 1;
    }
}
