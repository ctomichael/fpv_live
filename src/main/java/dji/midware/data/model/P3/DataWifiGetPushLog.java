package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataWifiGetPushLog extends DataBase {
    private static DataWifiGetPushLog mInstance = null;

    public static synchronized DataWifiGetPushLog getInstance() {
        DataWifiGetPushLog dataWifiGetPushLog;
        synchronized (DataWifiGetPushLog.class) {
            if (mInstance == null) {
                mInstance = new DataWifiGetPushLog();
            }
            dataWifiGetPushLog = mInstance;
        }
        return dataWifiGetPushLog;
    }

    public String getPushLogStrs() {
        return BytesUtil.getStringUTF8(this._recData);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
