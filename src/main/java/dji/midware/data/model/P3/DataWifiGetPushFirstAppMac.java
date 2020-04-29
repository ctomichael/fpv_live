package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataWifiGetPushFirstAppMac extends DataBase {
    private static DataWifiGetPushFirstAppMac mInstance = null;

    public static synchronized DataWifiGetPushFirstAppMac getInstance() {
        DataWifiGetPushFirstAppMac dataWifiGetPushFirstAppMac;
        synchronized (DataWifiGetPushFirstAppMac.class) {
            if (mInstance == null) {
                mInstance = new DataWifiGetPushFirstAppMac();
            }
            dataWifiGetPushFirstAppMac = mInstance;
        }
        return dataWifiGetPushFirstAppMac;
    }

    public String getMac() {
        if (this._recData == null || this._recData.length == 0) {
            return null;
        }
        String b1 = BytesUtil.byte2hex(BytesUtil.getByte(((Integer) get(0, 1, Integer.class)).intValue()));
        String b2 = BytesUtil.byte2hex(BytesUtil.getByte(((Integer) get(1, 1, Integer.class)).intValue()));
        String b3 = BytesUtil.byte2hex(BytesUtil.getByte(((Integer) get(2, 1, Integer.class)).intValue()));
        String b4 = BytesUtil.byte2hex(BytesUtil.getByte(((Integer) get(3, 1, Integer.class)).intValue()));
        String b5 = BytesUtil.byte2hex(BytesUtil.getByte(((Integer) get(4, 1, Integer.class)).intValue()));
        return b1 + ":" + b2 + ":" + b3 + ":" + b4 + ":" + b5 + ":" + BytesUtil.byte2hex(BytesUtil.getByte(((Integer) get(5, 1, Integer.class)).intValue()));
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
