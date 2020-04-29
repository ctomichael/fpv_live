package antistatic.spinnerwheel;

import android.content.Context;
import android.view.MotionEvent;
import antistatic.spinnerwheel.WheelScroller;

public class WheelHorizontalScroller extends WheelScroller {
    public WheelHorizontalScroller(Context context, WheelScroller.ScrollingListener listener) {
        super(context, listener);
    }

    /* access modifiers changed from: protected */
    public int getCurrentScrollerPosition() {
        return this.scroller.getCurrX();
    }

    /* access modifiers changed from: protected */
    public int getFinalScrollerPosition() {
        return this.scroller.getFinalX();
    }

    /* access modifiers changed from: protected */
    public float getMotionEventPosition(MotionEvent event) {
        return event.getX();
    }

    /* access modifiers changed from: protected */
    public void scrollerStartScroll(int distance, int time) {
        this.scroller.startScroll(0, 0, distance, 0, time);
    }

    /* access modifiers changed from: protected */
    public void scrollerFling(int position, int velocityX, int velocityY) {
        this.scroller.fling(position, 0, -velocityX, 0, -2147483647, Integer.MAX_VALUE, 0, 0);
    }
}
