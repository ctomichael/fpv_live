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
public class DataCameraGetIso extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetIso instance = null;

    public static synchronized DataCameraGetIso getInstance() {
        DataCameraGetIso dataCameraGetIso;
        synchronized (DataCameraGetIso.class) {
            if (instance == null) {
                instance = new DataCameraGetIso();
            }
            dataCameraGetIso = instance;
        }
        return dataCameraGetIso;
    }

    public TYPE getType() {
        return TYPE.find(((Short) get(0, 1, Short.class)).shortValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetIso.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum TYPE {
        AUTO(0),
        AUTOHIGH(1),
        ISO50(2),
        ISO100(3),
        ISO200(4),
        ISO400(5),
        ISO800(6),
        ISO1600(7),
        ISO3200(8),
        ISO6400(9),
        ISO12800(10),
        ISO25600(11),
        LOCK(255),
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
