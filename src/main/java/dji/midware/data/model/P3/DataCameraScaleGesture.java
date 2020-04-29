package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraScaleGesture extends DataBase implements DJIDataAsync2Listener {
    private static DataCameraScaleGesture instance = null;
    private int scale;

    public static synchronized DataCameraScaleGesture getInstance() {
        DataCameraScaleGesture dataCameraScaleGesture;
        synchronized (DataCameraScaleGesture.class) {
            if (instance == null) {
                instance = new DataCameraScaleGesture();
            }
            dataCameraScaleGesture = instance;
        }
        return dataCameraScaleGesture;
    }

    public DataCameraScaleGesture setScale(int scale2) {
        this.scale = scale2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.scale), this._sendData);
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.ScaleGesture.value();
        pack.data = getSendData();
        start(pack);
    }
}
