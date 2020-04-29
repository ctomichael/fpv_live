package com.mapbox.android.gestures;

import android.content.Context;
import android.support.annotation.UiThread;
import android.view.MotionEvent;
import android.view.WindowManager;
import java.util.Iterator;
import java.util.Set;

@UiThread
public abstract class BaseGesture<L> {
    protected final Context context;
    private MotionEvent currentEvent;
    private long gestureDuration;
    private final AndroidGesturesManager gesturesManager;
    private boolean isEnabled = true;
    protected L listener;
    private MotionEvent previousEvent;
    protected final WindowManager windowManager;

    /* access modifiers changed from: protected */
    public abstract boolean analyzeEvent(MotionEvent motionEvent);

    public BaseGesture(Context context2, AndroidGesturesManager gesturesManager2) {
        this.context = context2;
        this.windowManager = (WindowManager) context2.getSystemService("window");
        this.gesturesManager = gesturesManager2;
    }

    /* access modifiers changed from: protected */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return analyze(motionEvent);
    }

    private boolean analyze(MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        if (this.previousEvent != null) {
            this.previousEvent.recycle();
            this.previousEvent = null;
        }
        if (this.currentEvent != null) {
            this.previousEvent = MotionEvent.obtain(this.currentEvent);
            this.currentEvent.recycle();
            this.currentEvent = null;
        }
        this.currentEvent = MotionEvent.obtain(motionEvent);
        this.gestureDuration = this.currentEvent.getEventTime() - this.currentEvent.getDownTime();
        return analyzeEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public boolean canExecute(int invokedGestureType) {
        if (this.listener == null || !this.isEnabled) {
            return false;
        }
        for (Set<Integer> exclusives : this.gesturesManager.getMutuallyExclusiveGestures()) {
            if (exclusives.contains(Integer.valueOf(invokedGestureType))) {
                for (Integer num : exclusives) {
                    int gestureType = num.intValue();
                    Iterator<BaseGesture> it2 = this.gesturesManager.getDetectors().iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            BaseGesture detector = it2.next();
                            if (detector instanceof ProgressiveGesture) {
                                ProgressiveGesture progressiveDetector = (ProgressiveGesture) detector;
                                if (progressiveDetector.getHandledTypes().contains(Integer.valueOf(gestureType)) && progressiveDetector.isInProgress()) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                continue;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void setListener(L listener2) {
        this.listener = listener2;
    }

    /* access modifiers changed from: protected */
    public void removeListener() {
        this.listener = null;
    }

    public long getGestureDuration() {
        return this.gestureDuration;
    }

    public MotionEvent getCurrentEvent() {
        return this.currentEvent;
    }

    public MotionEvent getPreviousEvent() {
        return this.previousEvent;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
}
