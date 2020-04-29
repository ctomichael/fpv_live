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
public class DataOsdGetSdrConfig extends DataBase implements DJIDataSyncListener {
    private static DataOsdGetSdrConfig instance = null;

    public static synchronized DataOsdGetSdrConfig getInstance() {
        DataOsdGetSdrConfig dataOsdGetSdrConfig;
        synchronized (DataOsdGetSdrConfig.class) {
            if (instance == null) {
                instance = new DataOsdGetSdrConfig();
            }
            dataOsdGetSdrConfig = instance;
        }
        return dataOsdGetSdrConfig;
    }

    public int getSelectionMode() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getBandwidthType() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getSdrNf() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public int getSdrBand() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public boolean getIsSupportDualBand() {
        return ((Integer) get(5, 1, Integer.class)).intValue() == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.GetSdrConfig.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
