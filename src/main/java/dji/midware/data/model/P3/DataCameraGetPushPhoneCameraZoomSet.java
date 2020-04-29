package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushPhoneCameraZoomSet extends DataBase {
    private static DataCameraGetPushPhoneCameraZoomSet instance = null;
    private final String TAG = DataCameraGetPushPhoneCameraZoomSet.class.getSimpleName();

    public static synchronized DataCameraGetPushPhoneCameraZoomSet getInstance() {
        DataCameraGetPushPhoneCameraZoomSet dataCameraGetPushPhoneCameraZoomSet;
        synchronized (DataCameraGetPushPhoneCameraZoomSet.class) {
            if (instance == null) {
                instance = new DataCameraGetPushPhoneCameraZoomSet();
            }
            dataCameraGetPushPhoneCameraZoomSet = instance;
        }
        return dataCameraGetPushPhoneCameraZoomSet;
    }

    public int getZoomType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getSetZoomSpeed() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getZoomDirection() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getZoomFocusLenthHigh() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
