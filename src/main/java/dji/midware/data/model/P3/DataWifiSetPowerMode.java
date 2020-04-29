package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdWifi;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataWifiSetPowerMode extends DataBase implements DJIDataSyncListener {
    private static DataWifiSetPowerMode mInstance = null;
    private DJIWifiPowerMode mode = null;

    public static synchronized DataWifiSetPowerMode getInstance() {
        DataWifiSetPowerMode dataWifiSetPowerMode;
        synchronized (DataWifiSetPowerMode.class) {
            if (mInstance == null) {
                mInstance = new DataWifiSetPowerMode();
            }
            dataWifiSetPowerMode = mInstance;
        }
        return dataWifiSetPowerMode;
    }

    public DataWifiSetPowerMode setMode(DJIWifiPowerMode mode2) {
        this.mode = mode2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mode.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.WIFI_G.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.SetPowerMode.value();
        start(pack, callBack);
    }

    @Keep
    public enum DJIWifiPowerMode {
        FCC(0),
        CE(1),
        OTHER(100);
        
        private int data;

        private DJIWifiPowerMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIWifiPowerMode find(int b) {
            DJIWifiPowerMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
