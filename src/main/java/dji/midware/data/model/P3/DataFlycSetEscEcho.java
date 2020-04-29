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

@Keep
@EXClassNullAway
public class DataFlycSetEscEcho extends DataBase implements DJIDataSyncListener {
    private int mIndex = 0;
    private SetEchoCmd mSetEchoCmd = SetEchoCmd.OPEN_ALL;

    public DataFlycSetEscEcho setEchoCmd(SetEchoCmd cmd) {
        this.mSetEchoCmd = cmd;
        return this;
    }

    public DataFlycSetEscEcho setEchoIndex(int index) {
        this.mIndex = index;
        return this;
    }

    public SetResult getSetResult() {
        return SetResult.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public boolean isEchoing() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) != 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.mSetEchoCmd.value();
        this._sendData[1] = (byte) this.mIndex;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetEscEcho.value();
        start(pack, callBack);
    }

    @Keep
    public enum SetEchoCmd {
        CLOSE_ALL(1),
        OPEN_ALL(2),
        CLOSE_ONE(3),
        OPEN_ONE(4),
        REQUEST_SOME(5),
        OTHER(100);
        
        private final int data;

        private SetEchoCmd(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SetEchoCmd find(int b) {
            SetEchoCmd result = CLOSE_ALL;
            SetEchoCmd[] values = values();
            for (SetEchoCmd tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum SetResult {
        SUCCESS(0),
        FAIL_MOTORUP(1),
        FAIL_NONSMART(2),
        FAIL_ILLEGALCMD(3),
        FAIL_ERRORID(4),
        OTHER(100);
        
        private final int data;

        private SetResult(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SetResult find(int b) {
            SetResult result = SUCCESS;
            SetResult[] values = values();
            for (SetResult tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
