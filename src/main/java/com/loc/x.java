package com.loc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.drew.metadata.exif.ExifDirectoryBase;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* compiled from: DeviceInfo */
public final class x {
    static String a = "";
    static String b = "";
    public static boolean c = false;
    static String d = "";
    static boolean e = false;
    static int f = -1;
    static String g = "";
    static String h = "";
    private static String i = null;
    private static boolean j = false;
    private static String k = "";
    private static String l = "";
    private static String m = "";
    private static String n = "";
    private static String o = "";
    private static String p = "";
    private static boolean q = false;
    private static long r;
    private static int s;
    private static String t;
    private static String u = "";

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0091 A[SYNTHETIC, Splitter:B:32:0x0091] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0098 A[SYNTHETIC, Splitter:B:37:0x0098] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String A(android.content.Context r9) {
        /*
            r1 = 1
            r3 = 0
            r0 = 0
            java.lang.String r2 = "android.permission.READ_EXTERNAL_STORAGE"
            boolean r2 = com.loc.ad.a(r9, r2)     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            if (r2 == 0) goto L_0x00a6
            java.lang.String r2 = "mounted"
            java.lang.String r4 = android.os.Environment.getExternalStorageState()     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            boolean r2 = r2.equals(r4)     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            if (r2 == 0) goto L_0x00a6
            java.io.File r2 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            java.lang.String r2 = r2.getAbsolutePath()     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            r4.<init>()     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            java.lang.StringBuilder r2 = r4.append(r2)     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            java.lang.String r4 = "/.UTSystemConfig/Global/Alvin2.xml"
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            java.io.File r5 = new java.io.File     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            r5.<init>(r2)     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            org.xmlpull.v1.XmlPullParser r6 = android.util.Xml.newPullParser()     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            int r4 = r6.getEventType()     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            r2.<init>(r5)     // Catch:{ Throwable -> 0x0095, all -> 0x008d }
            java.lang.String r0 = "utf-8"
            r6.setInput(r2, r0)     // Catch:{ Throwable -> 0x00af, all -> 0x00ac }
            r0 = r3
        L_0x004e:
            if (r1 == r4) goto L_0x00a5
            switch(r4) {
                case 0: goto L_0x0053;
                case 1: goto L_0x0053;
                case 2: goto L_0x0058;
                case 3: goto L_0x008b;
                case 4: goto L_0x007f;
                default: goto L_0x0053;
            }     // Catch:{ Throwable -> 0x00af, all -> 0x00ac }
        L_0x0053:
            int r4 = r6.next()     // Catch:{ Throwable -> 0x00af, all -> 0x00ac }
            goto L_0x004e
        L_0x0058:
            int r4 = r6.getAttributeCount()     // Catch:{ Throwable -> 0x00af, all -> 0x00ac }
            if (r4 <= 0) goto L_0x0053
            int r5 = r6.getAttributeCount()     // Catch:{ Throwable -> 0x00af, all -> 0x00ac }
            r4 = r3
        L_0x0063:
            if (r4 >= r5) goto L_0x0053
            java.lang.String r7 = r6.getAttributeValue(r4)     // Catch:{ Throwable -> 0x00af, all -> 0x00ac }
            java.lang.String r8 = "UTDID2"
            boolean r8 = r8.equals(r7)     // Catch:{ Throwable -> 0x00af, all -> 0x00ac }
            if (r8 != 0) goto L_0x007b
            java.lang.String r8 = "UTDID"
            boolean r7 = r8.equals(r7)     // Catch:{ Throwable -> 0x00af, all -> 0x00ac }
            if (r7 == 0) goto L_0x007c
        L_0x007b:
            r0 = r1
        L_0x007c:
            int r4 = r4 + 1
            goto L_0x0063
        L_0x007f:
            if (r0 == 0) goto L_0x0053
            java.lang.String r0 = r6.getText()     // Catch:{ Throwable -> 0x00af, all -> 0x00ac }
            if (r2 == 0) goto L_0x008a
            r2.close()     // Catch:{ Throwable -> 0x00a3 }
        L_0x008a:
            return r0
        L_0x008b:
            r0 = r3
            goto L_0x0053
        L_0x008d:
            r1 = move-exception
            r2 = r0
        L_0x008f:
            if (r2 == 0) goto L_0x0094
            r2.close()     // Catch:{ Throwable -> 0x00a1 }
        L_0x0094:
            throw r1
        L_0x0095:
            r1 = move-exception
        L_0x0096:
            if (r0 == 0) goto L_0x009b
            r0.close()     // Catch:{ Throwable -> 0x009f }
        L_0x009b:
            java.lang.String r0 = ""
            goto L_0x008a
        L_0x009f:
            r0 = move-exception
            goto L_0x009b
        L_0x00a1:
            r0 = move-exception
            goto L_0x0094
        L_0x00a3:
            r1 = move-exception
            goto L_0x008a
        L_0x00a5:
            r0 = r2
        L_0x00a6:
            if (r0 == 0) goto L_0x009b
            r0.close()     // Catch:{ Throwable -> 0x009f }
            goto L_0x009b
        L_0x00ac:
            r0 = move-exception
            r1 = r0
            goto L_0x008f
        L_0x00af:
            r0 = move-exception
            r0 = r2
            goto L_0x0096
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.x.A(android.content.Context):java.lang.String");
    }

    private static String B(Context context) throws InvocationTargetException, IllegalAccessException {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        if (u != null && !"".equals(u)) {
            return u;
        }
        if (!b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            return u;
        }
        TelephonyManager G = G(context);
        if (G == null) {
            return "";
        }
        Method a2 = ad.a(G.getClass(), "UZ2V0U3Vic2NyaWJlcklk", new Class[0]);
        if (a2 != null) {
            u = (String) a2.invoke(G, new Object[0]);
        }
        if (u == null) {
            u = "";
        }
        return u;
    }

    private static String C(Context context) {
        if (!b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            return null;
        }
        TelephonyManager G = G(context);
        if (G == null) {
            return "";
        }
        String simOperatorName = G.getSimOperatorName();
        return TextUtils.isEmpty(simOperatorName) ? G.getNetworkOperatorName() : simOperatorName;
    }

    private static int D(Context context) {
        ConnectivityManager E;
        NetworkInfo activeNetworkInfo;
        if (context == null || !b(context, ad.c("AYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19ORVRXT1JLX1NUQVRF")) || (E = E(context)) == null || (activeNetworkInfo = E.getActiveNetworkInfo()) == null) {
            return -1;
        }
        return activeNetworkInfo.getType();
    }

    private static ConnectivityManager E(Context context) {
        return (ConnectivityManager) context.getSystemService("connectivity");
    }

    private static int F(Context context) {
        TelephonyManager G;
        if (b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU=")) && (G = G(context)) != null) {
            return G.getNetworkType();
        }
        return -1;
    }

    private static TelephonyManager G(Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    public static String a() {
        return i;
    }

    public static String a(final Context context) {
        if (!TextUtils.isEmpty(b)) {
            return b;
        }
        String a2 = a.a(context);
        if (!TextUtils.isEmpty(a2)) {
            b = a2;
            return a2;
        }
        if (!j) {
            j = true;
            aq.d().submit(new Runnable() {
                /* class com.loc.x.AnonymousClass1 */

                public final void run() {
                    Map<String, String> a2 = a.a();
                    if (a2 != null) {
                        String a3 = a.a(x.g(context), x.u(context), x.l(context), x.x(context));
                        if (!TextUtils.isEmpty(a3)) {
                            byte[] bArr = new byte[0];
                            try {
                                bg.a();
                                bArr = bg.a(new bf(a3.getBytes(), a2));
                            } catch (t e) {
                                e.printStackTrace();
                            }
                            a.a(context, new String(bArr));
                        }
                    }
                }
            });
        }
        return "";
    }

    public static String a(Context context, String str) {
        int i2 = 0;
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        try {
            if (Build.VERSION.SDK_INT < 21) {
                return "";
            }
            if (!TextUtils.isEmpty(g)) {
                return g;
            }
            TelephonyManager G = G(context);
            if (f == -1) {
                Method a2 = ad.a(TelephonyManager.class, "UZ2V0UGhvbmVDb3VudA=", new Class[0]);
                if (a2 != null) {
                    f = ((Integer) a2.invoke(G, new Object[0])).intValue();
                } else {
                    f = 0;
                }
            }
            Class<TelephonyManager> cls = TelephonyManager.class;
            Method a3 = ad.a(cls, "MZ2V0SW1laQ=", Integer.TYPE);
            if (a3 == null) {
                f = 0;
                return "";
            }
            StringBuilder sb = new StringBuilder();
            while (i2 < f) {
                try {
                    sb.append((String) a3.invoke(G, Integer.valueOf(i2))).append(str);
                    i2++;
                } catch (Throwable th) {
                }
            }
            String sb2 = sb.toString();
            if (sb2.length() == 0) {
                f = 0;
                return "";
            }
            String substring = sb2.substring(0, sb2.length() - 1);
            g = substring;
            return substring;
        } catch (Throwable th2) {
            return "";
        }
    }

    private static List<ScanResult> a(List<ScanResult> list) {
        int size = list.size();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= size - 1) {
                return list;
            }
            int i4 = 1;
            while (true) {
                int i5 = i4;
                if (i5 >= size - i3) {
                    break;
                }
                if (list.get(i5 - 1).level > list.get(i5).level) {
                    list.set(i5 - 1, list.get(i5));
                    list.set(i5, list.get(i5 - 1));
                }
                i4 = i5 + 1;
            }
            i2 = i3 + 1;
        }
    }

    public static void a(String str) {
        i = str;
    }

    public static String b() {
        if (!TextUtils.isEmpty(d)) {
            return d;
        }
        String b2 = a.b();
        d = b2;
        return b2;
    }

    public static String b(Context context) {
        try {
            return C(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    private static boolean b(Context context, String str) {
        return context != null && context.checkCallingOrSelfPermission(str) == 0;
    }

    public static String c(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        try {
            String x = x(context);
            return (x == null || x.length() < 5) ? "" : x.substring(3, 5);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static void c() {
        try {
            if (Build.VERSION.SDK_INT > 14) {
                TrafficStats.class.getDeclaredMethod("setThreadStatsTag", Integer.TYPE).invoke(null, Integer.valueOf((int) ExifDirectoryBase.TAG_RELATED_SOUND_FILE));
            }
        } catch (Throwable th) {
        }
    }

    public static int d(Context context) {
        try {
            return F(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return -1;
        }
    }

    public static long d() {
        long blockCount;
        long blockSize;
        if (r != 0) {
            return r;
        }
        try {
            StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            StatFs statFs2 = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (Build.VERSION.SDK_INT >= 18) {
                blockCount = (statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / 1048576;
                blockSize = (statFs2.getBlockSizeLong() * statFs2.getBlockCountLong()) / 1048576;
            } else {
                blockCount = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / 1048576;
                blockSize = (((long) statFs2.getBlockSize()) * ((long) statFs2.getBlockCount())) / 1048576;
            }
            r = blockSize + blockCount;
        } catch (Throwable th) {
        }
        return r;
    }

    public static int e(Context context) {
        try {
            return D(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return -1;
        }
    }

    public static String e() {
        if (!TextUtils.isEmpty(t)) {
            return t;
        }
        String property = System.getProperty("os.arch");
        t = property;
        return property;
    }

    private static String f() {
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    byte[] bArr = null;
                    if (Build.VERSION.SDK_INT >= 9) {
                        bArr = networkInterface.getHardwareAddress();
                    }
                    if (bArr == null) {
                        return "";
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte b2 : bArr) {
                        String upperCase = Integer.toHexString(b2 & 255).toUpperCase();
                        if (upperCase.length() == 1) {
                            sb.append("0");
                        }
                        sb.append(upperCase).append(":");
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e2) {
        }
        return "";
    }

    public static String f(Context context) {
        try {
            return B(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static String g(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        try {
            if (a != null && !"".equals(a)) {
                return a;
            }
            if (b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLldSSVRFX1NFVFRJTkdT"))) {
                a = Settings.System.getString(context.getContentResolver(), "mqBRboGZkQPcAkyk");
            }
            if (a != null && !"".equals(a)) {
                return a;
            }
            try {
                a = A(context);
            } catch (Throwable th) {
            }
            return a == null ? "" : a;
        } catch (Throwable th2) {
        }
    }

    public static String h(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        if (!TextUtils.isEmpty(l)) {
            return l;
        }
        if (!b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            return "";
        }
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                return (String) ad.a(Build.class, "MZ2V0U2VyaWFs", new Class[0]).invoke(Build.class, new Object[0]);
            }
            if (Build.VERSION.SDK_INT >= 9) {
                l = Build.SERIAL;
            }
            return l == null ? "" : l;
        } catch (Throwable th) {
        }
    }

    public static String i(Context context) {
        if (!TextUtils.isEmpty(k)) {
            return k;
        }
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), ad.c(new String(al.a(13))));
            k = string;
            return string == null ? "" : k;
        } catch (Throwable th) {
            return k;
        }
    }

    static String j(Context context) {
        String str;
        WifiManager wifiManager;
        if (context == null) {
            return "";
        }
        try {
            if (!b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19XSUZJX1NUQVRF")) || (wifiManager = (WifiManager) context.getSystemService("wifi")) == null) {
                return "";
            }
            if (wifiManager.isWifiEnabled()) {
                str = wifiManager.getConnectionInfo().getBSSID();
                return str;
            }
            str = "";
            return str;
        } catch (Throwable th) {
        }
    }

    static String k(Context context) {
        StringBuilder sb = new StringBuilder();
        if (context != null) {
            try {
                if (b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19XSUZJX1NUQVRF"))) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                    if (wifiManager == null) {
                        return "";
                    }
                    if (wifiManager.isWifiEnabled()) {
                        List<ScanResult> scanResults = wifiManager.getScanResults();
                        if (scanResults == null || scanResults.size() == 0) {
                            return sb.toString();
                        }
                        List<ScanResult> a2 = a(scanResults);
                        boolean z = true;
                        int i2 = 0;
                        while (i2 < a2.size() && i2 < 7) {
                            ScanResult scanResult = a2.get(i2);
                            if (z) {
                                z = false;
                            } else {
                                sb.append(";");
                            }
                            sb.append(scanResult.BSSID);
                            i2++;
                        }
                    }
                    return sb.toString();
                }
            } catch (Throwable th) {
            }
        }
        return sb.toString();
    }

    public static String l(Context context) {
        try {
            if (m != null && !"".equals(m)) {
                return m;
            }
            if (!b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19XSUZJX1NUQVRF"))) {
                return m;
            }
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager == null) {
                return "";
            }
            m = wifiManager.getConnectionInfo().getMacAddress();
            if (ad.c("YMDI6MDA6MDA6MDA6MDA6MDA").equals(m) || ad.c("YMDA6MDA6MDA6MDA6MDA6MDA").equals(m)) {
                m = f();
            }
            return m;
        } catch (Throwable th) {
        }
    }

    static String[] m(Context context) {
        try {
            if (!b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU=")) || !b(context, ad.c("EYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19DT0FSU0VfTE9DQVRJT04="))) {
                return new String[]{"", ""};
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager == null) {
                return new String[]{"", ""};
            }
            CellLocation cellLocation = telephonyManager.getCellLocation();
            if (cellLocation instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                return new String[]{gsmCellLocation.getLac() + "||" + gsmCellLocation.getCid(), "gsm"};
            }
            if (cellLocation instanceof CdmaCellLocation) {
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                int systemId = cdmaCellLocation.getSystemId();
                int networkId = cdmaCellLocation.getNetworkId();
                return new String[]{systemId + "||" + networkId + "||" + cdmaCellLocation.getBaseStationId(), "cdma"};
            }
            return new String[]{"", ""};
        } catch (Throwable th) {
        }
    }

    static String n(Context context) {
        try {
            TelephonyManager G = G(context);
            if (G == null) {
                return "";
            }
            String networkOperator = G.getNetworkOperator();
            return (TextUtils.isEmpty(networkOperator) || networkOperator.length() < 3) ? "" : networkOperator.substring(0, 3);
        } catch (Throwable th) {
            return "";
        }
    }

    static String o(Context context) {
        try {
            TelephonyManager G = G(context);
            if (G == null) {
                return "";
            }
            String networkOperator = G.getNetworkOperator();
            return (TextUtils.isEmpty(networkOperator) || networkOperator.length() < 3) ? "" : networkOperator.substring(3);
        } catch (Throwable th) {
            return "";
        }
    }

    public static int p(Context context) {
        try {
            return F(context);
        } catch (Throwable th) {
            return -1;
        }
    }

    public static int q(Context context) {
        try {
            return D(context);
        } catch (Throwable th) {
            return -1;
        }
    }

    public static NetworkInfo r(Context context) {
        ConnectivityManager E;
        if (b(context, ad.c("AYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19ORVRXT1JLX1NUQVRF")) && (E = E(context)) != null) {
            return E.getActiveNetworkInfo();
        }
        return null;
    }

    static String s(Context context) {
        try {
            NetworkInfo r2 = r(context);
            if (r2 == null) {
                return null;
            }
            return r2.getExtraInfo();
        } catch (Throwable th) {
            return null;
        }
    }

    static String t(Context context) {
        try {
            if (n != null && !"".equals(n)) {
                return n;
            }
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            if (windowManager == null) {
                return "";
            }
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int i2 = displayMetrics.widthPixels;
            int i3 = displayMetrics.heightPixels;
            n = i3 > i2 ? i2 + "*" + i3 : i3 + "*" + i2;
            return n;
        } catch (Throwable th) {
        }
    }

    public static String u(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                return "";
            }
            if (o != null && !"".equals(o)) {
                return o;
            }
            if (!b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
                return o;
            }
            TelephonyManager G = G(context);
            if (G == null) {
                return "";
            }
            Method a2 = ad.a(G.getClass(), "QZ2V0RGV2aWNlSWQ", new Class[0]);
            if (Build.VERSION.SDK_INT >= 26) {
                a2 = ad.a(G.getClass(), "QZ2V0SW1laQ==", new Class[0]);
            }
            if (a2 != null) {
                o = (String) a2.invoke(G, new Object[0]);
            }
            if (o == null) {
                o = "";
            }
            return o;
        } catch (Throwable th) {
        }
    }

    public static String v(Context context) {
        return u(context) + "#" + a(context);
    }

    public static String w(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        try {
            if (p != null && !"".equals(p)) {
                return p;
            }
            if (!b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
                return p;
            }
            TelephonyManager G = G(context);
            if (G == null) {
                return "";
            }
            if (Build.VERSION.SDK_INT >= 26) {
                Method a2 = ad.a(G.getClass(), "QZ2V0TWVpZA==", new Class[0]);
                if (a2 != null) {
                    p = (String) a2.invoke(G, new Object[0]);
                }
                if (p == null) {
                    p = "";
                }
            }
            return p;
        } catch (Throwable th) {
        }
    }

    public static String x(Context context) {
        try {
            return B(context);
        } catch (Throwable th) {
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0069 A[SYNTHETIC, Splitter:B:26:0x0069] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int y(android.content.Context r5) {
        /*
            r1 = 0
            int r0 = com.loc.x.s
            if (r0 == 0) goto L_0x0008
            int r1 = com.loc.x.s
        L_0x0007:
            return r1
        L_0x0008:
            int r0 = android.os.Build.VERSION.SDK_INT
            r2 = 16
            if (r0 < r2) goto L_0x002c
            java.lang.String r0 = "activity"
            java.lang.Object r0 = r5.getSystemService(r0)
            android.app.ActivityManager r0 = (android.app.ActivityManager) r0
            if (r0 == 0) goto L_0x0007
            android.app.ActivityManager$MemoryInfo r1 = new android.app.ActivityManager$MemoryInfo
            r1.<init>()
            r0.getMemoryInfo(r1)
            long r0 = r1.totalMem
            r2 = 1024(0x400, double:5.06E-321)
            long r0 = r0 / r2
            int r0 = (int) r0
        L_0x0027:
            int r1 = r0 / 1024
            com.loc.x.s = r1
            goto L_0x0007
        L_0x002c:
            r0 = 0
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            java.lang.String r2 = "/proc/meminfo"
            r3.<init>(r2)     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            java.io.FileReader r4 = new java.io.FileReader     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            r4.<init>(r3)     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            r2.<init>(r4)     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            java.lang.String r0 = r2.readLine()     // Catch:{ Throwable -> 0x0074, all -> 0x0071 }
            java.lang.String r3 = "\\s+"
            java.lang.String[] r0 = r0.split(r3)     // Catch:{ Throwable -> 0x0074, all -> 0x0071 }
            r3 = 1
            r0 = r0[r3]     // Catch:{ Throwable -> 0x0074, all -> 0x0071 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Throwable -> 0x0074, all -> 0x0071 }
            int r0 = r0.intValue()     // Catch:{ Throwable -> 0x0074, all -> 0x0071 }
            if (r2 == 0) goto L_0x0027
            r2.close()     // Catch:{ IOException -> 0x005b }
            goto L_0x0027
        L_0x005b:
            r1 = move-exception
            goto L_0x0027
        L_0x005d:
            r2 = move-exception
        L_0x005e:
            if (r0 == 0) goto L_0x0063
            r0.close()     // Catch:{ IOException -> 0x006d }
        L_0x0063:
            r0 = r1
            goto L_0x0027
        L_0x0065:
            r1 = move-exception
            r2 = r0
        L_0x0067:
            if (r2 == 0) goto L_0x006c
            r2.close()     // Catch:{ IOException -> 0x006f }
        L_0x006c:
            throw r1
        L_0x006d:
            r0 = move-exception
            goto L_0x0063
        L_0x006f:
            r0 = move-exception
            goto L_0x006c
        L_0x0071:
            r0 = move-exception
            r1 = r0
            goto L_0x0067
        L_0x0074:
            r0 = move-exception
            r0 = r2
            goto L_0x005e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.x.y(android.content.Context):int");
    }

    static String z(Context context) {
        try {
            return C(context);
        } catch (Throwable th) {
            return "";
        }
    }
}
