package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataEyeSetTimeLapseSubMode extends DataBase implements DJIDataSyncListener {
    public static final int FIX_DIRECTION_MODE = 4;
    public static final int FREE_MODE = 1;
    public static final int POI_MODE = 3;
    public static final int TRACK_MODE = 2;
    private static DataEyeSetTimeLapseSubMode instance = null;
    private int subMode;

    public static synchronized DataEyeSetTimeLapseSubMode getInstance() {
        DataEyeSetTimeLapseSubMode dataEyeSetTimeLapseSubMode;
        synchronized (DataEyeSetTimeLapseSubMode.class) {
            if (instance == null) {
                instance = new DataEyeSetTimeLapseSubMode();
            }
            dataEyeSetTimeLapseSubMode = instance;
        }
        return dataEyeSetTimeLapseSubMode;
    }

    public DataEyeSetTimeLapseSubMode setTimeLapseSubMode(int subMode2) {
        this.subMode = subMode2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.subMode;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.SetTimeLapseSubMode.value();
        start(pack, callBack);
    }
}
