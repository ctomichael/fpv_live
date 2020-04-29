package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICameraDataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushPrepareOpenFan extends DJICameraDataBase {
    private static DataCameraGetPushPrepareOpenFan instance = null;

    public static synchronized DataCameraGetPushPrepareOpenFan getInstance() {
        DataCameraGetPushPrepareOpenFan dataCameraGetPushPrepareOpenFan;
        synchronized (DataCameraGetPushPrepareOpenFan.class) {
            if (instance == null) {
                instance = new DataCameraGetPushPrepareOpenFan();
            }
            dataCameraGetPushPrepareOpenFan = instance;
        }
        return dataCameraGetPushPrepareOpenFan;
    }

    public int getLeftSeconds() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
