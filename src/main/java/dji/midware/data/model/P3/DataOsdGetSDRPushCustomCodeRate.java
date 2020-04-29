package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetSDRPushCustomCodeRate extends DataBase {
    private static DataOsdGetSDRPushCustomCodeRate instance = null;

    public static synchronized DataOsdGetSDRPushCustomCodeRate getInstance() {
        DataOsdGetSDRPushCustomCodeRate dataOsdGetSDRPushCustomCodeRate;
        synchronized (DataOsdGetSDRPushCustomCodeRate.class) {
            if (instance == null) {
                instance = new DataOsdGetSDRPushCustomCodeRate();
            }
            dataOsdGetSDRPushCustomCodeRate = instance;
        }
        return dataOsdGetSDRPushCustomCodeRate;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public float getCodeRate() {
        return ((Float) get(0, 4, Float.class)).floatValue();
    }
}
