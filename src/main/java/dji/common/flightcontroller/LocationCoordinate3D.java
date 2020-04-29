package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class LocationCoordinate3D {
    private float altitude;
    private double latitude;
    private double longitude;

    public LocationCoordinate3D(double latitude2, double longitude2, float altitude2) {
        this.longitude = longitude2;
        this.latitude = latitude2;
        this.altitude = altitude2;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude2) {
        this.latitude = latitude2;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude2) {
        this.longitude = longitude2;
    }

    public float getAltitude() {
        return this.altitude;
    }

    public void setAltitude(float altitude2) {
        this.altitude = altitude2;
    }

    public String toString() {
        return "LocationCoordinate3D{latitude=" + this.latitude + ", longitude=" + this.longitude + ", altitude=" + this.altitude + '}';
    }

    public boolean equals(Object obj) {
        boolean z;
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof LocationCoordinate3D)) {
            return super.equals(obj);
        }
        if (Double.compare(((LocationCoordinate3D) obj).latitude, this.latitude) == 0 && Double.compare(((LocationCoordinate3D) obj).longitude, this.longitude) == 0 && Float.compare(((LocationCoordinate3D) obj).altitude, this.altitude) == 0) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result = Float.floatToIntBits(this.altitude);
        long bits = Double.doubleToLongBits(this.longitude);
        int result2 = (result * 31) + ((int) ((bits >>> 32) ^ bits));
        long bits2 = Double.doubleToLongBits(this.latitude);
        return (result2 * 31) + ((int) ((bits2 >>> 32) ^ bits2));
    }
}
