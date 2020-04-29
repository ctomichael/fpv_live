package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraRequestSendFiles extends DataBase implements DJIDataSyncListener {
    private static DataCameraRequestSendFiles instance = null;
    private FILE_SELECT_MODE mode = FILE_SELECT_MODE.CURRENT;

    public static synchronized DataCameraRequestSendFiles getInstance() {
        DataCameraRequestSendFiles dataCameraRequestSendFiles;
        synchronized (DataCameraRequestSendFiles.class) {
            if (instance == null) {
                instance = new DataCameraRequestSendFiles();
            }
            dataCameraRequestSendFiles = instance;
        }
        return dataCameraRequestSendFiles;
    }

    public DataCameraRequestSendFiles setMode(FILE_SELECT_MODE mode2) {
        this.mode = mode2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mode.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.RequestSendFiles.value();
        start(pack, callBack);
    }

    @Keep
    public enum FILE_SELECT_MODE {
        CURRENT(0),
        NEXT(1),
        OTHER(100);
        
        private int data;

        private FILE_SELECT_MODE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FILE_SELECT_MODE find(int b) {
            FILE_SELECT_MODE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum Error {
        FileNotFound(34),
        INVALID_CMD(224),
        OTHER(100);
        
        private int data;

        private Error(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static Error find(int b) {
            Error result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
