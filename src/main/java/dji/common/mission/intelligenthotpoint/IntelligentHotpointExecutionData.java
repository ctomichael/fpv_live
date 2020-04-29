package dji.common.mission.intelligenthotpoint;

import android.graphics.RectF;
import dji.common.error.DJIError;
import dji.common.flightcontroller.flightassistant.IntelligentHotpointMissionMode;
import dji.common.flightcontroller.flightassistant.PointOfInterestExecutingState;
import dji.common.mission.MissionUtils;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataEyeGetPushPOIExecutionParams;
import dji.midware.data.model.P3.DataEyeGetPushPOITargetInformation;

@EXClassNullAway
public class IntelligentHotpointExecutionData {
    private float currentAltitude;
    private float currentRadius;
    private DJIError error;
    private LocationCoordinate2D hotpoint = null;
    private boolean isInitializing;
    private float maxVelocity;
    private IntelligentHotpointMissionMode missionMode;
    private RectF rectF = null;
    private PointOfInterestExecutingState state;
    private float targetAltitude;
    private float targetRadius;
    private float velocity;

    public IntelligentHotpointExecutionData() {
        if (!this.isInitializing) {
            this.isInitializing = true;
        }
    }

    public void updateMissionData(DataEyeGetPushPOIExecutionParams params, DJIError error2) {
        if (params != null) {
            this.currentRadius = params.getCircleRadius();
            this.targetRadius = params.getTargetRadius();
            this.currentAltitude = params.getHeight();
            this.targetAltitude = params.getTargetHeight();
            this.velocity = params.getSpeed();
            this.maxVelocity = params.getMaxSpeed();
            this.hotpoint = new LocationCoordinate2D(params.getLatitude(), params.getLongitude());
            this.missionMode = IntelligentHotpointMissionMode.find(params.getWorkMode());
            this.state = PointOfInterestExecutingState.find(params.getState());
            this.error = error2;
        }
    }

    public boolean isInitializing() {
        return this.isInitializing;
    }

    public float getRadius() {
        return this.currentRadius;
    }

    public float getTargetRadius() {
        return this.targetRadius;
    }

    public float getAltitude() {
        return this.currentAltitude;
    }

    public float getTargetAltitude() {
        return this.targetAltitude;
    }

    public float getAngularVelocity() {
        return (float) MissionUtils.Degree((double) (this.velocity / this.currentRadius));
    }

    public float getMaxAngularVelocity() {
        return (float) MissionUtils.Degree((double) (this.maxVelocity / this.currentRadius));
    }

    public void setRectF(DataEyeGetPushPOITargetInformation poiTargetInfo) {
        this.rectF = new RectF(poiTargetInfo.getCenterX() - (poiTargetInfo.getWidth() / 2.0f), poiTargetInfo.getCenterY() - (poiTargetInfo.getHeight() / 2.0f), poiTargetInfo.getCenterX() + (poiTargetInfo.getWidth() / 2.0f), poiTargetInfo.getCenterY() + (poiTargetInfo.getHeight() / 2.0f));
    }

    public RectF getTargetRectF() {
        return this.rectF;
    }

    public LocationCoordinate2D getHotpoint() {
        return this.hotpoint;
    }

    public IntelligentHotpointMissionMode getMissionMode() {
        return this.missionMode;
    }

    public DJIError getError() {
        return this.error;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        int floatToIntBits = ((((((((((((((((((((Float.floatToIntBits(this.currentRadius) * 31) + Float.floatToIntBits(this.targetRadius)) * 31) + Float.floatToIntBits(this.currentAltitude)) * 31) + Float.floatToIntBits(this.targetAltitude)) * 31) + Float.floatToIntBits(this.velocity)) * 31) + Float.floatToIntBits(this.maxVelocity)) * 31) + Float.floatToIntBits(this.rectF.left)) * 31) + Float.floatToIntBits(this.rectF.top)) * 31) + Float.floatToIntBits(this.rectF.right)) * 31) + Float.floatToIntBits(this.rectF.bottom)) * 31) + (this.isInitializing ? 1 : 0)) * 31;
        if (this.hotpoint != null) {
            i = this.hotpoint.hashCode();
        } else {
            i = 0;
        }
        int i5 = (floatToIntBits + i) * 31;
        if (this.missionMode != null) {
            i2 = this.missionMode.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 31;
        if (this.state != null) {
            i3 = this.state.hashCode();
        } else {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 31;
        if (this.error != null) {
            i4 = this.error.hashCode();
        }
        return i7 + i4;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        boolean isEqual = false;
        if (obj instanceof IntelligentHotpointExecutionData) {
            IntelligentHotpointExecutionData object = (IntelligentHotpointExecutionData) obj;
            if (object.isInitializing == this.isInitializing && object.currentRadius == this.currentRadius && object.targetRadius == this.targetRadius && object.currentAltitude == this.currentAltitude && object.targetAltitude == this.targetAltitude && object.velocity == this.velocity && object.maxVelocity == this.maxVelocity && isObjectEqual(object)) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        }
        return isEqual;
    }

    private boolean isObjectEqual(IntelligentHotpointExecutionData object) {
        boolean result = false;
        if (object == null) {
            return false;
        }
        if (object.hotpoint != null && object.hotpoint.equals(this.hotpoint)) {
            result = true;
        }
        if (object.rectF != null && object.rectF.equals(this.rectF)) {
            result = true;
        }
        if (object.missionMode != null && object.missionMode.equals(this.missionMode)) {
            result = true;
        }
        if (object.state != null && object.state.equals(this.state)) {
            result = true;
        }
        if (object.error != null && object.error.equals(this.error)) {
            result = true;
        }
        return result;
    }
}
