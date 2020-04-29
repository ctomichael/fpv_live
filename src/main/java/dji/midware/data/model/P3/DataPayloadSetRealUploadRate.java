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
import dji.midware.util.BytesUtil;

@Keep
public class DataPayloadSetRealUploadRate extends DataBase implements DJIDataSyncListener {
    private static DataPayloadSetRealUploadRate instance;
    private int realRate = 0;

    public static synchronized DataPayloadSetRealUploadRate getInstance() {
        DataPayloadSetRealUploadRate dataPayloadSetRealUploadRate;
        synchronized (DataPayloadSetRealUploadRate.class) {
            if (instance == null) {
                instance = new DataPayloadSetRealUploadRate();
            }
            dataPayloadSetRealUploadRate = instance;
        }
        return dataPayloadSetRealUploadRate;
    }

    public DataPayloadSetRealUploadRate setRealRate(int realRate2) {
        this.realRate = realRate2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = BytesUtil.getUnsignedBytes(this.realRate);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.receiverId = 0;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.PayloadSDK.value();
        pack.cmdId = CmdIdPayloadSDK.CmdIdType.setUploadRate.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }
}
