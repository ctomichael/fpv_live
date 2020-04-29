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

@Keep
@EXClassNullAway
public class DataRcSetWheelGain extends DataBase implements DJIDataSyncListener {
    private static DataRcSetWheelGain instance = null;
    private int gain;

    public static synchronized DataRcSetWheelGain getInstance() {
        DataRcSetWheelGain dataRcSetWheelGain;
        synchronized (DataRcSetWheelGain.class) {
            if (instance == null) {
                instance = new DataRcSetWheelGain();
            }
            dataRcSetWheelGain = instance;
        }
        return dataRcSetWheelGain;
    }

    public DataRcSetWheelGain setGain(int gain2) {
        this.gain = gain2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.gain;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetWheelGain.value();
        start(pack, callBack);
    }
}
