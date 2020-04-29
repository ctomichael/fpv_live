package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataRcGetPushFollowFocus extends DataBase {
    private static DataRcGetPushFollowFocus instance = null;

    public static synchronized DataRcGetPushFollowFocus getInstance() {
        DataRcGetPushFollowFocus dataRcGetPushFollowFocus;
        synchronized (DataRcGetPushFollowFocus.class) {
            if (instance == null) {
                instance = new DataRcGetPushFollowFocus();
            }
            dataRcGetPushFollowFocus = instance;
        }
        return dataRcGetPushFollowFocus;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        setRecData(data);
        post();
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

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum CtrlType {
        APERTURE(0),
        FOCAL_LENGTH(1),
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
