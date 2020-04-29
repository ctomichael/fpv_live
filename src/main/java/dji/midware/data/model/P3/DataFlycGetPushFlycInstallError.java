package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushFlycInstallError extends DataBase {
    private static DataFlycGetPushFlycInstallError ourInstance = new DataFlycGetPushFlycInstallError();

    public static DataFlycGetPushFlycInstallError getInstance() {
        return ourInstance;
    }

    private DataFlycGetPushFlycInstallError() {
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getYawInstallErrorLevel() {
        return ((Integer) get(0, 4, Integer.class)).intValue() & 3;
    }

    public int getRollInstallErrorLevel() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 2) & 3;
    }

    public int getPitchInstallErrorLevel() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 4) & 3;
    }

    public int getGyroXInstallErrorLevel() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 6) & 3;
    }

    public int getGyroYInstallErrorLevel() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 8) & 3;
    }

    public int getGyroZInstallErrorLevel() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 10) & 3;
    }

    public int getAccXInstallErrorLevel() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 12) & 3;
    }

    public int getAccYInstallErrorLevel() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 14) & 3;
    }

    public int getAccZInstallErrorLevel() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 16) & 3;
    }

    public int getThrustInstallErrorLevel() {
        return (((Integer) get(0, 4, Integer.class)).intValue() >> 18) & 3;
    }
}
