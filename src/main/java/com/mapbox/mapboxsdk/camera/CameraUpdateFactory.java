package com.mapbox.mapboxsdk.camera;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;
import com.mapbox.mapboxsdk.maps.UiSettings;
import java.util.Arrays;

public final class CameraUpdateFactory {
    public static CameraUpdate newCameraPosition(@NonNull CameraPosition cameraPosition) {
        return new CameraPositionUpdate(cameraPosition.bearing, cameraPosition.target, cameraPosition.tilt, cameraPosition.zoom, cameraPosition.padding);
    }

    public static CameraUpdate newLatLng(@NonNull LatLng latLng) {
        return new CameraPositionUpdate(-1.0d, latLng, -1.0d, -1.0d, null);
    }

    public static CameraUpdate newLatLngBounds(@NonNull LatLngBounds bounds, int padding) {
        return newLatLngBounds(bounds, padding, padding, padding, padding);
    }

    public static CameraUpdate newLatLngBounds(@NonNull LatLngBounds bounds, double bearing, double tilt, int padding) {
        return newLatLngBounds(bounds, bearing, tilt, padding, padding, padding, padding);
    }

    public static CameraUpdate newLatLngBounds(@NonNull LatLngBounds bounds, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        return new CameraBoundsUpdate(bounds, null, null, paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public static CameraUpdate newLatLngBounds(@NonNull LatLngBounds bounds, double bearing, double tilt, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        return new CameraBoundsUpdate(bounds, Double.valueOf(bearing), Double.valueOf(tilt), paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public static CameraUpdate newLatLngZoom(@NonNull LatLng latLng, double zoom) {
        return new CameraPositionUpdate(-1.0d, latLng, -1.0d, zoom, null);
    }

    public static CameraUpdate newLatLngPadding(@NonNull LatLng latLng, double left, double top, double right, double bottom) {
        return new CameraPositionUpdate(-1.0d, latLng, -1.0d, -1.0d, new double[]{left, top, right, bottom});
    }

    public static CameraUpdate zoomBy(double amount, Point focus) {
        return new ZoomUpdate(amount, (float) focus.x, (float) focus.y);
    }

    public static CameraUpdate zoomBy(double amount) {
        return new ZoomUpdate(2, amount);
    }

    public static CameraUpdate zoomIn() {
        return new ZoomUpdate(0);
    }

    public static CameraUpdate zoomOut() {
        return new ZoomUpdate(1);
    }

    public static CameraUpdate zoomTo(double zoom) {
        return new ZoomUpdate(3, zoom);
    }

    public static CameraUpdate bearingTo(double bearing) {
        return new CameraPositionUpdate(bearing, null, -1.0d, -1.0d, null);
    }

    public static CameraUpdate tiltTo(double tilt) {
        return new CameraPositionUpdate(-1.0d, null, tilt, -1.0d, null);
    }

    public static CameraUpdate paddingTo(double[] padding) {
        return new CameraPositionUpdate(-1.0d, null, -1.0d, -1.0d, padding);
    }

    public static CameraUpdate paddingTo(double left, double top, double right, double bottom) {
        return paddingTo(new double[]{left, top, right, bottom});
    }

    static final class CameraPositionUpdate implements CameraUpdate {
        private final double bearing;
        private final double[] padding;
        private final LatLng target;
        private final double tilt;
        private final double zoom;

        CameraPositionUpdate(double bearing2, LatLng target2, double tilt2, double zoom2, double[] padding2) {
            this.bearing = bearing2;
            this.target = target2;
            this.tilt = tilt2;
            this.zoom = zoom2;
            this.padding = padding2;
        }

        public LatLng getTarget() {
            return this.target;
        }

        public double getBearing() {
            return this.bearing;
        }

        public double getTilt() {
            return this.tilt;
        }

        public double getZoom() {
            return this.zoom;
        }

        public double[] getPadding() {
            return this.padding;
        }

        public CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap) {
            if (this.target != null) {
                return new CameraPosition.Builder(this).build();
            }
            return new CameraPosition.Builder(this).target(mapboxMap.getCameraPosition().target).build();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CameraPositionUpdate that = (CameraPositionUpdate) o;
            if (Double.compare(that.bearing, this.bearing) != 0 || Double.compare(that.tilt, this.tilt) != 0 || Double.compare(that.zoom, this.zoom) != 0) {
                return false;
            }
            if (this.target != null) {
                if (!this.target.equals(that.target)) {
                    return false;
                }
            } else if (that.target != null) {
                return false;
            }
            return Arrays.equals(this.padding, that.padding);
        }

        public int hashCode() {
            long temp = Double.doubleToLongBits(this.bearing);
            int result = (((int) ((temp >>> 32) ^ temp)) * 31) + (this.target != null ? this.target.hashCode() : 0);
            long temp2 = Double.doubleToLongBits(this.tilt);
            int result2 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
            long temp3 = Double.doubleToLongBits(this.zoom);
            return (((result2 * 31) + ((int) ((temp3 >>> 32) ^ temp3))) * 31) + Arrays.hashCode(this.padding);
        }

        public String toString() {
            return "CameraPositionUpdate{bearing=" + this.bearing + ", target=" + this.target + ", tilt=" + this.tilt + ", zoom=" + this.zoom + ", padding=" + Arrays.toString(this.padding) + '}';
        }
    }

    static final class CameraBoundsUpdate implements CameraUpdate {
        static final /* synthetic */ boolean $assertionsDisabled = (!CameraUpdateFactory.class.desiredAssertionStatus());
        private final Double bearing;
        private final LatLngBounds bounds;
        private final int[] padding;
        private final Double tilt;

        CameraBoundsUpdate(LatLngBounds bounds2, Double bearing2, Double tilt2, int[] padding2) {
            this.bounds = bounds2;
            this.padding = padding2;
            this.bearing = bearing2;
            this.tilt = tilt2;
        }

        CameraBoundsUpdate(LatLngBounds bounds2, Double bearing2, Double tilt2, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
            this(bounds2, bearing2, tilt2, new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom});
        }

        public LatLngBounds getBounds() {
            return this.bounds;
        }

        public int[] getPadding() {
            return this.padding;
        }

        public CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap) {
            if (this.bearing == null && this.tilt == null) {
                return mapboxMap.getCameraForLatLngBounds(this.bounds, this.padding);
            }
            if (!$assertionsDisabled && this.bearing == null) {
                throw new AssertionError();
            } else if ($assertionsDisabled || this.tilt != null) {
                return mapboxMap.getCameraForLatLngBounds(this.bounds, this.padding, this.bearing.doubleValue(), this.tilt.doubleValue());
            } else {
                throw new AssertionError();
            }
        }

        public boolean equals(@Nullable Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CameraBoundsUpdate that = (CameraBoundsUpdate) o;
            if (this.bounds.equals(that.bounds)) {
                return Arrays.equals(this.padding, that.padding);
            }
            return false;
        }

        public int hashCode() {
            return (this.bounds.hashCode() * 31) + Arrays.hashCode(this.padding);
        }

        public String toString() {
            return "CameraBoundsUpdate{bounds=" + this.bounds + ", padding=" + Arrays.toString(this.padding) + '}';
        }
    }

    static final class CameraMoveUpdate implements CameraUpdate {
        private float x;
        private float y;

        CameraMoveUpdate(float x2, float y2) {
            this.x = x2;
            this.y = y2;
        }

        public CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap) {
            UiSettings uiSettings = mapboxMap.getUiSettings();
            Projection projection = mapboxMap.getProjection();
            float viewPortWidth = uiSettings.getWidth();
            float viewPortHeight = uiSettings.getHeight();
            int[] padding = mapboxMap.getPadding();
            LatLng latLng = projection.fromScreenLocation(new PointF((((viewPortWidth - ((float) padding[0])) + ((float) padding[1])) / 2.0f) + this.x, (((((float) padding[1]) + viewPortHeight) - ((float) padding[3])) / 2.0f) + this.y));
            CameraPosition previousPosition = mapboxMap.getCameraPosition();
            return new CameraPosition.Builder().target(latLng).zoom(previousPosition.zoom).tilt(previousPosition.tilt).bearing(previousPosition.bearing).build();
        }

        public boolean equals(@Nullable Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CameraMoveUpdate that = (CameraMoveUpdate) o;
            if (Float.compare(that.x, this.x) != 0) {
                return false;
            }
            if (Float.compare(that.y, this.y) != 0) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int result;
            int i = 0;
            if (this.x != 0.0f) {
                result = Float.floatToIntBits(this.x);
            } else {
                result = 0;
            }
            int i2 = result * 31;
            if (this.y != 0.0f) {
                i = Float.floatToIntBits(this.y);
            }
            return i2 + i;
        }

        public String toString() {
            return "CameraMoveUpdate{x=" + this.x + ", y=" + this.y + '}';
        }
    }

