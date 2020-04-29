package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.thirdparty.afinal.core.Arrays;

@Keep
@EXClassNullAway
public class DataFlycGetPushAvoid extends DataBase {
    private static DataFlycGetPushAvoid instance = null;
    private final int[] mDistances = new int[4];

    public static synchronized DataFlycGetPushAvoid getInstance() {
        DataFlycGetPushAvoid dataFlycGetPushAvoid;
        synchronized (DataFlycGetPushAvoid.class) {
            if (instance == null) {
                instance = new DataFlycGetPushAvoid();
            }
            dataFlycGetPushAvoid = instance;
        }
        return dataFlycGetPushAvoid;
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
    }

    public boolean isVisualSensorEnable() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isVisualSensorWork() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isInStop() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 4) != 0;
    }

    public int[] getDistance() {
        Arrays.fill(this.mDistances, 0);
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() >= 10) {
            if (this._recData != null && this._recData.length >= 9) {
                for (int i = 0; i < this.mDistances.length; i++) {
                    this.mDistances[i] = ((Integer) get((i * 2) + 1, 2, Integer.class)).intValue();
                }
            }
        } else if (this._recData != null && this._recData.length >= 5) {
            for (int i2 = 0; i2 < this.mDistances.length; i2++) {
                this.mDistances[i2] = ((Integer) get(i2 + 1, 1, Integer.class)).intValue();
            }
        }
        return this.mDistances;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
