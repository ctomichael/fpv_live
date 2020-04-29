package dji.common.mission.hotpoint;

import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class HotpointMission {
    public static final double MAX_ALTITUDE = 500.0d;
    public static final double MAX_RADIUS = 500.0d;
    public static final double MIN_ALTITUDE = 5.0d;
    public static final double MIN_RADIUS = 5.0d;
    private double altitude;
    private float angularVelocity;
    private HotpointHeading heading;
    private LocationCoordinate2D hotpoint;
    private boolean isClockwise = false;
    private double radius;
    private HotpointStartPoint startPoint;

    public HotpointMission(LocationCoordinate2D hotpoint2, double altitude2, double radius2, float angularVelocity2, boolean isClockwise2, HotpointStartPoint startPoint2, HotpointHeading heading2) {
        this.hotpoint = hotpoint2;
        this.altitude = altitude2;
        this.radius = radius2;
        this.angularVelocity = angularVelocity2;
        this.isClockwise = isClockwise2;
        this.startPoint = startPoint2;
        this.heading = heading2;
    }

    public HotpointMission() {
    }

    public void resetMissionWithData(HotpointMission data) {
        this.hotpoint = data.hotpoint;
        this.altitude = data.altitude;
        this.radius = data.radius;
        this.angularVelocity = data.angularVelocity;
        this.isClockwise = data.isClockwise;
        this.startPoint = data.startPoint;
        this.heading = data.heading;
    }

    public LocationCoordinate2D getHotpoint() {
        return this.hotpoint;
    }

    public void setHotpoint(LocationCoordinate2D hotpoint2) {
        this.hotpoint = hotpoint2;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double altitude2) {
        this.altitude = altitude2;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius2) {
        this.radius = radius2;
    }

    public float getAngularVelocity() {
        return this.angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity2) {
        this.angularVelocity = angularVelocity2;
    }

    public boolean isClockwise() {
        return this.isClockwise;
    }

    public void setClockwise(boolean clockwise) {
        this.isClockwise = clockwise;
    }

    public HotpointStartPoint getStartPoint() {
        return this.startPoint;
    }

    public void setStartPoint(HotpointStartPoint startPoint2) {
        this.startPoint = startPoint2;
    }

    public HotpointHeading getHeading() {
        return this.heading;
    }

    public void setHeading(HotpointHeading heading2) {
        this.heading = heading2;
    }

    @Nullable
    public DJIError checkParameters() {
        boolean z = true;
        boolean isValid = true & this.hotpoint.isValid() & (this.altitude >= 5.0d && this.altitude <= 500.0d);
        if (this.radius < 5.0d || this.radius > 500.0d) {
            z = false;
        }
        if (isValid && z) {
            return null;
        }
        return DJIError.COMMON_PARAM_INVALID;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof HotpointMission)) {
            return false;
        }
        HotpointMission that = (HotpointMission) o;
        if (Double.compare(that.getAltitude(), getAltitude()) != 0 || Double.compare(that.getRadius(), getRadius()) != 0 || Float.compare(that.getAngularVelocity(), getAngularVelocity()) != 0 || isClockwise() != that.isClockwise()) {
            return false;
        }
        if (getHotpoint() != null) {
            if (!getHotpoint().equals(that.getHotpoint())) {
                return false;
            }
        } else if (that.getHotpoint() != null) {
            return false;
        }
        if (getStartPoint() != that.getStartPoint()) {
            return false;
        }
        if (getHeading() != that.getHeading()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4 = 0;
        if (getHotpoint() != null) {
            result = getHotpoint().hashCode();
        } else {
            result = 0;
        }
        long temp = Double.doubleToLongBits(getAltitude());
        int result2 = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        long temp2 = Double.doubleToLongBits(getRadius());
        int i5 = ((result2 * 31) + ((int) ((temp2 >>> 32) ^ temp2))) * 31;
        if (getAngularVelocity() != 0.0f) {
            i = Float.floatToIntBits(getAngularVelocity());
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (isClockwise()) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (getStartPoint() != null) {
            i3 = getStartPoint().hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (getHeading() != null) {
            i4 = getHeading().hashCode();
        }
        return i8 + i4;
    }
}
