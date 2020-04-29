package antistatic.spinnerwheel;

import android.content.Context;
import android.view.MotionEvent;
import antistatic.spinnerwheel.WheelScroller;

public class WheelVerticalScroller extends WheelScroller {
    public WheelVerticalScroller(Context context, WheelScroller.ScrollingListener listener) {
        super(context, listener);
    }

    /* access modifiers changed from: protected */
    public int getCurrentScrollerPosition() {
        return this.scroller.getCurrY();
    }

    /* access modifiers changed from: protected */
    public int getFinalScrollerPosition() {
        return this.scroller.getFinalY();
    }

    /* access modifiers changed from: protected */
    public float getMotionEventPosition(MotionEvent event) {
        return event.getY();
    }

    /* access modifiers changed from: protected */
    public void scrollerStartScroll(int distance, int time) {
        this.scroller.startScroll(0, 0, 0, distance, time);
    }

    /* access modifiers changed from: protected */
    public void scrollerFling(int position, int velocityX, int velocityY) {
        this.scroller.fling(0, position, 0, -velocityY, 0, 0, -2147483647, Integer.MAX_VALUE);
    }
}
