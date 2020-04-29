package dji.component.flysafe;

import android.graphics.Color;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FlyForbidBasePainter {
    public static final int AREA_TYPE_HELICOPTER = 19;
    public static final int AREA_TYPE_PRIVATE_AIRPORT = 14;
    protected final int mColorTransparent = Color.argb(0, 0, 0, 0);

    public enum DrawShape {
        SIMPLE_MARKER,
        POLYGON,
        CIRCLE
    }

    /* access modifiers changed from: protected */
    public boolean needDrawOuterCircle(int type, int outRadius) {
        return (type == 0 || type == 9 || type == 255) && outRadius != 0;
    }

    public void destroy() {
    }
}
