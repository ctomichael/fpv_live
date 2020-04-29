package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.Base64;
import dji.midware.data.config.P3.CmdIdADS_B;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataADSBSetUserId extends DataBase implements DJIDataSyncListener {
    private static DataADSBSetUserId mInstance = null;
    private String mUserIdBase64 = "";

    public static DataADSBSetUserId getInstance() {
        if (mInstance == null) {
            mInstance = new DataADSBSetUserId();
        }
        return mInstance;
    }

    public DataADSBSetUserId setUserIdBase64(String userIdBase64) {
        this.mUserIdBase64 = userIdBase64;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.timeOut = 3000;
        pack.cmdSet = CmdSet.ADS_B.value();
        pack.cmdId = CmdIdADS_B.CmdIdType.SetUserId.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = Base64.decode(this.mUserIdBase64, 0);
    }
}
