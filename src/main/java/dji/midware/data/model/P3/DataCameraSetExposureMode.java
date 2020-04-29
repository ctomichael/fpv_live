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
public class DataCameraSetExposureMode extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetExposureMode instance = null;
    private int expMode;
    private int senceMode;

    public static synchronized DataCameraSetExposureMode getInstance() {
        DataCameraSetExposureMode dataCameraSetExposureMode;
        synchronized (DataCameraSetExposureMode.class) {
            if (instance == null) {
                instance = new DataCameraSetExposureMode();
            }
            dataCameraSetExposureMode = instance;
        }
        return dataCameraSetExposureMode;
    }

    public DataCameraSetExposureMode setExpMode(int value) {
        this.expMode = value;
        return this;
    }

    public DataCameraSetExposureMode setSenceMode(int value) {
        this.senceMode = value;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.expMode;
        if (this.expMode == 6) {
            this._sendData[1] = (byte) this.senceMode;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetExposureMode.value();
        start(pack, callBack);
    }

    @Keep
    public enum ExposureMode {
        AUTO(0),
        P(1),
        S(2),
        A(3),
        M(4),
        B(5),
        SCN(6),
        C(7),
        OTHER(100);
        
        private int data;

        private ExposureMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ExposureMode find(int b) {
            ExposureMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
