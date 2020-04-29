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
public class DataWifiGetAuthCode extends DataBase implements DJIDataSyncListener {
    private static DataWifiGetAuthCode mInstance = null;
    private String mAuthCode;
    private String mMasterId;

    public static synchronized DataWifiGetAuthCode getInstance() {
        DataWifiGetAuthCode dataWifiGetAuthCode;
        synchronized (DataWifiGetAuthCode.class) {
            if (mInstance == null) {
                mInstance = new DataWifiGetAuthCode();
            }
            dataWifiGetAuthCode = mInstance;
        }
        return dataWifiGetAuthCode;
    }

    public DataWifiGetAuthCode setMasterId(String masterId) {
        this.mMasterId = masterId;
        return this;
    }

    public String getAuthCode() {
        return this.mAuthCode;
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        if (data != null) {
            this.mAuthCode = BytesUtil.getStringUTF8(data);
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = BytesUtil.getBytesUTF8(this.mMasterId);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.WIFI_G.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.timeOut = 30000;
        pack.cmdId = CmdIdWifi.CmdIdType.GetAuthCode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