    static final class ZoomUpdate implements CameraUpdate {
        static final int ZOOM_BY = 2;
        static final int ZOOM_IN = 0;
        static final int ZOOM_OUT = 1;
        static final int ZOOM_TO = 3;
        static final int ZOOM_TO_POINT = 4;
        private final int type;
        private float x;
        private float y;
        private final double zoom;

        ZoomUpdate(int type2) {
            this.type = type2;
            this.zoom = 0.0d;
        }

        ZoomUpdate(int type2, double zoom2) {
            this.type = type2;
            this.zoom = zoom2;
        }

        ZoomUpdate(double zoom2, float x2, float y2) {
            this.type = 4;
            this.zoom = zoom2;
            this.x = x2;
            this.y = y2;
        }

        public double getZoom() {
            return this.zoom;
        }

        public int getType() {
            return this.type;
        }

        public float getX() {
            return this.x;
        }

        public float getY() {
            return this.y;
        }

        /* access modifiers changed from: package-private */
        public double transformZoom(double currentZoom) {
            switch (getType()) {
                case 0:
                    return currentZoom + 1.0d;
                case 1:
                    double currentZoom2 = currentZoom - 1.0d;
                    if (currentZoom2 < 0.0d) {
                        return 0.0d;
                    }
                    return currentZoom2;
                case 2:
                    return currentZoom + getZoom();
                case 3:
                    return getZoom();
                case 4:
                    return currentZoom + getZoom();
                default:
                    return currentZoom;
            }
        }

