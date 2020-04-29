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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraSetShutterSpeed extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetShutterSpeed instance = null;
    private int decimal;
    private int integral;
    private int isReciprocal;
    private TYPE type;

    public static synchronized DataCameraSetShutterSpeed getInstance() {
        DataCameraSetShutterSpeed dataCameraSetShutterSpeed;
        synchronized (DataCameraSetShutterSpeed.class) {
            if (instance == null) {
                instance = new DataCameraSetShutterSpeed();
            }
            dataCameraSetShutterSpeed = instance;
        }
        return dataCameraSetShutterSpeed;
    }

    public DataCameraSetShutterSpeed setAuto() {
        this.type = TYPE.AUTO;
        return this;
    }

    public DataCameraSetShutterSpeed setAbsolute(boolean isReciprocal2, int integral2, int decimal2) {
        this.type = TYPE.Manual;
        this.isReciprocal = isReciprocal2 ? 1 : 0;
        this.integral = integral2;
        this.decimal = decimal2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.type.value();
        switch (this.type) {
            case AUTO:
                this.integral = 0;
                this.decimal = 0;
                break;
            case Manual:
                this.integral = (this.isReciprocal << 15) | this.integral;
                break;
        }
        System.arraycopy(BytesUtil.getUnsignedBytes(this.integral), 0, this._sendData, 1, 2);
        this._sendData[3] = (byte) this.decimal;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetShutterSpeed.value();
        start(pack, callBack);
    }

    @Keep
    public enum TYPE {
        AUTO(0),
        Manual(1),
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
