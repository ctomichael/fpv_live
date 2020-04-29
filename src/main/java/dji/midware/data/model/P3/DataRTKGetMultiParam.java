package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdRTK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataRTKGetMultiParam extends DataBase implements DJIDataSyncListener {
    private int paramId;

    public DataRTKGetMultiParam setParamId(RtkGetParam paramId2) {
        this.paramId = paramId2.getValue();
        return this;
    }

    public boolean getResult() {
        return ((Integer) get(1, 1, Integer.class)).intValue() == 0;
    }

    public int getRTKMode() {
        return getInteger();
    }

    public int getBaseStationSatelliteCount() {
        return getInteger();
    }

    public long getSDRSignalStrength() {
        return ((Long) get(1, 10, Long.class)).longValue();
    }

    public int getWifiWorkMode() {
        return getInteger();
    }

    public int getWifiState() {
        return getInteger();
    }

    public int getWifiChannel() {
        return getInteger();
    }

    public String getDeviceName() {
        return getString();
    }

    public String getDevicePassword() {
        return getString();
    }

    public String getWifiPassword() {
        return getString();
    }

    public String getBaseStationLocation() {
        return getString();
    }

    public String getCorsServerAddress() {
        return getString();
    }

    public String getCorsServerPort() {
        return getString();
    }

    public String getCorsLoginAccount() {
        return getString();
    }

    public String getCorsLoginPassword() {
        return getString();
    }

    public int getDeviceRemainingBattery() {
        return getInteger();
    }

    private int getInteger() {
        return ((Integer) get(4, getParamLength(), Integer.class)).intValue();
    }

    private String getString() {
        return get(4, getParamLength());
    }

    private int getParamLength() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.paramId;
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
        pack.cmdId = CmdIdRTK.CmdIdType.GetMultiParam.value();
        start(pack, callBack);
    }

    public enum RtkGetParam {
        MODE(0),
        SDR_PAIR(2),
        SDR_SIGNAL_STRENGTH(10),
        BS_SATELLITE_NUM(22),
        WIFI_WORK_MODE(25),
        WIFI_STATE(26),
        WIFI_CHANNEL(27),
        DEVICE_NAME(28),
        DEVICE_PWD(29),
        WIFI_PWD(30),
        BS_GPS_LOCATION(31),
        CORS_SERVER_ADDR(32),
        CORS_SERVER_PORT(33),
        CORS_LOGIN_ACCOUNT(34),
        CORS_LOGIN_PWD(35),
        DEVICE_BATTERY(36);
        
        private final int value;

        private RtkGetParam(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }
    }
}
