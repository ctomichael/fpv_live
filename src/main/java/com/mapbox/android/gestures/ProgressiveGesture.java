package com.mapbox.android.gestures;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import java.util.Set;

@UiThread
public abstract class ProgressiveGesture<L> extends MultiFingerGesture<L> {
    private final Set<Integer> handledTypes = provideHandledTypes();
    private boolean interrupted;
    private boolean isInProgress;
    VelocityTracker velocityTracker;
    float velocityX;
    float velocityY;

    /* access modifiers changed from: protected */
    @NonNull
    public abstract Set<Integer> provideHandledTypes();

    public ProgressiveGesture(Context context, AndroidGesturesManager gesturesManager) {
        super(context, gesturesManager);
    }

    /* access modifiers changed from: protected */
    public boolean analyzeEvent(MotionEvent motionEvent) {
        int action = motionEvent.getActionMasked();
        if (action == 0 || action == 5 || action == 6 || action == 3) {
            reset();
        }
        if (this.interrupted) {
            this.interrupted = false;
            reset();
            gestureStopped();
        }
        if (this.velocityTracker != null) {
            this.velocityTracker.addMovement(getCurrentEvent());
        }
        boolean movementHandled = super.analyzeEvent(motionEvent);
        if (action == 1 || action == 6) {
            if (this.pointerIdList.size() >= getRequiredPointersCount() || !this.isInProgress) {
                return movementHandled;
            }
            gestureStopped();
            return true;
        } else if (action != 3 || !this.isInProgress) {
            return movementHandled;
        } else {
            gestureStopped();
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void gestureStarted() {
        this.isInProgress = true;
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
    }

    /* access modifiers changed from: protected */
    public void gestureStopped() {
        this.isInProgress = false;
        if (this.velocityTracker != null) {
            this.velocityTracker.computeCurrentVelocity(1000);
            this.velocityX = this.velocityTracker.getXVelocity();
            this.velocityY = this.velocityTracker.getYVelocity();
            this.velocityTracker.recycle();
            this.velocityTracker = null;
        }
        reset();
    }

    /* access modifiers changed from: package-private */
    public Set<Integer> getHandledTypes() {
        return this.handledTypes;
    }

    public boolean isInProgress() {
        return this.isInProgress;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            interrupt();
        }
    }

    public void interrupt() {
        if (isInProgress()) {
            this.interrupted = true;
        }
    }
}
