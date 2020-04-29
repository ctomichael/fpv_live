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
public class DataSingleMoveTrackSelect extends DataBase implements DJIDataSyncListener {
    private float mCenterX = 0.0f;
    private float mCenterY = 0.0f;
    private MoveCtrlType mCtrlType = MoveCtrlType.CONCERN;
    private float mHeight = 0.0f;
    private float mWidth = 0.0f;

    public DataSingleMoveTrackSelect setCtrlType(MoveCtrlType type) {
        this.mCtrlType = type;
        return this;
    }

    public DataSingleMoveTrackSelect setCenterXY(float x, float y) {
        this.mCenterX = x;
        this.mCenterY = y;
        return this;
    }

    public DataSingleMoveTrackSelect setWH(float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[17];
        this._sendData[0] = (byte) this.mCtrlType.value();
        System.arraycopy(BytesUtil.getBytes(this.mCenterX), 0, this._sendData, 1, 4);
        System.arraycopy(BytesUtil.getBytes(this.mCenterY), 0, this._sendData, 5, 4);
        System.arraycopy(BytesUtil.getBytes(this.mWidth), 0, this._sendData, 9, 4);
        System.arraycopy(BytesUtil.getBytes(this.mHeight), 0, this._sendData, 13, 4);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.MoveTrackSelect.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    @Keep
    public enum MoveCtrlType {
        CONCERN(0),
        MODIFY(1),
        OTHER(8);
        
        private int data;

        private MoveCtrlType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static MoveCtrlType find(int b) {
            MoveCtrlType result = CONCERN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
