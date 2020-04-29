package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushPalmControlState extends DataBase {
    private static DataEyeGetPushPalmControlState instance;

    public static synchronized DataEyeGetPushPalmControlState getInstance() {
        DataEyeGetPushPalmControlState dataEyeGetPushPalmControlState;
        synchronized (DataEyeGetPushPalmControlState.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPalmControlState();
            }
            dataEyeGetPushPalmControlState = instance;
        }
        return dataEyeGetPushPalmControlState;
    }

    public int getPalmDetectionState() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getPalmControllingState() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getPalmControlDetectionResult() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
