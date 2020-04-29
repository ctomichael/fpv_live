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
public class DataCameraGetAudio extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetAudio instance = null;
    byte isEnable = 0;
    int src = 0;
    byte toFirstDataRate = 0;
    byte toSecondDataRate = 0;
    int type = 0;

    public static synchronized DataCameraGetAudio getInstance() {
        DataCameraGetAudio dataCameraGetAudio;
        synchronized (DataCameraGetAudio.class) {
            if (instance == null) {
                instance = new DataCameraGetAudio();
            }
            dataCameraGetAudio = instance;
        }
        return dataCameraGetAudio;
    }

    public boolean isEnable() {
        if (this._recData == null || (this._recData[0] & 1) == 0) {
            return false;
        }
        return true;
    }

    public boolean toSecondDataRate() {
        if (this._recData == null || (this._recData[0] & 1) == 0) {
            return false;
        }
        return true;
    }

    public boolean toFirstDataRate() {
        if (this._recData == null || (this._recData[0] & 1) == 0) {
            return false;
        }
        return true;
    }

    public int getType() {
        if (this._recData != null) {
            return this._recData[1] & 3;
        }
        return 0;
    }

    public int getSrc() {
        if (this._recData != null) {
            return this._recData[1] & 3;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetAudioParam.value();
        pack.data = getSendData();
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}
