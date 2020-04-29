package com.mapbox.mapboxsdk.location;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.MotionEvent;
import com.mapbox.android.gestures.AndroidGesturesManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.RotateGestureDetector;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.MapboxAnimator;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Transform;
import java.util.HashSet;
import java.util.Set;

final class LocationCameraController {
    private boolean adjustFocalPoint;
    /* access modifiers changed from: private */
    public int cameraMode;
    private final MapboxAnimator.AnimationsValueChangeListener<Float> compassBearingValueListener = new MapboxAnimator.AnimationsValueChangeListener<Float>() {
        /* class com.mapbox.mapboxsdk.location.LocationCameraController.AnonymousClass4 */

        public void onNewAnimationValue(Float value) {
            if (LocationCameraController.this.cameraMode == 32 || LocationCameraController.this.cameraMode == 16) {
                LocationCameraController.this.setBearing(value.floatValue());
            }
        }
    };
    private final MapboxAnimator.AnimationsValueChangeListener<Float> gpsBearingValueListener = new MapboxAnimator.AnimationsValueChangeListener<Float>() {
        /* class com.mapbox.mapboxsdk.location.LocationCameraController.AnonymousClass3 */

        public void onNewAnimationValue(Float value) {
            if (!(LocationCameraController.this.cameraMode == 36 && LocationCameraController.this.mapboxMap.getCameraPosition().bearing == 0.0d)) {
                LocationCameraController.this.setBearing(value.floatValue());
            }
        }
    };
    private final AndroidGesturesManager initialGesturesManager;
    private final OnCameraTrackingChangedListener internalCameraTrackingChangedListener;
    private final AndroidGesturesManager internalGesturesManager;
    /* access modifiers changed from: private */
    public boolean isTransitioning;
    private final MapboxAnimator.AnimationsValueChangeListener<LatLng> latLngValueListener = new MapboxAnimator.AnimationsValueChangeListener<LatLng>() {
        /* class com.mapbox.mapboxsdk.location.LocationCameraController.AnonymousClass2 */

        public void onNewAnimationValue(LatLng value) {
            LocationCameraController.this.setLatLng(value);
        }
    };
    /* access modifiers changed from: private */
    public final MapboxMap mapboxMap;
    private final MoveGestureDetector moveGestureDetector;
    private final OnCameraMoveInvalidateListener onCameraMoveInvalidateListener;
    @NonNull
    private MapboxMap.OnFlingListener onFlingListener = new MapboxMap.OnFlingListener() {
        /* class com.mapbox.mapboxsdk.location.LocationCameraController.AnonymousClass9 */

        public void onFling() {
            LocationCameraController.this.setCameraMode(8);
        }
    };
    @VisibleForTesting
    @NonNull
    MapboxMap.OnMoveListener onMoveListener = new MapboxMap.OnMoveListener() {
        /* class com.mapbox.mapboxsdk.location.LocationCameraController.AnonymousClass7 */
        private boolean interrupt;

        public void onMoveBegin(@NonNull MoveGestureDetector detector) {
            if (!LocationCameraController.this.options.trackingGesturesManagement() || detector.getPointersCount() <= 1 || detector.getMoveThreshold() == LocationCameraController.this.options.trackingMultiFingerMoveThreshold() || !LocationCameraController.this.isLocationTracking()) {
                LocationCameraController.this.setCameraMode(8);
                return;
            }
            detector.setMoveThreshold(LocationCameraController.this.options.trackingMultiFingerMoveThreshold());
            this.interrupt = true;
        }

        public void onMove(@NonNull MoveGestureDetector detector) {
            if (this.interrupt) {
                detector.interrupt();
            } else if (LocationCameraController.this.isLocationTracking() || LocationCameraController.this.isBearingTracking()) {
                LocationCameraController.this.setCameraMode(8);
                detector.interrupt();
            }
        }

        public void onMoveEnd(@NonNull MoveGestureDetector detector) {
            if (LocationCameraController.this.options.trackingGesturesManagement() && !this.interrupt && LocationCameraController.this.isLocationTracking()) {
                detector.setMoveThreshold(LocationCameraController.this.options.trackingInitialMoveThreshold());
            }
            this.interrupt = false;
        }
    };
    @NonNull
    private MapboxMap.OnRotateListener onRotateListener = new MapboxMap.OnRotateListener() {
        /* class com.mapbox.mapboxsdk.location.LocationCameraController.AnonymousClass8 */

        public void onRotateBegin(@NonNull RotateGestureDetector detector) {
            if (LocationCameraController.this.isBearingTracking()) {
                LocationCameraController.this.setCameraMode(8);
            }
        }

        public void onRotate(@NonNull RotateGestureDetector detector) {
        }

        public void onRotateEnd(@NonNull RotateGestureDetector detector) {
        }
    };
    /* access modifiers changed from: private */
    public LocationComponentOptions options;
    private final MapboxAnimator.AnimationsValueChangeListener<Float> tiltValueListener = new MapboxAnimator.AnimationsValueChangeListener<Float>() {
        /* class com.mapbox.mapboxsdk.location.LocationCameraController.AnonymousClass6 */

        public void onNewAnimationValue(Float value) {
            LocationCameraController.this.setTilt(value.floatValue());
        }
    };
    private final Transform transform;
    private final MapboxAnimator.AnimationsValueChangeListener<Float> zoomValueListener = new MapboxAnimator.AnimationsValueChangeListener<Float>() {
        /* class com.mapbox.mapboxsdk.location.LocationCameraController.AnonymousClass5 */

        public void onNewAnimationValue(Float value) {
            LocationCameraController.this.setZoom(value.floatValue());
        }
    };

