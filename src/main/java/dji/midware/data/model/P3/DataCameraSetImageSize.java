package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetImageSize;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraSetImageSize extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetImageSize instance = null;
    private DataCameraGetImageSize.RatioType ratio;
    private DataCameraGetImageSize.SizeType size;

    public static synchronized DataCameraSetImageSize getInstance() {
        DataCameraSetImageSize dataCameraSetImageSize;
        synchronized (DataCameraSetImageSize.class) {
            if (instance == null) {
                instance = new DataCameraSetImageSize();
            }
            dataCameraSetImageSize = instance;
        }
        return dataCameraSetImageSize;
    }

    public DataCameraSetImageSize setAll() {
        setSize(DataCameraGetPushShotParams.getInstance().getImageSize(this.receiverID));
        setRatio(DataCameraGetPushShotParams.getInstance().getImageRatio(this.receiverID));
        return this;
    }

    public DataCameraSetImageSize setSize(DataCameraGetImageSize.SizeType size2) {
        this.size = size2;
        return this;
    }

    public DataCameraSetImageSize setRatio(DataCameraGetImageSize.RatioType ratio2) {
        this.ratio = ratio2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.size.value();
        this._sendData[1] = (byte) this.ratio.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetImageSize.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
