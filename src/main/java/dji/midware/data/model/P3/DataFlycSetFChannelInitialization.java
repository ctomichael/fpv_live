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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycSetFChannelInitialization extends DataBase implements DJIDataSyncListener {
    private short frequency;
    private int initValue;
    private FChannelMode mode;
    private int portIndex = 0;

    public DataFlycSetFChannelInitialization setPortIndex(int portIndex2) {
        this.portIndex = portIndex2;
        return this;
    }

    public DataFlycSetFChannelInitialization setMode(FChannelMode mode2) {
        this.mode = mode2;
        return this;
    }

    public DataFlycSetFChannelInitialization setInitValue(int initValue2) {
        this.initValue = initValue2;
        return this;
    }

    public DataFlycSetFChannelInitialization setFrequency(short frequency2) {
        this.frequency = frequency2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.InitializeOnboardFChannel.value();
        pack.data = getSendData();
        pack.timeOut = 1000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[8];
        this._sendData[0] = (byte) this.portIndex;
        int index = 0 + 1;
        this._sendData[index] = (byte) this.mode.value();
        int index2 = index + 1;
        System.arraycopy(BytesUtil.getBytes(this.initValue), 0, this._sendData, index2, 4);
        System.arraycopy(BytesUtil.getBytes(this.frequency), 0, this._sendData, index2 + 4, 2);
    }

    @Keep
    public enum FChannelMode {
        PWM_OUTPUT(0),
        PWM_INPUT(1),
        GPIO_OUTPUT(2),
        GPIO_INPUT(3),
        ADC_INPUT(4),
        UNKNOWN(255);
        
        private final int data;

        private FChannelMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FChannelMode find(int b) {
            FChannelMode result = UNKNOWN;
            FChannelMode[] values = values();
            for (FChannelMode tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
