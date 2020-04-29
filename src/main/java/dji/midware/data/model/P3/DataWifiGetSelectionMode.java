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
public class DataWifiGetSelectionMode extends DataBase implements DJIDataSyncListener {
    public static DataWifiGetSelectionMode instance = null;

    public static synchronized DataWifiGetSelectionMode getInstance() {
        DataWifiGetSelectionMode dataWifiGetSelectionMode;
        synchronized (DataWifiGetSelectionMode.class) {
            if (instance == null) {
                instance = new DataWifiGetSelectionMode();
            }
            dataWifiGetSelectionMode = instance;
        }
        return dataWifiGetSelectionMode;
    }

    public int getSelectionMode() {
        if (this._recData == null) {
            return 0;
        }
        int res = BytesUtil.getInt(this._recData[0]);
        if (res > 125) {
            return (res - 255) - 1;
        }
        return res;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.WIFI.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.GetSelectionMode.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
