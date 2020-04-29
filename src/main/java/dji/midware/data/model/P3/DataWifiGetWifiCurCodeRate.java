package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdDm368;
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
public class DataWifiGetWifiCurCodeRate extends DataBase implements DJIDataSyncListener {
    private static DataWifiGetWifiCurCodeRate instance = null;

    public static synchronized DataWifiGetWifiCurCodeRate getInstance() {
        DataWifiGetWifiCurCodeRate dataWifiGetWifiCurCodeRate;
        synchronized (DataWifiGetWifiCurCodeRate.class) {
            if (instance == null) {
                instance = new DataWifiGetWifiCurCodeRate();
            }
            dataWifiGetWifiCurCodeRate = instance;
        }
        return dataWifiGetWifiCurCodeRate;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DM368.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.GetWifiCurCodeRate.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getCurCodeRate() {
        return BytesUtil.getInt(this._recData[0]);
    }
}
