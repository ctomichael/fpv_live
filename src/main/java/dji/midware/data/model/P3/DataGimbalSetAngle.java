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
public class DataGimbalSetAngle extends DataBase implements DJIDataSyncListener {
    private static DataGimbalSetAngle instance = null;
    int mErrorValue = 10;
    int mPitchAngle;
    int mRef = 0;
    int mRollAngle;
    int mYawAngle;
    int timtout = 2;

    public static synchronized DataGimbalSetAngle getInstance() {
        DataGimbalSetAngle dataGimbalSetAngle;
        synchronized (DataGimbalSetAngle.class) {
            if (instance == null) {
                instance = new DataGimbalSetAngle();
            }
            dataGimbalSetAngle = instance;
        }
        return dataGimbalSetAngle;
    }

    public DataGimbalSetAngle setYawAngle(int angle) {
        this.mYawAngle = angle;
        return this;
    }

    public DataGimbalSetAngle setPitchAngle(int angle) {
        this.mPitchAngle = angle;
        return this;
    }

    public DataGimbalSetAngle setRollAngle(int angle) {
        this.mRollAngle = angle;
        return this;
    }

    public DataGimbalSetAngle setErrorValue(int value) {
        this.mErrorValue = value;
        return this;
    }

    public DataGimbalSetAngle setTimeout(int timeout) {
        this.timtout = timeout;
        return this;
    }

    public DataGimbalSetAngle setAngleRef(int value) {
        this.mRef = value;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[10];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mYawAngle), 0, this._sendData, 0, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mRollAngle), 0, this._sendData, 2, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mPitchAngle), 0, this._sendData, 4, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mErrorValue), 0, this._sendData, 6, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes((this.mRef << 5) | 192), 0, this._sendData, 8, 1);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.timtout), 0, this._sendData, 9, 1);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.SetAngle.value();
        start(pack, callBack);
    }

    public void start(int timeout, int repeat, DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.SetAngle.value();
        pack.timeOut = timeout;
        pack.repeatTimes = repeat;
        start(pack, callBack);
    }
}
