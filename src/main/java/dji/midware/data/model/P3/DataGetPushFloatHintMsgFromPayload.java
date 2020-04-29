package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.util.BytesUtil;

@Keep
public class DataGetPushFloatHintMsgFromPayload extends DataBase {
    private static DataGetPushFloatHintMsgFromPayload instance = null;

    public static synchronized DataGetPushFloatHintMsgFromPayload getInstance() {
        DataGetPushFloatHintMsgFromPayload dataGetPushFloatHintMsgFromPayload;
        synchronized (DataGetPushFloatHintMsgFromPayload.class) {
            if (instance == null) {
                instance = new DataGetPushFloatHintMsgFromPayload();
            }
            dataGetPushFloatHintMsgFromPayload = instance;
        }
        return dataGetPushFloatHintMsgFromPayload;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public String getData() {
        return BytesUtil.getStringUTF8(BytesUtil.readBytes(this._recData, 1, ((Integer) get(0, 1, Integer.class)).intValue()));
    }

    public int getDataLength() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }
}
