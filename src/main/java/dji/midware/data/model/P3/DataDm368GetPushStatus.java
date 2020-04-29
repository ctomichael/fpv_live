package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataDm368GetPushStatus extends DataBase {
    private static DataDm368GetPushStatus instance = null;

    public static synchronized DataDm368GetPushStatus getInstance() {
        DataDm368GetPushStatus dataDm368GetPushStatus;
        synchronized (DataDm368GetPushStatus.class) {
            if (instance == null) {
                instance = new DataDm368GetPushStatus();
            }
            dataDm368GetPushStatus = instance;
        }
        return dataDm368GetPushStatus;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public boolean isDisableLiveview() {
        return ((Integer) get(3, 1, Integer.class)).intValue() == 1;
    }

    public int getEncodeMode() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public int getDualEncodeModePercentage() {
        return ((Integer) get(5, 1, Integer.class)).intValue();
    }

    public boolean isDualEncodeModeSupported() {
        if (getRecDataLen() < 6) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
