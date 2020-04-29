package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushCurPanoFileName extends DataBase {
    private static DataCameraGetPushCurPanoFileName instance = null;

    public static synchronized DataCameraGetPushCurPanoFileName getInstance() {
        DataCameraGetPushCurPanoFileName dataCameraGetPushCurPanoFileName;
        synchronized (DataCameraGetPushCurPanoFileName.class) {
            if (instance == null) {
                instance = new DataCameraGetPushCurPanoFileName();
            }
            dataCameraGetPushCurPanoFileName = instance;
        }
        return dataCameraGetPushCurPanoFileName;
    }

    public long getIndex() {
        return ((Long) get(0, 4, Long.class)).longValue();
    }

    public long getPanoCreateTime() {
        return ((Long) get(12, 4, Long.class)).longValue();
    }

    public int getCurSavedNumber() {
        return ((Integer) get(16, 1, Integer.class)).intValue();
    }

    public int getCurTakenNumber() {
        return ((Integer) get(17, 1, Integer.class)).intValue();
    }

    public int getTotalNumber() {
        return ((Integer) get(18, 1, Integer.class)).intValue();
    }

    public long getFileSize() {
        return ((Long) get(20, 8, Long.class)).longValue();
    }

    public long getCreateTime() {
        return ((Long) get(28, 4, Long.class)).longValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
