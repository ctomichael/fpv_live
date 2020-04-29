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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataSingleDebugCtrlParam extends DataBase implements DJIDataSyncListener {
    private static DataSingleDebugCtrlParam mInstance = null;
    private CtrlType mCtrlType = CtrlType.OTHER;
    private float mValue = 0.0f;

    public static DataSingleDebugCtrlParam getInstance() {
        if (mInstance == null) {
            mInstance = new DataSingleDebugCtrlParam();
        }
        return mInstance;
    }

    public DataSingleDebugCtrlParam() {
        super(false);
    }

    public DataSingleDebugCtrlParam setCtrlParam(CtrlType type, float value) {
        this.mCtrlType = type;
        this.mValue = value;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[8];
        System.arraycopy(BytesUtil.getBytes(this.mCtrlType.value()), 0, this._sendData, 0, 4);
        System.arraycopy(BytesUtil.getBytes(this.mValue), 0, this._sendData, 4, 4);
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
        pack.cmdId = CmdIdEYE.CmdIdType.DebugCtrlParam.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }

    @Keep
    public enum CtrlType {
        TRACKING_MAXIMUM_SPEED(42),
        CLEAR_OBSTACLE(100),
        TAPFLY_RESET(408),
        FIXWING_GIMBALCTRL(416),
        OTHER(10000);
        
        private final int data;

        private CtrlType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CtrlType find(int b) {
            CtrlType result = TAPFLY_RESET;
            CtrlType[] values = values();
            for (CtrlType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
