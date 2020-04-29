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

public class DataModule4GSetApn extends DataBase implements DJIDataSyncListener {
    private boolean isAuto;
    private String mAddress;
    private DeviceType mReceiverType = DeviceType.OSD;

    public DataModule4GSetApn setAuto(boolean isAuto2) {
        this.isAuto = isAuto2;
        return this;
    }

    public DataModule4GSetApn setAddress(String address) {
        this.mAddress = address;
        return this;
    }

    public DataModule4GSetApn setReceiverType(DeviceType type) {
        this.mReceiverType = type;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.isAuto) {
            this._sendData = new byte[1];
            this._sendData[0] = 0;
            return;
        }
        try {
            byte[] apnAddr = this.mAddress == null ? new byte[0] : this.mAddress.getBytes("UTF-8");
            this._sendData = new byte[(apnAddr.length + 1 + 1)];
            this._sendData[0] = 1;
            System.arraycopy(apnAddr, 0, this._sendData, 1, apnAddr.length);
            this._sendData[this._sendData.length - 1] = 0;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiverType == null ? DeviceType.OSD.value() : this.mReceiverType.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.Module4G.value();
        pack.cmdId = CmdIdModule4G.CmdIdType.SetNetAPN.value();
        start(pack, callBack);
    }
}
