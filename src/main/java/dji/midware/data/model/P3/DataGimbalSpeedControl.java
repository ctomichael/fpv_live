package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdGimbal;
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
public class DataGimbalSpeedControl extends DataBase implements DJIDataSyncListener {
    private static DataGimbalSpeedControl instance = null;
    private boolean ignoreGimbalStick;
    private boolean isGetPermission;
    private boolean multiControl;
    private int pitch;
    private int roll;
    private int yaw;

    public static synchronized DataGimbalSpeedControl getInstance() {
        DataGimbalSpeedControl dataGimbalSpeedControl;
        synchronized (DataGimbalSpeedControl.class) {
            if (instance == null) {
                instance = new DataGimbalSpeedControl();
            }
            dataGimbalSpeedControl = instance;
        }
        return dataGimbalSpeedControl;
    }

    public DataGimbalSpeedControl setPitch(int pitch2) {
        this.pitch = pitch2;
        return this;
    }

    public DataGimbalSpeedControl setRoll(int roll2) {
        this.roll = roll2;
        return this;
    }

    public DataGimbalSpeedControl setYaw(int yaw2) {
        this.yaw = yaw2;
        return this;
    }

    public DataGimbalSpeedControl setPermission(boolean isGetPermission2) {
        this.isGetPermission = isGetPermission2;
        return this;
    }

    public DataGimbalSpeedControl setIgnoreGimbalStick(boolean ignoreGimbalStick2) {
        this.ignoreGimbalStick = ignoreGimbalStick2;
        return this;
    }

    public DataGimbalSpeedControl setMultiControl(boolean multiControl2) {
        this.multiControl = multiControl2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 0;
        this._sendData = new byte[7];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.yaw), 0, this._sendData, 0, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.roll), 0, this._sendData, 2, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.pitch), 0, this._sendData, 4, 2);
        byte[] bArr = this._sendData;
        if (this.isGetPermission) {
            i = 1;
        }
        bArr[6] = (byte) ((this.ignoreGimbalStick ? 64 : 255) | (i << 7));
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.SpeedControl.value();
        super.start(pack);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.SpeedControl.value();
        start(pack, callBack);
    }
}
