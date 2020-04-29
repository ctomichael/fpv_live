package com.mapbox.android.gestures;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import java.util.HashSet;
import java.util.Set;

@UiThread
public class StandardScaleGestureDetector extends ProgressiveGesture<StandardOnScaleGestureListener> {
    private static final float QUICK_SCALE_MULTIPLIER = 0.5f;
    private static final Set<Integer> handledTypes = new HashSet();
    private float currentSpan;
    private float currentSpanX;
    private float currentSpanY;
    private final GestureDetectorCompat innerGestureDetector;
    private boolean isScalingOut;
    private float previousSpan;
    private float previousSpanX;
    private float previousSpanY;
    /* access modifiers changed from: private */
    public boolean quickScale;
    /* access modifiers changed from: private */
    public PointF quickScaleFocalPoint;
    private float scaleFactor;
    private float spanDeltaSinceStart;
    private float spanSinceStartThreshold;
    private float startSpan;
    private float startSpanX;
    private float startSpanY;

    public interface StandardOnScaleGestureListener {
        boolean onScale(StandardScaleGestureDetector standardScaleGestureDetector);

        boolean onScaleBegin(StandardScaleGestureDetector standardScaleGestureDetector);

        void onScaleEnd(StandardScaleGestureDetector standardScaleGestureDetector, float f, float f2);
    }

    static {
        handledTypes.add(1);
        handledTypes.add(15);
    }

