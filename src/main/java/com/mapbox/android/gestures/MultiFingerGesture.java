package com.mapbox.android.gestures;

import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.annotation.UiThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@UiThread
public abstract class MultiFingerGesture<L> extends BaseGesture<L> {
    private static final int DEFAULT_REQUIRED_FINGERS_COUNT = 2;
    private static final float PRESSURE_THRESHOLD = 0.67f;
    private DisplayMetrics displayMetrics;
    private final float edgeSlop;
    private PointF focalPoint = new PointF();
    private final PermittedActionsGuard permittedActionsGuard = new PermittedActionsGuard();
    final List<Integer> pointerIdList = new ArrayList();
    final HashMap<PointerDistancePair, MultiFingerDistancesObject> pointersDistanceMap = new HashMap<>();
    private float spanThreshold;

    public MultiFingerGesture(Context context, AndroidGesturesManager gesturesManager) {
        super(context, gesturesManager);
        this.edgeSlop = (float) ViewConfiguration.get(context).getScaledEdgeSlop();
        queryDisplayMetrics();
    }

    /* access modifiers changed from: protected */
    public boolean analyzeEvent(MotionEvent motionEvent) {
        boolean isMissingEvents;
        int action = motionEvent.getActionMasked();
        if (action == 0) {
            queryDisplayMetrics();
        }
        if (this.permittedActionsGuard.isMissingActions(action, motionEvent.getPointerCount(), this.pointerIdList.size()) || (action == 2 && isMissingPointers(motionEvent))) {
            isMissingEvents = true;
        } else {
            isMissingEvents = false;
        }
        if (isMissingEvents) {
            if ((this instanceof ProgressiveGesture) && ((ProgressiveGesture) this).isInProgress()) {
                ((ProgressiveGesture) this).gestureStopped();
            }
            this.pointerIdList.clear();
            this.pointersDistanceMap.clear();
        }
        if (!isMissingEvents || action == 0) {
            updatePointerList(motionEvent);
        }
        this.focalPoint = Utils.determineFocalPoint(motionEvent);
        if (isMissingEvents) {
            Log.w("MultiFingerGesture", "Some MotionEvents were not passed to the library or events from different view trees are merged.");
            return false;
        }
        if (action == 2 && this.pointerIdList.size() >= getRequiredPointersCount() && checkPressure()) {
            calculateDistances();
            if (!isSloppyGesture()) {
                return analyzeMovement();
            }
        }
        return false;
    }

