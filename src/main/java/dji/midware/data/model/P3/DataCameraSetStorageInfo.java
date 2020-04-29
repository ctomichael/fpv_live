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
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DataCameraSetStorageInfo extends DataBase implements DJIDataSyncListener {
    private static final int DEFAULT_STORAGE_LENGTH = 2;
    private static DataCameraSetStorageInfo instance = null;
    private boolean mIsGet = false;
    private Storage mStorage = Storage.SDCARD;

    @Keep
    public static class StorageDesc {
        public byte mLength;
        public StorageRelationship mRelationship = StorageRelationship.EXTEND;
        public List<Storage> mStorages = null;
    }

    public static synchronized DataCameraSetStorageInfo getInstance() {
        DataCameraSetStorageInfo dataCameraSetStorageInfo;
        synchronized (DataCameraSetStorageInfo.class) {
            if (instance == null) {
                instance = new DataCameraSetStorageInfo();
            }
            dataCameraSetStorageInfo = instance;
        }
        return dataCameraSetStorageInfo;
    }

    public void setStorageGet(boolean isGet) {
        this.mIsGet = isGet;
    }

    public void setStorageLocation(Storage storage) {
        this.mStorage = storage;
    }

    public Storage getStorageLocation() {
        if (this._recData == null || this._recData.length < 6) {
            return Storage.SDCARD;
        }
        return Storage.find(((Integer) get(5, 1, Integer.class)).intValue() & 3);
    }

    public int getStorageLength() {
        if (this._recData == null || this._recData.length < 4) {
            return 0;
        }
        return this._recData[3];
    }

    private StorageDesc getStorageDesc() {
        if (this._recData == null || this._recData.length < 4) {
            return null;
        }
        StorageDesc storageDesc = new StorageDesc();
        storageDesc.mLength = this._recData[3];
        storageDesc.mRelationship = StorageRelationship.find(((Integer) get(4, 1, Integer.class)).intValue() & 1);
        if (storageDesc.mStorages != null) {
            storageDesc.mStorages.clear();
        }
        storageDesc.mStorages = new ArrayList();
        for (int i = 0; i < this._recData.length - 5; i++) {
            storageDesc.mStorages.add(Storage.find(((Integer) get(i + 5, 1, Integer.class)).intValue() & 3));
        }
        return storageDesc;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.OptStorageCfg.value();
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i;
        int i2 = 2;
        if (this.mIsGet) {
            this._sendData = new byte[1];
            this._sendData[0] = 0;
            return;
        }
        StorageDesc storageDesc = getStorageDesc();
        int length = 0;
        if (!(storageDesc == null || storageDesc.mStorages == null)) {
            length = storageDesc.mStorages.size();
        }
        if (length == 0) {
            i = 2;
        } else {
            i = length;
        }
        this._sendData = new byte[(i + 6)];
        this._sendData[0] = 1;
        this._sendData[1] = 1;
        this._sendData[2] = 0;
        this._sendData[3] = 0;
        byte[] bArr = this._sendData;
        if (length != 0) {
            i2 = length;
        }
        bArr[4] = (byte) i2;
        this._sendData[5] = (byte) (storageDesc == null ? 0 : storageDesc.mRelationship.value());
        if (length != 0) {
            int n = 0;
            for (int i3 = 0; i3 < length; i3++) {
                Storage storage = storageDesc.mStorages.get(i3);
                if (storage.value() == this.mStorage.value()) {
                    this._sendData[6] = (byte) storage.value();
                } else if (this._sendData.length - 7 > n) {
                    n++;
                    this._sendData[n + 6] = (byte) storage.value();
                }
            }
            return;
        }
        this._sendData[6] = (byte) this.mStorage.value();
        if (this.mStorage == Storage.INNER_STORAGE) {
            this._sendData[7] = (byte) Storage.SDCARD.value();
        } else {
            this._sendData[7] = (byte) Storage.INNER_STORAGE.value();
        }
    }

    @Keep
    public enum StorageRelationship {
        EXTEND(0),
        BACKUP(1),
        OTHER(255);
        
        private int data;

        private StorageRelationship(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static StorageRelationship find(int b) {
            StorageRelationship result = EXTEND;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum Storage {
        SDCARD(0),
        INNER_STORAGE(1),
        OUTER_STORAGE(2),
        OTHER(255);
        
        private int data;

        private Storage(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static Storage find(int b) {
            Storage result = SDCARD;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
