package com.mapbox.android.telemetry.location;

import android.location.Location;
import android.support.v4.os.EnvironmentCompat;
import com.mapbox.android.telemetry.LocationEvent;
import java.math.BigDecimal;

public class LocationMapper {
    private static final double MAX_LONGITUDE = 180.0d;
    private static final double MIN_LONGITUDE = -180.0d;
    private static final int SEVEN_DIGITS_AFTER_DECIMAL = 7;
    private SessionIdentifier sessionIdentifier = new SessionIdentifier();

    public static LocationEvent create(Location location, String sessionId) {
        return createLocationEvent(location, EnvironmentCompat.MEDIA_UNKNOWN, sessionId);
    }

    public LocationEvent from(Location location, String applicationState) {
        return createLocationEvent(location, applicationState, this.sessionIdentifier.getSessionId());
    }

    public void updateSessionIdentifier(SessionIdentifier sessionIdentifier2) {
        this.sessionIdentifier = sessionIdentifier2;
    }

    private static LocationEvent createLocationEvent(Location location, String applicationState, String sessionId) {
        LocationEvent locationEvent = new LocationEvent(sessionId, round(location.getLatitude()), wrapLongitude(round(location.getLongitude())), applicationState);
        addAltitudeIfPresent(location, locationEvent);
        addAccuracyIfPresent(location, locationEvent);
        return locationEvent;
    }

    private static double round(double value) {
        return new BigDecimal(value).setScale(7, 1).doubleValue();
    }

    private static double wrapLongitude(double longitude) {
        double wrapped = longitude;
        if (longitude < -180.0d || longitude > 180.0d) {
            return wrap(longitude, -180.0d, 180.0d);
        }
        return wrapped;
    }

    private static double wrap(double value, double min, double max) {
        double delta = max - min;
        return ((((value - min) % delta) + delta) % delta) + min;
    }

    private static void addAltitudeIfPresent(Location location, LocationEvent locationEvent) {
        if (location.hasAltitude()) {
            locationEvent.setAltitude(Double.valueOf((double) Math.round(location.getAltitude())));
        }
    }

    private static void addAccuracyIfPresent(Location location, LocationEvent locationEvent) {
        if (location.hasAccuracy()) {
            locationEvent.setAccuracy(Float.valueOf((float) Math.round(location.getAccuracy())));
        }
    }
}
