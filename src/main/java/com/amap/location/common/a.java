package com.amap.location.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.amap.location.common.util.f;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

/* compiled from: DeviceInfo */
public class a {
    public static C0008a a;
    public static b b;
    private static volatile String c;
    private static volatile String d;
    private static volatile String e;
    private static volatile String f;
    private static volatile String g;
    private static volatile boolean h = true;

    /* renamed from: com.amap.location.common.a$a  reason: collision with other inner class name */
    /* compiled from: DeviceInfo */
    public interface C0008a {
        String a();
    }

    /* compiled from: DeviceInfo */
    public interface b {
        String a(Context context);
    }

    public static String a() {
        if (TextUtils.isEmpty(e)) {
            try {
                if (a != null) {
                    e = a.a();
                }
            } catch (Exception e2) {
            }
        }
        return e == null ? "" : e;
    }

    public static String a(Context context) {
        if (c == null && h) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService("phone");
                if (Build.VERSION.SDK_INT >= 26) {
                    c = telephonyManager.getImei();
                } else {
                    c = telephonyManager.getDeviceId();
                }
            } catch (Throwable th) {
            }
            if (c == null) {
                c = "";
            }
        }
        return c == null ? "" : c;
    }

    public static void a(Context context, String str) {
        if (!TextUtils.isEmpty(str) && !str.equals(d)) {
            d = str;
            try {
                context.getSharedPreferences("sp_common", 0).edit().putString("tid", com.amap.location.common.util.a.a(str)).apply();
            } catch (Exception e2) {
            }
        }
    }

    public static void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            e = str;
        }
    }

    public static void a(boolean z) {
        h = z;
    }

    public static String b() {
        return Build.MODEL == null ? "" : Build.MODEL;
    }

    public static String b(Context context) {
        if (d == null) {
            try {
                if (h) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("sp_common", 0);
                    String b2 = com.amap.location.common.util.a.b(sharedPreferences.getString("tid", null));
                    if (!TextUtils.isEmpty(b2) || b == null) {
                        d = b2;
                    } else {
                        String a2 = b.a(context);
                        if (!TextUtils.isEmpty(a2)) {
                            sharedPreferences.edit().putString("tid", com.amap.location.common.util.a.a(a2)).apply();
                            d = a2;
                        } else {
                            d = "";
                        }
                    }
                }
            } catch (Exception e2) {
            }
        }
        return d == null ? "" : d;
    }

    public static void b(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            e = str;
        }
    }

    public static String c() {
        return Build.MANUFACTURER == null ? "" : Build.MANUFACTURER;
    }

    public static String c(Context context) {
        if (TextUtils.isEmpty(e)) {
            try {
                if (a != null) {
                    e = a.a();
                }
            } catch (Exception e2) {
            }
        }
        return e == null ? "" : e;
    }

    public static int d() {
        return Build.VERSION.SDK_INT;
    }

    public static String d(Context context) {
        if (f == null) {
            try {
                if (h) {
                    f = ((TelephonyManager) context.getApplicationContext().getSystemService("phone")).getSubscriberId();
                }
            } catch (Exception | SecurityException e2) {
            }
            if (f == null) {
                f = "";
            }
        }
        return f == null ? "" : f;
    }

    public static long e(Context context) {
        return f.a(f(context));
    }

    private static String e() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces != null) {
                for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
                    if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                        byte[] bArr = null;
                        if (Build.VERSION.SDK_INT >= 9) {
                            bArr = networkInterface.getHardwareAddress();
                        }
                        if (bArr == null) {
                            return "";
                        }
                        StringBuilder sb = new StringBuilder();
                        int length = bArr.length;
                        for (int i = 0; i < length; i++) {
                            sb.append(String.format("%02X:", Byte.valueOf(bArr[i])));
                        }
                        if (sb.length() > 0) {
                            sb.deleteCharAt(sb.length() - 1);
                        }
                        return sb.toString();
                    }
                }
            }
        } catch (Exception e2) {
        }
        return "";
    }

    public static String f(Context context) {
        WifiInfo connectionInfo;
        if (!TextUtils.isEmpty(g)) {
            return g;
        }
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (!(wifiManager == null || (connectionInfo = wifiManager.getConnectionInfo()) == null)) {
                String macAddress = connectionInfo.getMacAddress();
                if (Build.VERSION.SDK_INT >= 23 && macAddress != null && macAddress.equals("02:00:00:00:00:00")) {
                    macAddress = e();
                }
                if (macAddress != null && macAddress.length() > 0) {
                    String replace = macAddress.replace(":", "");
                    if (replace == null || replace.length() <= 0) {
                        return replace;
                    }
                    g = replace;
                    return replace;
                }
            }
        } catch (Exception | SecurityException e2) {
        }
        return "";
    }
}
