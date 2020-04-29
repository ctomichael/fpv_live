package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataOsdGetSdrUplinkBandwidth extends DataBase implements DJIDataSyncListener {
    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getRemainListCnt() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getTbSize() {
        return ((Integer) get(1, 2, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.GetSdrUplinkBandwidth.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
