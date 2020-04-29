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
import dji.midware.util.AreaCodeUtil;

@Keep
@EXClassNullAway
public class DataWiFiSetWiFiCountryCode extends DataBase implements DJIDataSyncListener {
    private static DataWiFiSetWiFiCountryCode instance;
    private String codeFor2Point4G;
    private String codeFor5G;
    private boolean is5GSupported;

    public static synchronized DataWiFiSetWiFiCountryCode getInstance() {
        DataWiFiSetWiFiCountryCode dataWiFiSetWiFiCountryCode;
        synchronized (DataWiFiSetWiFiCountryCode.class) {
            if (instance == null) {
                instance = new DataWiFiSetWiFiCountryCode();
            }
            dataWiFiSetWiFiCountryCode = instance;
        }
        return dataWiFiSetWiFiCountryCode;
    }

    public DataWiFiSetWiFiCountryCode set2Point4GCountryCode(String code) {
        this.codeFor2Point4G = code;
        return this;
    }

    public DataWiFiSetWiFiCountryCode set5GCountryCode(String code) {
        this.codeFor5G = code;
        return this;
    }

    public DataWiFiSetWiFiCountryCode setSupported5G(boolean is5GSupported2) {
        this.is5GSupported = is5GSupported2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.receiverType = AreaCodeUtil.getSetAreaCodeDeviceType(false).value();
        pack.timeOut = 4000;
        pack.repeatTimes = 3;
        pack.cmdId = CmdIdWifi.CmdIdType.SetWiFiCountryCode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 1;
        this._sendData = new byte[10];
        if (this.codeFor2Point4G == null || this.codeFor2Point4G.length() < 2) {
            this._sendData[0] = 0;
            this._sendData[1] = 0;
        } else {
            this._sendData[0] = (byte) this.codeFor2Point4G.charAt(0);
            this._sendData[1] = (byte) this.codeFor2Point4G.charAt(1);
        }
        this._sendData[2] = 0;
        this._sendData[3] = 0;
        if (this.codeFor5G == null || this.codeFor5G.length() < 2) {
            this._sendData[4] = 0;
            this._sendData[5] = 0;
        } else {
            this._sendData[4] = (byte) this.codeFor5G.charAt(0);
            this._sendData[5] = (byte) this.codeFor5G.charAt(1);
        }
        this._sendData[6] = 0;
        this._sendData[7] = 0;
        byte[] bArr = this._sendData;
        if (!this.is5GSupported) {
            i = 0;
        }
        bArr[8] = (byte) i;
    }

    public boolean hasCountryCodeUpdated() {
        return this._recData != null && this._recData.length >= 2 && this._recData[1] == 1;
    }
}