    private void queryDisplayMetrics() {
        if (this.windowManager != null) {
            this.displayMetrics = new DisplayMetrics();
            Display display = this.windowManager.getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= 17) {
                display.getRealMetrics(this.displayMetrics);
            } else {
                display.getMetrics(this.displayMetrics);
            }
        } else {
            this.displayMetrics = this.context.getResources().getDisplayMetrics();
        }
    }

    private void updatePointerList(MotionEvent motionEvent) {
        int action = motionEvent.getActionMasked();
        if (action == 0 || action == 5) {
            this.pointerIdList.add(Integer.valueOf(motionEvent.getPointerId(motionEvent.getActionIndex())));
        } else if (action == 1 || action == 6) {
            this.pointerIdList.remove(Integer.valueOf(motionEvent.getPointerId(motionEvent.getActionIndex())));
        }
    }

    private boolean isMissingPointers(MotionEvent motionEvent) {
        boolean hasPointer;
        for (Integer num : this.pointerIdList) {
            if (motionEvent.findPointerIndex(num.intValue()) != -1) {
                hasPointer = true;
                continue;
            } else {
                hasPointer = false;
                continue;
            }
            if (!hasPointer) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean checkPressure() {
        return getCurrentEvent().getPressure() / getPreviousEvent().getPressure() > PRESSURE_THRESHOLD;
    }

    private boolean checkSpanBelowThreshold() {
        for (MultiFingerDistancesObject distancesObject : this.pointersDistanceMap.values()) {
            if (distancesObject.getCurrFingersDiffXY() < this.spanThreshold) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public int getRequiredPointersCount() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public boolean analyzeMovement() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isSloppyGesture() {
        boolean isSloppy;
        float rightSlopEdge = ((float) this.displayMetrics.widthPixels) - this.edgeSlop;
        float bottomSlopEdge = ((float) this.displayMetrics.heightPixels) - this.edgeSlop;
        float edgeSlop2 = this.edgeSlop;
        for (Integer num : this.pointerIdList) {
            int pointerIndex = getCurrentEvent().findPointerIndex(num.intValue());
            float x = Utils.getRawX(getCurrentEvent(), pointerIndex);
            float y = Utils.getRawY(getCurrentEvent(), pointerIndex);
            if (x < edgeSlop2 || y < edgeSlop2 || x > rightSlopEdge || y > bottomSlopEdge) {
                isSloppy = true;
                continue;
            } else {
                isSloppy = false;
                continue;
            }
            if (isSloppy) {
                return true;
            }
        }
        return checkSpanBelowThreshold();
    }

    /* access modifiers changed from: protected */
    public boolean canExecute(int invokedGestureType) {
        return super.canExecute(invokedGestureType) && !isSloppyGesture();
    }

    /* access modifiers changed from: protected */
    public void reset() {
    }

    private void calculateDistances() {
        this.pointersDistanceMap.clear();
        for (int i = 0; i < this.pointerIdList.size() - 1; i++) {
            for (int j = i + 1; j < this.pointerIdList.size(); j++) {
                int primaryPointerId = this.pointerIdList.get(i).intValue();
                int secondaryPointerId = this.pointerIdList.get(j).intValue();
                float px0 = getPreviousEvent().getX(getPreviousEvent().findPointerIndex(primaryPointerId));
                float py0 = getPreviousEvent().getY(getPreviousEvent().findPointerIndex(primaryPointerId));
                float px1 = getPreviousEvent().getX(getPreviousEvent().findPointerIndex(secondaryPointerId));
                float cx0 = getCurrentEvent().getX(getCurrentEvent().findPointerIndex(primaryPointerId));
                float cy0 = getCurrentEvent().getY(getCurrentEvent().findPointerIndex(primaryPointerId));
                this.pointersDistanceMap.put(new PointerDistancePair(Integer.valueOf(primaryPointerId), Integer.valueOf(secondaryPointerId)), new MultiFingerDistancesObject(px1 - px0, getPreviousEvent().getY(getPreviousEvent().findPointerIndex(secondaryPointerId)) - py0, getCurrentEvent().getX(getCurrentEvent().findPointerIndex(secondaryPointerId)) - cx0, getCurrentEvent().getY(getCurrentEvent().findPointerIndex(secondaryPointerId)) - cy0));
            }
        }
    }

    public float getCurrentSpan(int firstPointerIndex, int secondPointerIndex) {
        if (verifyPointers(firstPointerIndex, secondPointerIndex)) {
            return this.pointersDistanceMap.get(new PointerDistancePair(this.pointerIdList.get(firstPointerIndex), this.pointerIdList.get(secondPointerIndex))).getCurrFingersDiffXY();
        }
        throw new NoSuchElementException("There is no such pair of pointers!");
    }

    public float getPreviousSpan(int firstPointerIndex, int secondPointerIndex) {
        if (verifyPointers(firstPointerIndex, secondPointerIndex)) {
            return this.pointersDistanceMap.get(new PointerDistancePair(this.pointerIdList.get(firstPointerIndex), this.pointerIdList.get(secondPointerIndex))).getPrevFingersDiffXY();
        }
        throw new NoSuchElementException("There is no such pair of pointers!");
    }

    public float getCurrentSpanX(int firstPointerIndex, int secondPointerIndex) {
        if (verifyPointers(firstPointerIndex, secondPointerIndex)) {
            return Math.abs(this.pointersDistanceMap.get(new PointerDistancePair(this.pointerIdList.get(firstPointerIndex), this.pointerIdList.get(secondPointerIndex))).getCurrFingersDiffX());
        }
        throw new NoSuchElementException("There is no such pair of pointers!");
    }

    public float getCurrentSpanY(int firstPointerIndex, int secondPointerIndex) {
        if (verifyPointers(firstPointerIndex, secondPointerIndex)) {
            return Math.abs(this.pointersDistanceMap.get(new PointerDistancePair(this.pointerIdList.get(firstPointerIndex), this.pointerIdList.get(secondPointerIndex))).getCurrFingersDiffY());
        }
        throw new NoSuchElementException("There is no such pair of pointers!");
    }

    public float getPreviousSpanX(int firstPointerIndex, int secondPointerIndex) {
        if (verifyPointers(firstPointerIndex, secondPointerIndex)) {
            return Math.abs(this.pointersDistanceMap.get(new PointerDistancePair(this.pointerIdList.get(firstPointerIndex), this.pointerIdList.get(secondPointerIndex))).getPrevFingersDiffX());
        }
        throw new NoSuchElementException("There is no such pair of pointers!");
    }

    public float getPreviousSpanY(int firstPointerIndex, int secondPointerIndex) {
        if (verifyPointers(firstPointerIndex, secondPointerIndex)) {
            return Math.abs(this.pointersDistanceMap.get(new PointerDistancePair(this.pointerIdList.get(firstPointerIndex), this.pointerIdList.get(secondPointerIndex))).getPrevFingersDiffY());
        }
        throw new NoSuchElementException("There is no such pair of pointers!");
    }

    private boolean verifyPointers(int firstPointerIndex, int secondPointerIndex) {
        return firstPointerIndex != secondPointerIndex && firstPointerIndex >= 0 && secondPointerIndex >= 0 && firstPointerIndex < getPointersCount() && secondPointerIndex < getPointersCount();
    }

    public int getPointersCount() {
        return this.pointerIdList.size();
    }

    public PointF getFocalPoint() {
        return this.focalPoint;
    }

    public float getSpanThreshold() {
        return this.spanThreshold;
    }

    public void setSpanThreshold(float spanThreshold2) {
        this.spanThreshold = spanThreshold2;
    }

    public void setSpanThresholdResource(@DimenRes int spanThresholdDimen) {
        setSpanThreshold(this.context.getResources().getDimension(spanThresholdDimen));
    }
}
