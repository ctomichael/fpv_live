package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.Pack;

@Keep
@EXClassNullAway
public class DataFlycGetPushCheckStatus extends DataBase {
    private static DataFlycGetPushCheckStatus instance = null;

    public static synchronized DataFlycGetPushCheckStatus getInstance() {
        DataFlycGetPushCheckStatus dataFlycGetPushCheckStatus;
        synchronized (DataFlycGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataFlycGetPushCheckStatus();
            }
            dataFlycGetPushCheckStatus = instance;
        }
        return dataFlycGetPushCheckStatus;
    }

    public boolean isOK() {
        if (getIMUAdvanceCaliStatus() || getIMUBasicCaliStatus() || getIMUHorizontalCaliStatus() || getVersionStatus() || getIMUDirectionStatus() || getIMUInitStatus() || getBarometerInitStatus() || getAccDataStatus() || getGyroscopeStatus() || getBarometerDataStatus() || getAircraftAttiStatus() || getIMUDataStatus() || getDataLoggerStatus() || getLastIMUAdvanceCaliStatus() || getLastIMUBasicCaliStatus()) {
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

    public boolean getIMUAdvanceCaliStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 0) & 1) == 1;
    }

    public boolean getIMUBasicCaliStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 1) & 1) == 1;
    }

    public boolean getIMUHorizontalCaliStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 2) & 1) == 1;
    }

    public boolean getVersionStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 3) & 1) == 1;
    }

    public boolean getIMUDirectionStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 4) & 1) == 1;
    }

    public boolean getIMUInitStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 5) & 1) == 1;
    }

    public boolean getBarometerInitStatus() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 4) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 6) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean getAccDataStatus() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 4) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 7) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean getGyroscopeStatus() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 4) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 8) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean getBarometerDataStatus() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 4) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 9) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean getAircraftAttiStatus() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 4) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 10) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean getIMUDataStatus() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 4) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 11) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean getDataLoggerStatus() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 4) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 12) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean getLastIMUAdvanceCaliStatus() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 5) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 13) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean getLastIMUBasicCaliStatus() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 5) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 14) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isReadingData() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 16) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 15) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isKernelBoardHighTemperature() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 24) & 1) == 1;
    }

    public boolean isBatteryInstalledError() {
        if (getSenderId() != 2049) {
            return false;
        }
        if (((((Integer) get(0, 4, Integer.class)).intValue() >> 30) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean hasMcDataError() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 5) {
            return false;
        }
        if ((((Integer) get(0, 4, Integer.class)).intValue() & 8180) != 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
