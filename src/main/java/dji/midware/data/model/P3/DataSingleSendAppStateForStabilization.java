package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataSingleSendAppStateForStabilization extends DataBase implements DJIDataSyncListener {
    private CameraState cameraState = CameraState.NORMAL;
    private GimbalState gimbalState = GimbalState.NORMAL;

    public DataSingleSendAppStateForStabilization setGimbalState(GimbalState state) {
        this.cameraState = CameraState.NORMAL;
        this.gimbalState = state;
        return this;
    }

    public DataSingleSendAppStateForStabilization setCameraState(CameraState state) {
        this.gimbalState = GimbalState.NORMAL;
        this.cameraState = state;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.gimbalState.value();
        this._sendData[1] = (byte) this.cameraState.value();
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.AppState.value();
        start(pack);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.Stabilization.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }

    @Keep
    public enum GimbalState {
        NORMAL(0),
        START(1),
        OTHER(8);
        
        private int data;

        private GimbalState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GimbalState find(int b) {
            GimbalState result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum CameraState {
        NORMAL(0),
        ZOOM_IN(1),
        ZOOM_OUT(2),
        CHANGING_FOCUS(3),
        OTHER(8);
        
        private int data;

        private CameraState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CameraState find(int b) {
            CameraState result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
