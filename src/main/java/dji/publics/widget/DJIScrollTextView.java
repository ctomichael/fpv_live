package dji.publics.widget;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import dji.publics.DJIUI.DJITextView;

public class DJIScrollTextView extends DJITextView {
    public DJIScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
