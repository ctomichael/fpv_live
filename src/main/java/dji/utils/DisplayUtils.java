package dji.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import dji.component.flysafe.FlyForbidProtocol;

public class DisplayUtils {
    private static final String HUAWEI = "huawei";
    private static final String OPPO = "oppo";
    private static final String SMARTISAN = "smartisan";
    private static final String VIVO = "vivo";
    private static final String XIAOMI = "xiaomi";

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService("window");
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        if (isPortrait(context)) {
            return point.x;
        }
        return point.y;
    }

    public static int getMinScreenSize(Context context) {
        return getScreenHeight(context) < getScreenWidth(context) ? getScreenHeight(context) : getScreenWidth(context);
    }

    public static int getMaxScreenSize(Context context) {
        return getScreenHeight(context) < getScreenWidth(context) ? getScreenWidth(context) : getScreenHeight(context);
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService("window");
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        if (isPortrait(context)) {
            return point.y;
        }
        return point.x;
    }

    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public static int getScreenDentisyDpi() {
        return Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    public static void setFullScreen(@NonNull Activity activity) {
        activity.getWindow().addFlags(1024);
    }

    public static void setNonFullScreen(@NonNull Activity activity) {
        activity.getWindow().clearFlags(1024);
    }

    public static boolean isFullScreen(@NonNull Activity activity) {
        return (activity.getWindow().getAttributes().flags & 1024) == 1024;
    }

    public static void setLandscape(@NonNull Activity activity) {
        activity.setRequestedOrientation(0);
    }

    public static void setPortrait(@NonNull Activity activity) {
        activity.setRequestedOrientation(1);
    }

    public static boolean isLandscape(Context context) {
        return context.getApplicationContext().getResources().getConfiguration().orientation == 2;
    }

    public static boolean isPortrait(Context context) {
        return context.getApplicationContext().getResources().getConfiguration().orientation == 1;
    }

    public static int getScreenRotation(@NonNull Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case 0:
            default:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
        }
    }

    public static boolean isNotchScreen(Context context) {
        String manufacture = DeviceUtils.getManufacture();
        char c = 65535;
        switch (manufacture.hashCode()) {
            case -1443430368:
                if (manufacture.equals(SMARTISAN)) {
                    c = 4;
                    break;
                }
                break;
            case -1206476313:
                if (manufacture.equals(HUAWEI)) {
                    c = 2;
                    break;
                }
                break;
            case -759499589:
                if (manufacture.equals(XIAOMI)) {
                    c = 3;
                    break;
                }
                break;
            case 3418016:
                if (manufacture.equals(OPPO)) {
                    c = 1;
                    break;
                }
                break;
            case 3620012:
                if (manufacture.equals(VIVO)) {
                    c = 0;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return hasNotchInVivo(context);
            case 1:
                return hasNotchInOppo(context);
            case 2:
                return hasNotchInHuawei(context);
            case 3:
                return hasNotchInXiaomi(context);
            case 4:
                return hasNotchInSmartisan(context);
            default:
                return false;
        }
    }

    public static int getNotchHeight(Context context) {
        if (hasNotchInOppo(context)) {
            return 80;
        }
        if (hasNotchInVivo(context)) {
            return UnitUtils.dp2px(27.0f);
        }
        if (hasNotchInHuawei(context)) {
            return 80;
        }
        if (hasNotchInXiaomi(context)) {
            return getXiaomiNotchHeight(context);
        }
        if (hasNotchInSmartisan(context)) {
            return 82;
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", FlyForbidProtocol.PLATFORM_NAME_FOR_JNI);
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private static boolean hasNotchInHuawei(Context context) {
        try {
            Class HwNotchSizeUtil = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            return ((Boolean) HwNotchSizeUtil.getMethod("hasNotchInScreen", new Class[0]).invoke(HwNotchSizeUtil, new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean hasNotchInVivo(Context context) {
        try {
            Class FtFeature = context.getClassLoader().loadClass("android.util.FtFeature");
            return ((Boolean) FtFeature.getMethod("isFeatureSupport", Integer.TYPE).invoke(FtFeature, 32)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean hasNotchInOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    private static boolean hasNotchInXiaomi(Context context) {
        try {
            Class systemProperties = context.getClassLoader().loadClass("android.os.SystemProperties");
            if (((Integer) systemProperties.getMethod("getInt", String.class, Integer.TYPE).invoke(systemProperties, "ro.miui.notch", 0)).intValue() == 1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasNotchInSmartisan(Context context) {
        try {
            Class DisplayUtilsSmt = context.getClassLoader().loadClass("smartisanos.api.DisplayUtilsSmt");
            return ((Boolean) DisplayUtilsSmt.getMethod("isFeatureSupport", Integer.TYPE).invoke(DisplayUtilsSmt, 1)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int getXiaomiNotchHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", FlyForbidProtocol.PLATFORM_NAME_FOR_JNI);
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return getStatusBarHeight(context);
    }

    public static void hideNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(4098);
        }
    }
}
