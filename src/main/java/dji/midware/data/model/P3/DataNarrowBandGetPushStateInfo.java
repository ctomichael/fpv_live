package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.util.BytesUtil;

@Keep
public class DataNarrowBandGetPushStateInfo extends DataBase {
    private static DataNarrowBandGetPushStateInfo instance = null;

    public static synchronized DataNarrowBandGetPushStateInfo getInstance() {
        DataNarrowBandGetPushStateInfo dataNarrowBandGetPushStateInfo;
        synchronized (DataNarrowBandGetPushStateInfo.class) {
            if (instance == null) {
                instance = new DataNarrowBandGetPushStateInfo();
            }
            dataNarrowBandGetPushStateInfo = instance;
        }
        return dataNarrowBandGetPushStateInfo;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getMasterMode() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getCurrentWorkBand() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public boolean isBandWorking(int flagIndex) {
        return BytesUtil.getBitsFromByte(((Integer) get(1, 1, Integer.class)).intValue(), flagIndex, 1) == 1;
    }

    public boolean getConnectState(int flagIndex) {
        return BytesUtil.getBitsFromByte(((Integer) get(2, 1, Integer.class)).intValue(), flagIndex, 1) == 1;
    }

    public boolean isBandAvailable(int flagIndex) {
        return BytesUtil.getBitsFromByte(((Integer) get(3, 1, Integer.class)).intValue(), flagIndex, 1) == 1;
    }

    public int getAvailableBandNum() {
        return BytesUtil.getBitsFromByte(((Integer) get(4, 1, Integer.class)).intValue(), 4, 4);
    }

    public int getAvailableDevicesNum() {
        return BytesUtil.getBitsFromByte(((Integer) get(4, 1, Integer.class)).intValue(), 0, 4);
    }

    public int getBand(int bandIndex) {
        return ((Integer) get(((getAvailableDevicesNum() + 1) * bandIndex) + 5, 1, Integer.class)).intValue();
    }

    public int getInfo(int bandIndex, int deviceIndex) {
        return ((Integer) get(((getAvailableDevicesNum() + 1) * bandIndex) + deviceIndex + 6, 1, Integer.class)).intValue();
    }
}
