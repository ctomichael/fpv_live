package com.mapbox.android.gestures;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.MotionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UiThread
public class MoveGestureDetector extends ProgressiveGesture<OnMoveGestureListener> {
    private static final int MOVE_REQUIRED_POINTERS_COUNT = 1;
    private static final Set<Integer> handledTypes = new HashSet();
    float lastDistanceX;
    float lastDistanceY;
    private final Map<Integer, MoveDistancesObject> moveDistancesObjectMap = new HashMap();
    private float moveThreshold;
    private PointF previousFocalPoint;
    private boolean resetFocal;

    public interface OnMoveGestureListener {
        boolean onMove(MoveGestureDetector moveGestureDetector, float f, float f2);

        boolean onMoveBegin(MoveGestureDetector moveGestureDetector);

        void onMoveEnd(MoveGestureDetector moveGestureDetector, float f, float f2);
    }

    static {
        handledTypes.add(13);
    }

    public MoveGestureDetector(Context context, AndroidGesturesManager gesturesManager) {
        super(context, gesturesManager);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Set<Integer> provideHandledTypes() {
        return handledTypes;
    }

    public static class SimpleOnMoveGestureListener implements OnMoveGestureListener {
        public boolean onMoveBegin(MoveGestureDetector detector) {
            return true;
        }

        public boolean onMove(MoveGestureDetector detector, float distanceX, float distanceY) {
            return false;
        }

        public void onMoveEnd(MoveGestureDetector detector, float velocityX, float velocityY) {
        }
    }

    /* access modifiers changed from: protected */
    public boolean analyzeEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case 0:
            case 5:
                this.resetFocal = true;
                this.moveDistancesObjectMap.put(Integer.valueOf(motionEvent.getPointerId(motionEvent.getActionIndex())), new MoveDistancesObject(motionEvent.getX(motionEvent.getActionIndex()), motionEvent.getY(motionEvent.getActionIndex())));
                break;
            case 1:
                this.moveDistancesObjectMap.clear();
                break;
            case 3:
                this.moveDistancesObjectMap.clear();
                break;
            case 6:
                this.resetFocal = true;
                this.moveDistancesObjectMap.remove(Integer.valueOf(motionEvent.getPointerId(motionEvent.getActionIndex())));
                break;
        }
        return super.analyzeEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public boolean analyzeMovement() {
        super.analyzeMovement();
        updateMoveDistancesObjects();
        if (isInProgress()) {
            PointF currentFocalPoint = getFocalPoint();
            this.lastDistanceX = this.previousFocalPoint.x - currentFocalPoint.x;
            this.lastDistanceY = this.previousFocalPoint.y - currentFocalPoint.y;
            this.previousFocalPoint = currentFocalPoint;
            if (!this.resetFocal) {
                return ((OnMoveGestureListener) this.listener).onMove(this, this.lastDistanceX, this.lastDistanceY);
            }
            this.resetFocal = false;
            return ((OnMoveGestureListener) this.listener).onMove(this, 0.0f, 0.0f);
        } else if (!canExecute(13) || !((OnMoveGestureListener) this.listener).onMoveBegin(this)) {
            return false;
        } else {
            gestureStarted();
            this.previousFocalPoint = getFocalPoint();
            this.resetFocal = false;
            return true;
        }
    }

    private void updateMoveDistancesObjects() {
        for (Integer num : this.pointerIdList) {
            int pointerId = num.intValue();
            this.moveDistancesObjectMap.get(Integer.valueOf(pointerId)).addNewPosition(getCurrentEvent().getX(getCurrentEvent().findPointerIndex(pointerId)), getCurrentEvent().getY(getCurrentEvent().findPointerIndex(pointerId)));
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x0010  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean checkAnyMoveAboveThreshold() {
        /*
            r4 = this;
            java.util.Map<java.lang.Integer, com.mapbox.android.gestures.MoveDistancesObject> r1 = r4.moveDistancesObjectMap
            java.util.Collection r1 = r1.values()
            java.util.Iterator r1 = r1.iterator()
        L_0x000a:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0034
            java.lang.Object r0 = r1.next()
            com.mapbox.android.gestures.MoveDistancesObject r0 = (com.mapbox.android.gestures.MoveDistancesObject) r0
            float r2 = r0.getDistanceXSinceStart()
            float r2 = java.lang.Math.abs(r2)
            float r3 = r4.moveThreshold
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 >= 0) goto L_0x0032
            float r2 = r0.getDistanceYSinceStart()
            float r2 = java.lang.Math.abs(r2)
            float r3 = r4.moveThreshold
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 < 0) goto L_0x000a
        L_0x0032:
            r1 = 1
        L_0x0033:
            return r1
        L_0x0034:
            r1 = 0
            goto L_0x0033
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapbox.android.gestures.MoveGestureDetector.checkAnyMoveAboveThreshold():boolean");
    }

    /* access modifiers changed from: protected */
    public boolean canExecute(int invokedGestureType) {
        return super.canExecute(invokedGestureType) && checkAnyMoveAboveThreshold();
    }

    /* access modifiers changed from: protected */
    public void reset() {
        super.reset();
    }

    /* access modifiers changed from: protected */
    public void gestureStopped() {
        super.gestureStopped();
        ((OnMoveGestureListener) this.listener).onMoveEnd(this, this.velocityX, this.velocityY);
    }

    /* access modifiers changed from: protected */
    public int getRequiredPointersCount() {
        return 1;
    }

    public float getMoveThreshold() {
        return this.moveThreshold;
    }

    public void setMoveThreshold(float moveThreshold2) {
        this.moveThreshold = moveThreshold2;
    }

    public void setMoveThresholdResource(@DimenRes int moveThresholdDimen) {
        setMoveThreshold(this.context.getResources().getDimension(moveThresholdDimen));
    }

    public float getLastDistanceX() {
        return this.lastDistanceX;
    }

    public float getLastDistanceY() {
        return this.lastDistanceY;
    }

    public MoveDistancesObject getMoveObject(int pointerIndex) {
        if (!isInProgress() || pointerIndex < 0 || pointerIndex >= getPointersCount()) {
            return null;
        }
        return this.moveDistancesObjectMap.get(this.pointerIdList.get(pointerIndex));
    }
}
