package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdGimbal;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraInfoNotify extends DataBase {
    private static final String TAG = DataCameraInfoNotify.class.getSimpleName();
    private static DataCameraInfoNotify instance = null;
    private int cameraId = 0;
    private DeviceType mDeviceType = DeviceType.GIMBAL;
    private int phoneAttitude = -1;
    private ShootAction shootAction = ShootAction.NORMAL;

    public static synchronized DataCameraInfoNotify getInstance() {
        DataCameraInfoNotify dataCameraInfoNotify;
        synchronized (DataCameraInfoNotify.class) {
            if (instance == null) {
                instance = new DataCameraInfoNotify();
            }
            dataCameraInfoNotify = instance;
        }
        return dataCameraInfoNotify;
    }

    public DataCameraInfoNotify setCameraId(int cameraId2) {
        this.cameraId = cameraId2;
        return this;
    }

    public DataCameraInfoNotify setPhoneAttitude(int attitude) {
        this.phoneAttitude = attitude;
        return this;
    }

    public DataCameraInfoNotify setShootAction(ShootAction actionType) {
        this.shootAction = actionType;
        return this;
    }

    public DataCameraInfoNotify setReceiver(DeviceType type) {
        this.mDeviceType = type;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) ((this.cameraId & 1) | ((this.phoneAttitude & 1) << 1) | ((this.shootAction.cmd & 7) << 2));
    }

    public RecvPack getRecvPack() {
        return this.recvPack;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mDeviceType.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.NotifyCameraInfo.value();
        super.start(pack, callBack);
        Log.d(TAG, "start: send data = " + BytesUtil.byte2hex(pack.buffer));
    }

    @Keep
    public enum ShootAction {
        TAKE_PHOTO_SINGLE(1),
        RECORD(2),
        TAKE_PHOTO_BURST(3),
        TAKE_PHOTO_LONG_EXPOSURE(4),
        NORMAL(7);
        
        public int cmd;

        private ShootAction(int cmd2) {
            this.cmd = cmd2;
        }
    }
}
