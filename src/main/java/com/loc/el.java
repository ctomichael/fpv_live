package com.loc;

import android.content.Context;
import android.net.Proxy;
import android.os.Build;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import dji.publics.protocol.ResponseBase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/* compiled from: DnsManager */
public final class el {
    private static el c = null;
    eo a = null;
    int b = 0;
    private Object d = null;
    private Context e = null;
    private ExecutorService f = null;
    private boolean g = false;
    private boolean h = true;
    private final int i = 2;
    private String j = "";
    private String k = "";
    private String[] l = null;
    private final int m = 5;
    private final int n = 2;

    /* compiled from: DnsManager */
    class a implements Runnable {
        eo a = null;

        a(eo eoVar) {
            this.a = eoVar;
        }

        public final void run() {
            el.this.b++;
            el.this.b(this.a);
            el elVar = el.this;
            elVar.b--;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, int):int
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, java.lang.String):java.lang.String
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, int):int
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, java.lang.String):java.lang.String
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, int):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, java.lang.String):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, boolean):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0051, code lost:
        if (r4 <= r2) goto L_0x0053;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private el(android.content.Context r9) {
        /*
            r8 = this;
            r2 = 22
            r3 = 8
            r5 = 0
            r0 = 1
            r1 = 0
            r8.<init>()
            r8.d = r5
            r8.e = r5
            r8.f = r5
            r8.g = r1
            r8.h = r0
            r8.a = r5
            r4 = 2
            r8.i = r4
            java.lang.String r4 = ""
            r8.j = r4
            java.lang.String r4 = ""
            r8.k = r4
            r8.l = r5
            r8.b = r1
            r4 = 5
            r8.m = r4
            r4 = 2
            r8.n = r4
            r8.e = r9
            android.content.Context r5 = r8.e
            java.lang.Object[] r4 = new java.lang.Object[r0]
            java.lang.String r6 = "DnsManager ==> init "
            r4[r1] = r6
            com.loc.fa.a()
            java.lang.String r4 = "pref"
            java.lang.String r6 = "dnab"
            boolean r4 = com.loc.ez.b(r5, r4, r6, r0)
            if (r4 == 0) goto L_0x00a4
            int r6 = android.os.Build.VERSION.SDK_INT
            if (r5 != 0) goto L_0x0086
        L_0x004b:
            if (r6 < r3) goto L_0x00a4
            int r4 = android.os.Build.VERSION.SDK_INT
            if (r5 != 0) goto L_0x0094
        L_0x0051:
            if (r4 > r2) goto L_0x00a4
        L_0x0053:
            if (r0 == 0) goto L_0x0085
            java.lang.Object r0 = r8.d     // Catch:{ Throwable -> 0x00ff }
            if (r0 != 0) goto L_0x0085
            java.lang.String r0 = "pref"
            java.lang.String r1 = "ok6"
            r2 = 0
            int r0 = com.loc.ez.b(r5, r0, r1, r2)     // Catch:{ Throwable -> 0x00ff }
            java.lang.String r1 = "pref"
            java.lang.String r2 = "ok8"
            r6 = 0
            long r2 = com.loc.ez.b(r5, r1, r2, r6)     // Catch:{ Throwable -> 0x00ff }
            if (r0 == 0) goto L_0x00a6
            r6 = 0
            int r1 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r1 == 0) goto L_0x00a6
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00ff }
            long r2 = r6 - r2
            r6 = 259200000(0xf731400, double:1.280618154E-315)
            int r1 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r1 >= 0) goto L_0x00a6
        L_0x0085:
            return
        L_0x0086:
            java.lang.String r4 = "pref"
            java.lang.String r7 = "dnmi"
            int r4 = com.loc.ez.b(r5, r4, r7, r3)
            if (r4 <= r3) goto L_0x004b
            r3 = r4
            goto L_0x004b
        L_0x0094:
            java.lang.String r3 = "pref"
            java.lang.String r6 = "dnma"
            int r3 = com.loc.ez.b(r5, r3, r6, r2)
            r6 = 28
            if (r3 >= r6) goto L_0x0051
            r2 = r3
            goto L_0x0051
        L_0x00a4:
            r0 = r1
            goto L_0x0053
        L_0x00a6:
            java.lang.String r1 = "pref"
            java.lang.String r2 = "ok6"
            int r0 = r0 + 1
            com.loc.ez.a(r5, r1, r2, r0)     // Catch:{ Throwable -> 0x00ff }
            java.lang.String r0 = "pref"
            java.lang.String r1 = "ok8"
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00ff }
            com.loc.ez.a(r5, r0, r1, r2)     // Catch:{ Throwable -> 0x00ff }
            r0 = 1
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x00ff }
            r1 = 0
            java.lang.String r2 = "DnsManager ==> initForJar "
            r0[r1] = r2     // Catch:{ Throwable -> 0x00ff }
            com.loc.fa.a()     // Catch:{ Throwable -> 0x00ff }
            java.lang.String r0 = "com.autonavi.httpdns.HttpDnsManager"
            r1 = 1
            java.lang.Class[] r1 = new java.lang.Class[r1]     // Catch:{ Throwable -> 0x010b }
            r2 = 0
            java.lang.Class<android.content.Context> r3 = android.content.Context.class
            r1[r2] = r3     // Catch:{ Throwable -> 0x010b }
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x010b }
            r3 = 0
            r2[r3] = r5     // Catch:{ Throwable -> 0x010b }
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Throwable -> 0x010b }
            java.lang.reflect.Constructor r0 = r0.getConstructor(r1)     // Catch:{ Throwable -> 0x010b }
            java.lang.Object r0 = r0.newInstance(r2)     // Catch:{ Throwable -> 0x010b }
            r8.d = r0     // Catch:{ Throwable -> 0x010b }
        L_0x00e9:
            java.lang.String r0 = "pref"
            java.lang.String r1 = "ok6"
            r2 = 0
            com.loc.ez.a(r5, r0, r1, r2)     // Catch:{ Throwable -> 0x00ff }
            java.lang.String r0 = "pref"
            java.lang.String r1 = "ok8"
            r2 = 0
            com.loc.ez.a(r5, r0, r1, r2)     // Catch:{ Throwable -> 0x00ff }
            goto L_0x0085
        L_0x00ff:
            r0 = move-exception
            java.lang.String r1 = "APSCoManager"
            java.lang.String r2 = "init"
            com.loc.es.a(r0, r1, r2)
            goto L_0x0085
        L_0x010b:
            r0 = move-exception
            java.lang.String r1 = "DnsManager"
            java.lang.String r2 = "initForJar"
            com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x00ff }
            goto L_0x00e9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.el.<init>(android.content.Context):void");
    }

    public static el a(Context context) {
        if (c == null) {
            c = new el(context);
        }
        return c;
    }

    private String a(String str) {
        String str2;
        int i2;
        if (e()) {
            try {
                String[] strArr = (String[]) ew.a(this.d, "getIpsByHostAsync", str);
                if (strArr == null || strArr.length <= 0) {
                    str2 = null;
                } else {
                    if (this.l == null) {
                        this.l = strArr;
                        str2 = strArr[0];
                        i2 = 1;
                    } else if (a(strArr, this.l)) {
                        str2 = this.l[0];
                        i2 = 1;
                    } else {
                        this.l = strArr;
                        str2 = strArr[0];
                    }
                    ey.a(this.e, "HttpDns", i2);
                }
                i2 = 1;
            } catch (Throwable th) {
                i2 = 0;
                str2 = null;
            }
            ey.a(this.e, "HttpDns", i2);
        } else {
            str2 = null;
        }
        new Object[1][0] = "DnsManager ==> getIpAsync  host ： " + str + " ， ip ： " + str2;
        fa.a();
        return str2;
    }

    private static boolean a(String[] strArr, String[] strArr2) {
        if (strArr != null && strArr2 == null) {
            return false;
        }
        if (strArr == null && strArr2 != null) {
            return false;
        }
        if (strArr == null && strArr2 == null) {
            return true;
        }
        try {
            if (strArr.length != strArr2.length) {
                return false;
            }
            ArrayList arrayList = new ArrayList(12);
            ArrayList arrayList2 = new ArrayList(12);
            arrayList.addAll(Arrays.asList(strArr));
            arrayList2.addAll(Arrays.asList(strArr2));
            Collections.sort(arrayList);
            Collections.sort(arrayList2);
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (!((String) arrayList.get(i2)).equals(arrayList2.get(i2))) {
                    return false;
                }
            }
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public static void d() {
        c = null;
    }

    private boolean e() {
        return this.d != null && !f() && ez.b(this.e, "pref", "dns_faile_count_total", 0) < 2;
    }

    private boolean f() {
        int i2;
        String str = null;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                str = System.getProperty("http.proxyHost");
                String property = System.getProperty("http.proxyPort");
                if (property == null) {
                    property = AmapLoc.RESULT_TYPE_AMAP_INDOOR;
                }
                i2 = Integer.parseInt(property);
            } else {
                str = Proxy.getHost(this.e);
                i2 = Proxy.getPort(this.e);
            }
        } catch (Throwable th) {
            th.printStackTrace();
            i2 = -1;
        }
        return (str == null || i2 == -1) ? false : true;
    }

    public final void a() {
        if (TextUtils.isEmpty(this.k)) {
            return;
        }
        if (TextUtils.isEmpty(this.j) || !this.k.equals(this.j)) {
            this.j = this.k;
            ez.a(this.e, ResponseBase.STRING_IP, "last_ip", this.k);
        }
    }

    public final void a(eo eoVar) {
        try {
            this.g = false;
            if (e() && eoVar != null) {
                this.a = eoVar;
                String c2 = eoVar.c();
                String host = new URL(c2).getHost();
                if (!"http://abroad.apilocate.amap.com/mobile/binary".equals(c2) && !"abroad.apilocate.amap.com".equals(host)) {
                    String str = "apilocate.amap.com".equalsIgnoreCase(host) ? "httpdns.apilocate.amap.com" : host;
                    String a2 = a(str);
                    if (this.h && TextUtils.isEmpty(a2)) {
                        this.h = false;
                        a2 = ez.b(this.e, ResponseBase.STRING_IP, "last_ip", "");
                        if (!TextUtils.isEmpty(a2)) {
                            this.j = a2;
                        }
                    }
                    if (!TextUtils.isEmpty(a2)) {
                        this.k = a2;
                        eoVar.g = c2.replace(host, a2);
                        eoVar.b().put("host", str);
                        eoVar.a(str);
                        this.g = true;
                    }
                }
            }
        } catch (Throwable th) {
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, int):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, java.lang.String):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, boolean):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void */
    public final void b() {
        if (this.g) {
            ez.a(this.e, "pref", "dns_faile_count_total", 0L);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, int):int
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, java.lang.String):java.lang.String
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, int):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, java.lang.String):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, boolean):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void */
    /* access modifiers changed from: package-private */
    public final synchronized void b(eo eoVar) {
        try {
            eoVar.g = es.a();
            long b2 = ez.b(this.e, "pref", "dns_faile_count_total", 0L);
            if (b2 < 2) {
                bg.a();
                bg.a(eoVar, false);
                ez.a(this.e, "pref", "dns_faile_count_total", b2 + 1);
            }
        } catch (Throwable th) {
            ez.a(this.e, "pref", "dns_faile_count_total", 0L);
        }
        return;
    }

    public final void c() {
        String[] strArr;
        try {
            if (e()) {
                if (!(!this.g || this.l == null || (strArr = this.l) == null)) {
                    try {
                        if (strArr.length > 1) {
                            ArrayList arrayList = new ArrayList(12);
                            arrayList.addAll(Arrays.asList(strArr));
                            Iterator it2 = arrayList.iterator();
                            it2.remove();
                            arrayList.add((String) it2.next());
                            arrayList.toArray(strArr);
                        }
                    } catch (Throwable th) {
                    }
                }
                if (this.b <= 5 && this.g) {
                    if (this.f == null) {
                        this.f = aq.d();
                    }
                    if (!this.f.isShutdown()) {
                        this.f.submit(new a(this.a));
                    }
                }
            }
        } catch (Throwable th2) {
        }
    }
}
