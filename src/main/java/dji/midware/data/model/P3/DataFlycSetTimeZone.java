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
public class DataFlycSetTimeZone extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetTimeZone instance = null;
    public int mTimeZone = -4;

    public static synchronized DataFlycSetTimeZone getInstance() {
        DataFlycSetTimeZone dataFlycSetTimeZone;
        synchronized (DataFlycSetTimeZone.class) {
            if (instance == null) {
                instance = new DataFlycSetTimeZone();
            }
            dataFlycSetTimeZone = instance;
        }
        return dataFlycSetTimeZone;
    }

    public DataFlycSetTimeZone setTimeZone(int utc) {
        this.mTimeZone = utc;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        System.arraycopy(BytesUtil.getBytes((short) this.mTimeZone), 0, this._sendData, 0, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetTimeZone.value();
        start(pack, callBack);
    }
}
