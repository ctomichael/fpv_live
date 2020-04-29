package dji.publics.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class DJIThumbSeekBar extends SeekBar {
    private boolean mHandleEvent = true;

    public DJIThumbSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent event) {
        Drawable thumb = getThumb();
        if (thumb == null) {
            this.mHandleEvent = true;
        } else if (event.getAction() == 0) {
            if (!thumb.getBounds().contains((int) event.getX(), (int) event.getY())) {
                this.mHandleEvent = false;
            } else {
                this.mHandleEvent = true;
            }
        }
        if (this.mHandleEvent) {
            return super.onTouchEvent(event);
        }
        return true;
    }
}
