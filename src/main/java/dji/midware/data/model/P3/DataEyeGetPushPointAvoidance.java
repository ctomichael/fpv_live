package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.thirdparty.afinal.core.Arrays;

@Keep
@EXClassNullAway
public class DataEyeGetPushPointAvoidance extends DataBase {
    private static DataEyeGetPushPointAvoidance instance = null;

    public static synchronized DataEyeGetPushPointAvoidance getInstance() {
        DataEyeGetPushPointAvoidance dataEyeGetPushPointAvoidance;
        synchronized (DataEyeGetPushPointAvoidance.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPointAvoidance();
            }
            dataEyeGetPushPointAvoidance = instance;
        }
        return dataEyeGetPushPointAvoidance;
    }

    public int getAlertLevel() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getObserveCount() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int[] getObserveValues() {
        int count = getObserveCount();
        int[] values = null;
        if (count != 0) {
            values = new int[count];
            Arrays.fill(values, Integer.MAX_VALUE);
            int i = 0;
            while (i < count && i + 2 < this._recData.length) {
                values[i] = ((Integer) get(i + 2, 1, Integer.class)).intValue();
                i++;
            }
        }
        return values;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
