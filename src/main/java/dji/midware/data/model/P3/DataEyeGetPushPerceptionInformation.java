package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICommonDataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushPerceptionInformation extends DJICommonDataBase {
    private static DataEyeGetPushPerceptionInformation instance = null;

    public static synchronized DataEyeGetPushPerceptionInformation getInstance() {
        DataEyeGetPushPerceptionInformation dataEyeGetPushPerceptionInformation;
        synchronized (DataEyeGetPushPerceptionInformation.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPerceptionInformation();
            }
            dataEyeGetPushPerceptionInformation = instance;
        }
        return dataEyeGetPushPerceptionInformation;
    }

    public int getPerceptionGestureEnableValue() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public boolean isPerceptionGestureEnabled() {
        return ((Integer) get(1, 1, Integer.class)).intValue() == 1;
    }

    public int getDirectionIndex() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getGestureStatus() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }
}
