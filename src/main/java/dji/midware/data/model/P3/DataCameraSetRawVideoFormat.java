package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetPushRawParams;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraSetRawVideoFormat extends DataBase implements DJIDataSyncListener {
    private int FOVType = -1;
    private int playSpeedType;
    private int playSpeedValue;
    private DataCameraGetPushRawParams.RawMode rawMode = DataCameraGetPushRawParams.RawMode.Unknow;
    private int rawVideoFrameRate = Integer.MAX_VALUE;
    private int rawVideoResolution = Integer.MAX_VALUE;
    private int secondVideoResolution;
    private int secondVideoSetting;
    private int videoFrameRate = -1;
    private int videoResolution = -1;

    public DataCameraSetRawVideoFormat setVideoResolution(int value) {
        this.videoResolution = value;
        return this;
    }

    public DataCameraSetRawVideoFormat setVideoFrameRate(int value) {
        this.videoFrameRate = value;
        return this;
    }

    public DataCameraSetRawVideoFormat setFOV(int value) {
        this.FOVType = value;
        return this;
    }

    public DataCameraSetRawVideoFormat setRawMode(int value) {
        this.rawMode = DataCameraGetPushRawParams.RawMode.find(value);
        return this;
    }

    public DataCameraSetRawVideoFormat setRawVideoResolution(int value) {
        this.rawVideoResolution = value;
        return this;
    }

    public DataCameraSetRawVideoFormat setRawVideoFrameRate(int value) {
        this.rawVideoFrameRate = value;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int videoFormat;
        int videoFps;
        int resolution;
        int fps;
        this._sendData = new byte[8];
        byte[] bArr = this._sendData;
        if (this.videoResolution != -1) {
            videoFormat = this.videoResolution;
        } else {
            videoFormat = DataCameraGetPushShotParams.getInstance().getVideoFormat(this.receiverID);
        }
        bArr[0] = (byte) videoFormat;
        byte[] bArr2 = this._sendData;
        if (this.videoFrameRate != -1) {
            videoFps = this.videoFrameRate;
        } else {
            videoFps = DataCameraGetPushShotParams.getInstance().getVideoFps(this.receiverID);
        }
        bArr2[1] = (byte) videoFps;
        this._sendData[2] = (byte) (this.FOVType != -1 ? this.FOVType : DataCameraGetPushShotParams.getInstance().getVideoFov(this.receiverID));
        this._sendData[3] = 0;
        this._sendData[4] = 0;
        this._sendData[5] = (byte) (this.rawMode != DataCameraGetPushRawParams.RawMode.Unknow ? this.rawMode.value() : DataCameraGetPushRawParams.getInstance().getRawMode(this.receiverID).value());
        byte[] bArr3 = this._sendData;
        if (this.rawVideoResolution != Integer.MAX_VALUE) {
            resolution = this.rawVideoResolution;
        } else {
            resolution = DataCameraGetPushRawParams.getInstance().getResolution(this.receiverID);
        }
        bArr3[6] = (byte) resolution;
        byte[] bArr4 = this._sendData;
        if (this.rawVideoFrameRate != Integer.MAX_VALUE) {
            fps = this.rawVideoFrameRate;
        } else {
            fps = DataCameraGetPushRawParams.getInstance().getFps(this.receiverID);
        }
        bArr4[7] = (byte) fps;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.timeOut = 6000;
        pack.cmdId = CmdIdCamera.CmdIdType.SetRawVideoFormat.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
