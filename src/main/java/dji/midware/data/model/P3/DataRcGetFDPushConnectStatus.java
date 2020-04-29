package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcGetRcRole;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcGetFDPushConnectStatus extends DataBase implements DJIDataSyncListener {
    private static DataRcGetFDPushConnectStatus instance = null;

    public static synchronized DataRcGetFDPushConnectStatus getInstance() {
        DataRcGetFDPushConnectStatus dataRcGetFDPushConnectStatus;
        synchronized (DataRcGetFDPushConnectStatus.class) {
            if (instance == null) {
                instance = new DataRcGetFDPushConnectStatus();
                instance.isNeedPushLosed = true;
            }
            dataRcGetFDPushConnectStatus = instance;
        }
        return dataRcGetFDPushConnectStatus;
    }

    public DataRcGetRcRole.RcRole getRole() {
        return DataRcGetRcRole.RcRole.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public int getMainControlStatus() {
        return ((Integer) get(1, 1, Integer.class)).intValue() & 15;
    }

    public int getMainDeviceType() {
        return (((Integer) get(1, 1, Integer.class)).intValue() >> 4) & 15;
    }

    public int getSlaveControlStatus() {
        return ((Integer) get(2, 1, Integer.class)).intValue() & 15;
    }

    public int getSlaveDeviceType() {
        return (((Integer) get(2, 1, Integer.class)).intValue() >> 4) & 15;
    }

    public int getCurTrainer() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getControlNums() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetFDPushConnectStatus.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
