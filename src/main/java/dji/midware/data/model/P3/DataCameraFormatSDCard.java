package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraFormatSDCard extends DataBase implements DJIDataSyncListener {
    private static DataCameraFormatSDCard instance = null;
    private DataCameraSetStorageInfo.Storage mStorage = DataCameraSetStorageInfo.Storage.SDCARD;

    public static synchronized DataCameraFormatSDCard getInstance() {
        DataCameraFormatSDCard dataCameraFormatSDCard;
        synchronized (DataCameraFormatSDCard.class) {
            if (instance == null) {
                instance = new DataCameraFormatSDCard();
            }
            dataCameraFormatSDCard = instance;
        }
        return dataCameraFormatSDCard;
    }

    public void setStorageLocation(DataCameraSetStorageInfo.Storage storage) {
        this.mStorage = storage;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) (this.mStorage != null ? this.mStorage.value() : DataCameraSetStorageInfo.Storage.SDCARD.value());
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.FormatSDCard.value();
        pack.timeOut = 40000;
        start(pack, callBack);
    }
}
