package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;

@Keep
public class DataEyeGetPushTimeLapseOverallData extends DataBase {
    private static DataEyeGetPushTimeLapseOverallData instance = null;

    public static synchronized DataEyeGetPushTimeLapseOverallData getInstance() {
        DataEyeGetPushTimeLapseOverallData dataEyeGetPushTimeLapseOverallData;
        synchronized (DataEyeGetPushTimeLapseOverallData.class) {
            if (instance == null) {
                instance = new DataEyeGetPushTimeLapseOverallData();
            }
            dataEyeGetPushTimeLapseOverallData = instance;
        }
        return dataEyeGetPushTimeLapseOverallData;
    }

    public int getSubModeType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getTotalShot() {
        return ((Integer) get(1, 2, Integer.class)).intValue();
    }

    public int getDeltTime() {
        return ((Integer) get(3, 2, Integer.class)).intValue();
    }

    public long getTrajId() {
        return ((Long) get(5, 8, Long.class)).longValue();
    }

    public int getDirection() {
        return ((Integer) get(13, 1, Integer.class)).intValue();
    }

    public int getFinishedNumberOfShooting() {
        return ((Integer) get(14, 2, Integer.class)).intValue();
    }

    public int getProgress() {
        return ((Integer) get(16, 1, Integer.class)).intValue();
    }

    public int getCurrentState() {
        return ((Integer) get(17, 1, Integer.class)).intValue();
    }

    public boolean isTimeLapseExecuting() {
        return getCurrentState() == 1 || getCurrentState() == 7;
    }

    public boolean isTimeLapsePaused() {
        return getCurrentState() == 3;
    }

    public int getException() {
        return ((Integer) get(18, 1, Integer.class)).intValue();
    }

    public int getPrompt() {
        return ((Integer) get(19, 1, Integer.class)).intValue();
    }

    public int getTotalTime() {
        return ((Integer) get(20, 4, Integer.class)).intValue() / 10;
    }

    public int getExecutedTime() {
        return ((Integer) get(24, 4, Integer.class)).intValue() / 10;
    }

    public int getDuration() {
        return ((Integer) get(28, 2, Integer.class)).intValue() / 10;
    }

    public float getMinGimbalAltitude() {
        return ((Float) get(30, 4, Float.class)).floatValue();
    }

    public float getMaxGimbalAltitude() {
        return ((Float) get(34, 4, Float.class)).floatValue();
    }

    public float getCurGimbalAltitude() {
        return ((Float) get(38, 4, Float.class)).floatValue();
    }

    public int getFramesNumber() {
        return ((Integer) get(42, 1, Integer.class)).intValue();
    }

    public boolean isInfiniteSupported() {
        return ((Integer) get(43, 1, Integer.class)).intValue() == 1;
    }

    public int getMinSuggestTime() {
        return ((Integer) get(44, 4, Integer.class)).intValue();
    }

    public int getMaxSuggestTime() {
        return ((Integer) get(48, 4, Integer.class)).intValue();
    }

    public boolean isSpeedLocked() {
        return (((Integer) get(52, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean isSpeedLockAvailable() {
        return (((Integer) get(52, 1, Integer.class)).intValue() & 2) == 2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
