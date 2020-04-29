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
public class DataBaseCameraSetting extends DataBase implements DJIDataSyncListener {
    protected String cmdId;
    protected CmdIdCamera.CmdIdType cmdIdType;
    protected CmdSet cmdSet = CmdSet.CAMERA;
    protected int mRepeatTime = 2;
    protected int mTimeOut = 1000;
    protected int value;

    public DataBaseCameraSetting setCmdId(String cmdId2) {
        this.cmdId = cmdId2;
        return this;
    }

    public DataBaseCameraSetting setCmdId(CmdIdCamera.CmdIdType cmdIdType2) {
        this.cmdIdType = cmdIdType2;
        return this;
    }

    public DataBaseCameraSetting setValue(int value2) {
        this.value = value2;
        return this;
    }

    public void setPackParam(int timeout, int repeat) {
        if (timeout > 0) {
            this.mTimeOut = timeout;
        }
        if (repeat > 0 && repeat <= 3) {
            this.mRepeatTime = repeat;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.value;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = this.cmdSet.value();
        if (this.cmdIdType != null) {
            pack.cmdId = this.cmdIdType.value();
        } else {
            pack.cmdId = CmdIdCamera.CmdIdType.valueOf("Set" + this.cmdId).value();
        }
        pack.timeOut = this.mTimeOut;
        pack.repeatTimes = this.mRepeatTime;
        start(pack, callBack);
    }

    @Keep
    public enum MFDemarcateAction {
        Stop(0),
        Start(1),
        Unknown(8);
        
        private static volatile MFDemarcateAction[] sValues = null;
        private int _value = 0;

        private MFDemarcateAction(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static MFDemarcateAction find(int value) {
            if (sValues == null) {
                sValues = values();
            }
            MFDemarcateAction[] mFDemarcateActionArr = sValues;
            for (MFDemarcateAction ts : mFDemarcateActionArr) {
                if (ts._equals(value)) {
                    return ts;
                }
            }
            return Unknown;
        }
    }
}
