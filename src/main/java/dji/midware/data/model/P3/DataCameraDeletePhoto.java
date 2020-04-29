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
public class DataCameraDeletePhoto extends DataBase implements DJIDataSyncListener {
    private static DataCameraDeletePhoto instance = null;
    private int index;
    private TYPE type;

    public static synchronized DataCameraDeletePhoto getInstance() {
        DataCameraDeletePhoto dataCameraDeletePhoto;
        synchronized (DataCameraDeletePhoto.class) {
            if (instance == null) {
                instance = new DataCameraDeletePhoto();
            }
            dataCameraDeletePhoto = instance;
        }
        return dataCameraDeletePhoto;
    }

    public DataCameraDeletePhoto setType(TYPE type2) {
        this.type = type2;
        return this;
    }

    public DataCameraDeletePhoto setIndex(int index2) {
        this.index = index2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.type.value();
        this._sendData[1] = (byte) this.index;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.DeletePhoto.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum TYPE {
        SingleOk(0),
        MultipleOk(1),
        GoMultiple(2),
        OutMultiple(3),
        MultipleIndex(4),
        OTHER(100);
        
        private int data;

        private TYPE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TYPE find(int b) {
            TYPE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
