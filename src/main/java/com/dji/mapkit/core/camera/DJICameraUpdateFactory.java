package com.dji.mapkit.core.camera;

import android.support.annotation.NonNull;
import com.dji.mapkit.core.maps.DJIMap;
import com.dji.mapkit.core.models.DJICameraPosition;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.models.DJILatLngBounds;

public final class DJICameraUpdateFactory {
    public static DJICameraUpdate newCameraPosition(@NonNull DJICameraPosition cameraPosition) {
        return new CameraPositionUpdate(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, cameraPosition.bearing);
    }

    public static DJICameraUpdate newLatLngBounds(@NonNull DJILatLngBounds bounds, int width, int height, int zoom, int padding) {
        return new CameraBoundsUpdate(bounds, width, height, zoom, padding);
    }

    public static DJICameraUpdate newLatLngBounds(@NonNull DJILatLngBounds bounds, int width, int height, int zoom) {
        return new CameraBoundsUpdate(bounds, width, height, zoom, 0);
    }

    public static DJICameraUpdate newLatLngBounds(@NonNull DJILatLngBounds bounds, int zoom, int padding) {
        return new CameraBoundsUpdate(bounds, 0, 0, zoom, padding);
    }

    public static DJICameraUpdate newLatLngBounds(@NonNull DJILatLngBounds bounds, int paddingtop, int paddingRight, int paddingBottom, int paddingLeft, int zoom) {
        return new CameraBoundsUpdate(bounds, paddingtop, paddingRight, paddingBottom, paddingLeft, zoom);
    }

    public static DJICameraUpdate newLatLngBounds(@NonNull DJILatLngBounds bounds, int zoom) {
        return new CameraBoundsUpdate(bounds, 0, 0, zoom, 0);
    }

    public static final class CameraPositionUpdate implements DJICameraUpdate {
        private final float bearing;
        private final DJILatLng target;
        private final float tilt;
        private final float zoom;

        public CameraPositionUpdate(DJILatLng target2, float zoom2, float tilt2, float bearing2) {
            this.target = target2;
            this.zoom = zoom2;
            this.tilt = tilt2;
            this.bearing = bearing2;
        }

        public DJILatLng getTarget() {
            return this.target;
        }

        public float getZoom() {
            return this.zoom;
        }

        public float getTilt() {
            return this.tilt;
        }

        public float getBearing() {
            return this.bearing;
        }

        public DJICameraPosition getCameraPosition(@NonNull DJIMap map) {
            DJICameraPosition previousPosition = map.getCameraPosition();
            DJICameraPosition.Builder builder = new DJICameraPosition.Builder().zoom(this.zoom).tilt(this.tilt).bearing(this.bearing);
            if (this.target == null) {
                builder.target(previousPosition.target);
            } else {
                builder.target(this.target);
            }
            return builder.build();
        }
    }

    public static final class CameraBoundsUpdate implements DJICameraUpdate {
        private DJILatLngBounds bounds;
        private int height;
        private int padding = -1;
        private int paddingBottom;
        private int paddingLeft;
        private int paddingRight;
        private int paddingTop;
        private int width;
        private int zoom;

        CameraBoundsUpdate(DJILatLngBounds bounds2, int width2, int height2, int zoom2, int padding2) {
            this.bounds = bounds2;
            this.width = width2;
            this.height = height2;
            this.zoom = zoom2;
            this.padding = padding2;
        }

        CameraBoundsUpdate(DJILatLngBounds bounds2, int paddingTop2, int paddingRight2, int paddingBottom2, int paddingLeft2, int zoom2) {
            this.bounds = bounds2;
            this.paddingTop = paddingTop2;
            this.paddingRight = paddingRight2;
            this.paddingBottom = paddingBottom2;
            this.paddingLeft = paddingLeft2;
            this.zoom = zoom2;
        }

        public DJICameraPosition getCameraPosition(@NonNull DJIMap map) {
            return null;
        }

        public DJILatLng getTarget() {
            return null;
        }

        public DJILatLngBounds getBounds() {
            return this.bounds;
        }

        public float getZoom() {
            return (float) this.zoom;
        }

        public float getTilt() {
            return 0.0f;
        }

        public float getBearing() {
            return 0.0f;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public int getPadding() {
            return this.padding;
        }

        public int getPaddingTop() {
            return this.paddingTop;
        }

        public int getPaddingRight() {
            return this.paddingRight;
        }

        public int getPaddingBottom() {
            return this.paddingBottom;
        }

        public int getPaddingLeft() {
            return this.paddingLeft;
        }
    }
}
