package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
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
public class DataFlycHotPointResetRadius extends DataBase implements DJIDataSyncListener {
    private static DataFlycHotPointResetRadius instance = null;
    private float radius = 0.0f;

    public static synchronized DataFlycHotPointResetRadius getInstance() {
        DataFlycHotPointResetRadius dataFlycHotPointResetRadius;
        synchronized (DataFlycHotPointResetRadius.class) {
            if (instance == null) {
                instance = new DataFlycHotPointResetRadius();
            }
            dataFlycHotPointResetRadius = instance;
        }
        return dataFlycHotPointResetRadius;
    }

    public DataFlycHotPointResetRadius setRadius(float radius2) {
        this.radius = radius2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.HotPointResetRadius.value();
        super.start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        System.arraycopy(BytesUtil.getBytes(this.radius), 0, this._sendData, 0, 4);
    }
}
