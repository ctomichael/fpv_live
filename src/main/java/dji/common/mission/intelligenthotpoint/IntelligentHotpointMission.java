package dji.common.mission.intelligenthotpoint;

import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class IntelligentHotpointMission {
    public static final double MAX_ALTITUDE = 500.0d;
    public static final double MAX_RADIUS = 500.0d;
    public static final double MIN_ALTITUDE = 5.0d;
    public static final double MIN_RADIUS = 5.0d;
    private float altitude;
    private float angularVelocity;
    private LocationCoordinate2D hotpoint;
    private float radius;

    public IntelligentHotpointMission(LocationCoordinate2D hotpoint2, float altitude2, float radius2, float angularVelocity2) {
        this.hotpoint = hotpoint2;
        this.altitude = altitude2;
        this.radius = radius2;
        this.angularVelocity = angularVelocity2;
    }

    public IntelligentHotpointMission(LocationCoordinate2D hotpoint2) {
        this.hotpoint = hotpoint2;
    }

    public IntelligentHotpointMission() {
    }

    public void resetMissionWithData(IntelligentHotpointMission data) {
        this.hotpoint = data.hotpoint;
    }

    public void setHotpoint(LocationCoordinate2D hotpoint2) {
        this.hotpoint = hotpoint2;
    }

    public LocationCoordinate2D getHotpoint() {
        return this.hotpoint;
    }

    public void setAltitude(float altitude2) {
        this.altitude = altitude2;
    }

    public float getAltitude() {
        return this.altitude;
    }

    public void setRadius(float radius2) {
        this.radius = radius2;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setAngularVelocity(float angularVelocity2) {
        this.angularVelocity = angularVelocity2;
    }

    public float getAngularVelocity() {
        return this.angularVelocity;
    }

    @Nullable
    public DJIError checkParameters() {
        if (true && this.hotpoint.isValid()) {
            return null;
        }
        return DJIError.COMMON_PARAM_INVALID;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntelligentHotpointMission)) {
            return false;
        }
        IntelligentHotpointMission that = (IntelligentHotpointMission) o;
        if (getHotpoint() != null) {
            if (getHotpoint().equals(that.getHotpoint())) {
                return true;
            }
        } else if (that.getHotpoint() == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (getHotpoint() != null) {
            return getHotpoint().hashCode();
        }
        return 0;
    }
}
