package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOfdmGetPushCheckStatus extends DataBase {
    private static DataOfdmGetPushCheckStatus instance = null;

    public static synchronized DataOfdmGetPushCheckStatus getInstance() {
        DataOfdmGetPushCheckStatus dataOfdmGetPushCheckStatus;
        synchronized (DataOfdmGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataOfdmGetPushCheckStatus();
            }
            dataOfdmGetPushCheckStatus = instance;
        }
        return dataOfdmGetPushCheckStatus;
    }

    public boolean isOK() {
        return false;
    }

    public boolean isFirmwareNotMatch() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 32) != 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
