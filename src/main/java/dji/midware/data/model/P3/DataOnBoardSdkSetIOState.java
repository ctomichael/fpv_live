package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOnBoardSDK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataOnBoardSdkSetIOState extends DataBase implements DJIDataSyncListener {
    private CtrlAction action = CtrlAction.OTHER;
    private int dutyRatio = 0;
    private int frequency = 0;
    private GPIOMode gpiOMode = GPIOMode.OTHER;
    private int iOIndex = 0;
    private int initDutyRatio = 0;
    private IOProperty property = IOProperty.OTHER;
    private boolean setHighElectricLevel = false;

    public DataOnBoardSdkSetIOState setIOIndex(int iOIndex2) {
        this.iOIndex = iOIndex2;
        return this;
    }

    public DataOnBoardSdkSetIOState setHighElectricLevel(boolean setHighElectricLevel2) {
        this.setHighElectricLevel = setHighElectricLevel2;
        return this;
    }

    public DataOnBoardSdkSetIOState setAction(CtrlAction action2) {
        this.action = action2;
        return this;
    }

    public DataOnBoardSdkSetIOState setGPIOMode(GPIOMode ioMode) {
        this.gpiOMode = ioMode;
        return this;
    }

    public DataOnBoardSdkSetIOState setProperty(IOProperty property2) {
        this.property = property2;
        return this;
    }

    public DataOnBoardSdkSetIOState setInitDutyRatio(int initDutyRatio2) {
        this.initDutyRatio = initDutyRatio2;
        return this;
    }

    public void setFrequency(int frequency2) {
        this.frequency = frequency2;
    }

    public void setDutyRatio(int dutyRatio2) {
        this.dutyRatio = dutyRatio2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[6];
        this._sendData[0] = 0;
        this._sendData[1] = (byte) (this.iOIndex & 15);
        byte[] bArr = this._sendData;
        bArr[1] = (byte) (bArr[1] | ((byte) ((this.action.value() & 3) << 4)));
        byte[] bArr2 = this._sendData;
        bArr2[1] = (byte) (bArr2[1] | ((byte) ((this.property.value() & 3) << 6)));
        byte[] params = new byte[4];
        if (CtrlAction.Initiate.equals(this.action)) {
            if (IOProperty.GPIO.equals(this.property)) {
                params[0] = (byte) this.gpiOMode.value();
                params[3] = 0;
                params[2] = 0;
                params[1] = 0;
            } else if (IOProperty.PWM.equals(this.property)) {
                params = BytesUtil.getBytes((this.frequency << 10) | (this.initDutyRatio & DJIDiagnosticsError.Camera.INTERNAL_STORAGE_INVALID));
            }
        } else if (CtrlAction.SetParams.equals(this.action)) {
            if (IOProperty.GPIO.equals(this.property)) {
                if (this.setHighElectricLevel) {
                    params[0] = 1;
                } else {
                    params[0] = 0;
                }
                params[3] = 0;
                params[2] = 0;
                params[1] = 0;
            } else if (IOProperty.PWM.equals(this.property)) {
                params = BytesUtil.getBytes(this.dutyRatio & DJIDiagnosticsError.Camera.INTERNAL_STORAGE_INVALID);
            }
        }
        BytesUtil.arraycopy(params, this._sendData, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OnboardSDK.value();
        pack.cmdId = CmdIdOnBoardSDK.CmdIdType.setExternalIO.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }

    @Keep
    public enum CtrlAction {
        Initiate(0),
        SetParams(1),
        OTHER(255);
        
        private int data;

        private CtrlAction(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CtrlAction find(int b) {
            CtrlAction result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum IOProperty {
        GPIO(0),
        PWM(1),
        OTHER(255);
        
        private int data;

        private IOProperty(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static IOProperty find(int b) {
            IOProperty result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum GPIOMode {
        FloatInput(0),
        PullUpInput(1),
        PullDownInput(2),
        PushPullOutput(3),
        OTHER(255);
        
        private int data;

        private GPIOMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GPIOMode find(int b) {
            GPIOMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
