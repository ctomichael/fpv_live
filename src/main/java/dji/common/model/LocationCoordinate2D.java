package dji.common.model;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.Serializable;

@EXClassNullAway
public class LocationCoordinate2D implements Serializable {
    private static final double HALF_METER_OFFSET = 4.49661E-6d;
    public static final double UNKNOWN = Double.MIN_VALUE;
    private final double latitude;
    private final double longitude;

    public LocationCoordinate2D(double latitude2, double longitude2) {
        this.latitude = latitude2;
        this.longitude = longitude2;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationCoordinate2D that = (LocationCoordinate2D) o;
        if (Double.compare(that.getLatitude(), getLatitude()) != 0) {
            return false;
        }
        if (Double.compare(that.getLongitude(), getLongitude()) != 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        return this.latitude >= -90.0d && this.latitude <= 90.0d && this.longitude >= -180.0d && this.longitude <= 180.0d;
    }

    public static boolean isValid(double latitude2, double longitude2) {
        return latitude2 >= -90.0d && latitude2 <= 90.0d && longitude2 >= -180.0d && longitude2 <= 180.0d;
    }

    public boolean isEqualPosition(LocationCoordinate2D other) {
        if ((other instanceof LocationCoordinate2D) && Math.abs(this.latitude - other.getLatitude()) < HALF_METER_OFFSET && Math.abs(this.longitude - other.getLongitude()) < HALF_METER_OFFSET) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(getLatitude());
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(getLongitude());
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }

    public String toString() {
        return "LocationCoordinate2D{latitude=" + this.latitude + ", longitude=" + this.longitude + '}';
    }
}
