package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdPayloadSDK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataPayloadSendDataToWidget extends DataBase implements DJIDataSyncListener {
    public static final int MAX = 32;
    private byte[] datas;

    public DataPayloadSendDataToWidget setData(byte[] data) {
        if (data.length > 32) {
            throw new RuntimeException();
        }
        this.datas = data;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int length = 32;
        if (this.datas.length <= 32) {
            length = this.datas.length;
        }
        this._sendData = new byte[(length + 1)];
        this._sendData[0] = (byte) length;
        System.arraycopy(this.datas, 0, this._sendData, 1, length);
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
        pack.cmdId = CmdIdPayloadSDK.CmdIdType.sendDataToPayLoad.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 5;
        start(pack, callBack);
    }
}
