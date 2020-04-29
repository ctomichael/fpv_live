package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushPalmControlNotification extends DataBase {
    private static DataEyeGetPushPalmControlNotification instance;

    public static synchronized DataEyeGetPushPalmControlNotification getInstance() {
        DataEyeGetPushPalmControlNotification dataEyeGetPushPalmControlNotification;
        synchronized (DataEyeGetPushPalmControlNotification.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPalmControlNotification();
            }
            dataEyeGetPushPalmControlNotification = instance;
        }
        return dataEyeGetPushPalmControlNotification;
    }

    public int getPalmControlActionState() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getPalmControlControlMode() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getDetectionLogic() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getCameraAction() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getLandingCounting() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public int getShootPhotoCounting() {
        return ((Integer) get(5, 1, Integer.class)).intValue();
    }

    public int getNumberOfPeopleInPic() {
        return ((Integer) get(6, 1, Integer.class)).intValue();
    }

    public int getDetectingStateOfFanProtection() {
        return ((Integer) get(7, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
