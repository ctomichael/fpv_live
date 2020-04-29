package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdRTK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.nio.charset.Charset;

public class DataRTKSetMultiParam extends DataBase implements DJIDataSyncListener {
    private int paramId;
    private int paramLength;
    private byte[] paramValue;

    public DataRTKSetMultiParam setMode(int value) {
        this.paramId = 0;
        this.paramLength = 1;
        this.paramValue = new byte[]{(byte) value};
        return this;
    }

    public DataRTKSetMultiParam sdrPairing(SDRPairingAction action) {
        this.paramId = 2;
        this.paramLength = 1;
        this.paramValue = new byte[]{(byte) action.getValue()};
        return this;
    }

    public DataRTKSetMultiParam setDeviceName(String name) {
        this.paramId = 28;
        this.paramValue = name.getBytes(Charset.forName("UTF-8"));
        this.paramLength = 32;
        return this;
    }

    public DataRTKSetMultiParam setDevicePassword(String password) {
        this.paramId = 29;
        this.paramValue = password.getBytes(Charset.forName("UTF-8"));
        this.paramLength = 32;
        return this;
    }

    public DataRTKSetMultiParam setWifiPassword(String password) {
        this.paramId = 30;
        this.paramValue = password.getBytes(Charset.forName("UTF-8"));
        this.paramLength = 32;
        return this;
    }

    public DataRTKSetMultiParam setBaseStationLocation(String location) {
        this.paramId = 31;
        this.paramValue = location.getBytes(Charset.forName("UTF-8"));
        this.paramLength = 128;
        return this;
    }

    public DataRTKSetMultiParam setCorsServerAddress(String address) {
        this.paramId = 32;
        this.paramValue = address.getBytes(Charset.forName("UTF-8"));
        this.paramLength = this.paramValue.length;
        return this;
    }

    public DataRTKSetMultiParam setCorsServerPort(String port) {
        this.paramId = 33;
        this.paramValue = port.getBytes(Charset.forName("UTF-8"));
        this.paramLength = this.paramValue.length;
        return this;
    }

    public DataRTKSetMultiParam setCorsLoginAccount(String account) {
        this.paramId = 34;
        this.paramValue = account.getBytes(Charset.forName("UTF-8"));
        this.paramLength = 32;
        return this;
    }

    public DataRTKSetMultiParam setCorsLoginPassword(String password) {
        this.paramId = 35;
        this.paramValue = password.getBytes(Charset.forName("UTF-8"));
        this.paramLength = 32;
        return this;
    }

    public int getResult() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[(this.paramLength + 2)];
        this._sendData[0] = (byte) this.paramId;
        this._sendData[1] = (byte) this.paramLength;
        System.arraycopy(this.paramValue, 0, this._sendData, 2, Math.min(this.paramValue.length, this.paramLength));
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GPS.value();
        pack.receiverId = 5;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RTK.value();
        pack.cmdId = CmdIdRTK.CmdIdType.SetMultiParam.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    class MGRtkParam {
        public static final int BS_GPS_LOCATION = 31;
        public static final int BS_SATELLITE_NUM = 22;
        public static final int CORS_LOGIN_ACCOUNT = 34;
        public static final int CORS_LOGIN_PWD = 35;
        public static final int CORS_SERVER_ADDR = 32;
        public static final int CORS_SERVER_PORT = 33;
        public static final int DEVICE_BATTERY = 36;
        public static final int DEVICE_NAME = 28;
        public static final int DEVICE_PWD = 29;
        public static final int MODE = 0;
        public static final int SDR_PAIR = 2;
        public static final int WIFI_CHANNEL = 27;
        public static final int WIFI_PWD = 30;
        public static final int WIFI_STATE = 26;
        public static final int WIFI_WORK_MODE = 25;

        MGRtkParam() {
        }
    }

    enum RTKMode {
        DEFAULT(0),
        BS_4G_SDR(1),
        BS_WIFI(2),
        BS_CORS(3),
        HANDHELD_SDR(4),
        HANDHELD_NETWORK(5),
        UNKNOWN(255);
        
        private final int value;

        private RTKMode(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static RTKMode find(int value2) {
            RTKMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    enum SDRPairingAction {
        NO_ACTION(0),
        START_PAIRING(1),
        STOP_PAIRING(2);
        
        private final int value;

        private SDRPairingAction(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }
    }

    enum SDRPairingState {
        PAIRED(10),
        PAIRING(11);
        
        private final int value;

        private SDRPairingState(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }
    }
}
