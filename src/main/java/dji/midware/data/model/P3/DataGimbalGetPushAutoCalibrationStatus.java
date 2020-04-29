package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataGimbalGetPushAutoCalibrationStatus extends DataBase {
    private static DataGimbalGetPushAutoCalibrationStatus instance = null;

    public static synchronized DataGimbalGetPushAutoCalibrationStatus getInstance() {
        DataGimbalGetPushAutoCalibrationStatus dataGimbalGetPushAutoCalibrationStatus;
        synchronized (DataGimbalGetPushAutoCalibrationStatus.class) {
            if (instance == null) {
                instance = new DataGimbalGetPushAutoCalibrationStatus();
            }
            dataGimbalGetPushAutoCalibrationStatus = instance;
        }
        return dataGimbalGetPushAutoCalibrationStatus;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getProgress() {
        return getProgress(-1);
    }

    public int getProgress(int index) {
        return ((Integer) get(0, 1, Integer.class, index)).intValue();
    }

    public int getStatus() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }
}
