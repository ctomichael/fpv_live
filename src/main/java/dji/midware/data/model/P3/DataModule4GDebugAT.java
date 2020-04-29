package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdModule4G;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.io.UnsupportedEncodingException;

public class DataModule4GDebugAT extends DataBase implements DJIDataSyncListener {
    private String mATCmd;
    private DeviceType mReceiver = DeviceType.OSD;

    public DataModule4GDebugAT setATCmd(String atCmd) {
        this.mATCmd = atCmd;
        return this;
    }

    public DataModule4GDebugAT setReceiverType(DeviceType receiver) {
        this.mReceiver = receiver;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        try {
            byte[] cmd = this.mATCmd == null ? new byte[0] : this.mATCmd.getBytes("UTF-8");
            this._sendData = new byte[(cmd.length + 2 + 1)];
            int len = cmd.length + 1;
            this._sendData[0] = (byte) ((len >>> 0) & 255);
            this._sendData[1] = (byte) ((len >>> 8) & 255);
            System.arraycopy(cmd, 0, this._sendData, 2, cmd.length);
            this._sendData[this._sendData.length - 1] = 0;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiver == null ? DeviceType.OSD.value() : this.mReceiver.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.Module4G.value();
        pack.cmdId = CmdIdModule4G.CmdIdType.DebugATCmd.value();
        start(pack, callBack);
    }

    public String getResponse() {
        if (!isGetted()) {
            return null;
        }
        int len = ((Integer) get(0, 2, Integer.class)).intValue() - 1;
        if (len > 0) {
            return get(2, len);
        }
        return null;
    }
}
