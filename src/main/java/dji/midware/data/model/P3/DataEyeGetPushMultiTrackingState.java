package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataEyeGetPushTrackStatus;
import dji.midware.data.model.base.DJICommonDataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushMultiTrackingState extends DJICommonDataBase {
    private static final int LENGTH_OF_PROTOCOL = 7;
    private static DataEyeGetPushMultiTrackingState instance;

    public static synchronized DataEyeGetPushMultiTrackingState getInstance() {
        DataEyeGetPushMultiTrackingState dataEyeGetPushMultiTrackingState;
        synchronized (DataEyeGetPushMultiTrackingState.class) {
            if (instance == null) {
                instance = new DataEyeGetPushMultiTrackingState();
            }
            dataEyeGetPushMultiTrackingState = instance;
        }
        return dataEyeGetPushMultiTrackingState;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public boolean isInMultiTracking() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 1;
    }

    public boolean isInTracking() {
        int value = ((Integer) get(0, 1, Integer.class)).intValue();
        return value == 1 || value == 2;
    }

    public boolean isInSingleTracking() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 1;
    }

    public boolean isTrackingExecuting() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 2;
    }

    public int getNumberOfDetectedTargets() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getOverallTrackingState() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getOverallTrackingMode() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getMaximumTrackingSpeed() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public int getTrackingSpeedThreshold() {
        return ((Integer) get(5, 1, Integer.class)).intValue();
    }

    public DataEyeGetPushTrackStatus.TrackException getTrackingException() {
        if (this._recData == null || this._recData.length < 7) {
            return DataEyeGetPushTrackStatus.TrackException.NONE;
        }
        return DataEyeGetPushTrackStatus.TrackException.find(this._recData[6]);
    }

    public boolean isUsingGPS() {
        return ((Integer) get(7, 1, Integer.class)).intValue() == 1;
    }

    public int takeControlError() {
        return ((Integer) get(8, 1, Integer.class)).intValue();
    }

    public boolean isHighSpeedEnabled() {
        return (((Integer) get(9, 1, Integer.class)).intValue() & 1) == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