    LocationCameraController(Context context, MapboxMap mapboxMap2, Transform transform2, OnCameraTrackingChangedListener internalCameraTrackingChangedListener2, @NonNull LocationComponentOptions options2, OnCameraMoveInvalidateListener onCameraMoveInvalidateListener2) {
        this.mapboxMap = mapboxMap2;
        this.transform = transform2;
        this.initialGesturesManager = mapboxMap2.getGesturesManager();
        this.internalGesturesManager = new LocationGesturesManager(context);
        this.moveGestureDetector = this.internalGesturesManager.getMoveGestureDetector();
        mapboxMap2.addOnRotateListener(this.onRotateListener);
        mapboxMap2.addOnFlingListener(this.onFlingListener);
        mapboxMap2.addOnMoveListener(this.onMoveListener);
        this.internalCameraTrackingChangedListener = internalCameraTrackingChangedListener2;
        this.onCameraMoveInvalidateListener = onCameraMoveInvalidateListener2;
        initializeOptions(options2);
    }

    LocationCameraController(MapboxMap mapboxMap2, Transform transform2, MoveGestureDetector moveGestureDetector2, OnCameraTrackingChangedListener internalCameraTrackingChangedListener2, OnCameraMoveInvalidateListener onCameraMoveInvalidateListener2, AndroidGesturesManager initialGesturesManager2, AndroidGesturesManager internalGesturesManager2) {
        this.mapboxMap = mapboxMap2;
        this.transform = transform2;
        this.moveGestureDetector = moveGestureDetector2;
        this.internalCameraTrackingChangedListener = internalCameraTrackingChangedListener2;
        this.onCameraMoveInvalidateListener = onCameraMoveInvalidateListener2;
        this.internalGesturesManager = internalGesturesManager2;
        this.initialGesturesManager = initialGesturesManager2;
    }

    /* access modifiers changed from: package-private */
    public void initializeOptions(LocationComponentOptions options2) {
        this.options = options2;
        if (options2.trackingGesturesManagement()) {
            if (this.mapboxMap.getGesturesManager() != this.internalGesturesManager) {
                this.mapboxMap.setGesturesManager(this.internalGesturesManager, true, true);
            }
            adjustGesturesThresholds();
        } else if (this.mapboxMap.getGesturesManager() != this.initialGesturesManager) {
            this.mapboxMap.setGesturesManager(this.initialGesturesManager, true, true);
        }
    }

