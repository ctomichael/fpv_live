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

@Keep
@EXClassNullAway
public class DataCameraSetParamName extends DataBase implements DJIDataSyncListener {
    public static final int MAX_NAME_LENGTH = 63;
    private static DataCameraSetParamName instance = null;
    private DataCameraSaveParams.USER mParamIndex = DataCameraSaveParams.USER.USER1;
    private byte[] mParamName = null;

    public static synchronized DataCameraSetParamName getInstance() {
        DataCameraSetParamName dataCameraSetParamName;
        synchronized (DataCameraSetParamName.class) {
            if (instance == null) {
                instance = new DataCameraSetParamName(true);
            }
            dataCameraSetParamName = instance;
        }
        return dataCameraSetParamName;
    }

    public DataCameraSetParamName(boolean register) {
        super(register);
    }

    public DataCameraSetParamName setParamIndex(DataCameraSaveParams.USER index) {
        this.mParamIndex = index;
        return this;
    }

    public DataCameraSetParamName setName(byte[] name) {
        this.mParamName = name;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int length = 63;
        this._sendData = new byte[65];
        this._sendData[0] = (byte) this.mParamIndex.value();
        if (this.mParamName != null && this.mParamName.length > 0) {
            if (this.mParamName.length <= 63) {
                length = this.mParamName.length;
            }
            System.arraycopy(this.mParamName, 0, this._sendData, 1, length);
        }
        this._sendData[64] = 0;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetParamName.value();
        start(pack, callBack);
    }
}
