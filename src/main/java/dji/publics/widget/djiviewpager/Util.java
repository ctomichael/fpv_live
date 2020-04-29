package dji.publics.widget.djiviewpager;

import android.content.res.Resources;
import android.util.TypedValue;

public class Util {
    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(1, (float) dp, res.getDisplayMetrics());
    }
}
