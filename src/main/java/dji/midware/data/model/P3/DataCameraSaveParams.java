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
public class DataCameraSaveParams extends DataBase implements DJIDataSyncListener {
    private static DataCameraSaveParams instance = null;
    private USER user = USER.DEFAULT;

    public static synchronized DataCameraSaveParams getInstance() {
        DataCameraSaveParams dataCameraSaveParams;
        synchronized (DataCameraSaveParams.class) {
            if (instance == null) {
                instance = new DataCameraSaveParams();
            }
            dataCameraSaveParams = instance;
        }
        return dataCameraSaveParams;
    }

    public DataCameraSaveParams setMode(USER user2) {
        this.user = user2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.user.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SaveParams.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum USER {
        DEFAULT(0),
        USER1(1),
        USER2(2),
        USER3(3),
        USER4(4),
        OTHER(6);
        
        private int data;

        private USER(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static USER find(int b) {
            USER result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