        public CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap) {
            CameraPosition cameraPosition = mapboxMap.getCameraPosition();
            if (getType() != 4) {
                return new CameraPosition.Builder(cameraPosition).zoom(transformZoom(cameraPosition.zoom)).build();
            }
            return new CameraPosition.Builder(cameraPosition).zoom(transformZoom(cameraPosition.zoom)).target(mapboxMap.getProjection().fromScreenLocation(new PointF(getX(), getY()))).build();
        }

        public boolean equals(@Nullable Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ZoomUpdate that = (ZoomUpdate) o;
            if (this.type != that.type || Double.compare(that.zoom, this.zoom) != 0 || Float.compare(that.x, this.x) != 0) {
                return false;
            }
            if (Float.compare(that.y, this.y) != 0) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int i;
            int i2 = 0;
            int result = this.type;
            long temp = Double.doubleToLongBits(this.zoom);
            int i3 = ((result * 31) + ((int) ((temp >>> 32) ^ temp))) * 31;
            if (this.x != 0.0f) {
                i = Float.floatToIntBits(this.x);
            } else {
                i = 0;
            }
            int i4 = (i3 + i) * 31;
            if (this.y != 0.0f) {
                i2 = Float.floatToIntBits(this.y);
            }
            return i4 + i2;
        }

        public String toString() {
            return "ZoomUpdate{type=" + this.type + ", zoom=" + this.zoom + ", x=" + this.x + ", y=" + this.y + '}';
        }
    }
}
