package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcGetSlaveList extends DataBase implements DJIDataSyncListener {
    private static DataRcGetSlaveList instance = null;

    @Keep
    public static class RcModel {
        public int id;
        public boolean isOpen;
        public String name;
        public int password;
        public boolean pitch;
        public boolean playback;
        public int quality;
        public boolean record;
        public boolean roll;
        public boolean takephoto;
        public boolean yaw;
    }

    public static synchronized DataRcGetSlaveList getInstance() {
        DataRcGetSlaveList dataRcGetSlaveList;
        synchronized (DataRcGetSlaveList.class) {
            if (instance == null) {
                instance = new DataRcGetSlaveList();
            }
            dataRcGetSlaveList = instance;
        }
        return dataRcGetSlaveList;
    }

    public SparseArray<RcModel> getList() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        SparseArray<RcModel> list = new SparseArray<>();
        int size = this._recData.length / 12;
        for (int i = 0; i < size; i++) {
            RcModel item = new RcModel();
            item.id = ((Integer) get(i * 12, 4, Integer.class)).intValue();
            item.name = getUTF8((i * 12) + 4, 6);
            item.quality = ((Integer) get((i * 12) + 10, 1, Integer.class)).intValue();
            if (((((Integer) get((i * 12) + 11, 1, Integer.class)).intValue() >> 7) & 1) == 1) {
                z = true;
            } else {
                z = false;
            }
            item.takephoto = z;
            if (((((Integer) get((i * 12) + 11, 1, Integer.class)).intValue() >> 6) & 1) == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            item.record = z2;
            if (((((Integer) get((i * 12) + 11, 1, Integer.class)).intValue() >> 5) & 1) == 1) {
                z3 = true;
            } else {
                z3 = false;
            }
            item.playback = z3;
            if (((((Integer) get((i * 12) + 11, 1, Integer.class)).intValue() >> 4) & 1) == 1) {
                z4 = true;
            } else {
                z4 = false;
            }
            item.pitch = z4;
            if (((((Integer) get((i * 12) + 11, 1, Integer.class)).intValue() >> 3) & 1) == 1) {
                z5 = true;
            } else {
                z5 = false;
            }
            item.roll = z5;
            if (((((Integer) get((i * 12) + 11, 1, Integer.class)).intValue() >> 2) & 1) == 1) {
                z6 = true;
            } else {
                z6 = false;
            }
            item.yaw = z6;
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
        pack.cmdId = CmdIdRc.CmdIdType.GetSlaveList.value();
        start(pack, callBack);
    }
}
