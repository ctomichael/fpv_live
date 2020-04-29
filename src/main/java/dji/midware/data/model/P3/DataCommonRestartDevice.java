package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCommonRestartDevice extends DataBase implements DJIDataSyncListener {
    private static DataCommonRestartDevice instance = null;
    private int mDelay = 0;
    private int mEncrypt = 0;
    private int mReceiveId = 0;
    private DeviceType mReceiveType = DeviceType.RC;
    private int mRestartType = 0;

    public static synchronized DataCommonRestartDevice getInstance() {
        DataCommonRestartDevice dataCommonRestartDevice;
        synchronized (DataCommonRestartDevice.class) {
            if (instance == null) {
                instance = new DataCommonRestartDevice();
            }
            dataCommonRestartDevice = instance;
        }
        return dataCommonRestartDevice;
    }

    public DataCommonRestartDevice setReceiveType(DeviceType type) {
        this.mReceiveType = type;
        return this;
    }

    public DataCommonRestartDevice setRestartType(int type) {
        this.mRestartType = type;
        return this;
    }

    public DataCommonRestartDevice setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    public DataCommonRestartDevice setDelay(int delay) {
        this.mDelay = delay;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[14];
        this._sendData[0] = (byte) this.mEncrypt;
        this._sendData[1] = (byte) this.mRestartType;
        System.arraycopy(BytesUtil.getBytes(this.mDelay), 0, this._sendData, 2, 4);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.RestartDevice.value();
        pack.timeOut = 3000;
        start(pack, callBack);
    }
}
