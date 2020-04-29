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

@Keep
@EXClassNullAway
public class DataWifiSetWifiCodeRate extends DataBase implements DJIDataSyncListener {
    private static DataWifiSetWifiCodeRate instance = null;
    private int mCodeRate = 2;

    public static synchronized DataWifiSetWifiCodeRate getInstance() {
        DataWifiSetWifiCodeRate dataWifiSetWifiCodeRate;
        synchronized (DataWifiSetWifiCodeRate.class) {
            if (instance == null) {
                instance = new DataWifiSetWifiCodeRate();
            }
            dataWifiSetWifiCodeRate = instance;
        }
        return dataWifiSetWifiCodeRate;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DM368.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.SetWifiCodeRate.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mCodeRate;
    }

    public DataWifiSetWifiCodeRate setCodeRate(int _codeRate) {
        this.mCodeRate = _codeRate;
        return this;
    }
}
