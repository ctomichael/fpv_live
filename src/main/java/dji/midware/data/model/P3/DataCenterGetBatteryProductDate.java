package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCenter;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCenterGetBatteryProductDate extends DataBase implements DJIDataSyncListener {
    private static final String TAG = "DataCenterGetBatteryPro";
    private static DataCenterGetBatteryProductDate mInstance = null;

    public static synchronized DataCenterGetBatteryProductDate getInstance() {
        DataCenterGetBatteryProductDate dataCenterGetBatteryProductDate;
        synchronized (DataCenterGetBatteryProductDate.class) {
            if (mInstance == null) {
                mInstance = new DataCenterGetBatteryProductDate();
            }
            dataCenterGetBatteryProductDate = mInstance;
        }
        return dataCenterGetBatteryProductDate;
    }

    public int getProductDate() {
        return ((Integer) get(10, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CENTER.value();
        pack.cmdId = CmdIdCenter.CmdIdType.GetSmartBatteryRequest.value();
        pack.data = getSendData();
        pack.timeOut = 5000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }
}
