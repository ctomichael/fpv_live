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
public class DataDm368GetForesightShowed extends DataBase implements DJIDataSyncListener {
    private static DataDm368GetForesightShowed instance = null;

    public static synchronized DataDm368GetForesightShowed getInstance() {
        DataDm368GetForesightShowed dataDm368GetForesightShowed;
        synchronized (DataDm368GetForesightShowed.class) {
            if (instance == null) {
                instance = new DataDm368GetForesightShowed();
            }
            dataDm368GetForesightShowed = instance;
        }
        return dataDm368GetForesightShowed;
    }

    public boolean isOpen() {
        if (this._recData[0] == 0) {
            return false;
        }
        return true;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DM368.value();
        pack.receiverId = 0;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.GetForesightShowed.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
