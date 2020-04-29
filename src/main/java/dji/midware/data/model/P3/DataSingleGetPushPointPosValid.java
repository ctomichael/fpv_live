package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataSingleGetPushPointPosValid extends DataBase {
    private static DataSingleGetPushPointPosValid instance;

    public static synchronized DataSingleGetPushPointPosValid getInstance() {
        DataSingleGetPushPointPosValid dataSingleGetPushPointPosValid;
        synchronized (DataSingleGetPushPointPosValid.class) {
            if (instance == null) {
                instance = new DataSingleGetPushPointPosValid();
            }
            dataSingleGetPushPointPosValid = instance;
        }
        return dataSingleGetPushPointPosValid;
    }

    private DataSingleGetPushPointPosValid() {
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getSessionId() {
        return ((Short) get(0, 2, Short.class)).shortValue();
    }

    public boolean canFly() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 1;
    }

    public short getErrorCode() {
        return ((Short) get(3, 2, Short.class)).shortValue();
    }

    public boolean hasObstacle() {
        return ((getErrorCode() >> 0) & 1) == 1;
    }

    public boolean isFovLimetReached() {
        return ((getErrorCode() >> 1) & 1) == 1;
    }

    public boolean isTooLow() {
        return ((getErrorCode() >> 2) & 1) == 1;
    }

    public boolean isTooHigh() {
        return ((getErrorCode() >> 3) & 1) == 1;
    }

    public boolean isPointedAreaInvalid() {
        return ((getErrorCode() >> 4) & 1) == 1;
    }

    public boolean isNotFlying() {
        return ((getErrorCode() >> 5) & 1) == 1;
    }

    public boolean isInRestrictedArea() {
        return ((getErrorCode() >> 6) & 1) == 1;
    }

    public boolean isRouteCollectionLoadFailed() {
        return ((getErrorCode() >> 7) & 1) == 1;
    }
}
