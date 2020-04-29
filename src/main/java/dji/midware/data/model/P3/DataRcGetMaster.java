package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcSetMaster;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcGetMaster extends DataBase implements DJIDataSyncListener {
    private static DataRcGetMaster instance = null;

    public static synchronized DataRcGetMaster getInstance() {
        DataRcGetMaster dataRcGetMaster;
        synchronized (DataRcGetMaster.class) {
            if (instance == null) {
                instance = new DataRcGetMaster();
            }
            dataRcGetMaster = instance;
        }
        return dataRcGetMaster;
    }

    public DataRcSetMaster.MODE getMode() {
        return DataRcSetMaster.MODE.find((((Integer) get(0, 1, Integer.class)).intValue() >> 6) & 3);
    }

    public boolean isConnected() {
        return ((((Integer) get(0, 1, Integer.class)).intValue() >> 5) & 1) == 1;
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
        pack.cmdId = CmdIdRc.CmdIdType.GetMaster.value();
        if (DJIProductManager.getInstance().getType() == ProductType.litchiC) {
            this._recData = new byte[]{32};
            callBack.onSuccess(this);
            return;
        }
        start(pack, callBack);
    }
}
