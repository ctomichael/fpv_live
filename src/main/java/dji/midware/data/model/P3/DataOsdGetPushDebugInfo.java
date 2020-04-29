package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushDebugInfo extends DataBase {
    private static DataOsdGetPushDebugInfo instance = null;

    public static synchronized DataOsdGetPushDebugInfo getInstance() {
        DataOsdGetPushDebugInfo dataOsdGetPushDebugInfo;
        synchronized (DataOsdGetPushDebugInfo.class) {
            if (instance == null) {
                instance = new DataOsdGetPushDebugInfo();
            }
            dataOsdGetPushDebugInfo = instance;
        }
        return dataOsdGetPushDebugInfo;
    }

    public DataOsdGetPushDebugInfo() {
    }

    public DataOsdGetPushDebugInfo(boolean isRegist) {
        super(isRegist);
    }

    public DebugType getType() {
        return DebugType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public byte[] getData() {
        if (this._recData == null || this._recData.length < 2) {
            return null;
        }
        int length = this._recData.length - 1;
        byte[] tmpAr = new byte[length];
        System.arraycopy(this._recData, 1, tmpAr, 0, length);
        return tmpAr;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum DebugType {
        OFDM(0),
        OFDM_G(1),
        SWEEP_G(2),
        OTHER(100);
        
        private final int data;

        private DebugType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DebugType find(int b) {
            DebugType result = OFDM;
            DebugType[] values = values();
            for (DebugType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
