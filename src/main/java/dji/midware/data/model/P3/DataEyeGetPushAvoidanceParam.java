package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushAvoidanceParam extends DataBase {
    private static DataEyeGetPushAvoidanceParam instance = null;

    public static synchronized DataEyeGetPushAvoidanceParam getInstance() {
        DataEyeGetPushAvoidanceParam dataEyeGetPushAvoidanceParam;
        synchronized (DataEyeGetPushAvoidanceParam.class) {
            if (instance == null) {
                instance = new DataEyeGetPushAvoidanceParam();
            }
            dataEyeGetPushAvoidanceParam = instance;
        }
        return dataEyeGetPushAvoidanceParam;
    }

    public boolean isBraking() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isVisualSensorWorking() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isAvoidOpen() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 4) != 0;
    }

    public boolean beShuttleMode() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 8) != 0;
    }

    public boolean isAvoidFrontWork() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 16) != 0;
    }

    public boolean isAvoidRightWork() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 32) != 0;
    }

    public boolean isAvoidBehindWork() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 64) != 0;
    }

    public boolean isAvoidLeftWork() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 128) != 0;
    }

    public int getAvoidFrontDistanceLevel() {
        return ((Integer) get(1, 1, Integer.class)).intValue() & 15;
    }

    public int getAvoidFrontAlertLevel() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 240) >>> 4;
    }

    public int getAvoidRightDistanceLevel() {
        return ((Integer) get(2, 1, Integer.class)).intValue() & 15;
    }

    public int getAvoidRightAlertLevel() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 240) >>> 4;
    }

    public int getAvoidBehindDistanceLevel() {
        return ((Integer) get(3, 1, Integer.class)).intValue() & 15;
    }

    public int getAvoidBehindAlertLevel() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 240) >>> 4;
    }

    public int getAvoidLeftDistanceLevel() {
        return ((Integer) get(4, 1, Integer.class)).intValue() & 15;
    }

    public int getAvoidLeftAlertLevel() {
        return (((Integer) get(4, 1, Integer.class)).intValue() & 240) >>> 4;
    }

    public boolean allowFront() {
        DataEyeGetPushFunctionList func = DataEyeGetPushFunctionList.getInstance();
        if (!func.isGetted() || !func.sensorStatusSource()) {
            return true;
        }
        return (((Integer) get(5, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean allowRight() {
        DataEyeGetPushFunctionList func = DataEyeGetPushFunctionList.getInstance();
        if (!func.isGetted() || !func.sensorStatusSource()) {
            return true;
        }
        return (((Integer) get(5, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean allowBack() {
        DataEyeGetPushFunctionList func = DataEyeGetPushFunctionList.getInstance();
        if (!func.isGetted() || !func.sensorStatusSource()) {
            return true;
        }
        return (((Integer) get(5, 1, Integer.class)).intValue() & 4) != 0;
    }

    public boolean allowLeft() {
        DataEyeGetPushFunctionList func = DataEyeGetPushFunctionList.getInstance();
        if (!func.isGetted() || !func.sensorStatusSource()) {
            return true;
        }
        return (((Integer) get(5, 1, Integer.class)).intValue() & 8) != 0;
    }

    public boolean isOpenFrontRadar() {
        DataEyeGetPushFunctionList func = DataEyeGetPushFunctionList.getInstance();
        if (!func.isGetted() || !func.sensorStatusSource()) {
            return true;
        }
        return (((Integer) get(5, 1, Integer.class)).intValue() & 16) != 0;
    }

    public boolean isOpenRightRadar() {
        DataEyeGetPushFunctionList func = DataEyeGetPushFunctionList.getInstance();
        if (!func.isGetted() || !func.sensorStatusSource()) {
            return true;
        }
        return (((Integer) get(5, 1, Integer.class)).intValue() & 32) != 0;
    }

    public boolean isOpenBackRadar() {
        DataEyeGetPushFunctionList func = DataEyeGetPushFunctionList.getInstance();
        if (!func.isGetted() || !func.sensorStatusSource()) {
            return true;
        }
        return (((Integer) get(5, 1, Integer.class)).intValue() & 64) != 0;
    }

    public boolean isOpenLeftRadar() {
        DataEyeGetPushFunctionList func = DataEyeGetPushFunctionList.getInstance();
        if (!func.isGetted() || !func.sensorStatusSource()) {
            return true;
        }
        return (((Integer) get(5, 1, Integer.class)).intValue() & 128) != 0;
    }

    public int getIndex() {
        return ((Integer) get(6, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        setRecData(data);
        if (isWantPush()) {
            post();
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
