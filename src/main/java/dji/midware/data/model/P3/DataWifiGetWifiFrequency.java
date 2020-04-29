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
public class DataWifiGetWifiFrequency extends DataBase implements DJIDataSyncListener {
    private static DataWifiGetWifiFrequency instance;
    private boolean isFromLongan = false;

    public static synchronized DataWifiGetWifiFrequency getInstance() {
        DataWifiGetWifiFrequency dataWifiGetWifiFrequency;
        synchronized (DataWifiGetWifiFrequency.class) {
            if (instance == null) {
                instance = new DataWifiGetWifiFrequency();
            }
            dataWifiGetWifiFrequency = instance;
        }
        return dataWifiGetWifiFrequency;
    }

    public DataWifiGetWifiFrequency setFromLongan(boolean isFromLongan2) {
        this.isFromLongan = isFromLongan2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
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
        pack.cmdId = CmdIdWifi.CmdIdType.GetWifiFrequency.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
