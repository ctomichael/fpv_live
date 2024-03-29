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
public class DataCameraSetFocusAid extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetFocusAid instance = null;
    private boolean mDigitalFocusA = true;
    private boolean mDigitalFocusM = true;

    public static synchronized DataCameraSetFocusAid getInstance() {
        DataCameraSetFocusAid dataCameraSetFocusAid;
        synchronized (DataCameraSetFocusAid.class) {
            if (instance == null) {
                instance = new DataCameraSetFocusAid();
            }
            dataCameraSetFocusAid = instance;
        }
        return dataCameraSetFocusAid;
    }

    public DataCameraSetFocusAid setDigitalFocus(boolean AF, boolean MF) {
        this.mDigitalFocusA = AF;
        this.mDigitalFocusM = MF;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        if (this.mDigitalFocusM) {
            byte[] bArr = this._sendData;
            bArr[0] = (byte) (bArr[0] | 1);
        }
        if (this.mDigitalFocusA) {
            byte[] bArr2 = this._sendData;
            bArr2[0] = (byte) (bArr2[0] | 2);
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
        pack.cmdId = CmdIdCamera.CmdIdType.SetFocusAid.value();
        start(pack, callBack);
    }
}
