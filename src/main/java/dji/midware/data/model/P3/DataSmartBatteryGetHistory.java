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
public class DataSmartBatteryGetHistory extends DataBase implements DJIDataSyncListener {
    private static DataSmartBatteryGetHistory mInstance = null;
    private int index = 0;

    public static DataSmartBatteryGetHistory getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryGetHistory();
        }
        return mInstance;
    }

    public DataSmartBatteryGetHistory setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getIndex() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public long[] getHistory() {
        long[] history = new long[16];
        for (int i = 0; i < history.length; i++) {
            history[i] = ((Long) get((i * 4) + 2, 4, Long.class)).longValue();
        }
        return history;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SMARTBATTERY.value();
        pack.cmdId = CmdIdSmartBattery.CmdIdType.GetHistory.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[9];
        this._sendData[0] = (byte) this.index;
    }
}
