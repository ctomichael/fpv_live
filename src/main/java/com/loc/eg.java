package com.loc;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

/* compiled from: WifiManagerWrapper */
public final class eg {
    static long c = 0;
    static long d = 0;
    static long e = 0;
    static long f = 0;
    static long g = 0;
    public static HashMap<String, Long> q = new HashMap<>(36);
    public static long r = 0;
    static int s = 0;
    WifiManager a;
    ArrayList<ScanResult> b = new ArrayList<>();
    Context h;
    boolean i = false;
    StringBuilder j = null;
    boolean k = true;
    boolean l = true;
    boolean m = true;
    String n = null;
    TreeMap<Integer, ScanResult> o = null;
    public boolean p = true;
    ConnectivityManager t = null;
    volatile boolean u = false;
    private volatile WifiInfo v = null;
    private long w = 30000;

    public eg(Context context, WifiManager wifiManager) {
        this.a = wifiManager;
        this.h = context;
    }

    public static long a() {
        return ((fa.c() - r) / 1000) + 1;
    }

    private static boolean a(int i2) {
        int i3 = 20;
        try {
            i3 = WifiManager.calculateSignalLevel(i2, 20);
        } catch (ArithmeticException e2) {
            es.a(e2, "Aps", "wifiSigFine");
        }
        return i3 > 0;
    }

    public static boolean a(WifiInfo wifiInfo) {
        return wifiInfo != null && !TextUtils.isEmpty(wifiInfo.getSSID()) && fa.a(wifiInfo.getBSSID());
    }

    public static String k() {
        return String.valueOf(fa.c() - f);
    }

    private List<ScanResult> l() {
        if (this.a != null) {
            try {
                List<ScanResult> scanResults = this.a.getScanResults();
                if (Build.VERSION.SDK_INT >= 17) {
                    HashMap<String, Long> hashMap = new HashMap<>(36);
                    for (ScanResult scanResult : scanResults) {
                        hashMap.put(scanResult.BSSID, Long.valueOf(scanResult.timestamp));
                    }
                    if (q.isEmpty() || !q.equals(hashMap)) {
                        q = hashMap;
                        r = fa.c();
                    }
                } else {
                    r = fa.c();
                }
                this.n = null;
                return scanResults;
            } catch (SecurityException e2) {
                this.n = e2.getMessage();
            } catch (Throwable th) {
                this.n = null;
                es.a(th, "WifiManagerWrapper", "getScanResults");
            }
        }
        return null;
    }

