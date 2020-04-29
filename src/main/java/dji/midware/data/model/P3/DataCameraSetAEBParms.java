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

@Keep
@EXClassNullAway
public class DataCameraSetAEBParms extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetAEBParms instance = null;
    private int mAEBNumber;
    private int mExposureValue = 1;

    public static synchronized DataCameraSetAEBParms getInstance() {
        DataCameraSetAEBParms dataCameraSetAEBParms;
        synchronized (DataCameraSetAEBParms.class) {
            if (instance == null) {
                instance = new DataCameraSetAEBParms();
            }
            dataCameraSetAEBParms = instance;
        }
        return dataCameraSetAEBParms;
    }

    public DataCameraSetAEBParms setExposureValue(int value) {
        this.mExposureValue = value;
        return this;
    }

    public DataCameraSetAEBParms setNumber(int number) {
        this.mAEBNumber = number;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.mExposureValue;
        this._sendData[1] = (byte) this.mAEBNumber;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetAEBParms.value();
        start(pack, callBack);
    }
}
