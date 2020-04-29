package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushActiveRequest extends DataBase {
    private static DataFlycGetPushActiveRequest instance = null;

    public static synchronized DataFlycGetPushActiveRequest getInstance() {
        DataFlycGetPushActiveRequest dataFlycGetPushActiveRequest;
        synchronized (DataFlycGetPushActiveRequest.class) {
            if (instance == null) {
                instance = new DataFlycGetPushActiveRequest();
            }
            dataFlycGetPushActiveRequest = instance;
        }
        return dataFlycGetPushActiveRequest;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public int getAppId() {
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public int getAppLevel() {
        return ((Integer) get(4, 4, Integer.class)).intValue();
    }

    public int getAppVersion() {
        return ((Integer) get(8, 4, Integer.class)).intValue();
    }

    public String getAppBundleId() {
        return getUTF8(12, 32);
    }

    public String getDevSn() {
        return getUTF8(44, 32);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
