package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushUAVState extends DataBase {
    private static DataEyeGetPushUAVState instance = null;

    public static synchronized DataEyeGetPushUAVState getInstance() {
        DataEyeGetPushUAVState dataEyeGetPushUAVState;
        synchronized (DataEyeGetPushUAVState.class) {
            if (instance == null) {
                instance = new DataEyeGetPushUAVState();
            }
            dataEyeGetPushUAVState = instance;
        }
        return dataEyeGetPushUAVState;
    }

    public float getPosX() {
        return ((Float) get(0, 4, Float.class)).floatValue();
    }

    public float getPosY() {
        return ((Float) get(4, 4, Float.class)).floatValue();
    }

    public float getPosZ() {
        return ((Float) get(8, 4, Float.class)).floatValue();
    }

    public float getGimbalRoll() {
        return ((Float) get(12, 4, Float.class)).floatValue();
    }

    public float getGimbalPitch() {
        return ((Float) get(16, 4, Float.class)).floatValue();
    }

    public float getGimbalYaw() {
        return ((Float) get(20, 4, Float.class)).floatValue();
    }

    public float getRemainingDistance() {
        return ((Float) get(24, 4, Float.class)).floatValue();
    }

    public float getCurrentStepInTrajectory() {
        return ((Float) get(28, 4, Float.class)).floatValue();
    }

    public int getCurrentTrajectoryIndex() {
        return ((Integer) get(32, 4, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
