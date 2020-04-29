package dji.publics.DJIUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DJIDragLayout extends DJIRelativeLayout {
    protected int mDeltaX = 0;
    protected int mDeltaY = 0;
    protected int mHeight = 0;
    protected boolean mIsDragging = false;
    protected boolean mSupportDrag = false;
    protected int mWidth = 0;

    public DJIDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
    }

    /* access modifiers changed from: protected */
    public void trackMotion(MotionEvent event) {
        trackXY(event.getRawX(), event.getRawY());
    }

    /* access modifiers changed from: protected */
    public void trackXY(float x, float y) {
        setX(x - ((float) (this.mWidth / 2)));
        setY(y - ((float) (this.mHeight / 2)));
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mSupportDrag) {
            switch (event.getAction()) {
                case 0:
                    this.mIsDragging = true;
                    this.mDeltaX = (int) event.getX();
                    this.mDeltaY = (int) event.getY();
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case 1:
                case 3:
                    if (this.mIsDragging) {
                        this.mIsDragging = false;
                        break;
                    }
                    break;
                case 2:
                    if (this.mIsDragging) {
                        trackMotion(event);
                        break;
                    }
                    break;
            }
        }
        return this.mIsDragging;
    }
}
