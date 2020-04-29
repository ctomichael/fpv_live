package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataSimulatorGetPushFlightStatusParams extends DataBase {
    private static DataSimulatorGetPushFlightStatusParams instance = null;

    public static synchronized DataSimulatorGetPushFlightStatusParams getInstance() {
        DataSimulatorGetPushFlightStatusParams dataSimulatorGetPushFlightStatusParams;
        synchronized (DataSimulatorGetPushFlightStatusParams.class) {
            if (instance == null) {
                instance = new DataSimulatorGetPushFlightStatusParams();
            }
            dataSimulatorGetPushFlightStatusParams = instance;
        }
        return dataSimulatorGetPushFlightStatusParams;
    }

    public byte[] getResult() {
        return this._recData;
    }

    public int getLength() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public boolean hasMotorTurnedOn() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean isInTheAir() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 2) == 2;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
