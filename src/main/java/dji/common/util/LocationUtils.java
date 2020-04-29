package dji.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.permission.Permission;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.publics.protocol.ResponseBase;
import dji.sdksharedlib.util.Util;
import java.util.Locale;

@EXClassNullAway
public class LocationUtils {
    private static final String TAG = "LocationUtils";
    private static Location currentBestLocation = null;
    private static LocationManager locationManager = null;

    public static boolean checkValidGPSCoordinate(double lat, double lng) {
        return lat >= -90.0d && lat <= 90.0d && lng >= -180.0d && lng <= 180.0d;
    }

    public static boolean validateLatitude(double latitude) {
        return latitude > 90.0d || latitude < -90.0d;
    }

    public static boolean validateLongitude(double longitude) {
        return longitude > 180.0d || longitude < -180.0d;
    }

    public static Location getLastBestLocation() {
        if (!checkLocationPermission()) {
            return null;
        }
        Context context = null;
        Location locationGPS = null;
        Location locationNet = null;
        try {
            context = Util.getApplication().getApplicationContext();
            if (locationManager == null) {
                locationManager = (LocationManager) Util.getApplication().getSystemService(ResponseBase.STRING_LOCATION);
            }
        } catch (Exception e) {
            DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
        }
        if (locationManager == null || context == null) {
            return null;
        }
        if (locationManager.isProviderEnabled("gps")) {
            locationGPS = locationManager.getLastKnownLocation("gps");
        }
        if (locationManager.isProviderEnabled("network")) {
            locationNet = locationManager.getLastKnownLocation("network");
        }
        long GPSLocationTime = 0;
        if (locationGPS != null) {
            GPSLocationTime = locationGPS.getTime();
        }
        long NetLocationTime = 0;
        if (locationNet != null) {
            NetLocationTime = locationNet.getTime();
        }
        if (0 < GPSLocationTime - NetLocationTime) {
            currentBestLocation = locationGPS;
        } else {
            currentBestLocation = locationNet;
        }
        if (currentBestLocation != null) {
            return currentBestLocation;
        }
        if (locationGPS != null) {
            return locationGPS;
        }
        if (locationNet != null) {
            return locationNet;
        }
        return null;
    }

    public static boolean checkLocationPermission() {
        int res = -1;
        try {
            res = ContextCompat.checkSelfPermission(Util.getApplication().getApplicationContext(), Permission.ACCESS_FINE_LOCATION);
        } catch (Exception e) {
            DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
        }
        if (res == 0) {
            return true;
        }
        return false;
    }

    @SuppressLint({"FloatMath"})
    public static double gps2m(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = (Math.sin(dLat / 2.0d) * Math.sin(dLat / 2.0d)) + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2.0d) * Math.sin(dLng / 2.0d));
        return (double) ((float) (6371000.0d * 2.0d * Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a))));
    }

    public static double gps2m(LocationCoordinate2D first, LocationCoordinate2D second) {
        return gps2m(first.getLatitude(), first.getLongitude(), second.getLatitude(), second.getLongitude());
    }

    public static double gps2m(LocationCoordinate2D first, double heightFirst, LocationCoordinate2D second, double heightSecond) {
        double horDistance = gps2m(first.getLatitude(), first.getLongitude(), second.getLatitude(), second.getLongitude());
        double heiDistance = Math.abs(heightFirst - heightSecond);
        return Math.sqrt((horDistance * horDistance) + (heiDistance * heiDistance));
    }

    public static double DegreeToRadian(double x) {
        return (3.141592653589793d * x) / 180.0d;
    }

    public static boolean isInUSA() {
        return Locale.getDefault().getISO3Country().equals("USA");
    }

    public static double getDistanceInMeterFromTwoGPSLocations(double firstLong, double firstLat, double secondLong, double secondLat) {
        double dlon = degreeToRadius(secondLong - firstLong);
        double dlat = degreeToRadius(secondLat - firstLat);
        double a = (Math.sin(dlat / 2.0d) * Math.sin(dlat / 2.0d)) + (Math.cos(degreeToRadius(firstLat)) * Math.cos(degreeToRadius(secondLat)) * Math.sin(dlon / 2.0d) * Math.sin(dlon / 2.0d));
        return 6371000.0d * 2.0d * Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a));
    }

    private static double degreeToRadius(double degree) {
        return 0.017453292519943295d * degree;
    }

    public static double radianToDegree(double x) {
        return (180.0d * x) / 3.141592653589793d;
    }

    public static float getDistance(DJILatLng p1, DJILatLng p2) {
        float[] results = new float[1];
        Location.distanceBetween(p1.latitude, p1.longitude, p2.latitude, p2.longitude, results);
        return results[0];
    }
}
