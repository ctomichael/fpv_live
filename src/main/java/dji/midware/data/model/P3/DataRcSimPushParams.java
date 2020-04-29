package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataRcSimPushParams extends DataBase {
    public static final int MAX_VALUE = 1684;
    public static final int MID_VALUE = 1024;
    public static final int MIN_VALUE = 364;
    private static DataRcSimPushParams instance = null;

    public static synchronized DataRcSimPushParams getInstance() {
        DataRcSimPushParams dataRcSimPushParams;
        synchronized (DataRcSimPushParams.class) {
            if (instance == null) {
                instance = new DataRcSimPushParams();
            }
            dataRcSimPushParams = instance;
        }
        return dataRcSimPushParams;
    }

    public int getAileron() {
        return getData(0, 2);
    }

    public int getElevator() {
        return getData(2, 2);
    }

    public int getThrottle() {
        return getData(4, 2);
    }

    public int getRudder() {
        return getData(6, 2);
    }

    private int getData(int from, int length) {
        if (this._recData == null || this._recData.length < from + length) {
            return 1024;
        }
        int data = ((Integer) get(from, length, Integer.class)).intValue();
        if (data < 364) {
            return 364;
        }
        if (data > 1684) {
            return 1684;
        }
        return data;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
