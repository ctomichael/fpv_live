package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushSDRBarInterference extends DataBase {
    private static DataOsdGetPushSDRBarInterference instance = null;

    public static synchronized DataOsdGetPushSDRBarInterference getInstance() {
        DataOsdGetPushSDRBarInterference dataOsdGetPushSDRBarInterference;
        synchronized (DataOsdGetPushSDRBarInterference.class) {
            if (instance == null) {
                instance = new DataOsdGetPushSDRBarInterference();
            }
            dataOsdGetPushSDRBarInterference = instance;
        }
        return dataOsdGetPushSDRBarInterference;
    }

    public int getBeInterfered() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