    public StandardScaleGestureDetector(Context context, AndroidGesturesManager androidGesturesManager) {
        super(context, androidGesturesManager);
        this.innerGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            /* class com.mapbox.android.gestures.StandardScaleGestureDetector.AnonymousClass1 */

            public boolean onDoubleTapEvent(MotionEvent event) {
                if (event.getActionMasked() == 0) {
                    boolean unused = StandardScaleGestureDetector.this.quickScale = true;
                    PointF unused2 = StandardScaleGestureDetector.this.quickScaleFocalPoint = new PointF(event.getX(), event.getY());
                }
                return true;
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean analyzeMovement() {
        boolean z = false;
        int i = 1;
        super.analyzeMovement();
        if (!isInProgress() || !this.quickScale || getPointersCount() <= 1) {
            PointF focal = this.quickScale ? this.quickScaleFocalPoint : getFocalPoint();
            this.currentSpanX = 0.0f;
            this.currentSpanY = 0.0f;
            for (int i2 = 0; i2 < getPointersCount(); i2++) {
                this.currentSpanX += Math.abs(getCurrentEvent().getX(i2) - focal.x);
                this.currentSpanY += Math.abs(getCurrentEvent().getY(i2) - focal.y);
            }
            this.currentSpanX *= 2.0f;
            this.currentSpanY *= 2.0f;
            if (this.quickScale) {
                this.currentSpan = this.currentSpanY;
            } else {
                this.currentSpan = (float) Math.hypot((double) this.currentSpanX, (double) this.currentSpanY);
            }
            if (this.startSpan == 0.0f) {
                this.startSpan = this.currentSpan;
                this.startSpanX = this.currentSpanX;
                this.startSpanY = this.currentSpanY;
            }
            this.spanDeltaSinceStart = Math.abs(this.startSpan - this.currentSpan);
            this.scaleFactor = calculateScaleFactor();
            if (this.scaleFactor < 1.0f) {
                z = true;
            }
            this.isScalingOut = z;
            boolean handled = false;
            if (!isInProgress() || this.currentSpan <= 0.0f) {
                if (this.quickScale) {
                    i = 15;
                }
                if (canExecute(i) && this.spanDeltaSinceStart >= this.spanSinceStartThreshold && (handled = ((StandardOnScaleGestureListener) this.listener).onScaleBegin(this))) {
                    gestureStarted();
                }
            } else {
                handled = ((StandardOnScaleGestureListener) this.listener).onScale(this);
            }
            this.previousSpan = this.currentSpan;
            this.previousSpanX = this.currentSpanX;
            this.previousSpanY = this.currentSpanY;
            return handled;
        }
        gestureStopped();
        return false;
    }

    /* access modifiers changed from: protected */
    public void gestureStopped() {
        super.gestureStopped();
        ((StandardOnScaleGestureListener) this.listener).onScaleEnd(this, this.velocityX, this.velocityY);
        this.quickScale = false;
    }

    /* access modifiers changed from: protected */
    public void reset() {
        super.reset();
        this.startSpan = 0.0f;
        this.spanDeltaSinceStart = 0.0f;
        this.currentSpan = 0.0f;
        this.previousSpan = 0.0f;
        this.scaleFactor = 1.0f;
    }

    /* access modifiers changed from: protected */
    public boolean analyzeEvent(MotionEvent motionEvent) {
        int action = motionEvent.getActionMasked();
        if (this.quickScale) {
            if (action == 5 || action == 3) {
                if (isInProgress()) {
                    interrupt();
                } else {
                    this.quickScale = false;
                }
            } else if (!isInProgress() && action == 1) {
                this.quickScale = false;
            }
        }
        return this.innerGestureDetector.onTouchEvent(motionEvent) | super.analyzeEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public int getRequiredPointersCount() {
        if (!isInProgress() || this.quickScale) {
            return 1;
        }
        return 2;
    }

    /* access modifiers changed from: protected */
    public boolean isSloppyGesture() {
        return super.isSloppyGesture() || (!this.quickScale && getPointersCount() < 2);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Set<Integer> provideHandledTypes() {
        return handledTypes;
    }

    public static class SimpleStandardOnScaleGestureListener implements StandardOnScaleGestureListener {
        public boolean onScaleBegin(StandardScaleGestureDetector detector) {
            return true;
        }

        public boolean onScale(StandardScaleGestureDetector detector) {
            return false;
        }

        public void onScaleEnd(StandardScaleGestureDetector detector, float velocityX, float velocityY) {
        }
    }

    public boolean isScalingOut() {
        return this.isScalingOut;
    }

    public float getSpanSinceStartThreshold() {
        return this.spanSinceStartThreshold;
    }

    public void setSpanSinceStartThreshold(float spanSinceStartThreshold2) {
        this.spanSinceStartThreshold = spanSinceStartThreshold2;
    }

    public void setSpanSinceStartThresholdResource(@DimenRes int spanSinceStartThresholdDimen) {
        setSpanSinceStartThreshold(this.context.getResources().getDimension(spanSinceStartThresholdDimen));
    }

    public float getScaleFactor() {
        return this.scaleFactor;
    }

    public float getStartSpan() {
        return this.startSpan;
    }

    public float getStartSpanX() {
        return this.startSpanX;
    }

    public float getStartSpanY() {
        return this.startSpanY;
    }

    public float getCurrentSpan() {
        return this.currentSpan;
    }

    public float getCurrentSpanX() {
        return this.currentSpanX;
    }

    public float getCurrentSpanY() {
        return this.currentSpanY;
    }

    public float getPreviousSpan() {
        return this.previousSpan;
    }

    public float getPreviousSpanX() {
        return this.previousSpanX;
    }

    public float getPreviousSpanY() {
        return this.previousSpanY;
    }

    private float calculateScaleFactor() {
        if (this.quickScale) {
            boolean scaleOut = (getCurrentEvent().getY() < this.quickScaleFocalPoint.y && this.currentSpan < this.previousSpan) || (getCurrentEvent().getY() > this.quickScaleFocalPoint.y && this.currentSpan > this.previousSpan);
            float spanDiff = Math.abs(1.0f - (this.currentSpan / this.previousSpan)) * QUICK_SCALE_MULTIPLIER;
            if (this.previousSpan <= 0.0f) {
                return 1.0f;
            }
            return scaleOut ? 1.0f + spanDiff : 1.0f - spanDiff;
        } else if (this.previousSpan > 0.0f) {
            return this.currentSpan / this.previousSpan;
        } else {
            return 1.0f;
        }
    }
}
