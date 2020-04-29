package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlycStartHotPointMissionWithInfo;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycHotPointResetParams extends DataBase implements DJIDataSyncListener {
    private static DataFlycHotPointResetParams instance = null;
    private float angleSpeed = 0.0f;
    private DataFlycStartHotPointMissionWithInfo.ROTATION_DIR rotationDir = DataFlycStartHotPointMissionWithInfo.ROTATION_DIR.Anti_Clockwise;

    public static synchronized DataFlycHotPointResetParams getInstance() {
        DataFlycHotPointResetParams dataFlycHotPointResetParams;
        synchronized (DataFlycHotPointResetParams.class) {
            if (instance == null) {
                instance = new DataFlycHotPointResetParams();
            }
            dataFlycHotPointResetParams = instance;
        }
        return dataFlycHotPointResetParams;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataFlycHotPointResetParams setVelocity(float angleSpeed2) {
        this.angleSpeed = angleSpeed2;
        return this;
    }

    public DataFlycHotPointResetParams setRotationDir(DataFlycStartHotPointMissionWithInfo.ROTATION_DIR rotationDir2) {
        this.rotationDir = rotationDir2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.HotPointResetParams.value();
        super.start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        this._sendData[0] = (byte) this.rotationDir.value();
        System.arraycopy(BytesUtil.getBytes(this.angleSpeed), 0, this._sendData, 1, 4);
    }
}
