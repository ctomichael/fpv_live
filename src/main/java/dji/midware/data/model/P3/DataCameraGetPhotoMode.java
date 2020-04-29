package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraGetPhotoMode extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetPhotoMode instance = null;

    public static synchronized DataCameraGetPhotoMode getInstance() {
        DataCameraGetPhotoMode dataCameraGetPhotoMode;
        synchronized (DataCameraGetPhotoMode.class) {
            if (instance == null) {
                instance = new DataCameraGetPhotoMode();
            }
            dataCameraGetPhotoMode = instance;
        }
        return dataCameraGetPhotoMode;
    }

    public DataCameraSetPhoto.TYPE getType() {
        return DataCameraSetPhoto.TYPE.find(this._recData[0]);
    }

    public int getContinueNum() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getTimeType() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getTimeNum() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getTimeInterval() {
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetPhotoMode.value();
        start(pack, callBack);
    }
}
