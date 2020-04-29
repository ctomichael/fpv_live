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
public class DataGimbalControl extends DataBase implements DJIDataSyncListener {
    private static DataGimbalControl instance = null;
    private boolean isReset;
    private MODE mode;
    private int pitch;
    private int roll;
    private int yaw;

    public static synchronized DataGimbalControl getInstance() {
        DataGimbalControl dataGimbalControl;
        synchronized (DataGimbalControl.class) {
            if (instance == null) {
                instance = new DataGimbalControl();
            }
            dataGimbalControl = instance;
        }
        return dataGimbalControl;
    }

    public DataGimbalControl setPitch(int pitch2) {
        this.pitch = pitch2;
        return this;
    }

    public DataGimbalControl setRoll(int roll2) {
        this.roll = roll2;
        return this;
    }

    public DataGimbalControl setYaw(int yaw2) {
        this.yaw = yaw2;
        return this;
    }

    public DataGimbalControl setMode(MODE mode2) {
        this.mode = mode2;
        return this;
    }

    public DataGimbalControl reset(boolean isReset2) {
        this.isReset = isReset2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i;
        this._sendData = new byte[8];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.pitch), 0, this._sendData, 0, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.roll), 0, this._sendData, 2, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.yaw), 0, this._sendData, 4, 2);
        int value = this.mode.value() << 6;
        if (this.isReset) {
            i = 1;
        } else {
            i = 0;
        }
        System.arraycopy(BytesUtil.getUnsignedBytes((i << 5) | value), 0, this._sendData, 6, 1);
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.Control.value();
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
        pack.cmdId = CmdIdGimbal.CmdIdType.Control.value();
        start(pack, callBack);
    }

    @Keep
    public enum MODE {
        YawNoFollow(0),
        FPV(1),
        YawFollow(2),
        OTHER(254);
        
        private static volatile MODE[] modes;
        private int data;

        private MODE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static MODE find(int b) {
            MODE result = OTHER;
            if (modes == null) {
                modes = values();
            }
            for (int i = 0; i < modes.length; i++) {
                if (modes[i]._equals(b)) {
                    return modes[i];
                }
            }
            return result;
        }
    }
}
