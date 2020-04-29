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
public class DataCameraSetCameraRotationMode extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetCameraRotationMode instance = null;
    private int imageOrientationMod = -1;
    private RotationAngleType orientation;

    public static synchronized DataCameraSetCameraRotationMode getInstance() {
        DataCameraSetCameraRotationMode dataCameraSetCameraRotationMode;
        synchronized (DataCameraSetCameraRotationMode.class) {
            if (instance == null) {
                instance = new DataCameraSetCameraRotationMode();
            }
            dataCameraSetCameraRotationMode = instance;
        }
        return dataCameraSetCameraRotationMode;
    }

    public DataCameraSetCameraRotationMode setImageOrientationMode(int value) {
        this.imageOrientationMod = value;
        return this;
    }

    public DataCameraSetCameraRotationMode setOrientation(RotationAngleType value) {
        this.orientation = value;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetCameraRotationMode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.imageOrientationMod;
        this._sendData[1] = (byte) this.orientation.value();
    }

    @Keep
    public enum RotationAngleType {
        Rotate0(0),
        Rotate90(1),
        Rotate180(2),
        Rotate270(3),
        Unknown(255);
        
        private int data;

        private RotationAngleType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static RotationAngleType find(int b) {
            RotationAngleType result = Unknown;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
