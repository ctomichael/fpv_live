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
public class DataCameraSetFocusArea extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetFocusArea instance = null;
    private float mFocusX = 0.5f;
    private float mFocusY = 0.5f;

    public static synchronized DataCameraSetFocusArea getInstance() {
        DataCameraSetFocusArea dataCameraSetFocusArea;
        synchronized (DataCameraSetFocusArea.class) {
            if (instance == null) {
                instance = new DataCameraSetFocusArea();
            }
            dataCameraSetFocusArea = instance;
        }
        return dataCameraSetFocusArea;
    }

    public DataCameraSetFocusArea setFocusAxisX(float x) {
        this.mFocusX = x;
        return this;
    }

    public DataCameraSetFocusArea setFocusAxisY(float y) {
        this.mFocusY = y;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[8];
        System.arraycopy(BytesUtil.getBytes(this.mFocusX), 0, this._sendData, 0, 4);
        System.arraycopy(BytesUtil.getBytes(this.mFocusY), 0, this._sendData, 4, 4);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetFocusArea.value();
        start(pack, callBack);
    }
}
