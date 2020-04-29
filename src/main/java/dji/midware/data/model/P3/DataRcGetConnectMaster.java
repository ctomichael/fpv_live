package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcGetSlaveList;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataRcGetConnectMaster extends DataBase implements DJIDataSyncListener {
    private static DataRcGetConnectMaster instance = null;

    public static synchronized DataRcGetConnectMaster getInstance() {
        DataRcGetConnectMaster dataRcGetConnectMaster;
        synchronized (DataRcGetConnectMaster.class) {
            if (instance == null) {
                instance = new DataRcGetConnectMaster();
            }
            dataRcGetConnectMaster = instance;
        }
        return dataRcGetConnectMaster;
    }

    public DataRcGetSlaveList.RcModel getMaster() {
        DataRcGetSlaveList.RcModel master = new DataRcGetSlaveList.RcModel();
        master.id = ((Integer) get(0, 4, Integer.class)).intValue();
        master.name = BytesUtil.getString(BytesUtil.readBytes(this._recData, 4, 6));
        master.password = ((Integer) get(10, 2, Integer.class)).intValue();
        return master;
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
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetConnectMaster.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
