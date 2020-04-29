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
public class DataCameraSetSSDVideoFormat extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetSSDVideoFormat instance = null;
    private int fps;
    private int ratio;

    public static synchronized DataCameraSetSSDVideoFormat getInstance() {
        DataCameraSetSSDVideoFormat dataCameraSetSSDVideoFormat;
        synchronized (DataCameraSetSSDVideoFormat.class) {
            if (instance == null) {
                instance = new DataCameraSetSSDVideoFormat();
            }
            dataCameraSetSSDVideoFormat = instance;
        }
        return dataCameraSetSSDVideoFormat;
    }

    public DataCameraSetSSDVideoFormat setRatio(int ratio2) {
        this.ratio = ratio2;
        return this;
    }

    public DataCameraSetSSDVideoFormat setFps(int fps2) {
        this.fps = fps2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.ratio;
        this._sendData[1] = (byte) this.fps;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetSSDVideoFormat.value();
        start(pack, callBack);
    }
}
