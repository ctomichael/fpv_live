package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushLimitState extends DataBase {
    private static DataFlycGetPushLimitState instance = null;

    public static synchronized DataFlycGetPushLimitState getInstance() {
        DataFlycGetPushLimitState dataFlycGetPushLimitState;
        synchronized (DataFlycGetPushLimitState.class) {
            if (instance == null) {
                instance = new DataFlycGetPushLimitState();
            }
            dataFlycGetPushLimitState = instance;
        }
        return dataFlycGetPushLimitState;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public double getLatitude() {
        return ((Double) get(0, 8, Double.class)).doubleValue();
    }

    public double getLongitude() {
        return ((Double) get(8, 8, Double.class)).doubleValue();
    }

    public int getInnerRadius() {
        return ((Integer) get(16, 2, Integer.class)).intValue();
    }

    public int getOuterRadius() {
        return ((Integer) get(18, 2, Integer.class)).intValue();
    }

    public int getType() {
        return ((Integer) get(20, 1, Integer.class)).intValue();
    }

    public int getAreaState() {
        return ((Integer) get(21, 1, Integer.class)).intValue();
    }

    public int getActionState() {
        return ((Integer) get(22, 1, Integer.class)).intValue();
    }

    public boolean isEnable() {
        return ((Integer) get(23, 1, Integer.class)).intValue() == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum DJILimitsAreaStatus {
        None(0),
        NearLimit(1),
        InnerHeightLimit(2),
        InnerLimit(3),
        OTHER(100);
        
        private int data;

        private DJILimitsAreaStatus(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJILimitsAreaStatus find(int b) {
            DJILimitsAreaStatus result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
