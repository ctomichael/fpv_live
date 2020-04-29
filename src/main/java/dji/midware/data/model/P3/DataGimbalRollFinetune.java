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
import dji.midware.media.DJIVideoDecoder;

@Keep
@EXClassNullAway
public class DataGimbalRollFinetune extends DataBase implements DJIDataSyncListener {
    public static final int CAMERA_GIMBAL_TYPE_FPV = 1;
    public static final int CAMERA_GIMBAL_TYPE_NORMAL = 0;
    private static DataGimbalRollFinetune instance = null;
    private int mAxis = FineTuneAxis.ROLL.cmdValue;
    private byte mData = 0;
    private int mRepeatTimes = -1;
    private int mTimeOut = -1;
    private int mType = 0;

    public static synchronized DataGimbalRollFinetune getInstance() {
        DataGimbalRollFinetune dataGimbalRollFinetune;
        synchronized (DataGimbalRollFinetune.class) {
            if (instance == null) {
                instance = new DataGimbalRollFinetune();
            }
            dataGimbalRollFinetune = instance;
        }
        return dataGimbalRollFinetune;
    }

    public DataGimbalRollFinetune setFineTuneValue(byte value) {
        this.mData = value;
        return this;
    }

    public DataGimbalRollFinetune setFineTuneAxis(FineTuneAxis axis) {
        this.mAxis = axis.cmdValue;
        return this;
    }

    public void setTimeOut(int millisecond) {
        this.mTimeOut = millisecond;
    }

    public void setRepeatTimes(int n) {
        this.mRepeatTimes = n;
    }

    public CallBackStatus getRollCallBackStatus() {
        if (this._recData == null || this._recData.length < 1) {
            return CallBackStatus.OTHER;
        }
        return CallBackStatus.find(((Integer) get(0, 1, Integer.class)).intValue() & 3);
    }

    public CallBackStatus getPitchCallBackStatus() {
        if (this._recData == null || this._recData.length < 1) {
            return CallBackStatus.OTHER;
        }
        return CallBackStatus.find(((Integer) get(0, 1, Integer.class)).intValue() & 12);
    }

    public CallBackStatus getYawCallBackStatus() {
        if (this._recData == null || this._recData.length < 1) {
            return CallBackStatus.OTHER;
        }
        return CallBackStatus.find(((Integer) get(0, 1, Integer.class)).intValue() & 48);
    }

    public void setCameraGimbalType(int type) {
        this.mType = type;
    }

    public int getCameraGimbalType() {
        return this.mType;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = this.mData;
        this._sendData[1] = (byte) this.mAxis;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        if (this.mType == 0) {
            pack.receiverId = 0;
        } else if (this.mType == 1) {
            pack.receiverId = 4;
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.RollFinetune.value();
        this.mTimeOut = DJIVideoDecoder.connectLosedelay;
        if (this.mTimeOut > 0) {
            pack.timeOut = this.mTimeOut;
        }
        pack.repeatTimes = 0;
        if (this.mRepeatTimes > 0) {
            pack.repeatTimes = this.mRepeatTimes;
        }
        start(pack, callBack);
    }

    @Keep
    public enum FineTuneAxis {
        ROLL(0),
        PITCH(2),
        YAW(4);
        
        /* access modifiers changed from: private */
        public final int cmdValue;

        private FineTuneAxis(int value) {
            this.cmdValue = value;
        }
    }

    @Keep
    public enum CallBackStatus {
        NORMAL(0),
        PLUSMAX(1),
        MINUSMAX(2),
        OTHER(3);
        
        private final int value;

        private CallBackStatus(int _value) {
            this.value = _value;
        }

        public int value() {
            return this.value;
        }

        public boolean _equals(int b) {
            return this.value == b;
        }

        public static CallBackStatus find(int b) {
            CallBackStatus result = NORMAL;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
