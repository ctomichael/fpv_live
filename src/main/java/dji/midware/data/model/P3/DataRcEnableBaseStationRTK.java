package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataRcEnableBaseStationRTK extends DataBase implements DJIDataSyncListener {
    private boolean enabled = false;

    public DataRcEnableBaseStationRTK enableBaseStationRTK(boolean enable) {
        this.enabled = enable;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i;
        this._sendData = new byte[2];
        byte[] bArr = this._sendData;
        if (this.enabled) {
            i = 1;
        } else {
            i = 0;
        }
        bArr[0] = (byte) i;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.EnableBaseStationRTK.value();
        pack.timeOut = 3000;
        start(pack, callBack);
    }
}
