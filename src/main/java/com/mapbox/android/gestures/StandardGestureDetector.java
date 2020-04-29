package com.mapbox.android.gestures;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

@UiThread
public class StandardGestureDetector extends BaseGesture<StandardOnGestureListener> {
    private final GestureDetectorCompat gestureDetector;
    final StandardOnGestureListener innerListener = new StandardOnGestureListener() {
        /* class com.mapbox.android.gestures.StandardGestureDetector.AnonymousClass1 */

        public boolean onSingleTapUp(MotionEvent e) {
            return StandardGestureDetector.this.canExecute(5) && ((StandardOnGestureListener) StandardGestureDetector.this.listener).onSingleTapUp(e);
        }

        public void onLongPress(MotionEvent e) {
            if (StandardGestureDetector.this.canExecute(6)) {
                ((StandardOnGestureListener) StandardGestureDetector.this.listener).onLongPress(e);
            }
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return StandardGestureDetector.this.canExecute(0) && ((StandardOnGestureListener) StandardGestureDetector.this.listener).onScroll(e1, e2, distanceX, distanceY);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return StandardGestureDetector.this.canExecute(7) && ((StandardOnGestureListener) StandardGestureDetector.this.listener).onFling(e1, e2, velocityX, velocityY);
        }

        public void onShowPress(MotionEvent e) {
            if (StandardGestureDetector.this.canExecute(8)) {
                ((StandardOnGestureListener) StandardGestureDetector.this.listener).onShowPress(e);
            }
        }

        public boolean onDown(MotionEvent e) {
            return StandardGestureDetector.this.canExecute(9) && ((StandardOnGestureListener) StandardGestureDetector.this.listener).onDown(e);
        }

        public boolean onDoubleTap(MotionEvent e) {
            return StandardGestureDetector.this.canExecute(10) && ((StandardOnGestureListener) StandardGestureDetector.this.listener).onDoubleTap(e);
        }

        public boolean onDoubleTapEvent(MotionEvent e) {
            return StandardGestureDetector.this.canExecute(11) && ((StandardOnGestureListener) StandardGestureDetector.this.listener).onDoubleTapEvent(e);
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            return StandardGestureDetector.this.canExecute(12) && ((StandardOnGestureListener) StandardGestureDetector.this.listener).onSingleTapConfirmed(e);
        }
    };

    public interface StandardOnGestureListener extends GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    }

    public StandardGestureDetector(Context context, AndroidGesturesManager androidGesturesManager) {
        super(context, androidGesturesManager);
        this.gestureDetector = new GestureDetectorCompat(context, this.innerListener);
    }

    public static class SimpleStandardOnGestureListener implements StandardOnGestureListener {
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        public boolean onDown(MotionEvent e) {
            return false;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public boolean analyzeEvent(MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    public boolean isLongpressEnabled() {
        return this.gestureDetector.isLongpressEnabled();
    }

    public void setIsLongpressEnabled(boolean enabled) {
        this.gestureDetector.setIsLongpressEnabled(enabled);
    }
}
