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
public class DataCameraSetQuickPlayBack extends DataBase implements DJIDataSyncListener {
    private static final byte FLAG_ENABLE = Byte.MIN_VALUE;
    private static DataCameraSetQuickPlayBack instance = null;
    private boolean enbale = false;
    private byte mTime = 0;

    public static synchronized DataCameraSetQuickPlayBack getInstance() {
        DataCameraSetQuickPlayBack dataCameraSetQuickPlayBack;
        synchronized (DataCameraSetQuickPlayBack.class) {
            if (instance == null) {
                instance = new DataCameraSetQuickPlayBack();
            }
            dataCameraSetQuickPlayBack = instance;
        }
        return dataCameraSetQuickPlayBack;
    }

    public void setTime(byte time) {
        this.mTime = time;
    }

    public void setEnable(boolean flag) {
        this.enbale = flag;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = 0;
        if (this.mTime != 0 && this.enbale) {
            byte[] bArr = this._sendData;
            bArr[0] = (byte) (bArr[0] | Byte.MIN_VALUE);
            byte[] bArr2 = this._sendData;
            bArr2[0] = (byte) (bArr2[0] | this.mTime);
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
        pack.cmdId = CmdIdCamera.CmdIdType.SetQuickPlayBack.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
