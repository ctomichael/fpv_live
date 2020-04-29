package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCommonControlUpgrade extends DataBase implements DJIDataSyncListener {
    private static DataCommonControlUpgrade instance = null;
    private ControlCmd controlCmd;
    private int mReceiveId = 0;
    private DeviceType mReceiveType = DeviceType.DM368;

    public static synchronized DataCommonControlUpgrade getInstance() {
        DataCommonControlUpgrade dataCommonControlUpgrade;
        synchronized (DataCommonControlUpgrade.class) {
            if (instance == null) {
                instance = new DataCommonControlUpgrade();
            }
            dataCommonControlUpgrade = instance;
        }
        return dataCommonControlUpgrade;
    }

    public DataCommonControlUpgrade setControlCmd(ControlCmd controlCmd2) {
        this.controlCmd = controlCmd2;
        return this;
    }

    public DataCommonControlUpgrade setReceiveType(DeviceType type) {
        this.mReceiveType = type;
        return this;
    }

    public DataCommonControlUpgrade setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.controlCmd.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.ControlUpgrade.value();
        start(pack, callBack);
    }

    @Keep
    public enum ControlCmd {
        Cancel(0),
        Start(1),
        Pause(2),
        Stop(3),
        StopPush(4),
        Restart(5),
        OTHER(7);
        
        private int data;

        private ControlCmd(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ControlCmd find(int b) {
            ControlCmd result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
