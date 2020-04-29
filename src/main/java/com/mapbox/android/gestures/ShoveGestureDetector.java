package com.mapbox.android.gestures;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import java.util.HashSet;
import java.util.Set;

@UiThread
public class ShoveGestureDetector extends ProgressiveGesture<OnShoveGestureListener> {
    private static final Set<Integer> handledTypes = new HashSet();
    float deltaPixelSinceLast;
    float deltaPixelsSinceStart;
    private float maxShoveAngle;
    private float pixelDeltaThreshold;

    public interface OnShoveGestureListener {
        boolean onShove(ShoveGestureDetector shoveGestureDetector, float f, float f2);

        boolean onShoveBegin(ShoveGestureDetector shoveGestureDetector);

        void onShoveEnd(ShoveGestureDetector shoveGestureDetector, float f, float f2);
    }

    static {
        handledTypes.add(3);
    }

    public ShoveGestureDetector(Context context, AndroidGesturesManager gesturesManager) {
        super(context, gesturesManager);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Set<Integer> provideHandledTypes() {
        return handledTypes;
    }

    public static class SimpleOnShoveGestureListener implements OnShoveGestureListener {
        public boolean onShoveBegin(ShoveGestureDetector detector) {
            return true;
        }

        public boolean onShove(ShoveGestureDetector detector, float deltaPixelsSinceLast, float deltaPixelsSinceStart) {
            return false;
        }

        public void onShoveEnd(ShoveGestureDetector detector, float velocityX, float velocityY) {
        }
    }

    /* access modifiers changed from: protected */
    public boolean analyzeMovement() {
        super.analyzeMovement();
        this.deltaPixelSinceLast = calculateDeltaPixelsSinceLast();
        this.deltaPixelsSinceStart += this.deltaPixelSinceLast;
        if (isInProgress() && this.deltaPixelSinceLast != 0.0f) {
            return ((OnShoveGestureListener) this.listener).onShove(this, this.deltaPixelSinceLast, this.deltaPixelsSinceStart);
        }
        if (!canExecute(3) || !((OnShoveGestureListener) this.listener).onShoveBegin(this)) {
            return false;
        }
        gestureStarted();
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean canExecute(int invokedGestureType) {
        return Math.abs(this.deltaPixelsSinceStart) >= this.pixelDeltaThreshold && super.canExecute(invokedGestureType);
    }

    /* access modifiers changed from: protected */
    public boolean isSloppyGesture() {
        return super.isSloppyGesture() || !isAngleAcceptable();
    }

    /* access modifiers changed from: protected */
    public void gestureStopped() {
        super.gestureStopped();
        ((OnShoveGestureListener) this.listener).onShoveEnd(this, this.velocityX, this.velocityY);
    }

    /* access modifiers changed from: protected */
    public void reset() {
        super.reset();
        this.deltaPixelsSinceStart = 0.0f;
    }

    /* access modifiers changed from: package-private */
    public boolean isAngleAcceptable() {
        MultiFingerDistancesObject distancesObject = (MultiFingerDistancesObject) this.pointersDistanceMap.get(new PointerDistancePair((Integer) this.pointerIdList.get(0), (Integer) this.pointerIdList.get(1)));
        double angle = Math.toDegrees(Math.abs(Math.atan2((double) distancesObject.getCurrFingersDiffY(), (double) distancesObject.getCurrFingersDiffX())));
        return angle <= ((double) this.maxShoveAngle) || 180.0d - angle <= ((double) this.maxShoveAngle);
    }

    /* access modifiers changed from: package-private */
    public float calculateDeltaPixelsSinceLast() {
        return ((getCurrentEvent().getY(getCurrentEvent().findPointerIndex(((Integer) this.pointerIdList.get(0)).intValue())) + getCurrentEvent().getY(getCurrentEvent().findPointerIndex(((Integer) this.pointerIdList.get(1)).intValue()))) / 2.0f) - ((getPreviousEvent().getY(getPreviousEvent().findPointerIndex(((Integer) this.pointerIdList.get(0)).intValue())) + getPreviousEvent().getY(getPreviousEvent().findPointerIndex(((Integer) this.pointerIdList.get(1)).intValue()))) / 2.0f);
    }

    public float getDeltaPixelsSinceStart() {
        return this.deltaPixelsSinceStart;
    }

    public float getDeltaPixelSinceLast() {
        return this.deltaPixelSinceLast;
    }

    public float getPixelDeltaThreshold() {
        return this.pixelDeltaThreshold;
    }

    public void setPixelDeltaThreshold(float pixelDeltaThreshold2) {
        this.pixelDeltaThreshold = pixelDeltaThreshold2;
    }

    public void setPixelDeltaThresholdResource(@DimenRes int pixelDeltaThresholdDimen) {
        setPixelDeltaThreshold(this.context.getResources().getDimension(pixelDeltaThresholdDimen));
    }

    public float getMaxShoveAngle() {
        return this.maxShoveAngle;
    }

    public void setMaxShoveAngle(float maxShoveAngle2) {
        this.maxShoveAngle = maxShoveAngle2;
    }
}
