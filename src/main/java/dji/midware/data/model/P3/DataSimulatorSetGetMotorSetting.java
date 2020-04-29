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
public class DataSimulatorSetGetMotorSetting extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorSetGetMotorSetting instance;
    private float[] mCl;
    private float[] mCq;
    private int mFlag;
    private int[] mIMax;
    private int[] mIMin;
    private float[] mInertiaMotor;
    private float[] mInertiaProp;
    private int[] mKV;
    private int mMarkBits;
    private int[] mMotorTiltAngle;
    private boolean mReqFlag = false;
    private int[] mRm;
    private int[] mVoltMax;

    public static synchronized DataSimulatorSetGetMotorSetting getInstance() {
        synchronized (DataSimulatorSetGetMotorSetting.class) {
            if (instance == null) {
                instance = new DataSimulatorSetGetMotorSetting();
            }
        }
        return null;
    }

    public DataSimulatorSetGetMotorSetting setMarkBits(int markBits) {
        this.mMarkBits = markBits;
        return this;
    }

    public DataSimulatorSetGetMotorSetting setAckFlag(boolean AckFlag) {
        if (AckFlag) {
            this.mFlag |= 1;
        } else {
            this.mFlag |= 0;
        }
        return this;
    }

    public DataSimulatorSetGetMotorSetting setReqFlag(boolean AckFlag) {
        if (AckFlag) {
            this.mFlag |= 2;
        } else {
            this.mFlag |= 0;
        }
        this.mReqFlag = true;
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
        pack.cmdId = CmdIdSimulator.CmdIdType.SetGetMotorSetting.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (!this.mReqFlag) {
            this._sendData = new byte[2];
            this._sendData[0] = (byte) this.mMarkBits;
            this._sendData[1] = (byte) this.mFlag;
            return;
        }
        this._sendData = new byte[((this.mMarkBits * 28) + 2)];
        for (int i = 0; i < this.mMarkBits; i++) {
            System.arraycopy(BytesUtil.getUnsignedBytes(this.mVoltMax[i]), 0, this._sendData, (i * 28) + 2, 2);
            System.arraycopy(BytesUtil.getUnsignedBytes(this.mKV[i]), 0, this._sendData, (i * 28) + 4, 2);
            System.arraycopy(BytesUtil.getUnsignedBytes(this.mRm[i]), 0, this._sendData, (i * 28) + 6, 2);
            System.arraycopy(BytesUtil.getUnsignedBytes(this.mIMax[i]), 0, this._sendData, (i * 28) + 8, 2);
            System.arraycopy(BytesUtil.getUnsignedBytes(this.mIMin[i]), 0, this._sendData, (i * 28) + 10, 2);
            System.arraycopy(BytesUtil.getUnsignedBytes(this.mMotorTiltAngle[i]), 0, this._sendData, (i * 28) + 12, 2);
            System.arraycopy(BytesUtil.getUnsignedBytes(this.mInertiaMotor[i]), 0, this._sendData, (i * 28) + 14, 4);
            System.arraycopy(BytesUtil.getUnsignedBytes(this.mCl[i]), 0, this._sendData, (i * 28) + 18, 4);
            System.arraycopy(BytesUtil.getUnsignedBytes(this.mCq[i]), 0, this._sendData, (i * 28) + 22, 4);
            System.arraycopy(BytesUtil.getUnsignedBytes(this.mInertiaProp[i]), 0, this._sendData, (i * 28) + 26, 2);
        }
    }

    public int[] getmVoltMax() {
        return this.mVoltMax;
    }

    public void setmVoltMax(int[] mVoltMax2) {
        this.mVoltMax = mVoltMax2;
    }

    public int[] getmKV() {
        return this.mKV;
    }

    public void setmKV(int[] mKV2) {
        this.mKV = mKV2;
    }

    public int[] getmRm() {
        return this.mRm;
    }

    public void setmRm(int[] mRm2) {
        this.mRm = mRm2;
    }

    public int[] getmIMax() {
        return this.mIMax;
    }

    public void setmIMax(int[] mIMax2) {
        this.mIMax = mIMax2;
    }

    public int[] getmIMin() {
        return this.mIMin;
    }

    public void setmIMin(int[] mIMin2) {
        this.mIMin = mIMin2;
    }

    public int[] getmMotorTiltAngle() {
        return this.mMotorTiltAngle;
    }

    public void setmMotorTiltAngle(int[] mMotorTiltAngle2) {
        this.mMotorTiltAngle = mMotorTiltAngle2;
    }

    public float[] getmInertiaMotor() {
        return this.mInertiaMotor;
    }

    public void setmInertiaMotor(float[] mInertiaMotor2) {
        this.mInertiaMotor = mInertiaMotor2;
    }

    public float[] getmCl() {
        return this.mCl;
    }

    public void setmCl(float[] mCl2) {
        this.mCl = mCl2;
    }

    public float[] getmCq() {
        return this.mCq;
    }

    public void setmCq(float[] mCq2) {
        this.mCq = mCq2;
    }

    public float[] getmInertiaProp() {
        return this.mInertiaProp;
    }

    public void setmInertiaProp(float[] mInertiaProp2) {
        this.mInertiaProp = mInertiaProp2;
    }
}
