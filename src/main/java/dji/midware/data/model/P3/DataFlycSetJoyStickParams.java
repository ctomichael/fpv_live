package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.config.P3.CmdIdSpecial;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycSetJoyStickParams extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetJoyStickParams instance = null;
    private int buttonData = 0;
    private int change = 0;
    private int channel0 = 1024;
    private int channel1 = 1024;
    private int channel2 = 1024;
    private int channel3 = 1024;
    private int channel4 = 1024;
    private int channel5 = 1024;
    private int channel6 = 1024;
    private int focus = 1;
    private int gohome = 0;
    private int ioc_key = 0;
    private boolean isButton = false;
    private boolean isMenu = false;
    private boolean isPlayback = false;
    private boolean isRecord = false;
    private int mode_sw = FlycMode.P.value();
    private byte rs_key = 0;
    private int shutter = 0;
    private int symbol = 0;
    private int transform_sw = 0;

    @Keep
    public enum FlycMode {
        A(0),
        P(1),
        F(2),
        OTHER(100);
        
        private int data;

        private FlycMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FlycMode find(int b) {
            FlycMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static synchronized DataFlycSetJoyStickParams getInstance() {
        DataFlycSetJoyStickParams dataFlycSetJoyStickParams;
        synchronized (DataFlycSetJoyStickParams.class) {
            if (instance == null) {
                instance = new DataFlycSetJoyStickParams(false);
            }
            dataFlycSetJoyStickParams = instance;
        }
        return dataFlycSetJoyStickParams;
    }

    public DataFlycSetJoyStickParams(boolean isRegist) {
        super(isRegist);
    }

    public DataFlycSetJoyStickParams setJoyStick(int throttle, int pitch, int roll, int yaw) {
        this.channel0 = roll;
        this.channel1 = pitch;
        this.channel2 = throttle;
        this.channel3 = yaw;
        return this;
    }

    public DataFlycSetJoyStickParams setMode(FlycMode mode) {
        this.mode_sw = mode.value();
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SPECIAL.value();
        pack.cmdId = CmdIdSpecial.CmdIdType.JoySitckSetParams.value();
        start(pack, callBack);
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SPECIAL.value();
        pack.cmdId = CmdIdSpecial.CmdIdType.JoySitckSetParams.value();
        if ((DJIProductManager.getInstance().getType() == ProductType.KumquatX && DJIUSBWifiSwitchManager.getInstance().isWifiConnected()) || (DJIUSBWifiSwitchManager.getInstance().isWifiConnected() && !DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null))) {
            super.start(pack);
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this._sendData == null) {
            this._sendData = new byte[13];
        }
        this._sendData[0] = 0;
        if (this.isMenu) {
            byte[] bArr = this._sendData;
            bArr[0] = (byte) (bArr[0] | 1);
        }
        if (this.isPlayback) {
            byte[] bArr2 = this._sendData;
            bArr2[0] = (byte) (bArr2[0] | 2);
        }
        if (this.isRecord) {
            byte[] bArr3 = this._sendData;
            bArr3[0] = (byte) (bArr3[0] | 4);
        }
        BytesUtil.arraycopy(generateChannelByte(), this._sendData, 1);
        this._sendData[11] = 0;
        if (this.isButton) {
            byte[] bArr4 = this._sendData;
            bArr4[11] = (byte) (bArr4[11] | 1);
        }
        byte[] bArr5 = this._sendData;
        bArr5[11] = (byte) (bArr5[11] | (this.buttonData << 1));
        byte[] bArr6 = this._sendData;
        bArr6[11] = (byte) (bArr6[11] | (this.symbol << 6));
        byte[] bArr7 = this._sendData;
        bArr7[11] = (byte) (bArr7[11] | (this.change << 7));
        byte[] bArr8 = this._sendData;
        bArr8[12] = (byte) (bArr8[12] | this.shutter);
        byte[] bArr9 = this._sendData;
        bArr9[12] = (byte) (bArr9[12] | (this.focus << 1));
        byte[] bArr10 = this._sendData;
        bArr10[12] = (byte) (bArr10[12] | (this.mode_sw << 2));
        byte[] bArr11 = this._sendData;
        bArr11[12] = (byte) (bArr11[12] | (this.transform_sw << 4));
        byte[] bArr12 = this._sendData;
        bArr12[12] = (byte) (bArr12[12] | (this.gohome << 6));
    }

    private byte[] generateChannelByte() {
        byte[] data = new byte[10];
        byte[] temp = BytesUtil.getBytes(this.channel0 << 0);
        data[0] = (byte) (data[0] | temp[0]);
        data[1] = temp[1];
        byte[] temp2 = BytesUtil.getBytes(this.channel1 << 3);
        data[1] = (byte) (data[1] | temp2[0]);
        data[2] = temp2[1];
        byte[] temp3 = BytesUtil.getBytes(this.channel2 << 6);
        data[2] = (byte) (data[2] | temp3[0]);
        data[3] = temp3[1];
        data[4] = temp3[2];
        byte[] temp4 = BytesUtil.getBytes(this.channel3 << 1);
        data[4] = (byte) (data[4] | temp4[0]);
        data[5] = temp4[1];
        byte[] temp5 = BytesUtil.getBytes(this.channel4 << 4);
        data[5] = (byte) (data[5] | temp5[0]);
        data[6] = temp5[1];
        byte[] temp6 = BytesUtil.getBytes(this.channel5 << 7);
        data[6] = (byte) (data[6] | temp6[0]);
        data[7] = temp6[1];
        data[8] = temp6[2];
        byte[] temp7 = BytesUtil.getBytes(this.channel6 << 2);
        data[8] = (byte) (data[8] | temp7[0]);
        data[9] = temp7[1];
        int offset = 0 + 3 + 3 + 3 + 3 + 3 + 3 + 3;
        data[9] = (byte) (data[9] | (this.ioc_key << 5));
        data[9] = (byte) (data[9] | (this.rs_key << 7));
        return data;
    }

    public byte[] getDataForRecord() {
        return this._sendData;
    }

    public int getRollForRecord() {
        if (this._recData == null) {
            return 1024;
        }
        return BytesUtil.getInt(new byte[]{(byte) (this._recData[1] & 255), (byte) (this._recData[2] & 7)});
    }

    public int getPitchForRecord() {
        if (this._recData == null) {
            return 1024;
        }
        byte[] temp = new byte[2];
        temp[0] = (byte) ((this._recData[2] & 255) >>> 3);
        temp[0] = (byte) (temp[0] | ((this._recData[3] & 255) << 5));
        temp[1] = (byte) ((this._recData[3] & 255) >>> 3);
        temp[1] = (byte) (temp[1] & 7);
        return BytesUtil.getInt(temp);
    }

    public int getThrottleForRecord() {
        if (this._recData == null) {
            return 1024;
        }
        byte[] temp = new byte[2];
        temp[0] = (byte) ((this._recData[3] & 255) >>> 6);
        temp[0] = (byte) (temp[0] | ((this._recData[4] & 255) << 2));
        temp[1] = (byte) ((this._recData[4] & 255) >>> 6);
        temp[1] = (byte) (temp[1] | ((this._recData[5] & 255) << 2));
        temp[1] = (byte) (temp[1] & 7);
        return BytesUtil.getInt(temp);
    }

    public int getYawForRecord() {
        if (this._recData == null) {
            return 1024;
        }
        byte[] temp = new byte[2];
        temp[0] = (byte) ((this._recData[5] & 255) >>> 1);
        temp[0] = (byte) (temp[0] | ((this._recData[6] & 255) << 7));
        temp[1] = (byte) ((this._recData[6] & 255) >>> 1);
        temp[1] = (byte) (temp[1] & 7);
        return BytesUtil.getInt(temp);
    }

    public int getModeForRecord() {
        if (this._recData != null) {
            return (this._recData[12] >> 2) & 3;
        }
        return -1;
    }
}
