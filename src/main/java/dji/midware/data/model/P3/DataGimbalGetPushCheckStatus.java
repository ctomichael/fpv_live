package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataGimbalGetPushType;
import dji.midware.data.packages.P3.Pack;

@Keep
@EXClassNullAway
public class DataGimbalGetPushCheckStatus extends DataBase {
    private static DataGimbalGetPushCheckStatus instance = null;

    public static synchronized DataGimbalGetPushCheckStatus getInstance() {
        DataGimbalGetPushCheckStatus dataGimbalGetPushCheckStatus;
        synchronized (DataGimbalGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataGimbalGetPushCheckStatus();
            }
            dataGimbalGetPushCheckStatus = instance;
        }
        return dataGimbalGetPushCheckStatus;
    }

    public boolean hasException(boolean isFactory) {
        boolean res;
        if (getGyroscopeStatus() || getPitchStatus() || getRollStatus() || getYawStatus() || getDataReceiveStatus() || getGimbalWholeCalibrateIsError() || getGimbalAngleSensorInitError() || (isFactory && getGimbalCalibrateForFactoryIsError())) {
            res = true;
        } else {
            res = false;
        }
        DataGimbalGetPushType gimbalType = DataGimbalGetPushType.getInstance();
        if (!gimbalType.isGetted() || gimbalType.getType() != DataGimbalGetPushType.DJIGimbalType.WM220) {
            return res;
        }
        if (res || getLimitStatus() != 0 || getVibrateStatus()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (pack == null || pack.data == null || pack.data.length != 21) {
            super.setPushRecPack(pack);
        }
    }

    public boolean getGyroscopeStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 0) & 1) == 1;
    }

    public boolean getPitchStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 1) & 1) == 1;
    }

    public boolean getRollStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 2) & 1) == 1;
    }

    public boolean getYawStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 3) & 1) == 1;
    }

    public boolean getDataReceiveStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 4) & 1) == 1;
    }

    public boolean getIMUCalibrateMatchStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 7) & 1) == 1;
    }

    public boolean getGimbalCalibrateForFactoryIsError() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 15) & 1) == 1;
    }

    public boolean getGimbalAngleSensorInitError() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 16) & 1) == 1;
    }

    public boolean getGimbalWholeCalibrateIsError() {
        boolean z = true;
        if (DJIProductManager.getInstance().getType() != ProductType.WM160) {
            if (((((Integer) get(0, 4, Integer.class)).intValue() >> 12) & 1) != 1) {
                z = false;
            }
            return z;
        } else if (((((Integer) get(0, 4, Integer.class)).intValue() >> 24) & 1) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int getLimitStatus() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 8) & 3;
    }

    public boolean getVibrateStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 10) & 1) == 1;
    }

    public boolean getGimbalBrokenState() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 8) & 1) == 1;
    }

    public boolean getGimbalMotorProtectionEnabled() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 9) & 1) == 1;
    }

    public boolean getGimbalRotationError() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 11) & 1) == 1;
    }

    public boolean getGimbalReachedRollMechanicalLimit() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 13) & 1) == 1;
    }

    public boolean getGimbalReachedPitchMechanicalLimit() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 14) & 1) == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
