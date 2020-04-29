package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;

@Keep
public class DataOnBoardSDKGetPushBeaconInfo extends DataBase {
    public static DataOnBoardSDKGetPushBeaconInfo getInstance() {
        return SingletonHolder.instance;
    }

    @Keep
    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataOnBoardSDKGetPushBeaconInfo instance = new DataOnBoardSDKGetPushBeaconInfo();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
    }

    public boolean isNavigationLEDEnabled() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getNavigationLEDPower() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getNavigationLEDFlickerFrequency() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }
}
