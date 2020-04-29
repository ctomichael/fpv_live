package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICommonDataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushPowerParam extends DJICommonDataBase {
    private static DataFlycGetPushPowerParam instance = null;

    public static synchronized DataFlycGetPushPowerParam getInstance() {
        DataFlycGetPushPowerParam dataFlycGetPushPowerParam;
        synchronized (DataFlycGetPushPowerParam.class) {
            if (instance == null) {
                instance = new DataFlycGetPushPowerParam();
            }
            dataFlycGetPushPowerParam = instance;
        }
        return dataFlycGetPushPowerParam;
    }

    public float getEscAverageSpeed() {
        return ((Float) get(0, 4, Float.class)).floatValue();
    }

    public float getLift() {
        return ((Float) get(4, 4, Float.class)).floatValue();
    }

    public float getPowerPercentage() {
        return ((Float) get(8, 4, Float.class)).floatValue();
    }

    public float getRemainingPower() {
        return ((Float) get(12, 4, Float.class)).floatValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
