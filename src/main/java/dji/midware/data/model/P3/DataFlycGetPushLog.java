package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushLog extends DataBase {
    private static DataFlycGetPushLog instance = null;

    public static synchronized DataFlycGetPushLog getInstance() {
        DataFlycGetPushLog dataFlycGetPushLog;
        synchronized (DataFlycGetPushLog.class) {
            if (instance == null) {
                instance = new DataFlycGetPushLog();
            }
            dataFlycGetPushLog = instance;
        }
        return dataFlycGetPushLog;
    }

    public int getType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public String getLog() {
        return get(1, this._recData.length - 1);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
