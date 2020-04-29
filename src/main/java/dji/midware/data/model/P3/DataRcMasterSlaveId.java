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
public class DataRcMasterSlaveId extends DataBase implements DJIDataSyncListener {
    private static DataRcMasterSlaveId instance = null;
    private boolean isGetMode = true;
    private String rcId;

    public static synchronized DataRcMasterSlaveId getInstance() {
        DataRcMasterSlaveId dataRcMasterSlaveId;
        synchronized (DataRcMasterSlaveId.class) {
            if (instance == null) {
                instance = new DataRcMasterSlaveId();
            }
            dataRcMasterSlaveId = instance;
        }
        return dataRcMasterSlaveId;
    }

    public void setGetMode(boolean enable) {
        this.isGetMode = enable;
    }

    public void setRcId(String id) {
        this.rcId = id;
    }

    public String getRcId() {
        return getUTF8(0, 6);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.isGetMode) {
            this._sendData = new byte[1];
            this._sendData[0] = 0;
            return;
        }
        this._sendData = new byte[7];
        this._sendData[0] = 1;
        System.arraycopy(this.rcId.getBytes(), 0, this._sendData, 1, 6);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.MaterSlaveId.value();
        start(pack, callBack);
    }
}
