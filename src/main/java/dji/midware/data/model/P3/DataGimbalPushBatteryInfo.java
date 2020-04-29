package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataGimbalPushBatteryInfo extends DataBase {
    private static DataGimbalPushBatteryInfo instance = null;

    public static synchronized DataGimbalPushBatteryInfo getInstance() {
        DataGimbalPushBatteryInfo dataGimbalPushBatteryInfo;
        synchronized (DataGimbalPushBatteryInfo.class) {
            if (instance == null) {
                instance = new DataGimbalPushBatteryInfo();
            }
            dataGimbalPushBatteryInfo = instance;
        }
        return dataGimbalPushBatteryInfo;
    }

    public int getCapacityPercentage() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
