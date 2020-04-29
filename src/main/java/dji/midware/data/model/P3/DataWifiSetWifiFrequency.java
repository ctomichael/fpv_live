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
public class DataWifiSetWifiFrequency extends DataBase implements DJIDataSyncListener {
    private static DataWifiSetWifiFrequency instance;
    private int frequencyMode = 0;
    private boolean isFromLongan = false;

    public DataWifiSetWifiFrequency setFrequency(int frequencyMode2) {
        this.frequencyMode = frequencyMode2;
        return this;
    }

    public static synchronized DataWifiSetWifiFrequency getInstance() {
        DataWifiSetWifiFrequency dataWifiSetWifiFrequency;
        synchronized (DataWifiSetWifiFrequency.class) {
            if (instance == null) {
                instance = new DataWifiSetWifiFrequency();
            }
            dataWifiSetWifiFrequency = instance;
        }
        return dataWifiSetWifiFrequency;
    }

    public DataWifiSetWifiFrequency setFromLongan(boolean isFromLongan2) {
        this.isFromLongan = isFromLongan2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        if (this.isFromLongan) {
            pack.receiverType = DeviceType.WIFI_G.value();
        } else {
            pack.receiverType = DeviceType.WIFI.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.SetWifiFrequency.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.frequencyMode;
    }
}
