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
public class DataSmartBatteryGetMultBatteryInfo extends DataBase implements DJIDataSyncListener {
    public static int DATA_TYPE_IOC = 0;
    public static int DATA_TYPE_VOLTAGE = 1;
    private static DataSmartBatteryGetMultBatteryInfo mInstance = null;

    public static DataSmartBatteryGetMultBatteryInfo getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryGetMultBatteryInfo();
        }
        return mInstance;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getType() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getNum() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int[] getValues() {
        int num = getNum();
        if (num <= 0) {
            return null;
        }
        int[] values = new int[num];
        for (int i = 0; i < num; i++) {
            values[i] = ((Integer) get((i * 4) + 3, 4, Integer.class)).intValue();
        }
        return values;
    }

    public int getValue(int index) {
        int num;
        if (index < 0 || (num = getNum()) <= 0 || index >= num) {
            return 0;
        }
        int[] values = new int[num];
        for (int i = 0; i < num; i++) {
            values[i] = ((Integer) get((i * 4) + 3, 4, Integer.class)).intValue();
        }
        return values[index];
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SMARTBATTERY.value();
        pack.cmdId = CmdIdSmartBattery.CmdIdType.GetMultBatteryInfo.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
