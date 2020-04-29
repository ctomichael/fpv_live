package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdGimbal;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataGimbalRelease extends DataBase implements DJIDataSyncListener {
    private static DataGimbalRelease instance = null;

    public static synchronized DataGimbalRelease getInstance() {
        DataGimbalRelease dataGimbalRelease;
        synchronized (DataGimbalRelease.class) {
            if (instance == null) {
                instance = new DataGimbalRelease();
            }
            dataGimbalRelease = instance;
        }
        return dataGimbalRelease;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.SetGimbalRelease.value();
        start(pack, callBack);
    }
}
