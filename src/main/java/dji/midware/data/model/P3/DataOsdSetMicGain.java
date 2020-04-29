package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataOsdSetMicGain extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetMicGain instance = null;
    private int mMicGainValue = 0;

    public static synchronized DataOsdSetMicGain getInstance() {
        DataOsdSetMicGain dataOsdSetMicGain;
        synchronized (DataOsdSetMicGain.class) {
            if (instance == null) {
                instance = new DataOsdSetMicGain();
            }
            dataOsdSetMicGain = instance;
        }
        return dataOsdSetMicGain;
    }

    public DataOsdSetMicGain setMicGain(int value) {
        this.mMicGainValue = value;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mMicGainValue;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetMicGain.value();
        start(pack, callBack);
    }
}
