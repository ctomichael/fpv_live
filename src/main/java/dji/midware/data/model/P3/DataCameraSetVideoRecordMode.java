package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdCamera;
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
public class DataCameraSetVideoRecordMode extends DataBase implements DJIDataSyncListener {
    private int mDuration = 0;
    private int mGimbalIndex = 0;
    private int mInterval = 0;
    private int mMode = 0;
    int mPitchAngle = 0;
    int mRollAngle = 0;
    private int mTimelapseControlMode = 0;
    private int mTimelapseSaveType = 0;
    int mYawAngle = 0;

    public DataCameraSetVideoRecordMode setTimelapseControlMode(int mode) {
        this.mTimelapseControlMode = mode;
        return this;
    }

    public DataCameraSetVideoRecordMode setTimelapseSaveType(int type) {
        this.mTimelapseSaveType = type;
        return this;
    }

    public DataCameraSetVideoRecordMode setGimbalIndex(int index) {
        this.mGimbalIndex = index;
        return this;
    }

    public DataCameraSetVideoRecordMode setYawAngle(int angle) {
        this.mYawAngle = angle;
        return this;
    }

    public DataCameraSetVideoRecordMode setRollAngle(int angle) {
        this.mRollAngle = angle;
        return this;
    }

    public DataCameraSetVideoRecordMode setPitchAngle(int angle) {
        this.mPitchAngle = angle;
        return this;
    }

    public DataCameraSetVideoRecordMode setInterval(int interval) {
        this.mInterval = interval;
        return this;
    }

    public DataCameraSetVideoRecordMode setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public DataCameraSetVideoRecordMode setVideoRecordMode(int mode, int interval, int duration) {
        this.mMode = mode;
        this.mInterval = interval;
        this.mDuration = duration;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[15];
        this._sendData[0] = (byte) this.mMode;
        this._sendData[1] = (byte) (this.mTimelapseControlMode | (this.mGimbalIndex << 2));
        this._sendData[2] = (byte) this.mTimelapseSaveType;
        this._sendData[3] = (byte) (this.mInterval & 255);
        this._sendData[4] = (byte) ((this.mInterval & 65280) >> 8);
        byte[] b = BytesUtil.getBytes(this.mDuration);
        System.arraycopy(b, 0, this._sendData, 5, 2);
        System.arraycopy(b, 2, this._sendData, 7, 2);
        this._sendData[9] = (byte) (this.mYawAngle & 255);
        this._sendData[10] = (byte) ((this.mYawAngle & 65280) >> 8);
        this._sendData[11] = (byte) (this.mRollAngle & 255);
        this._sendData[12] = (byte) ((this.mRollAngle & 65280) >> 8);
        this._sendData[13] = (byte) (this.mPitchAngle & 255);
        this._sendData[14] = (byte) ((this.mPitchAngle & 65280) >> 8);
    }

    public void start(DJIDataCallBack callBack) {
        start(callBack, 200, 3);
    }

    public void start(DJIDataCallBack callBack, int timeout, int times) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetVideoRecordMode.value();
        pack.data = getSendData();
        pack.timeOut = timeout;
        pack.repeatTimes = times;
        start(pack, callBack);
        DJILogHelper.getInstance().LOGD("", "DJIMethod : start (215)" + this.mGimbalIndex, false, true);
    }
}
