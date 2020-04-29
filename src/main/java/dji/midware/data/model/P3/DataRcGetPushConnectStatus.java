package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataRcGetPushConnectStatus extends DataBase {
    private static DataRcGetPushConnectStatus instance = null;

    public static synchronized DataRcGetPushConnectStatus getInstance() {
        DataRcGetPushConnectStatus dataRcGetPushConnectStatus;
        synchronized (DataRcGetPushConnectStatus.class) {
            if (instance == null) {
                instance = new DataRcGetPushConnectStatus();
            }
            dataRcGetPushConnectStatus = instance;
        }
        return dataRcGetPushConnectStatus;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public int getIP() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getSignalMaster() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getSignalSlave(int index) {
        return ((Integer) get(index + 2, 1, Integer.class)).intValue();
    }

    public boolean isSlaveConnected() {
        for (int i = 0; i < 6; i++) {
            if (getSignalSlave(i) > 0) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
