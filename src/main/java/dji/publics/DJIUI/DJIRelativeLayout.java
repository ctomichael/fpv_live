package dji.publics.DJIUI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import dji.publics.interfaces.DJIViewAnimShowInterface;
import dji.publics.interfaces.DJIViewShowInterface;

public class DJIRelativeLayout extends RelativeLayout implements DJIViewShowInterface, DJIViewAnimShowInterface {
    private AnimatorListenerAdapter animGoListener = new AnimatorListenerAdapter() {
        /* class dji.publics.DJIUI.DJIRelativeLayout.AnonymousClass1 */

        public void onAnimationEnd(Animator animation) {
            DJIRelativeLayout.this.go();
            DJIRelativeLayout.this.animate().setListener(null);
        }
    };
    private AnimatorListenerAdapter animShowListener = new AnimatorListenerAdapter() {
        /* class dji.publics.DJIUI.DJIRelativeLayout.AnonymousClass2 */

        public void onAnimationStart(Animator animation) {
            DJIRelativeLayout.this.show();
        }

        public void onAnimationEnd(Animator animation) {
            DJIRelativeLayout.this.animate().setListener(null);
        }
    };
    private OnResizeListener resizeListener;

    public interface OnResizeListener {
        void onResizeChanged(int i, int i2, int i3, int i4);
    }

    public DJIRelativeLayout(Context context) {
        super(context);
    }

    public DJIRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DJIRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void show() {
        if (getVisibility() != 0) {
            setVisibility(0);
        }
    }

    public void hide() {
        if (getVisibility() != 4) {
            setVisibility(4);
        }
    }

    public void go() {
        if (getVisibility() != 8) {
            setVisibility(8);
        }
    }

    public void setOnResizeListener(OnResizeListener listener) {
        this.resizeListener = listener;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.resizeListener != null) {
            this.resizeListener.onResizeChanged(w, h, oldw, oldh);
        }
    }

    public void animGo() {
        animate().alpha(0.0f).setDuration(300).setListener(this.animGoListener).start();
    }

    public void animShow() {
        animate().alpha(1.0f).setDuration(300).setListener(this.animShowListener).start();
    }
}
