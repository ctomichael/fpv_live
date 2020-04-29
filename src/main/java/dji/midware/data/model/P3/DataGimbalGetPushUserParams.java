package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICameraDataBase;

@Keep
@EXClassNullAway
public class DataGimbalGetPushUserParams extends DJICameraDataBase {
    private static DataGimbalGetPushUserParams instance;

    public static synchronized DataGimbalGetPushUserParams getInstance() {
        DataGimbalGetPushUserParams dataGimbalGetPushUserParams;
        synchronized (DataGimbalGetPushUserParams.class) {
            if (instance == null) {
                instance = new DataGimbalGetPushUserParams();
                instance.isNeedPushLosed = true;
                instance.isRemoteModel = true;
            }
            dataGimbalGetPushUserParams = instance;
        }
        return dataGimbalGetPushUserParams;
    }

    public int getPresetID() {
        return getPresetID(-1);
    }

    public int getPresetID(int index) {
        return ((Integer) get(2, 1, Integer.class, index)).intValue();
    }

    public int getYawSpeed() {
        return ((Integer) get(9, 2, Integer.class)).intValue();
    }

    public int getPitchSpeed() {
        return ((Integer) get(13, 2, Integer.class)).intValue();
    }

    public int getYawDeadband() {
        return ((Integer) get(17, 2, Integer.class)).intValue();
    }

    public int getPitchDeadband() {
        return ((Integer) get(21, 2, Integer.class)).intValue();
    }

    public int getStickYawSpeed() {
        return ((Integer) get(25, 2, Integer.class)).intValue();
    }

    public int getStickPitchSpeed() {
        return ((Integer) get(29, 2, Integer.class)).intValue();
    }

    public int getStickYawSmooth() {
        return ((Integer) get(33, 2, Integer.class)).intValue();
    }

    public int getStickPitchSmooth() {
        return ((Integer) get(37, 2, Integer.class)).intValue();
    }

    public int getRollSpeed() {
        return ((Integer) get(47, 2, Integer.class)).intValue();
    }

    public int getRollDeadband() {
        return ((Integer) get(51, 2, Integer.class)).intValue();
    }

    public int getYawAccel() {
        return ((Integer) get(55, 2, Integer.class)).intValue();
    }

    public int getPitchAccel() {
        return ((Integer) get(59, 2, Integer.class)).intValue();
    }

    public int getRollAccel() {
        return ((Integer) get(63, 2, Integer.class)).intValue();
    }

    public int getYawSmoothTrack() {
        return ((Integer) get(67, 1, Integer.class)).intValue();
    }

    public int getPitchSmoothTrack() {
        return ((Integer) get(70, 1, Integer.class)).intValue();
    }

    public int getRollSmoothTrack() {
        return ((Integer) get(73, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
