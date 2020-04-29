package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;

@Keep
public class DataEyeGetPushPOIExecutionParams extends DataBase {
    private static DataEyeGetPushPOIExecutionParams instance;

    public static synchronized DataEyeGetPushPOIExecutionParams getInstance() {
        DataEyeGetPushPOIExecutionParams dataEyeGetPushPOIExecutionParams;
        synchronized (DataEyeGetPushPOIExecutionParams.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPOIExecutionParams();
            }
            dataEyeGetPushPOIExecutionParams = instance;
        }
        return dataEyeGetPushPOIExecutionParams;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public int getState() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getWorkMode() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public boolean doesHaveException() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 1;
    }

    public int getException() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public float getMaxSpeed() {
        return ((Float) get(3, 4, Float.class)).floatValue();
    }

    public float getSpeed() {
        return ((Float) get(7, 4, Float.class)).floatValue();
    }

    public float getHeight() {
        return ((Float) get(11, 4, Float.class)).floatValue();
    }

    public float getCircleRadius() {
        return ((Float) get(15, 4, Float.class)).floatValue();
    }

    public double getLatitude() {
        return ((Double) get(19, 8, Double.class)).doubleValue();
    }

    public double getLongitude() {
        return ((Double) get(27, 8, Double.class)).doubleValue();
    }

    public float getTargetRadius() {
        return ((Float) get(35, 4, Float.class)).floatValue();
    }

    public float getTargetHeight() {
        return ((Float) get(39, 4, Float.class)).floatValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
