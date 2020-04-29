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
public class DataCameraSetForeArmLed extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetForeArmLed instance = null;
    private byte mEnableMask = -1;

    public static synchronized DataCameraSetForeArmLed getInstance() {
        DataCameraSetForeArmLed dataCameraSetForeArmLed;
        synchronized (DataCameraSetForeArmLed.class) {
            if (instance == null) {
                instance = new DataCameraSetForeArmLed();
            }
            dataCameraSetForeArmLed = instance;
        }
        return dataCameraSetForeArmLed;
    }

    public DataCameraSetForeArmLed setEnable(boolean enable) {
        this.mEnableMask = (byte) (enable ? 1 : 0);
        return this;
    }

    public DataCameraSetForeArmLed setEnable(byte mask) {
        this.mEnableMask = mask;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[]{this.mEnableMask};
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetForeArmLed.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
