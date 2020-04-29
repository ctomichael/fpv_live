package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;

@Keep
@EXClassNullAway
public class DataBatteryActiveStatus extends DataAbstractGetPushActiveStatus {
    private static DataBatteryActiveStatus instance = null;
    private int mIndexMultiBattery = 0;

    public static synchronized DataBatteryActiveStatus getInstance() {
        DataBatteryActiveStatus dataBatteryActiveStatus;
        synchronized (DataBatteryActiveStatus.class) {
            if (instance == null) {
                instance = new DataBatteryActiveStatus();
            }
            dataBatteryActiveStatus = instance;
        }
        return dataBatteryActiveStatus;
    }

    public DataBatteryActiveStatus setIndexMultiBattery(int index) {
        this.mIndexMultiBattery = index;
        return this;
    }

    public int getSenderIndex() {
        return this.pack.senderId;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.receiverId = this.mIndexMultiBattery;
        this.mIndexMultiBattery = 0;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.ActiveStatus.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}