    /* access modifiers changed from: package-private */
    public void setCameraMode(int cameraMode2) {
        setCameraMode(cameraMode2, null, 750, null, null, null, null);
    }

    /* access modifiers changed from: package-private */
    public void setCameraMode(int cameraMode2, @Nullable Location lastLocation, long transitionDuration, @Nullable Double zoom, @Nullable Double bearing, @Nullable Double tilt, @Nullable OnLocationCameraTransitionListener internalTransitionListener) {
        if (this.cameraMode != cameraMode2) {
            boolean wasTracking = isLocationTracking();
            this.cameraMode = cameraMode2;
            if (cameraMode2 != 8) {
                this.mapboxMap.cancelTransitions();
            }
            adjustGesturesThresholds();
            notifyCameraTrackingChangeListener(wasTracking);
            transitionToCurrentLocation(wasTracking, lastLocation, transitionDuration, zoom, bearing, tilt, internalTransitionListener);
        }
    }

    private void transitionToCurrentLocation(boolean wasTracking, Location lastLocation, long transitionDuration, Double zoom, Double bearing, Double tilt, OnLocationCameraTransitionListener internalTransitionListener) {
        if (!wasTracking && isLocationTracking() && lastLocation != null) {
            this.isTransitioning = true;
            LatLng target = new LatLng(lastLocation);
            CameraPosition.Builder builder = new CameraPosition.Builder().target(target);
            if (zoom != null) {
                builder.zoom(zoom.doubleValue());
            }
            if (tilt != null) {
                builder.tilt(tilt.doubleValue());
            }
            if (bearing != null) {
                builder.bearing(bearing.doubleValue());
            } else if (isLocationBearingTracking()) {
                builder.bearing(this.cameraMode == 36 ? 0.0d : (double) lastLocation.getBearing());
            }
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(builder.build());
            final OnLocationCameraTransitionListener onLocationCameraTransitionListener = internalTransitionListener;
            MapboxMap.CancelableCallback callback = new MapboxMap.CancelableCallback() {
                /* class com.mapbox.mapboxsdk.location.LocationCameraController.AnonymousClass1 */

                public void onCancel() {
                    boolean unused = LocationCameraController.this.isTransitioning = false;
                    if (onLocationCameraTransitionListener != null) {
                        onLocationCameraTransitionListener.onLocationCameraTransitionCanceled(LocationCameraController.this.cameraMode);
                    }
                }

                public void onFinish() {
                    boolean unused = LocationCameraController.this.isTransitioning = false;
                    if (onLocationCameraTransitionListener != null) {
                        onLocationCameraTransitionListener.onLocationCameraTransitionFinished(LocationCameraController.this.cameraMode);
                    }
                }
            };
            if (Utils.immediateAnimation(this.mapboxMap.getProjection(), this.mapboxMap.getCameraPosition().target, target)) {
                this.transform.moveCamera(this.mapboxMap, update, callback);
            } else {
                this.transform.animateCamera(this.mapboxMap, update, (int) transitionDuration, callback);
            }
        } else if (internalTransitionListener != null) {
            internalTransitionListener.onLocationCameraTransitionFinished(this.cameraMode);
        }
    }

    /* access modifiers changed from: package-private */
    public int getCameraMode() {
        return this.cameraMode;
    }

    /* access modifiers changed from: private */
    public void setBearing(float bearing) {
        if (!this.isTransitioning) {
            this.transform.moveCamera(this.mapboxMap, CameraUpdateFactory.bearingTo((double) bearing), null);
            this.onCameraMoveInvalidateListener.onInvalidateCameraMove();
        }
    }

