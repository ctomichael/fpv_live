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
public class DataFlycSetBatteryValidStste extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetBatteryValidStste instance;
    private boolean isBatteryValid = true;

    public static synchronized DataFlycSetBatteryValidStste getInstance() {
        DataFlycSetBatteryValidStste dataFlycSetBatteryValidStste;
        synchronized (DataFlycSetBatteryValidStste.class) {
            if (instance == null) {
                instance = new DataFlycSetBatteryValidStste();
            }
            dataFlycSetBatteryValidStste = instance;
        }
        return dataFlycSetBatteryValidStste;
    }

    public DataFlycSetBatteryValidStste setIsBatteryValid(boolean isBatteryValid2) {
        this.isBatteryValid = isBatteryValid2;
        return this;
    }

    private DataFlycSetBatteryValidStste() {
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 2;
        byte[] bArr = new byte[2];
        bArr[0] = 0;
        if (this.isBatteryValid) {
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
        pack.cmdId = CmdIdFlyc.CmdIdType.SetBatteryValidStste.value();
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
        pack.cmdId = CmdIdFlyc.CmdIdType.SetBatteryValidStste.value();
        super.start(pack, callBack);
    }
}
