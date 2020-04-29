package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraSetVideoFormat extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetVideoFormat instance = null;
    private int fov;
    private int fps;
    private int ratio;
    private int secondOpen;
    private int secondRatio;

    public static synchronized DataCameraSetVideoFormat getInstance() {
        DataCameraSetVideoFormat dataCameraSetVideoFormat;
        synchronized (DataCameraSetVideoFormat.class) {
            if (instance == null) {
                instance = new DataCameraSetVideoFormat();
            }
            dataCameraSetVideoFormat = instance;
        }
        return dataCameraSetVideoFormat;
    }

    public DataCameraSetVideoFormat setAll() {
        setRatio(DataCameraGetPushShotParams.getInstance().getVideoFormat(this.receiverID));
        setFps(DataCameraGetPushShotParams.getInstance().getVideoFps(this.receiverID));
        setFov(DataCameraGetPushShotParams.getInstance().getVideoFov(this.receiverID));
        setSecondOpen(DataCameraGetPushShotParams.getInstance().getVideoSecondOpen(this.receiverID));
        setSecondRatio(DataCameraGetPushShotParams.getInstance().getVideoSecondRatio(this.receiverID));
        return this;
    }

    public DataCameraSetVideoFormat setRatio(int ratio2) {
        this.ratio = ratio2;
        return this;
    }

    public DataCameraSetVideoFormat setFps(int fps2) {
        this.fps = fps2;
        return this;
    }

    public DataCameraSetVideoFormat setFov(int fov2) {
        this.fov = fov2;
        return this;
    }

    public DataCameraSetVideoFormat setSecondOpen(int secondOpen2) {
        this.secondOpen = secondOpen2;
        return this;
    }

    public DataCameraSetVideoFormat setSecondRatio(int secondRatio2) {
        this.secondRatio = secondRatio2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        this._sendData[0] = (byte) this.ratio;
        this._sendData[1] = (byte) this.fps;
        this._sendData[2] = (byte) this.fov;
        this._sendData[3] = (byte) this.secondOpen;
        this._sendData[4] = (byte) this.secondRatio;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetVideoFormat.value();
        start(pack, callBack);
    }
}
