package com.amap.openapi;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import java.lang.reflect.Method;

/* compiled from: DeviceUtil */
public class az {
    private static String a;
    private static long b = 0;
    private static boolean c;

    @NonNull
    public static String a(@NonNull Context context) {
        if (a == null) {
            a = context.getPackageName();
        }
        String a2 = bd.a(a);
        a = a2;
        return a2;
    }

    public static boolean a(Location location) {
        if (location == null) {
            return false;
        }
        try {
            Method method = Location.class.getMethod("isFromMockProvider", null);
            return method != null ? ((Boolean) method.invoke(location, null)).booleanValue() : false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean b(@NonNull Context context) {
        boolean z = false;
        if (Build.VERSION.SDK_INT >= 23) {
            return false;
        }
        if (b > 0 && SystemClock.elapsedRealtime() - b < 180000) {
            return c;
        }
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), "mock_location");
            if (string != null && !string.equals("0")) {
                z = true;
            }
        } catch (SecurityException e) {
        }
        c = z;
        b = SystemClock.elapsedRealtime();
        return z;
    }

    public static boolean c(Context context) {
        try {
            PowerManager powerManager = (PowerManager) context.getSystemService("power");
            return Build.VERSION.SDK_INT >= 20 ? powerManager.isInteractive() : powerManager.isScreenOn();
        } catch (Throwable th) {
            return false;
        }
    }
}
