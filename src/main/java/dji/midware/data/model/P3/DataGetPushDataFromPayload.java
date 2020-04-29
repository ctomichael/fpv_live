package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataGetPushDataFromPayload extends DataBase {
    private static DataGetPushDataFromPayload instance = null;

    public static synchronized DataGetPushDataFromPayload getInstance() {
        DataGetPushDataFromPayload dataGetPushDataFromPayload;
        synchronized (DataGetPushDataFromPayload.class) {
            if (instance == null) {
                instance = new DataGetPushDataFromPayload();
            }
            dataGetPushDataFromPayload = instance;
        }
        return dataGetPushDataFromPayload;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public byte[] getData() {
        return BytesUtil.readBytes(this._recData, 1, ((Integer) get(0, 1, Integer.class)).intValue());
    }

    public int getDataLength() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }
}
