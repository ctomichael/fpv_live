package com.mapbox.mapboxsdk.maps;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import com.mapbox.android.gestures.AndroidGesturesManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.MultiFingerTapGestureDetector;
import com.mapbox.android.gestures.R;
import com.mapbox.android.gestures.RotateGestureDetector;
import com.mapbox.android.gestures.ShoveGestureDetector;
import com.mapbox.android.gestures.StandardGestureDetector;
import com.mapbox.android.gestures.StandardScaleGestureDetector;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.utils.MathUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

final class MapGestureDetector {
    @NonNull
    private Handler animationsTimeoutHandler = new Handler();
    /* access modifiers changed from: private */
    public final AnnotationManager annotationManager;
    /* access modifiers changed from: private */
    public final CameraChangeDispatcher cameraChangeDispatcher;
    @NonNull
    private final Runnable cancelAnimatorsRunnable = new Runnable() {
        /* class com.mapbox.mapboxsdk.maps.MapGestureDetector.AnonymousClass1 */

        public void run() {
            MapGestureDetector.this.cancelAnimators();
        }
    };
    /* access modifiers changed from: private */
    @Nullable
    public PointF constantFocalPoint;
    /* access modifiers changed from: private */
    @NonNull
    public PointF doubleTapFocalPoint = new PointF();
    private boolean doubleTapRegistered;
    /* access modifiers changed from: private */
    public AndroidGesturesManager gesturesManager;
    private final CopyOnWriteArrayList<MapboxMap.OnFlingListener> onFlingListenerList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MapboxMap.OnMapClickListener> onMapClickListenerList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MapboxMap.OnMapLongClickListener> onMapLongClickListenerList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MapboxMap.OnMoveListener> onMoveListenerList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MapboxMap.OnRotateListener> onRotateListenerList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MapboxMap.OnScaleListener> onScaleListenerList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MapboxMap.OnShoveListener> onShoveListenerList = new CopyOnWriteArrayList<>();
    private final Projection projection;
    /* access modifiers changed from: private */
    public Animator rotateAnimator;
    /* access modifiers changed from: private */
    public Animator scaleAnimator;
    private final List<Animator> scheduledAnimators = new ArrayList();
    /* access modifiers changed from: private */
    public final Transform transform;
    /* access modifiers changed from: private */
    public final UiSettings uiSettings;

    MapGestureDetector(@Nullable Context context, Transform transform2, Projection projection2, UiSettings uiSettings2, AnnotationManager annotationManager2, CameraChangeDispatcher cameraChangeDispatcher2) {
        this.annotationManager = annotationManager2;
        this.transform = transform2;
        this.projection = projection2;
        this.uiSettings = uiSettings2;
        this.cameraChangeDispatcher = cameraChangeDispatcher2;
        if (context != null) {
            initializeGesturesManager(new AndroidGesturesManager(context), true);
            initializeGestureListeners(context, true);
        }
    }

    private void initializeGestureListeners(@NonNull Context context, boolean attachDefaultListeners) {
        if (attachDefaultListeners) {
            StandardGestureListener standardGestureListener = new StandardGestureListener(context.getResources().getDimension(R.dimen.mapbox_defaultScaleSpanSinceStartThreshold));
            MoveGestureListener moveGestureListener = new MoveGestureListener();
            ScaleGestureListener scaleGestureListener = new ScaleGestureListener((double) context.getResources().getDimension(com.mapbox.mapboxsdk.R.dimen.mapbox_density_constant), context.getResources().getDimension(com.mapbox.mapboxsdk.R.dimen.mapbox_minimum_scale_speed), context.getResources().getDimension(com.mapbox.mapboxsdk.R.dimen.mapbox_minimum_angled_scale_speed), context.getResources().getDimension(com.mapbox.mapboxsdk.R.dimen.mapbox_minimum_scale_velocity));
            RotateGestureListener rotateGestureListener = new RotateGestureListener(context.getResources().getDimension(com.mapbox.mapboxsdk.R.dimen.mapbox_minimum_scale_span_when_rotating), (double) context.getResources().getDimension(com.mapbox.mapboxsdk.R.dimen.mapbox_density_constant), context.getResources().getDimension(com.mapbox.mapboxsdk.R.dimen.mapbox_angular_velocity_multiplier), context.getResources().getDimension(com.mapbox.mapboxsdk.R.dimen.mapbox_minimum_angular_velocity), context.getResources().getDimension(R.dimen.mapbox_defaultScaleSpanSinceStartThreshold));
            ShoveGestureListener shoveGestureListener = new ShoveGestureListener();
            TapGestureListener tapGestureListener = new TapGestureListener();
            this.gesturesManager.setStandardGestureListener(standardGestureListener);
            this.gesturesManager.setMoveGestureListener(moveGestureListener);
            this.gesturesManager.setStandardScaleGestureListener(scaleGestureListener);
            this.gesturesManager.setRotateGestureListener(rotateGestureListener);
            this.gesturesManager.setShoveGestureListener(shoveGestureListener);
            this.gesturesManager.setMultiFingerTapGestureListener(tapGestureListener);
        }
    }

