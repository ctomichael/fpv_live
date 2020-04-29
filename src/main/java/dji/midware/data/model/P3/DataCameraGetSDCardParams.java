package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetStateInfo;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
class DataCameraGetSDCardParams extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetSDCardParams instance = null;
    private DataCameraSetStorageInfo.Storage mStorage = DataCameraSetStorageInfo.Storage.SDCARD;

    DataCameraGetSDCardParams() {
    }

    public static synchronized DataCameraGetSDCardParams getInstance() {
        DataCameraGetSDCardParams dataCameraGetSDCardParams;
        synchronized (DataCameraGetSDCardParams.class) {
            if (instance == null) {
                instance = new DataCameraGetSDCardParams();
            }
            dataCameraGetSDCardParams = instance;
        }
        return dataCameraGetSDCardParams;
    }

    public boolean getSDCardInsertState() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1;
    }

    public DataCameraGetStateInfo.SDCardState getSDCardState() {
        return DataCameraGetStateInfo.SDCardState.find(((Integer) get(0, 1, Integer.class)).intValue() & 30);
    }

    public int getSDCardTotalSize() {
        return ((Integer) get(1, 4, Integer.class)).intValue();
    }

    public int getSDCardFreeSize() {
        return ((Integer) get(5, 4, Integer.class)).intValue();
    }

    public int getRemainedShots() {
        return ((Integer) get(9, 4, Integer.class)).intValue();
    }

    public int getRemainedTime() {
        return ((Integer) get(13, 4, Integer.class)).intValue();
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetSDCardParams.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