    private WifiInfo m() {
        try {
            if (this.a != null) {
                return this.a.getConnectionInfo();
            }
        } catch (Throwable th) {
            es.a(th, "WifiManagerWrapper", "getConnectionInfo");
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004a, code lost:
        if (r4 < r0) goto L_0x007f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0069 A[Catch:{ Throwable -> 0x0081 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A[Catch:{ Throwable -> 0x0081 }, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void n() {
        /*
            r8 = this;
            r2 = 30000(0x7530, double:1.4822E-319)
            boolean r0 = r8.o()
            if (r0 == 0) goto L_0x006f
            long r0 = com.loc.fa.c()     // Catch:{ Throwable -> 0x0081 }
            long r4 = com.loc.eg.c     // Catch:{ Throwable -> 0x0081 }
            long r4 = r0 - r4
            r0 = 4900(0x1324, double:2.421E-320)
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x007f
            android.net.ConnectivityManager r0 = r8.t     // Catch:{ Throwable -> 0x0081 }
            if (r0 != 0) goto L_0x0027
            android.content.Context r0 = r8.h     // Catch:{ Throwable -> 0x0081 }
            java.lang.String r1 = "connectivity"
            java.lang.Object r0 = com.loc.fa.a(r0, r1)     // Catch:{ Throwable -> 0x0081 }
            android.net.ConnectivityManager r0 = (android.net.ConnectivityManager) r0     // Catch:{ Throwable -> 0x0081 }
            r8.t = r0     // Catch:{ Throwable -> 0x0081 }
        L_0x0027:
            android.net.ConnectivityManager r0 = r8.t     // Catch:{ Throwable -> 0x0081 }
            boolean r0 = r8.a(r0)     // Catch:{ Throwable -> 0x0081 }
            if (r0 == 0) goto L_0x0035
            r0 = 9900(0x26ac, double:4.8912E-320)
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x007f
        L_0x0035:
            int r0 = com.loc.eg.s     // Catch:{ Throwable -> 0x0081 }
            r1 = 1
            if (r0 <= r1) goto L_0x004c
            long r0 = r8.w     // Catch:{ Throwable -> 0x0081 }
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 == 0) goto L_0x0070
            long r0 = r8.w     // Catch:{ Throwable -> 0x0081 }
        L_0x0042:
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0081 }
            r3 = 28
            if (r2 < r3) goto L_0x004c
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x007f
        L_0x004c:
            android.net.wifi.WifiManager r0 = r8.a     // Catch:{ Throwable -> 0x0081 }
            if (r0 == 0) goto L_0x007f
            long r0 = com.loc.fa.c()     // Catch:{ Throwable -> 0x0081 }
            com.loc.eg.c = r0     // Catch:{ Throwable -> 0x0081 }
            int r0 = com.loc.eg.s     // Catch:{ Throwable -> 0x0081 }
            r1 = 2
            if (r0 >= r1) goto L_0x0061
            int r0 = com.loc.eg.s     // Catch:{ Throwable -> 0x0081 }
            int r0 = r0 + 1
            com.loc.eg.s = r0     // Catch:{ Throwable -> 0x0081 }
        L_0x0061:
            android.net.wifi.WifiManager r0 = r8.a     // Catch:{ Throwable -> 0x0081 }
            boolean r0 = r0.startScan()     // Catch:{ Throwable -> 0x0081 }
        L_0x0067:
            if (r0 == 0) goto L_0x006f
            long r0 = com.loc.fa.c()     // Catch:{ Throwable -> 0x0081 }
            com.loc.eg.e = r0     // Catch:{ Throwable -> 0x0081 }
        L_0x006f:
            return
        L_0x0070:
            long r0 = com.loc.er.B()     // Catch:{ Throwable -> 0x0081 }
            r6 = -1
            int r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r0 == 0) goto L_0x008c
            long r0 = com.loc.er.B()     // Catch:{ Throwable -> 0x0081 }
            goto L_0x0042
        L_0x007f:
            r0 = 0
            goto L_0x0067
        L_0x0081:
            r0 = move-exception
            java.lang.String r1 = "WifiManager"
            java.lang.String r2 = "wifiScan"
            com.loc.es.a(r0, r1, r2)
            goto L_0x006f
        L_0x008c:
            r0 = r2
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eg.n():void");
    }

    private boolean o() {
        this.p = this.a == null ? false : fa.h(this.h);
        if (!this.p || !this.k) {
            return false;
        }
        if (e == 0) {
            return true;
        }
        if (fa.c() - e < 4900 || fa.c() - f < 1500) {
            return false;
        }
        return fa.c() - f > 4900 ? true : true;
    }

    public final void a(boolean z) {
        Context context = this.h;
        if (er.A() && this.m && this.a != null && context != null && z && fa.d() > 17) {
            ContentResolver contentResolver = context.getContentResolver();
            try {
                if (((Integer) ew.a("android.provider.Settings$Global", "getInt", new Object[]{contentResolver, "wifi_scan_always_enabled"}, new Class[]{ContentResolver.class, String.class})).intValue() == 0) {
                    ew.a("android.provider.Settings$Global", "putInt", new Object[]{contentResolver, "wifi_scan_always_enabled", 1}, new Class[]{ContentResolver.class, String.class, Integer.TYPE});
                }
            } catch (Throwable th) {
                es.a(th, "WifiManagerWrapper", "enableWifiAlwaysScan");
            }
        }
    }

    public final void a(boolean z, boolean z2, boolean z3, long j2) {
        this.k = z;
        this.l = z2;
        this.m = z3;
        if (j2 < 10000) {
            this.w = 10000;
        } else {
            this.w = j2;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0019, code lost:
        if (a(r2.getConnectionInfo()) != false) goto L_0x001b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean a(android.net.ConnectivityManager r5) {
        /*
            r4 = this;
            r0 = 1
            r1 = 0
            android.net.wifi.WifiManager r2 = r4.a
            if (r2 != 0) goto L_0x0007
        L_0x0006:
            return r1
        L_0x0007:
            android.net.NetworkInfo r3 = r5.getActiveNetworkInfo()     // Catch:{ Throwable -> 0x001d }
            int r3 = com.loc.fa.a(r3)     // Catch:{ Throwable -> 0x001d }
            if (r3 != r0) goto L_0x0027
            android.net.wifi.WifiInfo r2 = r2.getConnectionInfo()     // Catch:{ Throwable -> 0x001d }
            boolean r2 = a(r2)     // Catch:{ Throwable -> 0x001d }
            if (r2 == 0) goto L_0x0027
        L_0x001b:
            r1 = r0
            goto L_0x0006
        L_0x001d:
            r0 = move-exception
            java.lang.String r2 = "WifiManagerWrapper"
            java.lang.String r3 = "wifiAccess"
            com.loc.es.a(r0, r2, r3)
        L_0x0027:
            r0 = r1
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eg.a(android.net.ConnectivityManager):boolean");
    }

    public final String b() {
        return this.n;
    }

    public final void b(boolean z) {
        if (!z) {
            n();
        } else if (o()) {
            long c2 = fa.c();
            if (c2 - d >= 10000) {
                this.b.clear();
                g = f;
            }
            n();
            if (c2 - d >= 10000) {
                for (int i2 = 20; i2 > 0 && f == g; i2--) {
                    try {
                        Thread.sleep(150);
                    } catch (Throwable th) {
                    }
                }
            }
        }
        if (this.u) {
            this.u = false;
            d();
        }
        if (g != f) {
            List<ScanResult> list = null;
            try {
                list = l();
            } catch (Throwable th2) {
                es.a(th2, "WifiManager", "updateScanResult");
            }
            g = f;
            if (list != null) {
                this.b.clear();
                this.b.addAll(list);
            } else {
                this.b.clear();
            }
        }
        if (fa.c() - f > 20000) {
            this.b.clear();
        }
        d = fa.c();
        if (this.b.isEmpty()) {
            f = fa.c();
            List<ScanResult> l2 = l();
            if (l2 != null) {
                this.b.addAll(l2);
            }
        }
        if (this.b != null && !this.b.isEmpty()) {
            if (fa.c() - f > 3600000) {
                d();
            }
            if (this.o == null) {
                this.o = new TreeMap<>(Collections.reverseOrder());
            }
            this.o.clear();
            int size = this.b.size();
            for (int i3 = 0; i3 < size; i3++) {
                ScanResult scanResult = this.b.get(i3);
                if (fa.a(scanResult != null ? scanResult.BSSID : "") && (size <= 20 || a(scanResult.level))) {
                    if (TextUtils.isEmpty(scanResult.SSID)) {
                        scanResult.SSID = "unkwn";
                    } else if (!"<unknown ssid>".equals(scanResult.SSID)) {
                        scanResult.SSID = String.valueOf(i3);
                    }
                    this.o.put(Integer.valueOf((scanResult.level * 25) + i3), scanResult);
                }
            }
            this.b.clear();
            for (ScanResult scanResult2 : this.o.values()) {
                this.b.add(scanResult2);
            }
            this.o.clear();
        }
    }

    public final ArrayList<ScanResult> c() {
        if (this.b == null) {
            return null;
        }
        ArrayList<ScanResult> arrayList = new ArrayList<>();
        if (this.b.isEmpty()) {
            return arrayList;
        }
        arrayList.addAll(this.b);
        return arrayList;
    }

    public final void d() {
        this.v = null;
        this.b.clear();
    }

    public final void e() {
        if (this.a != null && fa.c() - f > 4900) {
            f = fa.c();
        }
    }

    public final void f() {
        int i2 = 4;
        if (this.a != null) {
            try {
                if (this.a != null) {
                    i2 = this.a.getWifiState();
                }
            } catch (Throwable th) {
                es.a(th, "Aps", "onReceive part");
            }
            if (this.b == null) {
                this.b = new ArrayList<>();
            }
            switch (i2) {
                case 0:
                case 1:
                case 4:
                    this.u = true;
                    return;
                case 2:
                case 3:
                default:
                    return;
            }
        }
    }

    public final WifiInfo g() {
        this.v = m();
        return this.v;
    }

    public final boolean h() {
        return this.i;
    }

    public final String i() {
        if (this.j == null) {
            this.j = new StringBuilder(700);
        } else {
            this.j.delete(0, this.j.length());
        }
        this.i = false;
        this.v = g();
        String bssid = a(this.v) ? this.v.getBSSID() : "";
        int size = this.b.size();
        int i2 = 0;
        boolean z = false;
        boolean z2 = false;
        while (i2 < size) {
            String str = this.b.get(i2).BSSID;
            boolean z3 = (this.l || "<unknown ssid>".equals(this.b.get(i2).SSID)) ? z2 : true;
            String str2 = "nb";
            if (bssid.equals(str)) {
                str2 = "access";
                z = true;
            }
            this.j.append(String.format(Locale.US, "#%s,%s", str, str2));
            i2++;
            z2 = z3;
        }
        if (this.b.size() == 0) {
            z2 = true;
        }
        if (!this.l && !z2) {
            this.i = true;
        }
        if (!z && !TextUtils.isEmpty(bssid)) {
            this.j.append("#").append(bssid);
            this.j.append(",access");
        }
        return this.j.toString();
    }

    public final void j() {
        d();
        this.b.clear();
    }
}
