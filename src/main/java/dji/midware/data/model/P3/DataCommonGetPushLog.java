package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataCommonGetPushLog extends DataBase {
    private static DataCommonGetPushLog instance;

    private static synchronized DataCommonGetPushLog getInstance() {
        DataCommonGetPushLog dataCommonGetPushLog;
        synchronized (DataCommonGetPushLog.class) {
            if (instance == null) {
                instance = new DataCommonGetPushLog();
            }
            dataCommonGetPushLog = instance;
        }
        return dataCommonGetPushLog;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
