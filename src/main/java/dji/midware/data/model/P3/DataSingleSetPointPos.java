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
public class DataSingleSetPointPos extends DataBase implements DJIDataSyncListener {
    private float mPosX = 0.0f;
    private float mPosY = 0.0f;
    private short mSessionId = 0;
    private TapMode mTapMode = TapMode.POSITIVE_FLY;

    public DataSingleSetPointPos setPosXY(float x, float y) {
        this.mPosX = x;
        this.mPosY = y;
        return this;
    }

    public DataSingleSetPointPos setSessionId(short id) {
        this.mSessionId = id;
        return this;
    }

    public DataSingleSetPointPos setTapMode(TapMode mode) {
        this.mTapMode = mode;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[11];
        System.arraycopy(BytesUtil.getBytes(this.mPosX), 0, this._sendData, 0, 4);
        System.arraycopy(BytesUtil.getBytes(this.mPosY), 0, this._sendData, 4, 4);
        System.arraycopy(BytesUtil.getBytes(this.mSessionId), 0, this._sendData, 8, 2);
        this._sendData[10] = (byte) this.mTapMode.value();
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetPointPos.value();
        pack.timeOut = 5000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    @Keep
    public enum TapMode {
        POSITIVE_FLY(0),
        REVERSE_FLY(1),
        HEADLESS_CIRCLE(2),
        HEADLESS_PARALLEL(3),
        POSITION_FLY(7),
        OTHER(100);
        
        private final int data;

        private TapMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TapMode find(int b) {
            TapMode result = POSITIVE_FLY;
            TapMode[] values = values();
            for (TapMode tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
