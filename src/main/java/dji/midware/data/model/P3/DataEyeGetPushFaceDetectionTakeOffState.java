package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushFaceDetectionTakeOffState extends DataBase {
    public static DataEyeGetPushFaceDetectionTakeOffState instance;

    public static synchronized DataEyeGetPushFaceDetectionTakeOffState getInstance() {
        DataEyeGetPushFaceDetectionTakeOffState dataEyeGetPushFaceDetectionTakeOffState;
        synchronized (DataEyeGetPushFaceDetectionTakeOffState.class) {
            if (instance == null) {
                instance = new DataEyeGetPushFaceDetectionTakeOffState();
            }
            dataEyeGetPushFaceDetectionTakeOffState = instance;
        }
        return dataEyeGetPushFaceDetectionTakeOffState;
    }

    public int getDetectionTakeOffState() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
