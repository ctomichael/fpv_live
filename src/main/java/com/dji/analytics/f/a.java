package com.dji.analytics.f;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import com.dji.analytics.DJIA;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/* compiled from: CollectDevicesInfo */
public class a {
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(android.content.Context r6) {
        /*
            java.lang.String r2 = ""
            java.lang.String r1 = ""
            java.lang.String r0 = "wifi"
            java.lang.Object r0 = r6.getSystemService(r0)     // Catch:{ Exception -> 0x0086 }
            android.net.wifi.WifiManager r0 = (android.net.wifi.WifiManager) r0     // Catch:{ Exception -> 0x0086 }
            android.net.wifi.WifiInfo r0 = r0.getConnectionInfo()     // Catch:{ Exception -> 0x0086 }
            java.lang.String r0 = r0.getMacAddress()     // Catch:{ Exception -> 0x0086 }
            if (r0 == 0) goto L_0x006d
            java.lang.String r2 = ":"
            java.lang.String r3 = ""
            java.lang.String r0 = r0.replace(r2, r3)     // Catch:{ Exception -> 0x00a1 }
        L_0x0023:
            java.lang.String r0 = b(r0)     // Catch:{ Exception -> 0x00a1 }
            r2 = r0
        L_0x0028:
            java.lang.String r0 = android.os.Build.SERIAL     // Catch:{ Exception -> 0x0094 }
        L_0x002a:
            boolean r1 = com.dji.analytics.DJIA.DEV_FLAG
            if (r1 == 0) goto L_0x0054
            com.dji.analytics.c.b r1 = com.dji.analytics.DJIA.log
            java.lang.String r3 = com.dji.analytics.DJIA.LOG_TAG
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Mac address is: "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r2)
            java.lang.String r5 = "build serial : "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r4 = r4.toString()
            r1.a(r3, r4)
        L_0x0054:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = "_"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r0 = r0.toString()
            return r0
        L_0x006d:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a1 }
            r2.<init>()     // Catch:{ Exception -> 0x00a1 }
            java.lang.String r3 = ""
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x00a1 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x00a1 }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Exception -> 0x00a1 }
            java.lang.String r0 = r2.toString()     // Catch:{ Exception -> 0x00a1 }
            goto L_0x0023
        L_0x0086:
            r0 = move-exception
            r0 = r2
        L_0x0088:
            com.dji.analytics.c.b r2 = com.dji.analytics.DJIA.log
            java.lang.String r3 = com.dji.analytics.DJIA.LOG_TAG
            java.lang.String r4 = "get mac error: "
            r2.b(r3, r4)
            r2 = r0
            goto L_0x0028
        L_0x0094:
            r0 = move-exception
            com.dji.analytics.c.b r0 = com.dji.analytics.DJIA.log
            java.lang.String r3 = com.dji.analytics.DJIA.LOG_TAG
            java.lang.String r4 = "get serial error: "
            r0.b(r3, r4)
            r0 = r1
            goto L_0x002a
        L_0x00a1:
            r2 = move-exception
            goto L_0x0088
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.analytics.f.a.a(android.content.Context):java.lang.String");
    }

    public static String b(Context context) {
        try {
            String str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            if (str != null && str.length() >= 0) {
                return str;
            }
        } catch (Exception e) {
            DJIA.log.b(DJIA.LOG_TAG, "Get app version fail.");
        }
        return "0.0.0";
    }

    public static String a() {
        return Build.BRAND;
    }

    public static String b() {
        return Build.MODEL;
    }

    public static String c(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.x + "*" + point.y;
    }

    public static int c() {
        return 2;
    }

    public static String d() {
        return Build.VERSION.RELEASE;
    }

    public static String e() {
        return Locale.getDefault().getLanguage();
    }

    public static String f() {
        return Locale.getDefault().getCountry();
    }

    public static String g() {
        return "GooglePlay";
    }

    public static byte[] a(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(str.getBytes("UTF-8"));
            return instance.digest();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String b(String str) {
        byte[] a = a(str);
        if (a == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : a) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                stringBuffer.append('0');
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString();
    }
}
