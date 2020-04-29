package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.SparseIntArray;
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
public class DataRcGetHardwareParams extends DataBase implements DJIDataSyncListener {
    private static DataRcGetHardwareParams instance = null;

    public static synchronized DataRcGetHardwareParams getInstance() {
        DataRcGetHardwareParams dataRcGetHardwareParams;
        synchronized (DataRcGetHardwareParams.class) {
            if (instance == null) {
                instance = new DataRcGetHardwareParams();
            }
            dataRcGetHardwareParams = instance;
        }
        return dataRcGetHardwareParams;
    }

    public SparseIntArray getList() {
        SparseIntArray result = new SparseIntArray();
        if (this._recData != null) {
            int num = this._recData.length / 2;
            for (int i = 0; i < num; i++) {
                result.put(i, ((Integer) get(i * 2, 2, Integer.class)).intValue());
            }
        }
        return result;
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
        pack.cmdId = CmdIdRc.CmdIdType.GetHardwareParams.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
