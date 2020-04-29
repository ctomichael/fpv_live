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
public class DataFlycNavigationSwitch extends DataBase implements DJIDataSyncListener {
    private static DataFlycNavigationSwitch instance = null;
    private int mRetryTimes = 2;
    private GS_COMMAND type;

    public static synchronized DataFlycNavigationSwitch getInstance() {
        DataFlycNavigationSwitch dataFlycNavigationSwitch;
        synchronized (DataFlycNavigationSwitch.class) {
            if (instance == null) {
                instance = new DataFlycNavigationSwitch();
            }
            dataFlycNavigationSwitch = instance;
        }
        return dataFlycNavigationSwitch;
    }

    public DataFlycNavigationSwitch setCommand(GS_COMMAND type2) {
        this.type = type2;
        return this;
    }

    public DataFlycNavigationSwitch setRetryTimes(int times) {
        this.mRetryTimes = times;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    @Keep
    public enum GS_COMMAND {
        OPEN_GROUND_STATION(1),
        CLOSE_GROUND_STATION(2),
        OTHER(100);
        
        private int data;

        private GS_COMMAND(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GS_COMMAND find(int b) {
            GS_COMMAND result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.type.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.NavigationSwitch.value();
        pack.data = getSendData();
        pack.repeatTimes = this.mRetryTimes;
        this.mRetryTimes = 2;
        start(pack, callBack);
    }
}
