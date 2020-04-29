package dji.utils;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.util.Locale;
import java.util.UUID;

public class DeviceUtils {
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    public static final String KEY_MANUFACTOR_HUAWEI = "HUAWEI";
    private static final String KEY_MANUFACTOR_OPPO = "oppo";
    private static final String KEY_MANUFACTOR_SMARTISAN = "smartisan";
    private static final String KEY_MANUFACTOR_SUMSUNG = "samsung";
    private static final String KEY_MANUFACTOR_VIVO = "vivo";
    private static final String KEY_MANUFACTOR_XIAOMI = "xiaomi";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.name";

    public static boolean isTablet(Context context) {
        return (context.getApplicationContext().getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static String getManufacture() {
        return Build.MANUFACTURER.toLowerCase();
    }

    public static String getModel() {
        return Build.MODEL.toLowerCase();
    }

    public static boolean isMIUI() {
        if (!TextUtils.isEmpty(getSystemProperty(KEY_MIUI_VERSION_CODE))) {
            return true;
        }
        return false;
    }

    public static boolean isHuawei() {
        return KEY_MANUFACTOR_HUAWEI.toLowerCase().equals(getManufacture());
    }

    public static boolean isOppo() {
        return KEY_MANUFACTOR_OPPO.equals(getManufacture());
    }

    public static boolean isVivo() {
        if (!TextUtils.isEmpty(getSystemProperty("ro.vivo.os.name"))) {
            return true;
        }
        return false;
    }

    public static boolean isSumsung() {
        return KEY_MANUFACTOR_SUMSUNG.equals(getManufacture());
    }

    public static boolean isSmartisan() {
        return KEY_MANUFACTOR_SMARTISAN.equals(getManufacture());
    }

    public static int getEmuiVersion() {
        if (KEY_MANUFACTOR_HUAWEI.equals(getManufacture())) {
            return Integer.valueOf(String.valueOf(getSystemProperty(KEY_EMUI_VERSION_CODE).charAt(10))).intValue();
        }
        return -1;
    }

    public static String getAndroidID(Context context) {
        try {
            return Settings.System.getString(context.getContentResolver(), "android_id");
        } catch (Throwable th) {
            return "";
        }
    }

    public static String getUUID(Context context) {
        return UUID.randomUUID().toString();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.StringBuilder.append(java.lang.CharSequence, int, int):java.lang.StringBuilder}
     arg types: [java.lang.String, int, int]
     candidates:
      ClspMth{java.lang.StringBuilder.append(java.lang.CharSequence, int, int):java.lang.Appendable throws java.io.IOException}
      ClspMth{java.lang.StringBuilder.append(char[], int, int):java.lang.StringBuilder}
      ClspMth{java.lang.Appendable.append(java.lang.CharSequence, int, int):java.lang.Appendable throws java.io.IOException}
      ClspMth{java.lang.StringBuilder.append(java.lang.CharSequence, int, int):java.lang.StringBuilder} */
    public static String getMCC(Context context) {
        String mcc_mnc;
        if (context == null || (mcc_mnc = ((TelephonyManager) context.getSystemService("phone")).getSimOperator()) == null || mcc_mnc.length() < 3) {
            return null;
        }
        StringBuilder mcc = new StringBuilder();
        mcc.append((CharSequence) mcc_mnc, 0, 3);
        return mcc.toString();
    }

    public static String getCountry(Context context) {
        Locale locale;
        if (context == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale.getCountry();
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0047 A[SYNTHETIC, Splitter:B:17:0x0047] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0053 A[SYNTHETIC, Splitter:B:23:0x0053] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getSystemProperty(java.lang.String r9) {
        /*
            r2 = 0
            java.lang.Runtime r6 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x0040 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0040 }
            r7.<init>()     // Catch:{ IOException -> 0x0040 }
            java.lang.String r8 = "getprop "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ IOException -> 0x0040 }
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch:{ IOException -> 0x0040 }
            java.lang.String r7 = r7.toString()     // Catch:{ IOException -> 0x0040 }
            java.lang.Process r5 = r6.exec(r7)     // Catch:{ IOException -> 0x0040 }
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0040 }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0040 }
            java.io.InputStream r7 = r5.getInputStream()     // Catch:{ IOException -> 0x0040 }
            r6.<init>(r7)     // Catch:{ IOException -> 0x0040 }
            r7 = 1024(0x400, float:1.435E-42)
            r3.<init>(r6, r7)     // Catch:{ IOException -> 0x0040 }
            java.lang.String r4 = r3.readLine()     // Catch:{ IOException -> 0x005f, all -> 0x005c }
            r3.close()     // Catch:{ IOException -> 0x005f, all -> 0x005c }
            if (r3 == 0) goto L_0x0039
            r3.close()     // Catch:{ IOException -> 0x003b }
        L_0x0039:
            r2 = r3
        L_0x003a:
            return r4
        L_0x003b:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0039
        L_0x0040:
            r1 = move-exception
        L_0x0041:
            r1.printStackTrace()     // Catch:{ all -> 0x0050 }
            r4 = 0
            if (r2 == 0) goto L_0x003a
            r2.close()     // Catch:{ IOException -> 0x004b }
            goto L_0x003a
        L_0x004b:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x003a
        L_0x0050:
            r6 = move-exception
        L_0x0051:
            if (r2 == 0) goto L_0x0056
            r2.close()     // Catch:{ IOException -> 0x0057 }
        L_0x0056:
            throw r6
        L_0x0057:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0056
        L_0x005c:
            r6 = move-exception
            r2 = r3
            goto L_0x0051
        L_0x005f:
            r1 = move-exception
            r2 = r3
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.DeviceUtils.getSystemProperty(java.lang.String):java.lang.String");
    }

    public static int getBattery(Context context) {
        return ((BatteryManager) context.getSystemService("batterymanager")).getIntProperty(4);
    }

    public static boolean supportArm64ABI() {
        if (Build.SUPPORTED_64_BIT_ABIS.length <= 0) {
            return false;
        }
        String[] strArr = Build.SUPPORTED_64_BIT_ABIS;
        for (String abi : strArr) {
            if (abi != null && abi.equals("arm64-v8a")) {
                return true;
            }
        }
        return false;
    }
}
