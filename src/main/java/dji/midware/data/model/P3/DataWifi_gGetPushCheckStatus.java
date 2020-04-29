package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataWifi_gGetPushCheckStatus extends DataBase {
    private static DataWifi_gGetPushCheckStatus instance = null;

    public static synchronized DataWifi_gGetPushCheckStatus getInstance() {
        DataWifi_gGetPushCheckStatus dataWifi_gGetPushCheckStatus;
        synchronized (DataWifi_gGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataWifi_gGetPushCheckStatus();
            }
            dataWifi_gGetPushCheckStatus = instance;
        }
        return dataWifi_gGetPushCheckStatus;
    }

    public boolean wasApsrvDown() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean was9342Down() {
        return ((((Integer) get(0, 1, Integer.class)).intValue() >> 1) & 1) == 1;
    }

    public boolean isGoHomeFail() {
        return ((((Integer) get(0, 1, Integer.class)).intValue() >> 4) & 1) == 1;
    }

    public boolean isLinked() {
        return ((((Integer) get(1, 1, Integer.class)).intValue() >> 2) & 1) == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
