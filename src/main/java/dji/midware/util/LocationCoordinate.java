package dji.midware.util;

public class LocationCoordinate {
    public float altitude;
    public double latitude;
    public double longitude;

    public LocationCoordinate(double latitude2, double longitude2, float altitude2) {
        this.longitude = longitude2;
        this.latitude = latitude2;
        this.altitude = altitude2;
    }

    public LocationCoordinate(double latitude2, double longitude2) {
        this.longitude = longitude2;
        this.latitude = latitude2;
    }
}
