package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataEyeGetPushTrackLog extends DataBase {
    private static DataEyeGetPushTrackLog instance = null;

    public static synchronized DataEyeGetPushTrackLog getInstance() {
        DataEyeGetPushTrackLog dataEyeGetPushTrackLog;
        synchronized (DataEyeGetPushTrackLog.class) {
            if (instance == null) {
                instance = new DataEyeGetPushTrackLog();
            }
            dataEyeGetPushTrackLog = instance;
        }
        return dataEyeGetPushTrackLog;
    }

    public String getLog() {
        return BytesUtil.getStringUTF8(this._recData);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
