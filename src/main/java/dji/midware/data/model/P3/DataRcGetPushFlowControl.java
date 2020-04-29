package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataRcGetPushFlowControl extends DataBase {
    private static DataRcGetPushFlowControl instance = null;

    public static synchronized DataRcGetPushFlowControl getInstance() {
        DataRcGetPushFlowControl dataRcGetPushFlowControl;
        synchronized (DataRcGetPushFlowControl.class) {
            if (instance == null) {
                instance = new DataRcGetPushFlowControl();
            }
            dataRcGetPushFlowControl = instance;
        }
        return dataRcGetPushFlowControl;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public boolean canSendFlow() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 0;
    }
}
