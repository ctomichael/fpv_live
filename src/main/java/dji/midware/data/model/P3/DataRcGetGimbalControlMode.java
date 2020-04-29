package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcSetGimbalControlMode;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcGetGimbalControlMode extends DataBase implements DJIDataSyncListener {
    private static DataRcGetGimbalControlMode instance = null;
    private DataRcSetGimbalControlMode.MODE mode;

    public static synchronized DataRcGetGimbalControlMode getInstance() {
        DataRcGetGimbalControlMode dataRcGetGimbalControlMode;
        synchronized (DataRcGetGimbalControlMode.class) {
            if (instance == null) {
                instance = new DataRcGetGimbalControlMode();
            }
            dataRcGetGimbalControlMode = instance;
        }
        return dataRcGetGimbalControlMode;
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        this.mode = DataRcSetGimbalControlMode.MODE.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public void setMode(DataRcSetGimbalControlMode.MODE mode2) {
        this.mode = mode2;
    }

    public DataRcSetGimbalControlMode.MODE getMode() {
        return this.mode;
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
        pack.cmdId = CmdIdRc.CmdIdType.GetGimbalControlMode.value();
        start(pack, callBack);
    }
}
