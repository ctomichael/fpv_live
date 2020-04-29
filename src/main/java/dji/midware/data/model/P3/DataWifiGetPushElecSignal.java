package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataWifiGetPushElecSignal extends DataBase {
    private static DataWifiGetPushElecSignal mInstance = null;

    public static synchronized DataWifiGetPushElecSignal getInstance() {
        DataWifiGetPushElecSignal dataWifiGetPushElecSignal;
        synchronized (DataWifiGetPushElecSignal.class) {
            if (mInstance == null) {
                mInstance = new DataWifiGetPushElecSignal();
            }
            dataWifiGetPushElecSignal = mInstance;
        }
        return dataWifiGetPushElecSignal;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
    }

    public SIGNAL_STATUS getSignalStatus() {
        if (this._recData == null || this._recData.length == 0) {
            return SIGNAL_STATUS.OTHER;
        }
        return SIGNAL_STATUS.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum SIGNAL_STATUS {
        Good(0),
        Medium(1),
        Poor(2),
        OTHER(100);
        
        private int data;

        private SIGNAL_STATUS(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SIGNAL_STATUS find(int b) {
            SIGNAL_STATUS result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
