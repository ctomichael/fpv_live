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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataOsdGetPushConfig extends DataBase implements DJIDataSyncListener {
    private static DataOsdGetPushConfig instance = null;

    public static synchronized DataOsdGetPushConfig getInstance() {
        DataOsdGetPushConfig dataOsdGetPushConfig;
        synchronized (DataOsdGetPushConfig.class) {
            if (instance == null) {
                instance = new DataOsdGetPushConfig();
            }
            dataOsdGetPushConfig = instance;
        }
        return dataOsdGetPushConfig;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        if (this.pack.cmdType == 0) {
            super.setPushRecData(BytesUtil.arrayRemove(data, 1));
        } else {
            super.setPushRecData(data);
        }
    }

    public int getChannel() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public boolean getIsAuto() {
        return ((Integer) get(2, 1, Integer.class)).intValue() != 0;
    }

    public boolean getIsMaster() {
        return ((Integer) get(3, 1, Integer.class)).intValue() == 1;
    }

    public int getMcs() {
        return ((Integer) get(9, 1, Integer.class)).intValue();
    }

    public boolean getSingleOrDouble() {
        return ((Integer) get(10, 1, Integer.class)).intValue() == 2;
    }

    public int getBandWidthPercent() {
        return ((Integer) get(11, 1, Integer.class)).intValue();
    }

    public int getWorkingFreq() {
        return ((Integer) get(15, 1, Integer.class)).intValue();
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
        pack.cmdId = CmdIdOsd.CmdIdType.GetPushConfig.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
