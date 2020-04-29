package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdSmartBattery;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataSmartBatteryGetSetSelfDischargeDays extends DataBase implements DJIDataSyncListener {
    private static DataSmartBatteryGetSetSelfDischargeDays mInstance = null;
    private int days = 0;
    private int index = 0;
    private boolean set = false;

    public static DataSmartBatteryGetSetSelfDischargeDays getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryGetSetSelfDischargeDays();
        }
        return mInstance;
    }

    public DataSmartBatteryGetSetSelfDischargeDays setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataSmartBatteryGetSetSelfDischargeDays setType(boolean set2) {
        this.set = set2;
        return this;
    }

    public DataSmartBatteryGetSetSelfDischargeDays setDays(int days2) {
        this.days = days2;
        return this;
    }

    public int getIndex() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getDays() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SMARTBATTERY.value();
        pack.cmdId = CmdIdSmartBattery.CmdIdType.GetSetSelfDischargeDays.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 0;
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.index;
        byte[] bArr = this._sendData;
        if (this.set) {
            i = 1;
        }
        bArr[1] = (byte) i;
        this._sendData[2] = (byte) this.days;
    }
}
