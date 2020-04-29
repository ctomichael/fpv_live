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
public class DataSmartBatteryGetStaticData extends DataBase implements DJIDataSyncListener {
    private static DataSmartBatteryGetStaticData mInstance = null;
    private int index = 0;

    public static DataSmartBatteryGetStaticData getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryGetStaticData();
        }
        return mInstance;
    }

    public DataSmartBatteryGetStaticData setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getIndex() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public long getDesignCapacity() {
        return ((Long) get(2, 4, Long.class)).longValue();
    }

    public int getCycleTimes() {
        return ((Integer) get(6, 2, Integer.class)).intValue();
    }

    public int getDesignVoltage() {
        return ((Integer) get(8, 4, Integer.class)).intValue();
    }

    public int getProductionDate() {
        return ((Integer) get(12, 2, Integer.class)).intValue();
    }

    public int getSerialNumber() {
        return ((Integer) get(14, 2, Integer.class)).intValue();
    }

    public String getCellProvider() {
        return get(16, 5);
    }

    public String getBoardProvider() {
        return get(21, 5);
    }

    public String getDeviceName() {
        return get(26, 5);
    }

    public long getLoaderVer() {
        return ((Long) get(31, 4, Long.class)).longValue();
    }

    public long getAppVer() {
        return ((Long) get(35, 4, Long.class)).longValue();
    }

    public long getLifePercent() {
        return ((Long) get(39, 1, Long.class)).longValue();
    }

    public int getBatteryType() {
        return ((Integer) get(40, 1, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SMARTBATTERY.value();
        pack.cmdId = CmdIdSmartBattery.CmdIdType.GetStaticData.value();
        pack.data = getSendData();
        pack.timeOut = 4000;
        pack.repeatTimes = 5;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[9];
        this._sendData[0] = (byte) this.index;
    }

    @Keep
    public enum SmartBatteryType {
        Others(0),
        MgBattery(81);
        
        int id;

        private SmartBatteryType(int i) {
            this.id = i;
        }

        public static SmartBatteryType find(int i) {
            SmartBatteryType[] values = values();
            for (SmartBatteryType type : values) {
                if (type.id == i) {
                    return type;
                }
            }
            return Others;
        }
    }
}
