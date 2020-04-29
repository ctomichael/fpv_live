package dji.log;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.widget.TextView;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class LogView extends TextView {
    public LogView(Context context) {
        this(context, null);
    }

    public LogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
