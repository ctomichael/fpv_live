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
public class DataCameraSetLockGimbalWhenShot extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetLockGimbalWhenShot instance = null;
    private boolean isLockGimbalWhenShot = false;

    public static synchronized DataCameraSetLockGimbalWhenShot getInstance() {
        DataCameraSetLockGimbalWhenShot dataCameraSetLockGimbalWhenShot;
        synchronized (DataCameraSetLockGimbalWhenShot.class) {
            if (instance == null) {
                instance = new DataCameraSetLockGimbalWhenShot();
            }
            dataCameraSetLockGimbalWhenShot = instance;
        }
        return dataCameraSetLockGimbalWhenShot;
    }

    public DataCameraSetLockGimbalWhenShot setLockGimbalWhenShot(boolean enable) {
        this.isLockGimbalWhenShot = enable;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetLockGimbalWhenShot.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        if (this.isLockGimbalWhenShot) {
            this._sendData[0] = 1;
        } else {
            this._sendData[0] = 0;
        }
    }
}
