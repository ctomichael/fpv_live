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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataOsdSetLED extends DataBase implements DJIDataSyncListener {
    private LEDCtlUnit mBlueUnit = new LEDCtlUnit(1, 0, 32, 1);
    private LEDCtlUnit mGreenUnit = new LEDCtlUnit(1, 0, 32, 1);
    private LEDCtlUnit mRedUnit = new LEDCtlUnit(1, 0, 32, 1);

    @Keep
    public class LEDCtlUnit {
        char isControl;
        char length;
        char repeatcount;
        int sequence;

        public LEDCtlUnit(int isControl2, int sequence2, int length2, int repeatcount2) {
            this.isControl = (char) isControl2;
            this.sequence = sequence2;
            this.length = (char) length2;
            this.repeatcount = (char) repeatcount2;
        }

        public void reset() {
            this.isControl = 1;
            this.sequence = 0;
            this.length = 1;
            this.repeatcount = 1;
        }
    }

    public DataOsdSetLED reset() {
        this.mRedUnit.reset();
        this.mBlueUnit.reset();
        this.mGreenUnit.reset();
        return this;
    }

    public DataOsdSetLED setRedUnit(int isOn, int sequence, int length, int repeatcount) {
        this.mRedUnit.isControl = (char) isOn;
        this.mRedUnit.sequence = sequence;
        this.mRedUnit.length = (char) length;
        this.mRedUnit.repeatcount = (char) repeatcount;
        return this;
    }

    public DataOsdSetLED setBlueUnit(int isOn, int sequence, int length, int repeatcount) {
        this.mBlueUnit.isControl = (char) isOn;
        this.mBlueUnit.sequence = sequence;
        this.mBlueUnit.length = (char) length;
        this.mBlueUnit.repeatcount = (char) repeatcount;
        return this;
    }

    public DataOsdSetLED setGreenUnit(int isOn, int sequence, int length, int repeatcount) {
        this.mGreenUnit.isControl = (char) isOn;
        this.mGreenUnit.sequence = sequence;
        this.mGreenUnit.length = (char) length;
        this.mGreenUnit.repeatcount = (char) repeatcount;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[20];
        this._sendData[1] = (byte) (this.mRedUnit.isControl | (this.mGreenUnit.isControl << 1) | (this.mBlueUnit.isControl << 2));
        System.arraycopy(BytesUtil.getBytes(this.mRedUnit.sequence), 0, this._sendData, 2, 4);
        this._sendData[6] = (byte) this.mRedUnit.length;
        this._sendData[7] = (byte) this.mRedUnit.repeatcount;
        System.arraycopy(BytesUtil.getBytes(this.mGreenUnit.sequence), 0, this._sendData, 8, 4);
        this._sendData[12] = (byte) this.mGreenUnit.length;
        this._sendData[13] = (byte) this.mGreenUnit.repeatcount;
        System.arraycopy(BytesUtil.getBytes(this.mBlueUnit.sequence), 0, this._sendData, 14, 4);
        this._sendData[18] = (byte) this.mBlueUnit.length;
        this._sendData[19] = (byte) this.mBlueUnit.repeatcount;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetLED.value();
        pack.timeOut = 500;
        pack.repeatTimes = 10;
        start(pack, callBack);
    }
}
