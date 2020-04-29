package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataGimbalGetPushHandheldStickState extends DataBase {
    private static DataGimbalGetPushHandheldStickState instance = null;

    public static synchronized DataGimbalGetPushHandheldStickState getInstance() {
        DataGimbalGetPushHandheldStickState dataGimbalGetPushHandheldStickState;
        synchronized (DataGimbalGetPushHandheldStickState.class) {
            if (instance == null) {
                instance = new DataGimbalGetPushHandheldStickState();
            }
            dataGimbalGetPushHandheldStickState = instance;
        }
        return dataGimbalGetPushHandheldStickState;
    }

    public int getStickX() {
        return ((Short) get(0, 2, Short.class)).shortValue();
    }

    public int getStickY() {
        return ((Short) get(2, 2, Short.class)).shortValue();
    }

    public boolean isTriggerPressed() {
        return (((Short) get(4, 2, Short.class)).shortValue() & 1) == 1;
    }

    public boolean isStickGimbalControlEnabled() {
        return (((Short) get(4, 2, Short.class)).shortValue() & 2) == 2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
