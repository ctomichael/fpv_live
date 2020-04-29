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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraSetPhotoMode extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetPhotoMode instance = null;
    private int continueNum;
    private int timeInterval;
    private int timeNum;
    private int timeType;
    private DataCameraSetPhoto.TYPE type;

    public static synchronized DataCameraSetPhotoMode getInstance() {
        DataCameraSetPhotoMode dataCameraSetPhotoMode;
        synchronized (DataCameraSetPhotoMode.class) {
            if (instance == null) {
                instance = new DataCameraSetPhotoMode();
            }
            dataCameraSetPhotoMode = instance;
        }
        return dataCameraSetPhotoMode;
    }

    public DataCameraSetPhotoMode setType(DataCameraSetPhoto.TYPE type2) {
        this.type = type2;
        return this;
    }

    public DataCameraSetPhotoMode setType(int type2) {
        this.type = DataCameraSetPhoto.TYPE.find(type2);
        return this;
    }

    public DataCameraSetPhotoMode setContinueNum(int continueNum2) {
        this.continueNum = continueNum2;
        return this;
    }

    public DataCameraSetPhotoMode setTimeType(int timeType2) {
        this.timeType = timeType2;
        return this;
    }

    public DataCameraSetPhotoMode setTimeNum(int timeNum2) {
        this.timeNum = timeNum2;
        return this;
    }

    public DataCameraSetPhotoMode setTimeInterval(int timeInterval2) {
        this.timeInterval = timeInterval2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[6];
        this._sendData[0] = (byte) this.type.value();
        this._sendData[1] = (byte) this.continueNum;
        this._sendData[2] = (byte) this.timeType;
        this._sendData[3] = (byte) this.timeNum;
        System.arraycopy(BytesUtil.getUnsignedBytes(this.timeInterval), 0, this._sendData, 4, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetPhotoMode.value();
        pack.repeatTimes = 4;
        start(pack, callBack);
    }
}
