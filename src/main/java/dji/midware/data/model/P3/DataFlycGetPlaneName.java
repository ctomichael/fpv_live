package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataFlycGetPlaneName extends DataBase implements DJIDataSyncListener {
    private static DataFlycGetPlaneName instance = null;

    public static synchronized DataFlycGetPlaneName getInstance() {
        DataFlycGetPlaneName dataFlycGetPlaneName;
        synchronized (DataFlycGetPlaneName.class) {
            if (instance == null) {
                instance = new DataFlycGetPlaneName();
            }
            dataFlycGetPlaneName = instance;
        }
        return dataFlycGetPlaneName;
    }

    public String getName() {
        int i = 32;
        if (!isGetted()) {
            return "";
        }
        if (this._recData.length <= 32) {
            i = this._recData.length;
        }
        return getUTF8(0, i);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.GetPlaneName.value();
        start(pack, callBack);
    }
}
