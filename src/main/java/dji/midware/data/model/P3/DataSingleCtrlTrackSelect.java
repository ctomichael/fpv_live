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
public class DataSingleCtrlTrackSelect extends DataBase implements DJIDataSyncListener {
    private short mSessionId = 0;
    private TrackCtrlState mState = TrackCtrlState.CONFIRM;

    public DataSingleCtrlTrackSelect setCtrlState(TrackCtrlState state) {
        this.mState = state;
        return this;
    }

    public DataSingleCtrlTrackSelect setSessionId(short id) {
        this.mSessionId = id;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.mState.value();
        System.arraycopy(BytesUtil.getBytes(this.mSessionId), 0, this._sendData, 1, 2);
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
        pack.cmdId = CmdIdEYE.CmdIdType.CtrlTrackSelect.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    @Keep
    public enum TrackCtrlState {
        CANCEL(0),
        CONFIRM(1),
        PAUSE(2),
        OTHER(8);
        
        private int data;

        private TrackCtrlState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TrackCtrlState find(int b) {
            TrackCtrlState result = CONFIRM;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
