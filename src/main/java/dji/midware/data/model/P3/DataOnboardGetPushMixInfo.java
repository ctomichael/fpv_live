package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOnboardGetPushMixInfo extends DataBase {
    private static DataOnboardGetPushMixInfo instance = null;

    public static synchronized DataOnboardGetPushMixInfo getInstance() {
        DataOnboardGetPushMixInfo dataOnboardGetPushMixInfo;
        synchronized (DataOnboardGetPushMixInfo.class) {
            if (instance == null) {
                instance = new DataOnboardGetPushMixInfo();
            }
            dataOnboardGetPushMixInfo = instance;
        }
        return dataOnboardGetPushMixInfo;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getMainCameraPercent() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getSecondaryCameraPercent() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getFpvPercent() {
        return (100 - getMainCameraPercent()) - getSecondaryCameraPercent();
    }

    public int getMainCameraPercentRelativeToWholeCameraBandwidth() {
        return Math.round(10.0f * ((1.0f * ((float) getMainCameraPercent())) / ((float) (getMainCameraPercent() + getSecondaryCameraPercent()))));
    }

    public int getMappedGimbal() {
        return (((Integer) get(4, 1, Integer.class)).intValue() & 1) != 0 ? 1 : 0;
    }

    public boolean isSimultaneousControlGimbal() {
        return (((Integer) get(4, 1, Integer.class)).intValue() & 2) != 0;
    }
}
