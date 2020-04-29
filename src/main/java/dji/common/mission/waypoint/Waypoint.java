package dji.common.mission.waypoint;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.LocationUtils;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.LinkedList;
import java.util.List;

@EXClassNullAway
public class Waypoint {
    public static final int MAX_ACTION_COUNT = 15;
    public static final int MAX_ACTION_REPEAT_TIMES = 15;
    public static final int MAX_ACTION_TIMEOUT = 999;
    public static final float MAX_ALTITUDE = 500.0f;
    public static final float MAX_CORNER_RADIUS = 1000.0f;
    public static final float MAX_GIMBAL_PITCH = 45.0f;
    public static final int MAX_HEADING = 180;
    public static final float MAX_SPEED = 15.0f;
    public static final int MIN_ACTION_REPEAT_TIMES = 1;
    public static final int MIN_ACTION_TIMEOUT = 0;
    public static final float MIN_ALTITUDE = -200.0f;
    public static final float MIN_CORNER_RADIUS = 0.2f;
    public static final float MIN_GIMBAL_PITCH = -135.0f;
    public static final int MIN_HEADING = -180;
    public static final float MIN_SPEED = 0.0f;
    @IntRange(from = 1, to = 15)
    public int actionRepeatTimes = 1;
    @IntRange(from = 0, to = 999)
    public int actionTimeoutInSeconds = 999;
    @FloatRange(from = -200.0d, to = 500.0d)
    public float altitude;
    public LocationCoordinate2D coordinate;
    @FloatRange(from = 0.20000000298023224d, to = 1000.0d)
    public float cornerRadiusInMeters = 0.2f;
    @FloatRange(from = -135.0d, to = 45.0d)
    public float gimbalPitch;
    @IntRange(from = -180, to = 180)
    public int heading;
    public boolean isUseCustomDirection = false;
    public float shootPhotoDistanceInterval;
    public float shootPhotoTimeInterval;
    @FloatRange(from = 0.0d, to = 15.0d)
    public float speed;
    public WaypointTurnMode turnMode;
    public List<WaypointAction> waypointActions;

    public Waypoint(double latitude, double longitude, float altitude2) {
        this.coordinate = new LocationCoordinate2D(latitude, longitude);
        this.altitude = altitude2;
        this.waypointActions = new LinkedList();
    }

    public boolean addAction(WaypointAction action) {
        if (this.waypointActions.size() >= 15 || action == null) {
            return false;
        }
        this.waypointActions.add(action);
        return true;
    }

    public int getHeadingInner() {
        if (this.heading < 0) {
            return this.heading + 360;
        }
        return this.heading;
    }

    public void setHeadingInner(int heading2) {
        if (heading2 > 180) {
            this.heading = heading2 - 360;
        } else {
            this.heading = heading2;
        }
    }

    public boolean insertAction(WaypointAction action, int index) {
        if (index > this.waypointActions.size() || this.waypointActions.size() >= 15) {
            return false;
        }
        this.waypointActions.add(index, action);
        return true;
    }

    public void removeAllAction() {
        this.waypointActions.clear();
    }

    public boolean removeActionAtIndex(int index) {
        if (index >= this.waypointActions.size()) {
            return false;
        }
        this.waypointActions.remove(index);
        return true;
    }

    public boolean removeAction(WaypointAction action) {
        return this.waypointActions.remove(action);
    }

    public WaypointAction getActionAtIndex(int index) {
        if (index >= this.waypointActions.size()) {
            return new WaypointAction(WaypointActionType.STAY, 0);
        }
        return this.waypointActions.get(index);
    }

    public boolean adjustActionAtIndex(int index, WaypointAction action) {
        if (index >= this.waypointActions.size() || action == null) {
            return false;
        }
        this.waypointActions.set(index, action);
        return true;
    }

    @Nullable
    public DJIError checkParameters() {
        if (!this.coordinate.isValid()) {
            return DJIMissionError.WAYPOINT_COORDINATE_NOT_VALID;
        }
        if (this.altitude < -200.0f || this.altitude > 500.0f) {
            return DJIMissionError.ALTITUDE_NOT_VALID;
        }
        if (this.heading < -180 || this.heading > 180) {
            return DJIMissionError.HEADING_NOT_VALID;
        }
        if (this.actionRepeatTimes < 1 || this.actionRepeatTimes > 15) {
            return DJIMissionError.ACTION_REPEAT_TIME_NOT_VALID;
        }
        if (this.actionTimeoutInSeconds < 0 || this.actionTimeoutInSeconds > 999) {
            return DJIMissionError.ACTION_TIMEOUT_NOT_VALID;
        }
        if (this.cornerRadiusInMeters < 0.2f || this.cornerRadiusInMeters > 1000.0f) {
            return DJIMissionError.CORNER_RADIUS_NOT_VALID;
        }
        if (this.gimbalPitch < -135.0f || this.gimbalPitch > 45.0f) {
            return DJIMissionError.GIMBAL_PITCH_NOT_VALID;
        }
        if (this.speed < 0.0f || this.speed > 15.0f) {
            return DJIMissionError.WAYPOINT_SPEED_NOT_VALID;
        }
        if (this.shootPhotoDistanceInterval < 0.0f || this.shootPhotoTimeInterval < 0.0f) {
            return DJIMissionError.SHOOT_PHOTO_NOT_VALID;
        }
        if (this.shootPhotoDistanceInterval > 0.0f && this.shootPhotoTimeInterval > 0.0f) {
            return DJIMissionError.SHOOT_PHOTO_NOT_VALID;
        }
        for (WaypointAction eachAction : this.waypointActions) {
            if (eachAction.actionType == WaypointActionType.STAY) {
                if (eachAction.actionParam < 0 || eachAction.actionParam > 32767) {
                    return DJIMissionError.STAY_ACTION_NOT_VALID;
                }
            } else if (eachAction.actionType == WaypointActionType.GIMBAL_PITCH) {
                if (((float) eachAction.actionParam) < -135.0f || ((float) eachAction.actionParam) > 45.0f) {
                    return DJIMissionError.GIMBAL_PITCH_NOT_VALID;
                }
            } else if (eachAction.actionType == WaypointActionType.ROTATE_AIRCRAFT && (eachAction.actionParam > 180 || eachAction.actionParam < -180)) {
                return DJIMissionError.ROTATE_AIRCRAFT_ACTION_NOT_VALID;
            }
        }
        return null;
    }

    public boolean isEqualPosition(Waypoint waypoint) {
        return LocationUtils.getDistanceInMeterFromTwoGPSLocations(this.coordinate.getLatitude(), this.coordinate.getLongitude(), waypoint.coordinate.getLatitude(), waypoint.coordinate.getLongitude()) < 0.5d && ((double) Math.abs(this.altitude - waypoint.altitude)) < 0.5d;
    }

    public String toString() {
        return this.coordinate.toString();
    }
}
