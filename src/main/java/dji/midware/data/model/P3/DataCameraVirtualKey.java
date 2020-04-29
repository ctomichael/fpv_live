package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;
import dji.midware.interfaces.DJIDataCallBack;

@Keep
@EXClassNullAway
public class DataCameraVirtualKey extends DataBase implements DJIDataAsync2Listener {
    private static DataCameraVirtualKey instance = null;
    private KEY key;

    public static synchronized DataCameraVirtualKey getInstance() {
        DataCameraVirtualKey dataCameraVirtualKey;
        synchronized (DataCameraVirtualKey.class) {
            if (instance == null) {
                instance = new DataCameraVirtualKey();
            }
            dataCameraVirtualKey = instance;
        }
        return dataCameraVirtualKey;
    }

    public DataCameraVirtualKey setKey(KEY key2) {
        this.key = key2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.key.value();
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.VirtualKey.value();
        pack.data = getSendData();
        start(pack);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.VirtualKey.value();
        pack.data = getSendData();
        pack.timeOut = 500;
        start(pack, callBack);
    }

    @Keep
    public enum KEY {
        S1(1),
        S2(2),
        REC(3),
        DEL(4),
        MODE(5),
        UP(6),
        DOWN(7),
        LEFT(8),
        RIGHT(9),
        OK(10),
        BACK(11),
        ZOOMIN(12),
        ZOOMOUT(13),
        EnterMultiDisplay(14),
        PagePrev(15),
        PageNext(16),
        Cancel(17),
        Download(18),
        OTHER(100);
        
        private int data;

        private KEY(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static KEY find(int b) {
            KEY result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
