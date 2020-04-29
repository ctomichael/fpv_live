package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdNarrowBand;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
public class DataNarrowBandBaseInfoConfig extends DataBase implements DJIDataSyncListener {
    private static final int FLAG_NEED_CONNECT = 64;
    private static final int FLAG_NEED_DISCONNECT = 128;
    private static final int FLAG_NEED_SET_MODE = 32;
    private static DataNarrowBandBaseInfoConfig instance = null;
    private byte[] mAuthCode;
    private int mAuthCodeLength = 0;
    private CmdType mCmdType = CmdType.OTHER;
    private byte[] mDeviceId;
    private byte mNarrowBandValue = 0;
    private byte mValue = 0;

    public static synchronized DataNarrowBandBaseInfoConfig getInstance() {
        DataNarrowBandBaseInfoConfig dataNarrowBandBaseInfoConfig;
        synchronized (DataNarrowBandBaseInfoConfig.class) {
            if (instance == null) {
                instance = new DataNarrowBandBaseInfoConfig();
            }
            dataNarrowBandBaseInfoConfig = instance;
        }
        return dataNarrowBandBaseInfoConfig;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void post() {
        super.post();
    }

    public DataNarrowBandBaseInfoConfig setCmdType(CmdType type) {
        this.mCmdType = type;
        return this;
    }

    public DataNarrowBandBaseInfoConfig setValue(byte value) {
        this.mValue = value;
        return this;
    }

    public DataNarrowBandBaseInfoConfig setNarrowBandConnect(String deviceID, String authCode, byte modeValue) {
        setCmdType(CmdType.NARROW_BAND_STATUS);
        setValue((byte) 1);
        this.mNarrowBandValue = (byte) (modeValue | 96);
        this.mDeviceId = BytesUtil.hex2byte(deviceID);
        this.mAuthCode = BytesUtil.hex2byte(authCode);
        this.mAuthCodeLength = this.mAuthCode.length;
        return this;
    }

    public DataNarrowBandBaseInfoConfig setNarrowBandDisConnect(String deviceID, String authCode) {
        setCmdType(CmdType.NARROW_BAND_STATUS);
        setValue((byte) 1);
        this.mNarrowBandValue = Byte.MIN_VALUE;
        this.mDeviceId = BytesUtil.hex2byte(deviceID);
        this.mAuthCode = BytesUtil.hex2byte(authCode);
        this.mAuthCodeLength = this.mAuthCode.length;
        return this;
    }

    public DataNarrowBandBaseInfoConfig setNarrowBandWorkMode(byte value) {
        setCmdType(CmdType.NARROW_BAND_STATUS);
        setValue((byte) 1);
        this.mNarrowBandValue = (byte) (value | 32);
        return this;
    }

    public DataNarrowBandBaseInfoConfig setNarrowBandAuthCode(String authCode) {
        setCmdType(CmdType.NARROW_BAND_SLAVE_MASTER_PARAMS);
        setValue((byte) 1);
        this.mNarrowBandValue = (byte) BytesUtil.hex2byte(authCode).length;
        this.mAuthCode = BytesUtil.hex2byte(authCode);
        return this;
    }

    public CmdType getCmdType() {
        return CmdType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public int getValue() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public boolean isForNarrowBand() {
        return CmdType.find(((Integer) get(1, 1, Integer.class)).intValue()) == CmdType.NARROW_BAND_STATUS;
    }

    public boolean getNarrowBandConnectionState() {
        return BytesUtil.getBitsFromByte(((Integer) get(2, 1, Integer.class)).intValue(), 7, 1) == 1;
    }

    public boolean getPadAttennnaConnectionState() {
        return BytesUtil.getBitsFromByte(((Integer) get(2, 1, Integer.class)).intValue(), 6, 1) == 1;
    }

    public boolean isNarrowBandConnected() {
        return BytesUtil.getBitsFromByte(((Integer) get(2, 1, Integer.class)).intValue(), 5, 1) == 1;
    }

    public boolean isNarrowBandShieldUpCmd() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 16) != 0;
    }

    public int getNarrowBandWorkMode() {
        return BytesUtil.getBitsFromByte(((Integer) get(2, 1, Integer.class)).intValue(), 0, 4);
    }

    public String getNarrowBandMasterId() {
        byte[] sub = new byte[3];
        try {
            System.arraycopy(this._recData, 3, sub, 0, 3);
            return BytesUtil.byte2hexNoSep(sub);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getNarrowBandSelfId() {
        byte[] sub = new byte[3];
        try {
            System.arraycopy(this._recData, 6, sub, 0, 3);
            return BytesUtil.byte2hexNoSep(sub);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getNarrowBandAuthCode() {
        int length = ((Integer) get(9, 1, Integer.class)).intValue();
        byte[] sub = new byte[length];
        try {
            System.arraycopy(this._recData, 10, sub, 0, length);
            return BytesUtil.byte2hexNoSep(sub);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.mCmdType == CmdType.NARROW_BAND_SLAVE_MASTER_PARAMS) {
            this._sendData = new byte[(this.mNarrowBandValue + 3)];
            this._sendData[0] = (byte) this.mCmdType.value();
            this._sendData[1] = this.mValue;
            this._sendData[2] = this.mNarrowBandValue;
            System.arraycopy(this.mAuthCode, 0, this._sendData, 3, this.mNarrowBandValue);
        } else if (this.mCmdType == CmdType.NARROW_BAND_STATUS) {
            if ((this.mNarrowBandValue & 64) == 64 || (this.mNarrowBandValue & 128) == 128) {
                this._sendData = new byte[(this.mAuthCodeLength + 7)];
                this._sendData[0] = (byte) this.mCmdType.value();
                this._sendData[1] = this.mValue;
                this._sendData[2] = this.mNarrowBandValue;
                if (this.mDeviceId != null && this.mDeviceId.length >= 3) {
                    System.arraycopy(this.mDeviceId, 0, this._sendData, 3, 3);
                }
                this._sendData[6] = (byte) this.mAuthCodeLength;
                if (this.mAuthCode != null) {
                    System.arraycopy(this.mAuthCode, 0, this._sendData, 7, this.mAuthCodeLength);
                    return;
                }
                return;
            }
            this._sendData = new byte[3];
            this._sendData[0] = (byte) this.mCmdType.value();
            this._sendData[1] = this.mValue;
            this._sendData[2] = this.mNarrowBandValue;
        } else if (CmdType.NARROW_BAND_SHIELD_UP_CMD == this.mCmdType) {
            this._sendData = new byte[2];
            this._sendData[0] = (byte) this.mCmdType.value();
            this._sendData[1] = this.mValue;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.NarrowBand.value();
        pack.cmdId = CmdIdNarrowBand.CmdIdType.BaseInfoConfig.value();
        pack.timeOut = 15000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    @Keep
    public enum CmdType {
        NARROW_BAND_SHIELD_UP_CMD(32),
        NARROW_BAND_STATUS(33),
        NARROW_BAND_SLAVE_MASTER_PARAMS(34),
        OTHER(100);
        
        private final int data;

        private CmdType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdType find(int b) {
            CmdType result = OTHER;
            CmdType[] values = values();
            for (CmdType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
