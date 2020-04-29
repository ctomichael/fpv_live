package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataRcGetPushFollowFocus2 extends DataBase {
    private static DataRcGetPushFollowFocus2 instance = null;

    public static synchronized DataRcGetPushFollowFocus2 getInstance() {
        DataRcGetPushFollowFocus2 dataRcGetPushFollowFocus2;
        synchronized (DataRcGetPushFollowFocus2.class) {
            if (instance == null) {
                instance = new DataRcGetPushFollowFocus2();
            }
            dataRcGetPushFollowFocus2 = instance;
        }
        return dataRcGetPushFollowFocus2;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        setRecData(data);
        post();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getCurCtrlStatus() {
        return ((Integer) get(0, 1, Integer.class)).intValue() & 1;
    }

    public CtrlType getCtrlType() {
        return CtrlType.find((((Integer) get(0, 1, Integer.class)).intValue() & 14) >>> 1);
    }

    public CtrlDirection getCtrlDirection() {
        return CtrlDirection.find((((Integer) get(0, 1, Integer.class)).intValue() & 16) >>> 4);
    }

    public int getCurrentValue() {
        return ((Integer) get(1, 2, Integer.class)).intValue();
    }

    public int getHandWheelType() {
        return ((Integer) get(3, 1, Integer.class)).intValue() & 3;
    }

    @Keep
    public enum CtrlType {
        APERTURE(0),
        FOCUS_POSITION(1),
        FOCUS_LENGTH(2),
        OTHER(10);
        
        private int data;

        private CtrlType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CtrlType find(int b) {
            CtrlType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum CtrlDirection {
        CW(0),
        CCW(1),
        OTHER(10);
        
        private int data;

        private CtrlDirection(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CtrlDirection find(int b) {
            CtrlDirection result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
