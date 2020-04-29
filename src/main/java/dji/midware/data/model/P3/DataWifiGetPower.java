package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdWifi;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataWifiGetPower extends DataBase implements DJIDataSyncListener {
    private static DataWifiGetPower instance;
    private boolean isSendToSky = false;

    public static synchronized DataWifiGetPower getInstance() {
        DataWifiGetPower dataWifiGetPower;
        synchronized (DataWifiGetPower.class) {
            if (instance == null) {
                instance = new DataWifiGetPower();
            }
            dataWifiGetPower = instance;
        }
        return dataWifiGetPower;
    }

    public DataWifiGetPower setSendToSky(boolean sendToSky) {
        this.isSendToSky = sendToSky;
        return this;
    }

    public int getPower() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        if (this.isSendToSky) {
            pack.receiverType = DeviceType.WIFI.value();
        } else {
            pack.receiverType = DeviceType.WIFI_G.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.GetPower.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
