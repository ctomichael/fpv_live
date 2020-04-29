package com.mapbox.android.gestures;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import java.util.HashSet;
import java.util.Set;

@UiThread
public class RotateGestureDetector extends ProgressiveGesture<OnRotateGestureListener> {
    private static final Set<Integer> handledTypes = new HashSet();
    private float angleThreshold;
    float deltaSinceLast;
    float deltaSinceStart;

    public interface OnRotateGestureListener {
        boolean onRotate(RotateGestureDetector rotateGestureDetector, float f, float f2);

        boolean onRotateBegin(RotateGestureDetector rotateGestureDetector);

        void onRotateEnd(RotateGestureDetector rotateGestureDetector, float f, float f2, float f3);
    }

    static {
        handledTypes.add(2);
    }

    public RotateGestureDetector(Context context, AndroidGesturesManager gesturesManager) {
        super(context, gesturesManager);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Set<Integer> provideHandledTypes() {
        return handledTypes;
    }

    public static class SimpleOnRotateGestureListener implements OnRotateGestureListener {
        public boolean onRotateBegin(RotateGestureDetector detector) {
            return true;
        }

        public boolean onRotate(RotateGestureDetector detector, float rotationDegreesSinceLast, float rotationDegreesSinceFirst) {
            return true;
        }

        public void onRotateEnd(RotateGestureDetector detector, float velocityX, float velocityY, float angularVelocity) {
        }
    }

    /* access modifiers changed from: protected */
    public boolean analyzeMovement() {
        super.analyzeMovement();
        this.deltaSinceLast = getRotationDegreesSinceLast();
        this.deltaSinceStart += this.deltaSinceLast;
        if (isInProgress() && this.deltaSinceLast != 0.0f) {
            return ((OnRotateGestureListener) this.listener).onRotate(this, this.deltaSinceLast, this.deltaSinceStart);
        }
        if (!canExecute(2) || !((OnRotateGestureListener) this.listener).onRotateBegin(this)) {
            return false;
        }
        gestureStarted();
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean canExecute(int invokedGestureType) {
        return Math.abs(this.deltaSinceStart) >= this.angleThreshold && super.canExecute(invokedGestureType);
    }

    /* access modifiers changed from: protected */
    public void gestureStopped() {
        super.gestureStopped();
        if (this.deltaSinceLast == 0.0f) {
            this.velocityX = 0.0f;
            this.velocityY = 0.0f;
        }
        ((OnRotateGestureListener) this.listener).onRotateEnd(this, this.velocityX, this.velocityY, calculateAngularVelocityVector(this.velocityX, this.velocityY));
    }

    /* access modifiers changed from: protected */
    public void reset() {
        super.reset();
        this.deltaSinceStart = 0.0f;
    }

    /* access modifiers changed from: package-private */
    public float getRotationDegreesSinceLast() {
        MultiFingerDistancesObject distancesObject = (MultiFingerDistancesObject) this.pointersDistanceMap.get(new PointerDistancePair((Integer) this.pointerIdList.get(0), (Integer) this.pointerIdList.get(1)));
        return (float) Math.toDegrees(Math.atan2((double) distancesObject.getPrevFingersDiffY(), (double) distancesObject.getPrevFingersDiffX()) - Math.atan2((double) distancesObject.getCurrFingersDiffY(), (double) distancesObject.getCurrFingersDiffX()));
    }

    /* access modifiers changed from: package-private */
    public float calculateAngularVelocityVector(float velocityX, float velocityY) {
        float angularVelocity = Math.abs((float) (((double) ((getFocalPoint().x * velocityY) + (getFocalPoint().y * velocityX))) / (Math.pow((double) getFocalPoint().x, 2.0d) + Math.pow((double) getFocalPoint().y, 2.0d))));
        if (this.deltaSinceLast < 0.0f) {
            return -angularVelocity;
        }
        return angularVelocity;
    }

    public float getDeltaSinceStart() {
        return this.deltaSinceStart;
    }

    public float getDeltaSinceLast() {
        return this.deltaSinceLast;
    }

    public float getAngleThreshold() {
        return this.angleThreshold;
    }

    public void setAngleThreshold(float angleThreshold2) {
        this.angleThreshold = angleThreshold2;
    }
}
