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
public class DataWifiSetSelectionMode extends DataBase implements DJIDataSyncListener {
    private static DataWifiSetSelectionMode instance = null;
    private int selectionMode;

    private static synchronized DataWifiSetSelectionMode getInstance() {
        DataWifiSetSelectionMode dataWifiSetSelectionMode;
        synchronized (DataWifiSetSelectionMode.class) {
            if (instance == null) {
                instance = new DataWifiSetSelectionMode();
            }
            dataWifiSetSelectionMode = instance;
        }
        return dataWifiSetSelectionMode;
    }

    public DataWifiSetSelectionMode setSelectionMode(int selectionMode2) {
        this.selectionMode = selectionMode2;
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
        pack.cmdId = CmdIdWifi.CmdIdType.SetSelectionMode.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.selectionMode;
    }
}
