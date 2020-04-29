package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataTransform_gGetPushCheckStatus extends DataBase {
    private static DataTransform_gGetPushCheckStatus instance = null;

    public static synchronized DataTransform_gGetPushCheckStatus getInstance() {
        DataTransform_gGetPushCheckStatus dataTransform_gGetPushCheckStatus;
        synchronized (DataTransform_gGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataTransform_gGetPushCheckStatus();
            }
            dataTransform_gGetPushCheckStatus = instance;
        }
        return dataTransform_gGetPushCheckStatus;
    }

    public boolean isOK() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
