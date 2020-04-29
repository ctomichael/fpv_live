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

@Keep
@EXClassNullAway
public class DataSingleGetMainCameraBandwidthPercent extends DataBase implements DJIDataSyncListener {
    private static DataSingleGetMainCameraBandwidthPercent instance = null;

    public static synchronized DataSingleGetMainCameraBandwidthPercent getInstance() {
        DataSingleGetMainCameraBandwidthPercent dataSingleGetMainCameraBandwidthPercent;
        synchronized (DataSingleGetMainCameraBandwidthPercent.class) {
            if (instance == null) {
                instance = new DataSingleGetMainCameraBandwidthPercent();
            }
            dataSingleGetMainCameraBandwidthPercent = instance;
        }
        return dataSingleGetMainCameraBandwidthPercent;
    }

    public int getPercent() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverId = 7;
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.GetMainCameraBandwidthPercent.value();
        start(pack, callBack);
    }
}
