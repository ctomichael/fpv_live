package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataEyeSetPOIAction extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetPOIAction instance = null;
    private int cmdId;

    public static synchronized DataEyeSetPOIAction getInstance() {
        DataEyeSetPOIAction dataEyeSetPOIAction;
        synchronized (DataEyeSetPOIAction.class) {
            if (instance == null) {
                instance = new DataEyeSetPOIAction();
            }
            dataEyeSetPOIAction = instance;
        }
        return dataEyeSetPOIAction;
    }

    public enum ActionType {
        ESTIMATE(1),
        PAUSE(2),
        RESUME(3),
        STOP(4),
        UNKNOWN(255);
        
        int data;

        private ActionType(int value) {
            this.data = value;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ActionType find(int b) {
            ActionType result = UNKNOWN;
            ActionType[] values = values();
            for (ActionType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    public DataEyeSetPOIAction setCmdId(int cmdId2) {
        this.cmdId = cmdId2;
        return this;
    }

    public DataEyeSetPOIAction setActionType(ActionType actionType) {
        this.cmdId = actionType.data;
        return this;
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetPOIAction.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.cmdId;
    }
}
