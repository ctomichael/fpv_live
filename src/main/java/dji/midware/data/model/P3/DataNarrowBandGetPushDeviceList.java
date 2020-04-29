package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.log.DJILog;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.List;

@Keep
public class DataNarrowBandGetPushDeviceList extends DataBase {
    private static DataNarrowBandGetPushDeviceList instance = null;

    public static synchronized DataNarrowBandGetPushDeviceList getInstance() {
        DataNarrowBandGetPushDeviceList dataNarrowBandGetPushDeviceList;
        synchronized (DataNarrowBandGetPushDeviceList.class) {
            if (instance == null) {
                instance = new DataNarrowBandGetPushDeviceList();
            }
            dataNarrowBandGetPushDeviceList = instance;
        }
        return dataNarrowBandGetPushDeviceList;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public List<DeviceInfo> getDeviceList() {
        if (this._recData == null) {
            return new ArrayList();
        }
        byte[] sub = new byte[3];
        int length = this._recData.length / 20;
        List<DeviceInfo> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            try {
                System.arraycopy(this._recData, (i * 20) + 4, sub, 0, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DeviceInfo d = new DeviceInfo(BytesUtil.byte2hexNoSep(sub), ((Integer) get((i * 20) + 3, 1, Integer.class)).intValue());
            if (!d.getId().equals("000000")) {
                list.add(d);
            }
            DJILog.e("NarrowBandScanDevice", d.toString(), new Object[0]);
        }
        return list;
    }

    @Keep
    public static class DeviceInfo {
        private int connectionState;
        private String id;

        public DeviceInfo(String id2, int connectionState2) {
            this.id = id2;
            this.connectionState = connectionState2;
        }

        public String toString() {
            return "Id: " + getId() + " Role: " + this.connectionState;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public String getId() {
            return this.id;
        }

        public boolean getConnectState(int flagIndex) {
            return BytesUtil.getBitsFromByte(this.connectionState, flagIndex, 1) == 1;
        }

        public boolean isFull() {
            return getConnectState(1) && getConnectState(2) && getConnectState(3);
        }
    }
}
