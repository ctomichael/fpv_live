package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushPreciseLandingEnergy extends DataBase {
    private static DataEyeGetPushPreciseLandingEnergy instance = null;

    public static synchronized DataEyeGetPushPreciseLandingEnergy getInstance() {
        DataEyeGetPushPreciseLandingEnergy dataEyeGetPushPreciseLandingEnergy;
        synchronized (DataEyeGetPushPreciseLandingEnergy.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPreciseLandingEnergy();
            }
            dataEyeGetPushPreciseLandingEnergy = instance;
        }
        return dataEyeGetPushPreciseLandingEnergy;
    }

    public int getEnery() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
