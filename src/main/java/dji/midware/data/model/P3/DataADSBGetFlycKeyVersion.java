package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdADS_B;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataADSBGetFlycKeyVersion extends DataBase implements DJIDataSyncListener {
    private static DataADSBGetFlycKeyVersion mInstance = null;
    private int mRepeatTime = -1;

    public static DataADSBGetFlycKeyVersion getInstance() {
        if (mInstance == null) {
            mInstance = new DataADSBGetFlycKeyVersion();
        }
        return mInstance;
    }

    public DataADSBGetFlycKeyVersion setRepeatTime(int repeatTime) {
        this.mRepeatTime = repeatTime;
        return this;
    }

    public boolean isSuccess() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 0;
    }

    public int getRetKeyVersion() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        if (this.mRepeatTime != -1) {
            pack.repeatTimes = this.mRepeatTime;
        }
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.timeOut = 3000;
        pack.cmdSet = CmdSet.ADS_B.value();
        pack.cmdId = CmdIdADS_B.CmdIdType.GetKeyVersion.value();
        start(pack, callBack);
    }
}
