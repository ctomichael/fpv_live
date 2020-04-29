package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataSimulatorGetPushMainControllerReturnParams extends DataBase {
    private static DataSimulatorGetPushMainControllerReturnParams instance;

    public static synchronized DataSimulatorGetPushMainControllerReturnParams getInstance() {
        DataSimulatorGetPushMainControllerReturnParams dataSimulatorGetPushMainControllerReturnParams;
        synchronized (DataSimulatorGetPushMainControllerReturnParams.class) {
            if (instance == null) {
                instance = new DataSimulatorGetPushMainControllerReturnParams();
            }
            dataSimulatorGetPushMainControllerReturnParams = instance;
        }
        return dataSimulatorGetPushMainControllerReturnParams;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public int getDroneType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
