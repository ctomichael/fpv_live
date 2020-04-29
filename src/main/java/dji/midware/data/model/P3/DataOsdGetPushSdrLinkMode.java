package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushSdrLinkMode extends DataBase {
    private static DataOsdGetPushSdrLinkMode instance = null;

    @Keep
    public enum LinkMode {
        NORMAL(0),
        REVERSE(1),
        CHANGING(2),
        OTHER(3);
        
        private final int mValue;

        private LinkMode(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        public boolean _equals(int value) {
            return this.mValue == value;
        }

        public static LinkMode find(int b) {
            LinkMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static synchronized DataOsdGetPushSdrLinkMode getInstance() {
        DataOsdGetPushSdrLinkMode dataOsdGetPushSdrLinkMode;
        synchronized (DataOsdGetPushSdrLinkMode.class) {
            if (instance == null) {
                instance = new DataOsdGetPushSdrLinkMode();
            }
            dataOsdGetPushSdrLinkMode = instance;
        }
        return dataOsdGetPushSdrLinkMode;
    }

    public LinkMode getLinkMode() {
        return LinkMode.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