    private void initializeGesturesManager(@NonNull AndroidGesturesManager androidGesturesManager, boolean setDefaultMutuallyExclusives) {
        if (setDefaultMutuallyExclusives) {
            Set<Integer> shoveScaleSet = new HashSet<>();
            shoveScaleSet.add(3);
            shoveScaleSet.add(1);
            Set<Integer> shoveRotateSet = new HashSet<>();
            shoveRotateSet.add(3);
            shoveRotateSet.add(2);
            Set<Integer> ScaleLongPressSet = new HashSet<>();
            ScaleLongPressSet.add(1);
            ScaleLongPressSet.add(6);
            androidGesturesManager.setMutuallyExclusiveGestures(shoveScaleSet, shoveRotateSet, ScaleLongPressSet);
        }
        this.gesturesManager = androidGesturesManager;
        this.gesturesManager.getRotateGestureDetector().setAngleThreshold(3.0f);
    }

    /* access modifiers changed from: package-private */
    public void setFocalPoint(@Nullable PointF focalPoint) {
        if (focalPoint == null && this.uiSettings.getFocalPoint() != null) {
            focalPoint = this.uiSettings.getFocalPoint();
        }
        this.constantFocalPoint = focalPoint;
    }

    /* access modifiers changed from: package-private */
    public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        if (motionEvent.getButtonState() != 0 && motionEvent.getButtonState() != 1) {
            return false;
        }
        if (motionEvent.getActionMasked() == 0) {
            cancelAnimators();
            this.transform.setGestureInProgress(true);
        }
        boolean onTouchEvent = this.gesturesManager.onTouchEvent(motionEvent);
        switch (motionEvent.getActionMasked()) {
            case 1:
                doubleTapFinished();
                this.transform.setGestureInProgress(false);
                if (this.scheduledAnimators.isEmpty()) {
                    return onTouchEvent;
                }
                this.animationsTimeoutHandler.removeCallbacksAndMessages(null);
                for (Animator animator : this.scheduledAnimators) {
                    animator.start();
                }
                this.scheduledAnimators.clear();
                return onTouchEvent;
            case 2:
            case 4:
            default:
                return onTouchEvent;
            case 3:
                this.scheduledAnimators.clear();
                this.transform.setGestureInProgress(false);
                doubleTapFinished();
                return onTouchEvent;
            case 5:
                doubleTapFinished();
                return onTouchEvent;
        }
    }

    /* access modifiers changed from: package-private */
    public void cancelAnimators() {
        this.animationsTimeoutHandler.removeCallbacksAndMessages(null);
        this.scheduledAnimators.clear();
        cancelAnimator(this.scaleAnimator);
        cancelAnimator(this.rotateAnimator);
        dispatchCameraIdle();
    }

    private void cancelAnimator(@Nullable Animator animator) {
        if (animator != null && animator.isStarted()) {
            animator.cancel();
        }
    }

    /* access modifiers changed from: private */
    public void scheduleAnimator(Animator animator) {
        this.scheduledAnimators.add(animator);
        this.animationsTimeoutHandler.removeCallbacksAndMessages(null);
        this.animationsTimeoutHandler.postDelayed(this.cancelAnimatorsRunnable, 150);
    }

    /* access modifiers changed from: package-private */
    public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & 2) != 2) {
            return false;
        }
        switch (event.getActionMasked()) {
            case 8:
                if (!this.uiSettings.isZoomGesturesEnabled()) {
                    return false;
                }
                this.transform.cancelTransitions();
                this.transform.zoomBy((double) event.getAxisValue(9), new PointF(event.getX(), event.getY()));
                return true;
            default:
                return false;
        }
    }

    private final class StandardGestureListener extends StandardGestureDetector.SimpleStandardOnGestureListener {
        private final float doubleTapMovementThreshold;

        StandardGestureListener(float doubleTapMovementThreshold2) {
            this.doubleTapMovementThreshold = doubleTapMovementThreshold2;
        }

        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            MapGestureDetector.this.transform.cancelTransitions();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            PointF tapPoint = new PointF(motionEvent.getX(), motionEvent.getY());
            if (MapGestureDetector.this.annotationManager.onTap(tapPoint)) {
                return true;
            }
            if (MapGestureDetector.this.uiSettings.isDeselectMarkersOnTap()) {
                MapGestureDetector.this.annotationManager.deselectMarkers();
            }
            MapGestureDetector.this.notifyOnMapClickListeners(tapPoint);
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() == 0) {
                PointF unused = MapGestureDetector.this.doubleTapFocalPoint = new PointF(motionEvent.getX(), motionEvent.getY());
                MapGestureDetector.this.doubleTapStarted();
            }
            if (motionEvent.getActionMasked() != 1) {
                return super.onDoubleTapEvent(motionEvent);
            }
            float diffX = Math.abs(motionEvent.getX() - MapGestureDetector.this.doubleTapFocalPoint.x);
            float diffY = Math.abs(motionEvent.getY() - MapGestureDetector.this.doubleTapFocalPoint.y);
            if (diffX > this.doubleTapMovementThreshold || diffY > this.doubleTapMovementThreshold) {
                return false;
            }
            if (!MapGestureDetector.this.uiSettings.isZoomGesturesEnabled() || !MapGestureDetector.this.uiSettings.isDoubleTapGesturesEnabled()) {
                return false;
            }
            if (MapGestureDetector.this.constantFocalPoint != null) {
                PointF unused2 = MapGestureDetector.this.doubleTapFocalPoint = MapGestureDetector.this.constantFocalPoint;
            }
            MapGestureDetector.this.zoomInAnimated(MapGestureDetector.this.doubleTapFocalPoint, false);
            return true;
        }

        public void onLongPress(MotionEvent motionEvent) {
            MapGestureDetector.this.notifyOnMapLongClickListeners(new PointF(motionEvent.getX(), motionEvent.getY()));
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!MapGestureDetector.this.uiSettings.isScrollGesturesEnabled()) {
                return false;
            }
            MapGestureDetector.this.notifyOnFlingListeners();
            if (!MapGestureDetector.this.uiSettings.isFlingVelocityAnimationEnabled()) {
                return false;
            }
            float screenDensity = MapGestureDetector.this.uiSettings.getPixelRatio();
            double velocityXY = Math.hypot((double) (velocityX / screenDensity), (double) (velocityY / screenDensity));
            if (velocityXY < 1000.0d) {
                return false;
            }
            MapGestureDetector.this.transform.cancelTransitions();
            MapGestureDetector.this.cameraChangeDispatcher.onCameraMoveStarted(1);
            double tilt = MapGestureDetector.this.transform.getTilt();
            double tiltFactor = 1.5d + (tilt != 0.0d ? tilt / 10.0d : 0.0d);
            MapGestureDetector.this.transform.moveBy((((double) velocityX) / tiltFactor) / ((double) screenDensity), (((double) velocityY) / tiltFactor) / ((double) screenDensity), (long) (((velocityXY / 7.0d) / tiltFactor) + 150.0d));
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void doubleTapStarted() {
        this.gesturesManager.getMoveGestureDetector().setEnabled(false);
        this.doubleTapRegistered = true;
    }

    private void doubleTapFinished() {
        if (this.doubleTapRegistered) {
            this.gesturesManager.getMoveGestureDetector().setEnabled(true);
            this.doubleTapRegistered = false;
        }
    }

    private final class MoveGestureListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        private MoveGestureListener() {
        }

        public boolean onMoveBegin(@NonNull MoveGestureDetector detector) {
            if (!MapGestureDetector.this.uiSettings.isScrollGesturesEnabled()) {
                return false;
            }
            MapGestureDetector.this.cancelTransitionsIfRequired();
            MapGestureDetector.this.notifyOnMoveBeginListeners(detector);
            return true;
        }

        public boolean onMove(@NonNull MoveGestureDetector detector, float distanceX, float distanceY) {
            if (!(distanceX == 0.0f && distanceY == 0.0f)) {
                MapGestureDetector.this.cameraChangeDispatcher.onCameraMoveStarted(1);
                MapGestureDetector.this.transform.moveBy((double) (-distanceX), (double) (-distanceY), 0);
                MapGestureDetector.this.notifyOnMoveListeners(detector);
            }
            return true;
        }

        public void onMoveEnd(@NonNull MoveGestureDetector detector, float velocityX, float velocityY) {
            MapGestureDetector.this.dispatchCameraIdle();
            MapGestureDetector.this.notifyOnMoveEndListeners(detector);
        }
    }

    private final class ScaleGestureListener extends StandardScaleGestureDetector.SimpleStandardOnScaleGestureListener {
        private final float minimumAngledGestureSpeed;
        private final float minimumGestureSpeed;
        private final float minimumVelocity;
        private boolean quickZoom;
        private final double scaleVelocityRatioThreshold;
        private double screenHeight;
        private float spanSinceLast;
        private double startZoom;

        ScaleGestureListener(double densityMultiplier, float minimumGestureSpeed2, float minimumAngledGestureSpeed2, float minimumVelocity2) {
            this.minimumGestureSpeed = minimumGestureSpeed2;
            this.minimumAngledGestureSpeed = minimumAngledGestureSpeed2;
            this.minimumVelocity = minimumVelocity2;
            this.scaleVelocityRatioThreshold = 0.004d * densityMultiplier;
        }

        public boolean onScaleBegin(@NonNull StandardScaleGestureDetector detector) {
            this.quickZoom = detector.getPointersCount() == 1;
            if (!MapGestureDetector.this.uiSettings.isZoomGesturesEnabled()) {
                return false;
            }
            if (this.quickZoom) {
                if (!MapGestureDetector.this.uiSettings.isQuickZoomGesturesEnabled()) {
                    return false;
                }
                MapGestureDetector.this.gesturesManager.getMoveGestureDetector().setEnabled(false);
            } else if (detector.getPreviousSpan() <= 0.0f) {
                return false;
            } else {
                float currSpan = detector.getCurrentSpan();
                float prevSpan = detector.getPreviousSpan();
                double currTime = (double) detector.getCurrentEvent().getEventTime();
                double prevTime = (double) detector.getPreviousEvent().getEventTime();
                if (currTime == prevTime) {
                    return false;
                }
                double speed = ((double) Math.abs(currSpan - prevSpan)) / (currTime - prevTime);
                if (speed < ((double) this.minimumGestureSpeed)) {
                    return false;
                }
                if (!MapGestureDetector.this.gesturesManager.getRotateGestureDetector().isInProgress()) {
                    if (((double) Math.abs(MapGestureDetector.this.gesturesManager.getRotateGestureDetector().getDeltaSinceLast())) > 0.4d && speed < ((double) this.minimumAngledGestureSpeed)) {
                        return false;
                    }
                    if (MapGestureDetector.this.uiSettings.isDisableRotateWhenScaling()) {
                        MapGestureDetector.this.gesturesManager.getRotateGestureDetector().setEnabled(false);
                    }
                }
            }
            this.screenHeight = (double) Resources.getSystem().getDisplayMetrics().heightPixels;
            this.startZoom = MapGestureDetector.this.transform.getRawZoom();
            MapGestureDetector.this.cancelTransitionsIfRequired();
            MapGestureDetector.this.notifyOnScaleBeginListeners(detector);
            this.spanSinceLast = Math.abs(detector.getCurrentSpan() - detector.getPreviousSpan());
            return true;
        }

        public boolean onScale(@NonNull StandardScaleGestureDetector detector) {
            MapGestureDetector.this.cameraChangeDispatcher.onCameraMoveStarted(1);
            PointF focalPoint = getScaleFocalPoint(detector);
            if (this.quickZoom) {
                double pixelDeltaChange = (double) Math.abs(detector.getCurrentEvent().getY() - MapGestureDetector.this.doubleTapFocalPoint.y);
                boolean zoomedOut = detector.getCurrentEvent().getY() < MapGestureDetector.this.doubleTapFocalPoint.y;
                double normalizedDeltaChange = MathUtils.normalize(pixelDeltaChange, 0.0d, this.screenHeight, 0.0d, 4.0d);
                MapGestureDetector.this.transform.setZoom((zoomedOut ? this.startZoom - normalizedDeltaChange : this.startZoom + normalizedDeltaChange) * ((double) MapGestureDetector.this.uiSettings.getZoomRate()), focalPoint);
            } else {
                MapGestureDetector.this.transform.zoomBy((Math.log((double) detector.getScaleFactor()) / Math.log(1.5707963267948966d)) * 0.6499999761581421d * ((double) MapGestureDetector.this.uiSettings.getZoomRate()), focalPoint);
            }
            MapGestureDetector.this.notifyOnScaleListeners(detector);
            this.spanSinceLast = Math.abs(detector.getCurrentSpan() - detector.getPreviousSpan());
            return true;
        }

        public void onScaleEnd(@NonNull StandardScaleGestureDetector detector, float velocityX, float velocityY) {
            if (this.quickZoom) {
                MapGestureDetector.this.gesturesManager.getMoveGestureDetector().setEnabled(true);
            } else {
                MapGestureDetector.this.gesturesManager.getRotateGestureDetector().setEnabled(true);
            }
            MapGestureDetector.this.notifyOnScaleEndListeners(detector);
            float velocityXY = Math.abs(velocityX) + Math.abs(velocityY);
            if (!MapGestureDetector.this.uiSettings.isScaleVelocityAnimationEnabled() || velocityXY < this.minimumVelocity || ((double) (this.spanSinceLast / velocityXY)) < this.scaleVelocityRatioThreshold) {
                MapGestureDetector.this.dispatchCameraIdle();
                return;
            }
            double zoomAddition = calculateScale((double) velocityXY, detector.isScalingOut());
            Animator unused = MapGestureDetector.this.scaleAnimator = MapGestureDetector.this.createScaleAnimator(MapGestureDetector.this.transform.getRawZoom(), zoomAddition, getScaleFocalPoint(detector), (long) ((Math.log(Math.abs(zoomAddition) + (1.0d / Math.pow(2.718281828459045d, 2.0d))) + 2.0d) * 150.0d));
            MapGestureDetector.this.scheduleAnimator(MapGestureDetector.this.scaleAnimator);
        }

        @NonNull
        private PointF getScaleFocalPoint(@NonNull StandardScaleGestureDetector detector) {
            if (MapGestureDetector.this.constantFocalPoint != null) {
                return MapGestureDetector.this.constantFocalPoint;
            }
            if (this.quickZoom) {
                return new PointF(MapGestureDetector.this.uiSettings.getWidth() / 2.0f, MapGestureDetector.this.uiSettings.getHeight() / 2.0f);
            }
            return detector.getFocalPoint();
        }

        private double calculateScale(double velocityXY, boolean isScalingOut) {
            double zoomAddition = MathUtils.clamp(velocityXY * 2.5d * 1.0E-4d, 0.0d, 2.5d);
            if (isScalingOut) {
                return -zoomAddition;
            }
            return zoomAddition;
        }
    }

    private final class RotateGestureListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        private final float angularVelocityMultiplier;
        private final float defaultSpanSinceStartThreshold;
        private final float minimumAngularVelocity;
        private final float minimumScaleSpanWhenRotating;
        private final double rotateVelocityRatioThreshold;

        RotateGestureListener(float minimumScaleSpanWhenRotating2, double densityMultiplier, float angularVelocityMultiplier2, float minimumAngularVelocity2, float defaultSpanSinceStartThreshold2) {
            this.minimumScaleSpanWhenRotating = minimumScaleSpanWhenRotating2;
            this.angularVelocityMultiplier = angularVelocityMultiplier2;
            this.minimumAngularVelocity = minimumAngularVelocity2;
            this.rotateVelocityRatioThreshold = 2.2000000000000003E-4d * densityMultiplier;
            this.defaultSpanSinceStartThreshold = defaultSpanSinceStartThreshold2;
        }

        public boolean onRotateBegin(@NonNull RotateGestureDetector detector) {
            if (!MapGestureDetector.this.uiSettings.isRotateGesturesEnabled()) {
                return false;
            }
            float deltaSinceLast = Math.abs(detector.getDeltaSinceLast());
            double currTime = (double) detector.getCurrentEvent().getEventTime();
            double prevTime = (double) detector.getPreviousEvent().getEventTime();
            if (currTime == prevTime) {
                return false;
            }
            double speed = ((double) deltaSinceLast) / (currTime - prevTime);
            float deltaSinceStart = Math.abs(detector.getDeltaSinceStart());
            if (speed < 0.04d) {
                return false;
            }
            if (speed > 0.07d && deltaSinceStart < 5.0f) {
                return false;
            }
            if (speed > 0.15d && deltaSinceStart < 7.0f) {
                return false;
            }
            if (speed > 0.5d && deltaSinceStart < 15.0f) {
                return false;
            }
            if (MapGestureDetector.this.uiSettings.isIncreaseScaleThresholdWhenRotating()) {
                MapGestureDetector.this.gesturesManager.getStandardScaleGestureDetector().setSpanSinceStartThreshold(this.minimumScaleSpanWhenRotating);
                MapGestureDetector.this.gesturesManager.getStandardScaleGestureDetector().interrupt();
            }
            MapGestureDetector.this.cancelTransitionsIfRequired();
            MapGestureDetector.this.notifyOnRotateBeginListeners(detector);
            return true;
        }

        public boolean onRotate(@NonNull RotateGestureDetector detector, float rotationDegreesSinceLast, float rotationDegreesSinceFirst) {
            MapGestureDetector.this.cameraChangeDispatcher.onCameraMoveStarted(1);
            PointF focalPoint = getRotateFocalPoint(detector);
            MapGestureDetector.this.transform.setBearing(MapGestureDetector.this.transform.getRawBearing() + ((double) rotationDegreesSinceLast), focalPoint.x, focalPoint.y);
            MapGestureDetector.this.notifyOnRotateListeners(detector);
            return true;
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.mapbox.mapboxsdk.utils.MathUtils.clamp(float, float, float):float
         arg types: [float, int, int]
         candidates:
          com.mapbox.mapboxsdk.utils.MathUtils.clamp(double, double, double):double
          com.mapbox.mapboxsdk.utils.MathUtils.clamp(float, float, float):float */
        public void onRotateEnd(@NonNull RotateGestureDetector detector, float velocityX, float velocityY, float angularVelocity) {
            if (MapGestureDetector.this.uiSettings.isIncreaseScaleThresholdWhenRotating()) {
                MapGestureDetector.this.gesturesManager.getStandardScaleGestureDetector().setSpanSinceStartThreshold(this.defaultSpanSinceStartThreshold);
            }
            MapGestureDetector.this.notifyOnRotateEndListeners(detector);
            float angularVelocity2 = MathUtils.clamp(angularVelocity * this.angularVelocityMultiplier, -30.0f, 30.0f);
            double ratio = (double) (Math.abs(detector.getDeltaSinceLast()) / (Math.abs(velocityX) + Math.abs(velocityY)));
            if (!MapGestureDetector.this.uiSettings.isRotateVelocityAnimationEnabled() || Math.abs(angularVelocity2) < this.minimumAngularVelocity || (MapGestureDetector.this.gesturesManager.getStandardScaleGestureDetector().isInProgress() && ratio < this.rotateVelocityRatioThreshold)) {
                MapGestureDetector.this.dispatchCameraIdle();
                return;
            }
            PointF focalPoint = getRotateFocalPoint(detector);
            Animator unused = MapGestureDetector.this.rotateAnimator = createRotateAnimator(angularVelocity2, (long) ((Math.log(((double) Math.abs(angularVelocity2)) + (1.0d / Math.pow(2.718281828459045d, 2.0d))) + 2.0d) * 150.0d), focalPoint);
            MapGestureDetector.this.scheduleAnimator(MapGestureDetector.this.rotateAnimator);
        }

        @NonNull
        private PointF getRotateFocalPoint(@NonNull RotateGestureDetector detector) {
            if (MapGestureDetector.this.constantFocalPoint != null) {
                return MapGestureDetector.this.constantFocalPoint;
            }
            return detector.getFocalPoint();
        }

        private Animator createRotateAnimator(float angularVelocity, long animationTime, @NonNull final PointF animationFocalPoint) {
            ValueAnimator animator = ValueAnimator.ofFloat(angularVelocity, 0.0f);
            animator.setDuration(animationTime);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                /* class com.mapbox.mapboxsdk.maps.MapGestureDetector.RotateGestureListener.AnonymousClass1 */

                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    MapGestureDetector.this.transform.setBearing(MapGestureDetector.this.transform.getRawBearing() + ((double) ((Float) animation.getAnimatedValue()).floatValue()), animationFocalPoint.x, animationFocalPoint.y, 0);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                /* class com.mapbox.mapboxsdk.maps.MapGestureDetector.RotateGestureListener.AnonymousClass2 */

                public void onAnimationStart(Animator animation) {
                    MapGestureDetector.this.transform.cancelTransitions();
                    MapGestureDetector.this.cameraChangeDispatcher.onCameraMoveStarted(1);
                }

                public void onAnimationCancel(Animator animation) {
                    MapGestureDetector.this.transform.cancelTransitions();
                }

                public void onAnimationEnd(Animator animation) {
                    MapGestureDetector.this.dispatchCameraIdle();
                }
            });
            return animator;
        }
    }

    private final class ShoveGestureListener extends ShoveGestureDetector.SimpleOnShoveGestureListener {
        private ShoveGestureListener() {
        }

        public boolean onShoveBegin(@NonNull ShoveGestureDetector detector) {
            if (!MapGestureDetector.this.uiSettings.isTiltGesturesEnabled()) {
                return false;
            }
            MapGestureDetector.this.cancelTransitionsIfRequired();
            MapGestureDetector.this.gesturesManager.getMoveGestureDetector().setEnabled(false);
            MapGestureDetector.this.notifyOnShoveBeginListeners(detector);
            return true;
        }

        public boolean onShove(@NonNull ShoveGestureDetector detector, float deltaPixelsSinceLast, float deltaPixelsSinceStart) {
            MapGestureDetector.this.cameraChangeDispatcher.onCameraMoveStarted(1);
            MapGestureDetector.this.transform.setTilt(Double.valueOf(MathUtils.clamp(MapGestureDetector.this.transform.getTilt() - ((double) (0.1f * deltaPixelsSinceLast)), 0.0d, 60.0d)));
            MapGestureDetector.this.notifyOnShoveListeners(detector);
            return true;
        }

        public void onShoveEnd(@NonNull ShoveGestureDetector detector, float velocityX, float velocityY) {
            MapGestureDetector.this.dispatchCameraIdle();
            MapGestureDetector.this.gesturesManager.getMoveGestureDetector().setEnabled(true);
            MapGestureDetector.this.notifyOnShoveEndListeners(detector);
        }
    }

    private final class TapGestureListener implements MultiFingerTapGestureDetector.OnMultiFingerTapGestureListener {
        private TapGestureListener() {
        }

        public boolean onMultiFingerTap(@NonNull MultiFingerTapGestureDetector detector, int pointersCount) {
            PointF zoomFocalPoint;
            if (!MapGestureDetector.this.uiSettings.isZoomGesturesEnabled() || pointersCount != 2) {
                return false;
            }
            MapGestureDetector.this.transform.cancelTransitions();
            MapGestureDetector.this.cameraChangeDispatcher.onCameraMoveStarted(1);
            if (MapGestureDetector.this.constantFocalPoint != null) {
                zoomFocalPoint = MapGestureDetector.this.constantFocalPoint;
            } else {
                zoomFocalPoint = detector.getFocalPoint();
            }
            MapGestureDetector.this.zoomOutAnimated(zoomFocalPoint, false);
            return true;
        }
    }

    /* access modifiers changed from: private */
    public Animator createScaleAnimator(double currentZoom, double zoomAddition, @NonNull final PointF animationFocalPoint, long animationTime) {
        ValueAnimator animator = ValueAnimator.ofFloat((float) currentZoom, (float) (currentZoom + zoomAddition));
        animator.setDuration(animationTime);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            /* class com.mapbox.mapboxsdk.maps.MapGestureDetector.AnonymousClass2 */

            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                MapGestureDetector.this.transform.setZoom((double) ((Float) animation.getAnimatedValue()).floatValue(), animationFocalPoint);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            /* class com.mapbox.mapboxsdk.maps.MapGestureDetector.AnonymousClass3 */

            public void onAnimationStart(Animator animation) {
                MapGestureDetector.this.transform.cancelTransitions();
                MapGestureDetector.this.cameraChangeDispatcher.onCameraMoveStarted(1);
            }

            public void onAnimationCancel(Animator animation) {
                MapGestureDetector.this.transform.cancelTransitions();
            }

            public void onAnimationEnd(Animator animation) {
                MapGestureDetector.this.dispatchCameraIdle();
            }
        });
        return animator;
    }

    /* access modifiers changed from: package-private */
    public void zoomInAnimated(@NonNull PointF zoomFocalPoint, boolean runImmediately) {
        zoomAnimated(true, zoomFocalPoint, runImmediately);
    }

    /* access modifiers changed from: package-private */
    public void zoomOutAnimated(@NonNull PointF zoomFocalPoint, boolean runImmediately) {
        zoomAnimated(false, zoomFocalPoint, runImmediately);
    }

    private void zoomAnimated(boolean zoomIn, @NonNull PointF zoomFocalPoint, boolean runImmediately) {
        cancelAnimator(this.scaleAnimator);
        this.scaleAnimator = createScaleAnimator(this.transform.getRawZoom(), zoomIn ? 1.0d : -1.0d, zoomFocalPoint, 300);
        if (runImmediately) {
            this.scaleAnimator.start();
        } else {
            scheduleAnimator(this.scaleAnimator);
        }
    }

    /* access modifiers changed from: private */
    public void dispatchCameraIdle() {
        if (noGesturesInProgress()) {
            this.transform.invalidateCameraPosition();
            this.cameraChangeDispatcher.onCameraIdle();
        }
    }

    /* access modifiers changed from: private */
    public void cancelTransitionsIfRequired() {
        if (noGesturesInProgress()) {
            this.transform.cancelTransitions();
        }
    }

    private boolean noGesturesInProgress() {
        return (!this.uiSettings.isScrollGesturesEnabled() || !this.gesturesManager.getMoveGestureDetector().isInProgress()) && (!this.uiSettings.isZoomGesturesEnabled() || !this.gesturesManager.getStandardScaleGestureDetector().isInProgress()) && ((!this.uiSettings.isRotateGesturesEnabled() || !this.gesturesManager.getRotateGestureDetector().isInProgress()) && (!this.uiSettings.isTiltGesturesEnabled() || !this.gesturesManager.getShoveGestureDetector().isInProgress()));
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x000c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyOnMapClickListeners(@android.support.annotation.NonNull android.graphics.PointF r4) {
        /*
            r3 = this;
            java.util.concurrent.CopyOnWriteArrayList<com.mapbox.mapboxsdk.maps.MapboxMap$OnMapClickListener> r1 = r3.onMapClickListenerList
            java.util.Iterator r1 = r1.iterator()
        L_0x0006:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x001e
            java.lang.Object r0 = r1.next()
            com.mapbox.mapboxsdk.maps.MapboxMap$OnMapClickListener r0 = (com.mapbox.mapboxsdk.maps.MapboxMap.OnMapClickListener) r0
            com.mapbox.mapboxsdk.maps.Projection r2 = r3.projection
            com.mapbox.mapboxsdk.geometry.LatLng r2 = r2.fromScreenLocation(r4)
            boolean r2 = r0.onMapClick(r2)
            if (r2 == 0) goto L_0x0006
        L_0x001e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapbox.mapboxsdk.maps.MapGestureDetector.notifyOnMapClickListeners(android.graphics.PointF):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x000c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyOnMapLongClickListeners(@android.support.annotation.NonNull android.graphics.PointF r4) {
        /*
            r3 = this;
            java.util.concurrent.CopyOnWriteArrayList<com.mapbox.mapboxsdk.maps.MapboxMap$OnMapLongClickListener> r1 = r3.onMapLongClickListenerList
            java.util.Iterator r1 = r1.iterator()
        L_0x0006:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x001e
            java.lang.Object r0 = r1.next()
            com.mapbox.mapboxsdk.maps.MapboxMap$OnMapLongClickListener r0 = (com.mapbox.mapboxsdk.maps.MapboxMap.OnMapLongClickListener) r0
            com.mapbox.mapboxsdk.maps.Projection r2 = r3.projection
            com.mapbox.mapboxsdk.geometry.LatLng r2 = r2.fromScreenLocation(r4)
            boolean r2 = r0.onMapLongClick(r2)
            if (r2 == 0) goto L_0x0006
        L_0x001e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapbox.mapboxsdk.maps.MapGestureDetector.notifyOnMapLongClickListeners(android.graphics.PointF):void");
    }

    /* access modifiers changed from: package-private */
    public void notifyOnFlingListeners() {
        Iterator<MapboxMap.OnFlingListener> it2 = this.onFlingListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onFling();
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnMoveBeginListeners(@NonNull MoveGestureDetector detector) {
        Iterator<MapboxMap.OnMoveListener> it2 = this.onMoveListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onMoveBegin(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnMoveListeners(@NonNull MoveGestureDetector detector) {
        Iterator<MapboxMap.OnMoveListener> it2 = this.onMoveListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onMove(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnMoveEndListeners(@NonNull MoveGestureDetector detector) {
        Iterator<MapboxMap.OnMoveListener> it2 = this.onMoveListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onMoveEnd(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnRotateBeginListeners(@NonNull RotateGestureDetector detector) {
        Iterator<MapboxMap.OnRotateListener> it2 = this.onRotateListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onRotateBegin(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnRotateListeners(@NonNull RotateGestureDetector detector) {
        Iterator<MapboxMap.OnRotateListener> it2 = this.onRotateListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onRotate(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnRotateEndListeners(@NonNull RotateGestureDetector detector) {
        Iterator<MapboxMap.OnRotateListener> it2 = this.onRotateListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onRotateEnd(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnScaleBeginListeners(@NonNull StandardScaleGestureDetector detector) {
        Iterator<MapboxMap.OnScaleListener> it2 = this.onScaleListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onScaleBegin(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnScaleListeners(@NonNull StandardScaleGestureDetector detector) {
        Iterator<MapboxMap.OnScaleListener> it2 = this.onScaleListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onScale(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnScaleEndListeners(@NonNull StandardScaleGestureDetector detector) {
        Iterator<MapboxMap.OnScaleListener> it2 = this.onScaleListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onScaleEnd(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnShoveBeginListeners(@NonNull ShoveGestureDetector detector) {
        Iterator<MapboxMap.OnShoveListener> it2 = this.onShoveListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onShoveBegin(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnShoveListeners(@NonNull ShoveGestureDetector detector) {
        Iterator<MapboxMap.OnShoveListener> it2 = this.onShoveListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onShove(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnShoveEndListeners(@NonNull ShoveGestureDetector detector) {
        Iterator<MapboxMap.OnShoveListener> it2 = this.onShoveListenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onShoveEnd(detector);
        }
    }

    /* access modifiers changed from: package-private */
    public void addOnMapClickListener(MapboxMap.OnMapClickListener onMapClickListener) {
        this.onMapClickListenerList.add(onMapClickListener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnMapClickListener(MapboxMap.OnMapClickListener onMapClickListener) {
        this.onMapClickListenerList.remove(onMapClickListener);
    }

    /* access modifiers changed from: package-private */
    public void addOnMapLongClickListener(MapboxMap.OnMapLongClickListener onMapLongClickListener) {
        this.onMapLongClickListenerList.add(onMapLongClickListener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnMapLongClickListener(MapboxMap.OnMapLongClickListener onMapLongClickListener) {
        this.onMapLongClickListenerList.remove(onMapLongClickListener);
    }

    /* access modifiers changed from: package-private */
    public void addOnFlingListener(MapboxMap.OnFlingListener onFlingListener) {
        this.onFlingListenerList.add(onFlingListener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnFlingListener(MapboxMap.OnFlingListener onFlingListener) {
        this.onFlingListenerList.remove(onFlingListener);
    }

    /* access modifiers changed from: package-private */
    public void addOnMoveListener(MapboxMap.OnMoveListener listener) {
        this.onMoveListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnMoveListener(MapboxMap.OnMoveListener listener) {
        this.onMoveListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnRotateListener(MapboxMap.OnRotateListener listener) {
        this.onRotateListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnRotateListener(MapboxMap.OnRotateListener listener) {
        this.onRotateListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnScaleListener(MapboxMap.OnScaleListener listener) {
        this.onScaleListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnScaleListener(MapboxMap.OnScaleListener listener) {
        this.onScaleListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addShoveListener(MapboxMap.OnShoveListener listener) {
        this.onShoveListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeShoveListener(MapboxMap.OnShoveListener listener) {
        this.onShoveListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public AndroidGesturesManager getGesturesManager() {
        return this.gesturesManager;
    }

    /* access modifiers changed from: package-private */
    public void setGesturesManager(@NonNull Context context, @NonNull AndroidGesturesManager gesturesManager2, boolean attachDefaultListeners, boolean setDefaultMutuallyExclusives) {
        initializeGesturesManager(gesturesManager2, setDefaultMutuallyExclusives);
        initializeGestureListeners(context, attachDefaultListeners);
    }
}
