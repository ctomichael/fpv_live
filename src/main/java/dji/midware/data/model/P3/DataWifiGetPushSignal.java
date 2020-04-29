package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataWifiGetPushSignal extends DataBase {
    private static DataWifiGetPushSignal mInstance = null;

    public static synchronized DataWifiGetPushSignal getInstance() {
        DataWifiGetPushSignal dataWifiGetPushSignal;
        synchronized (DataWifiGetPushSignal.class) {
            if (mInstance == null) {
                mInstance = new DataWifiGetPushSignal();
            }
            dataWifiGetPushSignal = mInstance;
        }
        return dataWifiGetPushSignal;
    }

    public int getSignal() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
