package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushSdrConfigInfo extends DataBase {
    private static DataOsdGetPushSdrConfigInfo instance = null;

    public static synchronized DataOsdGetPushSdrConfigInfo getInstance() {
        DataOsdGetPushSdrConfigInfo dataOsdGetPushSdrConfigInfo;
        synchronized (DataOsdGetPushSdrConfigInfo.class) {
            if (instance == null) {
                instance = new DataOsdGetPushSdrConfigInfo();
            }
            dataOsdGetPushSdrConfigInfo = instance;
        }
        return dataOsdGetPushSdrConfigInfo;
    }

    public int getNF() {
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }

    public int getBand() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
