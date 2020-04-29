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

@Keep
@EXClassNullAway
public class DataWifiSetWifiFreq5GMode extends DataBase implements DJIDataSyncListener {
    private static DataWifiSetWifiFreq5GMode instance = null;
    private int frequencyMode = 0;

    public static synchronized DataWifiSetWifiFreq5GMode getInstance() {
        DataWifiSetWifiFreq5GMode dataWifiSetWifiFreq5GMode;
        synchronized (DataWifiSetWifiFreq5GMode.class) {
            if (instance == null) {
                instance = new DataWifiSetWifiFreq5GMode();
            }
            dataWifiSetWifiFreq5GMode = instance;
        }
        return dataWifiSetWifiFreq5GMode;
    }

    public DataWifiSetWifiFreq5GMode setFrequencyMode(int frequencyMode2) {
        this.frequencyMode = frequencyMode2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.WIFI.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.SetWifiFreq5GMode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.frequencyMode;
    }
}
