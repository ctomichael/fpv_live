package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdRTK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class NRTKStartSender extends DataBase implements DJIDataSyncListener {
    public static final int DISENABLE = 0;
    public static final int ENABLE = 1;
    private int dataType;

    public NRTKStartSender setData(int type) {
        this.dataType = type;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.dataType;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RTK.value();
        pack.cmdId = CmdIdRTK.CmdIdType.setRTKType.value();
        start(pack, callBack);
    }
}
