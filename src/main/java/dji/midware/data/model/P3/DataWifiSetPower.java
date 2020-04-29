package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdWifi;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataWifiSetPower extends DataBase implements DJIDataSyncListener {
    private static DataWifiSetPower instance;
    private boolean isSendToSky = false;
    private int mPower = 0;

    public static synchronized DataWifiSetPower getInstance() {
        DataWifiSetPower dataWifiSetPower;
        synchronized (DataWifiSetPower.class) {
            if (instance == null) {
                instance = new DataWifiSetPower();
            }
            dataWifiSetPower = instance;
        }
        return dataWifiSetPower;
    }

    public DataWifiSetPower setSendToSky(boolean sendToSky) {
        this.isSendToSky = sendToSky;
        return this;
    }

    public DataWifiSetPower setPower(int power) {
        this.mPower = power;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mPower;
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
        pack.cmdId = CmdIdWifi.CmdIdType.SetPower.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
