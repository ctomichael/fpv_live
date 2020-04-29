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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraSetZoomParams extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetZoomParams instance = null;
    private int mDigitalScaleValue = 1;
    private byte mZoomControlByte = 9;

    public static synchronized DataCameraSetZoomParams getInstance() {
        DataCameraSetZoomParams dataCameraSetZoomParams;
        synchronized (DataCameraSetZoomParams.class) {
            if (instance == null) {
                instance = new DataCameraSetZoomParams();
            }
            dataCameraSetZoomParams = instance;
        }
        return dataCameraSetZoomParams;
    }

    public DataCameraSetZoomParams setDigitalZoomType(int type) {
        this.mZoomControlByte = (byte) (this.mZoomControlByte | (type & 1));
        return this;
    }

    public DataCameraSetZoomParams setDigitalZoomControl(int isControl) {
        this.mZoomControlByte = (byte) (this.mZoomControlByte | ((isControl & 1) << 3));
        return this;
    }

    public DataCameraSetZoomParams setDigitalZoomValue(int scale) {
        this.mDigitalScaleValue = scale;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        this._sendData[0] = this.mZoomControlByte;
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mDigitalScaleValue), 0, this._sendData, 3, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetZoomParams.value();
        pack.receiverId = 0;
        start(pack, callBack);
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetZoomParams.value();
        pack.receiverId = 0;
        super.start(pack);
    }
}
