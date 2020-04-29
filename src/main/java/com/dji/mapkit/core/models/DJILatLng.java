package com.dji.mapkit.core.models;

import com.dji.mapkit.core.utils.DJIGpsUtils;
import java.io.Serializable;

public class DJILatLng implements Serializable {
    public static final float ACCURACY_GPS_FINE = 16.0f;
    public static final float HIGH_ACCURACY_GPS_FINE = 6.0f;
    private static final String TAG = DJILatLng.class.getSimpleName();
    private static final double ZERO_DEBOUNCE_THRESHOLD = 1.0E-8d;
    private static final long serialVersionUID = 1038200165218942703L;
    public float accuracy;
    public double altitude;
    public long elapsedRealtimeNanos;
    public double latitude;
    public double longitude;
    public long time;

    public DJILatLng(double latitude2, double longitude2) {
        this(latitude2, longitude2, 0.0d);
    }

    public DJILatLng(double latitude2, double longitude2, double altitude2) {
        this(latitude2, longitude2, altitude2, 0.0f);
    }

    public DJILatLng(double latitude2, double longitude2, double altitude2, float accuracy2) {
        this(latitude2, longitude2, altitude2, accuracy2, 0, 0);
    }

    public DJILatLng(double latitude2, double longitude2, double altitude2, float accuracy2, long time2) {
        this(latitude2, longitude2, altitude2, accuracy2, time2, 0);
    }

    public DJILatLng(double latitude2, double longitude2, double altitude2, float accuracy2, long time2, long elapsedRealtimeNanos2) {
        this.latitude = latitude2;
        this.longitude = longitude2;
        this.altitude = altitude2;
        this.accuracy = accuracy2;
        this.time = time2;
        this.elapsedRealtimeNanos = elapsedRealtimeNanos2;
    }

    public DJILatLng(DJILatLng latLng) {
        this(latLng.latitude, latLng.longitude, latLng.altitude, latLng.accuracy, latLng.time, latLng.elapsedRealtimeNanos);
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

    public double getAltitude() {
        return this.altitude;
    }

    public float getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(float accuracy2) {
        this.accuracy = accuracy2;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time2) {
        this.time = time2;
    }

    public long getElapsedRealtimeNanos() {
        return this.elapsedRealtimeNanos;
    }

    public void setElapsedRealtimeNanos(long elapsedRealtimeNanos2) {
        this.elapsedRealtimeNanos = elapsedRealtimeNanos2;
    }

    public boolean isAvailable() {
        return DJIGpsUtils.isAvailable(this.latitude, this.longitude);
    }

    public boolean isFineAccuracy() {
        return isFineAccuracy(16.0f);
    }

    public boolean isFineAccuracy(float meters) {
        return isFineAccuracy(this.accuracy, meters);
    }

    public void copyFrom(DJILatLng latLng) {
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
        this.altitude = latLng.altitude;
        this.accuracy = latLng.accuracy;
        this.time = latLng.time;
        this.elapsedRealtimeNanos = latLng.elapsedRealtimeNanos;
    }

    public static boolean isFineAccuracy(float accuracy2, float meter) {
        return 0.0f < accuracy2 && accuracy2 <= meter;
    }

    public static DJILatLng valueOf(String string) {
        String[] s = string.split(",");
        if (s.length != 2) {
            return null;
        }
        return new DJILatLng(Double.valueOf(s[0]).doubleValue(), Double.valueOf(s[1]).doubleValue());
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DJILatLng latLng = (DJILatLng) o;
        if (Double.compare(latLng.latitude, this.latitude) != 0 || Double.compare(latLng.longitude, this.longitude) != 0 || Double.compare(latLng.altitude, this.altitude) != 0 || Float.compare(latLng.accuracy, this.accuracy) != 0) {
            return false;
        }
        if (this.time != latLng.time) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.latitude);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.longitude);
        int result2 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
        long temp3 = Double.doubleToLongBits(this.altitude);
        return (((((result2 * 31) + ((int) ((temp3 >>> 32) ^ temp3))) * 31) + (this.accuracy != 0.0f ? Float.floatToIntBits(this.accuracy) : 0)) * 31) + ((int) (this.time ^ (this.time >>> 32)));
    }

    public String toString() {
        double lat = this.latitude;
        double lng = this.longitude;
        double altitude2 = this.altitude;
        return new StringBuilder(60).append("lat/lng: (").append(lat).append(",").append(lng).append(")").append(" altitude=").append(altitude2).append(" accuracy=").append(this.accuracy).toString();
    }
}
