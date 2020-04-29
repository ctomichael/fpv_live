package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdWifi;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataWiFiGetSupportCountryCode extends DataBase implements DJIDataSyncListener {
    private static DataWiFiGetSupportCountryCode instance;
    private String codeFor2Point4G;

    public static synchronized DataWiFiGetSupportCountryCode getInstance() {
        DataWiFiGetSupportCountryCode dataWiFiGetSupportCountryCode;
        synchronized (DataWiFiGetSupportCountryCode.class) {
            if (instance == null) {
                instance = new DataWiFiGetSupportCountryCode();
            }
            dataWiFiGetSupportCountryCode = instance;
        }
        return dataWiFiGetSupportCountryCode;
    }

    public DataWiFiGetSupportCountryCode set2Point4GCountryCode(String code) {
        this.codeFor2Point4G = code;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.receiverType = DeviceType.WIFI.value();
        pack.timeOut = 4000;
        pack.repeatTimes = 3;
        pack.cmdId = CmdIdWifi.CmdIdType.IsSupportCountryCode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        if (!TextUtils.isEmpty(this.codeFor2Point4G) && this.codeFor2Point4G.length() >= 2) {
            this._sendData[0] = (byte) this.codeFor2Point4G.charAt(0);
            this._sendData[1] = (byte) this.codeFor2Point4G.charAt(1);
        }
    }
}
