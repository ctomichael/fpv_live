package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushMicInfo extends DataBase {
    private static DataOsdGetPushMicInfo instance = null;

    public static synchronized DataOsdGetPushMicInfo getInstance() {
        DataOsdGetPushMicInfo dataOsdGetPushMicInfo;
        synchronized (DataOsdGetPushMicInfo.class) {
            if (instance == null) {
                instance = new DataOsdGetPushMicInfo();
            }
            dataOsdGetPushMicInfo = instance;
        }
        return dataOsdGetPushMicInfo;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getMicVolume() {
        return ((Short) get(0, 1, Short.class)).shortValue() >> 1;
    }

    public MIC_TYPE getMicType() {
        return MIC_TYPE.find(((Short) get(0, 1, Short.class)).shortValue() & 1);
    }

    @Keep
    public enum MIC_TYPE {
        IN(0),
        OUT(1),
        OTHER(2);
        
        private int data;

        private MIC_TYPE(int type) {
            this.data = type;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static MIC_TYPE find(int b) {
            MIC_TYPE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
