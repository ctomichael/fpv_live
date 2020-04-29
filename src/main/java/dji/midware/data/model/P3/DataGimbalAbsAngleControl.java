package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdGimbal;
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
public class DataGimbalAbsAngleControl extends DataBase implements DJIDataSyncListener {
    private static DataGimbalAbsAngleControl instance = null;
    private boolean PitchInvalid;
    private boolean RollInvalid;
    private boolean YawInvalid;
    private boolean controlMode;
    private int overtime = 10;
    private short pitchAngle;
    private short rollAngle;
    private short yawAngle;

    public static synchronized DataGimbalAbsAngleControl getInstance() {
        DataGimbalAbsAngleControl dataGimbalAbsAngleControl;
        synchronized (DataGimbalAbsAngleControl.class) {
            if (instance == null) {
                instance = new DataGimbalAbsAngleControl();
            }
            dataGimbalAbsAngleControl = instance;
        }
        return dataGimbalAbsAngleControl;
    }

    public DataGimbalAbsAngleControl setYaw(short yawAngle2) {
        this.yawAngle = yawAngle2;
        return this;
    }

    public DataGimbalAbsAngleControl setRoll(short rollAngle2) {
        this.rollAngle = rollAngle2;
        return this;
    }

    public DataGimbalAbsAngleControl setPitch(short pitchAngle2) {
        this.pitchAngle = pitchAngle2;
        return this;
    }

    public DataGimbalAbsAngleControl setControlMode(boolean controlMode2) {
        this.controlMode = controlMode2;
        return this;
    }

    public DataGimbalAbsAngleControl setPitchInvalid(boolean PitchInvalid2) {
        this.PitchInvalid = PitchInvalid2;
        return this;
    }

    public DataGimbalAbsAngleControl setYawInvalid(boolean YawInvalid2) {
        this.YawInvalid = YawInvalid2;
        return this;
    }

    public DataGimbalAbsAngleControl setRollInvalid(boolean RollInvalid2) {
        this.RollInvalid = RollInvalid2;
        return this;
    }

    public DataGimbalAbsAngleControl setOvertime(int overtime2) {
        this.overtime = overtime2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.AbsAngleControl.value();
        super.start(pack, callBack);
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.AbsAngleControl.value();
        super.start(pack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[8];
        System.arraycopy(BytesUtil.getBytes(this.yawAngle), 0, this._sendData, 0, 2);
        System.arraycopy(BytesUtil.getBytes(this.rollAngle), 0, this._sendData, 2, 2);
        System.arraycopy(BytesUtil.getBytes(this.pitchAngle), 0, this._sendData, 4, 2);
        this._sendData[6] = 0;
        if (this.controlMode) {
            this._sendData[6] = (byte) (this._sendData[6] | 1);
        }
        if (this.YawInvalid) {
            this._sendData[6] = (byte) (this._sendData[6] | 2);
        }
        if (this.RollInvalid) {
            this._sendData[6] = (byte) (this._sendData[6] | 4);
        }
        if (this.PitchInvalid) {
            this._sendData[6] = (byte) (this._sendData[6] | 8);
        }
        this._sendData[7] = (byte) this.overtime;
    }
}
