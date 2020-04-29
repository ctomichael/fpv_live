package dji.common.mission.waypoint;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import dji.common.error.DJIError;
import dji.common.mission.MissionUtils;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.LocationUtils;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public final class WaypointMission {
    public static final float ALMOST_ZERO_SPEED = 1.0E-7f;
    public static final float MAX_AUTO_FLIGHT_SPEED = 15.0f;
    public static final float MAX_FLIGHT_SPEED = 15.0f;
    public static final int MAX_WAYPOINT_COUNT = 99;
    public static final float MIN_AUTO_FLIGHT_SPEED = -15.0f;
    public static final float MIN_FLIGHT_SPEED = 2.0f;
    public static final int MIN_REPEAT_TIME = 0;
    public static final int MIN_WAYPOINT_COUNT = 2;
    /* access modifiers changed from: private */
    @FloatRange(from = -15.0d, to = 15.0d)
    public final float autoFlightSpeed;
    /* access modifiers changed from: private */
    public final WaypointMissionFinishedAction finishedAction;
    /* access modifiers changed from: private */
    public final WaypointMissionFlightPathMode flightPathMode;
    /* access modifiers changed from: private */
    public final WaypointMissionGotoWaypointMode gotoFirstWaypointMode;
    /* access modifiers changed from: private */
    public final WaypointMissionHeadingMode headingMode;
    /* access modifiers changed from: private */
    public final boolean isExitMissionOnRCSignalLostEnabled;
    /* access modifiers changed from: private */
    public final boolean isGimbalPitchRotationEnabled;
    /* access modifiers changed from: private */
    public final float maxFlightSpeed;
    /* access modifiers changed from: private */
    public int missionID;
    /* access modifiers changed from: private */
    public final LocationCoordinate2D pointOfInterest;
    /* access modifiers changed from: private */
    public final int repeatTimes;
    /* access modifiers changed from: private */
    public int waypointCount;
    /* access modifiers changed from: private */
    @Size(max = 99, min = 2)
    public final List<Waypoint> waypointList;

    public WaypointMission(Builder builder) {
        this.waypointCount = builder.waypointCount;
        this.maxFlightSpeed = builder.maxFlightSpeed;
        this.autoFlightSpeed = builder.autoFlightSpeed;
        this.finishedAction = builder.finishedAction;
        this.headingMode = builder.headingMode;
        this.flightPathMode = builder.flightPathMode;
        this.gotoFirstWaypointMode = builder.gotoFirstWaypointMode;
        this.isExitMissionOnRCSignalLostEnabled = builder.isExitMissionOnRCSignalLostEnabled;
        this.pointOfInterest = builder.pointOfInterest;
        this.isGimbalPitchRotationEnabled = builder.isGimbalPitchRotationEnabled;
        this.repeatTimes = builder.repeatTimes;
        this.waypointList = new ArrayList(builder.waypointList);
        this.missionID = builder.missionID;
    }

    public static class Builder {
        @FloatRange(from = -15.0d, to = 15.0d)
        protected float autoFlightSpeed;
        protected WaypointMissionFinishedAction finishedAction;
        protected WaypointMissionFlightPathMode flightPathMode;
        protected WaypointMissionGotoWaypointMode gotoFirstWaypointMode;
        protected WaypointMissionHeadingMode headingMode;
        protected boolean isExitMissionOnRCSignalLostEnabled;
        protected boolean isGimbalPitchRotationEnabled;
        protected boolean isMultiInterestPointMission;
        protected float lastCalculatedTotalDistance;
        protected Float lastCalculatedTotalTime;
        @FloatRange(from = 2.0d, to = 15.0d)
        protected float maxFlightSpeed;
        protected int missionID;
        protected LocationCoordinate2D pointOfInterest;
        @IntRange(from = 0)
        protected int repeatTimes;
        protected int waypointCount;
        @Size(max = 99, min = 2)
        protected List<Waypoint> waypointList;

        public Builder() {
            this.isMultiInterestPointMission = false;
            this.headingMode = WaypointMissionHeadingMode.AUTO;
            this.gotoFirstWaypointMode = WaypointMissionGotoWaypointMode.SAFELY;
            this.isExitMissionOnRCSignalLostEnabled = false;
            this.isGimbalPitchRotationEnabled = false;
            this.repeatTimes = 1;
            this.waypointList = new ArrayList();
            this.waypointCount = 0;
        }

        public Builder(WaypointMission waypointMissionToClone) {
            this.isMultiInterestPointMission = false;
            this.waypointCount = waypointMissionToClone.waypointCount;
            this.missionID = waypointMissionToClone.missionID;
            this.maxFlightSpeed = waypointMissionToClone.maxFlightSpeed;
            this.autoFlightSpeed = waypointMissionToClone.autoFlightSpeed;
            this.finishedAction = waypointMissionToClone.finishedAction;
            this.headingMode = waypointMissionToClone.headingMode;
            this.flightPathMode = waypointMissionToClone.flightPathMode;
            this.gotoFirstWaypointMode = waypointMissionToClone.gotoFirstWaypointMode;
            this.isExitMissionOnRCSignalLostEnabled = waypointMissionToClone.isExitMissionOnRCSignalLostEnabled;
            this.pointOfInterest = waypointMissionToClone.pointOfInterest;
            this.isGimbalPitchRotationEnabled = waypointMissionToClone.isGimbalPitchRotationEnabled;
            this.repeatTimes = waypointMissionToClone.repeatTimes;
            this.waypointList = new ArrayList(waypointMissionToClone.waypointList);
        }

        public Builder maxFlightSpeed(@FloatRange(from = 2.0d, to = 15.0d) float maxFlightSpeed2) {
            this.maxFlightSpeed = maxFlightSpeed2;
            return this;
        }

        public Builder autoFlightSpeed(@FloatRange(from = -15.0d, to = 15.0d) float autoFlightSpeed2) {
            this.autoFlightSpeed = autoFlightSpeed2;
            return this;
        }

        public Builder finishedAction(@NonNull WaypointMissionFinishedAction finishedAction2) {
            this.finishedAction = finishedAction2;
            return this;
        }

        public Builder headingMode(@NonNull WaypointMissionHeadingMode headingMode2) {
            this.headingMode = headingMode2;
            return this;
        }

        public Builder flightPathMode(@NonNull WaypointMissionFlightPathMode flightPathMode2) {
            this.flightPathMode = flightPathMode2;
            return this;
        }

        public Builder gotoFirstWaypointMode(@NonNull WaypointMissionGotoWaypointMode gotoFirstWaypointMode2) {
            this.gotoFirstWaypointMode = gotoFirstWaypointMode2;
            return this;
        }

        public Builder setExitMissionOnRCSignalLostEnabled(boolean enabled) {
            this.isExitMissionOnRCSignalLostEnabled = enabled;
            return this;
        }

        public Builder setPointOfInterest(@NonNull LocationCoordinate2D pointOfInterest2) {
            this.pointOfInterest = pointOfInterest2;
            return this;
        }

        public Builder setGimbalPitchRotationEnabled(boolean gimbalPitchRotationEnabled) {
            this.isGimbalPitchRotationEnabled = gimbalPitchRotationEnabled;
            return this;
        }

        public Builder repeatTimes(@IntRange(from = 0) int repeatTimes2) {
            this.repeatTimes = repeatTimes2;
            return this;
        }

        @Deprecated
        public Builder waypointList(@Size(max = 99, min = 2) @NonNull List<Waypoint> waypointList2) {
            this.waypointList = waypointList2;
            return this;
        }

        public Builder addWaypoint(@NonNull Waypoint waypoint) {
            this.waypointList.add(waypoint);
            this.waypointCount++;
            return this;
        }

        public Builder removeWaypoint(@NonNull Waypoint waypoint) {
            if (this.waypointList.remove(waypoint)) {
                this.waypointCount--;
            }
            return this;
        }

        public Builder removeWaypoint(@NonNull int index) {
            if (index < this.waypointList.size()) {
                this.waypointList.remove(index);
                this.waypointCount = this.waypointList.size();
            }
            return this;
        }

        public Builder insertWaypoint(@NonNull Waypoint waypoint, int index) {
            this.waypointList.add(index, waypoint);
            this.waypointCount++;
            return this;
        }

        public Builder waypointCount(int waypointCount2) {
            this.waypointCount = waypointCount2;
            return this;
        }

        public WaypointMission build() {
            return new WaypointMission(this);
        }

        public boolean isMissionComplete() {
            return (this.waypointCount == 0 || this.waypointList == null || this.waypointCount != this.waypointList.size()) ? false : true;
        }

        public int getWaypointCount() {
            return this.waypointCount;
        }

        public float getMaxFlightSpeed() {
            return this.maxFlightSpeed;
        }

        public float getAutoFlightSpeed() {
            return this.autoFlightSpeed;
        }

        public WaypointMissionFinishedAction getFinishedAction() {
            return this.finishedAction;
        }

        public WaypointMissionHeadingMode getHeadingMode() {
            return this.headingMode;
        }

        public WaypointMissionFlightPathMode getFlightPathMode() {
            return this.flightPathMode;
        }

        public WaypointMissionGotoWaypointMode getGotoFirstWaypointMode() {
            return this.gotoFirstWaypointMode;
        }

        public boolean isExitMissionOnRCSignalLostEnabled() {
            return this.isExitMissionOnRCSignalLostEnabled;
        }

        public LocationCoordinate2D getPointOfInterest() {
            return this.pointOfInterest;
        }

        public boolean isGimbalPitchRotationEnabled() {
            return this.isGimbalPitchRotationEnabled;
        }

        public int getRepeatTimes() {
            return this.repeatTimes;
        }

        @Nullable
        public DJIError checkParameters() {
            return MissionUtils.checkWaypointMissionParameters(this.maxFlightSpeed, this.autoFlightSpeed, this.repeatTimes, this.waypointCount, this.waypointList);
        }

        public List<Waypoint> getWaypointList() {
            return this.waypointList;
        }

        public int getMissionID() {
            return this.missionID;
        }

        public Builder setMissionID(@IntRange(from = 0, to = 65535) int id) {
            this.missionID = id;
            return this;
        }

        public float calculateTotalDistance() {
            float sum = 0.0f;
            if (this.waypointList == null || this.waypointList.size() <= 1) {
                this.lastCalculatedTotalDistance = 0.0f;
            } else {
                sum = 0.0f;
                Waypoint lastWaypoint = null;
                for (Waypoint waypoint : this.waypointList) {
                    float distance = 0.0f;
                    if (lastWaypoint != null) {
                        distance = (float) LocationUtils.gps2m(lastWaypoint.coordinate, (double) lastWaypoint.altitude, waypoint.coordinate, (double) waypoint.altitude);
                    }
                    sum += distance;
                    lastWaypoint = waypoint;
                }
                this.lastCalculatedTotalDistance = sum;
            }
            return sum;
        }

        public Float calculateTotalTime() {
            if (this.waypointList == null || this.waypointList.size() <= 1) {
                this.lastCalculatedTotalTime = Float.valueOf(Float.NaN);
                return Float.valueOf(Float.NaN);
            }
            Float sumTime = Float.valueOf(0.0f);
            Waypoint lastWaypoint = null;
            for (Waypoint waypoint : this.waypointList) {
                if (lastWaypoint != null) {
                    float distance = (float) LocationUtils.gps2m(lastWaypoint.coordinate, (double) lastWaypoint.altitude, waypoint.coordinate, (double) waypoint.altitude);
                    Float speed = Float.valueOf(Float.NaN);
                    if (lastWaypoint.speed > 1.0E-7f) {
                        speed = Float.valueOf(lastWaypoint.speed);
                    } else if (this.autoFlightSpeed > 1.0E-7f) {
                        speed = Float.valueOf(this.autoFlightSpeed);
                    }
                    if (speed.floatValue() != Float.NaN) {
                        sumTime = Float.valueOf(sumTime.floatValue() + (distance / speed.floatValue()));
                    } else {
                        this.lastCalculatedTotalTime = Float.valueOf(Float.NaN);
                        return Float.valueOf(Float.NaN);
                    }
                }
                lastWaypoint = waypoint;
            }
            this.lastCalculatedTotalTime = sumTime;
            return this.lastCalculatedTotalTime;
        }

        public float getLastCalculatedTotalDistance() {
            return this.lastCalculatedTotalDistance;
        }

        public Float getLastCalculatedTotalTime() {
            return this.lastCalculatedTotalTime;
        }
    }

    public int getWaypointCount() {
        return this.waypointCount;
    }

    public int getMissionID() {
        return this.missionID;
    }

    public float getMaxFlightSpeed() {
        return this.maxFlightSpeed;
    }

    public float getAutoFlightSpeed() {
        return this.autoFlightSpeed;
    }

    public WaypointMissionFinishedAction getFinishedAction() {
        return this.finishedAction;
    }

    public WaypointMissionHeadingMode getHeadingMode() {
        return this.headingMode;
    }

    public WaypointMissionFlightPathMode getFlightPathMode() {
        return this.flightPathMode;
    }

    public WaypointMissionGotoWaypointMode getGotoFirstWaypointMode() {
        return this.gotoFirstWaypointMode;
    }

    public boolean isExitMissionOnRCSignalLostEnabled() {
        return this.isExitMissionOnRCSignalLostEnabled;
    }

    public LocationCoordinate2D getPointOfInterest() {
        return this.pointOfInterest;
    }

    public boolean isGimbalPitchRotationEnabled() {
        return this.isGimbalPitchRotationEnabled;
    }

    public int getRepeatTimes() {
        return this.repeatTimes;
    }

    @Size(max = 99, min = 2)
    public List<Waypoint> getWaypointList() {
        return this.waypointList;
    }

    @Nullable
    public DJIError checkParameters() {
        return MissionUtils.checkWaypointMissionParameters(this.maxFlightSpeed, this.autoFlightSpeed, this.repeatTimes, this.waypointCount, this.waypointList);
    }
}
