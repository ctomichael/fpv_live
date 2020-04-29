package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushSDRHdConfig extends DataBase {
    private static DataOsdGetPushSDRHdConfig instance = null;

    public static synchronized DataOsdGetPushSDRHdConfig getInstance() {
        DataOsdGetPushSDRHdConfig dataOsdGetPushSDRHdConfig;
        synchronized (DataOsdGetPushSDRHdConfig.class) {
            if (instance == null) {
                instance = new DataOsdGetPushSDRHdConfig();
            }
            dataOsdGetPushSDRHdConfig = instance;
        }
        return dataOsdGetPushSDRHdConfig;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
