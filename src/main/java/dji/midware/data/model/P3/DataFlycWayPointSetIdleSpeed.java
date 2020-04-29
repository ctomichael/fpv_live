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
public class DataFlycWayPointSetIdleSpeed extends DataBase implements DJIDataSyncListener {
    private static DataFlycWayPointSetIdleSpeed instance = null;
    private float idleSpeed = 0.0f;

    public static synchronized DataFlycWayPointSetIdleSpeed getInstance() {
        DataFlycWayPointSetIdleSpeed dataFlycWayPointSetIdleSpeed;
        synchronized (DataFlycWayPointSetIdleSpeed.class) {
            if (instance == null) {
                instance = new DataFlycWayPointSetIdleSpeed();
            }
            dataFlycWayPointSetIdleSpeed = instance;
        }
        return dataFlycWayPointSetIdleSpeed;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public float getIdleSpeed() {
        return ((Float) get(1, 4, Float.class)).floatValue();
    }

    public void setIdleSpeed(float idleSpeed2) {
        this.idleSpeed = idleSpeed2;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.WayPointMissionSetIdleSpeed.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        System.arraycopy(BytesUtil.getBytes(this.idleSpeed), 0, this._sendData, 0, 4);
    }
}
