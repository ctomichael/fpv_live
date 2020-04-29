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
public class DataCameraSetFocusDistance extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetFocusDistance instance = null;
    private int mDistance = 0;
    private int mRepeatTimes = 2;

    public static synchronized DataCameraSetFocusDistance getInstance() {
        DataCameraSetFocusDistance dataCameraSetFocusDistance;
        synchronized (DataCameraSetFocusDistance.class) {
            if (instance == null) {
                instance = new DataCameraSetFocusDistance();
            }
            dataCameraSetFocusDistance = instance;
        }
        return dataCameraSetFocusDistance;
    }

    public DataCameraSetFocusDistance setStroke(int distance) {
        this.mDistance = distance;
        return this;
    }

    public DataCameraSetFocusDistance setRepeatTimes(int times) {
        this.mRepeatTimes = times;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mDistance), 0, this._sendData, 0, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetFocusDistance.value();
        pack.repeatTimes = this.mRepeatTimes;
        start(pack, callBack);
    }
}
