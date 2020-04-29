package dji.publics.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.DisplayCutout;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import dji.component.flysafe.FlyForbidProtocol;
import dji.log.DJILog;
import dji.utils.DeviceUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DisplayCutoutHelper {
    private static final int FLAG_NOTCH_SUPPORT = 65536;
    private static final String HUAWEI = "huawei";
    private static final String OPPO = "oppo";
    private static final String SAMSUNG = "samsung";
    private static final String TAG = "DisplayCutoutHelper";
    private static final String VIVO = "vivo";
    private static final String XIAOMI = "xiaomi";
    private static int sAndroidPNotchHeight = -1;
    private static int sNotchHeight = -1;

    public static String getManufacture() {
        return Build.MANUFACTURER.toLowerCase();
    }

    public static String getModel() {
        return Build.MODEL.toLowerCase();
    }

    public static boolean isNotchScreen(Context context) {
        String manufacture = getManufacture();
        char c = 65535;
        switch (manufacture.hashCode()) {
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
            case 1864941562:
                if (manufacture.equals(SAMSUNG)) {
                    c = 4;
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
                return hasNotchInSamsung(context);
            default:
                return false;
        }
    }

    public static int getNotchHeight(Window window) {
        if (Build.VERSION.SDK_INT >= 28) {
            sNotchHeight = getAndroidPNotchHeight(window);
        } else {
            sNotchHeight = getsAndroidPrePNotchHeight(window);
        }
        return sNotchHeight;
    }

    public static int getRotation(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService("window");
        if (wm != null) {
            return wm.getDefaultDisplay().getRotation();
        }
        return 0;
    }

    public static void setFullScreen(Window window) {
        setFullScreenWithSystemUi(window);
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.layoutInDisplayCutoutMode = 2;
            window.setAttributes(lp);
        } else if (DeviceUtils.isHuawei() && hasNotchInHuawei(window.getContext())) {
            setHuaWeiFullScreen(window);
        } else if (DeviceUtils.isMIUI() && hasNotchInXiaomi(window.getContext())) {
            setMiuiFullScreen(window);
        } else if (DeviceUtils.isOppo() && hasNotchInOppo(window.getContext())) {
            setOppoFullScreen(window);
        } else if (DeviceUtils.isVivo() && hasNotchInVivo(window.getContext())) {
            setVivoFullScreen(window);
        } else if (DeviceUtils.isSumsung() && hasNotchInSamsung(window.getContext())) {
            setSamsungFullScreen(window);
        }
    }

    public static void setAndroidPNotchHeight(int notchHeight) {
        sAndroidPNotchHeight = notchHeight;
    }

    @RequiresApi(api = 28)
    private static boolean hasNotchInAndroidP(Window window) {
        DisplayCutout displayCutout;
        WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
        if (windowInsets == null || (displayCutout = windowInsets.getDisplayCutout()) == null || displayCutout.getBoundingRects() == null) {
            return false;
        }
        return true;
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

    private static boolean hasNotchInSamsung(Context context) {
        try {
            Resources resources = context.getResources();
            int resId = resources.getIdentifier("config_mainBuiltInDisplayCutout", "string", FlyForbidProtocol.PLATFORM_NAME_FOR_JNI);
            String spec = resId > 0 ? resources.getString(resId) : null;
            if (spec == null || TextUtils.isEmpty(spec)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
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

    private static int getOppoNotchHeight(Context context) {
        return getStatusBarHeight(context);
    }

    private static int getXiaomiNotchHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", FlyForbidProtocol.PLATFORM_NAME_FOR_JNI);
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return getStatusBarHeight(context);
    }

    private static int getSamsungNotchHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", FlyForbidProtocol.PLATFORM_NAME_FOR_JNI);
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    private static int getsAndroidPrePNotchHeight(Window window) {
        if (sNotchHeight > -1) {
            return sNotchHeight;
        }
        if (DeviceUtils.isOppo() && hasNotchInOppo(window.getContext())) {
            sNotchHeight = getOppoNotchHeight(window.getContext());
        } else if (DeviceUtils.isVivo() && hasNotchInVivo(window.getContext())) {
            sNotchHeight = getOppoNotchHeight(window.getContext());
        } else if (DeviceUtils.isHuawei() && hasNotchInHuawei(window.getContext())) {
            sNotchHeight = getHuaWeiNotchHeight(window.getContext());
        } else if (DeviceUtils.isMIUI() && hasNotchInXiaomi(window.getContext())) {
            sNotchHeight = getXiaomiNotchHeight(window.getContext());
        } else if (DeviceUtils.isSumsung() && hasNotchInSamsung(window.getContext())) {
            sNotchHeight = getSamsungNotchHeight(window.getContext());
        }
        return sNotchHeight;
    }

    @RequiresApi(api = 28)
    private static int getAndroidPNotchHeight(Window window) {
        if (window.getAttributes().layoutInDisplayCutoutMode == 2) {
            return 0;
        }
        if (sAndroidPNotchHeight > -1) {
            return sAndroidPNotchHeight;
        }
        WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
        if (windowInsets == null) {
            return 0;
        }
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        if (displayCutout == null || displayCutout.getBoundingRects() == null) {
            return 0;
        }
        return displayCutout.getSafeInsetTop();
    }

    private static int getHuaWeiNotchHeight(Context context) {
        int[] ret = {0, 0};
        try {
            Class HwNotchSizeUtil = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            return ((int[]) HwNotchSizeUtil.getMethod("getNotchSize", new Class[0]).invoke(HwNotchSizeUtil, new Object[0]))[1];
        } catch (ClassNotFoundException e) {
            return ret[1];
        } catch (NoSuchMethodException e2) {
            return ret[1];
        } catch (Exception e3) {
            return ret[1];
        } catch (Throwable th) {
            return ret[1];
        }
    }

    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", FlyForbidProtocol.PLATFORM_NAME_FOR_JNI);
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private static void setHuaWeiFullScreen(Window window) {
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        try {
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Object layoutParamsExObj = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class).newInstance(layoutParams);
            layoutParamsExCls.getMethod("addHwFlags", Integer.TYPE).invoke(layoutParamsExObj, 65536);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            DJILog.e(TAG, "hua wei add notch screen flag api error " + e, new Object[0]);
        } catch (Exception e2) {
            DJILog.e(TAG, "hua wei add notch screen flag exception " + e2, new Object[0]);
        }
    }

    private static void setMiuiFullScreen(Window window) {
        if (Build.VERSION.SDK_INT >= 26) {
            Class<Window> cls = Window.class;
            try {
                Method method = cls.getMethod("addExtraFlags", Integer.TYPE);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(window, 1792);
            } catch (Exception e) {
                DJILog.e(TAG, "miui add notch screen flag exception " + e, new Object[0]);
            }
        }
    }

    private static void setOppoFullScreen(Window window) {
    }

    private static void setVivoFullScreen(Window window) {
    }

    private static void setFullScreenWithSystemUi(Window window) {
        int systemUiVisibility = 0;
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags |= 1024;
        window.setAttributes(attrs);
        if (Build.VERSION.SDK_INT >= 16) {
            systemUiVisibility = 1542;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            systemUiVisibility |= 6144;
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    private static void setSamsungFullScreen(Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        try {
            Field field = lp.getClass().getField("layoutInDisplayCutoutMode");
            field.setAccessible(true);
            field.setInt(lp, 1);
            window.setAttributes(lp);
        } catch (Exception e) {
            DJILog.e(TAG, "samsung add notch screen flag exception " + e, new Object[0]);
        }
    }
}
