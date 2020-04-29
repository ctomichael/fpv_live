package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushDevicesState extends DataBase {
    private static DataOsdGetPushDevicesState instance = null;

    public static synchronized DataOsdGetPushDevicesState getInstance() {
        DataOsdGetPushDevicesState dataOsdGetPushDevicesState;
        synchronized (DataOsdGetPushDevicesState.class) {
            if (instance == null) {
                instance = new DataOsdGetPushDevicesState();
            }
            dataOsdGetPushDevicesState = instance;
        }
        return dataOsdGetPushDevicesState;
    }

    public boolean getSignalQuality(DeviceType deviceType) {
        boolean result = false;
        if (this._recData == null) {
            return false;
        }
        int num = this._recData.length / 5;
        int i = 0;
        while (true) {
            if (i >= num) {
                break;
            } else if (!deviceType._equals(((Integer) get(i * 5, 1, Integer.class)).intValue())) {
                i++;
            } else if (((Integer) get((i * 5) + 1, 4, Integer.class)).intValue() != 0) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
