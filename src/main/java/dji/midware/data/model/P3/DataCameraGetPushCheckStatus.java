package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushCheckStatus extends DataBase {
    private static DataCameraGetPushCheckStatus instance = null;

    public static synchronized DataCameraGetPushCheckStatus getInstance() {
        DataCameraGetPushCheckStatus dataCameraGetPushCheckStatus;
        synchronized (DataCameraGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataCameraGetPushCheckStatus();
            }
            dataCameraGetPushCheckStatus = instance;
        }
        return dataCameraGetPushCheckStatus;
    }

    public boolean encryptionStatus() {
        return ((Integer) get(21, 2, Integer.class)).intValue() == 2;
    }

    public boolean hdmiStatus() {
        return ((Integer) get(20, 1, Integer.class)).intValue() == 0;
    }

    public boolean upgradeStatus() {
        return ((Integer) get(14, 2, Integer.class)).intValue() == 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
