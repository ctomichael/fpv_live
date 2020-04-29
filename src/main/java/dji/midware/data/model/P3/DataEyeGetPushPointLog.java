package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataEyeGetPushPointLog extends DataBase {
    private static DataEyeGetPushPointLog instance = null;

    public static synchronized DataEyeGetPushPointLog getInstance() {
        DataEyeGetPushPointLog dataEyeGetPushPointLog;
        synchronized (DataEyeGetPushPointLog.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPointLog();
            }
            dataEyeGetPushPointLog = instance;
        }
        return dataEyeGetPushPointLog;
    }

    public String getLog() {
        return BytesUtil.getStringUTF8(this._recData);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
