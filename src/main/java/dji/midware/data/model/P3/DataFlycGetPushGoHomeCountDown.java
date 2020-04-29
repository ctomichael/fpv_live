package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
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
public class DataFlycGetPushGoHomeCountDown extends DataBase implements DJIDataSyncListener {
    private static DataFlycGetPushGoHomeCountDown instance;
    private int mCmdType;
    private int mSendAction;

    public static synchronized DataFlycGetPushGoHomeCountDown getInstance() {
        DataFlycGetPushGoHomeCountDown dataFlycGetPushGoHomeCountDown;
        synchronized (DataFlycGetPushGoHomeCountDown.class) {
            if (instance == null) {
                instance = new DataFlycGetPushGoHomeCountDown();
            }
            dataFlycGetPushGoHomeCountDown = instance;
        }
        return dataFlycGetPushGoHomeCountDown;
    }

    public DataFlycGetPushGoHomeCountDown setCmdType(int cmdType) {
        this.mCmdType = cmdType;
        return this;
    }

    public DataFlycGetPushGoHomeCountDown setSendAction(int sendAction) {
        this.mSendAction = sendAction;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.mCmdType;
        if (this.mCmdType == 2) {
            this._sendData[1] = 1;
            this._sendData[2] = 0;
            this._sendData[3] = (byte) this.mSendAction;
            return;
        }
        this._sendData[1] = 0;
        this._sendData[3] = (byte) this.mSendAction;
    }

    public int getCmdType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public GoHomePushActionType getActionType() {
        return GoHomePushActionType.find(((Integer) get(1, 1, Integer.class)).intValue());
    }

    public int getCountDownSec() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getNewCountingInSec() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        pack.cmdId = CmdIdFlyc.CmdIdType.GetPushGoHomeCountDown.value();
        start(pack, callBack);
    }

    @Keep
    public enum GoHomePushActionType {
        LOSE_SATELLITE_RESTORE(0),
        FORCE_LANDING(3),
        INTELLIGENT_LANDING(4),
        OTHER(100);
        
        private final int data;

        private GoHomePushActionType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GoHomePushActionType find(int b) {
            GoHomePushActionType result = OTHER;
            GoHomePushActionType[] values = values();
            for (GoHomePushActionType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
