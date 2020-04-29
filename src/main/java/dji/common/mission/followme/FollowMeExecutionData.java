package dji.common.mission.followme;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;

@EXClassNullAway
public class FollowMeExecutionData {
    private float distance;
    private FollowMeMissionExecuteState executeState;
    private int gpsRate;

    public FollowMeExecutionData(DataFlycGetPushWayPointMissionInfo missionInfo) {
        this.executeState = FollowMeMissionExecuteState.find(missionInfo.getFollowMeStatus());
        this.distance = ((float) missionInfo.getFollowMeDistance()) / 100.0f;
        this.gpsRate = missionInfo.getFollowMeGpsLevel();
    }

    public FollowMeMissionExecuteState getExecutionState() {
        return this.executeState;
    }

    public float getHorizontalDistance() {
        return this.distance;
    }

    public int getGpsRate() {
        return this.gpsRate;
    }

    public int hashCode() {
        return (((this.executeState.hashCode() * 31) + this.gpsRate) * 31) + Float.floatToIntBits(this.distance);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        boolean isEqual = false;
        if (obj instanceof FollowMeExecutionData) {
            FollowMeExecutionData object = (FollowMeExecutionData) obj;
            if (object.executeState == this.executeState && object.gpsRate == this.gpsRate && object.distance == this.distance) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        }
        return isEqual;
    }
}
