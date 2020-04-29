package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;

@Keep
public class DataEyeGetPushPOITargetInformation extends DataBase {
    private static DataEyeGetPushPOITargetInformation instance = null;

    public static synchronized DataEyeGetPushPOITargetInformation getInstance() {
        DataEyeGetPushPOITargetInformation dataEyeGetPushPOITargetInformation;
        synchronized (DataEyeGetPushPOITargetInformation.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPOITargetInformation();
            }
            dataEyeGetPushPOITargetInformation = instance;
        }
        return dataEyeGetPushPOITargetInformation;
    }

    public int getStatus() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public float getCenterX() {
        return ((Float) get(1, 4, Float.class)).floatValue();
    }

    public float getCenterY() {
        return ((Float) get(5, 4, Float.class)).floatValue();
    }

    public float getWidth() {
        return ((Float) get(9, 4, Float.class)).floatValue();
    }

    public float getHeight() {
        return ((Float) get(13, 4, Float.class)).floatValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
