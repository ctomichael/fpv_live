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
public class DataRcSetSlavePermission extends DataBase implements DJIDataSyncListener {
    private static DataRcSetSlavePermission instance = null;
    private RcSlavePermission permission = new RcSlavePermission();

    @Keep
    public static class RcSlavePermission {
        public int id;
        public boolean pitch;
        public boolean playback;
        public boolean record;
        public boolean roll;
        public boolean takephoto;
        public boolean yaw;
    }

    public static synchronized DataRcSetSlavePermission getInstance() {
        DataRcSetSlavePermission dataRcSetSlavePermission;
        synchronized (DataRcSetSlavePermission.class) {
            if (instance == null) {
                instance = new DataRcSetSlavePermission();
            }
            dataRcSetSlavePermission = instance;
        }
        return dataRcSetSlavePermission;
    }

    public DataRcSetSlavePermission setId(int id) {
        this.permission.id = id;
        return this;
    }

    public DataRcSetSlavePermission settakephoto(boolean takephoto) {
        this.permission.takephoto = takephoto;
        return this;
    }

    public DataRcSetSlavePermission setrecord(boolean record) {
        this.permission.record = record;
        return this;
    }

    public DataRcSetSlavePermission setplayback(boolean playback) {
        this.permission.playback = playback;
        return this;
    }

    public DataRcSetSlavePermission setpitch(boolean pitch) {
        this.permission.pitch = pitch;
        return this;
    }

    public DataRcSetSlavePermission setroll(boolean roll) {
        this.permission.roll = roll;
        return this;
    }

    public DataRcSetSlavePermission setyaw(boolean yaw) {
        this.permission.yaw = yaw;
        return this;
    }

    private int getInt(boolean b) {
        return b ? 1 : 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        System.arraycopy(BytesUtil.getBytes(this.permission.id), 0, this._sendData, 0, 4);
        this._sendData[4] = (byte) ((getInt(this.permission.takephoto) << 7) | (getInt(this.permission.record) << 6) | (getInt(this.permission.playback) << 5) | (getInt(this.permission.pitch) << 4) | (getInt(this.permission.roll) << 3) | (getInt(this.permission.yaw) << 2));
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetSlavePermission.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
