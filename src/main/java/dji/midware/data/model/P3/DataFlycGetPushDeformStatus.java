package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataOsdGetPushCommon;

@Keep
@EXClassNullAway
public class DataFlycGetPushDeformStatus extends DataBase {
    private static DataFlycGetPushDeformStatus instance = null;

    public static synchronized DataFlycGetPushDeformStatus getInstance() {
        DataFlycGetPushDeformStatus dataFlycGetPushDeformStatus;
        synchronized (DataFlycGetPushDeformStatus.class) {
            if (instance == null) {
                instance = new DataFlycGetPushDeformStatus();
            }
            dataFlycGetPushDeformStatus = instance;
        }
        return dataFlycGetPushDeformStatus;
    }

    public DataFlycGetPushDeformStatus() {
    }

    public DataFlycGetPushDeformStatus(boolean isRegist) {
        super(isRegist);
    }

    public DEFORM_MODE getDeformMode() {
        return DEFORM_MODE.find((((Integer) get(0, 1, Integer.class)).intValue() & 48) >>> 4);
    }

    public DataOsdGetPushCommon.TRIPOD_STATUS getDeformStatus() {
        return DataOsdGetPushCommon.TRIPOD_STATUS.ofValue((byte) ((((Integer) get(0, 1, Integer.class)).intValue() & 14) >>> 1));
    }

    public boolean isDeformProtected() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isExceptionState() {
        return ((((Integer) get(0, 1, Integer.class)).intValue() & 128) >>> 7) != 0;
    }

    public boolean errReasionFixing() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) != 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum DEFORM_MODE {
        Pack(0),
        Protect(1),
        Normal(2),
        OTHER(3);
        
        private int data;

        private DEFORM_MODE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DEFORM_MODE find(int b) {
            DEFORM_MODE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
