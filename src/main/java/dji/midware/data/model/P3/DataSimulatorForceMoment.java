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
public class DataSimulatorForceMoment extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorForceMoment instance;
    private int mForceX;
    private int mForceY;
    private int mForceZ;
    private int mMomentX;
    private int mMomentY;
    private int mMomentZ;

    public static synchronized DataSimulatorForceMoment getInstance() {
        DataSimulatorForceMoment dataSimulatorForceMoment;
        synchronized (DataSimulatorForceMoment.class) {
            if (instance == null) {
                instance = new DataSimulatorForceMoment();
            }
            dataSimulatorForceMoment = instance;
        }
        return dataSimulatorForceMoment;
    }

    public DataSimulatorForceMoment setForceX(int forceX) {
        this.mForceX = forceX;
        return this;
    }

    public DataSimulatorForceMoment setForceY(int forceY) {
        this.mForceY = forceY;
        return this;
    }

    public DataSimulatorForceMoment setForceZ(int forceZ) {
        this.mForceZ = forceZ;
        return this;
    }

    public DataSimulatorForceMoment setMomentX(int momentX) {
        this.mMomentX = momentX;
        return this;
    }

    public DataSimulatorForceMoment setMomentY(int momentY) {
        this.mMomentY = momentY;
        return this;
    }

    public DataSimulatorForceMoment setMomentZ(int momentZ) {
        this.mMomentZ = momentZ;
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
        pack.cmdId = CmdIdSimulator.CmdIdType.ForceMoment.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[12];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mForceX), 0, this._sendData, 0, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mForceY), 0, this._sendData, 2, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mForceZ), 0, this._sendData, 4, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mMomentX), 0, this._sendData, 6, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mMomentY), 0, this._sendData, 8, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mMomentZ), 0, this._sendData, 10, 2);
    }
}
