package dji.common.mission.waypoint;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@EXClassNullAway
public class WaypointExecutionProgress {
    public static final int UNKNOWN = -1;
    @NonNull
    public WaypointMissionExecuteState executeState;
    public boolean isWaypointReached;
    @IntRange(from = 0, to = 98)
    public int targetWaypointIndex;
    @IntRange(from = 2, to = 99)
    public int totalWaypointCount;

    @Retention(RetentionPolicy.SOURCE)
    public @interface InitialValue {
    }

    public WaypointExecutionProgress(DataFlycGetPushWayPointMissionInfo info) {
        WaypointMissionExecuteState find;
        this.targetWaypointIndex = info.getTargetWayPoint();
        this.executeState = WaypointMissionExecuteState.find(info.getCurrentStatus());
        if (info.getCurrentStatus() == 7) {
            find = WaypointMissionExecuteState.MOVING;
        } else {
            find = WaypointMissionExecuteState.find(info.getCurrentStatus());
        }
        this.executeState = find;
        switch (this.executeState) {
            case BEGIN_ACTION:
            case DOING_ACTION:
            case FINISHED_ACTION:
                this.isWaypointReached = true;
                break;
            default:
                this.isWaypointReached = false;
                break;
        }
        this.totalWaypointCount = -1;
    }

    public WaypointExecutionProgress() {
    }

    public int hashCode() {
        return (((((this.executeState.hashCode() * 31) + this.targetWaypointIndex) * 31) + (this.isWaypointReached ? 1 : 0)) * 31) + this.totalWaypointCount;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        boolean isEqual = false;
        if (obj instanceof WaypointExecutionProgress) {
            WaypointExecutionProgress object = (WaypointExecutionProgress) obj;
            if (object.targetWaypointIndex == this.targetWaypointIndex && object.isWaypointReached == this.isWaypointReached && object.executeState == this.executeState && object.totalWaypointCount == this.totalWaypointCount) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        }
        return isEqual;
    }

    public String toString() {
        return "total count=" + this.totalWaypointCount + " target index=" + this.targetWaypointIndex + " is reached= " + this.isWaypointReached + " state=" + this.executeState;
    }
}
