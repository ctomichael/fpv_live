package dji.publics.DJIUI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import dji.frame.widget.R;
import dji.publics.interfaces.DJIViewAnimShowInterface;
import dji.publics.interfaces.DJIViewShowInterface;

public class DJITextView extends TextView implements DJIViewShowInterface, DJIViewAnimShowInterface {
    public static Typeface BOLD = null;
    public static Typeface DEMI = null;
    public static Typeface NBOLD = null;
    public static Typeface NLIGHT = null;
    public static final int TYPEFACE_BOLD = 3;
    public static final int TYPEFACE_DEMI = 0;
    public static final int TYPEFACE_NBOLD = 2;
    public static final int TYPEFACE_NLIGHT = 1;
    public static final int TYPEFACE_NONE = -1;
    public static Typeface face;
    private AnimatorListenerAdapter animGoListener = new AnimatorListenerAdapter() {
        /* class dji.publics.DJIUI.DJITextView.AnonymousClass1 */

        public void onAnimationEnd(Animator animation) {
            DJITextView.this.go();
            DJITextView.this.animate().setListener(null);
        }
    };
    private AnimatorListenerAdapter animShowListener = new AnimatorListenerAdapter() {
        /* class dji.publics.DJIUI.DJITextView.AnonymousClass2 */

        public void onAnimationStart(Animator animation) {
            DJITextView.this.show();
        }

        public void onAnimationEnd(Animator animation) {
            DJITextView.this.animate().setListener(null);
        }
    };

    public static Typeface getTypface(Context context, int tfValue) {
        initTypeface(context);
        if (tfValue == 0) {
            return DEMI;
        }
        if (1 == tfValue) {
            return NLIGHT;
        }
        if (2 == tfValue) {
            return NBOLD;
        }
        if (3 == tfValue) {
            return BOLD;
        }
        return null;
    }

    public static void initTypeface(Context context) {
        if (DEMI == null) {
            AssetManager am = context.getAssets();
            DEMI = Typeface.createFromAsset(am, "fonts/Dji-Demi.ttf");
            NLIGHT = Typeface.createFromAsset(am, "fonts/Dji_NLight.ttf");
            NBOLD = Typeface.createFromAsset(am, "fonts/Dji_NBold.ttf");
            BOLD = Typeface.createFromAsset(am, "fonts/Dji-Bold.ttf");
            face = DEMI;
        }
    }

    public DJITextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            TypedArray ar = context.obtainStyledAttributes(attrs, R.styleable.DJITextView);
            int faceValue = ar.getInt(R.styleable.DJITextView_djiTextFace, -1);
            ar.recycle();
            Typeface tf = getTypeface();
            if (faceValue == 0) {
                return;
            }
            if (1 == faceValue) {
                Typeface ltf = Typeface.create("sans-serif-light", 0);
                if (ltf != null) {
                    setTypeface(ltf);
                }
            } else if (2 == faceValue) {
                if (tf != null && !tf.isBold()) {
                    setTypeface(tf, 1);
                }
            } else if (3 == faceValue && tf != null && !tf.isBold()) {
                setTypeface(tf, 1);
            }
        }
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
