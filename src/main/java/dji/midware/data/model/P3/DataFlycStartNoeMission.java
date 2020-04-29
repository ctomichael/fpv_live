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
public class DataFlycStartNoeMission extends DataBase implements DJIDataSyncListener {
    private static DataFlycStartNoeMission instance;
    private float height;

    public static synchronized DataFlycStartNoeMission getInstance() {
        DataFlycStartNoeMission dataFlycStartNoeMission;
        synchronized (DataFlycStartNoeMission.class) {
            if (instance == null) {
                instance = new DataFlycStartNoeMission();
            }
            dataFlycStartNoeMission = instance;
        }
        return dataFlycStartNoeMission;
    }

    public DataFlycStartNoeMission setHeight(float height2) {
        this.height = height2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.StartNoeMission.value();
        super.start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        System.arraycopy(BytesUtil.getBytes(this.height), 0, this._sendData, 0, 4);
    }
}
