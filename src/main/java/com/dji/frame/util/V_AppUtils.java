package com.dji.frame.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import com.dji.frame.common.V_SoundPool;
import dji.thirdparty.afinal.FinalBitmap;
import dji.thirdparty.afinal.FinalDb;
import dji.thirdparty.afinal.FinalHttp;
import dji.thirdparty.afinal.utils.HttpsHelper;
import java.io.File;

public class V_AppUtils {
    private static FinalDb.DbUpdateListener dbUpdateListener;
    private static int dbVersion;
    static FinalBitmap finalBitmap;
    static FinalDb finalDb;
    static FinalHttp finalHttpWithOutSSL;
    static FinalHttp finalHttpWithSSL;
    @SuppressLint({"InlinedApi"})
    private static int flag = 5634;
    private static boolean isdebugDb = false;
    private static boolean mIsEnable = true;
    private static StrictMode.ThreadPolicy policy;
    public static boolean sSSLSwitch = true;
    static V_SoundPool soundPool;
    private static StrictMode.VmPolicy vmPolicy;

    public enum DJI_SYS_UI_EVENT {
        HIDE,
        HIDE_DELAY,
        SHOW
    }

    public static void setSslSwitch(boolean sslSwitch) {
        sSSLSwitch = sslSwitch;
    }

    public static synchronized FinalHttp getFinalHttp(Context context) {
        FinalHttp finalHttpWithOutSSL2;
        synchronized (V_AppUtils.class) {
            if (sSSLSwitch) {
                finalHttpWithOutSSL2 = getFinalHttpWithSSL(context);
            } else {
                finalHttpWithOutSSL2 = getFinalHttpWithOutSSL(context);
            }
        }
        return finalHttpWithOutSSL2;
    }

    public static synchronized FinalHttp getFinalHttp() {
        FinalHttp finalHttpWithOutSSL2;
        synchronized (V_AppUtils.class) {
            if (sSSLSwitch) {
                finalHttpWithOutSSL2 = getFinalHttpWithSSL();
            } else {
                finalHttpWithOutSSL2 = getFinalHttpWithOutSSL();
            }
        }
        return finalHttpWithOutSSL2;
    }

    public static synchronized FinalBitmap getFinalBitmap(Context context) {
        FinalBitmap finalBitmap2;
        synchronized (V_AppUtils.class) {
            if (finalBitmap == null) {
                finalBitmap = FinalBitmap.create(context.getApplicationContext());
            }
            finalBitmap2 = finalBitmap;
        }
        return finalBitmap2;
    }

    @Deprecated
    public static synchronized FinalHttp getFinalHttpWithSSL(Context context) {
        FinalHttp finalHttp;
        synchronized (V_AppUtils.class) {
            if (finalHttpWithSSL == null) {
                finalHttpWithSSL = new FinalHttp();
                finalHttpWithSSL.configSSLSocketFactory(HttpsHelper.getDJISSLSocketFactoryForApache());
            }
            finalHttp = finalHttpWithSSL;
        }
        return finalHttp;
    }

    @Deprecated
    public static synchronized FinalHttp getFinalHttpWithOutSSL(Context context) {
        FinalHttp finalHttp;
        synchronized (V_AppUtils.class) {
            if (finalHttpWithOutSSL == null) {
                finalHttpWithOutSSL = new FinalHttp();
            }
            finalHttp = finalHttpWithOutSSL;
        }
        return finalHttp;
    }

    @Deprecated
    public static synchronized FinalHttp getFinalHttpWithSSL() {
        FinalHttp finalHttp;
        synchronized (V_AppUtils.class) {
            if (finalHttpWithSSL == null) {
                finalHttpWithSSL = new FinalHttp();
                finalHttpWithSSL.configSSLSocketFactory(HttpsHelper.getDJISSLSocketFactoryForApache());
            }
            finalHttp = finalHttpWithSSL;
        }
        return finalHttp;
    }

    public static synchronized FinalHttp getFinalHttpWithOutSSL() {
        FinalHttp finalHttp;
        synchronized (V_AppUtils.class) {
            if (finalHttpWithOutSSL == null) {
                finalHttpWithOutSSL = new FinalHttp();
            }
            finalHttp = finalHttpWithOutSSL;
        }
        return finalHttp;
    }

    public static void setDbVersion(int version, FinalDb.DbUpdateListener updateListener) {
        dbVersion = version;
        dbUpdateListener = updateListener;
    }

    public static void setDbVersion(int version, boolean isdebug, FinalDb.DbUpdateListener updateListener) {
        dbVersion = version;
        isdebugDb = isdebug;
        dbUpdateListener = updateListener;
    }

    public static synchronized FinalDb getFinalDb(Context context) {
        FinalDb finalDb2;
        synchronized (V_AppUtils.class) {
            if (finalDb == null) {
                File file = new File(V_DiskUtil.getExternalCacheDirPath(context, "databases/"));
                if (!file.exists()) {
                    file.mkdirs();
                }
                Context applicationContext = context.getApplicationContext();
                if (isdebugDb) {
                }
                finalDb = FinalDb.create(applicationContext, null, "dji.db", false, dbVersion, dbUpdateListener);
            }
            finalDb2 = finalDb;
        }
        return finalDb2;
    }

    public static synchronized V_SoundPool getV_SoundPool(Context context) {
        V_SoundPool v_SoundPool;
        synchronized (V_AppUtils.class) {
            if (soundPool == null) {
                soundPool = new V_SoundPool(context.getApplicationContext());
            }
            v_SoundPool = soundPool;
        }
        return v_SoundPool;
    }

    public static void enterToggle(boolean isEnable) {
        mIsEnable = isEnable;
    }

    public static void enter(Window window) {
        if (Build.VERSION.SDK_INT >= 19 && mIsEnable) {
            window.getDecorView().setSystemUiVisibility(flag);
        }
    }

    public static void enter(View view) {
        if (Build.VERSION.SDK_INT >= 19 && mIsEnable) {
            view.setSystemUiVisibility(flag);
        }
    }

    public static void closeStrickMode() {
        policy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(policy).permitAll().build());
    }

    public static void openStrickMode() {
        StrictMode.setThreadPolicy(policy);
    }

    public static void closeVmStrickMode() {
        vmPolicy = StrictMode.getVmPolicy();
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(vmPolicy).detectAll().build());
    }

    public static void openVmStrickMode() {
        StrictMode.setVmPolicy(vmPolicy);
    }
}
