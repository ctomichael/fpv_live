package dji.common.mission.hotpoint;

import dji.common.mission.MissionUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;

@EXClassNullAway
public class HotpointExecutionData {
    private float angularVelocity;
    private float currentRadius;
    private boolean isInitializing;

    public HotpointExecutionData(DataFlycGetPushWayPointMissionInfo info) {
        if (info.getHotPointMissionStatus() == 0) {
            this.isInitializing = true;
        }
        this.currentRadius = ((float) info.getHotPointRadius()) / 100.0f;
        this.angularVelocity = (float) Math.abs(MissionUtils.Degree((double) (info.getMaxHotPointSpeed() / this.currentRadius)));
    }

    public boolean getInitializing() {
        return this.isInitializing;
    }

    public float getCurrentDistanceToHotpoint() {
        return this.currentRadius;
    }

    public float getAngularVelocity() {
        return this.angularVelocity;
    }

    public float getCurrentRadius() {
        return this.currentRadius;
    }

    public int hashCode() {
        return (((Float.floatToIntBits(this.currentRadius) * 31) + Float.floatToIntBits(this.angularVelocity)) * 31) + (this.isInitializing ? 1 : 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        boolean isEqual = false;
        if (obj instanceof HotpointExecutionData) {
            HotpointExecutionData object = (HotpointExecutionData) obj;
            if (object.currentRadius == this.currentRadius && object.angularVelocity == this.angularVelocity && object.isInitializing == this.isInitializing) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        }
        return isEqual;
    }
}
