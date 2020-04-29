package com.mapbox.android.gestures;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import java.util.HashSet;
import java.util.Set;

@UiThread
public class SidewaysShoveGestureDetector extends ProgressiveGesture<OnSidewaysShoveGestureListener> {
    private static final Set<Integer> handledTypes = new HashSet();
    float deltaPixelSinceLast;
    float deltaPixelsSinceStart;
    private float maxShoveAngle;
    private float pixelDeltaThreshold;

    public interface OnSidewaysShoveGestureListener {
        boolean onSidewaysShove(SidewaysShoveGestureDetector sidewaysShoveGestureDetector, float f, float f2);

        boolean onSidewaysShoveBegin(SidewaysShoveGestureDetector sidewaysShoveGestureDetector);

        void onSidewaysShoveEnd(SidewaysShoveGestureDetector sidewaysShoveGestureDetector, float f, float f2);
    }

    static {
        handledTypes.add(14);
    }

    public SidewaysShoveGestureDetector(Context context, AndroidGesturesManager gesturesManager) {
        super(context, gesturesManager);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Set<Integer> provideHandledTypes() {
        return handledTypes;
    }

    public static class SimpleOnSidewaysShoveGestureListener implements OnSidewaysShoveGestureListener {
        public boolean onSidewaysShoveBegin(SidewaysShoveGestureDetector detector) {
            return true;
        }

        public boolean onSidewaysShove(SidewaysShoveGestureDetector detector, float deltaPixelsSinceLast, float deltaPixelsSinceStart) {
            return false;
        }

        public void onSidewaysShoveEnd(SidewaysShoveGestureDetector detector, float velocityX, float velocityY) {
        }
    }

    /* access modifiers changed from: protected */
    public boolean analyzeMovement() {
        super.analyzeMovement();
        this.deltaPixelSinceLast = calculateDeltaPixelsSinceLast();
        this.deltaPixelsSinceStart += this.deltaPixelSinceLast;
        if (isInProgress() && this.deltaPixelSinceLast != 0.0f) {
            return ((OnSidewaysShoveGestureListener) this.listener).onSidewaysShove(this, this.deltaPixelSinceLast, this.deltaPixelsSinceStart);
        }
        if (!canExecute(14) || !((OnSidewaysShoveGestureListener) this.listener).onSidewaysShoveBegin(this)) {
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
        ((OnSidewaysShoveGestureListener) this.listener).onSidewaysShoveEnd(this, this.velocityX, this.velocityY);
    }

    /* access modifiers changed from: protected */
    public void reset() {
        super.reset();
        this.deltaPixelsSinceStart = 0.0f;
    }

    /* access modifiers changed from: package-private */
    public boolean isAngleAcceptable() {
        MultiFingerDistancesObject distancesObject = (MultiFingerDistancesObject) this.pointersDistanceMap.get(new PointerDistancePair((Integer) this.pointerIdList.get(0), (Integer) this.pointerIdList.get(1)));
        return Math.abs(Math.toDegrees(Math.abs(Math.atan2((double) distancesObject.getCurrFingersDiffY(), (double) distancesObject.getCurrFingersDiffX()))) - 90.0d) <= ((double) this.maxShoveAngle);
    }

    /* access modifiers changed from: package-private */
    public float calculateDeltaPixelsSinceLast() {
        return ((getCurrentEvent().getX(getCurrentEvent().findPointerIndex(((Integer) this.pointerIdList.get(0)).intValue())) + getCurrentEvent().getX(getCurrentEvent().findPointerIndex(((Integer) this.pointerIdList.get(1)).intValue()))) / 2.0f) - ((getPreviousEvent().getX(getPreviousEvent().findPointerIndex(((Integer) this.pointerIdList.get(0)).intValue())) + getPreviousEvent().getX(getPreviousEvent().findPointerIndex(((Integer) this.pointerIdList.get(1)).intValue()))) / 2.0f);
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
