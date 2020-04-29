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
public class DataSimulatorCtrlMotor extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorCtrlMotor instance;
    private int[] mMotorOutPut = new int[8];

    public static synchronized DataSimulatorCtrlMotor getInstance() {
        DataSimulatorCtrlMotor dataSimulatorCtrlMotor;
        synchronized (DataSimulatorCtrlMotor.class) {
            if (instance == null) {
                instance = new DataSimulatorCtrlMotor();
            }
            dataSimulatorCtrlMotor = instance;
        }
        return dataSimulatorCtrlMotor;
    }

    public DataSimulatorCtrlMotor setMotoOutput(int[] motorOutput) {
        this.mMotorOutPut = motorOutput;
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
        pack.cmdId = CmdIdSimulator.CmdIdType.CtrlMotor.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[8];
        for (int i = 0; i < 8; i++) {
            this._sendData[i] = (byte) this.mMotorOutPut[i];
        }
    }
}
