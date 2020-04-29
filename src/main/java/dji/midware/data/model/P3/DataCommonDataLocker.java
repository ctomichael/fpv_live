package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public abstract class DataCommonDataLocker extends DataBase implements DJIDataSyncListener {
    protected final String TAG = "DataCommonDataLocker";
    protected final DataLockerCmdType mCmdType;
    protected int mReceiveId = 4;
    protected DeviceType mReceiveType = DeviceType.DM368;

    protected DataCommonDataLocker(DataLockerCmdType type) {
        this.mCmdType = type;
    }

    public DataCommonDataLocker setReceiveType(DeviceType type) {
        this.mReceiveType = type;
        return this;
    }

    public DataCommonDataLocker setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.DataLocker.value();
        pack.repeatTimes = 3;
        start(pack, callBack);
    }

    @Keep
    public enum DataLockerCmdType {
        REQUEST_VERSION(0),
        REQUEST_STATUS(1),
        ENABLE_DATA_LOCKER(2),
        VERIFYING(3),
        VERIFY_SECURITY_CODE(4),
        CHANGE_SECURITY_CODE(5),
        CLEAR_SECURITY_CODE(6),
        RESETTING(7),
        FORMATING(11),
        REQUEST_FORMAT_STATE(12),
        UNKNOWN(255);
        
        private final int mValue;

        private DataLockerCmdType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        private boolean _equals(int b) {
            return this.mValue == b;
        }

        public static DataLockerCmdType find(int b) {
            DataLockerCmdType result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
