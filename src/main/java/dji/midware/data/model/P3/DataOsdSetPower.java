package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataOsdSetPower extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetPower instance = null;
    private POWER_TYPE mPowerType = POWER_TYPE.OTHER;

    public static synchronized DataOsdSetPower getInstance() {
        DataOsdSetPower dataOsdSetPower;
        synchronized (DataOsdSetPower.class) {
            if (instance == null) {
                instance = new DataOsdSetPower();
            }
            dataOsdSetPower = instance;
        }
        return dataOsdSetPower;
    }

    public DataOsdSetPower setPowerType(POWER_TYPE type) {
        this.mPowerType = type;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mPowerType.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetPower.value();
        start(pack, callBack);
    }

    @Keep
    public enum POWER_TYPE {
        NOT_CHANGE(0),
        SLEEP(1),
        AWEAK(2),
        RESET(3),
        POWER_OFF(4),
        OTHER(10);
        
        private int data;

        private POWER_TYPE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static POWER_TYPE find(int b) {
            POWER_TYPE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
