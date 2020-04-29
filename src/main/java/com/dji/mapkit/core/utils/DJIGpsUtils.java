package com.dji.mapkit.core.utils;

import com.dji.mapkit.core.Mapkit;
import com.dji.mapkit.core.models.DJILatLng;

public class DJIGpsUtils {
    private static final double M_PI = 3.141592653589793d;
    public static boolean OPEN = true;
    private static final double ZERO_DEBOUNCE_THRESHOLD = 8.99322E-6d;
    private static Rectangle[] exclude = {new Rectangle(25.398623d, 119.921265d, 21.785006d, 122.497559d), new Rectangle(22.284d, 101.8652d, 20.0988d, 106.665d), new Rectangle(21.5422d, 106.4525d, 20.4878d, 108.051d), new Rectangle(55.8175d, 109.0323d, 50.3257d, 119.127d), new Rectangle(55.8175d, 127.4568d, 49.5574d, 137.0227d), new Rectangle(44.8922d, 131.2662d, 42.5692d, 137.0227d)};
    private static Rectangle[] region = {new Rectangle(49.2204d, 79.4462d, 42.8899d, 96.33d), new Rectangle(54.1415d, 109.6872d, 39.3742d, 135.0002d), new Rectangle(42.8899d, 73.1246d, 29.5297d, 124.143255d), new Rectangle(29.5297d, 82.9684d, 26.7186d, 97.0352d), new Rectangle(29.5297d, 97.0253d, 20.414096d, 124.367395d), new Rectangle(20.414096d, 107.975793d, 17.871542d, 111.744104d)};
    private static final DeltaLatLngCache sDeltaLatLngCache = new DeltaLatLngCache();

    private static double transformLat(double x, double y) {
        return -100.0d + (2.0d * x) + (3.0d * y) + (0.2d * y * y) + (0.1d * x * y) + (0.2d * Math.sqrt(Math.abs(x))) + ((((20.0d * Math.sin((6.0d * x) * 3.141592653589793d)) + (20.0d * Math.sin((2.0d * x) * 3.141592653589793d))) * 2.0d) / 3.0d) + ((((20.0d * Math.sin(3.141592653589793d * y)) + (40.0d * Math.sin((y / 3.0d) * 3.141592653589793d))) * 2.0d) / 3.0d) + ((((160.0d * Math.sin((y / 12.0d) * 3.141592653589793d)) + (320.0d * Math.sin((3.141592653589793d * y) / 30.0d))) * 2.0d) / 3.0d);
    }

    private static double transformLon(double x, double y) {
        return 300.0d + x + (2.0d * y) + (0.1d * x * x) + (0.1d * x * y) + (0.1d * Math.sqrt(Math.abs(x))) + ((((20.0d * Math.sin((6.0d * x) * 3.141592653589793d)) + (20.0d * Math.sin((2.0d * x) * 3.141592653589793d))) * 2.0d) / 3.0d) + ((((20.0d * Math.sin(3.141592653589793d * x)) + (40.0d * Math.sin((x / 3.0d) * 3.141592653589793d))) * 2.0d) / 3.0d) + ((((150.0d * Math.sin((x / 12.0d) * 3.141592653589793d)) + (300.0d * Math.sin((x / 30.0d) * 3.141592653589793d))) * 2.0d) / 3.0d);
    }

    private static DJILatLng delta(DJILatLng source) {
        double dLat = transformLat(source.longitude - 105.0d, source.latitude - 35.0d);
        double dLng = transformLon(source.longitude - 105.0d, source.latitude - 35.0d);
        double radLat = (source.latitude / 180.0d) * 3.141592653589793d;
        double magic = Math.sin(radLat);
        double magic2 = 1.0d - ((0.006693421622965943d * magic) * magic);
        double sqrtMagic = Math.sqrt(magic2);
        return new DJILatLng((180.0d * dLat) / ((((1.0d - 0.006693421622965943d) * 6378245.0d) / (magic2 * sqrtMagic)) * 3.141592653589793d), (180.0d * dLng) / (((6378245.0d / sqrtMagic) * Math.cos(radLat)) * 3.141592653589793d));
    }

