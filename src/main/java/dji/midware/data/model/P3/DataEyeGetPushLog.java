package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushLog extends DataBase {
    private static DataEyeGetPushLog instance = null;

    public static synchronized DataEyeGetPushLog getInstance() {
        DataEyeGetPushLog dataEyeGetPushLog;
        synchronized (DataEyeGetPushLog.class) {
            if (instance == null) {
                instance = new DataEyeGetPushLog();
            }
            dataEyeGetPushLog = instance;
        }
        return dataEyeGetPushLog;
    }

    public String getLog() {
        return get(0, this._recData.length);
    }

    public byte[] getRecvData() {
        return this._recData;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
