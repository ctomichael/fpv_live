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
public class DataSimulatorMomentum extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorMomentum instance;
    private int mDurationTime;
    private int mMomentumX;
    private int mMomentumY;
    private int mMomentumZ;

    public static synchronized DataSimulatorMomentum getInstance() {
        DataSimulatorMomentum dataSimulatorMomentum;
        synchronized (DataSimulatorMomentum.class) {
            if (instance == null) {
                instance = new DataSimulatorMomentum();
            }
            dataSimulatorMomentum = instance;
        }
        return dataSimulatorMomentum;
    }

    public DataSimulatorMomentum setMomentumX(int momentumX) {
        this.mMomentumX = momentumX;
        return this;
    }

    public DataSimulatorMomentum setMomentumY(int momentumY) {
        this.mMomentumY = momentumY;
        return this;
    }

    public DataSimulatorMomentum setMomentumZ(int momentumZ) {
        this.mMomentumZ = momentumZ;
        return this;
    }

    public DataSimulatorMomentum setDurationTime(int durationTime) {
        this.mDurationTime = durationTime;
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
        pack.cmdId = CmdIdSimulator.CmdIdType.Momentum.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[8];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mMomentumX), 0, this._sendData, 0, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mMomentumY), 0, this._sendData, 4, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mMomentumZ), 0, this._sendData, 8, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mDurationTime), 0, this._sendData, 12, 4);
    }
}
