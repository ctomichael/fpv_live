package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
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
public class DataCameraFormatSSD extends DataBase implements DJIDataSyncListener {
    private static DataCameraFormatSSD instance = null;
    private int mDeviceType = 0;
    private int mFileSystem = 0;
    private int mFormatSpeed = 1;

    public static synchronized DataCameraFormatSSD getInstance() {
        DataCameraFormatSSD dataCameraFormatSSD;
        synchronized (DataCameraFormatSSD.class) {
            if (instance == null) {
                instance = new DataCameraFormatSSD();
            }
            dataCameraFormatSSD = instance;
        }
        return dataCameraFormatSSD;
    }

    public DataCameraFormatSSD setFormatSpeed(int type) {
        this.mFormatSpeed = type;
        return this;
    }

    public DataCameraFormatSSD setFileSystem(int type) {
        this.mFileSystem = type;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mFormatSpeed;
        byte[] bArr = this._sendData;
        bArr[0] = (byte) (bArr[0] | ((byte) (this.mDeviceType << 1)));
        byte[] bArr2 = this._sendData;
        bArr2[0] = (byte) (bArr2[0] | ((byte) (this.mFileSystem << 3)));
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.FormatSSD.value();
        pack.timeOut = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
        start(pack, callBack);
    }
}
