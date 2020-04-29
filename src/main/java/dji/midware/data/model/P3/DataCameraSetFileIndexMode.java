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
public class DataCameraSetFileIndexMode extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetFileIndexMode instance = null;
    private FileIndexMode mMode = FileIndexMode.RESET;
    private DataCameraSetStorageInfo.Storage mStorage = DataCameraSetStorageInfo.Storage.SDCARD;

    public static synchronized DataCameraSetFileIndexMode getInstance() {
        DataCameraSetFileIndexMode dataCameraSetFileIndexMode;
        synchronized (DataCameraSetFileIndexMode.class) {
            if (instance == null) {
                instance = new DataCameraSetFileIndexMode();
            }
            dataCameraSetFileIndexMode = instance;
        }
        return dataCameraSetFileIndexMode;
    }

    public void setFileIndexMode(FileIndexMode mode) {
        this.mMode = mode;
    }

    public void setStorageLocation(DataCameraSetStorageInfo.Storage storage) {
        this.mStorage = storage;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) (this.mMode != null ? this.mMode.value() : FileIndexMode.RESET.value());
        byte[] bArr = this._sendData;
        bArr[3] = (byte) (bArr[3] | (this.mStorage.value() << 6));
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetFileIndexMode.value();
        start(pack, callBack);
    }

    @Keep
    public enum FileIndexMode {
        RESET(0),
        CONTINUOUS(1);
        
        private int data;

        private FileIndexMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FileIndexMode find(int b) {
            FileIndexMode result = RESET;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
