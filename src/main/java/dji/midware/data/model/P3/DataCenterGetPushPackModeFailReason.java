package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataCenterGetPushPackModeFailReason extends DataBase {
    private static DataCenterGetPushPackModeFailReason instance = null;

    public static synchronized DataCenterGetPushPackModeFailReason getInstance() {
        DataCenterGetPushPackModeFailReason dataCenterGetPushPackModeFailReason;
        synchronized (DataCenterGetPushPackModeFailReason.class) {
            if (instance == null) {
                instance = new DataCenterGetPushPackModeFailReason();
            }
            dataCenterGetPushPackModeFailReason = instance;
        }
        return dataCenterGetPushPackModeFailReason;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        setRecData(data);
        if (isWantPush() && this.isRegist) {
            post();
        }
    }

    public int getPackType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public boolean isGimbalPositonError() {
        return ((Integer) get(1, 1, Integer.class)).intValue() == 1;
    }

    public boolean isDronePlaceError() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
