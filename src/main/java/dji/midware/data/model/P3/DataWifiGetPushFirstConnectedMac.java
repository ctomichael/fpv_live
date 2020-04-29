package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataWifiGetPushFirstConnectedMac extends DataBase {
    private static DataWifiGetPushFirstConnectedMac mInstance = null;

    public static synchronized DataWifiGetPushFirstConnectedMac getInstance() {
        DataWifiGetPushFirstConnectedMac dataWifiGetPushFirstConnectedMac;
        synchronized (DataWifiGetPushFirstConnectedMac.class) {
            if (mInstance == null) {
                mInstance = new DataWifiGetPushFirstConnectedMac();
            }
            dataWifiGetPushFirstConnectedMac = mInstance;
        }
        return dataWifiGetPushFirstConnectedMac;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
