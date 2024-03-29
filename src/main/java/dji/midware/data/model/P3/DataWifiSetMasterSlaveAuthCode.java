package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdWifi;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataWifiSetMasterSlaveAuthCode extends DataBase implements DJIDataSyncListener {
    private static DataWifiSetMasterSlaveAuthCode mInstance = null;
    private String mAuthCode;

    public static synchronized DataWifiSetMasterSlaveAuthCode getInstance() {
        DataWifiSetMasterSlaveAuthCode dataWifiSetMasterSlaveAuthCode;
        synchronized (DataWifiSetMasterSlaveAuthCode.class) {
            if (mInstance == null) {
                mInstance = new DataWifiSetMasterSlaveAuthCode();
            }
            dataWifiSetMasterSlaveAuthCode = mInstance;
        }
        return dataWifiSetMasterSlaveAuthCode;
    }

    public DataWifiSetMasterSlaveAuthCode setAuthCode(String authCode) {
        this.mAuthCode = authCode;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = BytesUtil.getBytesUTF8(this.mAuthCode);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.WIFI_G.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.SetMasterSlaveAuthCode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
