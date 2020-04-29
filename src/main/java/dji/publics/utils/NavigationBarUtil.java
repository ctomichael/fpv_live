package dji.publics.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class NavigationBarUtil {
    @TargetApi(19)
    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(134217728);
        }
    }

    @TargetApi(19)
    public static void noTransparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().clearFlags(134217728);
        }
    }

    public static void hide(Activity activity) {
        int flags;
        if (Build.VERSION.SDK_INT >= 19) {
            flags = 4098;
        } else {
            flags = 2;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }
}
