package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataFlycGetWaypointInterruption extends DataBase implements DJIDataSyncListener {
    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public double getLongitude() {
        return (((Double) get(1, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public double getLatitude() {
        return (((Double) get(9, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public float getAltitude() {
        return ((Float) get(17, 4, Float.class)).floatValue();
    }

    public int getCurrentIndex() {
        return ((Integer) get(21, 1, Integer.class)).intValue();
    }

    public int getMissionId() {
        return ((Integer) get(22, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = 0;
    }

    public enum InterruptionResult {
        VALID(0),
        INVALID_MISSION_COMPLETED(1),
        INVALID_BEFORE_FIRST_WAYPOINT(2);
        
        private final int data;

        private InterruptionResult(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static InterruptionResult find(int value) {
            InterruptionResult result = VALID;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.GetWaypointInterruption.value();
        start(pack, callBack);
    }
}
