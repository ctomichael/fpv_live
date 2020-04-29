package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataSingleVisualParam;

@Keep
@EXClassNullAway
public class DataEyeGetPushTrackStatus extends DataBase {
    private static DataEyeGetPushTrackStatus instance = null;

    public static synchronized DataEyeGetPushTrackStatus getInstance() {
        DataEyeGetPushTrackStatus dataEyeGetPushTrackStatus;
        synchronized (DataEyeGetPushTrackStatus.class) {
            if (instance == null) {
                instance = new DataEyeGetPushTrackStatus();
            }
            dataEyeGetPushTrackStatus = instance;
        }
        return dataEyeGetPushTrackStatus;
    }

    public TrackMode getRectMode() {
        return TrackMode.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public float getCenterX() {
        return ((Float) get(1, 4, Float.class)).floatValue();
    }

    public float getCenterY() {
        return ((Float) get(5, 4, Float.class)).floatValue();
    }

    public float getWidth() {
        return ((Float) get(9, 4, Float.class)).floatValue();
    }

    public float getHeight() {
        return ((Float) get(13, 4, Float.class)).floatValue();
    }

    public TrackException getException() {
        if (this._recData == null || this._recData.length <= 17) {
            return TrackException.NONE;
        }
        return TrackException.find(this._recData[17]);
    }

    public short getSessionId() {
        return ((Short) get(18, 2, Short.class)).shortValue();
    }

    public boolean isHumanTarget() {
        return (((Integer) get(20, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isHeadLock() {
        return (((Integer) get(20, 1, Integer.class)).intValue() & 2) != 0;
    }

    public DataSingleVisualParam.TrackingMode getTrackingMode() {
        return DataSingleVisualParam.TrackingMode.find(((Integer) get(21, 1, Integer.class)).intValue());
    }

    public boolean isSpotLight() {
        return getTrackingMode() == DataSingleVisualParam.TrackingMode.WATCH_TARGET;
    }

    public int getCurrentTrackingMaximumSpeed() {
        return ((Integer) get(24, 1, Integer.class)).intValue();
    }

    public int getTrackingSpeedThreshold() {
        return ((Integer) get(25, 1, Integer.class)).intValue();
    }

    public TargetObjType getTargetType() {
        if (this._recData != null && this._recData.length > 22) {
            return TargetObjType.find(((Integer) get(22, 1, Integer.class)).intValue());
        }
        if (isHumanTarget()) {
            return TargetObjType.PERSON;
        }
        return TargetObjType.UNKNOWN;
    }

    public TargetAction getTargetAction() {
        return TargetAction.find(((Integer) get(23, 1, Integer.class)).intValue());
    }

    public boolean isGpsUsed() {
        return (((Integer) get(26, 1, Integer.class)).intValue() & 2) != 0;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        setRecData(data);
        if (isWantPush()) {
            post();
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum TrackMode {
        LOST(0),
        NORMAL(1),
        WEAK(2),
        DETECT_AFTER_LOST(3),
        TRACKING(4),
        CONFIRM(5),
        PERSON(6),
        OTHER(100);
        
        private int data;

        private TrackMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TrackMode find(int b) {
            TrackMode result = LOST;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum TrackException {
        NONE(0),
        TIMEOUT(1),
        NO_VELOCITY(2),
        NO_IMAGE_INPUT(3),
        LOW_FPS(4),
        LIMIT(5),
        LOST_RC(6),
        LOST_APP(7),
        LOST_CONTROL(9),
        APP_STOP(10),
        EXIT_BYSELF(11),
        QUICK_MOVIE_FINISH_NORMALLY(12),
        QUICK_MOVIE_STOP_BY_USER(13),
        SHAKE(-1),
        LOW_DETECT(-2),
        RC_HALT(-3),
        APP_HALT(-4),
        SELFIE_UNABLE_TO_GET_DISTANCE(-5),
        TOO_HIGH(-11),
        OBSTACLE(-12),
        PITCH_LOW(-13),
        TOO_FAR(-14),
        TOO_CLOSE(-15),
        DRONE_TOO_HIGH(-16),
        DRONE_TOO_LOW(-17),
        RADAR_FAILED(-18),
        TOO_SMALL(-21),
        TOO_LARGE(-22),
        NO_FEATURE(-23),
        AUTHORITY_LOW_BATTERY(-25),
        AUTHORITY_MISSION_CONFLICT(-26),
        AUTHORITY_UNKNOWN_FC_ERROR(-27),
        OTHER(100);
        
        private int data;

        private TrackException(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TrackException find(int b) {
            TrackException result = NONE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum TargetObjType {
        UNKNOWN(0),
        PERSON(1),
        CAR(2),
        VAN(3),
        BIKE(4),
        ANIMAL(5),
        BOAT(6),
        HOT_POINT(7),
        OTHER(100);
        
        private final int data;

        private TargetObjType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TargetObjType find(int b) {
            TargetObjType result = UNKNOWN;
            TargetObjType[] values = values();
            for (TargetObjType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum TargetAction {
        Non(0),
        JUMP(1),
        OTHER(100);
        
        private final int data;

        private TargetAction(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TargetAction find(int b) {
            TargetAction result = Non;
            TargetAction[] values = values();
            for (TargetAction tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
