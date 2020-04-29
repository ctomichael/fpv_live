package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushRequestLimitUpdate extends DataBase {
    private static DataFlycGetPushRequestLimitUpdate instance = null;

    public static synchronized DataFlycGetPushRequestLimitUpdate getInstance() {
        DataFlycGetPushRequestLimitUpdate dataFlycGetPushRequestLimitUpdate;
        synchronized (DataFlycGetPushRequestLimitUpdate.class) {
            if (instance == null) {
                instance = new DataFlycGetPushRequestLimitUpdate();
            }
            dataFlycGetPushRequestLimitUpdate = instance;
        }
        return dataFlycGetPushRequestLimitUpdate;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
