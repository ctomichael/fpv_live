package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcGetToggle extends DataBase implements DJIDataSyncListener {
    private static DataRcGetToggle instance = null;

    public static synchronized DataRcGetToggle getInstance() {
        DataRcGetToggle dataRcGetToggle;
        synchronized (DataRcGetToggle.class) {
            if (instance == null) {
                instance = new DataRcGetToggle();
            }
            dataRcGetToggle = instance;
        }
        return dataRcGetToggle;
    }

    public boolean getIsOpen() {
        byte[] recData = this._recData;
        if (recData == null || recData.length <= 0) {
            return false;
        }
        if (1 == recData[0]) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetToggle.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
