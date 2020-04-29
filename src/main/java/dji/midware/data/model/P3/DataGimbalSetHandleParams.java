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

@Keep
@EXClassNullAway
public class DataGimbalSetHandleParams extends DataBase implements DJIDataSyncListener {
    int mCellphoneSensorDisable = 255;
    int mJoystickDualChannelEnable = 255;
    int mPanDirection = 255;
    int mPitchFree = 255;
    int mProfile = 255;
    int mRotationFocusEnable = 255;
    int mTiltDirection = 255;
    int mZoom2Speed = 255;

    public DataGimbalSetHandleParams setDualChannelEnable(int value) {
        this.mJoystickDualChannelEnable = value;
        return this;
    }

    public DataGimbalSetHandleParams setTiltDirection(int value) {
        this.mTiltDirection = value;
        return this;
    }

    public DataGimbalSetHandleParams setPanDirection(int value) {
        this.mPanDirection = value;
        return this;
    }

    public DataGimbalSetHandleParams setProfile(int value) {
        this.mProfile = value;
        return this;
    }

    public DataGimbalSetHandleParams setPitchFree(int value) {
        this.mPitchFree = value;
        return this;
    }

    public DataGimbalSetHandleParams setZoom2Speed(int value) {
        this.mZoom2Speed = value;
        return this;
    }

    public DataGimbalSetHandleParams setRotationFocusEnable(int enable) {
        this.mRotationFocusEnable = enable;
        return this;
    }

    public DataGimbalSetHandleParams setCellphoneSensorDisable(int disable) {
        this.mCellphoneSensorDisable = disable;
        return this;
    }

    private void reset() {
        this.mJoystickDualChannelEnable = 255;
        this.mTiltDirection = 255;
        this.mPanDirection = 255;
        this.mProfile = 255;
        this.mPitchFree = 255;
        this.mZoom2Speed = 255;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[8];
        this._sendData[0] = (byte) this.mJoystickDualChannelEnable;
        this._sendData[1] = (byte) this.mTiltDirection;
        this._sendData[2] = (byte) this.mPanDirection;
        this._sendData[3] = (byte) this.mProfile;
        this._sendData[4] = (byte) this.mPitchFree;
        this._sendData[5] = (byte) this.mZoom2Speed;
        this._sendData[6] = (byte) this.mRotationFocusEnable;
        this._sendData[7] = (byte) this.mCellphoneSensorDisable;
        reset();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.SetHandleParams.value();
        start(pack, callBack);
    }
}
