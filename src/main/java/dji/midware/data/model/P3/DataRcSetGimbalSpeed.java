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
public class DataRcSetGimbalSpeed extends DataBase implements DJIDataSyncListener {
    private static DataRcSetGimbalSpeed instance = null;
    private int pitch;
    private int roll;
    private int yaw;

    public static synchronized DataRcSetGimbalSpeed getInstance() {
        DataRcSetGimbalSpeed dataRcSetGimbalSpeed;
        synchronized (DataRcSetGimbalSpeed.class) {
            if (instance == null) {
                instance = new DataRcSetGimbalSpeed();
            }
            dataRcSetGimbalSpeed = instance;
        }
        return dataRcSetGimbalSpeed;
    }

    public DataRcSetGimbalSpeed setPitch(int pitch2) {
        this.pitch = pitch2;
        return this;
    }

    public DataRcSetGimbalSpeed setRoll(int roll2) {
        this.roll = roll2;
        return this;
    }

    public DataRcSetGimbalSpeed setYaw(int yaw2) {
        this.yaw = yaw2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.pitch;
        this._sendData[1] = (byte) this.roll;
        this._sendData[2] = (byte) this.yaw;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetGimbalSpeed.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
