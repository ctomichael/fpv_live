package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataDoubleGetPushCheckStatus extends DataBase {
    private static DataDoubleGetPushCheckStatus instance = null;

    public static synchronized DataDoubleGetPushCheckStatus getInstance() {
        DataDoubleGetPushCheckStatus dataDoubleGetPushCheckStatus;
        synchronized (DataDoubleGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataDoubleGetPushCheckStatus();
                instance.isNeedPushLosed = true;
                instance.isRemoteModel = true;
            }
            dataDoubleGetPushCheckStatus = instance;
        }
        return dataDoubleGetPushCheckStatus;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
