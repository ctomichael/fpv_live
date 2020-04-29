package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetHdvtPushException extends DataBase {
    private static DataOsdGetHdvtPushException instance = null;
    private boolean isChannelEncryptException = false;
    private boolean isGndRfException = false;
    private boolean isUavRfException = false;

    public static synchronized DataOsdGetHdvtPushException getInstance() {
        DataOsdGetHdvtPushException dataOsdGetHdvtPushException;
        synchronized (DataOsdGetHdvtPushException.class) {
            if (instance == null) {
                instance = new DataOsdGetHdvtPushException();
            }
            dataOsdGetHdvtPushException = instance;
        }
        return dataOsdGetHdvtPushException;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    /* access modifiers changed from: protected */
    public void post() {
        boolean z;
        if (this.pack.senderType == DeviceType.OFDM.value()) {
            if ((((Integer) get(0, 1, Integer.class)).intValue() & 1) != 0) {
                this.isUavRfException = true;
            } else {
                this.isUavRfException = false;
            }
        } else if (this.pack.senderType == DeviceType.OSD.value()) {
            if ((((Integer) get(0, 1, Integer.class)).intValue() & 1) != 0) {
                this.isGndRfException = true;
            } else {
                this.isGndRfException = false;
            }
        }
        if ((((Integer) get(0, 1, Integer.class)).intValue() & 2) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isChannelEncryptException = z;
        super.post();
    }

    public int getSenderType() {
        return this.pack.senderType;
    }

    public boolean getUavRfStatus() {
        return this.isUavRfException;
    }

    public boolean getGndRfStatus() {
        return this.isGndRfException;
    }

    public boolean getChannelEncryptStatus() {
        return this.isChannelEncryptException;
    }
}
