package antistatic.spinnerwheel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public abstract class WheelScroller {
    public static final int MIN_DELTA_FOR_SCROLLING = 1;
    private static final int SCROLLING_DURATION = 400;
    private final int MESSAGE_JUSTIFY = 1;
    private final int MESSAGE_SCROLL = 0;
    /* access modifiers changed from: private */
    public Handler animationHandler = new Handler() {
        /* class antistatic.spinnerwheel.WheelScroller.AnonymousClass2 */

        public void handleMessage(Message msg) {
            WheelScroller.this.scroller.computeScrollOffset();
            int currPosition = WheelScroller.this.getCurrentScrollerPosition();
            int delta = WheelScroller.this.lastScrollPosition - currPosition;
            int unused = WheelScroller.this.lastScrollPosition = currPosition;
            if (delta != 0) {
                WheelScroller.this.listener.onScroll(delta);
            }
            if (Math.abs(currPosition - WheelScroller.this.getFinalScrollerPosition()) < 1) {
                WheelScroller.this.scroller.forceFinished(true);
            }
            if (!WheelScroller.this.scroller.isFinished()) {
                WheelScroller.this.animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == 0) {
                WheelScroller.this.justify();
            } else {
                WheelScroller.this.finishScrolling();
            }
        }
    };
    private Context context;
    private GestureDetector gestureDetector;
    private boolean isScrollingPerformed;
    /* access modifiers changed from: private */
    public int lastScrollPosition;
    private float lastTouchedPosition;
    /* access modifiers changed from: private */
    public ScrollingListener listener;
    protected Scroller scroller;

    public interface ScrollingListener {
        void onFinished();

        void onJustify();

        void onScroll(int i);

        void onStarted();

        void onTouch();

        void onTouchUp();
    }

    /* access modifiers changed from: protected */
    public abstract int getCurrentScrollerPosition();

    /* access modifiers changed from: protected */
    public abstract int getFinalScrollerPosition();

    /* access modifiers changed from: protected */
    public abstract float getMotionEventPosition(MotionEvent motionEvent);

    /* access modifiers changed from: protected */
    public abstract void scrollerFling(int i, int i2, int i3);

    /* access modifiers changed from: protected */
    public abstract void scrollerStartScroll(int i, int i2);

    public WheelScroller(Context context2, ScrollingListener listener2) {
        this.gestureDetector = new GestureDetector(context2, new GestureDetector.SimpleOnGestureListener() {
            /* class antistatic.spinnerwheel.WheelScroller.AnonymousClass1 */

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int unused = WheelScroller.this.lastScrollPosition = 0;
                WheelScroller.this.scrollerFling(WheelScroller.this.lastScrollPosition, (int) velocityX, (int) velocityY);
                WheelScroller.this.setNextMessage(0);
                return true;
            }
        });
        this.gestureDetector.setIsLongpressEnabled(false);
        this.scroller = new Scroller(context2);
        this.listener = listener2;
        this.context = context2;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.scroller.forceFinished(true);
        this.scroller = new Scroller(this.context, interpolator);
    }

    public void scroll(int distance, int time) {
        this.scroller.forceFinished(true);
        this.lastScrollPosition = 0;
        if (time == 0) {
            time = SCROLLING_DURATION;
        }
        scrollerStartScroll(distance, time);
        setNextMessage(0);
        startScrolling();
    }

    public void stopScrolling() {
        this.scroller.forceFinished(true);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.lastTouchedPosition = getMotionEventPosition(event);
                this.scroller.forceFinished(true);
                clearMessages();
                this.listener.onTouch();
                break;
            case 1:
                if (this.scroller.isFinished()) {
                    this.listener.onTouchUp();
                    break;
                }
                break;
            case 2:
                int distance = (int) (getMotionEventPosition(event) - this.lastTouchedPosition);
                if (distance != 0) {
                    startScrolling();
                    this.listener.onScroll(distance);
                    this.lastTouchedPosition = getMotionEventPosition(event);
                    break;
                }
                break;
        }
        if (!this.gestureDetector.onTouchEvent(event) && event.getAction() == 1) {
            justify();
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void setNextMessage(int message) {
        clearMessages();
        this.animationHandler.sendEmptyMessage(message);
    }

    private void clearMessages() {
        this.animationHandler.removeMessages(0);
        this.animationHandler.removeMessages(1);
    }

    /* access modifiers changed from: private */
    public void justify() {
        this.listener.onJustify();
        setNextMessage(1);
    }

    private void startScrolling() {
        if (!this.isScrollingPerformed) {
            this.isScrollingPerformed = true;
            this.listener.onStarted();
        }
    }

    /* access modifiers changed from: protected */
    public void finishScrolling() {
        if (this.isScrollingPerformed) {
            this.listener.onFinished();
            this.isScrollingPerformed = false;
        }
    }
}
