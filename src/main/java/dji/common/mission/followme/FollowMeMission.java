package dji.common.mission.followme;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FollowMeMission {
    private float altitude = 0.0f;
    private FollowMeHeading heading = FollowMeHeading.TOWARD_FOLLOW_POSITION;
    private double latitude = 181.0d;
    private double longitude = 181.0d;

    public FollowMeMission(FollowMeHeading heading2, double latitude2, double longitude2, float altitude2) {
        this.heading = heading2;
        this.latitude = latitude2;
        this.longitude = longitude2;
        this.altitude = altitude2;
    }

    public int hashCode() {
        int result = this.heading != null ? this.heading.hashCode() : 0;
        long temp = Double.doubleToLongBits(this.latitude);
        int result2 = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        long temp2 = Double.doubleToLongBits(this.longitude);
        return (((result2 * 31) + ((int) ((temp2 >>> 32) ^ temp2))) * 31) + Float.floatToIntBits(this.altitude);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof FollowMeMission)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.heading == ((FollowMeMission) o).heading && this.latitude == ((FollowMeMission) o).latitude && this.longitude == ((FollowMeMission) o).longitude && this.altitude == ((FollowMeMission) o).altitude;
    }

    public FollowMeHeading getHeading() {
        return this.heading;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public float getAltitude() {
        return this.altitude;
    }
}
