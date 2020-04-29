package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushLog extends DataBase {
    private static DataCameraGetPushLog instance = null;

    public static synchronized DataCameraGetPushLog getInstance() {
        DataCameraGetPushLog dataCameraGetPushLog;
        synchronized (DataCameraGetPushLog.class) {
            if (instance == null) {
                instance = new DataCameraGetPushLog();
            }
            dataCameraGetPushLog = instance;
        }
        return dataCameraGetPushLog;
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
