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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataSimulatorSetGetAirParams extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorSetGetAirParams instance;
    private float mDensity;
    private int mFlag;
    private float mResistanceCoef;

    public static synchronized DataSimulatorSetGetAirParams getInstance() {
        DataSimulatorSetGetAirParams dataSimulatorSetGetAirParams;
        synchronized (DataSimulatorSetGetAirParams.class) {
            if (instance == null) {
                instance = new DataSimulatorSetGetAirParams();
            }
            dataSimulatorSetGetAirParams = instance;
        }
        return dataSimulatorSetGetAirParams;
    }

    public DataSimulatorSetGetAirParams setDensity(int density) {
        this.mDensity = (float) density;
        return this;
    }

    public DataSimulatorSetGetAirParams setResistanceCoef(int resistance) {
        this.mResistanceCoef = (float) resistance;
        return this;
    }

    public DataSimulatorSetGetAirParams setAckFlag(boolean ackFlag) {
        if (ackFlag) {
            this.mFlag |= 1;
        } else {
            this.mFlag |= 0;
        }
        return this;
    }

    public DataSimulatorSetGetAirParams setInitFlag(boolean initFlag) {
        if (initFlag) {
            this.mFlag |= 2;
        } else {
            this.mFlag |= 0;
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
        pack.cmdId = CmdIdSimulator.CmdIdType.SetGetAirParams.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[9];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mDensity), 0, this._sendData, 0, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mResistanceCoef), 0, this._sendData, 4, 4);
        this._sendData[8] = (byte) this.mFlag;
    }
}