    /* access modifiers changed from: private */
    public void setLatLng(@NonNull LatLng latLng) {
        if (!this.isTransitioning) {
            this.transform.moveCamera(this.mapboxMap, CameraUpdateFactory.newLatLng(latLng), null);
            this.onCameraMoveInvalidateListener.onInvalidateCameraMove();
            if (this.adjustFocalPoint) {
                this.mapboxMap.getUiSettings().setFocalPoint(this.mapboxMap.getProjection().toScreenLocation(latLng));
                this.adjustFocalPoint = false;
            }
        }
    }

    /* access modifiers changed from: private */
    public void setZoom(float zoom) {
        if (!this.isTransitioning) {
            this.transform.moveCamera(this.mapboxMap, CameraUpdateFactory.zoomTo((double) zoom), null);
            this.onCameraMoveInvalidateListener.onInvalidateCameraMove();
        }
    }

    /* access modifiers changed from: private */
    public void setTilt(float tilt) {
        if (!this.isTransitioning) {
            this.transform.moveCamera(this.mapboxMap, CameraUpdateFactory.tiltTo((double) tilt), null);
            this.onCameraMoveInvalidateListener.onInvalidateCameraMove();
        }
    }

    /* access modifiers changed from: package-private */
    public Set<AnimatorListenerHolder> getAnimationListeners() {
        Set<AnimatorListenerHolder> holders = new HashSet<>();
        if (isLocationTracking()) {
            holders.add(new AnimatorListenerHolder(1, this.latLngValueListener));
        }
        if (isLocationBearingTracking()) {
            holders.add(new AnimatorListenerHolder(4, this.gpsBearingValueListener));
        }
        if (isConsumingCompass()) {
            holders.add(new AnimatorListenerHolder(5, this.compassBearingValueListener));
        }
        holders.add(new AnimatorListenerHolder(7, this.zoomValueListener));
        holders.add(new AnimatorListenerHolder(8, this.tiltValueListener));
        return holders;
    }

    /* access modifiers changed from: package-private */
    public boolean isTransitioning() {
        return this.isTransitioning;
    }

    /* access modifiers changed from: private */
    public void adjustGesturesThresholds() {
        if (!this.options.trackingGesturesManagement()) {
            return;
        }
        if (isLocationTracking()) {
            this.adjustFocalPoint = true;
            this.moveGestureDetector.setMoveThreshold(this.options.trackingInitialMoveThreshold());
            return;
        }
        this.moveGestureDetector.setMoveThreshold(0.0f);
    }

    /* access modifiers changed from: package-private */
    public boolean isConsumingCompass() {
        return this.cameraMode == 32 || this.cameraMode == 16;
    }

    /* access modifiers changed from: private */
    public boolean isLocationTracking() {
        return this.cameraMode == 24 || this.cameraMode == 32 || this.cameraMode == 34 || this.cameraMode == 36;
    }

    /* access modifiers changed from: private */
    public boolean isBearingTracking() {
        return this.cameraMode == 16 || this.cameraMode == 32 || this.cameraMode == 22 || this.cameraMode == 34 || this.cameraMode == 36;
    }

    private boolean isLocationBearingTracking() {
        return this.cameraMode == 34 || this.cameraMode == 36 || this.cameraMode == 22;
    }

    private void notifyCameraTrackingChangeListener(boolean wasTracking) {
        this.internalCameraTrackingChangedListener.onCameraTrackingChanged(this.cameraMode);
        if (wasTracking && !isLocationTracking()) {
            this.mapboxMap.getUiSettings().setFocalPoint(null);
            this.internalCameraTrackingChangedListener.onCameraTrackingDismissed();
        }
    }

    private class LocationGesturesManager extends AndroidGesturesManager {
        LocationGesturesManager(Context context) {
            super(context);
        }

        public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
            if (motionEvent != null && motionEvent.getActionMasked() == 1) {
                LocationCameraController.this.adjustGesturesThresholds();
            }
            return super.onTouchEvent(motionEvent);
        }
    }
}
