package com.mapbox.mapboxsdk.location;

import android.animation.Animator;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.SparseArray;
import android.view.animation.LinearInterpolator;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.MapboxAnimator;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class LocationAnimatorCoordinator {
    private static final String TAG = "Mbgl-LocationAnimatorCoordinator";
    private boolean accuracyAnimationEnabled;
    @VisibleForTesting
    final SparseArray<MapboxAnimator> animatorArray = new SparseArray<>();
    private final MapboxAnimatorProvider animatorProvider;
    private final MapboxAnimatorSetProvider animatorSetProvider;
    private boolean compassAnimationEnabled;
    private float durationMultiplier;
    @VisibleForTesting
    final SparseArray<MapboxAnimator.AnimationsValueChangeListener> listeners = new SparseArray<>();
    private long locationUpdateTimestamp = -1;
    @VisibleForTesting
    int maxAnimationFps = Integer.MAX_VALUE;
    private float previousAccuracyRadius = -1.0f;
    private float previousCompassBearing = -1.0f;
    private Location previousLocation;
    private final Projection projection;

    LocationAnimatorCoordinator(@NonNull Projection projection2, @NonNull MapboxAnimatorSetProvider animatorSetProvider2, @NonNull MapboxAnimatorProvider animatorProvider2) {
        this.projection = projection2;
        this.animatorProvider = animatorProvider2;
        this.animatorSetProvider = animatorSetProvider2;
    }

    /* access modifiers changed from: package-private */
    public void updateAnimatorListenerHolders(@NonNull Set<AnimatorListenerHolder> listenerHolders) {
        this.listeners.clear();
        for (AnimatorListenerHolder holder : listenerHolders) {
            this.listeners.append(holder.getAnimatorType(), holder.getListener());
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.min(long, long):long}
     arg types: [long, int]
     candidates:
      ClspMth{java.lang.Math.min(double, double):double}
      ClspMth{java.lang.Math.min(float, float):float}
      ClspMth{java.lang.Math.min(int, int):int}
      ClspMth{java.lang.Math.min(long, long):long} */
    /* access modifiers changed from: package-private */
    public void feedNewLocation(@NonNull Location newLocation, @NonNull CameraPosition currentCameraPosition, boolean isGpsNorth) {
        long animationDuration;
        if (this.previousLocation == null) {
            this.previousLocation = newLocation;
            this.locationUpdateTimestamp = SystemClock.elapsedRealtime() - 750;
        }
        LatLng previousLayerLatLng = getPreviousLayerLatLng();
        float previousLayerBearing = getPreviousLayerGpsBearing();
        LatLng previousCameraLatLng = currentCameraPosition.target;
        LatLng targetLatLng = new LatLng(newLocation);
        float targetLayerBearing = newLocation.getBearing();
        float targetCameraBearing = checkGpsNorth(isGpsNorth, newLocation.getBearing());
        updateLayerAnimators(previousLayerLatLng, targetLatLng, previousLayerBearing, targetLayerBearing);
        updateCameraAnimators(previousCameraLatLng, (float) currentCameraPosition.bearing, targetLatLng, targetCameraBearing);
        long animationDuration2 = 0;
        if (!(Utils.immediateAnimation(this.projection, previousCameraLatLng, targetLatLng) || Utils.immediateAnimation(this.projection, previousLayerLatLng, targetLatLng))) {
            long previousUpdateTimeStamp = this.locationUpdateTimestamp;
            this.locationUpdateTimestamp = SystemClock.elapsedRealtime();
            if (previousUpdateTimeStamp == 0) {
                animationDuration = 0;
            } else {
                animationDuration = (long) (((float) (this.locationUpdateTimestamp - previousUpdateTimeStamp)) * this.durationMultiplier);
            }
            animationDuration2 = Math.min(animationDuration, 2000L);
        }
        playAnimators(animationDuration2, 0, 2, 1, 4);
        this.previousLocation = newLocation;
    }

    /* access modifiers changed from: package-private */
    public void feedNewCompassBearing(float targetCompassBearing, @NonNull CameraPosition currentCameraPosition) {
        if (this.previousCompassBearing < 0.0f) {
            this.previousCompassBearing = targetCompassBearing;
        }
        updateCompassAnimators(targetCompassBearing, getPreviousLayerCompassBearing(), (float) currentCameraPosition.bearing);
        playAnimators(this.compassAnimationEnabled ? 500 : 0, 3, 5);
        this.previousCompassBearing = targetCompassBearing;
    }

    /* access modifiers changed from: package-private */
    public void feedNewAccuracyRadius(float targetAccuracyRadius, boolean noAnimation) {
        if (this.previousAccuracyRadius < 0.0f) {
            this.previousAccuracyRadius = targetAccuracyRadius;
        }
        updateAccuracyAnimators(targetAccuracyRadius, getPreviousAccuracyRadius());
        playAnimators((noAnimation || !this.accuracyAnimationEnabled) ? 0 : 250, 6);
        this.previousAccuracyRadius = targetAccuracyRadius;
    }

    /* access modifiers changed from: package-private */
    public void feedNewZoomLevel(double targetZoomLevel, @NonNull CameraPosition currentCameraPosition, long animationDuration, @Nullable MapboxMap.CancelableCallback callback) {
        updateZoomAnimator((float) targetZoomLevel, (float) currentCameraPosition.zoom, callback);
        playAnimators(animationDuration, 7);
    }

    /* access modifiers changed from: package-private */
    public void feedNewTilt(double targetTilt, @NonNull CameraPosition currentCameraPosition, long animationDuration, @Nullable MapboxMap.CancelableCallback callback) {
        updateTiltAnimator((float) targetTilt, (float) currentCameraPosition.tilt, callback);
        playAnimators(animationDuration, 8);
    }

    private LatLng getPreviousLayerLatLng() {
        MapboxAnimator latLngAnimator = this.animatorArray.get(0);
        if (latLngAnimator != null) {
            return (LatLng) latLngAnimator.getAnimatedValue();
        }
        return new LatLng(this.previousLocation);
    }

    private float getPreviousLayerGpsBearing() {
        MapboxFloatAnimator animator = (MapboxFloatAnimator) this.animatorArray.get(2);
        if (animator != null) {
            return ((Float) animator.getAnimatedValue()).floatValue();
        }
        return this.previousLocation.getBearing();
    }

    private float getPreviousLayerCompassBearing() {
        MapboxFloatAnimator animator = (MapboxFloatAnimator) this.animatorArray.get(3);
        if (animator != null) {
            return ((Float) animator.getAnimatedValue()).floatValue();
        }
        return this.previousCompassBearing;
    }

    private float getPreviousAccuracyRadius() {
        MapboxAnimator animator = this.animatorArray.get(6);
        if (animator != null) {
            return ((Float) animator.getAnimatedValue()).floatValue();
        }
        return this.previousAccuracyRadius;
    }

    private void updateLayerAnimators(LatLng previousLatLng, LatLng targetLatLng, float previousBearing, float targetBearing) {
        createNewLatLngAnimator(0, previousLatLng, targetLatLng);
        float previousBearing2 = Utils.normalize(previousBearing);
        createNewFloatAnimator(2, previousBearing2, Utils.shortestRotation(targetBearing, previousBearing2));
    }

    private void updateCameraAnimators(LatLng previousCameraLatLng, float previousCameraBearing, LatLng targetLatLng, float targetBearing) {
        createNewLatLngAnimator(1, previousCameraLatLng, targetLatLng);
        createNewFloatAnimator(4, previousCameraBearing, Utils.shortestRotation(targetBearing, previousCameraBearing));
    }

    private void updateCompassAnimators(float targetCompassBearing, float previousLayerBearing, float previousCameraBearing) {
        createNewFloatAnimator(3, previousLayerBearing, Utils.shortestRotation(targetCompassBearing, previousLayerBearing));
        createNewFloatAnimator(5, previousCameraBearing, Utils.shortestRotation(targetCompassBearing, previousCameraBearing));
    }

    private void updateAccuracyAnimators(float targetAccuracyRadius, float previousAccuracyRadius2) {
        createNewFloatAnimator(6, previousAccuracyRadius2, targetAccuracyRadius);
    }

    private void updateZoomAnimator(float targetZoomLevel, float previousZoomLevel, @Nullable MapboxMap.CancelableCallback cancelableCallback) {
        createNewCameraAdapterAnimator(7, previousZoomLevel, targetZoomLevel, cancelableCallback);
    }

    private void updateTiltAnimator(float targetTilt, float previousTiltLevel, @Nullable MapboxMap.CancelableCallback cancelableCallback) {
        createNewCameraAdapterAnimator(8, previousTiltLevel, targetTilt, cancelableCallback);
    }

    private void createNewLatLngAnimator(int animatorType, LatLng previous, LatLng target) {
        cancelAnimator(animatorType);
        MapboxAnimator.AnimationsValueChangeListener listener = this.listeners.get(animatorType);
        if (listener != null) {
            this.animatorArray.put(animatorType, this.animatorProvider.latLngAnimator(previous, target, listener, this.maxAnimationFps));
        }
    }

    private void createNewFloatAnimator(int animatorType, float previous, float target) {
        cancelAnimator(animatorType);
        MapboxAnimator.AnimationsValueChangeListener listener = this.listeners.get(animatorType);
        if (listener != null) {
            this.animatorArray.put(animatorType, this.animatorProvider.floatAnimator(Float.valueOf(previous), Float.valueOf(target), listener, this.maxAnimationFps));
        }
    }

    private void createNewCameraAdapterAnimator(int animatorType, float previous, float target, @Nullable MapboxMap.CancelableCallback cancelableCallback) {
        cancelAnimator(animatorType);
        MapboxAnimator.AnimationsValueChangeListener listener = this.listeners.get(animatorType);
        if (listener != null) {
            this.animatorArray.put(animatorType, this.animatorProvider.cameraAnimator(Float.valueOf(previous), Float.valueOf(target), listener, cancelableCallback));
        }
    }

    private float checkGpsNorth(boolean isGpsNorth, float targetCameraBearing) {
        if (isGpsNorth) {
            return 0.0f;
        }
        return targetCameraBearing;
    }

    private void playAnimators(long duration, int... animatorTypes) {
        List<Animator> animators = new ArrayList<>();
        for (int animatorType : animatorTypes) {
            Animator animator = this.animatorArray.get(animatorType);
            if (animator != null) {
                animators.add(animator);
            }
        }
        this.animatorSetProvider.startAnimation(animators, new LinearInterpolator(), duration);
    }

    /* access modifiers changed from: package-private */
    public void resetAllCameraAnimations(@NonNull CameraPosition currentCameraPosition, boolean isGpsNorth) {
        resetCameraCompassAnimation(currentCameraPosition);
        playAnimators(resetCameraLocationAnimations(currentCameraPosition, isGpsNorth) ? 0 : 750, 1, 4);
    }

    private boolean resetCameraLocationAnimations(@NonNull CameraPosition currentCameraPosition, boolean isGpsNorth) {
        resetCameraGpsBearingAnimation(currentCameraPosition, isGpsNorth);
        return resetCameraLatLngAnimation(currentCameraPosition);
    }

    private boolean resetCameraLatLngAnimation(@NonNull CameraPosition currentCameraPosition) {
        MapboxLatLngAnimator animator = (MapboxLatLngAnimator) this.animatorArray.get(1);
        if (animator == null) {
            return false;
        }
        LatLng currentTarget = (LatLng) animator.getTarget();
        LatLng previousCameraTarget = currentCameraPosition.target;
        createNewLatLngAnimator(1, previousCameraTarget, currentTarget);
        return Utils.immediateAnimation(this.projection, previousCameraTarget, currentTarget);
    }

    private void resetCameraGpsBearingAnimation(@NonNull CameraPosition currentCameraPosition, boolean isGpsNorth) {
        MapboxFloatAnimator animator = (MapboxFloatAnimator) this.animatorArray.get(4);
        if (animator != null) {
            float currentTargetBearing = checkGpsNorth(isGpsNorth, ((Float) animator.getTarget()).floatValue());
            float previousCameraBearing = (float) currentCameraPosition.bearing;
            createNewFloatAnimator(4, previousCameraBearing, Utils.shortestRotation(currentTargetBearing, previousCameraBearing));
        }
    }

    private void resetCameraCompassAnimation(@NonNull CameraPosition currentCameraPosition) {
        MapboxFloatAnimator animator = (MapboxFloatAnimator) this.animatorArray.get(5);
        if (animator != null) {
            float currentTargetBearing = ((Float) animator.getTarget()).floatValue();
            float previousCameraBearing = (float) currentCameraPosition.bearing;
            createNewFloatAnimator(5, previousCameraBearing, Utils.shortestRotation(currentTargetBearing, previousCameraBearing));
        }
    }

    /* access modifiers changed from: package-private */
    public void resetAllLayerAnimations() {
        MapboxLatLngAnimator latLngAnimator = (MapboxLatLngAnimator) this.animatorArray.get(0);
        MapboxFloatAnimator gpsBearingAnimator = (MapboxFloatAnimator) this.animatorArray.get(2);
        MapboxFloatAnimator compassBearingAnimator = (MapboxFloatAnimator) this.animatorArray.get(3);
        if (!(latLngAnimator == null || gpsBearingAnimator == null)) {
            createNewLatLngAnimator(0, (LatLng) latLngAnimator.getAnimatedValue(), (LatLng) latLngAnimator.getTarget());
            createNewFloatAnimator(2, ((Float) gpsBearingAnimator.getAnimatedValue()).floatValue(), ((Float) gpsBearingAnimator.getTarget()).floatValue());
            playAnimators(latLngAnimator.getDuration() - latLngAnimator.getCurrentPlayTime(), 0, 2);
        }
        if (compassBearingAnimator != null) {
            createNewFloatAnimator(3, getPreviousLayerCompassBearing(), ((Float) compassBearingAnimator.getTarget()).floatValue());
            playAnimators(this.compassAnimationEnabled ? 500 : 0, 3);
        }
    }

    /* access modifiers changed from: package-private */
    public void cancelZoomAnimation() {
        cancelAnimator(7);
    }

    /* access modifiers changed from: package-private */
    public void cancelTiltAnimation() {
        cancelAnimator(8);
    }

    /* access modifiers changed from: package-private */
    public void cancelAllAnimations() {
        for (int i = 0; i < this.animatorArray.size(); i++) {
            cancelAnimator(this.animatorArray.keyAt(i));
        }
    }

    private void cancelAnimator(int animatorType) {
        MapboxAnimator animator = this.animatorArray.get(animatorType);
        if (animator != null) {
            animator.cancel();
            animator.removeAllUpdateListeners();
            animator.removeAllListeners();
            this.animatorArray.put(animatorType, null);
        }
    }

    /* access modifiers changed from: package-private */
    public void setTrackingAnimationDurationMultiplier(float trackingAnimationDurationMultiplier) {
        this.durationMultiplier = trackingAnimationDurationMultiplier;
    }

    /* access modifiers changed from: package-private */
    public void setCompassAnimationEnabled(boolean compassAnimationEnabled2) {
        this.compassAnimationEnabled = compassAnimationEnabled2;
    }

    /* access modifiers changed from: package-private */
    public void setAccuracyAnimationEnabled(boolean accuracyAnimationEnabled2) {
        this.accuracyAnimationEnabled = accuracyAnimationEnabled2;
    }

    /* access modifiers changed from: package-private */
    public void setMaxAnimationFps(int maxAnimationFps2) {
        if (maxAnimationFps2 <= 0) {
            Logger.e(TAG, "Max animation FPS cannot be less or equal to 0.");
        } else {
            this.maxAnimationFps = maxAnimationFps2;
        }
    }
}
