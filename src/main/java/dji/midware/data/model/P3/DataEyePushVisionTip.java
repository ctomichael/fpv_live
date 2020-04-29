package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyePushVisionTip extends DataBase {
    private static DataEyePushVisionTip mInstance;

    public static DataEyePushVisionTip getInstance() {
        if (mInstance == null) {
            mInstance = new DataEyePushVisionTip();
        }
        return mInstance;
    }

    public int getType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    @Keep
    public enum TrackingTipType {
        None(0),
        ConfirmGesture(1),
        TakePhotoGesture(2),
        RockerCircle(4),
        AutoCircle(8),
        MinDistance(16),
        MaxDistance(32),
        QuickMovieEndInNormalState(64),
        QuickMovieApproachingDistanceLimitation(128),
        OTHER(Integer.MAX_VALUE);
        
        private final int data;

        private TrackingTipType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TrackingTipType find(int b) {
            TrackingTipType result = None;
            TrackingTipType[] values = values();
            for (TrackingTipType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    public boolean isConfirmGesture() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean isTrackingFarAwayFromTarget() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 32) == 32;
    }

    public boolean isTrackingTooNearFromTarget() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 16) == 16;
    }

    public boolean isGoHomeGesture() {
        return (((Integer) get(2, 1, Integer.class)).intValue() & 1) == 1;
    }

    public TrackingTipType getTrackingTipType() {
        return TrackingTipType.find(((Integer) get(1, 1, Integer.class)).intValue());
    }

    public boolean isConfirmTracking() {
        return ((Integer) get(3, 1, Integer.class)).intValue() == 1;
    }

    public boolean isTakePhotoCountdown() {
        return ((Integer) get(4, 1, Integer.class)).intValue() == 1;
    }

    public int getRemaingSecondsToCapture() {
        return ((Integer) get(5, 1, Integer.class)).intValue();
    }

    public int getQuickMovieProgress() {
        return ((Integer) get(6, 1, Integer.class)).intValue();
    }

    public DynamicHomeGpsStatus getDynamicHomeGpsStatus() {
        return DynamicHomeGpsStatus.find(((Integer) get(7, 1, Integer.class)).intValue());
    }

    public int getQuickShotException() {
        return ((Integer) get(8, 1, Integer.class)).intValue();
    }

    @Keep
    public enum DynamicHomeGpsStatus {
        NORMAL(0),
        DEVICE_GPS_EXCEPTION(1),
        MC_GPS_EXCEPTION(2),
        OTHER(100);
        
        private static DynamicHomeGpsStatus[] _values = null;
        private final int data;

        private DynamicHomeGpsStatus(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DynamicHomeGpsStatus find(int b) {
            DynamicHomeGpsStatus result = NORMAL;
            if (_values == null) {
                _values = values();
            }
            DynamicHomeGpsStatus[] values = values();
            for (DynamicHomeGpsStatus tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
