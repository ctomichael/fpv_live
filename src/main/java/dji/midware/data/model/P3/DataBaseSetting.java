package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataBaseSetting extends DataBase implements DJIDataSyncListener {
    protected int cmdId;
    protected CmdSet cmdSet;
    protected int mRepeatTime = 2;
    protected byte[] mSendData = null;
    protected int mTimeOut = 1000;
    protected DeviceType receiver;
    protected int value;

    public DataBaseSetting setCmdSet(CmdSet cmdSet2) {
        this.cmdSet = cmdSet2;
        return this;
    }

    public DataBaseSetting setCmdId(int cmdId2) {
        this.cmdId = cmdId2;
        return this;
    }

    public DataBaseSetting setValue(int value2) {
        this.value = value2;
        return this;
    }

    public DataBaseSetting setReceiver(DeviceType device) {
        this.receiver = device;
        return this;
    }

    public DataBaseSetting setPackParam(int timeout, int repeat) {
        if (timeout > 0) {
            this.mTimeOut = timeout;
        }
        if (repeat > 0 && repeat <= 3) {
            this.mRepeatTime = repeat;
        }
        return this;
    }

    public DataBaseSetting setSendData(byte[] data) {
        this.mSendData = data;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.mSendData == null) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) this.value;
            return;
        }
        this._sendData = this.mSendData;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.receiver.value();
        pack.cmdType = DataConfig.CMDTYPE.ACK.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = this.cmdSet.value();
        pack.cmdId = this.cmdId;
        pack.timeOut = 200;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }
}
