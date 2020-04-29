package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;

public class DataOsdGetPushSdrLteStatus extends DataBase {
    private static final int LINK_STATUS_DATA_LEN = 6;

    public static DataOsdGetPushSdrLteStatus getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataOsdGetPushSdrLteStatus INSTANCE = new DataOsdGetPushSdrLteStatus();

        private Holder() {
        }
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    private P2PNetLinkStatus createLinkStatus(int from) {
        byte[] data = new byte[6];
        System.arraycopy(this._recData, from, data, 0, 6);
        return new P2PNetLinkStatus(data);
    }

    public P2PNetLinkStatus getMasterRc2DroneLink() {
        if (!isGetted() || this._recData.length < 7) {
            return null;
        }
        return createLinkStatus(1);
    }

    public P2PNetLinkStatus getDrone2MasterRcLink() {
        if (!isGetted() || this._recData.length < 13) {
            return null;
        }
        return createLinkStatus(7);
    }

    public P2PNetLinkStatus getSlaveRc2DroneLink() {
        if (!isGetted() || this._recData.length < 19) {
            return null;
        }
        return createLinkStatus(13);
    }

    public P2PNetLinkStatus getDrone2SlaveRcLink() {
        if (!isGetted() || this._recData.length < 25) {
            return null;
        }
        return createLinkStatus(19);
    }

    public P2PNetLinkStatus getRTK2DroneLink() {
        if (!isGetted() || this._recData.length < 31) {
            return null;
        }
        return createLinkStatus(25);
    }

    public P2PNetLinkStatus getDrone2RTKLink() {
        if (!isGetted() || this._recData.length < 37) {
            return null;
        }
        return createLinkStatus(31);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public static class P2PNetLinkStatus {
        private int mDroneType;
        private int mLTEBar;
        private NetLinkState mLTEStatus;
        private int mSDRBar;
        private NetLinkState mSDRStatus;

        public P2PNetLinkStatus(byte[] data) {
            if (data != null && data.length == 6) {
                this.mDroneType = data[0];
                this.mSDRStatus = NetLinkState.find(data[1] & 15);
                this.mLTEStatus = NetLinkState.find(data[1] >> 4);
                this.mSDRBar = data[2];
                this.mLTEBar = data[3];
            }
        }

        public NetLinkState getSdrState() {
            return this.mSDRStatus;
        }

        public NetLinkState getLteState() {
            return this.mLTEStatus;
        }

        public int getSdrBar() {
            return this.mSDRBar;
        }

        public int getLteBar() {
            return this.mLTEBar;
        }

        public int getDroneType() {
            return this.mDroneType;
        }
    }

    public enum NetLinkState {
        UNPAIRED(0),
        PAIRED_NOT_CONNECT(1),
        CONNECTED(2),
        USING(3),
        UNKNOWN(255);
        
        int value;

        private NetLinkState(int value2) {
            this.value = value2;
        }

        public static NetLinkState find(int value2) {
            NetLinkState target = UNKNOWN;
            NetLinkState[] values = values();
            for (NetLinkState status : values) {
                if (status.value == value2) {
                    return status;
                }
            }
            return target;
        }
    }
}
