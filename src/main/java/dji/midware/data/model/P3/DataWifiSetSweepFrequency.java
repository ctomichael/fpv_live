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
public class DataWifiSetSweepFrequency extends DataBase implements DJIDataSyncListener {
    private static DataWifiSetSweepFrequency mInstance = null;
    private int isOpen = 0;

    public static synchronized DataWifiSetSweepFrequency getInstance() {
        DataWifiSetSweepFrequency dataWifiSetSweepFrequency;
        synchronized (DataWifiSetSweepFrequency.class) {
            if (mInstance == null) {
                mInstance = new DataWifiSetSweepFrequency();
            }
            dataWifiSetSweepFrequency = mInstance;
        }
        return dataWifiSetSweepFrequency;
    }

    public DataWifiSetSweepFrequency setIsOpen(boolean _isopen) {
        this.isOpen = _isopen ? 1 : 0;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.isOpen;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.WIFI.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.SetSweepFrequency.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
