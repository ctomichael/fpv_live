package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.SparseArray;
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
public class DataRcGetSearchMasters extends DataBase implements DJIDataSyncListener {
    private static DataRcGetSearchMasters instance = null;

    public static synchronized DataRcGetSearchMasters getInstance() {
        DataRcGetSearchMasters dataRcGetSearchMasters;
        synchronized (DataRcGetSearchMasters.class) {
            if (instance == null) {
                instance = new DataRcGetSearchMasters();
            }
            dataRcGetSearchMasters = instance;
        }
        return dataRcGetSearchMasters;
    }

    public SparseArray<DataRcGetSlaveList.RcModel> getList() {
        SparseArray<DataRcGetSlaveList.RcModel> list = new SparseArray<>();
        int size = this._recData.length / 11;
        for (int i = 0; i < size; i++) {
            DataRcGetSlaveList.RcModel item = new DataRcGetSlaveList.RcModel();
            item.id = ((Integer) get(i * 11, 4, Integer.class)).intValue();
            item.name = BytesUtil.getStringUTF8(BytesUtil.readBytes(this._recData, (i * 11) + 4, 6));
            item.quality = ((Integer) get((i * 11) + 10, 1, Integer.class)).intValue();
            list.put(i, item);
        }
        return list;
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
        pack.cmdId = CmdIdRc.CmdIdType.GetSearchMasters.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
