package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdSmartBattery;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.Pack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataSmartBatteryGetPushCellVoltage extends DataBase implements DJIDataSyncListener {
    private static DataSmartBatteryGetPushCellVoltage mInstance = null;
    private int dataOffset = 0;
    private int index = 0;
    private boolean isRequestPush = false;
    private boolean isStopPush = true;
    private int pushFreq = 1;

    public static DataSmartBatteryGetPushCellVoltage getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryGetPushCellVoltage();
        }
        return mInstance;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataSmartBatteryGetPushCellVoltage setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public DataSmartBatteryGetPushCellVoltage setRequestPush(boolean push) {
        this.isRequestPush = push;
        this.dataOffset = this.isRequestPush ? 0 : 1;
        return this;
    }

    public DataSmartBatteryGetPushCellVoltage setContinuePush(boolean stop) {
        this.isStopPush = stop;
        return this;
    }

    public DataSmartBatteryGetPushCellVoltage setPushFreq(int freq) {
        this.pushFreq = freq;
        return this;
    }

    public int getIndex() {
        return ((Integer) get(this.dataOffset + 0, 1, Integer.class)).intValue();
    }

    public int getCells() {
        return ((Integer) get(this.dataOffset + 1, 1, Integer.class)).intValue();
    }

    public int[] getVoltages() {
        int len = ((Integer) get(this.dataOffset + 1, 1, Integer.class)).intValue();
        if (len <= 0) {
            return null;
        }
        int[] voltages = new int[len];
        for (int i = 0; i < voltages.length; i++) {
            voltages[i] = ((Integer) get((i * 2) + 2 + this.dataOffset, 2, Integer.class)).intValue();
        }
        return voltages;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES_BY_PUSH.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SMARTBATTERY.value();
        pack.cmdId = CmdIdSmartBattery.CmdIdType.GetPushCellVoltage.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i;
        int i2 = 1;
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.index;
        byte[] bArr = this._sendData;
        if (this.isRequestPush) {
            i = 1;
        } else {
            i = 0;
        }
        bArr[1] = (byte) i;
        byte[] bArr2 = this._sendData;
        if (!this.isStopPush) {
            i2 = 0;
        }
        bArr2[2] = (byte) i2;
        this._sendData[3] = (byte) this.pushFreq;
    }

    @Nullable
    public byte[] getDataForRecord() {
        if (this._recData == null || this._recData.length <= 1) {
            return null;
        }
        byte[] buffer = new byte[(this._recData.length - 1)];
        System.arraycopy(this._recData, 1, buffer, 0, buffer.length);
        return buffer;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (pack != null && pack.cmdType == 0 && pack.data != null && pack.data[0] == 0) {
            super.setPushRecPack(pack);
        }
    }
}
