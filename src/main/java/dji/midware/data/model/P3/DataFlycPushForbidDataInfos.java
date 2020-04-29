package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycPushForbidDataInfos extends DataBase {
    private static DataFlycPushForbidDataInfos instance = null;

    public static synchronized DataFlycPushForbidDataInfos getInstance() {
        DataFlycPushForbidDataInfos dataFlycPushForbidDataInfos;
        synchronized (DataFlycPushForbidDataInfos.class) {
            if (instance == null) {
                instance = new DataFlycPushForbidDataInfos();
            }
            dataFlycPushForbidDataInfos = instance;
        }
        return dataFlycPushForbidDataInfos;
    }

    private DataFlycPushForbidDataInfos() {
        this.isNeedPushLosed = true;
    }

    /* access modifiers changed from: protected */
    public int getPushLoseDelayTime() {
        return 10000;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public int getTimeStamp() {
        if (this._recData == null) {
            return -1;
        }
        return ((Integer) get(4, 4, Integer.class)).intValue();
    }

    public String getVersion() {
        if (this._recData == null) {
            return "00.00.00.00";
        }
        return addZero(this._recData[3] & 255) + "." + addZero(this._recData[2] & 255) + "." + addZero(this._recData[1] & 255) + "." + addZero(this._recData[0] & 255);
    }

    private String addZero(int val) {
        String res = "" + val;
        if (val < 10) {
            return "0" + res;
        }
        return res;
    }

    public boolean isSupportDynamicUpdate() {
        return (((Integer) get(8, 1, Integer.class)).intValue() & 1) == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
