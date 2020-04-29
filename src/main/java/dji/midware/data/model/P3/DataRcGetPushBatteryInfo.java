package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICommonDataBase;

@Keep
@EXClassNullAway
public class DataRcGetPushBatteryInfo extends DJICommonDataBase {
    private static DataRcGetPushBatteryInfo instance = null;

    public static synchronized DataRcGetPushBatteryInfo getInstance() {
        DataRcGetPushBatteryInfo dataRcGetPushBatteryInfo;
        synchronized (DataRcGetPushBatteryInfo.class) {
            if (instance == null) {
                instance = new DataRcGetPushBatteryInfo();
            }
            dataRcGetPushBatteryInfo = instance;
        }
        return dataRcGetPushBatteryInfo;
    }

    public int getBatteryVolume() {
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public int getBattery() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
