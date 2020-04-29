package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushAgpsStatus extends DataBase {
    private static DataFlycGetPushAgpsStatus instance = null;

    public static synchronized DataFlycGetPushAgpsStatus getInstance() {
        DataFlycGetPushAgpsStatus dataFlycGetPushAgpsStatus;
        synchronized (DataFlycGetPushAgpsStatus.class) {
            if (instance == null) {
                instance = new DataFlycGetPushAgpsStatus();
            }
            dataFlycGetPushAgpsStatus = instance;
        }
        return dataFlycGetPushAgpsStatus;
    }

    public int getTimeStamp() {
        if (this._recData == null) {
            return -1;
        }
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public int getDataLength() {
        return ((Integer) get(4, 4, Integer.class)).intValue();
    }

    public Short getCRC16Hash() {
        return (Short) get(8, 2, Short.class);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
