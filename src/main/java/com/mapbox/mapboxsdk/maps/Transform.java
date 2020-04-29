package com.mapbox.mapboxsdk.maps;

import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public final class Transform implements MapView.OnCameraDidChangeListener {
    private static final String TAG = "Mbgl-Transform";
    @Nullable
    private MapboxMap.CancelableCallback cameraCancelableCallback;
    /* access modifiers changed from: private */
    public CameraChangeDispatcher cameraChangeDispatcher;
    @Nullable
    private CameraPosition cameraPosition;
    private final Handler handler = new Handler();
    /* access modifiers changed from: private */
    public final MapView mapView;
    private final MapView.OnCameraDidChangeListener moveByChangeListener = new MapView.OnCameraDidChangeListener() {
        /* class com.mapbox.mapboxsdk.maps.Transform.AnonymousClass1 */

        public void onCameraDidChange(boolean animated) {
            if (animated) {
                Transform.this.cameraChangeDispatcher.onCameraIdle();
                Transform.this.mapView.removeOnCameraDidChangeListener(this);
            }
        }
    };
    private final NativeMap nativeMap;

    Transform(MapView mapView2, NativeMap nativeMap2, CameraChangeDispatcher cameraChangeDispatcher2) {
        this.mapView = mapView2;
        this.nativeMap = nativeMap2;
        this.cameraChangeDispatcher = cameraChangeDispatcher2;
    }

    /* access modifiers changed from: package-private */
    public void initialise(@NonNull MapboxMap mapboxMap, @NonNull MapboxMapOptions options) {
        CameraPosition position = options.getCamera();
        if (position != null && !position.equals(CameraPosition.DEFAULT)) {
            moveCamera(mapboxMap, CameraUpdateFactory.newCameraPosition(position), null);
        }
        setMinZoom(options.getMinZoomPreference());
        setMaxZoom(options.getMaxZoomPreference());
    }

    @Nullable
    @UiThread
    public final CameraPosition getCameraPosition() {
        if (this.cameraPosition == null) {
            this.cameraPosition = invalidateCameraPosition();
        }
        return this.cameraPosition;
    }

    public void onCameraDidChange(boolean animated) {
        if (animated) {
            invalidateCameraPosition();
            if (this.cameraCancelableCallback != null) {
                final MapboxMap.CancelableCallback callback = this.cameraCancelableCallback;
                this.cameraCancelableCallback = null;
                this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.maps.Transform.AnonymousClass2 */

                    public void run() {
                        callback.onFinish();
                    }
                });
            }
            this.cameraChangeDispatcher.onCameraIdle();
            this.mapView.removeOnCameraDidChangeListener(this);
        }
    }

    @UiThread
    public final void moveCamera(@NonNull MapboxMap mapboxMap, CameraUpdate update, @Nullable final MapboxMap.CancelableCallback callback) {
        CameraPosition cameraPosition2 = update.getCameraPosition(mapboxMap);
        if (isValidCameraPosition(cameraPosition2)) {
            cancelTransitions();
            this.cameraChangeDispatcher.onCameraMoveStarted(3);
            this.nativeMap.jumpTo(cameraPosition2.target, cameraPosition2.zoom, cameraPosition2.tilt, cameraPosition2.bearing, cameraPosition2.padding);
            this.cameraChangeDispatcher.onCameraIdle();
            invalidateCameraPosition();
            this.handler.post(new Runnable() {
                /* class com.mapbox.mapboxsdk.maps.Transform.AnonymousClass3 */

                public void run() {
                    if (callback != null) {
                        callback.onFinish();
                    }
                }
            });
        } else if (callback != null) {
            callback.onFinish();
        }
    }

    /* access modifiers changed from: package-private */
    @UiThread
    public final void easeCamera(@NonNull MapboxMap mapboxMap, CameraUpdate update, int durationMs, boolean easingInterpolator, @Nullable MapboxMap.CancelableCallback callback) {
        CameraPosition cameraPosition2 = update.getCameraPosition(mapboxMap);
        if (isValidCameraPosition(cameraPosition2)) {
            cancelTransitions();
            this.cameraChangeDispatcher.onCameraMoveStarted(3);
            if (callback != null) {
                this.cameraCancelableCallback = callback;
            }
            this.mapView.addOnCameraDidChangeListener(this);
            this.nativeMap.easeTo(cameraPosition2.target, cameraPosition2.zoom, cameraPosition2.bearing, cameraPosition2.tilt, cameraPosition2.padding, (long) durationMs, easingInterpolator);
        } else if (callback != null) {
            callback.onFinish();
        }
    }

    @UiThread
    public final void animateCamera(@NonNull MapboxMap mapboxMap, CameraUpdate update, int durationMs, @Nullable MapboxMap.CancelableCallback callback) {
        CameraPosition cameraPosition2 = update.getCameraPosition(mapboxMap);
        if (isValidCameraPosition(cameraPosition2)) {
            cancelTransitions();
            this.cameraChangeDispatcher.onCameraMoveStarted(3);
            if (callback != null) {
                this.cameraCancelableCallback = callback;
            }
            this.mapView.addOnCameraDidChangeListener(this);
            this.nativeMap.flyTo(cameraPosition2.target, cameraPosition2.zoom, cameraPosition2.bearing, cameraPosition2.tilt, cameraPosition2.padding, (long) durationMs);
        } else if (callback != null) {
            callback.onFinish();
        }
    }

    private boolean isValidCameraPosition(@Nullable CameraPosition cameraPosition2) {
        return cameraPosition2 != null && !cameraPosition2.equals(this.cameraPosition);
    }

    /* access modifiers changed from: package-private */
    @Nullable
    @UiThread
    public CameraPosition invalidateCameraPosition() {
        if (this.nativeMap != null) {
            CameraPosition cameraPosition2 = this.nativeMap.getCameraPosition();
            if (this.cameraPosition != null && !this.cameraPosition.equals(cameraPosition2)) {
                this.cameraChangeDispatcher.onCameraMove();
            }
            this.cameraPosition = cameraPosition2;
        }
        return this.cameraPosition;
    }

    /* access modifiers changed from: package-private */
    public void cancelTransitions() {
        this.cameraChangeDispatcher.onCameraMoveCanceled();
        if (this.cameraCancelableCallback != null) {
            final MapboxMap.CancelableCallback callback = this.cameraCancelableCallback;
            this.cameraChangeDispatcher.onCameraIdle();
            this.cameraCancelableCallback = null;
            this.handler.post(new Runnable() {
                /* class com.mapbox.mapboxsdk.maps.Transform.AnonymousClass4 */

                public void run() {
                    callback.onCancel();
                }
            });
        }
        this.nativeMap.cancelTransitions();
        this.cameraChangeDispatcher.onCameraIdle();
    }

    /* access modifiers changed from: package-private */
    @UiThread
    public void resetNorth() {
        cancelTransitions();
        this.nativeMap.resetNorth();
    }

    /* access modifiers changed from: package-private */
    public double getRawZoom() {
        return this.nativeMap.getZoom();
    }

    /* access modifiers changed from: package-private */
    public void zoomBy(double zoomAddition, @NonNull PointF focalPoint) {
        setZoom(this.nativeMap.getZoom() + zoomAddition, focalPoint);
    }

    /* access modifiers changed from: package-private */
    public void setZoom(double zoom, @NonNull PointF focalPoint) {
        this.nativeMap.setZoom(zoom, focalPoint, 0);
    }

    /* access modifiers changed from: package-private */
    public double getBearing() {
        double direction = -this.nativeMap.getBearing();
        while (direction > 360.0d) {
            direction -= 360.0d;
        }
        while (direction < 0.0d) {
            direction += 360.0d;
        }
        return direction;
    }

    /* access modifiers changed from: package-private */
    public double getRawBearing() {
        return this.nativeMap.getBearing();
    }

    /* access modifiers changed from: package-private */
    public void setBearing(double bearing) {
        this.nativeMap.setBearing(bearing, 0);
    }

    /* access modifiers changed from: package-private */
    public void setBearing(double bearing, float focalX, float focalY) {
        this.nativeMap.setBearing(bearing, (double) focalX, (double) focalY, 0);
    }

    /* access modifiers changed from: package-private */
    public void setBearing(double bearing, float focalX, float focalY, long duration) {
        this.nativeMap.setBearing(bearing, (double) focalX, (double) focalY, duration);
    }

    /* access modifiers changed from: package-private */
    public LatLng getLatLng() {
        return this.nativeMap.getLatLng();
    }

    /* access modifiers changed from: package-private */
    public double getTilt() {
        return this.nativeMap.getPitch();
    }

    /* access modifiers changed from: package-private */
    public void setTilt(Double pitch) {
        this.nativeMap.setPitch(pitch.doubleValue(), 0);
    }

    /* access modifiers changed from: package-private */
    public LatLng getCenterCoordinate() {
        return this.nativeMap.getLatLng();
    }

    /* access modifiers changed from: package-private */
    public void setCenterCoordinate(LatLng centerCoordinate) {
        this.nativeMap.setLatLng(centerCoordinate, 0);
    }

    /* access modifiers changed from: package-private */
    public void setGestureInProgress(boolean gestureInProgress) {
        this.nativeMap.setGestureInProgress(gestureInProgress);
        if (!gestureInProgress) {
            invalidateCameraPosition();
        }
    }

    /* access modifiers changed from: package-private */
    public void moveBy(double offsetX, double offsetY, long duration) {
        if (duration > 0) {
            this.mapView.addOnCameraDidChangeListener(this.moveByChangeListener);
        }
        this.nativeMap.moveBy(offsetX, offsetY, duration);
    }

    /* access modifiers changed from: package-private */
    public void setMinZoom(double minZoom) {
        if (minZoom < 0.0d || minZoom > 25.5d) {
            Logger.e(TAG, String.format("Not setting minZoomPreference, value is in unsupported range: %s", Double.valueOf(minZoom)));
            return;
        }
        this.nativeMap.setMinZoom(minZoom);
    }

    /* access modifiers changed from: package-private */
    public double getMinZoom() {
        return this.nativeMap.getMinZoom();
    }

    /* access modifiers changed from: package-private */
    public void setMaxZoom(double maxZoom) {
        if (maxZoom < 0.0d || maxZoom > 25.5d) {
            Logger.e(TAG, String.format("Not setting maxZoomPreference, value is in unsupported range: %s", Double.valueOf(maxZoom)));
            return;
        }
        this.nativeMap.setMaxZoom(maxZoom);
    }

    /* access modifiers changed from: package-private */
    public double getMaxZoom() {
        return this.nativeMap.getMaxZoom();
    }
}