    private static DeltaLatLngCache delta(double latitude, double longitude) {
        double dLat = transformLat(longitude - 105.0d, latitude - 35.0d);
        double dLng = transformLon(longitude - 105.0d, latitude - 35.0d);
        double radLat = (latitude / 180.0d) * 3.141592653589793d;
        double magic = Math.sin(radLat);
        double magic2 = 1.0d - ((0.006693421622965943d * magic) * magic);
        double sqrtMagic = Math.sqrt(magic2);
        double dLng2 = (180.0d * dLng) / (((6378245.0d / sqrtMagic) * Math.cos(radLat)) * 3.141592653589793d);
        double unused = sDeltaLatLngCache.latitude = (180.0d * dLat) / ((((1.0d - 0.006693421622965943d) * 6378245.0d) / (magic2 * sqrtMagic)) * 3.141592653589793d);
        double unused2 = sDeltaLatLngCache.longitude = dLng2;
        return sDeltaLatLngCache;
    }

    public static DJILatLng wgs2gcjInChina(DJILatLng source) {
        if (!Mapkit.isInMainlandChina() && !Mapkit.isInHongKong() && !Mapkit.isInMacau()) {
            return source;
        }
        DeltaLatLngCache cache = delta(source.getLatitude(), source.getLongitude());
        return new DJILatLng(source.getLatitude() + cache.latitude, source.getLongitude() + cache.longitude, source.getAltitude(), source.getAccuracy(), source.getTime());
    }

    public static DJILatLng wgs2gcjJustInMainlandChina(DJILatLng source) {
        if (!Mapkit.isInMainlandChina()) {
            return source;
        }
        DeltaLatLngCache cache = delta(source.getLatitude(), source.getLongitude());
        return new DJILatLng(source.getLatitude() + cache.latitude, source.getLongitude() + cache.longitude, source.getAltitude(), source.getAccuracy(), source.getTime());
    }

    public static DJILatLng gcj2wgsInChina(DJILatLng source) {
        if (!Mapkit.isInMainlandChina() && !Mapkit.isInHongKong() && !Mapkit.isInMacau()) {
            return source;
        }
        DJILatLng latLng = delta(source);
        return new DJILatLng(source.latitude - latLng.latitude, source.longitude - latLng.longitude, source.getAltitude(), source.getAccuracy(), source.getTime());
    }

    public static DJILatLng gcj2wgsJustInMainlandChina(DJILatLng source) {
        if (!Mapkit.isInMainlandChina()) {
            return source;
        }
        DJILatLng latLng = delta(source);
        return new DJILatLng(source.latitude - latLng.latitude, source.longitude - latLng.longitude, source.getAltitude(), source.getAccuracy(), source.getTime());
    }

    public static DJILatLng wgs2gcjMust(DJILatLng source) {
        if (!IsInsideChinaMust(source)) {
            return source;
        }
        DJILatLng latLng = delta(source);
        return new DJILatLng(source.latitude + latLng.latitude, source.longitude + latLng.longitude, source.getAltitude(), source.getAccuracy(), source.getTime());
    }

    public static DJILatLng gcj2wgsMust(DJILatLng source) {
        if (!Mapkit.isInMainlandChina()) {
            return source;
        }
        DJILatLng latLng = delta(source);
        return new DJILatLng(source.latitude - latLng.latitude, source.longitude - latLng.longitude, source.getAltitude(), source.getAccuracy(), source.getTime());
    }

