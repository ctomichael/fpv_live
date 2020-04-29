package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushFunctionList extends DataBase {
    private static DataEyeGetPushFunctionList instance = null;

    public static synchronized DataEyeGetPushFunctionList getInstance() {
        DataEyeGetPushFunctionList dataEyeGetPushFunctionList;
        synchronized (DataEyeGetPushFunctionList.class) {
            if (instance == null) {
                instance = new DataEyeGetPushFunctionList();
            }
            dataEyeGetPushFunctionList = instance;
        }
        return dataEyeGetPushFunctionList;
    }

    public int getTinkCount() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public boolean supportSelfCal() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 1) != 0;
    }

    public boolean sensorStatusSource() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isFrontDisable() {
        return (((Integer) get(5, 1, Integer.class)).intValue() & 251) != 0;
    }

    public boolean isFrontDisableWhenAutoLanding() {
        return (((Integer) get(5, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isFrontDisableByTripod() {
        return (((Integer) get(5, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isFrontDisableBySwitchSensor() {
        return (((Integer) get(5, 1, Integer.class)).intValue() & 4) != 0;
    }

    public boolean isFrontAttiTooLarge() {
        return (((Integer) get(5, 1, Integer.class)).intValue() & 8) != 0;
    }

    public boolean isBackDisable() {
        return (((Integer) get(6, 1, Integer.class)).intValue() & 251) != 0;
    }

    public boolean isBackDisableWhenAutoLanding() {
        return (((Integer) get(6, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isBackDisableByTripod() {
        return (((Integer) get(6, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isBackDisableBySwitchSensor() {
        return (((Integer) get(6, 1, Integer.class)).intValue() & 4) != 0;
    }

    public boolean isBackAttiTooLarge() {
        return (((Integer) get(6, 1, Integer.class)).intValue() & 8) != 0;
    }

    public boolean isRightDisable() {
        return ((Integer) get(7, 1, Integer.class)).intValue() != 0;
    }

    public boolean isRightDisableWhenAutoLanding() {
        return (((Integer) get(7, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isRightDisableByTripod() {
        return (((Integer) get(7, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isRightAttiTooLarge() {
        return (((Integer) get(7, 1, Integer.class)).intValue() & 8) != 0;
    }

    public boolean isLeftDisable() {
        return ((Integer) get(8, 1, Integer.class)).intValue() != 0;
    }

    public boolean isLeftDisableWhenAutoLanding() {
        return (((Integer) get(8, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isLeftDisableByTripod() {
        return (((Integer) get(8, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isLeftAttiTooLarge() {
        return (((Integer) get(8, 1, Integer.class)).intValue() & 8) != 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
