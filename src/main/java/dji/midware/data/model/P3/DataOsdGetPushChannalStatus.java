package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushChannalStatus extends DataBase {
    private static DataOsdGetPushChannalStatus instance = null;

    public static synchronized DataOsdGetPushChannalStatus getInstance() {
        DataOsdGetPushChannalStatus dataOsdGetPushChannalStatus;
        synchronized (DataOsdGetPushChannalStatus.class) {
            if (instance == null) {
                instance = new DataOsdGetPushChannalStatus();
            }
            dataOsdGetPushChannalStatus = instance;
        }
        return dataOsdGetPushChannalStatus;
    }

    public CHANNEL_STATUS getChannelStatus() {
        if (this._recData == null || this._recData.length == 0) {
            return CHANNEL_STATUS.OTHER;
        }
        return CHANNEL_STATUS.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum CHANNEL_STATUS {
        Excellent(0),
        Good(1),
        Medium(2),
        Poor(3),
        OTHER(100);
        
        private int data;

        private CHANNEL_STATUS(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CHANNEL_STATUS find(int b) {
            CHANNEL_STATUS result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
