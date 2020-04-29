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
public class DataWifiGetWifiFreqMode extends DataBase implements DJIDataSyncListener {
    private static DataWifiGetWifiFreqMode instance = null;

    public static synchronized DataWifiGetWifiFreqMode getInstance() {
        DataWifiGetWifiFreqMode dataWifiGetWifiFreqMode;
        synchronized (DataWifiGetWifiFreqMode.class) {
            if (instance == null) {
                instance = new DataWifiGetWifiFreqMode();
            }
            dataWifiGetWifiFreqMode = instance;
        }
        return dataWifiGetWifiFreqMode;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.WIFI.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.GetWifiFreqMode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getFreqMode() {
        int res = BytesUtil.getInt(this._recData[0]);
        if (res > 125) {
            return (res - 255) - 1;
        }
        return res;
    }
}
