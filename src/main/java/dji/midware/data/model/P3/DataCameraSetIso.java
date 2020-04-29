package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetIso;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraSetIso extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetIso instance = null;
    private int absValue;
    private int relValue;
    private int type;

    public static synchronized DataCameraSetIso getInstance() {
        DataCameraSetIso dataCameraSetIso;
        synchronized (DataCameraSetIso.class) {
            if (instance == null) {
                instance = new DataCameraSetIso();
            }
            dataCameraSetIso = instance;
        }
        return dataCameraSetIso;
    }

    public DataCameraSetIso setType(boolean isAbs) {
        this.type = isAbs ? 0 : 1;
        return this;
    }

    public DataCameraSetIso setAbsValue(DataCameraGetIso.TYPE type2) {
        this.absValue = type2.value();
        return this;
    }

    public DataCameraSetIso setRelValue(boolean isPlus) {
        this.relValue = isPlus ? 1 : 0;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        if (this.type == 0) {
            this._sendData[0] = (byte) ((this.type << 7) | this.absValue);
        } else {
            this._sendData[0] = (byte) ((this.type << 7) | this.relValue);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetIso.value();
        start(pack, callBack);
    }
}
