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
public class DataWifiSetModeChannel extends DataBase implements DJIDataSyncListener {
    private static DataWifiSetModeChannel instance;
    private int mWifiChannel;
    private int mWifiMode = 1;

    public static synchronized DataWifiSetModeChannel getInstance() {
        DataWifiSetModeChannel dataWifiSetModeChannel;
        synchronized (DataWifiSetModeChannel.class) {
            if (instance == null) {
                instance = new DataWifiSetModeChannel();
            }
            dataWifiSetModeChannel = instance;
        }
        return dataWifiSetModeChannel;
    }

    public DataWifiSetModeChannel setWifiMode(int wifiMode) {
        if (wifiMode < 0 || wifiMode > 1) {
            wifiMode = 1;
        }
        this.mWifiMode = wifiMode;
        return this;
    }

    public DataWifiSetModeChannel setWifiChannel(int wifiChannel) {
        this.mWifiChannel = wifiChannel;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        this._sendData[0] = (byte) this.mWifiMode;
        BytesUtil.arraycopy(BytesUtil.getBytes(this.mWifiChannel), this._sendData, 1);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.WIFI.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.SetWifiModeChannel.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
