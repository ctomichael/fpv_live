package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdFlyc2;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataFlyc2GetWaypointInfo extends DataBase implements DJIDataSyncListener {
    private CmdType type = CmdType.UNKNOWN;

    public DataFlyc2GetWaypointInfo setCmdType(CmdType type2) {
        this.type = type2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public CmdType getCmdType() {
        return CmdType.find(((Integer) get(1, 1, Integer.class)).intValue());
    }

    public int getVersion() {
        if (getCmdType() == CmdType.VERSION) {
            return ((Integer) get(2, 1, Integer.class)).intValue();
        }
        return -1;
    }

    public int getMaxWaypointNum() {
        if (getCmdType() == CmdType.WAYPOINT_MAX_NUM) {
            return ((Integer) get(2, 2, Integer.class)).intValue();
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.type.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC2.value();
        pack.cmdId = CmdIdFlyc2.CmdIdType.GetWaypointInfo.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    public enum CmdType {
        VERSION(0),
        WAYPOINT_MAX_NUM(1),
        UNKNOWN(255);
        
        private int data;

        private CmdType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdType find(int b) {
            CmdType result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
