package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycJoystick extends DataBase implements DJIDataAsync2Listener {
    private static DataFlycJoystick instance = null;
    private byte flag;
    private float pitch;
    private float roll;
    private float throttle;
    private float yaw;

    public static synchronized DataFlycJoystick getInstance() {
        DataFlycJoystick dataFlycJoystick;
        synchronized (DataFlycJoystick.class) {
            if (instance == null) {
                instance = new DataFlycJoystick();
            }
            dataFlycJoystick = instance;
        }
        return dataFlycJoystick;
    }

    public DataFlycJoystick setPitch(float pitch2) {
        this.pitch = pitch2;
        return this;
    }

    public DataFlycJoystick setRoll(float roll2) {
        this.roll = roll2;
        return this;
    }

    public DataFlycJoystick setYaw(float yaw2) {
        this.yaw = yaw2;
        return this;
    }

    public DataFlycJoystick setThrottle(float throttle2) {
        this.throttle = throttle2;
        return this;
    }

    public DataFlycJoystick setFlag(byte flag2) {
        this.flag = flag2;
        return this;
    }

    public byte[] getDataForRecord() {
        return this._sendData;
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.JoyStick.value();
        pack.data = getSendData();
        super.start(pack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[17];
        this._sendData[0] = this.flag;
        System.arraycopy(BytesUtil.getBytes(this.roll), 0, this._sendData, 1, 4);
        System.arraycopy(BytesUtil.getBytes(this.pitch), 0, this._sendData, 5, 4);
        System.arraycopy(BytesUtil.getBytes(this.yaw), 0, this._sendData, 9, 4);
        System.arraycopy(BytesUtil.getBytes(this.throttle), 0, this._sendData, 13, 4);
    }
}
