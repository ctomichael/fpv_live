package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.google.android.gms.common.ConnectionResult;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraSaveParams;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraLoadParams extends DataBase implements DJIDataSyncListener {
    private static DataCameraLoadParams instance = null;
    private DataCameraSaveParams.USER user = DataCameraSaveParams.USER.DEFAULT;

    public static synchronized DataCameraLoadParams getInstance() {
        DataCameraLoadParams dataCameraLoadParams;
        synchronized (DataCameraLoadParams.class) {
            if (instance == null) {
                instance = new DataCameraLoadParams();
            }
            dataCameraLoadParams = instance;
        }
        return dataCameraLoadParams;
    }

    public DataCameraLoadParams setMode(DataCameraSaveParams.USER user2) {
        this.user = user2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.user.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES_BY_PUSH.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.LoadParams.value();
        pack.timeOut = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}
