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
public class DataCameraFormatRaw extends DataBase implements DJIDataSyncListener {
    private static DataCameraFormatRaw instance = null;
    private int mDevice = 0;
    private boolean mbFast = false;

    public static synchronized DataCameraFormatRaw getInstance() {
        DataCameraFormatRaw dataCameraFormatRaw;
        synchronized (DataCameraFormatRaw.class) {
            if (instance == null) {
                instance = new DataCameraFormatRaw();
            }
            dataCameraFormatRaw = instance;
        }
        return dataCameraFormatRaw;
    }

    public DataCameraFormatRaw setFast(boolean fast) {
        this.mbFast = fast;
        return this;
    }

    public DataCameraFormatRaw setDevice(int device) {
        this.mDevice = device;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        byte b = 1;
        this._sendData = new byte[1];
        byte[] bArr = this._sendData;
        byte b2 = bArr[0];
        if (!this.mbFast) {
            b = 0;
        }
        bArr[0] = (byte) (b | b2);
        byte[] bArr2 = this._sendData;
        bArr2[0] = (byte) (bArr2[0] | ((byte) (this.mDevice << 1)));
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.FormatRawSSD.value();
        pack.timeOut = 5000;
        start(pack, callBack);
    }
}
