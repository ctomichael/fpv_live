package dji.common.flightcontroller.virtualfence;

public class VirtualCircleFenceArea {
    private double centerLatitude = 0.0d;
    private double centerLongitude = 0.0d;
    private float radius = 0.0f;

    public VirtualCircleFenceArea(double centerLatitude2, double centerLongitude2, float radius2) {
        this.centerLatitude = centerLatitude2;
        this.centerLongitude = centerLongitude2;
        this.radius = radius2;
    }

    public double getCenterLongitude() {
        return this.centerLongitude;
    }

    public double getCenterLatitude() {
        return this.centerLatitude;
    }

    public float getRadius() {
        return this.radius;
    }
}
