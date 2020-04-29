package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJIOsdDataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushPowerStatus extends DJIOsdDataBase {
    private static DataOsdGetPushPowerStatus instance = null;

    public static synchronized DataOsdGetPushPowerStatus getInstance() {
        DataOsdGetPushPowerStatus dataOsdGetPushPowerStatus;
        synchronized (DataOsdGetPushPowerStatus.class) {
            if (instance == null) {
                instance = new DataOsdGetPushPowerStatus();
            }
            dataOsdGetPushPowerStatus = instance;
        }
        return dataOsdGetPushPowerStatus;
    }

    public int getPowerStatus() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public boolean getIsPowerOff() {
        return ((Integer) get(1, 1, Integer.class)).intValue() == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
