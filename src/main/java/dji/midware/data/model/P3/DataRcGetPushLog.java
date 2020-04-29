package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataRcGetPushLog extends DataBase {
    private static DataRcGetPushLog instance = null;

    public static synchronized DataRcGetPushLog getInstance() {
        DataRcGetPushLog dataRcGetPushLog;
        synchronized (DataRcGetPushLog.class) {
            if (instance == null) {
                instance = new DataRcGetPushLog();
            }
            dataRcGetPushLog = instance;
        }
        return dataRcGetPushLog;
    }

    public int getType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public String getLog() {
        if (this._recData != null) {
            return get(1, this._recData.length - 1);
        }
        return "";
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
