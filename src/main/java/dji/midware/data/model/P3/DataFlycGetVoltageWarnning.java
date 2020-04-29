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
public class DataFlycGetVoltageWarnning extends DataBase implements DJIDataSyncListener {
    private static DataFlycGetVoltageWarnning instance = null;
    private WarnningLevel level;

    public static synchronized DataFlycGetVoltageWarnning getInstance() {
        DataFlycGetVoltageWarnning dataFlycGetVoltageWarnning;
        synchronized (DataFlycGetVoltageWarnning.class) {
            if (instance == null) {
                instance = new DataFlycGetVoltageWarnning();
            }
            dataFlycGetVoltageWarnning = instance;
        }
        return dataFlycGetVoltageWarnning;
    }

    public void setWarnningLevel(WarnningLevel level2) {
        this.level = level2;
    }

    public WarnningLevel getWarnningLevel() {
        return WarnningLevel.find(this._recData[0]);
    }

    public int getValue() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public boolean isNeedGoHome() {
        return this._recData[2] == 1;
    }

    public boolean isNeedLanding() {
        return this._recData[2] == 2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.level.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.GetVoltageWarnning.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum WarnningLevel {
        First(1),
        Second(2),
        OTHER(100);
        
        private int data;

        private WarnningLevel(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static WarnningLevel find(int b) {
            WarnningLevel result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
