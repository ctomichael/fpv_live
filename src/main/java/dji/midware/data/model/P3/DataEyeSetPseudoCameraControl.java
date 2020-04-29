package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataEyeGetPushPseudoCameraParams;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataEyeSetPseudoCameraControl extends DataBase implements DJIDataSyncListener {
    private int mTimeOut = 60000;
    private PseudoCameraCmd pseudoCameraCmd = PseudoCameraCmd.PSEUDO_CAMERA_CMD_RELEASE;

    public DataEyeSetPseudoCameraControl setPseudoCameraCmd(PseudoCameraCmd _pseudoCameraCmd) {
        this.pseudoCameraCmd = _pseudoCameraCmd;
        return this;
    }

    public DataEyeSetPseudoCameraControl setTimeOut(int timeOut) {
        this.mTimeOut = timeOut;
        return this;
    }

    public PseudoCameraCmdResult getPseudoCameraCmdResult() {
        return PseudoCameraCmdResult.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this._sendData == null) {
            this._sendData = new byte[1];
        }
        this._sendData[0] = this.pseudoCameraCmd.value();
        int index = 0 + 1;
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetPseudoCameraControlMode.value();
        pack.timeOut = this.mTimeOut;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    @Keep
    public enum PseudoCameraCmd {
        PSEUDO_CAMERA_CMD_ABORT(0),
        PSEUDO_CAMERA_CMD_CAPTURE(1),
        PSEUDO_CAMERA_CMD_RELEASE(DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_NONE.value()),
        PSEUDO_CAMERA_CMD_SET_MODE_PANO_1x3(DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_1x3.value()),
        PSEUDO_CAMERA_CMD_SET_MODE_BOKEH(DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_BOKEH.value()),
        PSEUDO_CAMERA_CMD_SET_MODE_GESTURE(DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_GESTURE.value()),
        PSEUDO_CAMERA_CMD_SET_MODE_PANO_3x1(DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_3x1.value()),
        PSEUDO_CAMERA_CMD_SET_MODE_PANO_3x3(DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_3x3.value()),
        PSEUDO_CAMERA_CMD_SET_MODE_PANO_180(DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_180.value()),
        PSEUDO_CAMERA_CMD_SET_MODE_PANO_SPHERE(DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_SPHERE.value()),
        PSEUDO_CAMERA_CMD_SET_MODE_PANO_SR(DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_SUPER_RESOLUTION.value()),
        OTHER(255);
        
        private int data;

        private PseudoCameraCmd(int _data) {
            this.data = _data;
        }

        public byte value() {
            return (byte) this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PseudoCameraCmd find(int b) {
            PseudoCameraCmd result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum PseudoCameraCmdResult {
        PSEUDO_CAMERA_ACK_SUCCESS(0),
        PSEUDO_CAMERA_ACK_FAIL(1),
        PSEUDO_CAMERA_ACK_FAIL_ABORT(2),
        PSEUDO_CAMERA_ACK_FAIL_NOT_SUPPORTED(3),
        PSEUDO_CAMERA_ACK_FAIL_BUSY(4),
        PSEUDO_CAMERA_ACK_FAIL_INVALID_REQUEST(5),
        PSEUDO_CAMERA_ACK_FAIL_ON_GROUND(6),
        PSEUDO_CAMERA_ACK_FAIL_CANT_TAKE_CONTROL(7),
        PSEUDO_CAMERA_ACK_FAIL_CAMERA_ERROR(8),
        PSEUDO_CAMERA_ACK_FAIL_TIMEOUT(9),
        PSEUDO_CAMERA_ACK_FAIL_FORBID(10),
        PSEUDO_CAMERA_ACK_FAIL_STORAGE_NOT_ENOUGH(11),
        PSEUDO_CAMERA_ACK_FAIL_DRONE_MOVING_FORBIDDEN(12),
        PSEUDO_CAMERA_ACK_FAIL_GIMBAL_MOVING_FORBIDDEN(13),
        PSEUDO_CAMERA_ACK_FAIL_USER_STICK(14),
        PSEUDO_CAMERA_ACK_FAIL_HEIGHT_RESTRICT(15),
        PSEUDO_CAMERA_ACK_FAIL_DISTANCE_RESTRICT(16),
        PSEUDO_CAMERA_ACT_FAIL_GIMBAL_STUCK(17),
        OTHER(255);
        
        private int data;

        private PseudoCameraCmdResult(int _data) {
            this.data = _data;
        }

        public byte value() {
            return (byte) this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PseudoCameraCmdResult find(int b) {
            PseudoCameraCmdResult result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
