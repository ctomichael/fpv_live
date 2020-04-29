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
public class DataSimulatorSetGetBatterySetting extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorSetGetBatterySetting instance;
    private int mCellNum;
    private int mCellVoltage;
    private int mCycleCnt;
    private int mDesignCapacity;
    private int mErrorCnt;
    private int mFlag;
    private int mInitialCapPer;
    private float mInitialTemperature;
    private float mInternalResistance;
    private int mManufactureDate;
    private int mSequenceNum;
    private float mStandbyCurrent;

    public static synchronized DataSimulatorSetGetBatterySetting getInstance() {
        DataSimulatorSetGetBatterySetting dataSimulatorSetGetBatterySetting;
        synchronized (DataSimulatorSetGetBatterySetting.class) {
            if (instance == null) {
                instance = new DataSimulatorSetGetBatterySetting();
            }
            dataSimulatorSetGetBatterySetting = instance;
        }
        return dataSimulatorSetGetBatterySetting;
    }

    public DataSimulatorSetGetBatterySetting setAckFlag(boolean AckFlag) {
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
        pack.cmdId = CmdIdSimulator.CmdIdType.SetGetBatterySetting.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[26];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mCellVoltage), 0, this._sendData, 0, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mDesignCapacity), 0, this._sendData, 2, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mInternalResistance), 0, this._sendData, 4, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mStandbyCurrent), 0, this._sendData, 8, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mInitialTemperature), 0, this._sendData, 12, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mSequenceNum), 0, this._sendData, 16, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mErrorCnt), 0, this._sendData, 18, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mManufactureDate), 0, this._sendData, 20, 2);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mCellNum), 0, this._sendData, 22, 1);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mInitialCapPer), 0, this._sendData, 23, 1);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mCycleCnt), 0, this._sendData, 24, 1);
        this._sendData[25] = (byte) this.mFlag;
    }

    public void setmCellVoltage(int mCellVoltage2) {
        this.mCellVoltage = mCellVoltage2;
    }

    public void setmDesignCapacity(int mDesignCapacity2) {
        this.mDesignCapacity = mDesignCapacity2;
    }

    public void setmInternalResistance(float mInternalResistance2) {
        this.mInternalResistance = mInternalResistance2;
    }

    public void setmStandbyCurrent(float mStandbyCurrent2) {
        this.mStandbyCurrent = mStandbyCurrent2;
    }

    public void setmInitialTemperature(float mInitialTemperature2) {
        this.mInitialTemperature = mInitialTemperature2;
    }

    public void setmSequenceNum(int mSequenceNum2) {
        this.mSequenceNum = mSequenceNum2;
    }

    public void setmErrorCnt(int mErrorCnt2) {
        this.mErrorCnt = mErrorCnt2;
    }

    public void setmManufactureDate(int mManufactureDate2) {
        this.mManufactureDate = mManufactureDate2;
    }

    public void setmCellNum(int mCellNum2) {
        this.mCellNum = mCellNum2;
    }

    public void setmInitialCapPer(int mInitialCapPer2) {
        this.mInitialCapPer = mInitialCapPer2;
    }

    public void setmCycleCnt(int mCycleCnt2) {
        this.mCycleCnt = mCycleCnt2;
    }
}
