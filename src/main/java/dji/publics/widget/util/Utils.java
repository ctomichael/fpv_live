package dji.publics.widget.util;

import android.content.Context;
import android.support.annotation.DimenRes;

public class Utils {
    public static int getDimens(Context context, @DimenRes int dimenId) {
        return (int) context.getResources().getDimension(dimenId);
    }
}
