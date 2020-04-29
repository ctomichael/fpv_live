package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;

@Keep
public class DataOnBoardSDKGetPushSearchlightInfo extends DataBase {
    public static DataOnBoardSDKGetPushSearchlightInfo getInstance() {
        return SingletonHolder.instance;
    }

    @Keep
    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataOnBoardSDKGetPushSearchlightInfo instance = new DataOnBoardSDKGetPushSearchlightInfo();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
    }

    public boolean isSearchlightEnabled() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getSearchlightPower() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public short getSearchlightTemperature() {
        return ((Short) get(2, 2, Short.class)).shortValue();
    }
}
