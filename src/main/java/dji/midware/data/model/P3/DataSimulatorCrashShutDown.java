package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdSimulator;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataSimulatorCrashShutDown extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorCrashShutDown instance;
    private int mFlag;

    public static synchronized DataSimulatorCrashShutDown getInstance() {
        DataSimulatorCrashShutDown dataSimulatorCrashShutDown;
        synchronized (DataSimulatorCrashShutDown.class) {
            if (instance == null) {
                instance = new DataSimulatorCrashShutDown();
            }
            dataSimulatorCrashShutDown = instance;
        }
        return dataSimulatorCrashShutDown;
    }

    public DataSimulatorCrashShutDown needCrashShutDown(boolean bool) {
        if (bool) {
            this.mFlag = 1;
        } else {
            this.mFlag = 0;
        }
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SIMULATOR.value();
        pack.cmdId = CmdIdSimulator.CmdIdType.CrashShutDown.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mFlag;
    }
}
