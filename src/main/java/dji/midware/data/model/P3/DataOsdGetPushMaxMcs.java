package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;

@Keep
@EXClassNullAway
public class DataOsdGetPushMaxMcs extends DataBase implements DJIDataAsync2Listener {
    private static DataOsdGetPushMaxMcs instance = null;

    public static synchronized DataOsdGetPushMaxMcs getInstance() {
        DataOsdGetPushMaxMcs dataOsdGetPushMaxMcs;
        synchronized (DataOsdGetPushMaxMcs.class) {
            if (instance == null) {
                instance = new DataOsdGetPushMaxMcs();
            }
            dataOsdGetPushMaxMcs = instance;
        }
        return dataOsdGetPushMaxMcs;
    }

    public int getMaxMcs() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.ACK.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.GetPushMaxMcs.value();
        start(pack);
    }
}
