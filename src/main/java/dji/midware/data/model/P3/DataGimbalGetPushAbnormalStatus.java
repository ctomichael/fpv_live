package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataGimbalGetPushAbnormalStatus extends DataBase {
    private static DataGimbalGetPushAbnormalStatus instance = null;

    public static synchronized DataGimbalGetPushAbnormalStatus getInstance() {
        DataGimbalGetPushAbnormalStatus dataGimbalGetPushAbnormalStatus;
        synchronized (DataGimbalGetPushAbnormalStatus.class) {
            if (instance == null) {
                instance = new DataGimbalGetPushAbnormalStatus();
            }
            dataGimbalGetPushAbnormalStatus = instance;
        }
        return dataGimbalGetPushAbnormalStatus;
    }

    public boolean isRollLocked() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean isPitchLocked() {
        return ((((Integer) get(0, 1, Integer.class)).intValue() >> 1) & 1) == 1;
    }

    public boolean isYawLocked() {
        return ((((Integer) get(0, 1, Integer.class)).intValue() >> 2) & 1) == 1;
    }

    public boolean isGimbalLocked() {
        return isRollLocked() || isPitchLocked();
    }

    public boolean isJointLockAfterStartup() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean isJointLockWhenStartup() {
        return ((((Integer) get(1, 1, Integer.class)).intValue() >> 1) & 1) == 1;
    }

    public boolean isMotorProtected() {
        return ((((Integer) get(1, 1, Integer.class)).intValue() >> 2) & 1) == 1;
    }

    public boolean isErrorRecentWhenStartUp() {
        return ((((Integer) get(1, 1, Integer.class)).intValue() >> 3) & 1) == 1;
    }

    public boolean isUpgrading() {
        return ((((Integer) get(1, 1, Integer.class)).intValue() >> 4) & 1) == 1;
    }

    public boolean isYawLimit() {
        return ((((Integer) get(1, 1, Integer.class)).intValue() >> 5) & 1) == 1;
    }

    public boolean isErrorRecentOrSelfie() {
        return ((((Integer) get(1, 1, Integer.class)).intValue() >> 6) & 1) == 1;
    }

    public boolean isPanoReady() {
        return ((((Integer) get(1, 1, Integer.class)).intValue() >> 7) & 1) == 1;
    }

    public int getFanDirection() {
        return (((Integer) get(2, 1, Integer.class)).intValue() >> 1) & 1;
    }

    public boolean isPhoneOutGimbal() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getGimbalGravity() {
        return (((Integer) get(3, 1, Integer.class)).intValue() >> 1) & 3;
    }

    public int getVerticalDirection() {
        return (((Integer) get(2, 1, Integer.class)).intValue() >> 2) & 1;
    }

    public boolean isInFlashlight() {
        return ((((Integer) get(2, 1, Integer.class)).intValue() >> 3) & 1) == 1;
    }

    public boolean isPortrait() {
        return ((((Integer) get(2, 1, Integer.class)).intValue() >> 4) & 1) == 1;
    }

    public int getGimbalDirectionWhenVertical() {
        return (((Integer) get(2, 1, Integer.class)).intValue() >> 5) & 1;
    }

    public boolean isYawLimitedInTracking() {
        return ((((Integer) get(3, 1, Integer.class)).intValue() >> 5) & 1) == 1;
    }

    public boolean isPitchLimitedInTracking() {
        return ((((Integer) get(3, 1, Integer.class)).intValue() >> 6) & 1) == 1;
    }

    public int getSleepMode() {
        return ((Integer) get(4, 1, Integer.class)).intValue() & 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
