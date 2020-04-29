package dji.pilot.fpv.model;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataEyeGetPushFrontAvoidance;
import dji.midware.data.model.P3.DataEyeGetPushOmniAvoidance;

@EXClassNullAway
public interface IVisionObjs {
    public static final int INVALID_LEVEL = Integer.MAX_VALUE;

    public enum AvoidType {
        NON(false, false, false),
        RADAR(true, true, true),
        TOF(false, true, false),
        TOF_MAMMOTH(true, true, true);
        
        private final boolean mHasAss;
        private final boolean mHasMultiAngle;
        private final boolean mbSensor;

        private AvoidType(boolean ass, boolean bSensor, boolean multiAngle) {
            this.mHasAss = ass;
            this.mbSensor = bSensor;
            this.mHasMultiAngle = multiAngle;
        }

        public boolean hasAss() {
            return this.mHasAss;
        }

        public boolean hasSensor() {
            return this.mbSensor;
        }

        public boolean supportMultiAngle() {
            return this.mHasMultiAngle;
        }
    }

    public enum VisualAssStatus {
        CLOSED(false, 0),
        DISABLE(false, 0),
        INVALID(false, 0),
        HIDE(true, 0),
        NORMAL(true, 0);
        
        private int mArg = Integer.MIN_VALUE;
        private boolean mBeNormal = false;

        private VisualAssStatus(boolean isNormal, int arg) {
            this.mBeNormal = isNormal;
            this.mArg = arg;
        }

        public boolean isNormal() {
            return this.mBeNormal;
        }
    }

    public static class OmniAvoidanceInfo {
        public int alertLevel = Integer.MAX_VALUE;
        public volatile int assInvalidResId = 0;
        public AvoidType avoidType = AvoidType.NON;
        public final DataEyeGetPushOmniAvoidance.Direction direction;
        public final AvoidDisableTipFlag disableTipFlag = new AvoidDisableTipFlag();
        public int radarDetectFailTimes = 0;
        public VisualAssStatus visualAssStatus = VisualAssStatus.CLOSED;

        public OmniAvoidanceInfo(DataEyeGetPushOmniAvoidance.Direction direction2) {
            this.direction = direction2;
        }

        public void resetData(AvoidType type, boolean sensorStatus) {
            this.radarDetectFailTimes = 0;
            this.assInvalidResId = 0;
            this.alertLevel = Integer.MAX_VALUE;
            this.avoidType = type;
            if (AvoidType.RADAR == type) {
                this.visualAssStatus = sensorStatus ? VisualAssStatus.NORMAL : VisualAssStatus.CLOSED;
            } else if (AvoidType.TOF == type) {
                this.visualAssStatus = VisualAssStatus.NORMAL;
            }
            this.disableTipFlag.resetData();
        }

        public boolean hasAss() {
            return this.avoidType.hasAss();
        }

        public boolean hasSensor() {
            return AvoidType.NON != this.avoidType;
        }
    }

    public static class AvoidInfo {
        public int mAlertLevel = Integer.MAX_VALUE;
        public volatile int mAssInvalidResId = 0;
        public AvoidType mAvoidType = AvoidType.NON;
        public final AvoidDisableTipFlag mDisableTipFlags = new AvoidDisableTipFlag();
        public boolean mIsOPen = true;
        public boolean mIsUnderExposure = false;
        public int mRadarDetectFailTimes = 0;
        public final DataEyeGetPushFrontAvoidance.SensorType mSensorType;
        public VisualAssStatus mVisualAssStatus = VisualAssStatus.CLOSED;

        public AvoidInfo(DataEyeGetPushFrontAvoidance.SensorType type) {
            this.mSensorType = type;
        }

        public void resetData(AvoidType type, boolean sensorStatus) {
            this.mRadarDetectFailTimes = 0;
            this.mAssInvalidResId = 0;
            this.mAlertLevel = Integer.MAX_VALUE;
            this.mAvoidType = type;
            if (AvoidType.RADAR == type) {
                this.mVisualAssStatus = sensorStatus ? VisualAssStatus.NORMAL : VisualAssStatus.CLOSED;
            } else if (AvoidType.TOF == type) {
                this.mVisualAssStatus = VisualAssStatus.NORMAL;
            }
            this.mDisableTipFlags.resetData();
        }

        public boolean hasAss() {
            return this.mAvoidType.hasAss();
        }

        public boolean hasSensor() {
            return AvoidType.NON != this.mAvoidType;
        }

        public boolean equals(Object o) {
            return super.equals(o);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("type-").append(this.mSensorType).append(";");
            sb.append("status-").append(this.mVisualAssStatus).append(";");
            sb.append("times-").append(String.valueOf(this.mRadarDetectFailTimes)).append(";");
            sb.append("reason-").append(String.valueOf(this.mAssInvalidResId)).append(";");
            sb.append("level-").append(String.valueOf(this.mAlertLevel)).append(";");
            sb.append("at-").append(String.valueOf(this.mAvoidType)).append(";");
            return sb.toString();
        }

        public int hashCode() {
            return super.hashCode();
        }
    }

    public static class AvoidDisableTipFlag {
        public boolean mByAll = false;
        public boolean mByAttiLarge = false;
        public boolean mByAutoLanding = false;
        public boolean mBySwitchSensor = false;
        public boolean mByTripod = false;

        public void resetData() {
            this.mByAll = false;
            this.mByAutoLanding = false;
            this.mByTripod = false;
            this.mBySwitchSensor = false;
            this.mByAttiLarge = false;
        }

        public boolean equals(Object o) {
            return super.equals(o);
        }

        public int hashCode() {
            int result;
            int i;
            int i2;
            int i3 = 1;
            if (this.mByAutoLanding) {
                result = 1;
            } else {
                result = 0;
            }
            int i4 = result * 31;
            if (this.mByTripod) {
                i = 1;
            } else {
                i = 0;
            }
            int i5 = (i4 + i) * 31;
            if (this.mBySwitchSensor) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            int i6 = (i5 + i2) * 31;
            if (!this.mByAttiLarge) {
                i3 = 0;
            }
            return i6 + i3;
        }

        public String toString() {
            return "AvoidDisableTipFlag{mByAutoLanding=" + this.mByAutoLanding + ", mByTripod=" + this.mByTripod + ", mBySwitchSensor=" + this.mBySwitchSensor + ", mByAttiLarge=" + this.mByAttiLarge + '}';
        }
    }
}
