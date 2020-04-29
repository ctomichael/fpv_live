package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcSetCustomFuction;

@Keep
@EXClassNullAway
public class DataRcGetPushRcProCustomButtonsStatus extends DataBase {
    private static DataRcGetPushRcProCustomButtonsStatus instance = null;

    public static synchronized DataRcGetPushRcProCustomButtonsStatus getInstance() {
        DataRcGetPushRcProCustomButtonsStatus dataRcGetPushRcProCustomButtonsStatus;
        synchronized (DataRcGetPushRcProCustomButtonsStatus.class) {
            if (instance == null) {
                instance = new DataRcGetPushRcProCustomButtonsStatus();
            }
            dataRcGetPushRcProCustomButtonsStatus = instance;
        }
        return dataRcGetPushRcProCustomButtonsStatus;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public DataRcSetCustomFuction.DJICustomType getCustomType() {
        return DataRcSetCustomFuction.DJICustomType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public int getCustomTypeIndex() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
