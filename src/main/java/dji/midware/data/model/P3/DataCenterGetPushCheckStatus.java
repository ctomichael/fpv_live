package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.Pack;

@Keep
@EXClassNullAway
public class DataCenterGetPushCheckStatus extends DataBase {
    private static DataCenterGetPushCheckStatus instance = null;

    public static synchronized DataCenterGetPushCheckStatus getInstance() {
        DataCenterGetPushCheckStatus dataCenterGetPushCheckStatus;
        synchronized (DataCenterGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataCenterGetPushCheckStatus();
            }
            dataCenterGetPushCheckStatus = instance;
        }
        return dataCenterGetPushCheckStatus;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (pack == null || pack.data == null || pack.data.length != 21) {
            super.setPushRecPack(pack);
        }
    }

    public boolean isOK() {
        return getBatteryConnectStatus() || getGpsConnectStatus() || getMcConnectStatus();
    }

    public boolean getBatteryConnectStatus() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1) == 1;
    }

    public boolean getGpsConnectStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 1) & 1) == 1;
    }

    public boolean getMcConnectStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 2) & 1) == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
