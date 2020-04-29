package com.mapbox.android.gestures;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.UiThread;
import android.view.MotionEvent;
import java.util.HashMap;

@UiThread
public class MultiFingerTapGestureDetector extends MultiFingerGesture<OnMultiFingerTapGestureListener> {
    private boolean invalidMovement;
    private int lastPointersDownCount;
    private float multiFingerTapMovementThreshold;
    private long multiFingerTapTimeThreshold;
    private boolean pointerLifted;

    public interface OnMultiFingerTapGestureListener {
        boolean onMultiFingerTap(MultiFingerTapGestureDetector multiFingerTapGestureDetector, int i);
    }

    public MultiFingerTapGestureDetector(Context context, AndroidGesturesManager gesturesManager) {
        super(context, gesturesManager);
    }

    /* access modifiers changed from: protected */
    public boolean analyzeEvent(MotionEvent motionEvent) {
        super.analyzeEvent(motionEvent);
        switch (motionEvent.getActionMasked()) {
            case 1:
                boolean handled = false;
                if (canExecute(4)) {
                    handled = ((OnMultiFingerTapGestureListener) this.listener).onMultiFingerTap(this, this.lastPointersDownCount);
                }
                reset();
                return handled;
            case 2:
                if (!this.invalidMovement) {
                    this.invalidMovement = exceededMovementThreshold(this.pointersDistanceMap);
                    break;
                }
                break;
            case 5:
                if (this.pointerLifted) {
                    this.invalidMovement = true;
                }
                this.lastPointersDownCount = this.pointerIdList.size();
                break;
            case 6:
                this.pointerLifted = true;
                break;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean exceededMovementThreshold(HashMap<PointerDistancePair, MultiFingerDistancesObject> map) {
        boolean z;
        for (MultiFingerDistancesObject distancesObject : map.values()) {
            float diffX = Math.abs(distancesObject.getCurrFingersDiffX() - distancesObject.getPrevFingersDiffX());
            float diffY = Math.abs(distancesObject.getCurrFingersDiffY() - distancesObject.getPrevFingersDiffY());
            if (diffX > this.multiFingerTapMovementThreshold || diffY > this.multiFingerTapMovementThreshold) {
                z = true;
            } else {
                z = false;
            }
            this.invalidMovement = z;
            if (this.invalidMovement) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean canExecute(int invokedGestureType) {
        if (this.lastPointersDownCount <= 1 || this.invalidMovement || getGestureDuration() >= this.multiFingerTapTimeThreshold || !super.canExecute(invokedGestureType)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void reset() {
        super.reset();
        this.lastPointersDownCount = 0;
        this.invalidMovement = false;
        this.pointerLifted = false;
    }

    public long getMultiFingerTapTimeThreshold() {
        return this.multiFingerTapTimeThreshold;
    }

    public void setMultiFingerTapTimeThreshold(long multiFingerTapTimeThreshold2) {
        this.multiFingerTapTimeThreshold = multiFingerTapTimeThreshold2;
    }

    public float getMultiFingerTapMovementThreshold() {
        return this.multiFingerTapMovementThreshold;
    }

    public void setMultiFingerTapMovementThreshold(float multiFingerTapMovementThreshold2) {
        this.multiFingerTapMovementThreshold = multiFingerTapMovementThreshold2;
    }

    public void setMultiFingerTapMovementThresholdResource(@DimenRes int multiFingerTapMovementThresholdDimen) {
        setMultiFingerTapMovementThreshold(this.context.getResources().getDimension(multiFingerTapMovementThresholdDimen));
    }
}
