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
public class DataCameraSetMCTF extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetMCTF instance = null;
    private int mStrength = 50;
    private boolean mbEnable = true;

    public static synchronized DataCameraSetMCTF getInstance() {
        DataCameraSetMCTF dataCameraSetMCTF;
        synchronized (DataCameraSetMCTF.class) {
            if (instance == null) {
                instance = new DataCameraSetMCTF();
            }
            dataCameraSetMCTF = instance;
        }
        return dataCameraSetMCTF;
    }

    public DataCameraSetMCTF setEnable(boolean enable) {
        this.mbEnable = enable;
        return this;
    }

    public DataCameraSetMCTF setStrength(int strength) {
        this.mStrength = strength;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        byte b;
        this._sendData = new byte[2];
        byte[] bArr = this._sendData;
        if (this.mbEnable) {
            b = 1;
        } else {
            b = 0;
        }
        bArr[0] = b;
        this._sendData[1] = (byte) this.mStrength;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetMCTF.value();
        start(pack, callBack);
    }
}
