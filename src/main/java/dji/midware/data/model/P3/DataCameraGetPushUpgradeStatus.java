package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.base.DJICameraDataBase;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraGetPushUpgradeStatus extends DJICameraDataBase {
    private static DataCameraGetPushUpgradeStatus instance = null;
    private SparseArray<UpgradeStatusModel> list = new SparseArray<>();

    @Keep
    public static class UpgradeStatusModel {
        public int degree;
        public DeviceType deviceType;
        public FirmwareUpgradeStatus status;
        public FirmwareType type;
        public String version;
    }

    public static synchronized DataCameraGetPushUpgradeStatus getInstance() {
        DataCameraGetPushUpgradeStatus dataCameraGetPushUpgradeStatus;
        synchronized (DataCameraGetPushUpgradeStatus.class) {
            if (instance == null) {
                instance = new DataCameraGetPushUpgradeStatus();
            }
            dataCameraGetPushUpgradeStatus = instance;
        }
        return dataCameraGetPushUpgradeStatus;
    }

    public SparseArray<UpgradeStatusModel> getList() {
        if (this._recData == null) {
            return this.list;
        }
        int size = (this._recData.length - 3) / 8;
        for (int i = 0; i < size; i++) {
            UpgradeStatusModel model = new UpgradeStatusModel();
            model.deviceType = DeviceType.find(((Integer) get((i * 8) + 0 + 3, 1, Integer.class)).intValue());
            model.type = FirmwareType.find(((Integer) get((i * 8) + 1 + 3, 1, Integer.class)).intValue());
            model.version = "v " + BytesUtil.byte2hex(this._recData[(i * 8) + 2 + 3]) + "." + BytesUtil.byte2hex(this._recData[(i * 8) + 3 + 3]) + "." + BytesUtil.byte2hex(this._recData[(i * 8) + 4 + 3]) + "." + BytesUtil.byte2hex(this._recData[(i * 8) + 5 + 3]);
            model.status = FirmwareUpgradeStatus.find(((Integer) get((i * 8) + 6 + 3, 1, Integer.class)).intValue());
            model.degree = ((Integer) get((i * 8) + 7 + 3, 1, Integer.class)).intValue();
            this.list.append(i, model);
        }
        return this.list;
    }

    public UpgradeStep getStep() {
        return UpgradeStep.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public int getCountdown() {
        return ((Integer) get(1, 2, Integer.class)).intValue() & 255;
    }

    public int getRound() {
        return ((Integer) get(1, 2, Integer.class)).intValue() & 7;
    }

    public int getFirmwareCount() {
        return (((Integer) get(1, 2, Integer.class)).intValue() >> 8) & 31;
    }

    public int getProgress() {
        return ((Integer) get(1, 2, Integer.class)).intValue() & 255;
    }

    public UpgradeEndCause getEndCause() {
        return UpgradeEndCause.find(((Integer) get(1, 2, Integer.class)).intValue() & 255);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum UpgradeEndCause {
        Success(1),
        Failed(2),
        FirmwareError(3),
        VersionSame(4),
        UserCancel(5),
        TimeoutCancel(6),
        MotorUp(7),
        OTHER(100);
        
        private int data;

        private UpgradeEndCause(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static UpgradeEndCause find(int b) {
            UpgradeEndCause result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum UpgradeStep {
        Check(1),
        Ack(2),
        Progress(3),
        End(4),
        OTHER(100);
        
        private int data;

        private UpgradeStep(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static UpgradeStep find(int b) {
            UpgradeStep result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum FirmwareType {
        Loader(1),
        Sys(2),
        App(3),
        OTHER(100);
        
        private int data;

        private FirmwareType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FirmwareType find(int b) {
            FirmwareType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum FirmwareUpgradeStatus {
        Success(0),
        Progressing(1),
        Waiting(2),
        Error(3),
        CanotCheck(4),
        OTHER(100);
        
        private int data;

        private FirmwareUpgradeStatus(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FirmwareUpgradeStatus find(int b) {
            FirmwareUpgradeStatus result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
