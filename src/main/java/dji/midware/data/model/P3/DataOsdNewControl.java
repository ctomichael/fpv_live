package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICameraDataBase;

@Keep
@EXClassNullAway
public class DataOsdNewControl extends DJICameraDataBase {
    private static DataOsdNewControl instance = null;

    public static synchronized DataOsdNewControl getInstance() {
        DataOsdNewControl dataOsdNewControl;
        synchronized (DataOsdNewControl.class) {
            if (instance == null) {
                instance = new DataOsdNewControl();
            }
            dataOsdNewControl = instance;
        }
        return dataOsdNewControl;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getCtrObjectForOne() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }
}
