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
public class DataGimbalAngleControl extends DataBase implements DJIDataSyncListener {
    private static DataGimbalAngleControl instance = null;
    private boolean absolute;
    private short allowDeviation = 300;
    private boolean mIsPitchCtrlIsValid = true;
    private boolean mIsRollCtrlIsValid = true;
    private boolean mIsYawCtrlIsValid = true;
    private int overtime;
    private boolean permission;
    private short pitchAngle;
    private boolean reference;
    private short rollAngle;
    private short yawAngle;

    public static synchronized DataGimbalAngleControl getInstance() {
        DataGimbalAngleControl dataGimbalAngleControl;
        synchronized (DataGimbalAngleControl.class) {
            if (instance == null) {
                instance = new DataGimbalAngleControl();
            }
            dataGimbalAngleControl = instance;
        }
        return dataGimbalAngleControl;
    }

    public DataGimbalAngleControl setYaw(short yawAngle2) {
        this.yawAngle = yawAngle2;
        return this;
    }

    public DataGimbalAngleControl setRoll(short rollAngle2) {
        this.rollAngle = rollAngle2;
        return this;
    }

    public DataGimbalAngleControl setPitch(short pitchAngle2) {
        this.pitchAngle = pitchAngle2;
        return this;
    }

    public DataGimbalAngleControl setPermission(boolean permission2) {
        this.permission = permission2;
        return this;
    }

    public DataGimbalAngleControl setReference(boolean reference2) {
        this.reference = reference2;
        return this;
    }

    public DataGimbalAngleControl setOvertime(int overtime2) {
        this.overtime = overtime2;
        return this;
    }

    public DataGimbalAngleControl setAbsolute(boolean absolute2) {
        this.absolute = absolute2;
        return this;
    }

    public DataGimbalAngleControl setYawCtrlIsValid(boolean isValid) {
        this.mIsYawCtrlIsValid = isValid;
        return this;
    }

    public DataGimbalAngleControl setRollCtrlIsValid(boolean isValid) {
        this.mIsRollCtrlIsValid = isValid;
        return this;
    }

    public DataGimbalAngleControl setPitchCtrlIsValid(boolean isValid) {
        this.mIsPitchCtrlIsValid = isValid;
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
        pack.cmdId = CmdIdGimbal.CmdIdType.AngleControl.value();
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
        pack.cmdId = CmdIdGimbal.CmdIdType.AngleControl.value();
        super.start(pack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        byte b;
        byte b2;
        byte b3 = 1;
        this._sendData = new byte[10];
        System.arraycopy(BytesUtil.getBytes(this.yawAngle), 0, this._sendData, 0, 2);
        System.arraycopy(BytesUtil.getBytes(this.rollAngle), 0, this._sendData, 2, 2);
        System.arraycopy(BytesUtil.getBytes(this.pitchAngle), 0, this._sendData, 4, 2);
        System.arraycopy(BytesUtil.getBytes(this.allowDeviation), 0, this._sendData, 6, 2);
        int info = 0;
        if (this.permission && this.reference) {
            info = 3;
        }
        if (!this.permission && this.reference) {
            info = 1;
        }
        if (this.permission && !this.reference) {
            info = 2;
        }
        this._sendData[8] = (byte) (((this.absolute ? 1 : 0) + (info << 6)) << 5);
        byte[] bArr = this._sendData;
        byte b4 = this._sendData[8];
        if (this.mIsYawCtrlIsValid) {
            b = 1;
        } else {
            b = 0;
        }
        bArr[8] = (byte) ((b + b4) << 4);
        byte[] bArr2 = this._sendData;
        byte b5 = this._sendData[8];
        if (this.mIsRollCtrlIsValid) {
            b2 = 1;
        } else {
            b2 = 0;
        }
        bArr2[8] = (byte) ((b2 + b5) << 3);
        byte[] bArr3 = this._sendData;
        byte b6 = this._sendData[8];
        if (!this.mIsPitchCtrlIsValid) {
            b3 = 0;
        }
        bArr3[8] = (byte) ((b3 + b6) << 2);
        this._sendData[9] = (byte) this.overtime;
    }
}
