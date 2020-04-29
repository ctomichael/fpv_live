package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdPayloadSDK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataPayloadTurnOnOffPush extends DataBase implements DJIDataSyncListener {
    private final int TURN_OFF = 0;
    private final int TURN_ON = 1;
    private int setData = 1;

    public DataPayloadTurnOnOffPush turnOn() {
        this.setData = 1;
        return this;
    }

    public DataPayloadTurnOnOffPush turnOff() {
        this.setData = 0;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.setData;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.receiverId = 0;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.PayloadSDK.value();
        pack.cmdId = CmdIdPayloadSDK.CmdIdType.turnOnOrOffPush.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 6;
        start(pack, callBack);
    }
}
