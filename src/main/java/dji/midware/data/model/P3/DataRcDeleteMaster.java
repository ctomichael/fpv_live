package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
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
public class DataRcDeleteMaster extends DataBase implements DJIDataSyncListener {
    private static DataRcDeleteMaster instance = null;
    private int ID;

    public static synchronized DataRcDeleteMaster getInstance() {
        DataRcDeleteMaster dataRcDeleteMaster;
        synchronized (DataRcDeleteMaster.class) {
            if (instance == null) {
                instance = new DataRcDeleteMaster();
            }
            dataRcDeleteMaster = instance;
        }
        return dataRcDeleteMaster;
    }

    public DataRcDeleteMaster setID(int ID2) {
        this.ID = ID2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.DeleteMaster.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        System.arraycopy(BytesUtil.getBytes(this.ID), 0, this._sendData, 0, 4);
    }
}
