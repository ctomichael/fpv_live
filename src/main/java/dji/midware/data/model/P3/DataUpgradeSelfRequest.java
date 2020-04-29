package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;

@Keep
@EXClassNullAway
public class DataUpgradeSelfRequest extends DataBase implements DJIDataAsync2Listener {
    private static DataUpgradeSelfRequest instance = null;
    private ControlCmd controlCmd;

    public static synchronized DataUpgradeSelfRequest getInstance() {
        DataUpgradeSelfRequest dataUpgradeSelfRequest;
        synchronized (DataUpgradeSelfRequest.class) {
            if (instance == null) {
                instance = new DataUpgradeSelfRequest();
            }
            dataUpgradeSelfRequest = instance;
        }
        return dataUpgradeSelfRequest;
    }

    public boolean isForce() {
        return this._recData != null && this._recData.length > 0 && this._recData[0] == 1;
    }

    public boolean isNeedForceUpgrade() {
        return this._recData != null && this._recData.length > 0 && this._recData[0] > 1;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public DataUpgradeSelfRequest setControlCmd(ControlCmd controlCmd2) {
        this.controlCmd = controlCmd2;
        return this;
    }

    public DeviceType getSenderDeviceType() {
        if (this.pack != null) {
            return DeviceType.find(this.pack.senderType);
        }
        return null;
    }

    public int getSenderDeviceId() {
        if (this.pack != null) {
            return this.pack.senderId;
        }
        return 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) Ccode.OK.value();
        this._sendData[1] = (byte) this.controlCmd.value();
    }

    public void start() {
        if (!DataOsdGetPushCommon.getInstance().isMotorUp()) {
            SendPack pack = new SendPack();
            if (this.pack != null && !DataOsdGetPushCommon.getInstance().isMotorUp()) {
                pack.seq = this.pack.seq;
                pack.senderType = DeviceType.APP.value();
                pack.receiverType = this.pack.senderType;
                pack.receiverId = this.pack.senderId;
                pack.cmdType = DataConfig.CMDTYPE.ACK.value();
                pack.isNeedAck = DataConfig.NEEDACK.NO.value();
                pack.encryptType = DataConfig.EncryptType.NO.value();
                pack.cmdSet = CmdSet.COMMON.value();
                pack.cmdId = this.pack.cmdId;
                start(pack);
            }
        }
    }

    @Keep
    public enum ControlCmd {
        UNDO(0),
        DO(1),
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
