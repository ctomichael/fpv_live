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
public class DataSimulatorSetGetArea extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorSetGetArea instance;
    private float mAreaX;
    private float mAreaY;
    private float mAreaZ;
    private int mFlag;

    public static synchronized DataSimulatorSetGetArea getInstance() {
        DataSimulatorSetGetArea dataSimulatorSetGetArea;
        synchronized (DataSimulatorSetGetArea.class) {
            if (instance == null) {
                instance = new DataSimulatorSetGetArea();
            }
            dataSimulatorSetGetArea = instance;
        }
        return dataSimulatorSetGetArea;
    }

    public DataSimulatorSetGetArea setAreaX(float mAreaX2) {
        this.mAreaX = mAreaX2;
        return this;
    }

    public DataSimulatorSetGetArea setAreaY(float mAreaY2) {
        this.mAreaY = mAreaY2;
        return this;
    }

    public DataSimulatorSetGetArea setAreaZ(float mAreaZ2) {
        this.mAreaZ = mAreaZ2;
        return this;
    }

    public DataSimulatorSetGetArea setAckFlag(boolean mAckFlag) {
        if (mAckFlag) {
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
        pack.cmdId = CmdIdSimulator.CmdIdType.SetGetArea.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[13];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mAreaX), 0, this._sendData, 0, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mAreaY), 0, this._sendData, 4, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mAreaZ), 0, this._sendData, 8, 4);
        this._sendData[12] = (byte) this.mFlag;
    }
}
