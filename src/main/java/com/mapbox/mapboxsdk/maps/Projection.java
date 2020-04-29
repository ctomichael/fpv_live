package com.mapbox.mapboxsdk.maps;

import android.graphics.PointF;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.geometry.ProjectedMeters;
import com.mapbox.mapboxsdk.geometry.VisibleRegion;
import java.util.ArrayList;

public class Projection {
    @NonNull
    private final MapView mapView;
    @NonNull
    private final NativeMap nativeMapView;

    Projection(@NonNull NativeMap nativeMapView2, @NonNull MapView mapView2) {
        this.nativeMapView = nativeMapView2;
        this.mapView = mapView2;
    }

    /* access modifiers changed from: package-private */
    public void setContentPadding(int[] contentPadding) {
        double[] output = new double[contentPadding.length];
        for (int i = 0; i < contentPadding.length; i++) {
            output[i] = (double) contentPadding[i];
        }
        this.nativeMapView.setContentPadding(output);
    }

    /* access modifiers changed from: package-private */
    public int[] getContentPadding() {
        double[] padding = this.nativeMapView.getCameraPosition().padding;
        return new int[]{(int) padding[0], (int) padding[1], (int) padding[2], (int) padding[3]};
    }

    @Deprecated
    public void invalidateContentPadding() {
    }

    @NonNull
    public ProjectedMeters getProjectedMetersForLatLng(@NonNull LatLng latLng) {
        return this.nativeMapView.projectedMetersForLatLng(latLng);
    }

    @NonNull
    public LatLng getLatLngForProjectedMeters(@NonNull ProjectedMeters projectedMeters) {
        return this.nativeMapView.latLngForProjectedMeters(projectedMeters);
    }

    public double getMetersPerPixelAtLatitude(@FloatRange(from = -90.0d, to = 90.0d) double latitude) {
        return this.nativeMapView.getMetersPerPixelAtLatitude(latitude);
    }

    @NonNull
    public LatLng fromScreenLocation(@NonNull PointF point) {
        return this.nativeMapView.latLngForPixel(point);
    }

    @NonNull
    public VisibleRegion getVisibleRegion() {
        return getVisibleRegion(true);
    }

    @NonNull
    public VisibleRegion getVisibleRegion(boolean ignorePadding) {
        float left;
        float right;
        float top;
        float bottom;
        if (ignorePadding) {
            left = 0.0f;
            right = (float) this.mapView.getWidth();
            top = 0.0f;
            bottom = (float) this.mapView.getHeight();
        } else {
            int[] contentPadding = getContentPadding();
            left = (float) contentPadding[0];
            right = (float) (this.mapView.getWidth() - contentPadding[2]);
            top = (float) contentPadding[1];
            bottom = (float) (this.mapView.getHeight() - contentPadding[3]);
        }
        LatLng center = fromScreenLocation(new PointF(((right - left) / 2.0f) + left, ((bottom - top) / 2.0f) + top));
        LatLng topLeft = fromScreenLocation(new PointF(left, top));
        LatLng topRight = fromScreenLocation(new PointF(right, top));
        LatLng bottomRight = fromScreenLocation(new PointF(right, bottom));
        LatLng bottomLeft = fromScreenLocation(new PointF(left, bottom));
        ArrayList<LatLng> arrayList = new ArrayList();
        arrayList.add(topRight);
        arrayList.add(bottomRight);
        arrayList.add(bottomLeft);
        arrayList.add(topLeft);
        double maxEastLonSpan = 0.0d;
        double maxWestLonSpan = 0.0d;
        double east = 0.0d;
        double west = 0.0d;
        double north = -90.0d;
        double south = 90.0d;
        for (LatLng latLng : arrayList) {
            if (bearing(center, latLng) >= 0.0d) {
                double span = getLongitudeSpan(latLng.getLongitude(), center.getLongitude());
                if (span > maxEastLonSpan) {
                    maxEastLonSpan = span;
                    east = latLng.getLongitude();
                }
            } else {
                double span2 = getLongitudeSpan(center.getLongitude(), latLng.getLongitude());
                if (span2 > maxWestLonSpan) {
                    maxWestLonSpan = span2;
                    west = latLng.getLongitude();
                }
            }
            if (north < latLng.getLatitude()) {
                north = latLng.getLatitude();
            }
            if (south > latLng.getLatitude()) {
                south = latLng.getLatitude();
            }
        }
        if (east < west) {
            return new VisibleRegion(topLeft, topRight, bottomLeft, bottomRight, LatLngBounds.from(north, east + 360.0d, south, west));
        }
        return new VisibleRegion(topLeft, topRight, bottomLeft, bottomRight, LatLngBounds.from(north, east, south, west));
    }

    static double bearing(@NonNull LatLng latLng1, @NonNull LatLng latLng2) {
        double lon1 = degreesToRadians(latLng1.getLongitude());
        double lon2 = degreesToRadians(latLng2.getLongitude());
        double lat1 = degreesToRadians(latLng1.getLatitude());
        double lat2 = degreesToRadians(latLng2.getLatitude());
        return radiansToDegrees(Math.atan2(Math.sin(lon2 - lon1) * Math.cos(lat2), (Math.cos(lat1) * Math.sin(lat2)) - ((Math.sin(lat1) * Math.cos(lat2)) * Math.cos(lon2 - lon1))));
    }

    static double degreesToRadians(double degrees) {
        return (3.141592653589793d * (degrees % 360.0d)) / 180.0d;
    }

    static double radiansToDegrees(double radians) {
        return (180.0d * (radians % 6.283185307179586d)) / 3.141592653589793d;
    }

    static double getLongitudeSpan(double east, double west) {
        double longSpan = Math.abs(east - west);
        return east > west ? longSpan : 360.0d - longSpan;
    }

    @NonNull
    public PointF toScreenLocation(@NonNull LatLng location) {
        return this.nativeMapView.pixelForLatLng(location);
    }

    /* access modifiers changed from: package-private */
    public float getHeight() {
        return (float) this.mapView.getHeight();
    }

    /* access modifiers changed from: package-private */
    public float getWidth() {
        return (float) this.mapView.getWidth();
    }

    public double calculateZoom(float minScale) {
        return this.nativeMapView.getZoom() + (Math.log((double) minScale) / Math.log(2.0d));
    }
}
