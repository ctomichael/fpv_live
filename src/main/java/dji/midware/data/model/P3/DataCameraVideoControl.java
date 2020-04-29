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
public class DataCameraVideoControl extends DataBase implements DJIDataSyncListener {
    private static DataCameraVideoControl instance = null;
    private int progress;
    private ControlType type;

    public static synchronized DataCameraVideoControl getInstance() {
        DataCameraVideoControl dataCameraVideoControl;
        synchronized (DataCameraVideoControl.class) {
            if (instance == null) {
                instance = new DataCameraVideoControl();
            }
            dataCameraVideoControl = instance;
        }
        return dataCameraVideoControl;
    }

    public DataCameraVideoControl setControlType(ControlType type2) {
        this.type = type2;
        return this;
    }

    public DataCameraVideoControl setProgress(int progress2) {
        this.progress = progress2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        this._sendData[0] = (byte) this.type.value();
        BytesUtil.arraycopy(BytesUtil.getBytes(this.progress), this._sendData, 1);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.VideoControl.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum ControlType {
        Stop(0),
        Start(1),
        FastForword(2),
        FastReverse(3),
        StepTo(4),
        Pause(5),
        OTHER(100);
        
        private int data;

        private ControlType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ControlType find(int b) {
            ControlType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
