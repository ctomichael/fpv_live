package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.base.DJICommonDataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCommonGetLockLicense extends DJICommonDataBase implements DJIDataSyncListener {
    private static DataCommonGetLockLicense instance = null;

    public static synchronized DataCommonGetLockLicense getInstance() {
        DataCommonGetLockLicense dataCommonGetLockLicense;
        synchronized (DataCommonGetLockLicense.class) {
            if (instance == null) {
                instance = new DataCommonGetLockLicense();
            }
            dataCommonGetLockLicense = instance;
        }
        return dataCommonGetLockLicense;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[]{69};
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverId = 1;
        pack.receiverType = DeviceType.DM368.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.LockLicense.value();
        pack.timeOut = 5000;
        start(pack, callBack);
    }
}
