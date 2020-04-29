package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataDoubleGetPushLog extends DataBase {
    private static DataDoubleGetPushLog instance = null;

    public static synchronized DataDoubleGetPushLog getInstance() {
        DataDoubleGetPushLog dataDoubleGetPushLog;
        synchronized (DataDoubleGetPushLog.class) {
            if (instance == null) {
                instance = new DataDoubleGetPushLog();
            }
            dataDoubleGetPushLog = instance;
        }
        return dataDoubleGetPushLog;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
