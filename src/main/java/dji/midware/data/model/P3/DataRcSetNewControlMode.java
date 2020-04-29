package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcGetRcRole;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcSetNewControlMode extends DataBase implements DJIDataSyncListener {
    private static DataRcSetNewControlMode instance = null;
    private int mFunction = 0;
    private DataRcGetRcRole.RcRole mRcRole = DataRcGetRcRole.RcRole.OTHER;

    public static synchronized DataRcSetNewControlMode getInstance() {
        DataRcSetNewControlMode dataRcSetNewControlMode;
        synchronized (DataRcSetNewControlMode.class) {
            if (instance == null) {
                instance = new DataRcSetNewControlMode();
            }
            dataRcSetNewControlMode = instance;
        }
        return dataRcSetNewControlMode;
    }

    public DataRcSetNewControlMode setRcRole(DataRcGetRcRole.RcRole _role) {
        this.mRcRole = _role;
        return this;
    }

    public DataRcSetNewControlMode setRcFunction(int _function) {
        this.mFunction = _function;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.mRcRole.value();
        this._sendData[1] = (byte) this.mFunction;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetNewControlFunction.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
