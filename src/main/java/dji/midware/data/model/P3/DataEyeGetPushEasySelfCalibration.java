package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICameraDataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushEasySelfCalibration extends DJICameraDataBase {
    private static DataEyeGetPushEasySelfCalibration instance = null;

    public static synchronized DataEyeGetPushEasySelfCalibration getInstance() {
        DataEyeGetPushEasySelfCalibration dataEyeGetPushEasySelfCalibration;
        synchronized (DataEyeGetPushEasySelfCalibration.class) {
            if (instance == null) {
                instance = new DataEyeGetPushEasySelfCalibration();
                instance.isNeedPushLosed = true;
                instance.isRemoteModel = true;
            }
            dataEyeGetPushEasySelfCalibration = instance;
        }
        return dataEyeGetPushEasySelfCalibration;
    }

    public int getTinkCount() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public CaliStatusCode getCaliStatusCode() {
        if (this._recData == null || this._recData.length <= 1) {
            return CaliStatusCode.NotCalibrating;
        }
        return CaliStatusCode.find(this._recData[1]);
    }

    public VisionSensorType getSensorType() {
        return VisionSensorType.find(((Integer) get(2, 1, Integer.class)).intValue());
    }

    public int getProgress() {
        if (this._recData == null || this._recData.length <= 3) {
            return 0;
        }
        return this._recData[3];
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum VisionSensorType {
        None(0),
        Bottom(1),
        Forward(2),
        Right(3),
        Backward(4),
        Left(5),
        Top(6),
        OTHER(100);
        
        private final int data;

        private VisionSensorType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static VisionSensorType find(int b) {
            VisionSensorType result = None;
            VisionSensorType[] values = values();
            for (VisionSensorType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum CaliStatusCode {
        NotCalibrating(0),
        WaitingMove(1),
        CollectImage(2),
        Caculating(3),
        WaitingNext(99),
        Success(100),
        MoveWrong(-1),
        MoveTooFast(-2),
        GroundDetailTooLess(-3),
        LightEnviromentInvalid(-4),
        FeatureLess(-5),
        DiffLarge(-6),
        AlreadyCali(-10),
        CalulateTimeOut(-100),
        ParamDiffSerious(-101),
        OTHER(100);
        
        private final int data;

        private CaliStatusCode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CaliStatusCode find(int b) {
            CaliStatusCode result = NotCalibrating;
            CaliStatusCode[] values = values();
            for (CaliStatusCode tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
