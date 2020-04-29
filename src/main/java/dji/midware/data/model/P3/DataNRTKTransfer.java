package dji.midware.data.model.P3;

import dji.log.DJILog;
import dji.midware.data.config.P3.CmdIdRTK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataNRTKTransfer extends DataBase implements DJIDataSyncListener {
    private byte[] data;
    private int index;
    private int type;

    public void setData(byte[] data2, int type2, int index2) {
        DJILog.d("RTCMTRANSFER", "transferData setData: " + data2.length, new Object[0]);
        this.data = data2;
        this.type = type2;
        this.index = index2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.data != null) {
            this._sendData = new byte[(this.data.length + 2)];
            this._sendData[0] = (byte) this.index;
            this._sendData[1] = (byte) this.type;
            System.arraycopy(this.data, 0, this._sendData, 2, this.data.length);
            return;
        }
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.index;
        this._sendData[1] = (byte) this.type;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RTK.value();
        pack.cmdId = CmdIdRTK.CmdIdType.NrtkDataTransfer.value();
        doPack();
        start(pack, callBack);
    }
}
