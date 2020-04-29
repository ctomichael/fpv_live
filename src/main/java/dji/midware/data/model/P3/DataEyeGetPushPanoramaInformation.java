package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushPanoramaInformation extends DataBase {
    private static DataEyeGetPushPanoramaInformation instance = null;

    public static synchronized DataEyeGetPushPanoramaInformation getInstance() {
        DataEyeGetPushPanoramaInformation dataEyeGetPushPanoramaInformation;
        synchronized (DataEyeGetPushPanoramaInformation.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPanoramaInformation();
            }
            dataEyeGetPushPanoramaInformation = instance;
        }
        return dataEyeGetPushPanoramaInformation;
    }

    public int getStatus() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getIndex() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getPhotoNeeded() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getException() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getMode() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
