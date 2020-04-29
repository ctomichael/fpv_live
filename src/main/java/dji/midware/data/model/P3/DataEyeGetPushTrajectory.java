package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.vision.DJITrajectoryHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;

@Keep
@EXClassNullAway
public class DataEyeGetPushTrajectory extends DataBase implements DJIDataAsync2Listener {
    private static final int POLY_PARAM_LENGTH = 6;
    private static DataEyeGetPushTrajectory instance = null;

    public static synchronized DataEyeGetPushTrajectory getInstance() {
        DataEyeGetPushTrajectory dataEyeGetPushTrajectory;
        synchronized (DataEyeGetPushTrajectory.class) {
            if (instance == null) {
                instance = new DataEyeGetPushTrajectory();
            }
            dataEyeGetPushTrajectory = instance;
        }
        return dataEyeGetPushTrajectory;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
        DJITrajectoryHelper.getInstance().updateTrajectory(this);
        start();
    }

    public int getFragmentIndex() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public boolean isLastFragment() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) != 0;
    }

    public int getSessionId() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getCount() {
        return ((Integer) get(3, 2, Integer.class)).intValue();
    }

    public PolynomialTrajectory[] getPolynomialTrajectory() {
        int count = getCount();
        PolynomialTrajectory[] polys = new PolynomialTrajectory[count];
        for (int i = 0; i < count; i++) {
            PolynomialTrajectory poly = new PolynomialTrajectory();
            int index = (i * 4 * 6) + 5;
            for (int j = 0; j < 6; j++) {
                poly.mPolyXAxis[j] = ((Float) get((j * 4) + index, 4, Float.class)).floatValue();
            }
            int index2 = index + (count * 4 * 6);
            for (int j2 = 0; j2 < 6; j2++) {
                poly.mPolyYAxis[j2] = ((Float) get((j2 * 4) + index2, 4, Float.class)).floatValue();
            }
            int index3 = index2 + (count * 4 * 6);
            for (int j3 = 0; j3 < 6; j3++) {
                poly.mPolyZAxis[j3] = ((Float) get((j3 * 4) + index3, 4, Float.class)).floatValue();
            }
            polys[i] = poly;
        }
        return polys;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) Ccode.OK.value();
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.pack.senderType;
        pack.receiverId = this.pack.senderId;
        pack.cmdType = DataConfig.CMDTYPE.ACK.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = this.pack.cmdSet;
        pack.cmdId = this.pack.cmdId;
        pack.seq = this.pack.seq;
        super.start(pack);
    }

    @Keep
    public static final class PolynomialTrajectory {
        public float[] mPolyXAxis = new float[6];
        public float[] mPolyYAxis = new float[6];
        public float[] mPolyZAxis = new float[6];

        public PolynomialTrajectory() {
        }

        public PolynomialTrajectory(float[] x, float[] y, float[] z) {
            if (x != null) {
                System.arraycopy(x, 0, this.mPolyXAxis, 0, Math.min(x.length, this.mPolyXAxis.length));
            }
            if (y != null) {
                System.arraycopy(y, 0, this.mPolyYAxis, 0, Math.min(y.length, this.mPolyYAxis.length));
            }
            if (z != null) {
                System.arraycopy(z, 0, this.mPolyZAxis, 0, Math.min(z.length, this.mPolyZAxis.length));
            }
        }
    }
}
