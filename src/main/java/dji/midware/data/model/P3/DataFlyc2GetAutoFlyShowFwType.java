package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdFlyc2;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataFlyc2GetAutoFlyShowFwType extends DataBase implements DJIDataSyncListener {
    public int getFwType() {
        return ((Byte) get(0, 1, Byte.class)).byteValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = 10;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.receiverId = 6;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC2.value();
        pack.cmdId = CmdIdFlyc2.CmdIdType.GetAutoFlyShowFwType.value();
        start(pack, callBack);
    }
}
