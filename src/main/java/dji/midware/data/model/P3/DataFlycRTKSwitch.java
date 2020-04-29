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
public class DataFlycRTKSwitch extends DataBase implements DJIDataSyncListener {
    private static DataFlycRTKSwitch instance = null;
    private boolean isRtkEnabled;

    public static synchronized DataFlycRTKSwitch getInstance() {
        DataFlycRTKSwitch dataFlycRTKSwitch;
        synchronized (DataFlycRTKSwitch.class) {
            if (instance == null) {
                instance = new DataFlycRTKSwitch();
            }
            dataFlycRTKSwitch = instance;
        }
        return dataFlycRTKSwitch;
    }

    public DataFlycRTKSwitch setRtkEnabled(boolean enable) {
        this.isRtkEnabled = enable;
        return this;
    }

    private DataFlycRTKSwitch() {
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 0;
        byte[] bArr = new byte[2];
        bArr[0] = 3;
        if (this.isRtkEnabled) {
            i = 1;
        }
        bArr[1] = (byte) i;
        this._sendData = bArr;
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.RTKSwitch.value();
        super.start(pack);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.RTKSwitch.value();
        super.start(pack, callBack);
    }
}
