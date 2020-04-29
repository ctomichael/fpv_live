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
public class DataSimulatorSetGetWind extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorSetGetWind instance;
    private int mFlag = 1;
    private int mWindSpeedX = 0;
    private int mWindSpeedY = 0;
    private int mWindSpeedZ = 0;

    public static synchronized DataSimulatorSetGetWind getInstance() {
        DataSimulatorSetGetWind dataSimulatorSetGetWind;
        synchronized (DataSimulatorSetGetWind.class) {
            if (instance == null) {
                instance = new DataSimulatorSetGetWind();
            }
            dataSimulatorSetGetWind = instance;
        }
        return dataSimulatorSetGetWind;
    }

    public DataSimulatorSetGetWind setWindSpeedX(int mWindSpeedX2) {
        this.mWindSpeedX = mWindSpeedX2;
        return this;
    }

    public DataSimulatorSetGetWind setWindSpeedY(int mWindSpeedY2) {
        this.mWindSpeedY = mWindSpeedY2;
        return this;
    }

    public DataSimulatorSetGetWind setWindSpeedZ(int mWindSpeedZ2) {
        this.mWindSpeedZ = mWindSpeedZ2;
        return this;
    }

    public DataSimulatorSetGetWind setAckFlag(boolean AckFlag) {
        if (AckFlag) {
            this.mFlag |= 1;
        } else {
            this.mFlag |= 0;
        }
        return this;
    }

    public DataSimulatorSetGetWind setInitFlag(boolean mInitFlag) {
        if (mInitFlag) {
            this.mFlag |= 2;
        } else {
            this.mFlag |= 0;
        }
        return this;
    }

    public int getWindSpeedX() {
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }

    public int getWindSpeedY() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public int getWindSpeedZ() {
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    public boolean getInitFlag() {
        Boolean initFlag = false;
        if ((((Integer) get(6, 1, Integer.class)).intValue() & 2) != 0) {
            initFlag = true;
        }
        return initFlag.booleanValue();
    }

    public boolean getAckFlag() {
        Boolean ackFlag = false;
        if ((((Integer) get(6, 1, Integer.class)).intValue() & 1) != 0) {
            ackFlag = true;
        }
        return ackFlag.booleanValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SIMULATOR.value();
        pack.cmdId = CmdIdSimulator.CmdIdType.SetGetWind.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[7];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mWindSpeedX), 0, this._sendData, 0, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mWindSpeedY), 0, this._sendData, 2, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mWindSpeedZ), 0, this._sendData, 4, 2);
        this._sendData[6] = (byte) this.mFlag;
    }
}
