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

@Keep
@EXClassNullAway
public class DataFlycSmartAck extends DataBase implements DJIDataSyncListener {
    private static DataFlycSmartAck mInstance = null;
    private byte mAck = 0;

    public static synchronized DataFlycSmartAck getInstance() {
        DataFlycSmartAck dataFlycSmartAck;
        synchronized (DataFlycSmartAck.class) {
            if (mInstance == null) {
                mInstance = new DataFlycSmartAck();
            }
            dataFlycSmartAck = mInstance;
        }
        return dataFlycSmartAck;
    }

    public DataFlycSmartAck setAck(byte ack) {
        this.mAck = ack;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = this.mAck;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SmartLowBatteryAck.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
