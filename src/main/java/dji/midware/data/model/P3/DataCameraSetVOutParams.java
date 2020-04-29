package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraSetVOutParams extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetVOutParams instance = null;
    private boolean isSettingFov = false;
    private boolean isSettingHDMI = false;
    private boolean isSettingLCD = false;
    private boolean isSettingStream = false;
    private LCDFormat mLCDParam = LCDFormat.AUTO_NO_GLASS_CONNECTED;
    private int mStream = 1;

    public static synchronized DataCameraSetVOutParams getInstance() {
        DataCameraSetVOutParams dataCameraSetVOutParams;
        synchronized (DataCameraSetVOutParams.class) {
            if (instance == null) {
                instance = new DataCameraSetVOutParams();
            }
            dataCameraSetVOutParams = instance;
        }
        return dataCameraSetVOutParams;
    }

    public DataCameraSetVOutParams setIsSettingLCD(boolean _settingLCD) {
        this.isSettingLCD = _settingLCD;
        return this;
    }

    public DataCameraSetVOutParams setStream(int stream) {
        this.isSettingStream = true;
        this.mStream = stream;
        return this;
    }

    public DataCameraSetVOutParams setLCDFormat(LCDFormat format) {
        this.mLCDParam = format;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetVOutParams.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[6];
        this._sendData[0] = 0;
        if (this.isSettingLCD) {
            this._sendData[0] = 1;
        }
        if (this.isSettingHDMI) {
            byte[] bArr = this._sendData;
            bArr[0] = (byte) (bArr[0] | 2);
        }
        if (this.isSettingStream) {
            byte[] bArr2 = this._sendData;
            bArr2[0] = (byte) (bArr2[0] | 4);
        }
        if (this.isSettingFov) {
            byte[] bArr3 = this._sendData;
            bArr3[0] = (byte) (bArr3[0] | 8);
        }
        if (this.isSettingLCD) {
            this._sendData[1] = (byte) this.mLCDParam.ordinal();
        } else {
            this._sendData[1] = 0;
        }
        this._sendData[2] = 0;
        if (this.isSettingStream) {
            this._sendData[3] = (byte) this.mStream;
        } else {
            this._sendData[3] = 0;
        }
    }

    @Keep
    public enum LCDFormat {
        AUTO(0),
        R1280x720_FPS30(1),
        R1280x720_FPS60(2),
        R1920x1080_FPS30(3),
        R1920x1080_FPS60(4),
        AUTO_GLASS_CONNECTED(5),
        AUTO_NO_GLASS_CONNECTED(6),
        HD_FORMAT(7),
        OTHER(100);
        
        private final int data;

        private LCDFormat(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static LCDFormat find(int b) {
            LCDFormat result = AUTO_NO_GLASS_CONNECTED;
            LCDFormat[] values = values();
            for (LCDFormat tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
