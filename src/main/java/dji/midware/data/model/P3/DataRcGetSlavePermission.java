package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcSetSlavePermission;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcGetSlavePermission extends DataBase implements DJIDataSyncListener {
    private static DataRcGetSlavePermission instance = null;
    private SparseArray<DataRcSetSlavePermission.RcSlavePermission> result = new SparseArray<>();

    public static synchronized DataRcGetSlavePermission getInstance() {
        DataRcGetSlavePermission dataRcGetSlavePermission;
        synchronized (DataRcGetSlavePermission.class) {
            if (instance == null) {
                instance = new DataRcGetSlavePermission();
            }
            dataRcGetSlavePermission = instance;
        }
        return dataRcGetSlavePermission;
    }

    public void setRecData(byte[] data) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        super.setRecData(data);
        if (this._recData != null) {
            int num = this._recData.length / 5;
            for (int i = 0; i < num; i++) {
                int id = ((Integer) get(i * 5, 4, Integer.class)).intValue();
                DataRcSetSlavePermission.RcSlavePermission permission = this.result.get(id);
                if (permission == null) {
                    permission = new DataRcSetSlavePermission.RcSlavePermission();
                }
                int value = ((Integer) get((i * 5) + 4, 1, Integer.class)).intValue();
                if (((value >> 7) & 1) == 1) {
                    z = true;
                } else {
                    z = false;
                }
                permission.takephoto = z;
                if (((value >> 6) & 1) == 1) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                permission.record = z2;
                if (((value >> 5) & 1) == 1) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                permission.playback = z3;
                if (((value >> 4) & 1) == 1) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                permission.pitch = z4;
                if (((value >> 3) & 1) == 1) {
                    z5 = true;
                } else {
                    z5 = false;
                }
                permission.roll = z5;
                if (((value >> 2) & 1) == 1) {
                    z6 = true;
                } else {
                    z6 = false;
                }
                permission.yaw = z6;
                this.result.put(id, permission);
            }
        }
    }

    public DataRcSetSlavePermission.RcSlavePermission getPermission(int id) {
        if (this.result == null) {
            return null;
        }
        return this.result.get(id);
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
        pack.cmdId = CmdIdRc.CmdIdType.GetSlavePermission.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
