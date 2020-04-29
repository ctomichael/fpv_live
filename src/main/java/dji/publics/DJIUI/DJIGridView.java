package dji.publics.DJIUI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import dji.publics.interfaces.DJIViewAnimShowInterface;
import dji.publics.interfaces.DJIViewShowInterface;

public class DJIGridView extends GridView implements DJIViewShowInterface, DJIViewAnimShowInterface {
    private AnimatorListenerAdapter animGoListener = new AnimatorListenerAdapter() {
        /* class dji.publics.DJIUI.DJIGridView.AnonymousClass1 */

        public void onAnimationEnd(Animator animation) {
            DJIGridView.this.go();
            DJIGridView.this.animate().setListener(null);
        }
    };
    private AnimatorListenerAdapter animShowListener = new AnimatorListenerAdapter() {
        /* class dji.publics.DJIUI.DJIGridView.AnonymousClass2 */

        public void onAnimationStart(Animator animation) {
            DJIGridView.this.show();
        }

        public void onAnimationEnd(Animator animation) {
            DJIGridView.this.animate().setListener(null);
        }
    };

    public DJIGridView(Context context) {
        super(context);
    }

    public DJIGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DJIGridView(Context context, AttributeSet attrs, int defStyle) {
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

    public void animGo() {
        animate().alpha(0.0f).setDuration(300).setListener(this.animGoListener).start();
    }

    public void animShow() {
        animate().alpha(1.0f).setDuration(300).setListener(this.animShowListener).start();
    }
}
