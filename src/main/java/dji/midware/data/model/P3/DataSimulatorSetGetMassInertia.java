package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataSimulatorSetGetMassInertia extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorSetGetMassInertia instance;
    private int mFlag;
    private float mInertiaX;
    private float mInertiaY;
    private float mInertiaZ;
    private float mMass;

    public static synchronized DataSimulatorSetGetMassInertia getInstance() {
        DataSimulatorSetGetMassInertia dataSimulatorSetGetMassInertia;
        synchronized (DataSimulatorSetGetMassInertia.class) {
            if (instance == null) {
                instance = new DataSimulatorSetGetMassInertia();
            }
            dataSimulatorSetGetMassInertia = instance;
        }
        return dataSimulatorSetGetMassInertia;
    }

    public DataSimulatorSetGetMassInertia setMass(float mass) {
        this.mMass = mass;
        return this;
    }

    public DataSimulatorSetGetMassInertia setInertiaY(float inertiaY) {
        this.mInertiaY = inertiaY;
        return this;
    }

    public DataSimulatorSetGetMassInertia setInertiaX(float inertiaX) {
        this.mInertiaX = inertiaX;
        return this;
    }

    public DataSimulatorSetGetMassInertia setInertiaZ(float inertiaZ) {
        this.mInertiaZ = inertiaZ;
        return this;
    }

    public DataSimulatorSetGetMassInertia setAckFlag(boolean AckFlag) {
        if (AckFlag) {
            this.mFlag |= 1;
        } else {
            this.mFlag |= 0;
        }
        return this;
    }

    public void start(DJIDataCallBack callBack) {
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[17];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mMass), 0, this._sendData, 0, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mInertiaX), 0, this._sendData, 4, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mInertiaY), 0, this._sendData, 8, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mInertiaZ), 0, this._sendData, 12, 4);
        this._sendData[16] = (byte) this.mFlag;
    }
}
