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
public class DataSmartBatteryGetPushDynamicData extends DataBase implements DJIDataSyncListener {
    private static DataSmartBatteryGetPushDynamicData mInstance = null;
    private int dataOffset = 0;
    private int index = 0;
    private boolean isRequestPush = false;
    private boolean isStopPush = true;
    private int pushFreq = 1;

    public static DataSmartBatteryGetPushDynamicData getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryGetPushDynamicData();
        }
        return mInstance;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataSmartBatteryGetPushDynamicData setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public DataSmartBatteryGetPushDynamicData setRequestPush(boolean push) {
        this.isRequestPush = push;
        this.dataOffset = this.isRequestPush ? 0 : 1;
        return this;
    }

    public DataSmartBatteryGetPushDynamicData setContinuePush(boolean stop) {
        this.isStopPush = stop;
        return this;
    }

    public DataSmartBatteryGetPushDynamicData setPushFreq(int freq) {
        this.pushFreq = freq;
        return this;
    }

    public int getIndex() {
        return ((Integer) get(this.dataOffset + 0, 1, Integer.class)).intValue();
    }

    public int getVoltage() {
        return ((Integer) get(this.dataOffset + 1, 4, Integer.class)).intValue();
    }

    public int getCurrent() {
        return ((Integer) get(this.dataOffset + 5, 4, Integer.class)).intValue();
    }

    public int getFullCapacity() {
        return ((Integer) get(this.dataOffset + 9, 4, Integer.class)).intValue();
    }

    public int getRemainCapacity() {
        return ((Integer) get(this.dataOffset + 13, 4, Integer.class)).intValue();
    }

    public int getTemperature() {
        return ((Short) get(this.dataOffset + 17, 2, Short.class)).shortValue();
    }

    public int getCellSize() {
        return ((Integer) get(this.dataOffset + 19, 1, Integer.class)).intValue();
    }

    public int getRelativeCapacityPercentage() {
        return ((Integer) get(this.dataOffset + 20, 1, Integer.class)).intValue();
    }

    public long getStatus() {
        return ((Long) get(this.dataOffset + 21, 8, Long.class)).longValue();
    }

    public int getSOHState() {
        return (((Integer) get(this.dataOffset + 25, 1, Integer.class)).intValue() & 192) >>> 6;
    }

    public int getCycleLimit() {
        return (((Short) get(this.dataOffset + 26, 1, Short.class)).shortValue() & 63) * 10;
    }

    public boolean isBatteryEmbedOk() {
        return (((Short) get(this.dataOffset + 27, 2, Short.class)).shortValue() >> 5) == 0;
    }

    public long getVersion() {
        return ((Long) get(this.dataOffset + 29, 1, Long.class)).longValue();
    }

    public int getBatteryHeatState() {
        return ((Integer) get(this.dataOffset + 32, 2, Integer.class)).intValue();
    }

    public boolean isSentByBaseStation() {
        if (this.pack == null || this.pack.senderType != DeviceType.GPS.value()) {
            return false;
        }
        return true;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES_BY_PUSH.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SMARTBATTERY.value();
        pack.cmdId = CmdIdSmartBattery.CmdIdType.GetPushDynamicData.value();
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
