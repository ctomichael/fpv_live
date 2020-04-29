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
public class DataSimulatorGetPushConnectHeartPacket extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorGetPushConnectHeartPacket instance;
    private int flag;

    public static synchronized DataSimulatorGetPushConnectHeartPacket getInstance() {
        DataSimulatorGetPushConnectHeartPacket dataSimulatorGetPushConnectHeartPacket;
        synchronized (DataSimulatorGetPushConnectHeartPacket.class) {
            if (instance == null) {
                instance = new DataSimulatorGetPushConnectHeartPacket();
            }
            dataSimulatorGetPushConnectHeartPacket = instance;
        }
        return dataSimulatorGetPushConnectHeartPacket;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] bytes) {
        return true;
    }

    public int getResult() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public DataSimulatorGetPushConnectHeartPacket setFlag(int flag2) {
        this.flag = flag2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SIMULATOR.value();
        pack.cmdId = CmdIdSimulator.CmdIdType.GetPushConnectHeartPacket.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = 0;
        this._sendData[1] = (byte) this.flag;
        this._sendData[2] = 0;
        this._sendData[3] = 0;
    }
}
