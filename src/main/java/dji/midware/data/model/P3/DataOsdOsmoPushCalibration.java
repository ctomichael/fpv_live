package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdOsmoPushCalibration extends DataBase {
    private static DataOsdOsmoPushCalibration instance = null;

    public static synchronized DataOsdOsmoPushCalibration getInstance() {
        DataOsdOsmoPushCalibration dataOsdOsmoPushCalibration;
        synchronized (DataOsdOsmoPushCalibration.class) {
            if (instance == null) {
                instance = new DataOsdOsmoPushCalibration();
            }
            dataOsdOsmoPushCalibration = instance;
        }
        return dataOsdOsmoPushCalibration;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getMotorStatus() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getCompassStatus() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }
}
