package dji.midware.data.manager.Dpad;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;

@EXClassNullAway
public class DJISysPropManager {
    private static final String DEBUG_VSYNC = "sys.video.debugvsync";
    private static final String FPV_BROADCAST = "prop.dji.fpv.broadcast";
    public static final int MIN_SDK_VER = 17;
    private static final String PERSIST_BOARD = "persist.board.version";
    private static final String PROP_CAMERA_FPS = "dji.camera.fps";
    private static final String PROP_FPS = "prop.dji.fps";
    private static final String PROP_FPV = "prop.dji.fpv";
    public static final String SETTINGS_GO_3 = "dji_fps_go3_enable";
    public static final String SETTINGS_GO_4 = "dji_fps_go4_enable";
    private static final String TAG = "DJISysPropManager";
    private static final String VALUE_DISABLE = "0";
    private static final String VALUE_ENABLE = "1";
    private static final String VALUE_FPS_60 = "60";

    public static void setFpvBroadcastState(String state) {
        setProp(FPV_BROADCAST, state);
    }

    public static int getBoardVer() {
        return Integer.parseInt(getProp(PERSIST_BOARD, "0"));
    }

    public static boolean getEnableGo4Fps() {
        return getEnableGo4Fps(ServiceManager.getContext());
    }

    @TargetApi(17)
    private static boolean getEnableGo4Fps(Context context) {
        if (Build.VERSION.SDK_INT < 17) {
            return false;
        }
        return "1".equals(Settings.Global.getString(context.getContentResolver(), SETTINGS_GO_4));
    }

    public static void setFpv(int value) {
        setProp(PROP_FPV, "" + value);
    }

    public static void setFps(int value) {
        setProp(PROP_FPS, "" + value);
    }

    public static void setCamFps(int value) {
        setProp(PROP_CAMERA_FPS, "" + value);
    }

    public static int getFps() {
        return Integer.parseInt(getProp(PROP_FPS, VALUE_FPS_60));
    }

    private static String getProp(String key, String defaultValue) {
        LOGE("getProp( key = " + key + ", defaultValue=" + defaultValue + ")");
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            value = (String) c.getMethod("get", String.class, String.class).invoke(c, key, defaultValue);
        } catch (Exception e) {
            LOGE("getProp bus-error, " + e.getMessage());
        }
        LOGE("getProp " + key + " = " + value);
        return value;
    }

    private static boolean setProp(String key, String newValue) {
        LOGE("setProp( key = " + key + ", newValue=" + newValue + ")");
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            c.getMethod("set", String.class, String.class).invoke(c, key, newValue);
        } catch (Exception e) {
            LOGE("set property error, " + e.getMessage());
        }
        String checkValue = getProp(key, "");
        if (checkValue == null || checkValue.equals("")) {
            LOGE("bus-error, checkValue = " + checkValue);
            return false;
        }
        LOGE("setProp onSuccess, newValue = " + checkValue);
        return true;
    }

    private static void LOGE(String log) {
    }
}
