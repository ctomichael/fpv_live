package dji.publics.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import dji.fieldAnnotation.EXClassNullAway;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@EXClassNullAway
public class StatusBarUtil {
    public static final int FLYME = 2;
    public static final int MIUI = 1;
    public static final int OTHERS = 3;
    public static final int UNKNOW = 0;

    @TargetApi(19)
    public static void transparencyBar(Activity activity) {
        if (RomUtil.isOppo() && Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.clearFlags(67108864);
            window.getDecorView().setSystemUiVisibility(1280);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().setFlags(67108864, 67108864);
        }
    }

    public static int StatusBarLightMode(Activity activity) {
        if (Build.VERSION.SDK_INT < 19) {
            return 0;
        }
        if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
            return 1;
        }
        if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
            return 2;
        }
        if (Build.VERSION.SDK_INT < 23) {
            return 0;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(9216);
        return 3;
    }

    public static int StatusBarDarkMode(Activity activity) {
        if (Build.VERSION.SDK_INT < 19) {
            return 0;
        }
        if (MIUISetStatusBarLightMode(activity.getWindow(), false)) {
            return 1;
        }
        if (FlymeSetStatusBarLightMode(activity.getWindow(), false)) {
            return 2;
        }
        if (Build.VERSION.SDK_INT < 23) {
            return 0;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(0);
        return 3;
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        int value;
        if (window == null) {
            return false;
        }
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value2 = meizuFlags.getInt(lp);
            if (dark) {
                value = value2 | bit;
            } else {
                value = value2 & (bit ^ -1);
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                int darkModeFlag = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
                if (dark) {
                    extraFlagField.invoke(window, Integer.valueOf(darkModeFlag), Integer.valueOf(darkModeFlag));
                } else {
                    extraFlagField.invoke(window, 0, Integer.valueOf(darkModeFlag));
                }
                result = true;
                if (Build.VERSION.SDK_INT >= 23) {
                    if (dark) {
                        window.getDecorView().setSystemUiVisibility(9216);
                    } else {
                        window.getDecorView().setSystemUiVisibility(0);
                    }
                }
            } catch (Exception e) {
            }
        }
        return result;
    }
}
