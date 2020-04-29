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

@Keep
@EXClassNullAway
public class DataEyeDrawOperation extends DataBase implements DJIDataSyncListener {
    private static DataEyeDrawOperation instance = null;
    private OperateCmd mCmd = OperateCmd.START;
    private int mRepeatTime = 3;

    public static synchronized DataEyeDrawOperation getInstance() {
        DataEyeDrawOperation dataEyeDrawOperation;
        synchronized (DataEyeDrawOperation.class) {
            if (instance == null) {
                instance = new DataEyeDrawOperation();
            }
            dataEyeDrawOperation = instance;
        }
        return dataEyeDrawOperation;
    }

    public DataEyeDrawOperation setOperateCmd(OperateCmd cmd) {
        this.mCmd = cmd;
        return this;
    }

    public DataEyeDrawOperation setRepeatTime(int repeatTime) {
        this.mRepeatTime = repeatTime;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mCmd.value();
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
        pack.cmdId = CmdIdEYE.CmdIdType.DrawOperation.value();
        pack.timeOut = 1000;
        pack.repeatTimes = this.mRepeatTime;
        start(pack, callBack);
    }

    @Keep
    public enum OperateCmd {
        START(0),
        STOP(1),
        PAUSE(2),
        RESUME(3),
        PREPARE(4),
        UNPREPARE(5),
        OTHER(100);
        
        private final int data;

        private OperateCmd(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static OperateCmd find(int b) {
            OperateCmd result = START;
            OperateCmd[] values = values();
            for (OperateCmd tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
