package dji.publics.DJIUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;
import dji.frame.widget.R;

public class DJIRadioButton extends RadioButton {
    public static final int TYPEFACE_BOLD = 3;
    public static final int TYPEFACE_DEMI = 0;
    public static final int TYPEFACE_NBOLD = 2;
    public static final int TYPEFACE_NLIGHT = 1;
    public static final int TYPEFACE_NONE = -1;

    public DJIRadioButton(Context context, AttributeSet attrs) {
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
}
