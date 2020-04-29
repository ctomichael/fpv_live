package dji.midware.data.model.P3;

import android.support.annotation.Keep;
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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraGetParamName extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetParamName instance = null;
    private DataCameraSaveParams.USER mParamIndex = DataCameraSaveParams.USER.USER1;

    public static synchronized DataCameraGetParamName getInstance() {
        DataCameraGetParamName dataCameraGetParamName;
        synchronized (DataCameraGetParamName.class) {
            if (instance == null) {
                instance = new DataCameraGetParamName(true);
            }
            dataCameraGetParamName = instance;
        }
        return dataCameraGetParamName;
    }

    public DataCameraGetParamName(boolean register) {
        super(register);
    }

    public String getName() {
        if (this._recData == null || this._recData.length <= 0) {
            return null;
        }
        return BytesUtil.getStringUTF8Offset(this._recData, 0, this._recData.length);
    }

    public DataCameraGetParamName setParamIndex(DataCameraSaveParams.USER index) {
        this.mParamIndex = index;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mParamIndex.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetParamName.value();
        start(pack, callBack);
    }
}
