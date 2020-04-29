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
public class DataSimulatorSetGetArmLength extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorSetGetArmLength instance;
    private float mArmLength;
    private int mFlag;

    public static synchronized DataSimulatorSetGetArmLength getInstance() {
        DataSimulatorSetGetArmLength dataSimulatorSetGetArmLength;
        synchronized (DataSimulatorSetGetArmLength.class) {
            if (instance == null) {
                instance = new DataSimulatorSetGetArmLength();
            }
            dataSimulatorSetGetArmLength = instance;
        }
        return dataSimulatorSetGetArmLength;
    }

    public DataSimulatorSetGetArmLength setArmLength(float armLength) {
        this.mArmLength = armLength;
        return this;
    }

    public DataSimulatorSetGetArmLength setAckFlag(boolean AckFlag) {
        if (AckFlag) {
            this.mFlag |= 1;
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
        pack.cmdId = CmdIdSimulator.CmdIdType.SetGetArmLength.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mArmLength), 0, this._sendData, 0, 4);
        this._sendData[4] = (byte) this.mFlag;
    }
}