    public static DJILatLng gcj2wgs_exact(DJILatLng source) {
        double latitude = 0.0d;
        double longitude = 0.0d;
        double mLat = source.latitude - 0.01d;
        double mLng = source.longitude - 0.01d;
        double pLat = source.latitude + 0.01d;
        double pLng = source.longitude + 0.01d;
        for (int i = 0; i < 30; i++) {
            latitude = (mLat + pLat) / 2.0d;
            longitude = (mLng + pLng) / 2.0d;
            DJILatLng latLng = new DJILatLng(latitude, longitude);
            DJILatLng tmp = wgs2gcjInChina(new DJILatLng(latitude, longitude));
            double dLat = tmp.latitude - source.latitude;
            double dLng = tmp.longitude - source.longitude;
            if (Math.abs(dLat) < 1.0E-6d && Math.abs(dLng) < 1.0E-6d) {
                return latLng;
            }
            if (dLat > 0.0d) {
                pLat = latitude;
            } else {
                mLat = latitude;
            }
            if (dLng > 0.0d) {
                pLng = longitude;
            } else {
                mLng = longitude;
            }
        }
        return new DJILatLng(latitude, longitude, source.getAltitude(), source.getAccuracy(), source.getTime());
    }

    public static double distance(double latA, double lngA, double latB, double lngB) {
        double s = (Math.cos((3.141592653589793d * latA) / 180.0d) * Math.cos((3.141592653589793d * latB) / 180.0d) * Math.cos(((lngA - lngB) * 3.141592653589793d) / 180.0d)) + (Math.sin((3.141592653589793d * latA) / 180.0d) * Math.sin((3.141592653589793d * latB) / 180.0d));
        if (s > 1.0d) {
            s = 1.0d;
        }
        if (s < -1.0d) {
            s = -1.0d;
        }
        return Math.acos(s) * 6371000.0d;
    }

    public static double distance(DJILatLng latLngA, DJILatLng latLngB) {
        return distance(latLngA.latitude, latLngA.longitude, latLngB.latitude, latLngB.longitude);
    }

    public static boolean isAvailable(double latitude, double longitude) {
        return Math.abs(latitude) <= 90.0d && Math.abs(longitude) <= 180.0d && (!isZero(latitude) || !isZero(longitude));
    }

    private static boolean isZero(double value) {
        return -8.99322E-6d <= value && value <= 8.99322E-6d;
    }

    private static class Rectangle {
        public double East;
        public double North;
        public double South;
        public double West;

        public Rectangle(double latitude1, double longitude1, double latitude2, double longitude2) {
            this.West = Math.min(longitude1, longitude2);
            this.North = Math.max(latitude1, latitude2);
            this.East = Math.max(longitude1, longitude2);
            this.South = Math.min(latitude1, latitude2);
        }
    }

    public static boolean IsInsideChina(DJILatLng pos) {
        if (!OPEN) {
            return false;
        }
        for (int i = 0; i < region.length; i++) {
            if (InRectangle(region[i], pos)) {
                for (int j = 0; j < exclude.length; j++) {
                    if (InRectangle(exclude[j], pos)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static double getBearing(double lat1, double long1, double lat2, double long2) {
        return (bearing(lat2, long2, lat1, long1) + 180.0d) % 360.0d;
    }

    private static double bearing(double lat1, double long1, double lat2, double long2) {
        double phi1 = lat1 * 0.017453292519943295d;
        double phi2 = lat2 * 0.017453292519943295d;
        double lam1 = long1 * 0.017453292519943295d;
        double lam2 = long2 * 0.017453292519943295d;
        return (Math.atan2(Math.sin(lam2 - lam1) * Math.cos(phi2), (Math.cos(phi1) * Math.sin(phi2)) - ((Math.sin(phi1) * Math.cos(phi2)) * Math.cos(lam2 - lam1))) * 180.0d) / 3.141592653589793d;
    }

    public static boolean IsInsideChinaMust(DJILatLng pos) {
        for (int i = 0; i < region.length; i++) {
            if (InRectangle(region[i], pos)) {
                for (int j = 0; j < exclude.length; j++) {
                    if (InRectangle(exclude[j], pos)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static boolean InRectangle(Rectangle rect, DJILatLng pos) {
        return rect.West <= pos.longitude && rect.East >= pos.longitude && rect.North >= pos.latitude && rect.South <= pos.latitude;
    }

    private static class DeltaLatLngCache {
        /* access modifiers changed from: private */
        public double latitude;
        /* access modifiers changed from: private */
        public double longitude;

        private DeltaLatLngCache() {
        }
    }
}
